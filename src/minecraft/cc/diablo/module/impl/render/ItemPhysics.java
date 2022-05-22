package cc.diablo.module.impl.render;

import cc.diablo.module.Category;
import cc.diablo.module.Module;
import org.lwjgl.input.Keyboard;

public class ItemPhysics extends Module {
    public ItemPhysics(){
        super("ItemPhysics","Changes the physics of dropped stuff", Keyboard.KEY_NONE, Category.Render);
    }
}
