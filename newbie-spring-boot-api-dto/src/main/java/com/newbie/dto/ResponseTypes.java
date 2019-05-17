package com.newbie.dto;


public enum ResponseTypes {
    SUCCESS("0", "操作成功"),
    FAIL("1000", "操作失败"),
    PARAMETER_UNVALID("1001", "参数验证失败"),
    USER_PASSWORD_ERROR("1002", "用户名或者密码错误"),
    TOKEN_UNVALID("1003","token缺失或者无效"),
    BUSSINESS_FAIL("1004","业务执行失败"),
    FILE_DOWN_FAIL("1005", "文件下载服务异常"),
    File_SAVE_FAIL("1006","存储服务执行失败"),
    OBJECT_NOT_FOUND("1007","所查找的对象不存在"),
    FILE_DIR_NOT_FOUND("1008","文件或目录不存在"),
    BUCKET_CREATE_FAIL("1009","创建BUCKET失败"),
    FILE_DELETE_FAIL("1010", "删除文件失败"),
    USER_NOET_FOUND("1011", "查找用户不存在"),
    PASSWPRD_CHANGE_FIAL("1012", "密码修改失败"),
    CREATE_FAIL("1013","创建资源服务执行失败"),
    READ_FAIL("1014","查询资源服务执行失败"),
    UPDATE_FAIL("1015","更新资源服务执行失败"),
    DELETE_FAIL("1016","删除资源服务执行失败"),
    REMOTE_CALL_FAIL("1017","远程服务调用失败"),
    MESSAGE_SEND_FAIL("1018","消息发送服务执行失败"),
    INVALID_DATA("1019", "数据不完整或数据之间矛盾"),
    METHOD_NOT_ALLOWED("1020","方法不允许操作"),
    TIMEOUT("1021","操作超时"),
    UNAUTHORIZED("1022","当前操作未授权"),
    UNKNOW("1030","服务器内部错误,请联系管理员");

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