package com.mwsfot.web.controller.system;

import com.mwsfot.system.common.annotation.Log;
import com.mwsfot.system.common.constant.TenantConstants;
import com.mwsfot.system.common.core.domain.AjaxResult;
import com.mwsfot.system.common.core.page.TableDataInfo;
import com.mwsfot.system.common.enums.BusinessType;
import com.mwsfot.system.common.utils.SecurityUtils;
import com.mwsfot.system.common.utils.poi.ExcelUtil;
import com.mwsfot.system.controller.BaseController;
import com.mwsfot.system.domain.entity.SysTenant;
import com.mwsfot.system.domain.form.SysTenantForm;
import com.mwsfot.system.domain.vo.TenantVo;
import com.mwsfot.system.service.ISysTenantService;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 租户信息Controller
 *
 * @author ruoyi
 * @date 2024-12-05
 */
@RestController
@RequestMapping("/system/tenant")
public class SysTenantController extends BaseController {
    @Autowired
    private ISysTenantService sysTenantService;

    /**
     * 查询租户信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysTenant sysTenant) {
        startPage();
        List<SysTenant> list = sysTenantService.selectSysTenantList(sysTenant);
        return getDataTable(list);
    }

    /**
     * 查询平台下所有租户信息列表(仅限登录管理平台使用)
     */
    @GetMapping("/platform/login/list")
    public AjaxResult platformLoginList(String tenantName) {
        return success(sysTenantService.selectPlatformList(tenantName));
    }

    @GetMapping("/platform/page/list")
    public AjaxResult platformPageList(String tenantName) {
        Long tenantId = SecurityUtils.getTenantId();
        if ( !tenantId.equals(TenantConstants.DEFAULT_SYSTEM_TENANT_ID) ) {
            TenantVo vo = new TenantVo();
            vo.setTenantId(tenantId);
            vo.setTenantName(sysTenantService.selectSysTenantByTenantId(tenantId).getTenantName());
            return success(Collections.singletonList(vo));
        }
        return success(sysTenantService.selectPlatformList(tenantName));
    }

    /**
     * 导出租户信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:export')")
    @Log(title = "租户信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysTenant sysTenant) {
        List<SysTenant> list = sysTenantService.selectSysTenantList(sysTenant);
        ExcelUtil<SysTenant> util = new ExcelUtil<SysTenant>(SysTenant.class);
        util.exportExcel(response, list, "租户信息数据");
    }

    /**
     * 获取租户信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:query')")
    @GetMapping(value = "/{tenantId}")
    public AjaxResult getInfo(@PathVariable("tenantId") Long tenantId) {
        return success(sysTenantService.selectSysTenantByTenantId(tenantId));
    }

    /**
     * TODO 新增时, 默认初始化一个租户的管理员角色, 再默认初始化一个租户的部门
     * 再默认初始化一个租户管理员
     * 新增租户信息
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:add')")
    @Log(title = "租户信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysTenantForm form) {
        return sysTenantService.insertSysTenant(form);
    }

    /**
     * 修改租户信息
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:edit')")
    @Log(title = "租户信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysTenantForm form) {
        return sysTenantService.updateSysTenant(form);
    }

    /**
     * TODO 删除时, 删除租户下所有角色/部门/用户
     * 删除租户信息
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:remove')")
    @Log(title = "租户信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{tenantId}")
    public AjaxResult remove(@PathVariable Long tenantId) {
        return toAjax(sysTenantService.deleteSysTenantByTenantId(tenantId));
    }
}
