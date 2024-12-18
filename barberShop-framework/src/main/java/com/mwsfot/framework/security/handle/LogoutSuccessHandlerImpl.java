package com.mwsfot.framework.security.handle;

import com.alibaba.fastjson2.JSON;
import com.mwsfot.framework.manager.AsyncManager;
import com.mwsfot.framework.manager.factory.AsyncFactory;
import com.mwsfot.framework.web.service.TokenService;
import com.mwsfot.system.common.constant.Constants;
import com.mwsfot.system.common.core.domain.AjaxResult;
import com.mwsfot.system.common.utils.MessageUtils;
import com.mwsfot.system.common.utils.ServletUtils;
import com.mwsfot.system.common.utils.StringUtils;
import com.mwsfot.system.domain.model.LoginUser;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * 自定义退出处理类 返回成功
 * 
 * @author ruoyi
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler
{
    @Autowired
    private TokenService tokenService;

    /**
     * 退出处理
     * 
     * @return
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException
    {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser))
        {
            String userName = loginUser.getUsername();
            // 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getToken());
            // 记录用户退出日志
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGOUT, MessageUtils.message("user.logout.success")));
        }
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.success(MessageUtils.message("user.logout.success"))));
    }
}
