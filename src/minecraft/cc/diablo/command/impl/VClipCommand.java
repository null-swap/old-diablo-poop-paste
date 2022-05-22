package cc.diablo.command.impl;

import com.google.common.eventbus.Subscribe;
import cc.diablo.command.Command;
import cc.diablo.event.impl.ChatEvent;
import cc.diablo.helpers.render.ChatHelper;
import cc.diablo.module.Module;
import cc.diablo.manager.module.ModuleManager;

public class VClipCommand extends Command {
    public VClipCommand(){
        super("VClip", "Vertically teleport by a value");
    }

    @Subscribe
    public void onCommand(ChatEvent e){
        String[] message = e.message.split(" ");
        if(message[0].equals("vclip")){
            mc.thePlayer.setPosition(mc.thePlayer.posX,mc.thePlayer.posY+Double.parseDouble(message[1]),mc.thePlayer.posZ);
        }
    }
}
