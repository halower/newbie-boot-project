/*
 * Apache License
 *
 * Copyright (c) 2019  halower (halower@foxmail.com).
 *
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.newbie.core.event;

import com.google.common.collect.Lists;
import lombok.var;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class EventBus {
    private static ConcurrentHashMap<Class<? extends EventData>, List<EventHandler<? extends EventData>>> handlerMap = new ConcurrentHashMap<>();

    public synchronized static <T extends EventData> void register(Class<T> domainEventClazz, EventHandler<T> handler) {
        var eventHandlers = handlerMap.get(domainEventClazz);
        if (eventHandlers == null) {
            eventHandlers = Lists.newArrayList();
        }
        eventHandlers.add(handler);
        handlerMap.put(domainEventClazz, eventHandlers);
    }

    @SuppressWarnings("unchecked")
    public static <T extends EventData> void trigger(final T domainEvent) {
        if (domainEvent == null) {
            throw new IllegalArgumentException("事件参数不能为空");
        }
        List<EventHandler<? extends EventData>> handlers = handlerMap.get(domainEvent.getClass());
        if (handlers != null && !handlers.isEmpty()) {
            for (EventHandler handler : handlers) {
                handler.handle(domainEvent);
            }
        }
    }
}
