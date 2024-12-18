package com.mwsfot.system.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mwsfot.system.common.annotation.Excel;
import com.mwsfot.system.common.core.domain.BaseEntity;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 租户信息对象 sys_tenant
 *
 * @author ruoyi
 * @date 2024-12-05
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysTenant extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 租户名称
     */
    @Excel(name = "租户名称")
    private String tenantName;

    /**
     * 联系地址
     */
    @Excel(name = "联系地址")
    private String address;

    /**
     * 联系电话
     */
    @Excel(name = "联系电话")
    private String phoneNumber;

    /**
     * 是否可修改
     */
    private Integer isFix;

    /**
     * 开通时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "开通时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date openTime;

    /**
     * 过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "过期时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date expireTime;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @Excel(name = "删除标识", readConverterExp = "0=代表存在,2=代表删除")
    private String delFlag;
    /**
     * 角色状态（0正常 1停用）
     */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }

    public Date getOpenTime() {
        return openTime;
    }

    public void setExpireTime(Date expirTime) {
        this.expireTime = expirTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("tenantId", getTenantId())
            .append("tenantName", getTenantName())
            .append("address", getAddress())
            .append("phoneNumber", getPhoneNumber())
            .append("openTime", getOpenTime())
            .append("expirTime", getExpireTime())
            .append("remark", getRemark())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
