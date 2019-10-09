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

package com.newbie.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;


public class Mapper {
    public ModelMapper getModelMapper() {
        return modelMapper;
    }

    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    private static Mapper mapper;
    private ModelMapper modelMapper;
    private Mapper (){}
    public static synchronized <T> Mapper init() {
        if (mapper == null) {
            mapper = new Mapper();
            mapper.setModelMapper(new ModelMapper());
        }
        return mapper;
    }

    public <D> D map(Object source, Class<D> destinationType) {
        return mapper.map(source, destinationType);
    }


    public <S, D> ModelMapper customMappings(PropertyMap<S, D> propertyMap) {
        this.modelMapper.addMappings(propertyMap);
        return this.modelMapper;
    }

    public <D> D map(Object source, GenericsToken<D> destination) {
        return modelMapper.map(source, destination.getType());
    }
}
