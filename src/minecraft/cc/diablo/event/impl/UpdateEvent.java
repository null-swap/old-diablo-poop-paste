package cc.diablo.event.impl;

import cc.diablo.event.Event;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;

@Getter@Setter
public class UpdateEvent extends Event {
    private double posX, posY, posZ;
    private float rotationYaw, rotationPitch;
    private boolean onGround;
    private final boolean isPre;

    public UpdateEvent(double posX, double posY, double posZ, float rotationYaw, float rotationPitch, boolean onGround) {
        this(true);
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.rotationYaw = rotationYaw;
        this.rotationPitch = rotationPitch;
        this.onGround = onGround;
    }

    public UpdateEvent(boolean isPre) {
        this.isPre = isPre;
    }

    public double getX() {
        return posX;
    }

    public void setX(double posX) {
        this.posX = posX;
    }

    public double getY() {
        return posY;
    }

    public void setY(double posY) {
        this.posY = posY;
    }

    public double getZ() {
        return posZ;
    }

    public void setZ(double posZ) {
        this.posZ = posZ;
    }

    public float getYaw() {
        return rotationYaw;
    }

    public void setYaw(float rotationYaw) {
        Minecraft.getMinecraft().thePlayer.renderYawOffset = rotationYaw;
        Minecraft.getMinecraft().thePlayer.rotationYawHead = rotationYaw;
        this.rotationYaw = rotationYaw;
    }

    public void setRotationYawSilent(float rotationYaw) {
        this.rotationYaw = rotationYaw;
    }

    public float getPitch() {
        return rotationPitch;
    }

    public void setPitch(float rotationPitch) {
        Minecraft.getMinecraft().thePlayer.renderPitchHead = rotationPitch;
        this.rotationPitch = rotationPitch;
    }

    public void setRotationPitchSilent(float rotationPitch) {
        this.rotationPitch = rotationPitch;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public boolean isPre() {
        return isPre;
    }
}
