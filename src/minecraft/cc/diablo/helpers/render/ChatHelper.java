package cc.diablo.helpers.render;

import cc.diablo.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class ChatHelper {
    public static void addChat(String text) {
        if (Minecraft.theWorld != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(ChatColor.WHITE + "[" + ChatColor.DARK_PURPLE + Main.name.charAt(0) + ChatColor.WHITE + "]" + ChatColor.RESET + " " + text));
        }
    }

    public static void print(String text) {
        if (Minecraft.theWorld != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(text));
        }
    }

    public static String translateColorCodes(String toTranslate) {
        return toTranslate.replaceAll("&", "§");
    }
}
