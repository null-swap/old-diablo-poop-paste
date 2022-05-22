package cc.diablo.module.impl.render;

import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.setting.impl.NumberSetting;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class Crosshair extends Module {

    public static NumberSetting red = new NumberSetting("Red",70,1,255,1);
    public static NumberSetting green = new NumberSetting("Green",243,1,255,1);
    public static NumberSetting blue = new NumberSetting("Blue",99,1,255,1);
    public static NumberSetting thicknes = new NumberSetting("Thickness",0.5,1,4,0.5);
    public static NumberSetting length = new NumberSetting("Length",7,1,20,1);

    public Crosshair(){
        super("Crosshair","Customize your ingame crosshair", Keyboard.KEY_NONE, Category.Render);
        this.addSettings(red,green,blue,thicknes,length);
    }
}
