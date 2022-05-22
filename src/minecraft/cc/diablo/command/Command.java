package cc.diablo.command;

import cc.diablo.Main;
import cc.diablo.module.Category;
import cc.diablo.setting.Setting;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Command {
    public static Minecraft mc = Minecraft.getMinecraft();
    public String name, displayName, description;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
        Main.getInstance().getEventBus().register(this);
    }

    public String getDisplayName() {
        return displayName == null ? name : displayName;
    }
    public String getDescription() {
        return description;
    }
}
