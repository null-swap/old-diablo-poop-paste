package cc.diablo.command.impl;

import com.google.common.eventbus.Subscribe;
import cc.diablo.Main;
import cc.diablo.command.Command;
import cc.diablo.event.impl.ChatEvent;
import cc.diablo.helpers.render.ChatColor;
import cc.diablo.helpers.render.ChatHelper;
import cc.diablo.helpers.render.ColorHelper;
import cc.diablo.module.Module;
import cc.diablo.manager.module.ModuleManager;

import java.awt.*;

public class HelpCommand extends Command {
    public HelpCommand(){
        super("Help","Provides infromation to the user about the client");
    }

    @Subscribe
    public void onCommand(ChatEvent e){
        String[] message = e.message.split(" ");
        if(message[0].equals("help")){
            ChatHelper.addChat("Diablo " + ChatColor.DARK_PURPLE + Main.version + ChatColor.WHITE+ " [" + ChatColor.GREEN + Main.buildType + ChatColor.WHITE + "]");
            ChatHelper.addChat("Developed by " + Main.authors);
            ChatHelper.addChat("To list all modules do .modules");
            ChatHelper.addChat("To list all commands do .commands");
        }
    }
}
