package cc.diablo.module.impl.misc;

import cc.diablo.event.impl.ChatEvent;
import cc.diablo.helpers.Stopwatch;
import cc.diablo.manager.IRCClient;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import com.google.common.eventbus.Subscribe;
import org.lwjgl.input.Keyboard;

import java.net.URISyntaxException;
import java.security.Key;

public class IRC extends Module {
    private IRCClient client;
    Stopwatch time = new Stopwatch();

    public IRC(){
        super("IRC","Talk with other clients users", Keyboard.KEY_NONE, Category.Misc);
    }

    @Override
    public void onEnable() {
        time.reset();

        try {
            /*
            client = new IRCClient();
            client.connectBlocking();

             */
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        if (client != null) {
            client.close();
            client = null;
        }
        super.onDisable();
    }

    @Subscribe
    public void onChat(ChatEvent event) {
        String message = event.getMessage();

        if (message.startsWith("-") || message.startsWith("- ")) {
            event.setCancelled(true);
            client.send(event.getMessage().replace("- ", ""));
        }
    }
}
