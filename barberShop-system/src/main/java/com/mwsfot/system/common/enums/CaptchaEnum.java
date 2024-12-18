package com.mwsfot.system.common.enums;

/**
 * @author MinChang
 * @description 验证码枚举
 * @date 2023/9/12 10:33
 */
public enum CaptchaEnum {

    SUCCESS("0000", "校验成功"),
    PARAM_ERROR("0011", "参数不能为空"),
    EXPIRED("6110", "验证码已失效,请重新获取"),
    CHECK_ERROR("6111", "验证失败"),
    GET_ERROR("6112", "获取验证码失败"),
    IMAGE_ERROR("6113", "底图初始化失败,请联系管理员"),
    GET_LIMIT("6201", "获取验证码频繁,请稍后再试"),
    INVALID("6206", "无效请求, 请重新获取验证码"),
    ERROR_LIMIT("6202", "验证频繁,请稍后再试"),
    CHECK_LIMIT("6204", "校验频繁,请稍后再试"),

    ERROR("9999", "服务器异常"),
    ;
    private final String code;
    private final String msg;

    CaptchaEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public String getCode() {
        return code;
    }

    public static String getMsg(String code) {
        for (CaptchaEnum value : values()) {
            if ( value.code.equals(code) ) {
                return value.msg;
            }
        }
        return ERROR.msg;
    }
}
