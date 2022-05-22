package cc.diablo.module.impl.player;

import com.google.common.eventbus.Subscribe;
import cc.diablo.event.impl.CollideEvent;
import cc.diablo.event.impl.CollisionEvent;
import cc.diablo.event.impl.UpdateEvent;
import cc.diablo.helpers.player.KillAuraHelper;
import cc.diablo.helpers.MathHelper;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.setting.impl.ModeSetting;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.input.Keyboard;

public class NoFall extends Module {
    public ModeSetting nmode = new ModeSetting("NoFall mode","Verus","Edit", "Watchdog", "Verus");
    public NoFall(){
        super("NoFall", "Remove fall damage", Keyboard.KEY_NONE, Category.Player);
        this.addSettings(nmode);
    }

    @Subscribe
    public void onCollide(CollisionEvent e) {
        String mode = this.nmode.getMode();
        switch (mode) {
            case "Verus":
                double x = mc.thePlayer.posX;
                double y = mc.thePlayer.posY;
                double z = mc.thePlayer.posZ;
                if(mc.thePlayer.fallDistance >= 5) {
                    e.getList().add(new AxisAlignedBB(x, y, z));
                }
                break;
        }
    }
    @Subscribe
    public void onUpdate(UpdateEvent e) {
        String mode = this.nmode.getMode();
        this.setDisplayName("NoFall\2477 " + this.nmode.getMode());
        switch (mode) {
            case "Watchdog":
                if (mc.thePlayer.fallDistance > 2.69 && e.isPre()) {
                    sendPackets();
                    mc.thePlayer.fallDistance = 0;
                }
                break;
        }
    }
    public void sendPackets() {
        mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer(true));
    }
}