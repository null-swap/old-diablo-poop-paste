package cc.diablo.module.impl.player;

import cc.diablo.helpers.MathHelper;
import cc.diablo.manager.module.ModuleManager;
import cc.diablo.setting.impl.BooleanSetting;
import cc.diablo.setting.impl.ModeSetting;
import cc.diablo.setting.impl.NumberSetting;
import com.google.common.eventbus.Subscribe;
import cc.diablo.event.impl.UpdateEvent;
import cc.diablo.helpers.PacketHelper;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.input.Keyboard;

public class AntiVoid extends Module {
    public ModeSetting mode = new ModeSetting("Mode", "Watchdog", "Watchdog", "Vanilla");
    //public static BooleanSetting disableTemp = new BooleanSetting("Disable Temporarily",true);
    public static BooleanSetting enableScaffold = new BooleanSetting("Enable Scaffold", true);
    public static NumberSetting distance = new NumberSetting("Fall Distance", 3, 1, 50, 1);

    public AntiVoid() {
        super("AntiVoid", "Prevents Falling In The Void", Keyboard.KEY_NONE, Category.Player);
        this.addSettings(mode, enableScaffold, distance);
    }

    @Subscribe
    public void onUpdate(UpdateEvent e) {
        //ModuleManager.modules.remove(ModuleManager.getModuleByName("AntiVoid"));
        //ModuleManager.modules.add(new AntiVoid());

        if (!e.isPre()) return;
        if (this.isBlockUnder() || !(mc.thePlayer.fallDistance >= distance.getVal()) || mc.thePlayer.capabilities.isFlying || mc.thePlayer.capabilities.isCreativeMode)
            return;

        e.setY(e.getY());
        switch (this.mode.getMode()) {
            case "Vanilla":
                PacketHelper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.fallDistance + 12, mc.thePlayer.posZ, true));
            break;
            case "Watchdog":
                switch (mc.thePlayer.ticksExisted % 2) {
                    case 0:
                        e.setX(e.getX() +(-Math.sin(MathHelper.degToRad(e.getYaw() + 90)) * 0.2));
                        e.setZ(e.getZ() + (Math.cos(MathHelper.degToRad(e.getYaw() + 90)) * 0.2));
                    break;
                    case 1:
                        e.setX(e.getX() +(-Math.sin(MathHelper.degToRad(e.getYaw() - 90)) * 0.2));
                        e.setZ(e.getZ() + (Math.cos(MathHelper.degToRad(e.getYaw() - 90)) * 0.2));
                    break;
                }
            break;
        }

    }

    private boolean isBlockUnder() {
        if (!(mc.thePlayer.posY < 0.0D)) {
            for (int offset = 0; offset < (int) mc.thePlayer.posY + 2; offset += 2) {
                AxisAlignedBB bb = mc.thePlayer.getEntityBoundingBox().offset(0.0D, -offset, 0.0D);
                if (!Minecraft.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty()) {
                    return true;
                }
            }

        }
        return false;
    }
}





            /*if(disableTemp.isChecked()) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            ModuleManager.getModule(AntiVoid.class).setToggled(false);
                            ModuleManager.getModule(KillAura.class).setToggled(false);
                            ModuleManager.getModule(Fly.class).setToggled(false);
                            Thread.sleep(5000);
                            ModuleManager.getModule(AntiVoid.class).setToggled(true);
                            ModuleManager.getModule(AntiVoid.class).onEnable();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }*/