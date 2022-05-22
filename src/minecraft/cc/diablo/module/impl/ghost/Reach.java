package cc.diablo.module.impl.ghost;

import cc.diablo.module.Category;
import cc.diablo.module.Module;
import org.lwjgl.input.Keyboard;

public class Reach extends Module {
    public Reach(){
        super("Reach","Extends the player's reach", Keyboard.KEY_NONE, Category.Combat);
    }
}
