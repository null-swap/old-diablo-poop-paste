package cc.diablo.module.impl.misc;

import com.google.common.eventbus.Subscribe;
import cc.diablo.event.impl.PacketEvent;
import cc.diablo.event.impl.TickEvent;
import cc.diablo.helpers.PacketHelper;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.setting.impl.NumberSetting;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.lwjgl.input.Keyboard;

public class Timer extends Module {
    public NumberSetting speed = new NumberSetting("Speed",2,0.1,10,0.1);
    public Timer(){
        super("Timer", "Changes game time", Keyboard.KEY_NONE, Category.Misc);
        this.addSettings(speed);

    }
    @Override
    public void onEnable(){
        net.minecraft.util.Timer.timerSpeed = 1f;
        super.onEnable();
    }

    @Override
    public void onDisable(){
    net.minecraft.util.Timer.timerSpeed = 1f;
        super.onDisable();
    }
    @Subscribe
    public void onPacket(TickEvent e){
        if (mc.thePlayer != null) {
            this.setDisplayName("Timer\2477 " + speed.getVal());
            net.minecraft.util.Timer.timerSpeed = (float) speed.getVal();
        }
    }
}