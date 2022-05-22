package cc.diablo.module.impl.render;

import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.setting.impl.BooleanSetting;
import cc.diablo.setting.impl.ModeSetting;
import cc.diablo.setting.impl.NumberSetting;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.lwjgl.input.Keyboard;

public class Animations extends Module {
    public static ModeSetting mode = new ModeSetting("Animations Mode","Slide","Swing","Slide","Slide2","Slide3","Swang","Swank","Swong","Swaing","Sigma","Spinning","Punch","Stella","Styles");
    public static BooleanSetting otherSwing = new BooleanSetting("Other Swing",true);
    public static NumberSetting speed = new NumberSetting("Speed",20,1,30,1);
    public static NumberSetting scale = new NumberSetting("Scale",0.75,0.1,2,0.05);
    public static NumberSetting xOffset = new NumberSetting("X",3,-2,2,0.05);
    public static NumberSetting yOffset = new NumberSetting("Y",0.45,-2,2,0.05);
    public static NumberSetting zOffset = new NumberSetting("Z",-0.3,-2,2,0.05);
    public static NumberSetting xOffsetAnimation = new NumberSetting("Animation X",3,-2,2,0.05);
    public static NumberSetting yOffsetAnimation = new NumberSetting("Animation Y",0.45,-2,2,0.05);
    public static NumberSetting zOffsetAnimation = new NumberSetting("Animation Z",-0.3,-2,2,0.05);

    public Animations() {
        super("Animations", "Animations for blocking", Keyboard.KEY_NONE, Category.Render);
        this.addSettings(mode,otherSwing,speed,scale,xOffset,yOffset,zOffset,xOffsetAnimation,yOffsetAnimation,zOffsetAnimation);
    }
}
