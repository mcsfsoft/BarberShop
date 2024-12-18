package com.mwsfot.system.domain.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author MinChang
 * @description 租户表单
 * @date 2024/12/17 9:31
 */
@Data
public class SysTenantForm {
    private static final long serialVersionUID = 1L;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 租户名称
     */
    private String tenantName;

    /**
     * 联系地址
     */
    private String address;

    /**
     * 联系电话
     */
    private String phoneNumber;

    /**
     * 开通时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date openTime;

    /**
     * 过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expireTime;

    /**
     * 删除标志（0代表启用 1代表禁用）
     */
    private String delFlag;

    /**
     * 菜单ID列表字符串,逗号隔开.必填
     */
    @NotNull(message = "菜单列表不能为空")
    private List<Long> menuIds;

    /**
     * 备注信息
     */
    private String remark;
}
