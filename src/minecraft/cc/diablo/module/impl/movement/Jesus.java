package cc.diablo.module.impl.movement;

import cc.diablo.event.impl.PacketEvent;
import cc.diablo.event.impl.UpdateEvent;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.setting.impl.ModeSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;

public class Jesus extends Module {
    public ModeSetting mode = new ModeSetting("Mode","Verus","Vanila","Verus");
    public static boolean shouldOffsetPacket;

    public Jesus(){
        super("Jesus","BECOME THE LORD AND SAVIOR JESUS CHRIST AND WALK ON WATER", Keyboard.KEY_NONE, Category.Movement);
    }

    @Subscribe
    public void onPacket(PacketEvent e){
        switch (mode.getMode()){
            case "Vanilla":
                if(e.isOutgoing()) {
                    if(e.packet instanceof C03PacketPlayer && mc.thePlayer.isInWater()) {
                        final C03PacketPlayer packet = (C03PacketPlayer)e.packet;
                        shouldOffsetPacket = !shouldOffsetPacket;
                        if (shouldOffsetPacket) {
                            final C03PacketPlayer c03PacketPlayer = packet;
                            c03PacketPlayer.y -= 1.0E-6;
                        }
                    }
                }
        }
        this.setDisplayName("Jesus\2477 " + this.mode.getMode());
    }

    @Subscribe
    public void onUpdate(UpdateEvent e){
        switch (mode.getMode()){
            case "Verus":
                if (mc.thePlayer.isInWater()) {
                    mc.thePlayer.motionY = 5.9;
                }
                mc.thePlayer.jumpMovementFactor *= 0.9f;
                if (mc.thePlayer.isInWater()) {
                    mc.thePlayer.motionY = 0.2;
                }
        }
    }
}
