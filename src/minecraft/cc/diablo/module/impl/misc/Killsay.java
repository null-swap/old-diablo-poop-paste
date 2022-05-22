package cc.diablo.module.impl.misc;

import cc.diablo.event.impl.PacketEvent;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.setting.impl.ModeSetting;
import com.google.common.eventbus.Subscribe;
import org.lwjgl.input.Keyboard;

public class Killsay extends Module {
    public ModeSetting mode = new ModeSetting("Mode","Diablo","Custom","Diablo");
    public Killsay(){
        super("Killsay","Sends a message in chat upon killing a player", Keyboard.KEY_NONE, Category.Misc);
        this.addSettings(mode);
    }

    @Subscribe
    public void onPacket(PacketEvent e){

    }
}
