package cc.diablo.module.impl.misc;

import cc.diablo.Main;
import cc.diablo.event.impl.UpdateEvent;
import cc.diablo.helpers.render.ChatColor;
import cc.diablo.helpers.render.ChatHelper;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class NameHider extends Module {
    public static ArrayList<String> names = new ArrayList();
    public NameHider(){
        super("NameHider","Hides your name", Keyboard.KEY_NONE, Category.Misc);
    }

    @Subscribe
    public void onUpdate(UpdateEvent e){
        for (NetworkPlayerInfo networkplayerinfo :  mc.getNetHandler().getPlayerInfoMap()) {
            if (!names.contains(networkplayerinfo.getGameProfile().getName())) {
                names.add(networkplayerinfo.getGameProfile().getName());
            }
        }
    }
}
