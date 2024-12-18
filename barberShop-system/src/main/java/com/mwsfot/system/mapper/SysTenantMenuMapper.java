package com.mwsfot.system.mapper;

import com.mwsfot.system.domain.entity.SysTenantMenu;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * 租户菜单关联Mapper接口
 *
 * @author MinChang
 * @date 2024-12-17
 */
@Mapper
public interface SysTenantMenuMapper {
    /**
     * 查询租户菜单关联
     *
     * @param tenantId 租户菜单关联主键
     * @return 租户菜单关联
     */
    public List<SysTenantMenu> selectSysTenantMenuByTenantId(Long tenantId);

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
     * 批量新增租户菜单关联
     *
     * @param sysTenantMenuList 租户菜单关联集合
     * @return 结果
     */
    public int batchSysTenantMenu(List<SysTenantMenu> sysTenantMenuList);

    /**
     * 修改租户菜单关联
     *
     * @param sysTenantMenu 租户菜单关联
     * @return 结果
     */
    public int updateSysTenantMenu(SysTenantMenu sysTenantMenu);

    /**
     * 删除租户菜单关联
     *
     * @param tenantId 租户菜单关联主键
     * @return 结果
     */
    public int deleteSysTenantMenuByTenantId(Long tenantId);

    /**
     * 批量删除租户菜单关联
     *
     * @param tenantIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysTenantMenuByTenantIds(Long[] tenantIds);
}
