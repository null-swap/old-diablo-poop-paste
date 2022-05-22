package cc.diablo.module.impl.render;

import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.setting.impl.NumberSetting;
import org.lwjgl.input.Keyboard;

public class Glow extends Module {
    public static NumberSetting red = new NumberSetting("Red",45,1,255,1);
    public static NumberSetting green = new NumberSetting("Green",24,1,255,1);
    public static NumberSetting blue = new NumberSetting("Blue",158,1,255,1);
    public static NumberSetting size = new NumberSetting("Size",1,1,5,1);

    public Glow(){
        super("Glow","faggot esp", Keyboard.KEY_NONE, Category.Render);
        this.addSettings(red,green,blue,size);
    }
}
