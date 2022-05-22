package cc.diablo.command.impl;

import cc.diablo.manager.module.ModuleManager;
import com.google.common.eventbus.Subscribe;
import cc.diablo.command.Command;
import cc.diablo.event.impl.ChatEvent;
import cc.diablo.helpers.render.ChatColor;
import cc.diablo.helpers.render.ChatHelper;
import cc.diablo.module.Module;

public class ToggleCommand extends Command {

    public ToggleCommand(){
        super("Toggle","Toggle a module");
    }

    @Subscribe
    public void onCommand(ChatEvent e){
        String[] message = e.message.split(" ");
        if(message[0].equals("toggle")||message[0].equals("t")){
            try {
                Module module = ModuleManager.getModuleByName(message[1]);
                boolean moduleState = module.isToggled();
                if(moduleState){
                    ChatHelper.addChat(ChatColor.RED + "Disabled " + ChatColor.WHITE+ module.getName() + "!");
                    module.setToggled(false);
                } else {
                    ChatHelper.addChat(ChatColor.GREEN + "Enabled " + ChatColor.WHITE+ module.getName() + "!");
                    module.setToggled(true);
                }
            } catch (Exception exception) {
                ChatHelper.addChat("Invalid module parsed");
                exception.printStackTrace();
            }
        }
    }
}
