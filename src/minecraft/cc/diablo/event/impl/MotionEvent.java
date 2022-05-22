package cc.diablo.event.impl;

import cc.diablo.event.Event;
import cc.diablo.event.EventType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MotionEvent extends Event {
    public EventType type;
    public float yaw;
    public float pitch;
    public double y;
    public double x;
    public double z;
    public boolean onground;
    public boolean alwaysSend;
    public boolean cancelled;

    public MotionEvent(final double y,final double x, final double z, final float yaw, final float pitch, final boolean ground, EventType type) {
        this.type = type;
        this.cancelled = false;
        this.yaw = yaw;
        this.pitch = pitch;
        this.y = y;
        this.z = z;
        this.x = x;
        this.onground = ground;
    }

    public MotionEvent() {
        this.type = EventType.Post;
    }

    public EventType getType() {
        return this.type;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public double getY() {
        return this.y;
    }

    public void setY(final double y) {
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public void setX(final double x) {
        this.x = y;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(final double z) {
        this.y = z;
    }

    public boolean isOnground() {
        return this.onground;
    }

    public boolean shouldAlwaysSend() {
        return this.alwaysSend;
    }

    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(final float pitch) {
        this.pitch = pitch;
    }

    public void setGround(final boolean ground) {
        this.onground = ground;
    }

    public void setAlwaysSend(final boolean alwaysSend) {
        this.alwaysSend = alwaysSend;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(final boolean state) {
        this.cancelled = state;
    }
}