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

package com.newbie.core.entity;

import java.io.Serializable;

public abstract class AggregateRoot <K extends Serializable> implements Serializable {
    /**
     * get aggregate root
     *
     * @return root object
     */
    public abstract K getRoot();

    /**
     * get entity id
     *
     * @return entity id
     */
    public K getId() {
        return getRoot();
    }
}