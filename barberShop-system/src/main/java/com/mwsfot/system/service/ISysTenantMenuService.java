package com.mwsfot.system.service;

import com.mwsfot.system.domain.entity.SysTenantMenu;
import java.util.List;

/**
 * 租户菜单关联Service接口
 *
 * @author MinChang
 * @date 2024-12-17
 */
public interface ISysTenantMenuService {

    /**
     * 查询租户菜单关联列表
     *
     * @param sysTenantMenu 租户菜单关联
     * @return 租户菜单关联集合
     */
    public List<SysTenantMenu> selectSysTenantMenuList(SysTenantMenu sysTenantMenu);

    /**
     * 新增租户菜单关联
     *
     * @param sysTenantMenu 租户菜单关联
     * @return 结果
     */
    public int insertSysTenantMenu(SysTenantMenu sysTenantMenu);

    /**
     * 修改租户菜单关联
     *
     * @param sysTenantMenu 租户菜单关联
     * @return 结果
     */
    public int updateSysTenantMenu(SysTenantMenu sysTenantMenu);

    /**
     * 批量删除租户菜单关联
     *
     * @param tenantIds 需要删除的租户菜单关联主键集合
     * @return 结果
     */
    public int deleteSysTenantMenuByTenantIds(Long[] tenantIds);

    /**
     * 删除租户菜单关联信息
     *
     * @param tenantId 租户菜单关联主键
     * @return 结果
     */
    public int deleteSysTenantMenuByTenantId(Long tenantId);
}
