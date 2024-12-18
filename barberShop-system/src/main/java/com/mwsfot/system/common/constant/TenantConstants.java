package com.mwsfot.system.common.constant;

/**
 * @author MinChang
 * @description 租户枚举
 * @date 2024/12/6 14:12
 */
public interface TenantConstants {
    /**
     * 前端传递的租户ID字段名
     */
    String TENANT_ID_HEARER = "tenant-id";

    /**
     * 默认平台的ID
     */
    Long DEFAULT_SYSTEM_TENANT_ID = 1L;

    /**
     * 默认创建租户密码
     */
    String DEFAULT_PASSWORD = "123456";
}
