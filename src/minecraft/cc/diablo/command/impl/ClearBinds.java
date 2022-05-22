package cc.diablo.command.impl;

import cc.diablo.command.Command;
import cc.diablo.event.impl.ChatEvent;
import cc.diablo.helpers.render.ChatHelper;
import cc.diablo.manager.module.ModuleManager;
import cc.diablo.module.Module;
import com.google.common.eventbus.Subscribe;
import org.lwjgl.input.Keyboard;

public class ClearBinds extends Command {
    public ClearBinds(){
        super("ClearBinds", "Clear all binds of ");
    }

    @Subscribe
    public void onCommand(ChatEvent e){
        String[] message = e.message.split(" ");
        if(message[0].equals("clearbinds")){
            for(Module m : ModuleManager.getModules()){
                m.setKey(Keyboard.KEY_NONE);
            }
            ChatHelper.addChat("Reset binds");
        }
    }
}
