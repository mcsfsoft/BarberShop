<template>
  <div class="login">
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
      <h3 class="title">{{ title }}</h3>
      <el-form-item prop="tenantId">
        <el-select
            v-model="loginForm.tenantId"
            placeholder="请输入平台名称"
            filterable
            remote
            clearable
            :loading="tenantLoading"
            :remote-method="queryTenantAsync"
            @change="tenantSelect"
        >
          <svg-icon slot="prefix" icon-class="company" class="el-input__icon input-icon"/>
          <el-option
              v-for="item in tenantList"
              :key="item.tenantId"
              :label="item.tenantName"
              :value="item.tenantId">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="username">
        <el-input
            v-model="loginForm.username"
            type="text"
            auto-complete="off"
            placeholder="账号"
        >
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon"/>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="loginForm.password"
          type="password"
          auto-complete="off"
          placeholder="密码"
          @keyup.enter.native="handleLogin"
        >
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox>
      <el-form-item style="width:100%;">
        <el-button
          :loading="loading"
          size="medium"
          type="primary"
          style="width:100%;"
          @click.native.prevent="handleLogin"
        >
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
        <div style="float: right;" v-if="register">
          <router-link class="link-type" :to="'/register'">立即注册</router-link>
        </div>
      </el-form-item>
    </el-form>
    <!--  底部  -->
    <div class="el-login-footer">
    </div>
    <!--		 滑动验证码-->
    <Verify
        @success="success"
        mode="pop"
        captchaType="blockPuzzle"
        :imgSize="{ width: '330px', height: '155px' }"
        ref="verify"
    ></Verify>
  </div>
</template>

<script>
import Verify from '@/components/verify/verifition/Verify.vue'
import {getTenant} from "@/api/login";
import Cookies from "js-cookie";
import {decrypt, encrypt} from '@/utils/jsencrypt'

export default {
  name: "Login",
  components: {
    Verify
  },
  data() {
    return {
      title: '理发店管理系统',
      codeUrl: "",
      tenantList: [],
      tenantLoading: false,
      loginForm: {
        username: "admin",
        password: "admin123",
        tenantId: "",
        rememberMe: false,
        code: "",
        uuid: ""
      },
      loginRules: {
        username: [
          {required: true, trigger: "blur", message: "请输入您的账号"}
        ],
        password: [
          {required: true, trigger: "blur", message: "请输入您的密码"}
        ],
        code: [{required: true, trigger: "change", message: "请输入验证码"}],

        tenantId: [{required: true, trigger: "blur", message: "请输入平台"}]
      },
      loading: false,
      // 注册开关
      register: false,
      redirect: undefined
    };
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true
    }
  },
  created() {
    this.queryTenantAsync();
    this.getCookie();
  },
  methods: {
    /**
     * 租户选中事件
     * @param item 当前租户ID
     */
    tenantSelect(item) {
      this.title = '尊敬的' + this.tenantList.find(tenant => tenant.tenantId === item).tenantName + '用户,欢迎访问';
    },
    /**
     * 远程查询租户列表
     * @param query 模糊搜索字段
     */
    queryTenantAsync(query) {
      if (query) {
        this.tenantLoading = true;
        getTenant({'tenantName': query}).then(res => {
          this.tenantList = res.data;
          this.tenantLoading = false;
        }).catch(e => {
          console.error(e)
          this.tenantLoading = false;
        })
      }
    },
    getCookie() {
      const username = Cookies.get("username");
      const password = Cookies.get("password");
      const rememberMe = Cookies.get('rememberMe')
      const tenantStr = Cookies.get('tenant')
      let tenant;
      if (tenantStr) {
        tenant = JSON.parse(tenantStr);
        this.tenantList.push(tenant);
      }
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password: password === undefined ? this.loginForm.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe),
        tenantId: tenant === undefined ? this.loginForm.tenantId : tenant.tenantId
      };
    },
    //滑动验证码登录
    async success(params) {
      this.loading = true;
      if (this.loginForm.rememberMe) {
        Cookies.set("username", this.loginForm.username, {expires: 30});
        Cookies.set("password", encrypt(this.loginForm.password), {expires: 30});
        Cookies.set('rememberMe', this.loginForm.rememberMe, {expires: 30});
        Cookies.set('tenant', JSON.stringify(this.tenantList.find(item => item.tenantId === this.loginForm.tenantId)), {expires: 30});
      } else {
        Cookies.remove("username");
        Cookies.remove("password");
        Cookies.remove('rememberMe');
        Cookies.remove('tenant')
      }
      //向对象中添加字段值
      Object.assign(this.loginForm, {captchaVerification: params.captchaVerification})
      this.$store.dispatch("Login", this.loginForm).then(() => {
        this.$router.push({path: this.redirect || "/"}).catch(() => {
        });
      }).catch(() => {
        this.loading = false;
      });
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.$refs.verify.show();
        }
      });
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss">
//平台端选择框宽度调整
div.el-select {
  width: 100%;
}

.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url("../assets/images/login-background.jpg");
  background-size: cover;
}

.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #707070;
}

.login-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 25px 25px 5px 25px;
  .el-input {
    height: 38px;
    input {
      height: 38px;
    }
  }
  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
}
.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.login-code {
  width: 33%;
  height: 38px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}
.login-code-img {
  height: 38px;
}
</style>
