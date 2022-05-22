package cc.diablo.command.impl;

import cc.diablo.command.Command;
import cc.diablo.event.impl.ChatEvent;
import cc.diablo.helpers.render.ChatColor;
import cc.diablo.helpers.render.ChatHelper;
import cc.diablo.manager.module.ModuleManager;
import cc.diablo.module.Module;
import com.google.common.eventbus.Subscribe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.network.NetworkPlayerInfo;
import optifine.Json;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

public class SkinCommand extends Command {
    public static final String uuidURL = "https://sessionserver.mojang.com/session/minecraft/profile/";
    public static final String nameURL = "https://api.mojang.com/users/profiles/minecraft/";

    public static String skin = null;
    public SkinCommand(){
        super("Skin","Changes the player skin to any player's skin");
    }

    @Subscribe
    public void onCommand(ChatEvent e){
        String[] message = e.message.split(" ");
        if(message[0].equals("skin")){
            try {
                URL url = new URL("http://www.yahoo.com/image_to_read.jpg");
                InputStream in = new BufferedInputStream(url.openStream());
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int n = 0;
                while (-1!=(n=in.read(buf)))
                {
                    out.write(buf, 0, n);
                }
                out.close();
                in.close();
                byte[] response = out.toByteArray();
                FileOutputStream fos = new FileOutputStream("C://borrowed_image.jpg");
                fos.write(response);
                fos.close();
            } catch (Exception exception) {
                ChatHelper.addChat("A error occurred");
                exception.printStackTrace();
            }
        }
    }
    /*

    public static Json getPlayerJson(UUID uuid, String name) throws IOException {
        URL url;

        if (name != null) {
            url = new URL(nameURL + name);
        } else {
            url = new URL(uuidURL + uuid);
        }
        URLConnection urlConnection = url.openConnection();

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(urlConnection.getInputStream()));
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        bufferedReader.close();
        if(!stringBuilder.toString().startsWith("{")){
            return null;
        }
        return new Gson(stringBuilder.toString());
    }

    public static UUID getUUID(String name) throws IOException {
        String uuidTemp = (String) getPlayerJson(null, name).get("id");
        String uuid = "";
        for (int i = 0; i <= 31; i++) {
            uuid = uuid + uuidTemp.charAt(i);
            if (i == 7 || i == 11 || i == 15 || i == 19) {
                uuid = uuid + "-";
            }
        }

        if(getPlayerJson(null, name) != null){
            return UUID.fromString(uuid);
        }
        return null;
    }

    public static String getName(UUID uuid) throws IOException {
        if(getPlayerJson(uuid, null) != null){
            return (String) getPlayerJson(uuid, null).get("name");
        }
        return null;
    }

     */
}
