package com.newbie.core.event;

public interface EventHandler<T extends EventData> {
    void handle(T event);
}
