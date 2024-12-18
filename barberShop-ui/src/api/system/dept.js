import request from '@/utils/request'

// 查询部门列表
export function listDept(query) {
  return request({
    url: '/system/dept/list',
    method: 'get',
    params: query
  })
}

// 查询部门列表（排除节点）
export function listDeptExcludeChild(deptId, tenantId) {
  return request({
    url: '/system/dept/list/exclude/' + deptId / tenantId,
    method: 'get'
  })
}

// 查询部门详细
export function getDept(deptId, tenantId) {
  return request({
    url: '/system/dept/' + deptId + '/' + tenantId,
    method: 'get'
  })
}

// 新增部门
export function addDept(data) {
  return request({
    url: '/system/dept',
    method: 'post',
    data: data
  })
}

// 修改部门
export function updateDept(data) {
  return request({
    url: '/system/dept',
    method: 'put',
    data: data
  })
}

// 删除部门
export function delDept(deptId, tenantId) {
  return request({
    url: '/system/dept/' + deptId + '/' + tenantId,
    method: 'delete'
  })
}