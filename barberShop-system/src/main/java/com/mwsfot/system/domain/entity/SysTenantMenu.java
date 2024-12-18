package com.mwsfot.system.domain.entity;

import com.mwsfot.system.common.annotation.Excel;
import com.mwsfot.system.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 租户菜单关联对象 sys_tenant_menu
 *
 * @author MinChang
 * @date 2024-12-17
 */
@AllArgsConstructor
@NoArgsConstructor
public class SysTenantMenu extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 租户ID
     */
    @Excel(name = "租户ID")
    private Long tenantId;

    /**
     * 菜单ID
     */
    @Excel(name = "菜单ID")
    private Long menuId;

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getMenuId() {
        return menuId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("tenantId", getTenantId())
            .append("menuId", getMenuId())
            .toString();
    }
}
