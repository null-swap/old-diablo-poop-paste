package cc.diablo.module.impl.player;

import cc.diablo.Main;
import cc.diablo.manager.module.ModuleManager;
import cc.diablo.module.impl.combat.KillAura;
import cc.diablo.setting.impl.ModeSetting;
import cc.diablo.setting.impl.NumberSetting;
import com.google.common.eventbus.Subscribe;
import cc.diablo.event.impl.UpdateEvent;
import cc.diablo.helpers.player.KillAuraHelper;
import cc.diablo.helpers.MathHelper;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import org.lwjgl.input.Keyboard;

public class AntiAim extends Module {
    public ModeSetting watermarkMode = new ModeSetting("Mode","Simple","Simple","Spin");
    private NumberSetting pitch = new NumberSetting("Pitch",90,-90,90,1);
    private NumberSetting jitter = new NumberSetting("Jitter",90,-90,90,1);
    public AntiAim(){
        super("AntiAim", "autism", Keyboard.KEY_NONE, Category.Player);
        this.addSettings(watermarkMode,pitch,jitter);
    }


    @Subscribe
    public void onUpdate(UpdateEvent e){
        if(KillAura.target == null && !(ModuleManager.getModule(Scaffold.class).isToggled()) && !mc.gameSettings.keyBindUseItem.pressed) {
            switch (watermarkMode.getMode()) {
                case "Simple":
                    KillAuraHelper.setRotations(e, mc.thePlayer.rotationYaw - 180 + MathHelper.getRandInt((int) -jitter.getVal(), (int) jitter.getVal()), (float) pitch.getVal());
                break;
                case "Spin":
                    KillAuraHelper.setRotations(e, mc.thePlayer.ticksExisted * 80, (float) pitch.getVal());
                break;
            }
        }
    }
}
