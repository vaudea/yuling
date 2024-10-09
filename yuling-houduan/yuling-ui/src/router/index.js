import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import Login from '@/views/login.vue'
import Layout from '@/views/Layout.vue'
import request from '@/utils/request'
import UserView from '@/views/user/user.vue'
import Cookies from 'vue-cookies'
import store from '@/store'
import NProgress from 'nprogress'; // 导入 NProgress
import 'nprogress/nprogress.css'; // 导入NProgress的样式

Vue.use(VueRouter)
Vue.use(Cookies)

NProgress.configure({
  easing: 'ease', // 动画方式
  speed: 500, // 递增进度条的速度
  showSpinner: false, // 是否显示加载ico
  trickleSpeed: 200, // 自动递增间隔
  minimum: 0.3 // 初始化时的最小百分比
})

const routes = [
  {
    path: '/login',
    name: 'login',
    component: Login
  },
  {
    path: '/',
    name: 'layout',
    component: Layout,
    redirect: '/home',
    children: []
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
});

// 手动检查路由是否已经存在
function checkAndAddRoute(name, path, component) {
  const existingRoute = router.options.routes.find(route => route.name === name);
  if (!existingRoute) {
    let title = null;
    if (name === 'user') {
      title = '个人中心'
    }
    router.addRoute('layout', {
      path: path,
      name: name,
      component: component,
      meta: { title: title}
    });
  }
}

// 添加静态路由
checkAndAddRoute('home', '/home', HomeView);
checkAndAddRoute('user', '/user', UserView);
// 解析后端返回的路由数据，动态构造路由配置
export const parseRoutes = (data) => {
  if (!data) return null;
  return data.flatMap(item => {
    const uniqueName = item.title + '_' + Math.random().toString(36).substr(2, 9);
    // 其他代码不变
    const route = {
      path: encodeURI(item.router || ""),
      name: uniqueName || null,
      component: item.router ? () => import(`@/views${item.router}`).catch(error => { return { render(c) { return c('router-view'); } }; }) : { render(c) { return c('router-view'); } },
      meta: { title: item.title, isPlaceholder: !item.router }, 
      children: item.children ? parseRoutes(item.children) : []
    };
    return route;
  });
};

// 处理路由，以处理不包含具体视图组件的菜单项
export const emptyRoutes = (data, ppath = '', pname = []) => {
  return data.children.flatMap(item => {
    if (!item.router && item.children && item.children.length !== 0) {
      const newPath = item.router && item.router.charAt(0) === '/' ? item.router : `${ppath}/${item.router}`;
      return emptyRoutes(item, newPath, [...pname, item.title]);
    } else {
      const route = {
        path: encodeURI(item.router && item.router.charAt(0) === '/' ? item.router : `${ppath}/${item.router}`),
        name: [...pname, item.title],
        component: item.router ? () => import(`@/views${item.router}`).catch(error => { return { render(c) { return c('router-view'); } }; }) : { render(c) { return c('router-view'); } },
        children: item.children ? parseRoutes(item.children) : []
      };
      return route;
    }
  });
};
// 动态添加路由
function addDynamicRoute(rs) {
  let layoutRoute = router.options.routes.find(r => r.name === 'layout');
  if (layoutRoute) {
    const existingRoutes = layoutRoute.children.map(r => ({ name: r.name, path: r.path })); 
    
    parseRoutes(rs).forEach(r => {
      // 使用唯一标识符生成路由名称，防止重复
      r.name = r.name || 'route_' + Math.random().toString(36).substr(2, 9);
      
      const isRouteExisting = existingRoutes.some(route => route.name === r.name || route.path === r.path);
      
      if (!isRouteExisting) {
        layoutRoute.children.push(r);
      } else {
        // 清理重复的路由
        layoutRoute.children = layoutRoute.children.filter(route => route.name !== r.name && route.path !== r.path);
        layoutRoute.children.push(r);
      }
    });

    router.addRoute(layoutRoute);
  }
}



// 加载动态路由
export const loadDynamicRoutes = async () => {
  if (Vue.prototype.$cookies.get('user')) {
    //实现权限控制在这里加入工号参数
    const jobNumber = Vue.prototype.$cookies.get('user').jobNumber;
    const response = await request.get(`/menu/getMenu?jobNumber=${jobNumber}`);
    if (response.code == 200) {
      if (response.data.body) {
        addDynamicRoute(response.data.body);
      }
    }
  }
}
// 清除动态路由的方法
export const resetDynamicRoutes = async () => {
  const layoutRoute = router.options.routes.find(r => r.name === 'layout');
  if (layoutRoute) {
    // 过滤出静态路由，保留 layout 的 children 为静态路由
    layoutRoute.children = layoutRoute.children.filter(child => ['home', 'user'].includes(child.name));

    // 重新添加静态路由
    router.addRoute(layoutRoute);

    // 重新加载动态路由
    await loadDynamicRoutes();
  }
};


loadDynamicRoutes();

//路由守卫，处理页面访问权限
router.beforeEach(async (to, from, next) => {
  NProgress.start();

  const user = Vue.prototype.$cookies.get('user');
  if (to.path === '/login' && user) {
    return next('/home');
  } else if (!user && to.path !== '/login') {
    return next('/login');
  }
  next();
});

router.afterEach(() => {
  NProgress.done();
});
// 处理路由的push和replace方法，防止重复导航错误
let routerPush = VueRouter.prototype.push;
let routerReplace = VueRouter.prototype.replace;

VueRouter.prototype.push = function push(location) {
  return routerPush.call(this, location).catch(err => err);
}

VueRouter.prototype.replace = function replace(location) {
  return routerReplace.call(this, location).catch(err => err);
}

export default router;
