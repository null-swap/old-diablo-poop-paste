package cc.diablo.command.impl;

import com.google.common.eventbus.Subscribe;
import cc.diablo.command.Command;
import cc.diablo.command.CommandManager;
import cc.diablo.event.impl.ChatEvent;
import cc.diablo.helpers.render.ChatHelper;
import cc.diablo.module.Module;
import cc.diablo.manager.module.ModuleManager;

public class CommandsCommand extends Command {
    public CommandsCommand(){
        super("Commands","Lists all commands and their descriptions");
    }

    @Subscribe
    public void onCommand(ChatEvent e){
        String[] message = e.message.split(" ");
        if(message[0].equals("commands")){
            for(Command c : CommandManager.getModules()){
                ChatHelper.addChat(c.getDisplayName() + " | " + c.getDescription());
            }
        }
    }
}
