import Vue from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import ElementUI from 'element-ui';
import axios from 'axios';
import locale from 'element-ui/lib/locale/lang/zh-CN';
import Cookies from 'js-cookie';
// 导入全局 CSS
import 'element-ui/lib/theme-chalk/index.css';
import './assets/styles/index.scss';
import '@/assets/global.scss'
import model from './plugins/model';
import { download } from '@/utils/request';
import { debounce, throttle } from '@/utils/statice'
import moment from 'moment';
import bcrypt from 'bcryptjs';
Vue.use(bcrypt)

Vue.prototype.$debounce = debounce;
Vue.prototype.$throttle = throttle;
Vue.prototype.$model = model;
Vue.prototype.$cookies = Cookies;
Vue.prototype.$download = download
Vue.config.productionTip = false;

Vue.use(ElementUI,{
    locale: locale,
  // 支持 large、default、small
  size: Cookies.get('size') || 'default'
});
Vue.prototype.$axios = axios;

// 定义一个全局过滤器
Vue.filter('formatTimestamp', function (value) {
  if (!value) return '';
  return moment.unix(value).format('YYYY-MM-DD HH:mm:ss');
});


new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app');
