package cc.diablo.event.impl;

import cc.diablo.event.Event;

public class SlowdownEvent extends Event {
    public Type type;

    public SlowdownEvent(final Type type) {
        this.type = type;
    }

    public enum Type {
        Sprinting
    }
}
