package cc.diablo.module.impl.misc;

import cc.diablo.event.impl.PacketEvent;
import cc.diablo.helpers.PacketHelper;
import cc.diablo.helpers.render.ChatHelper;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.setting.impl.NumberSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;
import org.lwjgl.input.Keyboard;

import java.util.Locale;

public class AutoBlocksMC extends Module {
    public NumberSetting distance = new NumberSetting("Distance",3,1,6,1);
    public AutoBlocksMC(){
        super("AutoBlocksMC","Automatically phases out of cages", Keyboard.KEY_NONE, Category.Misc);
        this.addSettings(distance);
    }

    @Subscribe
    public void onPacket(PacketEvent e){
        if(e.isIncoming()){
            if(e.getPacket() instanceof S02PacketChat){
                S02PacketChat packetChat = (S02PacketChat) e.getPacket();
                if(packetChat.getChatComponent().getUnformattedText().contains("Cages open in") && packetChat.getChatComponent().getUnformattedText().contains("10")){
                    mc.thePlayer.setPosition(mc.thePlayer.posX,mc.thePlayer.posY - distance.getVal(),mc.thePlayer.posZ);
                    ChatHelper.addChat("Automatically phased out!");
                }
            }
        }
    }
}
