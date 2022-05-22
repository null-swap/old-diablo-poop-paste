package cc.diablo.manager;

import cc.diablo.Main;
import cc.diablo.helpers.render.ChatHelper;
import com.viaversion.viabackwards.utils.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class IRCClient extends WebSocketClient {
    public static char SPLIT = '\u0000';

    public IRCClient() throws URISyntaxException {
        super(new URI("ws://66.135.1.39:1337"));
        this.setAttachment(Main.username);
        this.addHeader("name", this.getAttachment());
        this.addHeader("uid", Main.uid);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("IRC Connected");
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§7[§bIRC§7] §f" + "Connected"));
    }

    @Override
    public void onMessage(String s) {
        System.out.println(s);

        if (s.contains(Character.toString(SPLIT))) {

            String[] split = s.split(Character.toString(SPLIT));
            if (split.length != 3) {
                return;
            }
            String username = split[0];
            String uid = split[1];
            String message = split[2];
            uid = uid.replace("(", "§7(§b").replace(")", "§7)");

            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(ChatHelper.translateColorCodes("§7[§bIRC§7] §b" + username + uid + " " + "§f: " + message)));
        } else {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§7[§bIRC§7] " + s));
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§7[§bIRC§7] §f" + "Disconnected"));
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }
}
