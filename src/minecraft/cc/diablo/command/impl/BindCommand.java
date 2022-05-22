package cc.diablo.command.impl;

import cc.diablo.manager.file.FileManager;
import cc.diablo.manager.file.files.KeyBindsFile;
import cc.diablo.manager.module.ModuleManager;
import com.google.common.eventbus.Subscribe;
import cc.diablo.command.Command;
import cc.diablo.event.impl.ChatEvent;
import cc.diablo.helpers.render.ChatColor;
import cc.diablo.helpers.render.ChatHelper;
import cc.diablo.module.Module;
import org.lwjgl.input.Keyboard;

import java.security.Key;
import java.util.Locale;

public class BindCommand extends Command {
    public BindCommand(){
        super("Bind","Bind A Module To A Key");
    }

    @Subscribe
    public void onCommand(ChatEvent e){
        String[] message = e.message.split(" ");
        if(message[0].equals("bind")) {
            try {
                //ChatHelper.addChat(e.getMessage().substring(message[0].length() + message[1].length() + 2));
                Module module = ModuleManager.getModuleByName(message[1]);
                if(module != null) {
                    module.setKey(Keyboard.getKeyIndex(message[2].toUpperCase(Locale.ROOT)));
                } else {
                    ChatHelper.addChat("Invalid Module");
                    return;
                }
                ChatHelper.addChat("Bound " + module.getName() + " to " + Keyboard.getKeyName(module.getKey()));
                new FileManager().saveFiles();
            } catch (Exception exception) {
                ChatHelper.addChat("Failed to bind module");
                exception.printStackTrace();
            }
        }
    }
}
