package cc.diablo.clickgui;

import cc.diablo.Main;
import cc.diablo.font.TTFFontRenderer;
import cc.diablo.font.TTFRenderer;
import cc.diablo.helpers.render.ChatHelper;
import cc.diablo.helpers.render.ColorHelper;
import cc.diablo.helpers.render.RenderUtils;
import cc.diablo.manager.module.ModuleManager;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.setting.Setting;
import cc.diablo.setting.impl.BooleanSetting;
import cc.diablo.setting.impl.ModeSetting;
import cc.diablo.setting.impl.NumberSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class ClickGUI extends GuiScreen {

    TTFFontRenderer fr = Main.getInstance().getSFUI(20);
    double x, y, x2, y2, width, height;
    public Category selectedCategory = null;
    public Module module1;
    public int catagoryCount, btnOffset;
    TTFRenderer fontTitle = new TTFRenderer("Comfortaa", 0, 20);
    TTFFontRenderer moduleTitle = Main.getInstance().getFontManager().getFont("clean 18");
    TTFFontRenderer moduleSubtitle = Main.getInstance().getFontManager().getFont("clean 16");
    public String activeDesc = "";
    public MouseEvent mouseEvt;
    public ArrayList<Module> combatModules = new ArrayList<Module>();
    public ArrayList<Module> movementModules = new ArrayList<Module>();
    public ArrayList<Module> playerModules = new ArrayList<Module>();
    public ArrayList<Module> renderModules = new ArrayList<Module>();
    public ArrayList<Module> exploitModules = new ArrayList<Module>();
    public ArrayList<Module> miscModules = new ArrayList<Module>();
    public ArrayList<Module> ghostModules = new ArrayList<Module>();
    public ArrayList<Module> expandedModules = new ArrayList<Module>();
    public boolean combatExpanded, movementExpanded, playerExpanded, renderExpanded, exploitExpanded, miscExpanded, ghostExpanded = false;
    public int yHeight = 29;
    public boolean dragging = false;

    @Override
    public void initGui() {
        combatModules.clear();
        movementModules.clear();
        playerModules.clear();
        renderModules.clear();
        exploitModules.clear();
        miscModules.clear();
        ghostModules.clear();
        module1 = null;
        btnOffset = 20;
        catagoryCount = 0;
        x = sr.getScaledWidth_double();
        y = sr.getScaledHeight_double();
        width = 100;
        height = 180;
        x2 = x;
        y2 = y;
        for(Module module : ModuleManager.getModules()) {
            if (module.getName() != "ClickGUI"){
                if (module.getCategory() == Category.Combat) {
                    combatModules.add(module);
                } else if (module.getCategory() == Category.Movement) {
                    movementModules.add(module);
                } else if (module.getCategory() == Category.Player) {
                    playerModules.add(module);
                } else if (module.getCategory() == Category.Render) {
                    renderModules.add(module);
                } else if (module.getCategory() == Category.Exploit) {
                    exploitModules.add(module);
                } else if (module.getCategory() == Category.Misc) {
                    miscModules.add(module);
                } else if (module.getCategory() == Category.Ghost) {
                    ghostModules.add(module);
                }
            }
        }

        super.initGui();
    }

    public TTFFontRenderer getFont(String font, double size) {
        return Main.getInstance().getFontManager().getFont(font + " " + size);
    }


    @Subscribe
    public void onClick(MouseEvent e){
        mouseEvt = e;
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        try{
            MovementInput.moveForward = 1;
            if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) mc.thePlayer.rotationPitch += 2f;
            if (Keyboard.isKeyDown(Keyboard.KEY_UP)) mc.thePlayer.rotationPitch -= 2f;
            if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) mc.thePlayer.rotationYaw += 2f;
            if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) mc.thePlayer.rotationYaw -= 2f;
            KeyBinding[] keys = {mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindRight,mc.gameSettings.keyBindJump};
            Arrays.stream(keys).forEach(key -> KeyBinding.setKeyBindState(key.getKeyCode(), Keyboard.isKeyDown(key.getKeyCode())));
        } catch (Exception e){

        }
        GL11.glPushMatrix();
        combatModules.sort(Comparator.comparing(module -> moduleSubtitle.getWidth(((Module)module).getName())).reversed());
        movementModules.sort(Comparator.comparing(module -> moduleSubtitle.getWidth(((Module)module).getName())).reversed());
        playerModules.sort(Comparator.comparing(module -> moduleSubtitle.getWidth(((Module)module).getName())).reversed());
        renderModules.sort(Comparator.comparing(module -> moduleSubtitle.getWidth(((Module)module).getName())).reversed());
        miscModules.sort(Comparator.comparing(module -> moduleSubtitle.getWidth(((Module)module).getName())).reversed());
        ghostModules.sort(Comparator.comparing(module -> moduleSubtitle.getWidth(((Module)module).getName())).reversed());
        //Combat
        int cX = 5;
        int cY = 29;
        int cW = 100;
        int cOff = cY + 1;
        int cH = combatExpanded ? (17 * combatModules.size()) : 0;
        int cH2 = 0;

        for(Module m : combatModules){
            if(expandedModules.contains(m)){
                for (Setting s : m.getSettingList()){
                    if(s instanceof ModeSetting) {
                        cH2 += 28;
                        if (((ModeSetting) s).getExpanded()) {
                            for (String modes : ((ModeSetting) s).getSettings()) {
                                if (modes != ((ModeSetting) s).getMode()) {
                                    cH2 += 12;
                                }
                            }
                        }
                        cH2 += 2;
                    }

                    if(s instanceof NumberSetting){
                        cH2 += 17;
                    }

                    if(s instanceof BooleanSetting){
                        cH2 += 17;
                    }
                }
            }
        }
        if(combatExpanded) cH += cH2;

        RenderUtils.drawRect(cX-1,cY-18,cX+cW+1,cY+cH+1,RenderUtils.transparency(new Color(37,37,37).getRGB(),0.6f));
        RenderUtils.drawRect(cX,cY -15,cX+cW,cY,RenderUtils.transparency(new Color(29, 29, 29, 255).getRGB(),0.8f));
        Main.getInstance().getFontManager().getFont("guicons 24").drawStringWithShadow("c", cX+1, cY - 11, -1);
        Main.getInstance().getFontManager().getFont("clean").drawStringWithShadow("Combat",cX + Main.getInstance().getFontManager().getFont("guicons 24").getWidth("c") + 1,cY - 12,-1);
        Main.getInstance().getFontManager().getFont("clean").drawStringWithShadow(combatExpanded? "-" : "+",cX + cW - Main.getInstance().getFontManager().getFont("clean").getWidth(combatExpanded? "-" : "+") - 1,cY - 12,-1);
        RenderUtils.drawRect(cX,cY,cX+cW,cY+cH,RenderUtils.transparency(new Color(45, 45, 45, 255).getRGB(),0.8f));
        RenderUtils.drawRect(cX,cY-17,cX+cW,cY-15,ColorHelper.getColor(0));
        if(combatExpanded) {
            drawModuleList(combatModules,cX,cW,mouseX,mouseY);
        }

        //Movement
        int mX = cX + cW + 5;
        int mY = 29;
        int mW = 100;
        int mOff = mY + 1;
        int mH = movementExpanded ? (17 * movementModules.size()) : 0;
        int mH2 = 0;

        for(Module m : movementModules){
            if(expandedModules.contains(m)){
                for (Setting s : m.getSettingList()){
                    if(s instanceof ModeSetting) {
                        mH2 += 28;
                        if (((ModeSetting) s).getExpanded()) {
                            for (String modes : ((ModeSetting) s).getSettings()) {
                                if (modes != ((ModeSetting) s).getMode()) {
                                    mH2 += 12;
                                }
                            }
                        }
                        mH2 += 2;
                    }

                    if(s instanceof NumberSetting){
                        mH2 += 17;
                    }

                    if(s instanceof BooleanSetting){
                        mH2 += 17;
                    }
                }
            }
        }
        if(movementExpanded) mH += mH2;

        RenderUtils.drawRect(mX-1,mY-18,mX+mW+1,mY+mH+1,RenderUtils.transparency(new Color(37,37,37).getRGB(),0.6f));
        RenderUtils.drawRect(mX,mY -15,mX+mW,mY,RenderUtils.transparency(new Color(29, 29, 29, 255).getRGB(),0.8f));
        Main.getInstance().getFontManager().getFont("guicons 24").drawStringWithShadow("n", mX+1, mY - 11, -1);
        Main.getInstance().getFontManager().getFont("clean").drawStringWithShadow("Movement",mX + Main.getInstance().getFontManager().getFont("guicons 24").getWidth("n") + 1,mY - 12,-1);
        Main.getInstance().getFontManager().getFont("clean").drawStringWithShadow(movementExpanded ? "-" : "+",mX + mW - Main.getInstance().getFontManager().getFont("clean").getWidth(movementExpanded? "-" : "+") - 1,mY - 12,-1);
        RenderUtils.drawRect(mX,mY,mX+mW,mY+mH,RenderUtils.transparency(new Color(45, 45, 45, 255).getRGB(),0.8f));
        RenderUtils.drawRect(mX,mY-17,mX+mW,mY-15,ColorHelper.getColor(0));
        if(movementExpanded) {
            drawModuleList(movementModules,mX,mW,mouseX,mouseY);
        }

        //Player
        int pX = mX + mW + 5;
        int pY = 29;
        int pW = 100;
        int pOff = cY + 1;
        int pH = playerExpanded ? (17 * playerModules.size()) : 0;
        int pH2 = 0;

        for(Module m : playerModules){
            if(expandedModules.contains(m)){
                for (Setting s : m.getSettingList()){
                    if(s instanceof ModeSetting) {
                        pH2 += 28;
                        if (((ModeSetting) s).getExpanded()) {
                            for (String modes : ((ModeSetting) s).getSettings()) {
                                if (modes != ((ModeSetting) s).getMode()) {
                                    pH2 += 12;
                                }
                            }
                        }
                        pH2 += 2;
                    }

                    if(s instanceof NumberSetting){
                        pH2 += 17;
                    }

                    if(s instanceof BooleanSetting){
                        pH2 += 17;
                    }
                }
            }
        }
        if(playerExpanded) pH += pH2;

        RenderUtils.drawRect(pX-1,pY-18,pX+pW+1,pY+pH+1,RenderUtils.transparency(new Color(37,37,37).getRGB(),0.6f));
        RenderUtils.drawRect(pX,pY -15,pX+pW,pY,RenderUtils.transparency(new Color(29, 29, 29, 255).getRGB(),0.8f));
        Main.getInstance().getFontManager().getFont("guicons 24").drawStringWithShadow("n", pX+1, pY - 11, -1);
        Main.getInstance().getFontManager().getFont("clean").drawStringWithShadow("Player",pX + Main.getInstance().getFontManager().getFont("guicons 24").getWidth("n") + 1,pY - 12,-1);
        Main.getInstance().getFontManager().getFont("clean").drawStringWithShadow(playerExpanded ? "-" : "+",pX + pW - Main.getInstance().getFontManager().getFont("clean").getWidth(playerExpanded? "-" : "+") - 1,pY - 12,-1);
        RenderUtils.drawRect(pX,pY,pX+pW,pY+pH,RenderUtils.transparency(new Color(45, 45, 45, 255).getRGB(),0.8f));
        RenderUtils.drawRect(pX,pY-17,pX+pW,pY-15,ColorHelper.getColor(0));

        if(playerExpanded) {
            drawModuleList(playerModules,pX,pW,mouseX,mouseY);
        }

        //Render
        int rX = pX + pW + 5;
        int rY = 29;
        int rW = 100;
        int rOff = cY + 1;
        int rH = renderExpanded ? (17 * renderModules.size()) : 0;
        int rH2 = 0;

        for(Module m : renderModules){
            if(expandedModules.contains(m)){
                for (Setting s : m.getSettingList()){
                    if(s instanceof ModeSetting) {
                        rH2 += 28;
                        if (((ModeSetting) s).getExpanded()) {
                            for (String modes : ((ModeSetting) s).getSettings()) {
                                if (modes != ((ModeSetting) s).getMode()) {
                                    rH2 += 12;
                                }
                            }
                        }
                        rH2 += 2;
                    }

                    if(s instanceof NumberSetting){
                        rH2 += 17;
                    }

                    if(s instanceof BooleanSetting){
                        rH2 += 17;
                    }
                }
            }
        }
        if(renderExpanded) rH += rH2;

        RenderUtils.drawRect(rX-1,rY-18,rX+rW+1,rY+rH+1,RenderUtils.transparency(new Color(37,37,37).getRGB(),0.6f));
        RenderUtils.drawRect(rX,rY -15,rX+rW,rY,RenderUtils.transparency(new Color(29, 29, 29, 255).getRGB(),0.8f));
        Main.getInstance().getFontManager().getFont("guicons 24").drawStringWithShadow("i", rX+1, rY - 11, -1);
        Main.getInstance().getFontManager().getFont("clean").drawStringWithShadow("Render",rX + Main.getInstance().getFontManager().getFont("guicons 24").getWidth("i") + 1,rY - 12,-1);
        Main.getInstance().getFontManager().getFont("clean").drawStringWithShadow(renderExpanded ? "-" : "+",rX + rW - Main.getInstance().getFontManager().getFont("clean").getWidth(renderExpanded? "-" : "+") - 1,rY - 12,-1);
        RenderUtils.drawRect(rX,rY,rX+rW,rY+rH,RenderUtils.transparency(new Color(45, 45, 45, 255).getRGB(),0.8f));
        RenderUtils.drawRect(rX,rY-17,rX+rW,rY-15,ColorHelper.getColor(0));
        if(renderExpanded) {
            drawModuleList(renderModules,rX,rW,mouseX,mouseY);
        }

        //Exploit
        int eX = rX + rW + 5;
        int eY = 29;
        int eW = 100;
        int eOff = cY + 1;
        int eH = exploitExpanded ? (17 * exploitModules.size()) : 0;
        int eH2 = 0;

        for(Module m : exploitModules){
            if(expandedModules.contains(m)){
                for (Setting s : m.getSettingList()){
                    if(s instanceof ModeSetting) {
                        eH2 += 28;
                        if (((ModeSetting) s).getExpanded()) {
                            for (String modes : ((ModeSetting) s).getSettings()) {
                                if (modes != ((ModeSetting) s).getMode()) {
                                    eH2 += 12;
                                }
                            }
                        }
                        eH2 += 2;
                    }

                    if(s instanceof NumberSetting){
                        eH2 += 17;
                    }

                    if(s instanceof BooleanSetting){
                        eH2 += 17;
                    }
                }
            }
        }
        if(exploitExpanded) eH += eH2;

        RenderUtils.drawRect(eX-1,eY-18,eX+eW+1,eY+eH+1,RenderUtils.transparency(new Color(37,37,37).getRGB(),0.6f));
        RenderUtils.drawRect(eX,eY -15,eX+eW,eY,RenderUtils.transparency(new Color(29, 29, 29, 255).getRGB(),0.8f));
        Main.getInstance().getFontManager().getFont("guicons 24").drawStringWithShadow("e", eX+1, eY - 11, -1);
        Main.getInstance().getFontManager().getFont("clean").drawStringWithShadow("Exploit",eX + Main.getInstance().getFontManager().getFont("guicons 24").getWidth("e") + 1,eY - 12,-1);
        Main.getInstance().getFontManager().getFont("clean").drawStringWithShadow(exploitExpanded ? "-" : "+",eX + eW - Main.getInstance().getFontManager().getFont("clean").getWidth(exploitExpanded? "-" : "+") - 1,eY - 12,-1);
        RenderUtils.drawRect(eX,eY,eX+eW,eY+eH,RenderUtils.transparency(new Color(45, 45, 45, 255).getRGB(),0.8f));
        RenderUtils.drawRect(eX,eY-17,eX+eW,eY-15,ColorHelper.getColor(0));
        if(exploitExpanded) {
            drawModuleList(exploitModules,eX,eW,mouseX,mouseY);
        }

        //Misc
        int miscX = eX + eW + 5;
        int miscY = 29;
        int miscW = 100;
        int miscOff = cY + 1;
        int miscH = miscExpanded ? (17 * miscModules.size()) : 0;
        int miscH2 = 0;

        for(Module m : miscModules){
            if(expandedModules.contains(m)){
                for (Setting s : m.getSettingList()){
                    if(s instanceof ModeSetting) {
                        miscH2 += 28;
                        if (((ModeSetting) s).getExpanded()) {
                            for (String modes : ((ModeSetting) s).getSettings()) {
                                if (modes != ((ModeSetting) s).getMode()) {
                                    miscH2 += 12;
                                }
                            }
                        }
                        miscH2 += 2;
                    }

                    if(s instanceof NumberSetting){
                        miscH2 += 17;
                    }

                    if(s instanceof BooleanSetting){
                        miscH2 += 17;
                    }
                }
            }
        }
        if(miscExpanded) miscH += miscH2;

        RenderUtils.drawRect(miscX-1,miscY-18,miscX+miscW+1,miscY+miscH+1,RenderUtils.transparency(new Color(37,37,37).getRGB(),0.6f));
        RenderUtils.drawRect(miscX,miscY -15,miscX+miscW,miscY,RenderUtils.transparency(new Color(29, 29, 29, 255).getRGB(),0.8f));
        Main.getInstance().getFontManager().getFont("guicons 24").drawStringWithShadow("m", miscX+1, miscY - 11, -1);
        Main.getInstance().getFontManager().getFont("clean").drawStringWithShadow("Misc",miscX + Main.getInstance().getFontManager().getFont("guicons 24").getWidth("m") + 1,miscY - 12,-1);
        Main.getInstance().getFontManager().getFont("clean").drawStringWithShadow(miscExpanded ? "-" : "+",miscX + miscW - Main.getInstance().getFontManager().getFont("clean").getWidth(miscExpanded? "-" : "+") - 1,miscY - 12,-1);
        RenderUtils.drawRect(miscX,miscY,miscX+miscW,miscY+miscH,RenderUtils.transparency(new Color(45, 45, 45, 255).getRGB(),0.8f));
        RenderUtils.drawRect(miscX,miscY-17,miscX+miscW,miscY-15,ColorHelper.getColor(0));
        if(miscExpanded) {
            drawModuleList(miscModules,miscX,miscW,mouseX,mouseY);
        }

        GL11.glPopMatrix();


        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    public void drawModuleButton(Module module, double x, double y, int mouseX, int mouseY) {
        RenderUtils.drawRoundedRectangle(x, y, x + 100, y + 16, 10, new Color(30, 30, 30).getRGB());
        fr.drawString(module.getName(), (float) x + 5, (float) y + 3, -1);
        if (isHovered(mouseX, mouseY, x, y, x + 100, y + 16)) {
            activeDesc = module.getDescription();
        }

    }

    public void drawModuleList(ArrayList<Module> category, float x, float width, int mouseX, int mouseY) {
        int offset = yHeight + 1;
        for (Module m : category) {
            int offSave = offset;

            if (m.isToggled())
                RenderUtils.drawRect(x + 0.5f, offset, x + width - 0.5f, offset + 17, RenderUtils.transparency(new Color(35, 35, 35, 255).getRGB(), 0.8f));
            moduleTitle.drawString(m.getName(), (x + width / 2) - (moduleTitle.getWidth(m.getName()) / 2), offset + 4, m.toggled ? -1 : new Color(166, 166, 166).getRGB());
            offset += 17;

            if (expandedModules.contains(m)) {
                for (Setting s : m.getSettingList()) {
                    try {
                        if (s instanceof ModeSetting) {
                            ModeSetting mode = (ModeSetting) s;
                            RenderUtils.drawRect(x + 2, offset + 2, x + width - 2, offset + 3, ColorHelper.getColor(0));
                            RenderUtils.drawRect(x + 2, offset + 3, x + width - 2, offset + 14, RenderUtils.transparency(new Color(29, 29, 29, 255).getRGB(), 0.8f));
                            RenderUtils.drawRect(x + 2, offset + 14, x + width - 2, offset + 28, RenderUtils.transparency(new Color(37, 37, 37).getRGB(), 0.8f));
                            moduleSubtitle.drawString(mode.name, x + 4, offset + 4.5f, new Color(255, 255, 255).getRGB());
                            moduleSubtitle.drawString(mode.getMode(), x + 4, offset + 17, new Color(255, 255, 255).getRGB());
                            offset += 28;

                            if (((ModeSetting) s).getExpanded()) {
                                for (String modes : ((ModeSetting) s).getSettings()) {
                                    if (modes != mode.getMode()) {
                                        RenderUtils.drawRect(x + 2, offset, x + width - 2, offset + 12, new Color(40, 40, 40).getRGB());
                                        moduleSubtitle.drawString(modes, x + 4, offset + 2, new Color(160, 160, 160).getRGB());
                                        offset += 12;
                                    }
                                }
                            }

                            offset += 2;
                        }


                        if (s instanceof NumberSetting) {
                            float width2 = width - 2;
                            float var37 = (float) (150);
                            float var42 = (float) ((double) Math.round(((NumberSetting) s).getVal() * width2) / width2);
                            float var46 = (float) (width2 / ((NumberSetting) s).getMax());
                            float var48 = var42 * var46;
                            if(((NumberSetting) s).getVal() < 0){
                                //ChatHelper.addChat("POOPY NEG" + ((NumberSetting) s).getVal());
                            }
                            float var51 = (var37 - 50.0F) / width2;
                            RenderUtils.drawRect(x + 2, offset, x + width - 2, offset + 14, RenderUtils.transparency(new Color(29, 29, 29, 255).getRGB(), 0.8f));
                            //RenderUtils.drawRect(x + 2,offset + 14,x + width- 2,offset + 28,RenderUtils.transparency(new Color(37,37,37).getRGB(),0.8f));
                            int widthFinal = (int) (width / (((NumberSetting) s).getMax() - ((NumberSetting) s).getVal()));
                            RenderUtils.drawRect(x + 2, offset + 13.5, x + var48 * var51 - 2, offset + 15, ColorHelper.getColor(0));
                            moduleSubtitle.drawString(s.name, x + 3, offset + 3, new Color(215, 215, 215).getRGB());
                            moduleSubtitle.drawString(String.valueOf(((NumberSetting) s).getVal()), x + width - 3 - moduleSubtitle.getWidth(String.valueOf(((NumberSetting) s).getVal())), offset + 3, new Color(215, 215, 215).getRGB());
                            if (this.dragging) {
                                if (isHovered(mouseX, mouseY, x, offset + 5, x + width, offset + 15)) {
                                    double difference = ((NumberSetting) s).getMax() - ((NumberSetting) s).getMin();
                                    double value = ((NumberSetting) s).getMin() + MathHelper.clamp_double((mouseX - x) / 100, 0, 1) * difference;
                                    double set = cc.diablo.helpers.MathHelper.getIncremental(value, ((NumberSetting) s).getInc());
                                    ((NumberSetting) s).setVal(set);
                                }
                            }
                            offset += 17;
                        }

                        if (s instanceof BooleanSetting) {
                            moduleSubtitle.drawString(s.name, x + 3, offset + 5, new Color(215, 215, 215).getRGB());
                            //RenderUtils.drawRoundedRectangle(x + width - 16, offset + 4, x + width - 6, offset + 13, 5, ((BooleanSetting) s).isChecked() ? new Color(37, 37, 37).getRGB() : new Color(27, 27, 27).getRGB());
                            RenderUtils.drawRoundedRectangle(x + width - 16, offset + 4, x + width - 6, offset + 13, 5, ((BooleanSetting) s).isChecked() ? ColorHelper.getColor(0) : new Color(27, 27, 27).getRGB());
                            offset += 17;
                        }
                    } catch (Exception e2) {
                        if(s instanceof ModeSetting){
                            ((ModeSetting) s).setMode(((ModeSetting) s).getDefaultMode());
                        }
                    }
                }
            }
        }
    }

    public void detectClicks(ArrayList<Module> category,int mouseX, int mouseY, int mouseButton,float x, float w){
        int off = yHeight + 1;
        for(Module m : category){
            if (isHovered(mouseX, mouseY, x + 1, off + 1, x + w - 1, off + 15)) {
                if (mouseButton == 0) {
                    m.toggle();
                } else if (mouseButton == 1) {
                    if (expandedModules.contains(m)) {
                        expandedModules.remove(m);
                    } else {
                        expandedModules.add(m);
                    }
                }
            }

            if (expandedModules.contains(m)) {
                for (Setting s : m.getSettingList()) {
                    if(s instanceof ModeSetting){
                        off += 28;
                        if (isHovered(mouseX, mouseY, x + 2,off,x + width - 2,off + 14)) {
                            ((ModeSetting) s).setExpanded(!((ModeSetting) s).getExpanded());
                        }

                        if(((ModeSetting) s).getExpanded()) {
                            off += 3;
                            for (String modes : ((ModeSetting) s).getSettings()) {
                                if(modes != ((ModeSetting) s).getMode()) {
                                    off += 12;
                                    if (isHovered(mouseX, mouseY, x + 2, off, x + width - 2, off + 12)) {
                                        ((ModeSetting) s).setMode(modes);
                                        ((ModeSetting) s).setExpanded(false);
                                    }
                                }
                            }
                        }
                        off += 2;
                    }

                    if (s instanceof NumberSetting) {
                        /*
                        float width2 = (float) (width - 2);
                        float var37 = (float)(150);
                        float var42 = (float)((double)Math.round((double)((NumberSetting) s).getVal() * width2) / width2);
                        float var46 = (float) (width2 / ((NumberSetting) s).getMax());
                        float var48 = var42 * var46;
                        float var51 = (float) ((var37 - 50.0F) / width2);
                        //RenderUtils.drawRect(x + 2,offset + 14,x + width- 2,offset + 28,RenderUtils.transparency(new Color(37,37,37).getRGB(),0.8f));
                        int widthFinal = (int) (width / (((NumberSetting) s).getMax() - ((NumberSetting) s).getVal()));
                        RenderUtils.drawRect(x + 2,off + 13.5,x + var48 * var51- 2,off + 15,ColorHelper.getColor(0));
                        if (isHovered(mouseX, mouseY, x + 2,off + 13.5,x + width - 2,off + 15)) {
                            float diff = mouseX - x;
                            float diff2 = diff * var51;
                            if(diff2 < var48) {
                                ((NumberSetting) s).setVal(((NumberSetting) s).getVal() + ((NumberSetting) s).getInc());
                            } else {
                                ((NumberSetting) s).setVal(((NumberSetting) s).getVal() - ((NumberSetting) s).getInc());
                            }
                        }

                         */
                        off += 17;
                    }

                    if (s instanceof BooleanSetting) {
                        off += 15;
                        if (isHovered(mouseX, mouseY, x + w - 16, off + 4, x + w - 6, off + 13)) {
                            ((BooleanSetting) s).setChecked(!((BooleanSetting) s).isChecked());
                        }
                        off += 2;
                    }
                }
            }
            off += 17;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.dragging = true;

        int cX = 5;
        int cY = 29;
        int cW = 100;
        int cOff = cY + 1;
        int cH = 17 * combatModules.size();

        if (isHovered(mouseX, mouseY, cX,cY -15,cX+cW,cY)) {
            if (mouseButton == 1) {
                combatExpanded = !combatExpanded;
            }
        }

        if(combatExpanded) {
            detectClicks(combatModules,mouseX,mouseY,mouseButton,cX,cW);
        }

        //Movement
        int mX = cX + cW + 5;
        int mY = 29;
        int mW = 100;
        int mOff = mY + 1;
        int mH = 17 * movementModules.size();

        if (isHovered(mouseX, mouseY, mX,mY -15,mX+mW,mY)) {
            if (mouseButton == 1) {
                movementExpanded = !movementExpanded;
                //movementExpanded = !movementExpanded;
            }
        }

        if(movementExpanded){
            detectClicks(movementModules,mouseX,mouseY,mouseButton,mX,mW);
        }

        //Player
        int pX = mX + mW + 5;
        int pY = 29;
        int pW = 100;
        int pOff = pY + 1;
        int pH = 17 * playerModules.size();

        if (isHovered(mouseX, mouseY, pX,pY -15,pX+pW,pY)) {
            if (mouseButton == 1) {
                playerExpanded = !playerExpanded;
            }
        }

        if(playerExpanded){
            detectClicks(playerModules,mouseX,mouseY,mouseButton,pX,pW);
        }

        //Render
        int rX = pX + pW + 5;
        int rY = 29;
        int rW = 100;
        int rOff = rY + 1;
        int rH = 17 * renderModules.size();

        if (isHovered(mouseX, mouseY, rX,rY -15,rX+rW,rY)) {
            if (mouseButton == 1) {
                renderExpanded = !renderExpanded;
            }
        }

        if(renderExpanded){
            detectClicks(renderModules,mouseX,mouseY,mouseButton,rX,rW);
        }

        //Exploit
        int eX = rX + rW + 5;
        int eY = 29;
        int eW = 100;
        int eOff = eY + 1;
        int eH = 17 * exploitModules.size();

        if (isHovered(mouseX, mouseY, eX,eY -15,eX+eW,eY)) {
            if (mouseButton == 1) {
                exploitExpanded = !exploitExpanded;
            }
        }

        if(exploitExpanded){
            detectClicks(exploitModules,mouseX,mouseY,mouseButton,eX,eW);
        }

        //Misc
        int miscX = eX + eW + 5;
        int miscY = 29;
        int miscW = 100;
        int miscOff = miscY + 1;
        int miscH = 17 * miscModules.size();

        if (isHovered(mouseX, mouseY, miscX,miscY -15,miscX+miscW,miscY)) {
            if (mouseButton == 1) {
                miscExpanded = !miscExpanded;
            }
        }

        if(miscExpanded){
            detectClicks(miscModules,mouseX,mouseY,mouseButton,miscX,miscW);
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        this.dragging = false;
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    @Override
    public void handleInput() throws IOException {
        super.handleInput();
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
    }

    @Override
    public void handleKeyboardInput() throws IOException {
        super.handleKeyboardInput();
    }

    @Override
    public void updateScreen() {

        super.updateScreen();
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }

    public boolean isHovered(int mouseX, int mouseY, double x, double y, double width, double height) {
        return mouseX >= x && mouseX <= width && mouseY >= y && mouseY <= height;
    }

}
