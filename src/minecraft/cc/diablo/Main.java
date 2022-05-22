package cc.diablo;

import cc.diablo.clickguinew.ClickGUI;
import cc.diablo.helpers.DiscordRPCHelper;
import cc.diablo.manager.file.FileManager;
import cc.diablo.manager.friend.FriendManager;
import cc.diablo.manager.module.ModuleManager;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cc.diablo.command.CommandManager;
import cc.diablo.event.impl.KeycodeEvent;
import cc.diablo.font.FontManager;
import cc.diablo.font.TTFFontRenderer;
import cc.diablo.module.Module;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import org.lwjgl.opengl.Display;
import store.intent.intentguard.annotation.Bootstrap;
import store.intent.intentguard.annotation.Native;

import java.awt.Color;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class Main {
    public static long timestamp = System.currentTimeMillis() / 1000;
    private static final Main Instance = new Main();
    public static String name;
    public static String customName;
    public static String authors;
    public static String version;
    public static String serverVersion;
    public static String buildType;
    public static Color clientColor;
    public static String username;
    public static String uid;
    public static File fileDir;
    private EventBus eventBus = new EventBus("Diablo");
    private FileManager fileManager;
    private ModuleManager moduleManager;
    private FontManager fontManager;
    private ClickGUI clickGUI;
    private CommandManager commandManager;
    private FriendManager friendManager;

    @Native
    @Bootstrap
    public static void StartClient() throws IOException {
        initClientVisuals();
        initClientVars();
        sendDiabloPrint("Launched Diablo, Running On Build " + version);
    }

    @Native
    public void setupEvents(){
        friendManager = new FriendManager();
        clientColor = new Color(80, 24, 116);
        eventBus = new EventBus(name);
        moduleManager = new ModuleManager();
        fontManager = new FontManager();
        clickGUI = new cc.diablo.clickguinew.ClickGUI();
        //clickGUI = new ClickGUI();
        fileDir = new File(Minecraft.getMinecraft().mcDataDir + "/" + name);
        fileManager = new FileManager();
        commandManager = new CommandManager();
        fileManager.loadFiles();
        eventBus.register(this);
    }

    @Native
    public static void initClientVars(){
        name = "Diablo";
        customName = "Diablo";
        authors = "Vince, kyle, and matt2";
        version = "1.6.1";
        buildType = "Developer";
    }

    @Native
    public static void initClientVisuals(){
        try {
            DiscordRPCHelper.updateRPC();
            InputStream inputStream = Config.getResourceStream(new ResourceLocation("Client/images/logo16.png"));
            InputStream inputStream1 = Config.getResourceStream( new ResourceLocation("Client/images/logo32.png"));
            Display.setIcon(new ByteBuffer[]{Config.readIconImage(inputStream),Config.readIconImage(inputStream1)});
            if(Main.version == null){Display.setTitle("Loading Diablo...");} else {Display.setTitle("Diablo " + Main.version);}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Main getInstance() {
        return Instance;
    }

    public static void sendDiabloPrint(String message) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss");

        System.out.println("[" + dtf.format(LocalDateTime.now()) + "] [Diablo] " + message);
    }

    @Subscribe
    public void onKeyCodePressed(KeycodeEvent e) {
        ModuleManager.getModules().stream().filter(module -> module.getKey() == e.getKey()).forEach(Module::toggle);
    }

    public TTFFontRenderer getSFUI(int size) {
        return fontManager.getFont("sfui " + size);
    }

    public TTFFontRenderer getSFPRO(int size) {
        return fontManager.getFont("sfpro " + size);
    }

}
