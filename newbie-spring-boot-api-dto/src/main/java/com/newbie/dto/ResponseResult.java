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

package com.newbie.dto;

public class ResponseResult<T> implements DTO {
    private String code;
    private boolean success;
    private String message;
    private T data;

    private static ResponseResult ourInstance = new ResponseResult();

    public static ResponseResult getInstance() {
        return ourInstance;
    }

    public ResponseResult(){}
    /**
     * 操作成功
     * @param data 返回值
     */
    public ResponseResult(T data){
        if(data instanceof ResponseTypes) {
            boolean success = data == ResponseTypes.SUCCESS;
            this.success = success;
            this.code = success? ResponseTypes.SUCCESS.getCode(): ((ResponseTypes) data).getCode();
            this.message =success? ResponseTypes.SUCCESS.getDesc(): ((ResponseTypes) data).getDesc();
        }

        else  {
            this.success = true;
            this.code = ResponseTypes.SUCCESS.getCode();
            this.message =ResponseTypes.SUCCESS.getDesc();
            this.data = data;
        }
    }


    /**
     * 操作成功
     * @param data 返回值
     * @param message 返回信息
     */
    public ResponseResult(String message, T data){
        this.success = true;
        this.code = ResponseTypes.SUCCESS.getCode();
        this.message =message;
        this.data = data;
    }

    /**
     * 无数据返回操作相应
     * @param type 响应类型
     * @param message 消息
     */
    public ResponseResult(ResponseTypes type, String message){
        this.success = type.equals(ResponseTypes.SUCCESS);
        this.code = type.getCode();
        this.message = message;
        this.data = null;
    }

    /**
     * 操作相应
     * @param type 响应类型
     * @param message 消息
     * @param data 返回值
     */
    public ResponseResult(ResponseTypes type, String message, T data){
        this.success = type.equals(ResponseTypes.SUCCESS);
        this.code = type.getCode();
        this.message = message;
        this.data = data;
    }

    /**
     * 自定义返回类型
     * @param success 是否成功
     * @param code 业务代码
     * @param message 消息
     * @param data 返回值
     */
    public ResponseResult(Boolean success, String code, String message, T data){
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static ResponseResult builder() {
        return getInstance();
    }

    public  ResponseResult success(T data) {
        this.success = true;
        this.code = ResponseTypes.SUCCESS.getCode();
        this.data = data;
        return this;
    }

    public ResponseResult error(T data) {
        this.success = false;
        this.code = ResponseTypes.FAIL.getCode();
        this.data = data;
        return this;
    }

    public ResponseResult message(String message) {
        this.message = message;
        return this;
    }
}
