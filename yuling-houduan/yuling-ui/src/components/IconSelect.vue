<template>
    <div class="icon-select">
      <el-input v-model="search" placeholder="请输入图标名称" />
      <el-row :gutter="20">
        <el-col
          :span="8"
          v-for="icon in filteredIcons"
          :key="icon.name"
          @click.native="containerClick(icon.name)"
          class="icon-item-container"
        >
          <i :class="icon.name" class="icon-item"></i>
          <span class="icon-text">{{ icon.alias }}</span>
        </el-col>
      </el-row>
    </div>
  </template>
  
  <script>
  import { elementIcons } from '@/assets/icon/element-icon'; // 导入图标列表
  
  export default {
    data() {
      return {
        search: '',
        icons: elementIcons,
      };
    },
    computed: {
      filteredIcons() {
        return this.icons.filter(
          (icon) => icon.name.includes(this.search) || icon.alias.includes(this.search)
        );
      },
    },
    methods: {
      containerClick(icon) {
        this.selectIcon(icon);
      },
      selectIcon(icon) {
        this.$emit('selected', icon);
      },
      reset() {
        this.search = '';
      },
    },
    mounted() {
    },
  };
  </script>
  
  
  <style scoped>
  .icon-select {
    overflow-x: hidden;
    max-height: 300px;
    overflow-y: auto;
    
  }


  .icon-item-container {
    display: flex;
    align-items: center;
    cursor: pointer;
  }
  .icon-item-container:hover {
    background-color: #ececec;
  }
  .icon-item {
    font-size: 24px;
    margin-right: 10px;
    pointer-events: none;
  }
  .icon-text {
    font-size: 14px;
    pointer-events: none;
  }

  /* 滚动条整体样式，包括轨道 */
.icon-select::-webkit-scrollbar {
  width: 8px; /* 滚动条的宽度 */
  background-color: transparent; /* 轨道的背景颜色 */
}

/* 滚动条滑块的样式 */
.icon-select::-webkit-scrollbar-thumb {
  background-color: rgba(204, 204, 204, 0.6); /* 滑块的背景颜色和透明度 */
  border-radius: 4px; /* 滑块的边角圆度 */
}

/* 滚动条滑块:hover的样式 */
.icon-select::-webkit-scrollbar-thumb:hover {
  background-color: rgba(204, 204, 204, 0.5); /* 悬停时滑块的颜色变深 */
}
  </style>
  