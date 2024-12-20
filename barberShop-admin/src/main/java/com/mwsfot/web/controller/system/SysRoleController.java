package com.mwsfot.web.controller.system;

import cn.hutool.core.util.IdUtil;
import com.mwsfot.framework.web.service.SysPermissionService;
import com.mwsfot.framework.web.service.TokenService;
import com.mwsfot.system.common.annotation.Log;
import com.mwsfot.system.common.constant.TenantConstants;
import com.mwsfot.system.common.core.domain.AjaxResult;
import com.mwsfot.system.common.core.page.TableDataInfo;
import com.mwsfot.system.common.enums.BusinessType;
import com.mwsfot.system.common.utils.SecurityUtils;
import com.mwsfot.system.common.utils.StringUtils;
import com.mwsfot.system.common.utils.poi.ExcelUtil;
import com.mwsfot.system.controller.BaseController;
import com.mwsfot.system.domain.SysUserRole;
import com.mwsfot.system.domain.entity.SysDept;
import com.mwsfot.system.domain.entity.SysRole;
import com.mwsfot.system.domain.entity.SysUser;
import com.mwsfot.system.domain.model.LoginUser;
import com.mwsfot.system.service.ISysDeptService;
import com.mwsfot.system.service.ISysRoleService;
import com.mwsfot.system.service.ISysUserService;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色信息
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/role")
public class SysRoleController extends BaseController {
    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysDeptService deptService;

    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysRole role) {
        startPage();
        checkIsAllowTenantId(role.getTenantId());
        List<SysRole> list = roleService.selectRoleList(role);
        return getDataTable(list);
    }

    @Log(title = "角色管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:role:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysRole role) {
        checkIsAllowTenantId(role.getTenantId());
        List<SysRole> list = roleService.selectRoleList(role);
        ExcelUtil<SysRole> util = new ExcelUtil<SysRole>(SysRole.class);
        util.exportExcel(response, list, "角色数据");
    }

    /**
     * 根据角色编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping(value = "/{roleId}")
    public AjaxResult getInfo(@PathVariable Long roleId) {
        roleService.checkRoleDataScope(roleId);
        roleService.checkRoleTenantAllowed(new SysRole(roleId));
        return success(roleService.selectRoleById(roleId));
    }

    /**
     * 新增角色
     */
    @PreAuthorize("@ss.hasPermi('system:role:add')")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysRole role) {
        roleService.checkRoleTenantAllowed(role);
        if ( !roleService.checkRoleNameUnique(role) ) {
            return error("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if ( !roleService.checkRoleKeyUnique(role) ) {
            return error("新增角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setCreateBy(getUsername());
        role.setRoleId(IdUtil.getSnowflakeNextId());
        return toAjax(roleService.insertRole(role));

    }

    /**
     * 修改保存角色
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        roleService.checkRoleTenantAllowed(role);
        if ( !roleService.checkRoleNameUnique(role) ) {
            return error("修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if ( !roleService.checkRoleKeyUnique(role) ) {
            return error("修改角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        //非平台端租户只能生成自身租户权限
        if ( !role.getTenantId().equals(TenantConstants.DEFAULT_SYSTEM_TENANT_ID) ) {
            role.setTenantId(SecurityUtils.getTenantId());
        }
        role.setUpdateBy(getUsername());

        if ( roleService.updateRole(role) > 0 ) {
            // 更新缓存用户权限
            LoginUser loginUser = getLoginUser();
            if ( StringUtils.isNotNull(loginUser.getUser()) && !loginUser.getUser().isAdmin() ) {
                SysUser sysUser =
                    userService.selectUserByUserNameAndTenantId(loginUser.getUser().getUserName(),
                        loginUser.getTenantId());
                loginUser.setUser(sysUser);
                loginUser.setPermissions(permissionService.getMenuPermission(loginUser.getUser()));
                loginUser.setTenantId(sysUser.getTenantId());
                tokenService.setLoginUser(loginUser);
            }
            return success();
        }
        return error("修改角色'" + role.getRoleName() + "'失败，请联系管理员");
    }

    /**
     * 修改保存数据权限
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/dataScope")
    public AjaxResult dataScope(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        roleService.checkRoleTenantAllowed(role);
        return toAjax(roleService.authDataScope(role));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        roleService.checkRoleTenantAllowed(role);
        role.setUpdateBy(getUsername());
        return toAjax(roleService.updateRoleStatus(role));
    }

    /**
     * 删除角色
     */
    @PreAuthorize("@ss.hasPermi('system:role:remove')")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{roleIds}")
    public AjaxResult remove(@PathVariable Long[] roleIds) {
        return toAjax(roleService.deleteRoleByIds(roleIds));
    }

    /**
     * 获取角色选择框列表
     */
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping("/optionselect")
    public AjaxResult optionselect(Long tenantId) {
        if ( tenantId == null ||
            !SecurityUtils.getTenantId().equals(TenantConstants.DEFAULT_SYSTEM_TENANT_ID) ) {
            tenantId = SecurityUtils.getTenantId();
        }
        return success(roleService.selectRoleAll(tenantId));
    }

    /**
     * 查询已分配用户角色列表
     */
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/authUser/allocatedList")
    public TableDataInfo allocatedList(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectAllocatedList(user);
        return getDataTable(list);
    }

    /**
     * 查询未分配用户角色列表
     */
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/authUser/unallocatedList")
    public TableDataInfo unallocatedList(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectUnallocatedList(user);
        return getDataTable(list);
    }

    /**
     * 取消授权用户
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancel")
    public AjaxResult cancelAuthUser(@RequestBody SysUserRole userRole) {
        return toAjax(roleService.deleteAuthUser(userRole));
    }

    /**
     * 批量取消授权用户
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancelAll")
    public AjaxResult cancelAuthUserAll(Long roleId, Long[] userIds) {
        return toAjax(roleService.deleteAuthUsers(roleId, userIds));
    }

    /**
     * 批量选择用户授权
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/selectAll")
    public AjaxResult selectAuthUserAll(Long roleId, Long[] userIds) {
        roleService.checkRoleDataScope(roleId);
        return toAjax(roleService.insertAuthUsers(roleId, userIds));
    }

    /**
     * 获取对应角色部门树列表
     */
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping(value = "/deptTree/{roleId}")
    public AjaxResult deptTree(@PathVariable("roleId") Long roleId) {
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", deptService.selectDeptListByRoleId(roleId));
        ajax.put("depts", deptService.selectDeptTreeList(new SysDept()));
        return ajax;
    }
}
