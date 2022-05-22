package cc.diablo.helpers.player;

import cc.diablo.event.impl.MoveRawEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;

public class MoveUtils
{
    public static float getDirection() {
        float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
        final float forward = Minecraft.getMinecraft().thePlayer.moveForward;
        final float strafe = Minecraft.getMinecraft().thePlayer.moveStrafing;
        yaw += ((forward < 0.0f) ? 180 : 0);
        if (strafe < 0.0f) {
            yaw += ((forward < 0.0f) ? -45 : ((forward == 0.0f) ? 90 : 45));
        }
        if (strafe > 0.0f) {
            yaw -= ((forward < 0.0f) ? -45 : ((forward == 0.0f) ? 90 : 45));
        }
        return yaw * 0.017453292f;
    }

    public static void setMoveSpeed(MoveRawEvent moveEvent, double moveSpeed) {
        float rotationYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
        MovementInput movementInput = Minecraft.getMinecraft().thePlayer.movementInput;
        double pseudoStrafe = MovementInput.moveStrafe;
        MovementInput movementInput2 = Minecraft.getMinecraft().thePlayer.movementInput;
        setMoveSpeed(moveEvent, moveSpeed, rotationYaw, pseudoStrafe, MovementInput.moveForward);
    }

    public static void setMoveSpeed(MoveRawEvent moveEvent, double moveSpeed, float pseudoYaw, double pseudoStrafe, double pseudoForward) {
        double forward = pseudoForward;
        double strafe = pseudoStrafe;
        float yaw = pseudoYaw;
        if (forward != 0.0) {
            if (strafe > 0.0) {
                yaw += ((forward > 0.0) ? -45 : 45);
            }
            else if (strafe < 0.0) {
                yaw += ((forward > 0.0) ? 45 : -45);
            }
            strafe = 0.0;
            if (forward > 0.0) {
                forward = 1.0;
            }
            else if (forward < 0.0) {
                forward = -1.0;
            }
        }
        if (strafe > 0.0) {
            strafe = 1.0;
        }
        else if (strafe < 0.0) {
            strafe = -1.0;
        }
        double mx = Math.cos(Math.toRadians(yaw + 90.0f));
        double mz = Math.sin(Math.toRadians(yaw + 90.0f));
        moveEvent.x = forward * moveSpeed * mx + strafe * moveSpeed * mz;
        moveEvent.z = forward * moveSpeed * mz - strafe * moveSpeed * mx;
    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = Minecraft.getMinecraft().thePlayer.capabilities.getWalkSpeed() * 2.875;
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSlowdown)) {
            baseSpeed /= 1.0 + 0.15 * (Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSlowdown).getAmplifier() + 1);
        }
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            baseSpeed *= 1.0 + 0.3 * (Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return baseSpeed;
    }

    public static double getVerusBaseMoveSpeed() {
        float speed = 0.0f;
        float speedPlus = 0.0f;
        speed = 0.24f;
        speedPlus = 0.0725f;
        float baseSpeed = speed;
        if(Minecraft.getMinecraft().thePlayer.hurtTime != 0){
            baseSpeed *= 2.5f;
        } else {
            if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
                final int amp = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
                baseSpeed *= 1.0f + speedPlus * (amp + 0);
            }
        }
        return baseSpeed;
    }

    public static double getSpeedModifier(double speed) {
        double baseSpeed = speed;
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            baseSpeed *= 1.0 + 0.15 * (Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier());
        }
        return baseSpeed;
    }

    public static double getJumpBoostModifier(double baseJumpHeight) {
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.jump)) {
            int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.jump).getAmplifier();
            baseJumpHeight += (amplifier + 1) * 0.1f;
        }
        return baseJumpHeight;
    }
}
