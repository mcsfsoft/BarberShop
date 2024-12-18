package com.mwsfot.framework.web.service;

import cn.hutool.core.text.StrPool;
import com.mwsfot.system.common.enums.UserStatus;
import com.mwsfot.system.common.exception.ServiceException;
import com.mwsfot.system.common.utils.MessageUtils;
import com.mwsfot.system.common.utils.StringUtils;
import com.mwsfot.system.domain.entity.SysUser;
import com.mwsfot.system.domain.model.LoginUser;
import com.mwsfot.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户验证处理
 *
 * @author ruoyi
 */
@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsServiceImpl.class);

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private SysPermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String loginData) throws UsernameNotFoundException {
        //将参数进行切割. 得到目前需要的信息
        String[] split = loginData.split(StrPool.COLON);
        String username = split[0];
        String tenantId = split[1];
        SysUser user =
            userService.selectUserByUserNameAndTenantId(username, Long.valueOf(tenantId));
        if ( StringUtils.isNull(user) ) {
            log.info("登录用户：{} 不存在.", loginData);
            throw new ServiceException(MessageUtils.message("user.not.exists"));
        } else if ( UserStatus.DELETED.getCode().equals(user.getDelFlag()) ) {
            log.info("登录用户：{} 已被删除.", loginData);
            throw new ServiceException(MessageUtils.message("user.password.delete"));
        }
        else if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
        {
            log.info("登录用户：{} 已被停用.", loginData);
            throw new ServiceException(MessageUtils.message("user.blocked"));
        }

        passwordService.validate(user);

        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user)
    {
        return new LoginUser(user.getUserId(), user.getDeptId(), user.getTenantId(), user,
            permissionService.getMenuPermission(user));
    }
}
