package com.mwsfot.common.constants;

/**
 * @author MinChang
 * @description 短信常量
 * @date 2024/12/19 15:30
 */
public interface SmsConstants {
    interface Alia {
        String SUCCESS_CODE = "OK";
        /**
         * 成功情况下 status_code 编码
         */
        Integer SUCCESS_STATUS_CODE = 200;
        /**
         * 验证码模版CODE
         */
        String VALID_PARAM_TEMPLATE_CODE = "xxxxx";

        /**
         * 验证码模版对应参数名
         */
        String VALID_PARAM_TEMPLATE_NAME = "validCode";
        /**
         * 验证码短信签名
         */
        String VALID_SIGN_NAME = "xxxxx";

        String ENDPOINT = "dysmsapi.aliyuncs.com";

    }
}
