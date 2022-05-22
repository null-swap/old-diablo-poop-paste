package cc.diablo.module.impl.ghost;

import cc.diablo.event.impl.UpdateEvent;
import cc.diablo.helpers.Stopwatch;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.setting.impl.NumberSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class AutoClicker extends Module {
    public NumberSetting minCPS = new NumberSetting("Min CPS",9,1,20,1);
    public NumberSetting maxCPS = new NumberSetting("Max CPS",14,1,20,1);
    private final Stopwatch timer = new Stopwatch();

    public AutoClicker(){
        super("AutoClicker","Automatically click for you", Keyboard.KEY_NONE, Category.Combat);
        this.addSettings(minCPS,maxCPS);
    }

    @Subscribe
    public void onUpdate(UpdateEvent e) {
        if (Minecraft.getMinecraft().currentScreen == null && Mouse.isButtonDown(0)) {
            if (timer.hasReached(1000 / RandomUtils.nextInt((int) minCPS.getVal(), (int) maxCPS.getVal()))) {
                KeyBinding.setKeyBindState(-100, true);
                KeyBinding.onTick(-100);
                timer.reset();
            } else {
                KeyBinding.setKeyBindState(-100, false);
            }
        }
    }
}
