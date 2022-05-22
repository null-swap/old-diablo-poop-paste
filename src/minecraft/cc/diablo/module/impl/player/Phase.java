package cc.diablo.module.impl.player;

import cc.diablo.event.impl.CollideEvent;
import cc.diablo.event.impl.UpdateEvent;
import cc.diablo.helpers.PacketHelper;
import cc.diablo.helpers.render.ChatHelper;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.setting.impl.ModeSetting;
import cc.diablo.setting.impl.NumberSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.Timer;
import org.lwjgl.input.Keyboard;

public class Phase extends Module {
    public ModeSetting mode = new ModeSetting("Phase Mode", "Watchdog", "VClip", "Invaded");
    public NumberSetting distance = new NumberSetting("Distance", 1.1, 0.1, 8, 0.1);

    public Phase() {
        super("Phase", "Phase through blocks", Keyboard.KEY_NONE, Category.Player);
        this.addSettings(mode, distance);
    }

    @Override
    public void onEnable() {
        if ("VClip".equals(mode.getMode())) {
            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - distance.getVal(), mc.thePlayer.posZ);
            this.setToggled(false);
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Subscribe
    public void onUpdate(UpdateEvent e) {
        this.setDisplayName("Phase\2477 " + mode.getMode());
        /*switch (mode.getMode()) {
            case "Watchdog":
                if(mc.thePlayer.onGround && mc.thePlayer.isCollided){

                }
        }
        */
        /*
        if (BlockHelper.isInsideBlock() && mc.thePlayer.isSneaking()) {
            final float yaw = mc.thePlayer.rotationYaw;
            mc.thePlayer.getEntityBoundingBox().offset(distance.getVal() * -Math.sin(Math.toRadians(yaw)), 0.0, distance.getVal() * Math.cos(Math.toRadians(yaw)));
        } else {
        }

         */
    }

    @Subscribe
    public void onCollide(CollideEvent event) {
        /*
        if (event.getBoundingBox() != null && event.getBoundingBox().maxY > mc.thePlayer.getEntityBoundingBox().minY && mc.thePlayer.isSneaking()) {
            event.setBoundingBox(null);
        }

         */
    }
}

