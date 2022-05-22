package cc.diablo.module.impl.render;

import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.setting.impl.BooleanSetting;
import cc.diablo.setting.impl.NumberSetting;
import org.lwjgl.input.Keyboard;

public class ChestChams extends Module {
    public static BooleanSetting colored = new BooleanSetting("Colored",true);
    public static BooleanSetting material = new BooleanSetting("Material",true);
    public static NumberSetting red = new NumberSetting("Red",53,1,255,1);
    public static NumberSetting green = new NumberSetting("Green",157,1,255,1);
    public static NumberSetting blue = new NumberSetting("Blue",255,1,255,1);
    public static NumberSetting red_hidden = new NumberSetting("Red Hidden",255,1,255,1);
    public static NumberSetting green_hidden = new NumberSetting("Green Hidden",54,1,255,1);
    public static NumberSetting blue_hidden = new NumberSetting("Blue Hidden",54,1,255,1);
    public static NumberSetting transparency = new NumberSetting("Transparency",230,1,255,1);

    public ChestChams(){
        super("Chest Chams","Chinnese government for jews", Keyboard.KEY_NONE, Category.Render);
        this.addSettings(colored,material,red,green,blue,red_hidden,green_hidden,blue_hidden,transparency);
    }
}
