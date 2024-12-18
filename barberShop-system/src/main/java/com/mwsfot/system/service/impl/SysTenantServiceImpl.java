package com.mwsfot.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import com.mwsfot.system.common.constant.Constants;
import com.mwsfot.system.common.constant.TenantConstants;
import com.mwsfot.system.common.constant.UserConstants;
import com.mwsfot.system.common.core.domain.AjaxResult;
import com.mwsfot.system.common.enums.RoleIdentificationEnum;
import com.mwsfot.system.common.exception.ServiceException;
import com.mwsfot.system.common.utils.DateUtils;
import com.mwsfot.system.common.utils.SecurityUtils;
import com.mwsfot.system.domain.SysPost;
import com.mwsfot.system.domain.SysRoleDept;
import com.mwsfot.system.domain.SysRoleMenu;
import com.mwsfot.system.domain.SysUserRole;
import com.mwsfot.system.domain.dto.SysTenantDto;
import com.mwsfot.system.domain.entity.SysDept;
import com.mwsfot.system.domain.entity.SysRole;
import com.mwsfot.system.domain.entity.SysTenant;
import com.mwsfot.system.domain.entity.SysTenantMenu;
import com.mwsfot.system.domain.entity.SysUser;
import com.mwsfot.system.domain.form.SysTenantForm;
import com.mwsfot.system.domain.vo.TenantVo;
import com.mwsfot.system.mapper.SysDeptMapper;
import com.mwsfot.system.mapper.SysPostMapper;
import com.mwsfot.system.mapper.SysRoleDeptMapper;
import com.mwsfot.system.mapper.SysRoleMapper;
import com.mwsfot.system.mapper.SysRoleMenuMapper;
import com.mwsfot.system.mapper.SysTenantMapper;
import com.mwsfot.system.mapper.SysTenantMenuMapper;
import com.mwsfot.system.mapper.SysUserMapper;
import com.mwsfot.system.mapper.SysUserPostMapper;
import com.mwsfot.system.mapper.SysUserRoleMapper;
import com.mwsfot.system.service.ISysTenantService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 租户信息Service业务层处理
 *
 * @author ruoyi
 * @date 2024-12-05
 */
@Service
public class SysTenantServiceImpl implements ISysTenantService {
    @Autowired
    private SysTenantMapper sysTenantMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysRoleDeptMapper sysRoleDeptMapper;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;
    @Autowired
    private SysUserPostMapper sysUserPostMapper;
    @Autowired
    private SysPostMapper sysPostMapper;
    @Autowired
    private SysTenantMenuMapper sysTenantMenuMapper;

    /**
     * 查询租户信息
     *
     * @param tenantId 租户信息主键
     * @return 租户信息
     */
    @Override
    public SysTenantDto selectSysTenantByTenantId(Long tenantId) {
        SysTenantDto sysTenantDto = sysTenantMapper.selectSysTenantByTenantId(tenantId);
        sysTenantDto.setMenuIds(sysTenantMenuMapper.selectSysTenantMenuByTenantId(tenantId).stream()
            .map(SysTenantMenu::getMenuId).collect(
                Collectors.toList()));
        return sysTenantDto;
    }

    /**
     * 查询租户信息列表
     *
     * @param sysTenant 租户信息
     * @return 租户信息
     */
    @Override
    public List<SysTenant> selectSysTenantList(SysTenant sysTenant) {
        return sysTenantMapper.selectSysTenantList(sysTenant);
    }

    /**
     * 新增租户信息
     *
     * @param form 租户信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult insertSysTenant(SysTenantForm form) {
        String createdBy = SecurityUtils.getUsername();
        Date createTime = new Date();
        long tenantId = IdUtil.getSnowflakeNextId();
        Long userId = IdUtil.getSnowflakeNextId();
        Long roleId = IdUtil.getSnowflakeNextId();
        Long deptId = IdUtil.getSnowflakeNextId();
        SysUser user = SysUser.builder().userId(userId)
            .userName(Constants.SUPER_ADMIN)
            .nickName(form.getTenantName() + "管理员")
            .phonenumber(form.getPhoneNumber())
            .status(UserConstants.NORMAL)
            .password(SecurityUtils.encryptPassword(TenantConstants.DEFAULT_PASSWORD))
            .delFlag(UserConstants.NORMAL)
            .deptId(deptId)
            .isFix(UserConstants.UN_FIX)
            .tenantId(tenantId)
            .roleId(roleId).build();
        user.setCreateBy(createdBy);
        user.setCreateTime(createTime);
        sysUserMapper.insertUser(user);

        SysDept dept = SysDept.builder().deptId(deptId)
            .deptName(form.getTenantName())
            .delFlag(UserConstants.DEPT_NORMAL)
            .orderNum(0)
            .parentId(0L)
            .ancestors("0")
            .status(UserConstants.DEPT_NORMAL)
            .isFix(UserConstants.UN_FIX)
            .tenantId(tenantId)
            .phone(form.getPhoneNumber()).build();
        dept.setCreateBy(createdBy);
        dept.setCreateTime(createTime);
        sysDeptMapper.insertDept(dept);

        SysRole role = SysRole.builder().roleId(roleId)
            .delFlag(UserConstants.ROLE_NORMAL)
            .status(UserConstants.ROLE_NORMAL)
            .identity(RoleIdentificationEnum.TENANT_ADMIN_ROLE.getLevel())
            .tenantId(tenantId)
            .roleName("平台管理员")
            .isFix(UserConstants.UN_FIX)
            .roleKey(PinyinUtil.getEngine().getFirstLetter(form.getTenantName(), "#").toUpperCase()
                .replace("#", "") + "_GLY")
            .roleSort(0)
            .menuCheckStrictly(true)
            .deptCheckStrictly(true)
            .dataScope("1").build();
        role.setCreateBy(createdBy);
        role.setCreateTime(createTime);
        sysRoleMapper.insertRole(role);

        //插入用户角色权限
        sysUserRoleMapper.batchUserRole(Collections.singletonList(new SysUserRole(userId, roleId)));

        List<SysRoleMenu> roleMenuList = new ArrayList<>();
        List<SysTenantMenu> tenantMenuList = new ArrayList<>();
        //插入角色菜单关联
        form.getMenuIds().forEach(menuId -> {
            roleMenuList.add(new SysRoleMenu(roleId, menuId));
            tenantMenuList.add(new SysTenantMenu(tenantId, menuId));
        });
        sysTenantMenuMapper.batchSysTenantMenu(tenantMenuList);
        sysRoleMenuMapper.batchRoleMenu(roleMenuList);

        //插入角色部门关联
        sysRoleDeptMapper.batchRoleDept(Collections.singletonList(new SysRoleDept(roleId, deptId)));
        //插入租户
        SysTenant sysTenant = new SysTenant();
        BeanUtil.copyProperties(form, sysTenant);
        sysTenant.setTenantId(tenantId);
        sysTenant.setCreateTime(createTime);
        sysTenant.setCreateBy(createdBy);
        int i = sysTenantMapper.insertSysTenant(sysTenant);
        if ( i > 0 ) {
            return AjaxResult.success();
        }
        throw new ServiceException("租户信息插入失败");
    }

    /**
     * 修改租户信息
     *
     * @param form 租户信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult updateSysTenant(SysTenantForm form) {
        if ( form.getTenantId() == null ) {
            return AjaxResult.error("租户ID不能为空");
        }
        //删除租户下所有菜单
        sysTenantMenuMapper.deleteSysTenantMenuByTenantId(form.getTenantId());
        //删除角色关联关系, 重新生成脉络
        List<SysRole> roleList =
            sysRoleMapper.selectRoleList(SysRole.builder().tenantId(form.getTenantId()).build());
        if ( roleList.size() > 0 ) {
            Long[] roleIds = roleList.stream().map(SysRole::getRoleId).toArray(Long[]::new);
            sysRoleMenuMapper.deleteRoleMenu(roleIds);
            //插入角色菜单关联
            List<SysRoleMenu> roleMenuList = new ArrayList<>();

            List<Long> menuIds = form.getMenuIds();
            for (Long roleId : roleIds) {
                assert menuIds != null;
                for (Long menuId : menuIds) {
                    roleMenuList.add(new SysRoleMenu(roleId, menuId));
                }
            }
            sysRoleMenuMapper.batchRoleMenu(roleMenuList);
        }
        //插入租户菜单权限
        List<SysTenantMenu> tenantMenuList = new ArrayList<>();
        List<Long> menuIds = form.getMenuIds();
        for (Long menuId : menuIds) {
            tenantMenuList.add(new SysTenantMenu(form.getTenantId(), menuId));
        }
        sysTenantMenuMapper.batchSysTenantMenu(tenantMenuList);

        SysTenant sysTenant = new SysTenant();
        BeanUtil.copyProperties(form, sysTenant);
        sysTenant.setUpdateBy(SecurityUtils.getUsername());
        sysTenant.setUpdateTime(DateUtils.getNowDate());
        int i = sysTenantMapper.updateSysTenant(sysTenant);
        if ( i > 0 ) {
            return AjaxResult.success();
        }
        throw new ServiceException("租户信息更新失败");
    }


    /**
     * 删除租户信息信息
     *
     * @param tenantId 租户信息主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSysTenantByTenantId(Long tenantId) {
        Long[] userIds =
            sysUserMapper.selectUserList(SysUser.builder().tenantId(tenantId).build()).stream()
                .map(SysUser::getUserId).toArray(Long[]::new);
        Long[] roleIds =
            sysRoleMapper.selectRoleList(SysRole.builder().tenantId(tenantId).build()).stream()
                .map(SysRole::getRoleId).toArray(Long[]::new);
        //删除角色
        if ( roleIds.length > 0 ) {
            sysRoleMapper.deleteRoleByIds(roleIds);
            sysRoleDeptMapper.deleteRoleDept(roleIds);
            sysRoleMenuMapper.deleteRoleMenu(roleIds);
        }


        //删除用户
        if ( userIds.length > 0 ) {
            sysUserMapper.deleteUserByIds(userIds);
            sysUserRoleMapper.deleteUserRole(userIds);
            sysUserPostMapper.deleteUserPost(userIds);
        }
        //删除部门
        Long[] deptIds =
            sysDeptMapper.selectDeptList(SysDept.builder().tenantId(tenantId).build()).stream()
                .map(SysDept::getDeptId).toArray(Long[]::new);
        if ( deptIds.length > 0 ) {
            sysDeptMapper.deleteDeptByIds(deptIds);
        }
        //删除职位信息
        Long[] postIds =
            sysPostMapper.selectPostList(SysPost.builder().tenantId(tenantId).build()).stream()
                .map(SysPost::getPostId).toArray(Long[]::new);
        if ( postIds.length > 0 ) {
            sysPostMapper.deletePostByIds(postIds);
        }
        //删除租户菜单关联
        sysTenantMenuMapper.deleteSysTenantMenuByTenantId(tenantId);
        return sysTenantMapper.deleteSysTenantByTenantId(tenantId);
    }

    /**
     * 查询租户平台列表
     *
     * @param tenantName 租户名称
     * @return 结果
     */
    @Override
    public List<TenantVo> selectPlatformList(String tenantName) {
        return sysTenantMapper.selectPlatformList(tenantName);
    }
}
