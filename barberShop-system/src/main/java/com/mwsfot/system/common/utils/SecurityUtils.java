package com.mwsfot.system.common.utils;

import com.mwsfot.system.common.constant.Constants;
import com.mwsfot.system.common.constant.HttpStatus;
import com.mwsfot.system.common.enums.RoleIdentificationEnum;
import com.mwsfot.system.common.exception.ServiceException;
import com.mwsfot.system.domain.entity.SysRole;
import com.mwsfot.system.domain.model.LoginUser;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.PatternMatchUtils;

/**
 * 安全服务工具类
 *
 * @author ruoyi
 */
public class SecurityUtils {

    /**
     * 用户ID
     **/
    public static Long getUserId() {
        try {
            return getLoginUser().getUserId();
        } catch (Exception e) {
            throw new ServiceException("获取用户ID异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取部门ID
     **/
    public static Long getDeptId() {
        try {
            return getLoginUser().getDeptId();
        } catch (Exception e) {
            throw new ServiceException("获取部门ID异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取部门ID
     **/
    public static Long getTenantId() {
        try {
            return getLoginUser().getTenantId();
        } catch (Exception e) {
            throw new ServiceException("获取部门ID异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取当前用户最大权限列表
     *
     * @return 角色等级
     */
    public static int maxRoleLevel() {
        Optional<SysRole> max = getLoginUser().getUser().getRoles().stream()
            .max(Comparator.comparing(SysRole::getIdentity));
        assert max.isPresent();
        return max.get().getIdentity();
    }

    /**
     * 获取用户账户
     **/
    public static String getUsername() {
        try {
            return getLoginUser().getUsername();
        } catch (Exception e) {
            throw new ServiceException("获取用户账户异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser() {
        try {
            return (LoginUser) getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new ServiceException("获取用户信息异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword     真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 验证用户是否具备某权限
     *
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    public static boolean hasPermi(String permission) {
        return hasPermi(getLoginUser().getPermissions(), permission);
    }

    /**
     * 是否为平台管理员
     *
     * @return true 是 false 否
     */
    public static boolean isSystemAdmin() {
        return RoleIdentificationEnum.SYS_ADMIN_ROLE.getLevel().equals(maxRoleLevel());
    }

    /**
     * 是否为平台管理员
     *
     * @return true 是 false 否
     */
    public static boolean isTenantAdmin() {
        return RoleIdentificationEnum.TENANT_ADMIN_ROLE.getLevel().equals(maxRoleLevel());
    }

    /**
     * 是否为普通用户
     *
     * @return true 是 false 否
     */
    public static boolean isNormalUser() {
        return RoleIdentificationEnum.NORMAL_USER_ROLE.getLevel().equals(maxRoleLevel());
    }

    /**
     * 为租户管理员或者平台管理员
     *
     * @return true 是 false 否
     */
    public static boolean isTenantOrSystemAdmin() {
        int level = maxRoleLevel();
        return level == RoleIdentificationEnum.TENANT_ADMIN_ROLE.getLevel() ||
            level == RoleIdentificationEnum.SYS_ADMIN_ROLE.getLevel();
    }

    /**
     * 判断是否包含权限
     *
     * @param authorities 权限列表
     * @param permission  权限字符串
     * @return 用户是否具备某权限
     */
    public static boolean hasPermi(Collection<String> authorities, String permission) {
        return authorities.stream().filter(StringUtils::hasText)
            .anyMatch(x -> Constants.ALL_PERMISSION.equals(x) ||
                PatternMatchUtils.simpleMatch(x, permission));
    }

    /**
     * 验证用户是否拥有某个角色
     *
     * @param role 角色标识
     * @return 用户是否具备某角色
     */
    public static boolean hasRole(String role) {
        List<SysRole> roleList = getLoginUser().getUser().getRoles();
        Collection<String> roles =
            roleList.stream().map(SysRole::getRoleKey).collect(Collectors.toSet());
        return hasRole(roles, role);
    }

    /**
     * 判断是否包含角色
     *
     * @param roles 角色列表
     * @param role  角色
     * @return 用户是否具备某角色权限
     */
    public static boolean hasRole(Collection<String> roles, String role) {
        return roles.stream().filter(StringUtils::hasText)
            .anyMatch(
                x -> Constants.SUPER_ADMIN.equals(x) || PatternMatchUtils.simpleMatch(x, role));
    }

}
