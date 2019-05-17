package com.newbie.dto;

public class ResponseResult<T> implements DTO {
    private String code;
    private boolean success;
    private String message;
    private T data;

    public ResponseResult(){}
    /**
     * 操作成功
     * @param data 返回值
     */
    public ResponseResult(T data){
        if(!(data instanceof ResponseTypes)) {
            this.success = true;
            this.code = ResponseTypes.SUCCESS.getCode();
            this.message =ResponseTypes.SUCCESS.getDesc();
            this.data = data;
        } else {
            boolean success = data == ResponseTypes.SUCCESS;
            this.success = success;
            this.code = success? ResponseTypes.SUCCESS.getCode(): ((ResponseTypes) data).getCode();
            this.message =success? ResponseTypes.SUCCESS.getDesc(): ((ResponseTypes) data).getDesc();
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
}
