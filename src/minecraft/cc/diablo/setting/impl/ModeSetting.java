package cc.diablo.setting.impl;

import cc.diablo.setting.Setting;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter@Setter
public class ModeSetting extends Setting {
    public int index;
    public List<String> settings;
    public boolean expanded;
    public String defaultMode;
    public ModeSetting(String name, String defaultMode, String... modes) {
        this.name = name;
        this.settings = Arrays.asList(modes);
        this.defaultMode = defaultMode;
        this.index = this.settings.indexOf(defaultMode);
        this.expanded = false;
    }
    public String getMode() {
        return settings.get(index);
    }

    public boolean isMode(String mode) {
        return index == settings.indexOf(mode);
    }

    public void setMode(String mode) {
        index = settings.indexOf(mode);
    }

    public boolean getExpanded(){
        return this.expanded;
    }

    public String getDefaultMode(){
        return this.defaultMode;
    }

    public void setExpanded(boolean bool){
        this.expanded = bool;
    }
}
