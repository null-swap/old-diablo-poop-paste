package cc.diablo.event.impl;

import cc.diablo.event.Event;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.Packet;

@Getter@Setter
public class PacketEvent extends Event {
    public Packet packet;
    public DirectionType dir;
    public PacketEvent(Packet packet, DirectionType dir) {
        this.packet = packet;
        this.dir = dir;
    }
    public boolean isIncoming() {
        return dir == DirectionType.Incoming;
    }
    public boolean isOutgoing() {
        return dir == DirectionType.Outgoing;
    }
    public enum DirectionType {
        Incoming, Outgoing
    }
}
