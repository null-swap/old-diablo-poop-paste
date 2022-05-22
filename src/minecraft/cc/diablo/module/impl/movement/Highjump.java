package cc.diablo.module.impl.movement;

import cc.diablo.event.impl.PacketEvent;
import cc.diablo.event.impl.UpdateEvent;
import cc.diablo.helpers.HypixelHelper;
import cc.diablo.helpers.MathHelper;
import cc.diablo.helpers.PacketHelper;
import cc.diablo.helpers.player.EntityHelper;
import cc.diablo.helpers.player.KillAuraHelper;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.setting.impl.ModeSetting;
import cc.diablo.setting.impl.NumberSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.lwjgl.input.Keyboard;

public class Highjump extends Module {
    public ModeSetting mode = new ModeSetting("Mode","Verus","Verus","HypixelSlime","Matrix");
    public NumberSetting speed = new NumberSetting("Speed",3,1,5,1);
    public boolean damaged;

    public Highjump(){
        super("Highjump","Extends your jump height", Keyboard.KEY_NONE, Category.Movement);
        this.addSettings(mode,speed);
    }

    @Subscribe
    public void onPacket(PacketEvent e){
        switch (mode.getMode()){
            case "Verus":
            case "HypixelSlime":
                if (e.getPacket() instanceof S12PacketEntityVelocity) {
                    if (mc.thePlayer.getEntityId() == ((S12PacketEntityVelocity) e.getPacket()).getEntityID()) {
                        mc.thePlayer.motionY = speed.getVal();
                        damaged = true;
                    }
                }
            break;
            case "Matrix":
                if (e.getPacket() instanceof S12PacketEntityVelocity) {
                    if (mc.thePlayer.getEntityId() == ((S12PacketEntityVelocity) e.getPacket()).getEntityID()) {
                        mc.thePlayer.motionY = speed.getVal();
                        damaged = true;
                    }
                }
            break;
        }

    }
    @Override
    public void onEnable(){
        damaged = false;
        switch (mode.getMode()){
            case "HypixelSlime":
                HypixelHelper.slimeDisable();
                break;
            case "Verus":
                double x = mc.thePlayer.posX, y = mc.thePlayer.posY, z = mc.thePlayer.posZ;
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 3.1, z, false));
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.0E-4, z, false));
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.0E-2, z, true));

                break;
            case "Matrix":
                PacketHelper.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                mc.playerController.onPlayerRightClick(mc.thePlayer, Minecraft.theWorld, mc.thePlayer.getHeldItem(), mc.thePlayer.getPosition(), Block.getFacingDirection(mc.thePlayer.getPosition()), mc.thePlayer.getLookVec());
                break;
        }
        super.onEnable();
    }
    @Subscribe
    public void onUpdate(UpdateEvent e){
        switch (mode.getMode()) {
            case "Verus":
            case "HypixelSlime":
                if (damaged) {
                    mc.thePlayer.motionY = speed.getVal();
                    damaged = false;
                } else {
                    if (mc.thePlayer.lastTickPosY > mc.thePlayer.posY) {
                        if (mc.thePlayer.isCollided) {
                            this.setToggled(false);
                        }
                    }
                }
            break;
            case "Matrix":
                mc.thePlayer.motionY = 1.3f;
                damaged = false;
                KillAuraHelper.setRotations(e,mc.thePlayer.rotationYaw,8f);
                if (mc.thePlayer.lastTickPosY > mc.thePlayer.posY) {
                    if (mc.thePlayer.isCollided) {
                        this.setToggled(false);
                    }
                }
                break;
        }
    }
}
