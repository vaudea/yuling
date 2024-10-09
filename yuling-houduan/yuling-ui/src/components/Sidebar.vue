<template>
  <div id="app">

    <!-- 固定子菜单 -->
    <div class="system" :style="{ width: isCollapse ? '64px' : '200px' }">
      
      <router-link to="/" class="system-link" :style="{ justifyContent: isCollapse ? 'center' : 'center' }">
        <img src="../assets/images/logo.png" class="system-logo" :style="{ marginRight: isCollapse ? '0' : '8px' }">
        <h1 class="systemName" style="font-size: 14px;">{{ title }}</h1>
      </router-link>
    </div>

    <!-- 固定背景颜色和样式 -->
    <div :class="{ 'collapsed': isCollapse, 'expanded': !isCollapse }" class="sidebar-background">
      <!-- 动画效果标签 -->

      <Transition name="menumain">
        <el-menu :default-active="activeIndex" :collapse="isCollapse" :background-color="'#222831'"
          :text-color="'#fff'" :unique-opened="true" :collapse-transition="false" :active-text-color="'#F7BDC1'"
          mode="vertical" router class="sidebarMenu el-menu-vertical-demo" 
          @select="handleMenuSelect">
          <el-menu-item index="/home" class="el-menu-item-home">
            <i class="el-icon-s-home"></i>
            <span slot="title">首页</span>
          </el-menu-item>

          <!-- 创建菜单实例 -->
          <app-menu v-for="menu in menuItems" :key="menu.id" :menu="menu" :isCollapse="isCollapse" />


        </el-menu>
      </Transition>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request';
import AppMenu from '@/components/Menu.vue';
export default {
  components: {
    "app-menu": AppMenu
  },
  computed: {
  },
  name: 'Sidebar',
  props: {
    isCollapse: {
      type: Boolean,

    }
  },
  created() {
    const jobNumber = JSON.parse(this.$cookies.get('user')).jobNumber;
    this.selectMenu();
    this.selectByJobnumber(jobNumber);
    this.setTitleBasedOnCollapse();
  },
  methods: {
    addTab(menu) {
      this.$store.commit("addTab", menu);
    },
    handleMenuSelect() {
      // 选择任一菜单项都将关闭抽屉
      this.$emit('close-drawer');
      // 可以继续添加其他逻辑，比如页面跳转等
    },
    setTitleBasedOnCollapse() {
      if (!this.isCollapse) {
        setTimeout(() => {
          this.title = "虞翎员工管理系统";
        }, 120);
      } else {
        this.title = "";
      }
    },
    adjustScrollbar() {
      this.$nextTick(() => {
        const scrollbar = this.$refs.scrollbar;
        if (scrollbar && scrollbar.$el) {
          const el = scrollbar.$el.querySelector('.el-scrollbar__wrap');
          el.style.overflowY = 'auto';
          el.style.overflowX = 'hidden'; // 隐藏横向滚动条
          el.style.height = `calc(100vh - 50px)`;
        }
      });
    },
    selectByJobnumber(jobNumber) {
      if (jobNumber) {
        request.get(`/employee/getEmployee?jobNumber=${jobNumber}`).then((res) => {
          this.user = res.data;
        });
      }
    },
    handleMenuSelect() {
      this.$emit('close-drawer'); // 选择任一菜单项都将关闭抽屉
    },
    // 获取菜单
    selectMenu() {
    const jobNumber = JSON.parse(this.$cookies.get('user')).jobNumber;
    
      request.get(`/menu/getMenu?jobNumber=${jobNumber}`).then((res) => {
        if (res.code == 200) {
          this.menuItems = res.data.body;
        } else {
          if (res.msg !== "token验证失败，请重新登录") {
            this.$model.msgError("获取菜单失败，请重新登录尝试");
          }
        }
      })
    },
  },
  watch: {
    // 监视路由变化
    '$route'(to, from) {
      this.activeIndex = to.path; // 更新activeIndex为新路由的路径
      const data = { index: to.path, title: to.meta.title }; 
      this.$store.commit('addTab', data);
    },
    isCollapse: {
      handler(newValue) {
        this.setTitleBasedOnCollapse(); // 当isCollapse改变时，根据状态设置标题
      },
      immediate: true // 立即应用，确保组件初始化时也进行处理
    }
  },
  mounted() {
    const jobNumber = JSON.parse(this.$cookies.get('user')).jobNumber;
    this.selectByJobnumber(jobNumber);
    this.setTitleBasedOnCollapse(); // 确保在组件挂载完成后再次根据折叠状态设置标题
  },
  data() {
    return {
      title: "",
      user: {},
      menuItems: [],
      activeIndex: this.$route.path
    };
  },


}
</script>


<style scoped>
/* 这个是动画效果 */
.menumain-enter-active {
  opacity: 0;
  transition: all .3s;
}

.menumain-enter-to {
  opacity: 1;
}
.el-menu-item:hover{
  background-color: rgb(0, 0, 0)!important;
}

.system {
  color: #FFFFFF;
  text-align: center;
  background-color: #222831;
  display: flex;
  align-items: center;
  height: 50px;
  transition: width 0.3s;
}

.system-link {
  display: flex;
  align-items: center;
  width: 100%;
  transition: justify-content 0.3s;
}


.system-logo {
  width: 32px;
  height: 32px;
  transition: margin-right 0.3s;
}

@media all and (max-width:870px) {
  .sidebar-background {
    height: calc(100vh - 50px);
  }
}

.collapsed {
  width: 64px;
}

.expanded {
  width: 200px;
}

.systemName {
  font-size: 14px;
  font-family: Avenir, Helvetica Neue, Arial, Helvetica, sans-serif;
  font-weight: 600;
  display: inline-block;
}


.el-menu {
  border-right: none;
  height: calc(100vh - 30px);
}


.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}

/* 修改 sidebar-background 样式 */
.sidebar-background {
  background-color: #222831;
  transition: width 0.3s;
  height: calc(100vh - 30px);
  overflow-y: auto;
  overflow-x: hidden;
  position: relative;
  /* 确保滚动条悬浮在内容上方 */
}

/* 滚动条整体样式 */
.sidebar-background::-webkit-scrollbar {
  width: 6px;
  background-color: transparent;
  /* 背景透明 */
  position: absolute;
  /* 绝对定位，悬浮效果 */
  right: 0;
  /* 紧贴右侧 */
}

/* 滚动条滑块 */
.sidebar-background::-webkit-scrollbar-thumb {
  background-color: rgba(204, 204, 204, 0.3);
  /* 灰色，30%透明度 */
  border-radius: 4px;
}

/* 滚动条轨道 */
.sidebar-background::-webkit-scrollbar-track {
  background-color: transparent;
}

/* el-menu 样式 */
.el-menu {
  border-right: none;
  height: calc(100vh - 30px);
  margin-right: -8px;
  /* 预留空间给滚动条 */
}

/* 滚动条容器 */
.sidebar-background .el-scrollbar__wrap {
  overflow: hidden;
  /* 隐藏默认滚动条 */
  padding-right: 8px;
  /* 预留空间给自定义滚动条 */
}

.sidebar-background .el-scrollbar__view {
  margin-right: -8px;
  /* 确保内容宽度不会被滚动条挤压 */
}
</style>