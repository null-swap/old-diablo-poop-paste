package cc.diablo.event.impl;

import cc.diablo.event.Event;

public class LightningEvent extends Event {
    public double y;
    public double x;
    public double z;
    public LightningEvent(final double y,final double x, final double z) {
        this.y = y;
        this.z = z;
        this.x = x;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

}
