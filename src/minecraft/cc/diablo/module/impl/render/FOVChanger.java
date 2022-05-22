package cc.diablo.module.impl.render;

import cc.diablo.event.impl.TickEvent;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.setting.impl.NumberSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import java.sql.Wrapper;

public class FOVChanger extends Module {
    public static NumberSetting fov = new NumberSetting("FOV",135,10,200,10);
    public static float oldFOV;
    public FOVChanger(){
        super("FOVChanger","Changes FOV", Keyboard.KEY_NONE, Category.Render);
        this.addSettings(fov);
    }

    @Override
    public void onEnable() {
        oldFOV = mc.gameSettings.fovSetting;
        super.onEnable();
    }

    @Override
    public void onDisable(){
        mc.gameSettings.fovSetting = oldFOV;
        super.onDisable();
    }

    @Subscribe
    public void onTick(TickEvent e){
        mc.gameSettings.fovSetting = (float) fov.getVal();
    }
}
