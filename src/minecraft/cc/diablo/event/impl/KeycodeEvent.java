package cc.diablo.event.impl;

import cc.diablo.event.Event;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class KeycodeEvent extends Event {
    public int key;
    public KeycodeEvent(int key) {
        this.key = key;
    }
}
