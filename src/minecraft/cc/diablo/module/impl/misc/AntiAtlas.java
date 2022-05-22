package cc.diablo.module.impl.misc;

import cc.diablo.event.impl.PacketEvent;
import cc.diablo.event.impl.UpdateEvent;
import cc.diablo.helpers.PacketHelper;
import cc.diablo.helpers.Stopwatch;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AntiAtlas extends Module {
    private final List<UUID> reported = new ArrayList<UUID>();
    private final Stopwatch timer = new Stopwatch();

    int total = 0;
    public AntiAtlas(){
        super("AntiAtlas","Automatically reports all players in your lobby on Hypixel", Keyboard.KEY_NONE, Category.Misc);
    }

    @Override
    public void onEnable(){
        timer.reset();
        reported.clear();
        super.onEnable();
    }

    @Subscribe
    public void onUpdate(UpdateEvent e){
        for (NetworkPlayerInfo playerInfo : mc.getNetHandler().getPlayerInfoMap()) {
            this.setDisplayName(this.getName()+"\2477 " + total);
            UUID uuid = playerInfo.getGameProfile().getId();
            String name = playerInfo.getGameProfile().getName();

            if (timer.hasReached(5000) && !reported.contains(uuid) && !uuid.equals(mc.thePlayer.getUniqueID())) {
                PacketHelper.sendPacketNoEvent(new C01PacketChatMessage("/report " + name + " killaura fly speed"));
                reported.add(uuid);
                total++;
                timer.reset();
            }
        }
    }

    @Subscribe
    public void onPacket(PacketEvent e){
        if (e.getPacket() instanceof S02PacketChat) {
            S02PacketChat packet = (S02PacketChat) e.getPacket();
            if (packet.getChatComponent().getFormattedText().startsWith("\u00A7cThere is no player named")) {
                e.setCancelled(true);
            }
        }
    }
}
