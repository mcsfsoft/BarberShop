package com.mwsfot.system.domain.dto;

import com.mwsfot.system.domain.entity.SysTenant;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author MinChang
 * @description 待完善
 * @date 2024/12/17 11:46
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysTenantDto extends SysTenant {
    private List<Long> menuIds;
}
