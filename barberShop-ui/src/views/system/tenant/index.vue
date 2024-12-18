<template xmlns:el-col="http://www.w3.org/1999/html">
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="租户名称" prop="tenantName">
        <el-input
            v-model="queryParams.tenantName"
            placeholder="请输入租户名称"
            clearable
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="联系电话" prop="phoneNumber">
        <el-input
            v-model="queryParams.phoneNumber"
            placeholder="请输入联系电话"
            clearable
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="开通时间" prop="openTime">
        <el-date-picker clearable
                        v-model="queryParams.openTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择开通时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="过期时间" prop="expireTime">
        <el-date-picker clearable
                        v-model="queryParams.expireTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择过期时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="是否启用" prop="status">
        <el-select v-model="queryParams.status" placeholder="是否启用" clearable>
          <el-option
              v-for="dict in dict.type.sys_normal_disable"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
            type="primary"
            plain
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd"
            v-hasPermi="['system:tenant:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="success"
            plain
            icon="el-icon-edit"
            size="mini"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['system:tenant:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="danger"
            plain
            icon="el-icon-delete"
            size="mini"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['system:tenant:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            v-hasPermi="['system:tenant:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="tenantList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="租户ID" align="center" prop="tenantId" show-overflow-tooltip/>
      <el-table-column label="租户名称" align="center" prop="tenantName"/>
      <el-table-column label="联系地址" align="center" prop="address" show-overflow-tooltip/>
      <el-table-column label="联系电话" align="center" prop="phoneNumber"/>
      <el-table-column label="开通时间" align="center" prop="openTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.openTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="开通人" align="center" prop="createBy" width="180"/>
      <el-table-column label="过期时间" align="center" prop="expireTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.expireTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="是否启用" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" show-overflow-tooltip/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <span v-show="scope.row.isFix === 0">
          <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['system:tenant:edit']"
          >修改
          </el-button>
          <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['system:tenant:remove']"
          >删除
          </el-button>
          </span>
        </template>
      </el-table-column>
    </el-table>

    <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
    />

    <!-- 添加或修改租户信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="60%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="租户名称" prop="tenantName">
              <el-input v-model="form.tenantName" placeholder="请输入租户名称" maxlength="50"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="联系地址">
              <el-input v-model="form.address" placeholder="请输入联系地址" maxlength="255"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="联系电话" prop="phoneNumber">
              <el-input v-model="form.phoneNumber" placeholder="请输入联系电话" maxlength="15"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="开通时间" prop="openTime">
              <el-date-picker clearable
                              style="width: 278px;"
                              v-model="form.openTime"
                              type="date"
                              format="yyyy-MM-dd"
                              value-format="yyyy-MM-dd 00:00:00"
                              :picker-options="openFormOptions"
                              placeholder="请选择开通时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="过期时间" prop="expireTime">
              <el-date-picker clearable
                              style="width: 278px;"
                              v-model="form.expireTime"
                              type="date"
                              :picker-options="expireFormOptions"
                              format="yyyy-MM-dd"
                              value-format="yyyy-MM-dd 23:59:59"
                              placeholder="请选择过期时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" :autosize="{ minRows: 3, maxRows: 6}"
                        placeholder="请输入备注信息" maxlength="500"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="16">
            <el-form-item label="菜单权限" prop="menuIds">
              <el-checkbox v-model="menuExpand" @change="handleCheckedTreeExpand($event, 'menu')">展开/折叠
              </el-checkbox>
              <el-checkbox v-model="menuNodeAll" @change="handleCheckedTreeNodeAll($event, 'menu')">全选/全不选
              </el-checkbox>
              <el-checkbox v-model="form.menuCheckStrictly" @change="handleCheckedTreeConnect($event, 'menu')">父子联动
              </el-checkbox>
              <el-tree
                  class="tree-border"
                  :data="menuOptions"
                  show-checkbox
                  ref="menu"
                  node-key="id"
                  empty-text="加载中，请稍候"
                  :props="defaultProps"
              ></el-tree>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="是否启用" prop="status">
              <el-switch
                  v-model="form.status"
                  active-color="#13ce66"
                  inactive-color="#ff4949"
                  active-text="启用"
                  inactive-text="禁用"
                  active-value="0"
                  inactive-value="1">
              </el-switch>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {addTenant, delTenant, getTenant, listTenant, updateTenant} from "@/api/system/tenant";
import {treeselect as menuTreeselect} from "@/api/system/menu";

export default {
  name: "Tenant",
  dicts: ['sys_normal_disable'],
  data() {
    return {
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 租户信息表格数据
      tenantList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tenantName: null,
        address: null,
        phoneNumber: null,
        openTime: null,
        expireTime: null,
        remark: null,
        status: null,
      },
      // 菜单列表
      menuOptions: [],
      // 是否显示弹出层（数据权限）
      openDataScope: false,
      menuExpand: false,
      menuNodeAll: false,
      deptExpand: true,
      deptNodeAll: false,
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        tenantName: [
          {required: true, message: "租户名称不能为空", trigger: "blur"}
        ],
        address: [
          {required: true, message: "联系地址不能为空", trigger: "blur"}
        ],
        phoneNumber: [
          {required: true, message: "联系电话不能为空", trigger: "blur"}
        ],
        openTime: [
          {required: true, message: "开通时间不能为空", trigger: "blur"}
        ],
        expireTime: [
          {required: true, message: "过期时间不能为空", trigger: "blur"}
        ],
        menuIds: [
          {required: true, message: "平台菜单权限不能为空", trigger: "blur"}
        ]
      },
      openFormOptions: {
        disabledDate: (time) => {
          if (this.form.expireTime) {
            return time.getTime() > new Date(this.form.expireTime).getTime()
          }
        }
      },
      expireFormOptions: {
        disabledDate: (time) => {
          if (this.form.openTime) {
            return time.getTime() < new Date(this.form.openTime).getTime() - 86400000;
          }
        }
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询租户信息列表 */
    getList() {
      this.loading = true;
      listTenant(this.queryParams).then(response => {
        this.tenantList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        tenantId: null,
        menuIds: [],
        tenantName: null,
        address: null,
        phoneNumber: null,
        openTime: null,
        expireTime: null,
        remark: null,
        status: '0',
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
      };
      this.resetForm("form");
    },
    // 树权限（展开/折叠）
    handleCheckedTreeExpand(value, type) {
      if (type == 'menu') {
        let treeList = this.menuOptions;
        for (let i = 0; i < treeList.length; i++) {
          this.$refs.menu.store.nodesMap[treeList[i].id].expanded = value;
        }
      } else if (type == 'dept') {
        let treeList = this.deptOptions;
        for (let i = 0; i < treeList.length; i++) {
          this.$refs.dept.store.nodesMap[treeList[i].id].expanded = value;
        }
      }
    },
    // 树权限（全选/全不选）
    handleCheckedTreeNodeAll(value, type) {
      if (type == 'menu') {
        this.$refs.menu.setCheckedNodes(value ? this.menuOptions : []);
      } else if (type == 'dept') {
        this.$refs.dept.setCheckedNodes(value ? this.deptOptions : []);
      }
    },
    // 树权限（父子联动）
    handleCheckedTreeConnect(value, type) {
      if (type == 'menu') {
        this.form.menuCheckStrictly = value ? true : false;
      } else if (type == 'dept') {
        this.form.deptCheckStrictly = value ? true : false;
      }
    },
    // 所有菜单节点数据
    getMenuAllCheckedKeys() {
      // 目前被选中的菜单节点
      let checkedKeys = this.$refs.menu.getCheckedKeys();
      // 半选中的菜单节点
      let halfCheckedKeys = this.$refs.menu.getHalfCheckedKeys();
      checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
      return checkedKeys;
    },
    /**
     * 获得菜单树
     */
    getMenuTreeselect() {
      return menuTreeselect().then(response => {
        this.menuOptions = response.data;
        return response;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.tenantId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.getMenuTreeselect();
      this.$nextTick(() => {
        this.$refs.menu.setCheckedKeys([]);
      });
      this.form.status = '0';
      this.open = true;
      this.title = "添加租户信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const menuTreeRes = this.getMenuTreeselect();
      const tenantId = row.tenantId || this.ids
      getTenant(tenantId).then(response => {
        this.form = response.data;
        this.form.status = '0';
        //tip: 设置回显选中状态
        this.$nextTick(() => {
          menuTreeRes.then(res => {
            let menuIds = response.data.menuIds;
            menuIds.forEach((v) => {
              this.$nextTick(() => {
                this.$refs.menu.setChecked(v, true, false);
              })
            })
          });
        });
        this.open = true;
        this.title = "修改租户信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.form.menuIds = this.getMenuAllCheckedKeys();
      if (this.form.menuIds.length === 0) {
        this.$modal.msgWarning("请选择租户所拥有的菜单");
      }
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.tenantId != null) {
            updateTenant(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTenant(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const tenantIds = row.tenantId || this.ids;
      this.$modal.confirm('是否确认删除租户信息编号为"' + tenantIds + '"的数据项？').then(function () {
        return delTenant(tenantIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/tenant/export', {
        ...this.queryParams
      }, `tenant_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
<style scoped lang="scss">
</style>