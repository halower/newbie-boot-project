package com.newbie.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @Author: 谢海龙
 * @Date: 2019/7/4 17:41
 * @Description
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {
        super();
    }
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public ResourceNotFoundException(String message) {
        super(message);
    }
    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }
}