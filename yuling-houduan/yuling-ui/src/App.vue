<template>
  <div id="app" width="100%">
    <router-view width="100%" />
    
  </div>
</template>

<script>
import HomeView from '@/views/HomeView.vue';
import store from '@/store';
// 引入Sidebar组件
import Sidebar from '@/components/Sidebar.vue';
export default {
  name: 'App',
  components: {
    Sidebar,
    HomeView,
  },
  created() {
    // 添加事件监听器，监听窗口大小变化
    window.addEventListener('resize', this.handleResize);
    this.handleResize();
    store.dispatch('GetInfo')
      .then(() => {
      })
      .catch(error => {
      });
  },
  beforeDestroy() {
    // 组件销毁前移除监听器
    window.removeEventListener('resize', this.handleResize);
  },
  methods: {
    handleResize() {
      const width = window.innerWidth;
      if (width < 1200 && width > 1100) {
        this.$store.commit('SETINISCOLLAPSE', true);
      } else {
        // 可以添加其他逻辑来处理宽度大于或等于1200的情况
        // 例如：this.$store.commit('SETINISCOLLAPSE', false);
      }
    }
  },
}
</script>

<style scoped>

</style>
