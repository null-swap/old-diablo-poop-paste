package cc.diablo.module.impl.render;

import cc.diablo.module.Category;
import cc.diablo.module.Module;
import org.lwjgl.input.Keyboard;

public class ShowName extends Module {
    public ShowName(){
        super("ShowName","Renders Your Own Nametag", Keyboard.KEY_NONE, Category.Render);
    }

    @Override
    public void onEnable(){
        mc.thePlayer.setAlwaysRenderNameTag(true);
    }

    public void onDisable() {
        mc.thePlayer.setAlwaysRenderNameTag(false);
    }
}
