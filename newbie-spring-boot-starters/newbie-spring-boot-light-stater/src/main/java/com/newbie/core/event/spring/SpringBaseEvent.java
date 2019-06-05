package com.newbie.core.event.spring;

import org.springframework.context.ApplicationEvent;

public abstract class SpringBaseEvent<T> extends ApplicationEvent {
    protected T eventData;

    public SpringBaseEvent(Object source, T eventData){
        super(source);
        this.eventData = eventData;
    }

    public SpringBaseEvent(T eventData){
        super(eventData);
    }

    public T getEventData() {
        return eventData;
    }
    public void setEventData(T eventData) {
        this.eventData = eventData;
    }
}