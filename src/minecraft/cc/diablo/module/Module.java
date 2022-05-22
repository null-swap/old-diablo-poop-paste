package cc.diablo.module;

import cc.diablo.Main;
import cc.diablo.helpers.module.ModuleData;
import cc.diablo.render.notification.Notification;
import cc.diablo.render.notification.NotificationManager;
import cc.diablo.setting.Setting;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter@Setter
public class Module {
    public static Minecraft mc = Minecraft.getMinecraft();
    public String name, displayName, description;
    public int key;
    public Category category;
    public boolean toggled,extended,clickRegistered;
    public List<Setting> settingList = new ArrayList<>();
    public Module() {
        name = getClass().getAnnotation(ModuleData.class).name();
        description = getClass().getAnnotation(ModuleData.class).description();
        key = getClass().getAnnotation(ModuleData.class).bind();
        category = getClass().getAnnotation(ModuleData.class).category();
    }

    public Module(String name, String description, int key, Category category) {
        this.name = name;
        this.description = description;
        this.key = key;
        this.category = category;
        this.toggled = false;
        this.extended = false;
        this.clickRegistered = false;
    }

    public void toggle() {
        toggled = !toggled;
        if(toggled)
            onEnable();
        else
            onDisable();
    }

    public String getDisplayName() {
        return displayName == null ? name : displayName;
    }
    public void addSettings(Setting... settings)
    {
        this.settingList.addAll(Arrays.asList(settings));
    }
    public void onEnable() {
        Main.getInstance().getEventBus().register(this);
    }
    public void onDisable() {
        Main.getInstance().getEventBus().unregister(this);
    }

    public void setToggled(boolean bool){
        this.toggled = bool;
        if(bool == true){
            this.onEnable();
        } else {
            this.onDisable();
        }
    }

    public boolean isExtended(){
        return extended;
    }

    public boolean isRegistered(){
        return clickRegistered;
    }

}
