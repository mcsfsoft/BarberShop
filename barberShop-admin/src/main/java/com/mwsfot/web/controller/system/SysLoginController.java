package com.mwsfot.web.controller.system;

import cn.hutool.core.bean.BeanUtil;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.mwsfot.framework.web.service.SysLoginService;
import com.mwsfot.framework.web.service.SysPermissionService;
import com.mwsfot.framework.web.service.TokenService;
import com.mwsfot.system.common.constant.Constants;
import com.mwsfot.system.common.core.domain.AjaxResult;
import com.mwsfot.system.common.enums.CaptchaEnum;
import com.mwsfot.system.common.utils.SecurityUtils;
import com.mwsfot.system.domain.entity.SysMenu;
import com.mwsfot.system.domain.entity.SysUser;
import com.mwsfot.system.domain.model.LoginBody;
import com.mwsfot.system.domain.model.LoginUser;
import com.mwsfot.system.service.ISysMenuService;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录验证
 * 
 * @author ruoyi
 */
@RestController
public class SysLoginController
{
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private CaptchaService captchaService;

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        CaptchaVO vo = new CaptchaVO();
        BeanUtil.copyProperties(loginBody, vo);
        ResponseModel verification = captchaService.verification(vo);
        //二次校验异常返回
        if ( !verification.isSuccess() ) {
            return AjaxResult.error(CaptchaEnum.getMsg(verification.getRepCode()));
        }
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(),
            loginBody.getTenantId());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 获取用户信息
     * 
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo()
    {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        if (!loginUser.getPermissions().equals(permissions))
        {
            loginUser.setPermissions(permissions);
            tokenService.refreshToken(loginUser);
        }
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 获取路由信息
     * 
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters()
    {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return AjaxResult.success(menuService.buildMenus(menus));
    }
}
