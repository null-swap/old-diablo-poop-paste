package cc.diablo.helpers;

import cc.diablo.Main;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;

public class DiscordRPCHelper {
        public static int killCount = 0;
        public static String serverIp = "none";
        public static long timestamp = System.currentTimeMillis() / 1000;
        public static void updateRPC(){

                try {
                        DiscordRPC.discordInitialize("916790950322376704", new DiscordEventHandlers(), true);
                        DiscordRichPresence rich = new DiscordRichPresence.Builder("intent.store").setDetails("Server: " + serverIp + " | " + "Kills : " + killCount).setStartTimestamps(timestamp).setBigImage("logo", Main.version + " (" + Main.buildType + ")").build();
                        DiscordRPC.discordUpdatePresence(rich);

                } catch (Exception e) {
                        e.printStackTrace();
                }
        }
}
