package cc.diablo.module.impl.render;

import cc.diablo.Main;
import cc.diablo.clickguinew.impl.Button;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import org.lwjgl.input.Keyboard;

public class ClickGUI extends Module {
    public ClickGUI() {
        super("ClickGUI", "ClickGUI", Keyboard.KEY_RSHIFT, Category.Render);
    }
    @Override
    public void onEnable() {
        mc.displayGuiScreen(Main.getInstance().getClickGUI());
        toggle();
        super.onEnable();
    }
    @Override
    public void onDisable(){
        super.onDisable();
    }
}
