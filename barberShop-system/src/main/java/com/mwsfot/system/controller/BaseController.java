package com.mwsfot.system.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mwsfot.system.common.constant.HttpStatus;
import com.mwsfot.system.common.constant.TenantConstants;
import com.mwsfot.system.common.core.domain.AjaxResult;
import com.mwsfot.system.common.core.page.PageDomain;
import com.mwsfot.system.common.core.page.TableDataInfo;
import com.mwsfot.system.common.core.page.TableSupport;
import com.mwsfot.system.common.exception.ServiceException;
import com.mwsfot.system.common.utils.DateUtils;
import com.mwsfot.system.common.utils.PageUtils;
import com.mwsfot.system.common.utils.SecurityUtils;
import com.mwsfot.system.common.utils.StringUtils;
import com.mwsfot.system.common.utils.sql.SqlUtil;
import com.mwsfot.system.domain.model.LoginUser;
import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * web层通用数据处理
 * 
 * @author ruoyi
 */
public class BaseController
{
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder)
    {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 租户ID校验
     *
     * @param tenantId 租户ID
     */
    public void checkIsAllowTenantId(Long tenantId) {
        boolean error = false;
        if ( tenantId != null ) {
            //操作平台下: 只有平台用户能操作平台
            if ( TenantConstants.DEFAULT_SYSTEM_TENANT_ID.equals(tenantId) ) {
                if ( !TenantConstants.DEFAULT_SYSTEM_TENANT_ID.equals(
                    SecurityUtils.getTenantId()) ) {
                    error = true;
                }
            } else {
                //操作租户下: 只有租户用户或者平台能操作
                if ( !TenantConstants.DEFAULT_SYSTEM_TENANT_ID.equals(
                    SecurityUtils.getTenantId()) ) {
                    //也不是自己的平台
                    if ( !SecurityUtils.getTenantId().equals(tenantId) ) {
                        error = true;
                    }
                }
            }
            if ( error ) {
                throw new ServiceException("非法操作, 请联系管理员");
            }
        }
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage() {
        PageUtils.startPage();
    }

    /**
     * 设置请求排序数据
     */
    protected void startOrderBy()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        if (StringUtils.isNotEmpty(pageDomain.getOrderBy()))
        {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.orderBy(orderBy);
        }
    }

    /**
     * 清理分页的线程变量
     */
    protected void clearPage()
    {
        PageUtils.clearPage();
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected TableDataInfo getDataTable(List<?> list)
    {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    /**
     * 返回成功
     */
    public AjaxResult success()
    {
        return AjaxResult.success();
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error()
    {
        return AjaxResult.error();
    }

    /**
     * 返回成功消息
     */
    public AjaxResult success(String message)
    {
        return AjaxResult.success(message);
    }
    
    /**
     * 返回成功消息
     */
    public AjaxResult success(Object data)
    {
        return AjaxResult.success(data);
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error(String message)
    {
        return AjaxResult.error(message);
    }

    /**
     * 返回警告消息
     */
    public AjaxResult warn(String message)
    {
        return AjaxResult.warn(message);
    }

    /**
     * 响应返回结果
     * 
     * @param rows 影响行数
     * @return 操作结果
     */
    protected AjaxResult toAjax(int rows)
    {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 响应返回结果
     * 
     * @param result 结果
     * @return 操作结果
     */
    protected AjaxResult toAjax(boolean result)
    {
        return result ? success() : error();
    }

    /**
     * 页面跳转
     */
    public String redirect(String url)
    {
        return StringUtils.format("redirect:{}", url);
    }

    /**
     * 获取用户缓存信息
     */
    public LoginUser getLoginUser()
    {
        return SecurityUtils.getLoginUser();
    }

    /**
     * 获取登录用户id
     */
    public Long getUserId()
    {
        return getLoginUser().getUserId();
    }

    /**
     * 获取登录部门id
     */
    public Long getDeptId()
    {
        return getLoginUser().getDeptId();
    }

    /**
     * 获取登录用户名
     */
    public String getUsername()
    {
        return getLoginUser().getUsername();
    }
}
