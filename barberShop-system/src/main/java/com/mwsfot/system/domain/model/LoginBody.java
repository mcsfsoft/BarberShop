package com.mwsfot.system.domain.model;

import com.anji.captcha.model.vo.PointVO;
import java.awt.Point;
import java.util.List;
import lombok.Data;

/**
 * 用户登录对象
 *
 * @author ruoyi
 */
@Data
public class LoginBody {
    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    private Long tenantId;


    //--------------------------aj校验相关参数----------------------------
    private String captchaId;
    private String projectCode;
    private String captchaType;
    private String captchaOriginalPath;
    private String captchaFontType;
    private Integer captchaFontSize;
    private String secretKey;
    private String originalImageBase64;
    private PointVO point;
    private String jigsawImageBase64;
    private List<String> wordList;
    private List<Point> pointList;
    private String pointJson;
    private String token;
    private Boolean result = false;
    private String captchaVerification;
    private String clientUid;
    private Long ts;
    private String browserInfo;

    public Long getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
