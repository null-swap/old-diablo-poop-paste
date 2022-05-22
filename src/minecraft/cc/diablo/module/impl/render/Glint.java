package cc.diablo.module.impl.render;

import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.setting.impl.NumberSetting;
import org.lwjgl.input.Keyboard;

public class Glint extends Module {
    public static NumberSetting red = new NumberSetting("Red",53,1,255,1);
    public static NumberSetting green = new NumberSetting("Green",157,1,255,1);
    public static NumberSetting blue = new NumberSetting("Blue",255,1,255,1);

    public Glint(){
        super("Glint","Changes the glint color", Keyboard.KEY_NONE, Category.Render);
        this.addSettings(red,green,blue);
    }
}
