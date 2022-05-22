package cc.diablo.module.impl.render;

import cc.diablo.event.impl.UpdateEvent;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import com.google.common.eventbus.Subscribe;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.lwjgl.input.Keyboard;

public class FullBright extends Module {

    public float prevGamma;

    public FullBright() {
        super("FullBright", "FullBright", Keyboard.KEY_NONE, Category.Render);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Subscribe
    public void onUpdate(UpdateEvent e){
        this.setDisplayName("Full Bright");
        prevGamma = mc.gameSettings.gammaSetting;
        if(!mc.thePlayer.isPotionActive(Potion.nightVision.getId()))
            mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.getId(), (int) 1E159));
        mc.gameSettings.gammaSetting = 10000F;
    }
    @Override
    public void onDisable() {
        if(mc.thePlayer.isPotionActive(Potion.nightVision.getId()))
            mc.thePlayer.removePotionEffect(Potion.nightVision.getId());
        mc.gameSettings.gammaSetting = prevGamma;
        super.onDisable();
    }
}
