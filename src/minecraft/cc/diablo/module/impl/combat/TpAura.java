package cc.diablo.module.impl.combat;

import cc.diablo.module.Category;
import cc.diablo.module.Module;
import org.lwjgl.input.Keyboard;

public class TpAura extends Module {

    public TpAura() {
        super("Tp Aura","Aura but tp :)", Keyboard.KEY_NONE, Category.Combat);
    }


}
