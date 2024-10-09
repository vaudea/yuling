<template>
  <div class="login">
    <el-form :model="loginForm" :rules="loginRules" ref="loginRef" class="login-form">
      <h3 class="title">虞翎员工管理系统</h3>
      <el-form-item prop="username">
        <el-input class="loginInput" v-model="loginForm.username" type="text" size="large" auto-complete="off"
          placeholder="请输入你的工号" prefix-icon="el-icon-user"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input class="loginInput" v-model="loginForm.password" type="password" size="large" auto-complete="off"
          placeholder="密码" prefix-icon="el-icon-lock" @keyup.enter.native="login" show-password>
        </el-input>
      </el-form-item>
      <el-form-item prop="code" v-if="captchaEnabled">
        <el-input class="loginInput" v-model="loginForm.code" size="large" auto-complete="off" placeholder="验证码"
          style="width: 63%" @keyup.enter.native="login" prefix-icon="el-icon-picture"></el-input>
        <div class="login-code">
          <img :src="captUrl" @click="clickImg" class="login-code-img" />
        </div>
      </el-form-item>
      <el-form-item style="width:100%;">
        <el-button :loading="loading" size="large" type="primary" style="width:100%;" @click.prevent="login">{{ loading
          ? '登 录 中...' : '登 录' }}</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import request from '@/utils/request';
import { resetDynamicRoutes } from '@/router';
import { getIV16, getKey, aesEncryption } from '@/utils/aesPassword'
export default {
  data() {
    return {
      loginForm: {
        username: "",
        password: "",
        code: "",
      },
      key: '',
      captUrl: '',
      loginRules: {
        username: [
          { required: true, trigger: "blur", message: "请输入您的账号" },
          { pattern: /^\d{11}$/, message: "账号必须为11位数字", trigger: "blur" }
        ],
        password: [
          { required: true, trigger: "blur", message: "请输入您的密码" },
          { pattern: /^[a-zA-Z0-9]{6,16}$/, message: "密码必须为6-16位字母或数字", trigger: "blur" }
        ],
        code: [{ required: true, trigger: "change", message: "请输入验证码" }]
      },
      codeUrl: "",
      loading: false,
      captchaEnabled: true,
    };
  },
  methods: {
    login() {
      this.$refs.loginRef.validate((valid) => {
        if (valid) {
          this.loading = true;
          setTimeout(() => {
            const iv = getIV16();
            const key = getKey(iv, this.loginForm.username);
            const loginInfo = JSON.parse(JSON.stringify(this.loginForm))
            loginInfo.iv = iv;
            loginInfo.password = aesEncryption(iv, key, this.loginForm.password);
            request.post(`/user/login?key=${this.key}`, loginInfo).then((res) => {
              if (res.code == 200) {
                // console.log(res.data);
                this.$store.commit('SETINISCOLLAPSE', false);
                this.$cookies.set('user', JSON.stringify(res.data));
                this.$store.dispatch('GetInfo')
                  .then(() => {
                    this.loading = false;
                    this.$store.dispatch('login')
                    if (this.$store.state.activeIndex === '/home') {
                      resetDynamicRoutes();
                      // console.log(this.$store.state.activeIndex);
                      this.$store.commit('RESET_TO_DEFAULTS');
                      this.$router.push('/');
                    } else {
                      resetDynamicRoutes();
                      // console.log(this.$store.state.activeIndex);
                      this.$router.push(this.$store.state.activeIndex);
                    }
                    setTimeout(() => {
                      this.$message.success("登录成功");
                    }, 200)
                    setTimeout(() => {
                      location.reload();  // 全局刷新页面
                    }, 150)
                  })
                  .catch(error => {
                    console.error(error);
                  });
              } else {
                this.$refs['loginRef'].resetFields();
                this.clickImg();
                this.$model.msgError(res.msg);
                this.loading = false;
              }
            })
          }, 800);
        }
      });
    }
    ,
    reset() {
      this.loginForm = {
        username: "",
        password: "",
        code: "",
      }
    },
    clickImg() {
      this.key = Math.random();
      this.captUrl = '/api/yuling/captcha?key=' + this.key;
    },
  },
  created() {
    this.clickImg();
  }
}
</script>

<style scoped>
.loginInput /deep/ .el-input__inner {
  background-color: white !important;
  border: 1px solid #DCDFE6;
}
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-image: url("../assets/images/login-background.jpg");
  background-size: 100vw 100vh;
  background-position: center;
  background-repeat: no-repeat;
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
}

.login-form .el-input {
  height: 40px;
}

.login-form .el-input input {
  height: 40px;
}

.login-form .input-icon {
  height: 39px;
  width: 14px;
  margin-left: 0px;
}

.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}

.login-code {
  width: 33%;
  height: 40px;
  float: right;
}

.login-code img {
  cursor: pointer;
  vertical-align: middle;
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
  height: 40px;
  padding-left: 12px;
  border: 1px solid #DCDFE6;
}
</style>
