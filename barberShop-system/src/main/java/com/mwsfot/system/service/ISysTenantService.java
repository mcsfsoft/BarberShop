package com.mwsfot.system.service;

import com.mwsfot.system.common.core.domain.AjaxResult;
import com.mwsfot.system.domain.entity.SysTenant;
import com.mwsfot.system.domain.form.SysTenantForm;
import com.mwsfot.system.domain.vo.TenantVo;
import java.util.List;

/**
 * 租户信息Service接口
 *
 * @author ruoyi
 * @date 2024-12-05
 */
public interface ISysTenantService {
    /**
     * 查询租户信息
     *
     * @param tenantId 租户信息主键
     * @return 租户信息
     */
    public SysTenant selectSysTenantByTenantId(Long tenantId);

    /**
     * 查询租户信息列表
     *
     * @param sysTenant 租户信息
     * @return 租户信息集合
     */
    public List<SysTenant> selectSysTenantList(SysTenant sysTenant);

    /**
     * 新增租户信息
     *
     * @param form 租户信息
     * @return 结果
     */
    public AjaxResult insertSysTenant(SysTenantForm form);

    /**
     * 修改租户信息
     *
     * @param form 租户信息
     * @return 结果
     */
    public AjaxResult updateSysTenant(SysTenantForm form);

    /**
     * 删除租户信息信息
     *
     * @param tenantId 租户信息主键
     * @return 结果
     */
    public int deleteSysTenantByTenantId(Long tenantId);

    /**
     * 查询租户平台列表
     *
     * @param tenantName 租户名称
     * @return 结果
     */
    List<TenantVo> selectPlatformList(String tenantName);
}
