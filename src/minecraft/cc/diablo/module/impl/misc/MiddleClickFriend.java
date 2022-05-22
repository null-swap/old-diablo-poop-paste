package cc.diablo.module.impl.misc;

import cc.diablo.event.impl.TickEvent;
import cc.diablo.event.impl.UpdateEvent;
import cc.diablo.helpers.render.ChatColor;
import cc.diablo.helpers.render.ChatHelper;
import cc.diablo.manager.friend.FriendManager;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

public class MiddleClickFriend extends Module {
    public MiddleClickFriend(){
        super("MiddleClickFriend","Adds friends", Keyboard.KEY_NONE, Category.Misc);
    }

    @Override
    public void onEnable(){
        this.setDisplayName("MCF");
    }

    @Subscribe
    public void onTick(UpdateEvent event) {
        if(mc.gameSettings.keyBindPickBlock.pressed) {
            final EntityPlayer player = (EntityPlayer) Minecraft.getMinecraft().objectMouseOver.entityHit;
            final String name = player.getName();
            if (FriendManager.isFriend(name)) {
                ChatHelper.addChat(ChatColor.RED + "Removed " + ChatColor.WHITE + player.getName() + " as a friend");
                FriendManager.removeFriend(player.getName());
            }
            else {
                ChatHelper.addChat(ChatColor.GREEN + "Added " + ChatColor.WHITE + player.getName() + " as a friend");
                FriendManager.addFriendToList(player.getName(),player.getName());

            }
            mc.gameSettings.keyBindPickBlock.pressed = false;
        }
    }
}

