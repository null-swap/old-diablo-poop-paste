package cc.diablo.setting.impl;

import cc.diablo.setting.Setting;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class BooleanSetting extends Setting {
    public boolean checked, def;
    public BooleanSetting(String name, boolean defaultChecked) {
        this.name = name;
        this.def = defaultChecked;
        this.checked = defaultChecked;
    }
    public boolean getDefault(){
        return def;
    }
    public void toggle() {
        this.checked = !this.checked;
    }
}
