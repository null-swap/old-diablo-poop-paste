package cc.diablo.module.impl.ghost;

import com.google.common.eventbus.Subscribe;
import cc.diablo.event.impl.PacketEvent;
import cc.diablo.event.impl.UpdateEvent;
import cc.diablo.helpers.player.KillAuraHelper;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.setting.impl.BooleanSetting;
import cc.diablo.setting.impl.NumberSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.MathHelper;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import optifine.MathUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class AimAssistance extends Module {
    private EntityLivingBase target;
    public NumberSetting speedAiming = new NumberSetting("Speed",2,0.5,6,0.1);
    public NumberSetting jitterRandom = new NumberSetting("RandomVal",1.5,0,3,0.25);
    public NumberSetting aimingDistance = new NumberSetting("Distance",5,0.5,10,0.5);
    public BooleanSetting rot_pitch = new BooleanSetting("Pitch",true);
    public BooleanSetting rot_yaw = new BooleanSetting("Yaw",true);
    public BooleanSetting lcCheck = new BooleanSetting("Click Aim",true);

    public AimAssistance(){
        super("AimAssist", "Made by Lxve <3", Keyboard.KEY_NONE, Category.Combat);
        this.addSettings(speedAiming,jitterRandom,aimingDistance,rot_pitch,rot_yaw,lcCheck);
    }

    @Subscribe
    public void onUpdate(UpdateEvent e){
        this.setDisplayName("AimAssist\2477");
        target = KillAuraHelper.getClosestEntity(this.aimingDistance.getVal());
        float random = jitterRandom.getVal() != 0 ? getRandomInRange((float) -jitterRandom.getVal(), (float) jitterRandom.getVal()) : 0;
        if (!lcCheck.isChecked() || Mouse.isButtonDown(0))
            if (target != null && mc.currentScreen == null) {
                if (rot_yaw.isChecked())
                    mc.thePlayer.rotationYaw = (getRotationsAimA(target, (float) (speedAiming.getVal() * 3D) + random)[0]) + random;
                if (rot_pitch.isChecked())
                    mc.thePlayer.rotationPitch = (getRotationsAimA(target, (float) (speedAiming.getVal() * 3D) + random)[1]) + random;
            }
    }

    public static float[] getRotationsAimA(EntityLivingBase entityIn, float speed) {
        float yaw = updateRotation(Minecraft.getMinecraft().thePlayer.rotationYaw,
                getNeededRotations(entityIn)[0],
                speed);
        float pitch = updateRotation(Minecraft.getMinecraft().thePlayer.rotationPitch,
                getNeededRotations(entityIn)[1],
                speed);
        return new float[]{yaw, pitch};
    }
    private static float updateRotation(float currentRotation, float intendedRotation, float increment) {
        float f = MathHelper.wrapAngleTo180_float(intendedRotation - currentRotation);

        if (f > increment)
            f = increment;

        if (f < -increment)
            f = -increment;

        return currentRotation + f;
    }
    public float getAngleChange(EntityLivingBase entityIn) {
        float yaw = getNeededRotations(entityIn)[0];
        float pitch = getNeededRotations(entityIn)[1];
        float playerYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
        float playerPitch = Minecraft.getMinecraft().thePlayer.rotationPitch;
        if (playerYaw < 0)
            playerYaw += 360;
        if (playerPitch < 0)
            playerPitch += 360;
        if (yaw < 0)
            yaw += 360;
        if (pitch < 0)
            pitch += 360;
        float yawChange = Math.max(playerYaw, yaw) - Math.min(playerYaw, yaw);
        float pitchChange = Math.max(playerPitch, pitch) - Math.min(playerPitch, pitch);
        return yawChange + pitchChange;
    }
    public static float getRandomInRange(float min, float max) {
        Random random = new Random();
        float range = max - min;
        float scaled = random.nextFloat() * range;
        return scaled + min;
    }
    public static float[] getNeededRotations(EntityLivingBase entityIn) {
        double d0 = entityIn.posX - Minecraft.getMinecraft().thePlayer.posX;
        double d1 = entityIn.posZ - Minecraft.getMinecraft().thePlayer.posZ;
        double d2 = entityIn.posY + entityIn.getEyeHeight() - (Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().minY
                + (Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().maxY - Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().minY));
        double d3 = MathHelper.sqrt_double(d0 * d0 + d1 * d1);
        float f = (float) (MathHelper.func_181159_b(d1, d0) * 180.0D / Math.PI) - 90.0F;
        float f1 = (float) (-(MathHelper.func_181159_b(d2, d3) * 180.0D / Math.PI));
        return new float[] { f, f1 };
    }
}