package com.newbie.core.exception;


public enum ResponseTypes {
    SUCCESS("200", "操作成功"),
    FAIL("1000", "操作失败"),
    PARAMETER_UNVALID("1001", "参数验证失败"),
    USER_PASSWORD_ERROR("1002", "用户名或者密码错误"),
    TOKEN_UNVALID("1003","token缺失或者无效"),
    BUSSINESS_FAIL("1004","业务服务异常"),
    FILE_DOWN_FAIL("1005", "文件下载异常"),
    File_SAVE_FAIL("1006","存储服务执行失败"),
    OBJECT_NOT_FOUND("1007","所查找的对象不存在"),
    FILE_DIR_NOT_FOUND("1008","文件或目录不存在"),
    BUCKET_CREATE_FAIL("1009","创建水桶失败"),
    FILE_DELETE_FAIL("1010", "文件下载异常"),
    UNKNOW("1011","服务器内部错误,请联系管理员");

    private String code;
    private String desc;

    ResponseTypes(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}