package com.mwsfot.system.mapper;

import com.mwsfot.system.domain.dto.SysTenantDto;
import com.mwsfot.system.domain.entity.SysTenant;
import com.mwsfot.system.domain.vo.TenantVo;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * 租户信息Mapper接口
 *
 * @author ruoyi
 * @date 2024-12-05
 */
@Mapper
public interface SysTenantMapper {
    /**
     * 查询租户信息
     *
     * @param tenantId 租户信息主键
     * @return 租户信息
     */
    public SysTenantDto selectSysTenantByTenantId(Long tenantId);

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
     * @param sysTenant 租户信息
     * @return 结果
     */
    public int insertSysTenant(SysTenant sysTenant);

    /**
     * 修改租户信息
     *
     * @param sysTenant 租户信息
     * @return 结果
     */
    public int updateSysTenant(SysTenant sysTenant);

    /**
     * 删除租户信息
     *
     * @param tenantId 租户信息主键
     * @return 结果
     */
    public int deleteSysTenantByTenantId(Long tenantId);

    /**
     * 批量删除租户信息
     *
     * @param tenantIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysTenantByTenantIds(Long[] tenantIds);

    /**
     * 查询平台租户列表
     *
     * @param tenantName 租户名称
     * @return 租户列表
     */
    List<TenantVo> selectPlatformList(String tenantName);
}
