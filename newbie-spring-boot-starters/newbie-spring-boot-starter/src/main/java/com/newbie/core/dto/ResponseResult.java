package com.newbie.core.dto;

import com.newbie.core.exception.ResponseTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseResult<T> {
    private String code;
    private boolean success;
    private String message;
    private T data;

    public ResponseResult(ResponseTypes type, T data){
        this.success = type.getCode().equals("200");
        this.code = type.getCode();
        this.message = type.getDesc();
        this.data = data;
    }

    public ResponseResult(ResponseTypes type, String message, T data){
        this.success = type.getCode().equals("200");
        this.code = type.getCode();
        this.message = message;
        this.data = data;
    }

    public ResponseResult(boolean success, String code, String message, T data){
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
