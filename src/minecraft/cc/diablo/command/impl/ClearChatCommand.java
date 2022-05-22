package cc.diablo.command.impl;

import com.google.common.eventbus.Subscribe;
import cc.diablo.Main;
import cc.diablo.command.Command;
import cc.diablo.event.impl.ChatEvent;
import cc.diablo.helpers.render.ChatColor;
import cc.diablo.helpers.render.ChatHelper;

public class ClearChatCommand extends Command {
    public ClearChatCommand(){
        super("ClearChat","Clears all messages in chat");
    }

    @Subscribe
    public void onCommand(ChatEvent e){
        String[] message = e.message.split(" ");
        if(message[0].equals("clearchat")){
            mc.ingameGUI.getChatGUI().clearChatMessages();
        }
    }
}
