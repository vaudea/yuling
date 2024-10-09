<template>
  <div class="secondaryMenu">
      <el-menu-item v-if="!menu.children || menu.children.length === 0" :key="menu.id" :index="menu.router">
        <i :class="menu.icon"></i>
        <span slot="title" v-if="!isCollapse">{{ menu.title }}</span>
      </el-menu-item>
    <el-submenu v-else :key="menu.id" :index="menu.router">
      <template slot="title">
        <i :class="menu.icon"></i>
        <span v-if="!isCollapse">{{ menu.title }}</span>
      </template>
      <sub-menu v-for="child in menu.children" :key="child.id" :menu="child" :is-collapsed="isCollapse" class="secondaryMenu" />
    </el-submenu>

  </div>
</template>

<script>
export default {
  name: "Menu",
  props: {
    menu: {
      type: Object,
      required: true
    },
    isCollapse: {
      type: Boolean,
      default: false
    }
  },

  components: {
    SubMenu: () => import('./Menu') // 组件递归引用自身处理子菜单
  },
  created() {
    // console.log(this.isCollapse);
  }
}
</script>

<style scoped>
.el-submenu>>> li {
  background-color: #14161a !important;
}

.el-submenu>>> li:hover {
  background-color: #000000 !important;
}
.el-menu-item:hover{
  background-color: #000000!important;
}
</style>
