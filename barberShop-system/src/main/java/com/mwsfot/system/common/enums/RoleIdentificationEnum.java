package com.mwsfot.system.common.enums;

import java.util.Arrays;
import java.util.List;

/**
 * @author MinChang
 * @description 用户身份标识枚举
 * @date 2024/12/17 16:52
 */
public enum RoleIdentificationEnum {

    SYS_ADMIN_ROLE(0, "平台管理员"),
    TENANT_ADMIN_ROLE(1, "租户管理员"),
    NORMAL_USER_ROLE(2, "普通用户");

    private final Integer level;
    private final String identification;

    RoleIdentificationEnum(Integer level, String identification) {
        this.level = level;
        this.identification = identification;
    }

    public String getIdentification() {
        return this.identification;
    }

    public Integer getLevel() {
        return this.level;
    }

    public static List<Integer> getAdmin() {
        return Arrays.asList(SYS_ADMIN_ROLE.level, TENANT_ADMIN_ROLE.level);
    }

}
