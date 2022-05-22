package cc.diablo.module.impl.movement;

import cc.diablo.setting.impl.NumberSetting;
import com.google.common.eventbus.Subscribe;
import cc.diablo.event.impl.UpdateEvent;
import cc.diablo.helpers.PacketHelper;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.module.impl.combat.KillAura;
import cc.diablo.setting.impl.ModeSetting;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.lwjgl.input.Keyboard;

import java.util.concurrent.ThreadLocalRandom;

public class NoSlow extends Module {
    public ModeSetting mode = new ModeSetting("NoSlow mode","Vanilla","Vanilla", "Watchdog","NCP", "Verus");
    public NumberSetting speed = new NumberSetting("Ticks", 6, 0, 20, 1);
    public NoSlow() {
        super("NoSlow", "No Slowdown", Keyboard.KEY_NONE, Category.Movement);
        this.addSettings(mode,speed);
    }

    @Subscribe
    public void onUpdate(UpdateEvent e) {
        this.setDisplayName("NoSlowdown\2477 " + this.mode.getMode() + " | " + speed.getVal());
        String mode = this.mode.getMode();
        switch (mode) {
            case "Vanilla":
                break;
            case "NCP":
                if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && mc.gameSettings.keyBindUseItem.isKeyDown()) {
                    if ((e.isPre())) {
                        PacketHelper.sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    } else {
                        PacketHelper.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                    }
                }
                break;
            case "Watchdog":
                if(mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && mc.gameSettings.keyBindUseItem.isKeyDown() && mc.thePlayer.ticksExisted % speed.getVal() == 0) {
                    if (e.isPost()) {
                        PacketHelper.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
                    } else if (e.isPre()) {
                        PacketHelper.sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN));
                    }
                }
                /*
                if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && mc.gameSettings.keyBindUseItem.isKeyDown() && KillAura.target != null && mc.thePlayer.ticksExisted % speed.getVal() == 0) {
                    if (e.isPost()) {
                        PacketHelper.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                        mc.playerController.onPlayerRightClick(mc.thePlayer, Minecraft.theWorld, mc.thePlayer.getHeldItem(), mc.thePlayer.getPosition(), Block.getFacingDirection(mc.thePlayer.getPosition()), mc.thePlayer.getLookVec());
                    }
                }

                 */
                break;
        }
        /*
        if(mc.thePlayer.isUsingItem() && !mc.thePlayer.isSneaking() && !mc.thePlayer.isCollidedHorizontally) {
            mc.thePlayer.setSprinting(mc.thePlayer.moveForward > 0);
        }

         */
    }

}
