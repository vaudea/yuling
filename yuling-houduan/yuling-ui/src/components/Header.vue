<template>
    <div id="app" class="navbar">
        <el-header style="height: 50px;">
            <!-- 折叠按钮 -->
            <button id="hamburger-container" class="hamburger-button" @click="toggleSidebar">
                <i :class="iconClass" class="ui"></i> <!-- 使用 Element UI 的菜单图标 -->
            </button>
            <!-- 面包屑 -->

            <div class="breadcrumb">
                <el-breadcrumb separator-style="font-weight: 700; color: #c0c4cc;">
                    <!-- 静态的首页面包屑 -->
                    <el-breadcrumb-item to="/">
                        首页
                    </el-breadcrumb-item>
                    <!-- 动态生成的其他面包屑 -->
                    <el-breadcrumb-item 
                        class="breadcrumb__inner" 
                        v-for="(crumb, index) in breadcrumbList" 
                        :key="index" 
                    >
                        {{ crumb.meta.title }}
                    </el-breadcrumb-item>
                </el-breadcrumb>
            </div>
            <!-- 用户名下拉菜单 -->
            <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
                <div class="avatar-wrapper">
                    <el-image :src="avatar" class="user-avatar" />
                    <i class="el-icon-caret-bottom" />
                </div>
                <el-dropdown-menu slot="dropdown">
                    <router-link to="/user">
                        <el-dropdown-item>个人中心</el-dropdown-item>
                    </router-link>
                    <el-dropdown-item divided @click.native="logout">
                        <span>退出登录</span>
                    </el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </el-header>
    </div>
</template>

<script>
import store from "@/store";
import { mapGetters } from 'vuex'

export default {
    data() {
        return {
            jobNumber: "",
            iconClass: '' // 默认图标
        };
    },
    created() {
        this.iconClass = this.isCollapse ? 'el-icon-s-unfold' : 'el-icon-s-fold';
        window.addEventListener('resize', this.updateIconClass);
        this.jobNumber = JSON.parse(this.$cookies.get('user')).jobNumber;
    },
    computed: {
        ...mapGetters([
            'avatar',
            'isCollapse'
        ]),
        breadcrumbList() {
            return this.$route.matched.filter(route => route.meta && route.meta.title).map(route => {
                return {
                    path: this.getPath(route),
                    meta: route.meta
                };
            });
        },
    },
    watch: {
        isCollapse(newVal) {
            this.iconClass = newVal ? 'el-icon-s-unfold' : 'el-icon-s-fold';
        }
    },
    methods: {
        toggleSidebar() {
            this.$store.commit('SET_ISCOLLAPSE');
        },
        logout() {
            this.$cookies.remove('user');
            localStorage.removeItem('user');
            localStorage.removeItem('avatar');
            localStorage.removeItem('avatarName');
            store.commit('CLEAR_AVATAR');
            store.commit('CLEAR_AVATARNAME');
            this.$router.push('/login');

        },
        getPath(route) {
            const paths = [];
            while (route) {
                paths.unshift(route.path);
                route = route.parent;
            }
            return paths.join('/');
        }
    },
}
</script>


<style scoped>
@media all and (max-width:350px) {
    .avatar-container {
        display: none !important;
    }
}

.breadcrumb__inner /deep/.el-breadcrumb__inner {
    color: #97a8be !important;
}

* {
    padding: 0;
    margin: 0;
}

.el-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    background-color: #f7f3f0;
    height: 50px;
    padding: 0;
    margin: 0;
    width: 100%;
    box-sizing: border-box;
    box-shadow:inset 10px 0 8px -8px rgba(0, 0, 0, 0.5);

}

.hamburger-button {
    border: none;
    background: none;
    cursor: pointer;
    display: flex;
    align-items: center;
    width: 50px;
    height: 50px;
    justify-content: center;
    /* 保持图标居中对齐 */
    margin-right: 5px;
}

.ui {
    font-size: 20px;
}

.hamburger-button:hover {
    background-color: #f8e9de;
    box-shadow:inset 10px 0 8px -8px rgba(0, 0, 0, 0.5);

}

/* 新增样式 */
.breadcrumb {
    display: flex;
    align-items: center;
    flex: 1;
    /* 让面包屑占据剩余空间 */
}

/* 保留并合并的样式 */
.navbar {
    height: 50px;
    overflow: hidden;
    position: relative;
    background: #fff;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.03)

}

.navbar .avatar-container {
    margin-right: 30px;
}

.navbar .avatar-container .avatar-wrapper {
    margin-top: 5px;
    position: relative;
}

.navbar .avatar-container .avatar-wrapper .user-avatar {
    cursor: pointer;
    width: 40px;
    height: 40px;
    border-radius: 50%;
    /* 将边框半径设置为 50% 使图片变成圆形 */
    object-fit: cover;
    /* 确保图片在容器内按比例缩放并填满容器 */
    border: 2px solid #ccc;
    /* 可选，添加边框 */
}

.navbar .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;
}

.navbar .right-menu:focus {
    outline: none;
}

.navbar .right-menu .right-menu-item {
    display: inline-block;
    padding: 0 8px;
    height: 100%;
    font-size: 18px;
    color: #5a5e66;
    vertical-align: text-bottom;
}

.navbar .right-menu .right-menu-item.hover-effect {
    cursor: pointer;
    transition: background 0.3s;
}

.navbar .right-menu .right-menu-item.hover-effect:hover {
    background: rgba(0, 0, 0, 0.025);
}
</style>
