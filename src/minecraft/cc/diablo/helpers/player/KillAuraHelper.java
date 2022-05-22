package cc.diablo.helpers.player;

import cc.diablo.event.impl.UpdateEvent;
import cc.diablo.helpers.PacketHelper;
import cc.diablo.manager.friend.FriendManager;
import cc.diablo.manager.module.ModuleManager;
import cc.diablo.module.impl.combat.KillAura;
import cc.diablo.module.impl.player.Scaffold;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;

public class KillAuraHelper {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static EntityLivingBase getClosestEntity(double range) {
        double dist = range;
        EntityLivingBase target = null;
        for (Entity entity : Minecraft.theWorld.loadedEntityList) {
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase player = (EntityLivingBase) entity;
                if (canAttack(player)) {
                    if(!FriendManager.isFriend(entity.getName())){
                        double currentDist = mc.thePlayer.getDistanceToEntity(player);
                        if (currentDist <= dist) {
                            dist = currentDist;
                            target = player;
                        }
                    }
                }
            }
        }
        return target;
    }

    public static EntityLivingBase getClosestPlayerEntity(double range) {
        double dist = range;
        EntityLivingBase target = null;
        for (EntityPlayer object : Minecraft.theWorld.playerEntities) {
            if (canAttack(object)) {
                if(!FriendManager.isFriend(object.getName())){
                    double currentDist = mc.thePlayer.getDistanceToEntity(object);
                    if (currentDist <= dist) {
                        dist = currentDist;
                        target = object;
                    }
                }
            }
        }
        return target;
    }

    public static EntityLivingBase getHealth(double range) {
        double health = range;
        EntityLivingBase target = null;
        for (Entity object : Minecraft.theWorld.loadedEntityList) {
            if (object instanceof EntityLivingBase) {
                EntityLivingBase player = (EntityLivingBase) object;
                if (canAttack(player)) {
                    if(!FriendManager.isFriend(object.getName())){
                        double currentHealth = player.getHealth();
                        if (currentHealth <= health) {
                            health = currentHealth;
                            target = player;
                        }
                    }
                }
            }
        }
        return target;
    }
    public static float[] getAngles(Entity e) {
        return new float[] { getYawChangeToEntity(e) + mc.thePlayer.rotationYaw, getPitchChangeToEntity(e) + mc.thePlayer.rotationPitch };
    }

    public static float getYawChangeToEntity(Entity entity) {
        double deltaX = entity.posX - mc.thePlayer.posX;
        double deltaZ = entity.posZ - mc.thePlayer.posZ;
        double yawToEntity;
        if (deltaZ < 0.0 && deltaX < 0.0) {
            yawToEntity = 90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
        }
        else if (deltaZ < 0.0 && deltaX > 0.0) {
            yawToEntity = -90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
        }
        else {
            yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
        }
        return Double.isNaN(mc.thePlayer.rotationYaw - yawToEntity) ? 0.0f : MathHelper.wrapAngleTo180_float(-(mc.thePlayer.rotationYaw - (float)yawToEntity));
    }


    public static float getPitchChangeToEntity(Entity entity) {
        double deltaX = entity.posX - mc.thePlayer.posX;
        double deltaZ = entity.posZ - mc.thePlayer.posZ;
        double deltaY = entity.posY - 1.6 + entity.getEyeHeight() - mc.thePlayer.posY;
        double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
        double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
        return Double.isNaN(mc.thePlayer.rotationPitch - pitchToEntity) ? 0.0f : -MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationPitch - (float)pitchToEntity);
    }

    public static void setRotations(UpdateEvent e, float yaw, float pitch) {
        e.setYaw(yaw);
        e.setPitch(pitch);
        mc.thePlayer.rotationYawHead = yaw;
        mc.thePlayer.rotationPitchHead = pitch;
        mc.thePlayer.renderYawOffset = yaw;
    }

    public static void setRotationsSilent(UpdateEvent e, float yaw, float pitch) {
        e.setRotationYawSilent(yaw);
        e.setRotationPitchSilent(pitch);
    }

    public static void setYaw(UpdateEvent e, float yaw) {
        e.setYaw(yaw);
        mc.thePlayer.rotationYawHead = yaw;
        //mc.thePlayer.renderYawOffset = yaw;
    }

    public static void setPitch(UpdateEvent e, float pitch) {
        e.setPitch(pitch);
        mc.thePlayer.rotationPitchHead = pitch;
    }

    public static void setRotationsPacket(float yaw, float pitch) {
        mc.thePlayer.rotationYawHead = yaw;
        mc.thePlayer.rotationPitchHead = pitch;
        mc.thePlayer.renderYawOffset = yaw;
        PacketHelper.sendPacketNoEvent(new C03PacketPlayer.C05PacketPlayerLook(yaw,pitch,mc.thePlayer.onGround));
    }

    public static EntityLivingBase getTarget() {
        return KillAura.target;
    }
    public static boolean canAttack(Entity entity) {
        if(entity.ticksExisted > KillAura.ticks.getVal()) {
            if ((!entity.isInvisible() || KillAura.targetInvis.isChecked()) && !ModuleManager.getModule(Scaffold.class).isToggled()) {
                return entity != mc.thePlayer && entity.isEntityAlive() && mc.thePlayer != null && Minecraft.theWorld != null && mc.thePlayer.ticksExisted > 30 && entity.ticksExisted > 15;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static float[] getPredictedRotations(EntityLivingBase ent) {
        double x = ent.posX + (ent.posX - ent.lastTickPosX);
        double z = ent.posZ + (ent.posZ - ent.lastTickPosZ);
        double y = ent.posY + (double)(ent.getEyeHeight() / 2.0F);
        return getRotationFromPosition(x, z, y);
    }
    public static float[] getRotationFromPosition(double x, double z, double y) {
        double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
        double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
        double yDiff = y - Minecraft.getMinecraft().thePlayer.posY - 1.2D;
        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
        float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D));
        return new float[]{yaw, pitch};
    }
    public static float getSensitivityMultiplier() {
        float f = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6F + 0.2F;
        return (f * f * f * 8.0F) * 0.15F;
    }
}