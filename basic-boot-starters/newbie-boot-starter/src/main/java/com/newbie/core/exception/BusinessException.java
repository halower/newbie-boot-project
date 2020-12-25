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

package com.newbie.core.exception;

import com.newbie.dto.ResponseTypes;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class BusinessException extends RuntimeException {
    private ResponseTypes exceptionType;
    private String message;
    private String detail_message;

    public  BusinessException(ResponseTypes responseType) {
        super(responseType.getDesc());
        this.message = responseType.getDesc();
        this.exceptionType = responseType;
    }

    public BusinessException(String msg) {
        super(msg);
        this.message = msg;
        this.exceptionType = ResponseTypes.BUSSINESS_FAIL;
    }

    public BusinessException(ResponseTypes responseType, String msg) {
        super(msg);
        this.message = msg;
        this.exceptionType = responseType;
    }

    @Override
    public String toString() {
        return "com.newbie.core.exception.BusinessException{" +
                "\"code\":" + "\""+ this.exceptionType.getCode() +"\"," +
                "\"success\": \"false\"," +
                "\"message\":" +"\"" +this.message +"\"" +
                "}";
    }
}