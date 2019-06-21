package com.newbie.core.exception;

import com.newbie.dto.ResponseTypes;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BusinessException extends RuntimeException {
    private ResponseTypes exceptionType;

    public  BusinessException(ResponseTypes responseType) {
        super(responseType.getDesc());
        this.exceptionType = responseType;
    }

    public BusinessException(String msg) {
        super(msg);
        this.exceptionType = ResponseTypes.BUSSINESS_FAIL;
    }

    /**
     * 提高性能
     * @return Throwable
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}