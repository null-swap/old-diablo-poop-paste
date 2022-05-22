package cc.diablo.module.impl.movement;

import cc.diablo.event.impl.PacketEvent;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.setting.impl.NumberSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.Timer;
import org.lwjgl.input.Keyboard;

public class BowFly extends Module {
    public NumberSetting speed = new NumberSetting("Speed",100D,50D,1000D,10D);
    public NumberSetting speedUp = new NumberSetting("Speed Up",100D,100D,300D,10D);
    public BowFly(){
        super("Bow Fly","Extend knockback from bow", Keyboard.KEY_NONE, Category.Movement);

        this.addSettings(speed,speedUp);
    }

    @Subscribe
    public void onPacket(PacketEvent e){
        speed.setValue(300);
        final double horizontal = speed.getVal();
        if (e.getPacket() instanceof S12PacketEntityVelocity) {
            S12PacketEntityVelocity packet = (S12PacketEntityVelocity) e.getPacket();
            if (packet.getEntityID() == mc.thePlayer.getEntityId()) {
                if (packet.getEntityID() != mc.thePlayer.getEntityId()) return;
                if (horizontal != 0) {
                    packet.motionX = (int) (horizontal * packet.getMotionX() / 100);
                    //packet.setMotionY(vertical * packet.getMotionY() / 100);
                    packet.motionZ = (int) (horizontal * packet.getMotionZ() / 100);
                } else e.setCancelled(true);
            }
        }
    }

    @Override
    public void onDisable(){
        Timer.timerSpeed = 1f;
        super.onDisable();
    }
}
