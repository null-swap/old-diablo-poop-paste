package cc.diablo.module.impl.render;

import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.setting.impl.BooleanSetting;
import org.lwjgl.input.Keyboard;

public class CustomChat extends Module {
    public static BooleanSetting clearChat = new BooleanSetting("Clear Chat",true);
    public static BooleanSetting customFont = new BooleanSetting("Custom Font",false);
    public CustomChat(){
        super("CustomChat","Customize the visuals of the chat", Keyboard.KEY_NONE, Category.Render);
        this.addSettings(clearChat,customFont);
    }
}
