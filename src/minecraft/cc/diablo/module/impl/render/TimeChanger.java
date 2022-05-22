package cc.diablo.module.impl.render;

import cc.diablo.event.impl.PacketEvent;
import cc.diablo.event.impl.TickEvent;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.setting.impl.NumberSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import org.lwjgl.input.Keyboard;

public class TimeChanger extends Module {
    public NumberSetting time = new NumberSetting("Time",200,0,240,1);
    public TimeChanger(){
        super("Time Changer","Changes the world time", Keyboard.KEY_NONE, Category.Render);
        this.addSettings(time);
    }

    @Subscribe
    public void onTick(TickEvent event) {
        Minecraft.theWorld.setWorldTime((long) time.getVal() * 100);
    }

    @Subscribe
    private void onReceive(PacketEvent event) {
        if (event.getDir() == PacketEvent.DirectionType.Incoming) {
            if (event.getPacket() instanceof S03PacketTimeUpdate) {
                //S03PacketTimeUpdate packetTimeUpdate = (S03PacketTimeUpdate) event.getPacket();
                //packetTimeUpdate.setWorldTime((long) time.getVal());
                event.setCancelled(true);
            }
        }
    }
}
