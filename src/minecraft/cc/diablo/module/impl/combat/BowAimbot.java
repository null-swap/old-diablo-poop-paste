package cc.diablo.module.impl.combat;

import cc.diablo.event.impl.UpdateEvent;
import cc.diablo.helpers.MathHelper;
import cc.diablo.helpers.player.EntityHelper;
import cc.diablo.helpers.player.KillAuraHelper;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.setting.impl.NumberSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.input.Keyboard;

public class BowAimbot extends Module {
    public NumberSetting range = new NumberSetting("Range",40,10,100,5);

    public BowAimbot(){
        super("BowAimbot","Aims at jews for u", Keyboard.KEY_NONE, Category.Combat);
        this.addSettings(range);
    }

    @Subscribe
    public void onUpdate(UpdateEvent e) {
        EntityLivingBase target = null;
        target = KillAuraHelper.getClosestEntity(this.range.getVal());

        float[] rotations = EntityHelper.getAngles(target);
        float rot0 = (float) (rotations[0] + MathHelper.randomNumber(1, 2));
        float rot1 = rotations[1] + MathHelper.getRandom();

        if (target != null) {
            KillAuraHelper.setRotations(e,rot0,rot1);
        }
    }
}
