package cc.diablo.module.impl.movement;

import cc.diablo.manager.module.ModuleManager;
import cc.diablo.module.impl.combat.KillAura;
import cc.diablo.module.impl.player.Scaffold;
import cc.diablo.setting.impl.ModeSetting;
import com.google.common.eventbus.Subscribe;
import cc.diablo.event.impl.UpdateEvent;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import org.lwjgl.input.Keyboard;

public class Sprint extends Module {
    //public ModeSetting sprintMode = new ModeSetting("Sprint Mode","Omni","Omni","Legit");

    public Sprint() {
        super("Sprint", "Automatic Sprinting!", Keyboard.KEY_NONE, Category.Movement);
        //this.addSettings(sprintMode);
    }

    @Override
    public void onDisable() {
        //mc.gameSettings.keyBindSprint.pressed = false;
        //mc.thePlayer.setSprinting(false);
        super.onDisable();
    }

    @Subscribe
    public void onUpdate(UpdateEvent e) {
        if(!ModuleManager.getModuleByName("Scaffold").toggled)
        mc.gameSettings.keyBindSprint.pressed = true;
        else
            mc.gameSettings.keyBindSprint.pressed = false;
        /*if (!(mc.thePlayer.isOnLadder() && mc.thePlayer.isSneaking() && mc.thePlayer.isSprinting() && mc.thePlayer.isPlayerSleeping() && mc.thePlayer == null && KillAura.stopSprint.isChecked()))
            switch (sprintMode.getMode()) {
                case "Omni":
                    if (mc.thePlayer.isMoving()) {
                        mc.thePlayer.setSprinting(true);
                    }
                    break;
                case "Legit":
                    break;
            }*/
    }
}
