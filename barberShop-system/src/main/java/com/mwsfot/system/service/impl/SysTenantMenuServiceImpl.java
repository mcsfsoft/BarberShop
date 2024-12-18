package com.mwsfot.system.service.impl;

import com.mwsfot.system.domain.entity.SysTenantMenu;
import com.mwsfot.system.mapper.SysTenantMenuMapper;
import com.mwsfot.system.service.ISysTenantMenuService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 租户菜单关联Service业务层处理
 *
 * @author MinChang
 * @date 2024-12-17
 */
@Service
public class SysTenantMenuServiceImpl implements ISysTenantMenuService {
    @Autowired
    private SysTenantMenuMapper sysTenantMenuMapper;

    /**
     * 查询租户菜单关联列表
     *
     * @param sysTenantMenu 租户菜单关联
     * @return 租户菜单关联
     */
    @Override
    public List<SysTenantMenu> selectSysTenantMenuList(SysTenantMenu sysTenantMenu) {
        return sysTenantMenuMapper.selectSysTenantMenuList(sysTenantMenu);
    }

    /**
     * 新增租户菜单关联
     *
     * @param sysTenantMenu 租户菜单关联
     * @return 结果
     */
    @Override
    public int insertSysTenantMenu(SysTenantMenu sysTenantMenu) {
        return sysTenantMenuMapper.insertSysTenantMenu(sysTenantMenu);
    }

    /**
     * 修改租户菜单关联
     *
     * @param sysTenantMenu 租户菜单关联
     * @return 结果
     */
    @Override
    public int updateSysTenantMenu(SysTenantMenu sysTenantMenu) {
        return sysTenantMenuMapper.updateSysTenantMenu(sysTenantMenu);
    }

    /**
     * 批量删除租户菜单关联
     *
     * @param tenantIds 需要删除的租户菜单关联主键
     * @return 结果
     */
    @Override
    public int deleteSysTenantMenuByTenantIds(Long[] tenantIds) {
        return sysTenantMenuMapper.deleteSysTenantMenuByTenantIds(tenantIds);
    }

    /**
     * 删除租户菜单关联信息
     *
     * @param tenantId 租户菜单关联主键
     * @return 结果
     */
    @Override
    public int deleteSysTenantMenuByTenantId(Long tenantId) {
        return sysTenantMenuMapper.deleteSysTenantMenuByTenantId(tenantId);
    }
}
