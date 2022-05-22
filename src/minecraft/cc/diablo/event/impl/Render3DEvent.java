package cc.diablo.event.impl;

import cc.diablo.event.Event;
import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class Render3DEvent extends Event {
    private final float partialTicks;

    public Render3DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
