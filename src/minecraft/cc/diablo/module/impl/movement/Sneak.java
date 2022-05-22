package cc.diablo.module.impl.movement;

import com.google.common.eventbus.Subscribe;
import cc.diablo.event.impl.PacketEvent;
import cc.diablo.event.impl.TickEvent;
import cc.diablo.event.impl.UpdateEvent;
import cc.diablo.helpers.PacketHelper;
import cc.diablo.helpers.render.ChatHelper;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.setting.impl.ModeSetting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import tv.twitch.chat.Chat;

import java.util.ArrayList;

public class Sneak extends Module {
    public ModeSetting mode = new ModeSetting("Disabler Mode","Packet","Packet", "Legit");

    public Sneak() {
        super("Sneak", "Sneaks for you", Keyboard.KEY_NONE, Category.Movement);
    }


    @Override
    public void onEnable(){
        switch (mode.getMode()) {
            case "Packet":
                PacketHelper.sendPacketNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
        break;
        }
        super.onEnable();
    }

    @Override
    public void onDisable(){
        super.onDisable();
    }

    @Subscribe
    public void onUpdate(UpdateEvent e){
    }


}
