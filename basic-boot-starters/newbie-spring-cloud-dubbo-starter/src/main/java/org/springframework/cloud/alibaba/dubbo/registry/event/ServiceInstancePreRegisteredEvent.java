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

package org.springframework.cloud.alibaba.dubbo.registry.event;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.context.ApplicationEvent;

/**
 * The before-{@link ServiceRegistry#register(Registration) register} event for {@link ServiceInstance}
 *
 * @Author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
public class ServiceInstancePreRegisteredEvent extends ApplicationEvent {

    public ServiceInstancePreRegisteredEvent(Registration source) {
        super(source);
    }

    @Override
    public Registration getSource() {
        return (Registration) super.getSource();
    }
}
