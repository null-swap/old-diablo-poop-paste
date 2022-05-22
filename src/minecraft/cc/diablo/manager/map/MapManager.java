package cc.diablo.manager.map;

import java.util.*;

public abstract class MapManager<T, U>
{
    protected Map<T, U> contents;

    public final Map<T, U> getContents() {
        return this.contents;
    }

    public abstract void setup();
}