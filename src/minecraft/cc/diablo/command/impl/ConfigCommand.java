package cc.diablo.command.impl;

import cc.diablo.Main;
import cc.diablo.command.Command;
import cc.diablo.event.impl.ChatEvent;
import cc.diablo.helpers.render.ChatHelper;
import cc.diablo.manager.file.FileManager;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.Minecraft;
import org.apache.commons.compress.utils.IOUtils;
import store.intent.intentguard.annotation.Native;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class ConfigCommand extends Command {
    public ConfigCommand(){
        super("Config", "Config Manager");
    }

    @Native
    @Subscribe
    public void onCommand(ChatEvent e){
        String[] message = e.message.split(" ");
        if(message[0].equals("cloudconfig")){
            switch (message[1]){
                case "list":
                    try {
                        URL u= new URL("https://diablo.wtf/configs/avalableconfigs.txt");
                        URLConnection  uc = u.openConnection();
                        uc.connect();
                        uc = u.openConnection();
                        uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
                        BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
                        ChatHelper.addChat("Listing all available online configs");
                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            ChatHelper.addChat(inputLine);
                        }
                        in.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        ChatHelper.addChat("An exception occurred");
                    }
                    break;
                case "download":
                    try {
                        URL u= new URL("https://diablo.wtf/configs/"+message[2]+".diablo");
                        URLConnection uc = u.openConnection();
                        uc.connect();
                        uc = u.openConnection();
                        uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
                        IOUtils.copy(uc.getInputStream(),new FileOutputStream(new File(Main.fileDir, "Config.diablo")));
                        new FileManager().loadFiles();
                        ChatHelper.addChat("Downloaded and loaded config! (" + message[2]+")");
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        ChatHelper.addChat("An exception occurred");
                    }
                    break;
            }
        }
        if(message[0].equals("config")){
            switch (message[1]){
                case "save":
                    new FileManager().saveFiles();
                    ChatHelper.addChat("Saved config!");
                    break;
                case "load":
                    new FileManager().loadFiles();
                    ChatHelper.addChat("Loaded config!");
                    break;
                case "download":
                    try {
                        URL u= new URL("https://diablo.wtf/configs/"+message[2]+".diablo");
                        URLConnection uc = u.openConnection();
                        uc.connect();
                        uc = u.openConnection();
                        uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
                        IOUtils.copy(uc.getInputStream(),new FileOutputStream(new File(Main.fileDir, "Config.diablo")));
                        new FileManager().loadFiles();
                        ChatHelper.addChat("Downloaded and loaded config! (" + message[2]+")");
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        ChatHelper.addChat("An IO exception occurred");
                    }
                    break;
            }
        }
    }
}
