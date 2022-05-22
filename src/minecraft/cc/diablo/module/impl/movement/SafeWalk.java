package cc.diablo.module.impl.movement;

import cc.diablo.event.impl.SafeWalkEvent;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import com.google.common.eventbus.Subscribe;
import org.lwjgl.input.Keyboard;

public class SafeWalk extends Module {
    public SafeWalk(){
        super("Safe Walk","Don't Fall Off Edges", Keyboard.KEY_NONE, Category.Movement);
    }

    @Subscribe
    public void onSafewalk(SafeWalkEvent e){
        if(mc.thePlayer.onGround){
            e.setWalkSafely(true);
        }
    }
}

