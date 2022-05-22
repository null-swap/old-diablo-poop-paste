package cc.diablo.event.impl;

import cc.diablo.event.Event;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MouseEvent extends Event {
    public double x;
    public double y;
    public int button;
    public MouseEvent(final double x, final double y, final int button) {
        this.x = x;
        this.y = y;
        this.button = button;
    }
}
