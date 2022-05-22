package cc.diablo.module.impl.render;

import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.setting.impl.NumberSetting;
import org.lwjgl.input.Keyboard;

public class SlowHit extends Module {
    public static NumberSetting speed = new NumberSetting("Speed",20,1,30,1);
    public SlowHit(){
        super("SlowHit","Slows down the player's swing animation", Keyboard.KEY_NONE, Category.Render);
        this.addSettings();
    }
}
