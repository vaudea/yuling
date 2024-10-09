<template>
  <div class="app-container">
    <el-row :gutter="20" class="user-carfd">
      <el-col :span="6" :xs="24">
        <el-card class="box-card user-carfd">
          <div slot="header" class="clearfix">
            <span>个人信息</span>
          </div>
          <div>
            <div class="text-center">
              <userAvatar />
            </div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <el-icon class="el-icon-user" />
                用户名称
                <div class="pull-right">{{ user.name }}</div>
              </li>
              <li class="list-group-item">
                <el-icon class="el-icon-phone" />
                手机号码
                <div class="pull-right">{{ user.phone }}</div>
              </li>
              <li class="list-group-item">
                <el-icon class="el-icon-message" />
                用户邮箱
                <div class="pull-right">{{ user.email }}</div>
              </li>
              <li class="list-group-item">
                <el-icon class="el-icon-s-cooperation" />
                所属部门
                <div class="pull-right" v-if="user.departmentName">{{ user.departmentName }}</div>
              </li>
              <li class="list-group-item">
                <el-icon class="el-icon-suitcase" />
                职位
                <div class="pull-right">{{ user.positionName }}</div>
              </li>
              <li class="list-group-item">
                <el-icon class="el-icon-date" />
                出生日期
                <div class="pull-right">{{ user.birthdate }}</div>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col class="userinfo" :span="18" :xs="24" style="width: 70%;">
        <el-card>
          <div slot="header" class="clearfix">
            <span>基本资料</span>
          </div>
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本资料" name="userinfo">
              <!-- 基本资料表单 -->
              <el-form ref="userInfo" :model="userForm" :rules="rulesUser" label-width="80px">
                <el-form-item label="姓名" prop="name">
                  <el-input v-model="userForm.name"></el-input>
                </el-form-item>
                <el-form-item label="手机号码" prop="phone">
                  <el-input v-model="userForm.phone"></el-input>
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                  <el-input v-model="userForm.email"></el-input>
                </el-form-item>
                <el-form-item label="性别" prop="gender">
                  <el-radio-group v-model="userForm.gender">
                    <el-radio :label="'0'">男</el-radio>
                    <el-radio :label="'1'">女</el-radio>
                  </el-radio-group>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" size="mini" @click="handleUpdate">保存</el-button>
                  <el-button type="danger" size="mini" @click="$router.push('/')">关闭</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
            <el-tab-pane label="修改密码" name="resetPwd" label-weight="100">
              <!-- 修改密码表单 -->
              <el-form ref="resetPwdForm" :rules="rulesPassword" :model="passwords" label-width="80px">
                <el-form-item label="当前密码" prop="password">
                  <el-input v-model="passwords.password" show-password placeholder="请输入旧密码"></el-input>
                </el-form-item>
                <el-form-item label="新密码" prop="newPsd">
                  <el-input v-model="passwords.newPsd" show-password placeholder="请输入新密码"></el-input>
                </el-form-item>
                <el-form-item label="确认密码" prop="confirmPsd">
                  <el-input v-model="passwords.confirmPsd" show-password placeholder="请确认新密码"></el-input>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" size="mini" @click="resetPassword">保存</el-button>
                  <el-button type="danger" size="mini" @click="$router.push('/')">关闭</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import userAvatar from "./userAvatar";
import request from '@/utils/request';
import { getIV16, getKey, aesEncryption } from '@/utils/aesPassword'
export default {
  name: "Profile",
  components: { userAvatar },
  data() {
    return {
      user: {},
      userForm: {},
      activeTab: "userinfo",
      passwords: {},
      // 表单校验规则
      rulesPassword: {
        password: [
          { required: true, message: "当前密码不能为空", trigger: "blur" }
        ],
        newPsd: [
          { required: true, message: "新密码不能为空", trigger: "blur" },
          { pattern: /^[a-zA-Z0-9]{6,16}$/, message: "密码必须为6-16位字母或数字", trigger: "blur" }
        ],
        confirmPsd: [
          { required: true, message: "确认密码不能为空", trigger: "blur" },
          { pattern: /^[a-zA-Z0-9]{6,16}$/, message: "密码必须为6-16位字母或数字", trigger: "blur" },
          { validator: this.validateConfirmPassword, trigger: 'blur' }
        ]
      },
      rulesUser: {
        name: [
          { required: true, message: '姓名不能为空', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '手机号码不能为空', trigger: 'blur' },
          { pattern: /^[0-9]{11}$/, message: '手机号码必须为11位数字', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '邮箱不能为空', trigger: 'blur' },
          { type: 'email', message: '邮箱格式不正确', trigger: ['blur', 'change'] }
        ],
        gender: [
          { required: true, message: '请选择性别', trigger: 'change' }
        ]
      }

    };
  },
  created() {
    this.getUserInfo();
  },
  methods: {
    //获取个人信息
    getUserInfo() {
      let cookieUser = JSON.parse(this.$cookies.get('user'));
      request.get(`/employee/getEmployee?jobNumber=${cookieUser.jobNumber}`).then((res) => {
        if (res.code == 200) {
          this.user = res.data;
          this.userForm = { ...this.user };
        } else {
          if (res.msg !== "token验证失败，请重新登录") {
            this.$model.msgError(res.msg);
          }
        }
      });
    },
    handleUpdate() {
      this.$refs.userInfo.validate((valid) => {
        if (valid) {
          this.$model.confirm('确认要修改个人信息吗?').then((res) => {
            const updatePermission = this.canUpdate('lastUserInfoUpdate');
            if (!updatePermission.allowed) return; // 检查更新权限
            this.userForm.userJobNumber = this.userForm.jobNumber;
            return request.put("/employee/employeeUpdate", this.userForm)
          }).then((res) => {
            if (res.code == 200) {
              this.$model.msgSuccess("修改成功");
              this.getUserInfo();
            } else {
              this.$model.msgError(res.msg);
            }
          }).catch((err) => {
            if (err !== 'cancel') { };
          });
        } else {
          this.$model.msgError("数据未正常输入，请检查并重试。");
        }
      });
    },

    resetPassword() {
      this.$refs.resetPwdForm.validate((valid) => {
        if (valid) {
          this.$model.confirm('确认要修改密码吗?').then((res) => {
            // const passwordUpdatePermission = this.canUpdate('lastPasswordUpdate');
            // if (!passwordUpdatePermission.allowed) return; // 检查更新权限
            this.passwords.jobNumber = this.user.jobNumber;
            const iv = getIV16();
            const key = getKey(iv,this.passwords.jobNumber);
            const resetPwd = JSON.parse(JSON.stringify(this.passwords))
            resetPwd.iv = iv;
            resetPwd.newPsd = aesEncryption(iv, key, this.passwords.newPsd);
            resetPwd.password = aesEncryption(iv, key, this.passwords.password);
            return request.put('/user/resetPassword', resetPwd)
          }).then((res) => {
            if (res.code == 200) {
              this.$model.msgSuccess("密码修改成功，请重新登录");
              setTimeout(() => {
                // this.logout();
              }, 400);

            } else {
              this.$model.msgError(res.msg);
            }
          }).catch((err) => {
            if (err !== 'cancel') { };
          });
        } else {
          this.$model.msgError("数据未正常输入，请检查并重试。");
        }
      });
    },

    validateConfirmPassword(rule, value, callback) {
      if (value !== this.passwords.newPsd) {
        callback(new Error("新密码与确认密码不一致"));
      } else {
        callback();
      }
    },
    // 方法用于检查是否允许进行更新操作
    canUpdate(key) {
      const now = Date.now();
      const lastTime = parseInt(localStorage.getItem(key) || '0');
      const remaining = 300000 - (now - lastTime); // 300000 毫秒 = 5 分钟

      if (lastTime && remaining > 0) {
        const minutes = Math.floor(remaining / 60000);
        const seconds = Math.ceil((remaining % 60000) / 1000);
        const displayMinutes = seconds === 60 ? minutes + 1 : minutes;
        const displaySeconds = seconds === 60 ? 0 : seconds;
        this.$message.error(`请等待 ${displayMinutes} 分 ${displaySeconds} 秒后再试`);
        return { allowed: false, remaining };
      }

      localStorage.setItem(key, now.toString());
      return { allowed: true, remaining: 0 };
    },
    logout() {
      // 清除用户相关的缓存数据
      this.$cookies.remove('user');
      localStorage.removeItem('user');
      localStorage.removeItem('avatar');
      localStorage.removeItem('avatarName');
      this.$store.commit('CLEAR_ISCOLLAPSE');
      this.$store.commit('CLEAR_AVATAR');
      this.$store.commit('CLEAR_AVATARNAME');
      // 重定向到登录页面
      this.$router.push('/login');

    },


  }
};
</script>

<style scoped>
@media all and (max-width: 750px) {
  .userinfo {
    display: block !important;
    width: 100% !important;
  }
}
</style>