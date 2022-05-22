package cc.diablo.command.impl;

import com.google.common.eventbus.Subscribe;
import cc.diablo.Main;
import cc.diablo.command.Command;
import cc.diablo.event.impl.ChatEvent;
import cc.diablo.helpers.render.ChatHelper;
import cc.diablo.module.Module;
import cc.diablo.manager.module.ModuleManager;

public class ModulesCommand extends Command {

    public ModulesCommand(){
        super("Modules","Lists all modules and their descriptions");
    }

    @Subscribe
    public void onCommand(ChatEvent e){
        String[] message = e.message.split(" ");
        if(message[0].equals("modules")){
            for(Module m : ModuleManager.getModules()){
                ChatHelper.addChat(m.getName() + " | " + m.getDescription());
            }
        }
    }
}
