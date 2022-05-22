package cc.diablo.module.impl.misc;

import cc.diablo.helpers.render.RenderUtils;
import cc.diablo.module.impl.render.XRay;
import cc.diablo.setting.impl.ModeSetting;
import com.google.common.eventbus.Subscribe;
import cc.diablo.event.impl.PacketEvent;
import cc.diablo.helpers.PacketHelper;
import cc.diablo.helpers.render.ChatHelper;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class AutoHypixel extends Module {
    public ModeSetting debug = new ModeSetting("Mode","1","1","2");
    public AutoHypixel(){
        super("AutoHypixel", "Automatically queues games for you", Keyboard.KEY_NONE, Category.Misc);
        this.addSettings(debug);
    }

    @Subscribe
    public void onPacket(PacketEvent e){
        if(e.getPacket() instanceof S02PacketChat) {
            S02PacketChat packetChat = (S02PacketChat) e.getPacket();
            String message = packetChat.getChatComponent().getUnformattedText();
            if (message.contains("You won! Want to play again?") || message.contains("You died! Want to play again?") || message.contains("Queued! Use the bed to return to lobby!")) {
                switch (debug.getMode()) {
                    case "1":
                        ChatHelper.addChat("Finding Item");
                        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(7));
                        mc.playerController.windowClick(1, 39, 0, 0, mc.thePlayer);
                    case "2":
                        PacketHelper.sendPacket(new C01PacketChatMessage("/play solo_insane"));
                }
            }

            if (message.contains("bedwars")) {
                ChatHelper.addChat("SEX");
                for (BlockPos object : XRay.blockPosList) {
                    if (Block.getStateId(Minecraft.theWorld.getBlockState(object)) == 56) {
                        PacketHelper.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(BlockPos.ORIGIN.getX(),BlockPos.ORIGIN.getY() + 5,BlockPos.ORIGIN.getZ(),false));
                    }
                }
            }
        }
    }
}
