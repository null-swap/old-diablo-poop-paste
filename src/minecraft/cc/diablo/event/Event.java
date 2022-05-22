package cc.diablo.event;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public abstract class Event {
    public boolean cancelled;
    private EventType type;
    public boolean isPre() {
        return type == EventType.Pre;
    }
    public boolean isPost() {
        return type == EventType.Post;
    }

    public void setCancelled(boolean bool){
        this.cancelled = bool;
    }
}
