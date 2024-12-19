package com.mwsfot.common.utils;

import cn.hutool.json.JSONUtil;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.aliyun.teaopenapi.models.Config;
import com.mwsfot.common.constants.SmsConstants;
import com.mwsfot.system.common.core.domain.AjaxResult;
import java.util.Map;

/**
 * @author MinChang
 * @description 发送短信工具类
 * @date 2024/12/19 15:48
 */
public class SmsUtil {

    public Client client() throws Exception {
        Config config = new Config()
            .setAccessKeyId(System.getenv("ALIBABA_CLOUD_ACCESS_KEY_ID"))
            // 配置 AccessKey Secret，请确保代码运行环境设置了环境变量。
            .setAccessKeySecret(System.getenv("ALIBABA_CLOUD_ACCESS_KEY_SECRET"));
        // System.getenv()方法表示获取系统环境变量，请配置环境变量后，在此填入环境变量名称，不要直接填入AccessKey信息。
        // 配置 Endpoint
        config.endpoint = SmsConstants.Alia.ENDPOINT;

        return new Client(config);
    }

    /**
     * 发送短信逻辑
     * 第一种: 验证本人用, {validCode} 用于发送验证码 <br/>
     * 第二种: 扣款提醒, {Consumption} 消费金额 {accountBalance} 账户余额 <br/>
     * 第三种: 开户提醒, {accountBalance} 账户余额 <br/>
     * 第四种: 充值提醒, {Consumption} 充值金额 {accountBalance} 当前余额 <br/>
     *
     * @param phoneNumbers 手机号
     * @param params       对应模版参数
     * @return 发送是否成功
     */
    public AjaxResult sendSms(String phoneNumbers, String signName, String templateCode,
                              Map<String, String> params) {
        // 初始化请求客户端
        // 构造请求对象，请填入请求参数值
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
            .setPhoneNumbers(phoneNumbers)
            .setSignName(signName)
            .setTemplateCode(templateCode)
            .setTemplateParam(JSONUtil.toJsonStr(params));
        // 获取响应对象
        try {
            SendSmsResponse sendSmsResponse = client().sendSms(sendSmsRequest);
            SendSmsResponseBody body = sendSmsResponse.body;
            if ( body.code.equals(SmsConstants.Alia.SUCCESS_CODE) ) {
                return AjaxResult.success("验证码发送成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.error("验证码发送失败,请联系管理员");
    }

}
