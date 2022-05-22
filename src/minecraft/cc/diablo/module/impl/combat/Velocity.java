package cc.diablo.module.impl.combat;

import cc.diablo.setting.impl.ModeSetting;
import cc.diablo.setting.impl.NumberSetting;
import com.google.common.eventbus.Subscribe;
import cc.diablo.event.impl.PacketEvent;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplode;
import org.lwjgl.input.Keyboard;

public class Velocity extends Module {
    public NumberSetting vertical = new NumberSetting("Vertical", 100, 0, 100, 1);
    public NumberSetting horizontal = new NumberSetting("Horizontal", 0, 0, 100, 1);
    public ModeSetting explosionBoost = new ModeSetting("Explosion Boost","Absolute","Cancel","Multiply","Absolute");
    public NumberSetting e_vertical = new NumberSetting("Explosion Vertical", 100, 0, 500, 1);
    public NumberSetting e_horizontal = new NumberSetting("Explosion Horizontal", 0, 0, 500, 1);

    public Velocity() {
        super("Velocity", "Modifies KB", Keyboard.KEY_NONE, Category.Combat);
        this.addSettings(vertical,horizontal,explosionBoost,e_vertical,e_horizontal);
    }

    @Subscribe
    public void onPacket(PacketEvent e) {
        /*
        if((vertical.getVal() == 0 && horizontal.getVal() == 0)) {
            this.setDisplayName("Velocity\2477 Cancel");
        } else {

         */
            this.setDisplayName("Velocity\2477 V:" + vertical.getVal() + " H:" + horizontal.getVal());

        if(e.getPacket() instanceof S27PacketExplode) {
            S27PacketExplode e_packet = (S27PacketExplode) e.getPacket();
            switch(explosionBoost.getMode()) {
                case "Cancel":
                    e.setCancelled(true);
                    break;
                case "Multiply":
                    e_packet.motionY = (float) ((e_packet.motionY / 100) * e_vertical.getVal());
                    e_packet.motionX = (float) ((e_packet.motionX / 100) * e_horizontal.getVal());
                    e_packet.motionZ = (float) ((e_packet.motionZ / 100) * e_horizontal.getVal());
                    break;
                case "Absolute":
                    e_packet.motionY = ((float) e_vertical.getVal() / 50);
                    e_packet.motionX = (float) ((e_packet.motionX / 100) * e_horizontal.getVal() / 50);
                    e_packet.motionZ = (float) ((e_packet.motionZ / 100) * e_horizontal.getVal() / 50);
                    break;
            }
        }

        if(e.getPacket() instanceof S12PacketEntityVelocity) {
            S12PacketEntityVelocity packet = (S12PacketEntityVelocity) e.getPacket();

            if((vertical.getVal() == 0 && horizontal.getVal() == 0)) {
                e.setCancelled(true);
            } else {
                packet.motionY = (int) ((packet.motionY / 100) * vertical.getVal());
                packet.motionX = (int) ((packet.motionX / 100) * horizontal.getVal());
                packet.motionZ = (int) ((packet.motionZ / 100) * horizontal.getVal());
            }
        }
    }
}
