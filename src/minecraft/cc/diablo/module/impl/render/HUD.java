package cc.diablo.module.impl.render;

import cc.diablo.Main;
import cc.diablo.event.impl.ServerJoinEvent;
import cc.diablo.font.TTFFontRenderer;
import cc.diablo.helpers.MathHelper;
import cc.diablo.helpers.Stopwatch;
import cc.diablo.helpers.render.ChatColor;
import cc.diablo.helpers.render.ChatHelper;
import cc.diablo.helpers.render.ColorHelper;
import cc.diablo.helpers.render.RenderUtils;
import cc.diablo.manager.module.ModuleManager;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.module.impl.exploit.Disabler;
import cc.diablo.module.impl.player.Stealer;
import cc.diablo.setting.impl.BooleanSetting;
import cc.diablo.setting.impl.ModeSetting;
import cc.diablo.setting.impl.NumberSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Timer;
import org.lwjgl.Sys;

import javax.annotation.Resource;
import java.awt.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HUD extends Module {
    public static ModeSetting arraylistMode = new ModeSetting("Arraylist","Boxed","Simple","Boxed","Boxed2","Infamous","Diablo","Diablo2","Astolfo");
    public static ModeSetting colorMode = new ModeSetting("Color Mode","Astolfo","Solid","Rainbow","Rainbow Light","Wave","Astolfo");
    public static ModeSetting watermarkMode = new ModeSetting("Watermark Mode","Simple","Simple","Diablo","Logo");
    public static BooleanSetting minecraftFont = new BooleanSetting("Minecraft Font", true);
    public static BooleanSetting hideVisual = new BooleanSetting("Hide Visuals", false);
    public static BooleanSetting sessionTime = new BooleanSetting("Session Time", true);
    public static BooleanSetting customHotbar = new BooleanSetting("Custom Hotbar", true);
    public static BooleanSetting date = new BooleanSetting("Date", true);
    public static NumberSetting delay = new NumberSetting("Delay",3000,100,10000,50);
    public static NumberSetting red = new NumberSetting("Red",45,1,255,1);
    public static NumberSetting green = new NumberSetting("Green",24,1,255,1);
    public static NumberSetting blue = new NumberSetting("Blue",158,1,255,1);
    public static NumberSetting offset = new NumberSetting("Offset",0,0,6,1);

    public static Stopwatch watch = new Stopwatch();
    public HUD() {
        super("HUD", "Displays the Client's Hud!", 0, Category.Render);
        this.addSettings(arraylistMode,colorMode,watermarkMode, minecraftFont,hideVisual,sessionTime,customHotbar,date,delay,red,green,blue,offset);
        toggled = true;
    }

    public void drawHud(ScaledResolution sr) {
        TTFFontRenderer fr = Main.getInstance().getSFUI(20);
        TTFFontRenderer frSmall = Main.getInstance().getSFUI(20);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDateTime now = LocalDateTime.now();

        String bps = "BPS\247s: " + String.format("%.2f", Math.hypot(mc.thePlayer.posX - mc.thePlayer.prevPosX, mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * (double) Timer.timerSpeed * 20.0D);
        String coords = "XYZ\247s: " + mc.thePlayer.getPosition().getX() + "," + mc.thePlayer.getPosition().getY() + "," + mc.thePlayer.getPosition().getZ();
        String userInfo = ChatColor.DARK_GRAY + Main.buildType + ChatColor.WHITE + " (" + ChatColor.WHITE + Main.version + ChatColor.WHITE + ") - " + dtf.format(now);

        int count = 0;
        int lastX = -1;
        double yOff = 0;
        double xOff = 0;
        ArrayList<Module> toggledModule = new ArrayList<>();
        Module lastModule = null;
        Module firstModule = null;
        try {
            switch (watermarkMode.getMode()) {
                case "Diablo":
                    String text = Main.customName.substring(0, 1).toLowerCase(Locale.ROOT) + "\2477" + ChatColor.WHITE + Main.customName.substring(1, Main.customName.length()).toLowerCase(Locale.ROOT) + "sense | " + Minecraft.getMinecraft().getCurrentServerData().serverIP + " | FPS:" + Minecraft.getDebugFPS();

                    TTFFontRenderer fr3 = Main.getInstance().getSFUI(16);

                    double x = 3;
                    double y = 5;
                    double width = fr3.getWidth(text) + 3;
                    double height = 12;

                    RenderUtils.drawRect(x, y - 1, x + width, y, ColorHelper.getColor(150));
                    RenderUtils.drawRect(x, y, x + width, y + height, RenderUtils.transparency(new Color(18, 18, 18).getRGB(), 0.6f));

                    fr3.drawStringWithShadow(text, (float) x + 2, (float) y + 2, ColorHelper.getColor(150));
                    break;
                case "Simple":
                    //fr.drawStringWithShadow(Main.customName.charAt(0) + "\2477" + Main.customName.substring(1) +" "+ Main.version, 2.5f, 2.5f, ColorHelper.getColor(0));
                    drawString(Main.customName.charAt(0) + "\2477" + Main.customName.substring(1) + " " + Main.version, 3.5f, 3.5f, ColorHelper.getColor(0));
                    break;
                case "Logo":
                    RenderUtils.drawImage(3,3,32,32, new ResourceLocation("Client\\images\\logo.png"));
                    break;
            }
        } catch (Exception e){}

        ModuleManager.getModules().sort(Comparator.comparing(module -> getFontWidth(((Module) module).getDisplayName())).reversed());

        switch (arraylistMode.getMode()) {
            case "Boxed":
                for (Module module : ModuleManager.getModules()) {
                    if (module.isToggled() && shouldShow(module)) {
                        yOff = offset.getVal() / 2;
                        int color = ColorHelper.getColor(count * 150);

                        toggledModule.add(module);
                        RenderUtils.drawRect(sr.getScaledWidth() - getFontWidth(module.getDisplayName()) - 1 - offset.getVal(), count * (getFontHeight(module.getDisplayName())) + (offset.getVal() / 2) - yOff, sr.getScaledWidth(), count * (getFontHeight(module.getDisplayName())) + getFontHeight(module.getDisplayName()) + (offset.getVal() / 2) - yOff, RenderUtils.transparency(new Color(18, 18, 18).getRGB(), 0.6f));
                        RenderUtils.drawRect(sr.getScaledWidth() - getFontWidth(module.getDisplayName()) - 2 - offset.getVal(), count * (getFontHeight(module.getDisplayName())) + (offset.getVal() / 2) - yOff, sr.getScaledWidth() - getFontWidth(module.getDisplayName()) - 1 - offset.getVal(), count * (getFontHeight(module.getDisplayName())) + getFontHeight(module.getDisplayName()) + (offset.getVal() / 2) - yOff, color);
                        if (lastX != -1) {
                            RenderUtils.drawRect(lastX - 1 - offset.getVal(), count * (getFontHeight(module.getDisplayName())) + (offset.getVal() / 2) - 1 - yOff, sr.getScaledWidth() - getFontWidth(module.getDisplayName()) - 1 - offset.getVal(), count * (getFontHeight(module.getDisplayName())) + (offset.getVal() / 2) - yOff, color);
                        }
                        drawString(module.getDisplayName(), (float) (sr.getScaledWidth() - getFontWidth(module.getDisplayName()) - (offset.getVal() / 2)), (float) ((float) (count * (getFontHeight(module.getDisplayName())) + (offset.getVal() * 0.5)) - yOff), color);
                        lastX = (int) ((int) (sr.getScaledWidth() - getFontWidth(module.getDisplayName())));
                        lastModule = module;
                        count++;
                    }
                }

                if (lastModule != null) {
                    RenderUtils.drawRect(sr.getScaledWidth() - getFontWidth(lastModule.getDisplayName()) - 2 - offset.getVal(), count * (getFontHeight(lastModule.getDisplayName())) + (offset.getVal() / 2) - yOff, sr.getScaledWidth(), count * (getFontHeight(lastModule.getDisplayName())) + (offset.getVal() / 2) + 1 - yOff, ColorHelper.getColor(count * 150));
                }
                //count -= (int) offset.getVal() / 6;
                break;
            case "Boxed2":
                for (Module module : ModuleManager.getModules()) {
                    if (module.isToggled() && shouldShow(module)) {
                        yOff = offset.getVal() / 2;
                        int color = ColorHelper.getColor(count * 150);

                        toggledModule.add(module);
                        RenderUtils.drawRect(sr.getScaledWidth() - getFontWidth(module.getDisplayName()) - 1 - offset.getVal(), count * (getFontHeight(module.getDisplayName())) + (offset.getVal() / 2) - yOff, sr.getScaledWidth(), count * (getFontHeight(module.getDisplayName())) + getFontHeight(module.getDisplayName()) + (offset.getVal() / 2) - yOff, RenderUtils.transparency(new Color(229, 229, 229).getRGB(), 0.125f));
                        RenderUtils.drawRect(sr.getScaledWidth() - getFontWidth(module.getDisplayName()) - 2 - offset.getVal(), count * (getFontHeight(module.getDisplayName())) + (offset.getVal() / 2) - yOff, sr.getScaledWidth() - getFontWidth(module.getDisplayName()) - 1 - offset.getVal(), count * (getFontHeight(module.getDisplayName())) + getFontHeight(module.getDisplayName()) + (offset.getVal() / 2) - yOff, color);
                        if (lastX != -1) {
                            //RenderUtils.drawRect(lastX - 1 - offset.getVal(), count * (getFontHeight(module.getDisplayName())) + (offset.getVal() / 2) - 1 - yOff, sr.getScaledWidth() - getFontWidth(module.getDisplayName()) - 1 - offset.getVal(), count * (getFontHeight(module.getDisplayName())) + (offset.getVal() / 2) - yOff, color);

                        }
                        drawString(module.getDisplayName(), (float) (sr.getScaledWidth() - getFontWidth(module.getDisplayName()) - (offset.getVal() / 2)), (float) ((float) (count * (getFontHeight(module.getDisplayName())) + (offset.getVal() * 0.5)) - yOff), color);
                        lastX = (int) ((int) (sr.getScaledWidth() - getFontWidth(module.getDisplayName())));
                        lastModule = module;
                        count++;
                    }
                }

                if (lastModule != null) {
                    RenderUtils.drawRect(sr.getScaledWidth() - getFontWidth(lastModule.getDisplayName()) - 2 - offset.getVal(), count * (getFontHeight(lastModule.getDisplayName())) + (offset.getVal() / 2) - yOff, sr.getScaledWidth(), count * (getFontHeight(lastModule.getDisplayName())) + (offset.getVal() / 2) + 1 - yOff, ColorHelper.getColor(count * 150));
                }
                //count -= (int) offset.getVal() / 6;
                break;
            case "Diablo":
                for (Module module : ModuleManager.getModules()) {
                    if (module.isToggled() && shouldShow(module)) {
                        if (firstModule == null) {
                            firstModule = module;
                        }
                        int color = ColorHelper.getColor(count * 150);

                        yOff = -6;
                        xOff = 5;
                        toggledModule.add(module);
                        //RenderUtils.drawRect(sr.getScaledWidth() - getFontWidth(module.getDisplayName()) - 4 - offset.getVal(), count * (getFontHeight(module.getDisplayName())) + (offset.getVal() / 2) - yOff, sr.getScaledWidth(), count * (getFontHeight(module.getDisplayName())) + getFontHeight(module.getDisplayName()) + (offset.getVal() / 2) - yOff, RenderUtils.transparency(new Color(37, 37, 37).getRGB(), 0.4f));
                        RenderUtils.drawRect(sr.getScaledWidth() - getFontWidth(module.getDisplayName()) - 1 - offset.getVal() - xOff, count * (getFontHeight(module.getDisplayName())) + (offset.getVal() / 2) - yOff, sr.getScaledWidth() - xOff, count * (getFontHeight(module.getDisplayName())) + getFontHeight(module.getDisplayName()) + (offset.getVal() / 2) - yOff, RenderUtils.transparency(new Color(57, 57, 57).getRGB(), 0.6f));
                        //RenderUtils.drawRect(sr.getScaledWidth() - getFontWidth(module.getDisplayName()) - 1.7 - offset.getVal()- xOff, count * (getFontHeight(module.getDisplayName())) + (offset.getVal() / 2) - yOff, sr.getScaledWidth() - getFontWidth(module.getDisplayName()) - 1 - offset.getVal() - xOff, count * (getFontHeight(module.getDisplayName())) + getFontHeight(module.getDisplayName()) + (offset.getVal() / 2) - yOff, color);

                        if (lastX != -1) {
                            //RenderUtils.drawRect(lastX - 1 - offset.getVal(), count * (getFontHeight(module.getDisplayName())) + (offset.getVal() / 2) - 1 - yOff, sr.getScaledWidth() - getFontWidth(module.getDisplayName()) - 1 - offset.getVal(), count * (getFontHeight(module.getDisplayName())) + (offset.getVal() / 2) - yOff, color);
                        }
                        drawString(module.getDisplayName(), (float) ((sr.getScaledWidth() - getFontWidth(module.getDisplayName()) - (offset.getVal() / 2)) - xOff), (float) ((float) (count * (getFontHeight(module.getDisplayName())) + (offset.getVal() * 0.5)) - yOff), color);
                        lastX = (int) ((int) (sr.getScaledWidth() - getFontWidth(module.getDisplayName())));
                        lastModule = module;
                        count++;
                    }
                }

                if (firstModule != null) {
                    RenderUtils.drawRect(sr.getScaledWidth() - getFontWidth(firstModule.getDisplayName()) - 1 - offset.getVal() - xOff, (offset.getVal() / 2) - yOff - 2, sr.getScaledWidth() - xOff, (offset.getVal() / 2) - yOff, RenderUtils.transparency(new Color(57, 57, 57).getRGB(), 0.6f));

                    RenderUtils.drawRect(sr.getScaledWidth() - getFontWidth(firstModule.getDisplayName()) - 1 - offset.getVal() - xOff, (offset.getVal() / 2) - yOff - 2, sr.getScaledWidth() - xOff, (offset.getVal() / 2) - yOff - 1.5, ColorHelper.getColor(150));
                }
                break;
            case "Diablo2":
                for (Module module : ModuleManager.getModules()) {
                    if (module.isToggled() && shouldShow(module)) {
                        if (firstModule == null) {
                            firstModule = module;
                        }
                        int color = ColorHelper.getColor(count * 150);

                        yOff = -6;
                        xOff = 5;
                        toggledModule.add(module);
                        //RenderUtils.drawRect(sr.getScaledWidth() - getFontWidth(module.getDisplayName()) - 4 - offset.getVal(), count * (getFontHeight(module.getDisplayName())) + (offset.getVal() / 2) - yOff, sr.getScaledWidth(), count * (getFontHeight(module.getDisplayName())) + getFontHeight(module.getDisplayName()) + (offset.getVal() / 2) - yOff, RenderUtils.transparency(new Color(37, 37, 37).getRGB(), 0.4f));
                        RenderUtils.drawRect(sr.getScaledWidth() - getFontWidth(module.getDisplayName()) - 1 - offset.getVal() - xOff, count * (getFontHeight(module.getDisplayName())) + (offset.getVal() / 2) - yOff, sr.getScaledWidth() - xOff, count * (getFontHeight(module.getDisplayName())) + getFontHeight(module.getDisplayName()) + (offset.getVal() / 2) - yOff, RenderUtils.transparency(new Color(57, 57, 57).getRGB(), 0.6f));
                        RenderUtils.drawRect(sr.getScaledWidth() - getFontWidth(module.getDisplayName()) - 1.7 - offset.getVal() - xOff, count * (getFontHeight(module.getDisplayName())) + (offset.getVal() / 2) - yOff, sr.getScaledWidth() - getFontWidth(module.getDisplayName()) - 1 - offset.getVal() - xOff, count * (getFontHeight(module.getDisplayName())) + getFontHeight(module.getDisplayName()) + (offset.getVal() / 2) - yOff, color);

                        if (lastX != -1) {
                            //RenderUtils.drawRect(lastX - 1 - offset.getVal(), count * (getFontHeight(module.getDisplayName())) + (offset.getVal() / 2) - 1 - yOff, sr.getScaledWidth() - getFontWidth(module.getDisplayName()) - 1 - offset.getVal(), count * (getFontHeight(module.getDisplayName())) + (offset.getVal() / 2) - yOff, color);
                        }
                        drawString(module.getDisplayName(), (float) ((sr.getScaledWidth() - getFontWidth(module.getDisplayName()) - (offset.getVal() / 2)) - xOff), (float) ((float) (count * (getFontHeight(module.getDisplayName())) + (offset.getVal() * 0.5)) - yOff), color);
                        lastX = (int) ((int) (sr.getScaledWidth() - getFontWidth(module.getDisplayName())));
                        lastModule = module;
                        count++;
                    }
                }

                if (firstModule != null) {
                    RenderUtils.drawRect(sr.getScaledWidth() - getFontWidth(firstModule.getDisplayName()) - 1.5 - offset.getVal() - xOff, (offset.getVal() / 2) - yOff - 2, sr.getScaledWidth() - xOff, (offset.getVal() / 2) - yOff, RenderUtils.transparency(new Color(57, 57, 57).getRGB(), 0.6f));

                    RenderUtils.drawRect(sr.getScaledWidth() - getFontWidth(firstModule.getDisplayName()) - 1.5 - offset.getVal() - xOff, (offset.getVal() / 2) - yOff - 2, sr.getScaledWidth() - xOff, (offset.getVal() / 2) - yOff - 1.5, ColorHelper.getColor(150));
                }
                break;
            case "Simple":
                for (Module module : ModuleManager.getModules()) {
                    if (module.isToggled() && shouldShow(module)) {
                        toggledModule.add(module);
                        int color = ColorHelper.getColor(count * 150);

                        drawString(module.getDisplayName(), sr.getScaledWidth() - getFontWidth(module.getDisplayName()) - 3, count * (getFontHeight(module.getDisplayName())) + 3, color);
                        count++;
                    }
                }
                break;
            case "Infamous":
                minecraftFont.setChecked(true);
                int y2 = (int) offset.getVal();
                for (Module m : ModuleManager.getModules()) {
                    if (m.isToggled() && shouldShow(m)) {
                        mc.fontRendererObj.drawStringWithShadow(m.getDisplayName(), sr.getScaledWidth() - mc.fontRendererObj.getStringWidth(m.getDisplayName()), y2, ColorHelper.getColor(0));
                        y2 += 14;
                    }
                }
                break;
            case "Astolfo":
                double offBlackBox = minecraftFont.isChecked() ? 4 : 3.5;

                RenderUtils.drawRect(sr.getScaledWidth() - offBlackBox - getFontWidth(Objects.requireNonNull(ModuleManager.getModules()).get(0).getDisplayName()) - offset.getVal(), offset.getVal() - 1, sr.getScaledWidth() - offset.getVal(), offset.getVal(), ColorHelper.getColor(150));

                for (Module module : ModuleManager.getModules()) {
                    if (module.isToggled() && shouldShow(module)) {
                        yOff = offset.getVal() / 2;
                        int color = ColorHelper.getColor(count * 150);

                        toggledModule.add(module);
                        RenderUtils.drawRect(sr.getScaledWidth() - getFontWidth(module.getDisplayName()) - offBlackBox - offset.getVal(), count * (getFontHeight(module.getDisplayName())) + (offset.getVal() / 2) - yOff + offset.getVal(), sr.getScaledWidth() - offset.getVal(), count * (getFontHeight(module.getDisplayName())) + getFontHeight(module.getDisplayName()) + (offset.getVal() / 2) - yOff + offset.getVal(), RenderUtils.transparency(new Color(18, 18, 18).getRGB(), 0.6f));
                        RenderUtils.drawRect(sr.getScaledWidth() - 0.75 - offset.getVal(), count * (getFontHeight(module.getDisplayName())) + (offset.getVal() / 2) - yOff + offset.getVal(), sr.getScaledWidth() - offset.getVal(), count * (getFontHeight(module.getDisplayName())) + getFontHeight(module.getDisplayName()) + (offset.getVal() / 2) - yOff + offset.getVal(), color);

                        drawString(module.getDisplayName(), (float) (sr.getScaledWidth() - getFontWidth(module.getDisplayName()) - offset.getVal() - 2), (float) (count * (getFontHeight(module.getDisplayName())) + (offset.getVal() * 0.5) - yOff + 0.5f + offset.getVal()), color);
                        lastX = (int) ((int) (sr.getScaledWidth() - getFontWidth(module.getDisplayName())));
                        lastModule = module;
                        count++;
                    }
                }
                break;
        }

        if (date.isChecked())
            drawString(userInfo, sr.getScaledWidth() - getFontWidth(userInfo) - 1, sr.getScaledHeight() - getFontHeight(userInfo) - 1, ColorHelper.getColor(0));

        if(!(mc.currentScreen == Main.getInstance().getClickGUI())) {
            if (sessionTime.isChecked()) {
                String text = GetElapsed(Main.timestamp, watch.getCurrentMS(), true);
                mc.fontRendererObj.drawStringWithShadow(text, sr.getScaledWidth() / 2 - (mc.fontRendererObj.getStringWidth(text) / 2), sr.getScaledHeight() / 7, -1);
            }

            if (Stealer.isStealing)
                drawString("Stealing... ", (sr.getScaledWidth() / 2) - (getFontWidth("Stealing... ") / 2), sr.getScaledHeight() / 2 + getFontHeight("Stealing") + 3, -1);

            if (!Disabler.joinWatch.hasReached((long) delay.getVal())) {
                mc.fontRendererObj.drawStringWithShadow("Disabling watchdog please wait... ", (sr.getScaledWidth() / 2) - (getFontWidth("Disabling watchdog please wait... ") / 2), ((sr.getScaledHeight() / 6) * 4.5f) + getFontHeight("Stealing") + 3, -1);
            } else if (!Disabler.joinWatch.hasReached((long) delay.getVal() + 7000)) {
                mc.fontRendererObj.drawStringWithShadow("You may flag during this period of time", (sr.getScaledWidth() / 2) - (getFontWidth("You may flag during this period of time") / 2), ((sr.getScaledHeight() / 6) * 4.5f) + getFontHeight("Stealing") + 3, -1);
            }
        }

        drawString(bps, 1, sr.getScaledHeight() - getFontHeight(bps) - getFontHeight(coords), ColorHelper.getColor(0));
        drawString(coords, 1, sr.getScaledHeight() - getFontHeight(coords), ColorHelper.getColor(0));

        if(ModuleManager.getModule(Crosshair.class).isToggled()){
            int colorCrosshair = new Color((int) Crosshair.red.getVal(),(int) Crosshair.green.getVal(),(int) Crosshair.blue.getVal()).getRGB();
            //RenderUtils.drawRoundedRectangle((float) (sr.getScaledWidth() / 2  - Crosshair.length.getVal()),(float) (sr.getScaledHeight() / 2 - 0.5),(float) sr.getScaledWidth() / 2 - 2,(float) (sr.getScaledHeight() / 2 + 0.5),(int) Crosshair.thicknes.getVal(),new Color(17,17,17).getRGB(),colorCrosshair);
        }
    }

    private boolean shouldShow(Module module) {
        if(hideVisual.isChecked()){
            return module.category != Category.Render;
        }
        return true;
    }

    @Override
    public void onEnable() {
        Main.timestamp = watch.getCurrentMS();
        super.onEnable();
    }

    public void onDisable() {
        this.toggle();
        super.onDisable();
    }

    @Subscribe
    public void onServerJoin(ServerJoinEvent e){
        this.setToggled(false);
        this.setToggled(true);
        watch.reset();
        Main.timestamp = watch.getCurrentMS();
    }

    public float getFontWidth(String text){
        TTFFontRenderer fr = Main.getInstance().getSFUI(20);
        return minecraftFont.isChecked() ? mc.fontRendererObj.getStringWidth(text) : fr.getWidth(text);
    }

    public float getFontHeight(String text){
        TTFFontRenderer fr = Main.getInstance().getSFUI(20);
        return minecraftFont.isChecked() ? mc.fontRendererObj.FONT_HEIGHT : fr.getHeight(text);
    }

    public void drawString(String text, float x, float y,int color){
        TTFFontRenderer fr = Main.getInstance().getSFUI(20);
        if(minecraftFont.isChecked()){
            mc.fontRendererObj.drawStringWithShadow(text,x,y,color);
        } else {
            fr.drawStringWithShadow(text,x,y,color);
        }
    }

    public static String GetElapsed(long aInitialTime, long aEndTime, boolean aIncludeMillis)
    {
        StringBuffer elapsed = new StringBuffer();

        Map<String, Long> units = new HashMap<String, Long>();

        long milliseconds = aEndTime - aInitialTime;

        long seconds = milliseconds / 1000;
        long minutes = milliseconds / (60 * 1000);
        long hours = milliseconds / (60 * 60 * 1000);
        long days = milliseconds / (24 * 60 * 60 * 1000);

        units.put("milliseconds", milliseconds);
        units.put("seconds", seconds);
        units.put("minutes", minutes);
        units.put("hours", hours);
        units.put("days", days);

        if (days > 0)
        {
            long leftoverHours = hours % 24;
            units.put("hours", leftoverHours);
        }

        if (hours > 0)
        {
            long leftoeverMinutes = minutes % 60;
            units.put("minutes", leftoeverMinutes);
        }

        if (minutes > 0)
        {
            long leftoverSeconds = seconds % 60;
            units.put("seconds", leftoverSeconds);
        }

        if (seconds > 0)
        {
            long leftoverMilliseconds = milliseconds % 1000;
            units.put("milliseconds", leftoverMilliseconds);
        }

        if(units.get("days") != 0){
            elapsed.append(units.get("days")).append("d ");
        }

        if(units.get("hours") != 0){
            elapsed.append(units.get("hours")).append("h ");
        }

        if(units.get("minutes") != 0){
            elapsed.append(units.get("minutes")).append("m ");
        }

        elapsed.append(units.get("seconds")).append("s ");

        return elapsed.toString();

    }

    private static String PrependZeroIfNeeded(long aValue)
    {
        return aValue < 10 ? "0" + aValue : Long.toString(aValue);
    }
}
