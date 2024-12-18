package com.mwsfot.system.domain.dto;

import com.mwsfot.system.domain.entity.SysMenu;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author MinChang
 * @description 待完善
 * @date 2024/12/17 16:08
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysMenuDto extends SysMenu {
    private Long tenantId;
}
