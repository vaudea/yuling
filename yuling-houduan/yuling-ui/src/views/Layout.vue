<template>
  <div id="app">
    <div v-if="loading" class="fade-in">
      <div class="loader"></div>
    </div>
    <div id="addesc">
      <el-container style="height: 100vh;">
        <Sidebar v-if="!menusill" :isCollapse="isCollapse" />
        <el-drawer v-if="menusill" :visible.sync="drawerVisible" :size="200" :with-header="false" direction="ltr"
          @close="handleCloseDrawer">
          <Sidebar @close-drawer="drawerVisible = false" />
        </el-drawer>
        <el-container class="main-vue">
          <el-main>
            <Header style="height: 50px;" />
            <!-- 标签栏 -->
            <div class="tabs">
              <el-tabs class="router-tabs" type="card" style="margin-left: 15px;margin-right: 15px;" v-model="activeIndex" @tab-remove="removeTab">
                <el-tab-pane v-for="item in openTab" :key="item.title" :label="item.title" :name="item.index"
                  :closable="item.title !== '首页'">
                  <div class="maincontent">
                    <!-- keep-alive 保持路由状态 -->
                    <keep-alive>
                      <router-view name="main"></router-view>
                    </keep-alive>
                  </div>
                </el-tab-pane>
              </el-tabs>
            </div>
            <transition name="fade-transform" mode="out-in">
              <router-view style="margin-left: 20px; margin-top: 20px;" />
            </transition>
          </el-main>
        </el-container>
      </el-container>
    </div>
  </div>
</template>

<script>
import Sidebar from '@/components/Sidebar.vue';
import Header from '@/components/Header.vue';
import request from '@/utils/request';
import { mapState } from 'vuex';
export default {
  name: 'App',
  components: {
    Sidebar,
    Header,
  },
  data() {
    return {
      loading: true,
      menusill: window.innerWidth <= 1020,
      menusetdrawer: false,
    };
  },
  computed: {
    ...mapState([
      'isCollapse'  // 直接映射 Vuex 中的 isCollapse 状态到组件的 computed 属性
    ]),
    drawerVisible: {
      get() {
        return !this.isCollapse;
      },
      set(value) {
        this.$store.commit('SET_ISCOLLAPSE', !value);
      }
    },
    openTab: {
      get() {
        // console.log(this.$store.state.openTab);
        return this.$store.state.openTab;
      },
      set(newValue) {
        this.$store.state.openTab = newValue;
      },
    },
    activeIndex: {
      get() {
        return this.$store.state.activeIndex;
      },
      set(newValue) {
          this.$store.state.activeIndex = newValue;
          this.$router.push(newValue);
      },
    }
  },
  methods: {
    addTab(data) {
      this.$store.commit("addTab", data);
    },
    removeTab(targetName) {
      this.$store.commit('REMOVE_TAB', targetName);
    },
    handleCloseDrawer() {
      this.$store.commit('SETINISCOLLAPSE', true);
    },
    updateMenuDisplay() {
      this.menusill = window.innerWidth <= 1020;
      if (window.innerWidth <= 1100) {
        this.$store.commit('SETINISCOLLAPSE', true);
      }
    },
    handleResize() {
      this.updateMenuDisplay();
    },
    selectByJobnumber(user) {
      if (user) {
        request.get(`/employee/getEmployee?jobNumber=${user.jobNumber}`).then((res) => {
          if (user.positionId === null || res.data === null) {
            this.$model.msgError("登录已过期，请重新登录");
          } else if (user.positionId != res.data.positionId || user.fatherId != res.data.fatherId) {
            this.logout();
            this.$message.error('您的职位或部门信息已发生更改，请重新登录');
          }
        });
      }
    },
    logout() {
      this.$cookies.remove('user');
      localStorage.removeItem('user');
      localStorage.removeItem('avatar');
      localStorage.removeItem('avatarName');
      this.$store.commit('SETINISCOLLAPSE', false);
      this.$store.commit('CLEAR_AVATAR');
      this.$store.commit('CLEAR_AVATARNAME');
      this.$router.push('/login');
    },
    handleRouteChange(to) {
      this.activeIndex = to.path;
      const data = { index: to.path, title: to.meta.title};
      this.$store.commit('addTab', data);
    }
  },
  mounted() {
    this.handleRouteChange(this.$route);
    this.updateMenuDisplay();
    window.addEventListener('resize', this.handleResize);
    this.selectByJobnumber(JSON.parse(this.$cookies.get('user') || '{}'));
    setTimeout(() => {
      this.loading = false;
    }, 300);
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize);
  },
  watch: {
    // 监视路由变化
    '$route'(to) {
      
      this.handleRouteChange(to);
    }
  }
}
</script>

<style scoped>
.main-vue{
  position: relative;
  padding-left: 20px; /* 这里可以调整内容与阴影的距离 */
}
.tabs {
  background-color: #f7f3f0;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.08), inset 10px 0 8px -8px rgba(0, 0, 0, 0.5);

  /* 底部阴影 */
}

/* 动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 1.5s ease;
}

.fade-enter,
.fade-leave-to {
  opacity: 0;
}


* {
  scrollbar-width: none;
  padding: 0px !important;
}

.el-drawer__body,
.el-drawer__body * {
  overflow: hidden !important;
  scrollbar-width: none !important;
}

.el-drawer__body::-webkit-scrollbar,
.el-drawer__body *::-webkit-scrollbar {
  display: none;
}

#addesc {
  overflow-y: hidden;
}


.el-main {
  z-index: 10;
  overflow-x: hidden;
  overflow-y: auto;
    background-color: #f7f3f0!important;

}

.el-main::-webkit-scrollbar {
  width: 5px;
  /* 确保这个宽度不是0或太小 */
}

.el-main::-webkit-scrollbar-thumb {
  background-color: rgba(204, 204, 204, 0.6);
  /* 确保颜色提供足够的对比 */
  border-radius: 5px;
}

.el-main::-webkit-scrollbar-track {
  background: transparent;
  /* 确保轨道透明不会隐藏滑块 */
}


.fade-in {
  animation: fadeIn 0.05s;
  display: flex;
  align-items: center;
  justify-content: center;
  background: white;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 9999;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }

  to {
    opacity: 1;
  }
}

.loader {
  border: 16px solid #f3f3f3;
  /* Light grey background */
  border-top: 16px solid #3498db;
  /* Blue color */
  border-radius: 50%;
  width: 120px;
  height: 120px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }

  100% {
    transform: rotate(360deg);
  }
}
</style>