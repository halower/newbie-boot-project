package com.newbie.core.dto;

import lombok.var;
import org.modelmapper.ModelMapper;

import java.lang.reflect.Type;


public interface InputDTO<T> extends DTO {
    default T map(Type ofType) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(this, ofType);
    }
}
