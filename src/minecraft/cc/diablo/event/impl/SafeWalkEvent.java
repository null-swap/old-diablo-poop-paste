package cc.diablo.event.impl;

import cc.diablo.event.Event;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.Packet;

@Getter@Setter
public class SafeWalkEvent extends Event {
    public boolean walkSafely;

    public SafeWalkEvent(boolean walkSafely) {
        this.walkSafely = walkSafely;
    }

    public boolean isWalkSafely() {
        return walkSafely;
    }
}
