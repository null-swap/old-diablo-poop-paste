package cc.diablo.module.impl.combat;

import cc.diablo.event.impl.SlowdownEvent;
import cc.diablo.helpers.MathHelper;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.setting.impl.ModeSetting;
import com.google.common.eventbus.Subscribe;
import optifine.MathUtils;
import org.lwjgl.input.Keyboard;

public class KeepSprint extends Module {
    public ModeSetting mode = new ModeSetting("Mode","Normal","Normal","Invaded");

    public KeepSprint() {
        super("KeepSprint", "Anti Slowdown when hitting.", Keyboard.KEY_NONE, Category.Combat);
        this.addSettings(mode);
    }

    @Subscribe
    public void onSlowdown(SlowdownEvent e){
        e.setCancelled(true);
        switch (mode.getMode()){
            case "Normal":
                e.setCancelled(true);
                break;
            case "Invaded":
                e.setCancelled(true);
                mc.thePlayer.motionX = mc.thePlayer.motionX * MathHelper.getRandomInRange(0.9375f, .95f);
                mc.thePlayer.motionZ = mc.thePlayer.motionZ * MathHelper.getRandomInRange(0.9375f, .95f);
                break;
        }
    }
}
