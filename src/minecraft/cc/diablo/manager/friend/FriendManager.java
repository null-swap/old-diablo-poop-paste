package cc.diablo.manager.friend;

import cc.diablo.helpers.render.ChatColor;
import cc.diablo.helpers.render.ChatHelper;
import cc.diablo.manager.map.MapManager;
import net.minecraft.util.*;
import net.minecraft.client.*;

import java.util.*;

public class FriendManager extends MapManager<String, String> {
    public static ArrayList<Friend> friendsList;

    static {
        FriendManager.friendsList = new ArrayList<Friend>();
    }

    public void addFriend(final String name, final String alias) {
        contents.put(name, alias);
    }

    public static void addFriendToList(String name, String alias) {
        ChatHelper.addChat(ChatColor.GREEN + "Added " + ChatColor.WHITE + name + " as a friend");
        friendsList.add(new Friend(name, alias));
    }

    public static String getAliasName(final String name) {
        String alias = "";
        for (final Friend friend : FriendManager.friendsList) {
            if (friend.name.equalsIgnoreCase(StringUtils.stripControlCodes(name))) {
                alias = friend.alias;
                break;
            }
        }
        if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().thePlayer.getGameProfile().getName() == name) {
            return name;
        }
        return alias;
    }

    public static void removeFriend(final String name) {
        ChatHelper.addChat(ChatColor.RED + "Removed " + ChatColor.WHITE + name + " as a friend");
        for (final Friend friend : FriendManager.friendsList) {
            if (friend.name.equalsIgnoreCase(name)) {
                FriendManager.friendsList.remove(friend);
                break;
            }
        }
    }

    public static String replace(String text) {
        for (final Friend friend : FriendManager.friendsList) {
            if (text.contains(friend.name)) {
                text = friend.alias;
            }
        }
        return text;
    }

    public static boolean isFriend(final String name) {
        boolean isFriend = false;
        for (final Friend friend : FriendManager.friendsList) {
            if (friend.name.equalsIgnoreCase(StringUtils.stripControlCodes(name))) {
                isFriend = true;
                break;
            }
        }
        if (Minecraft.getMinecraft().thePlayer.getGameProfile().getName() == name) {
            isFriend = true;
        }
        return isFriend;
    }

    public void addFriend(Friend friend) {
        friendsList.add(friend);
    }

    @Override
    public void setup() {
        this.contents = (Map) new HashMap<Object, Object>();
    }
}

