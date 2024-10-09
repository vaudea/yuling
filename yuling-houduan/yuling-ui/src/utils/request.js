import axios from "axios";
import Vue from 'vue';
import { Notification, MessageBox, Message, Loading } from 'element-ui'
import { saveAs } from 'file-saver'
// 创建一个axios对象
const request = axios.create({
    //请求地址 此处为后端的端口
  //baseURL: 'http://localhost:8080',
    //配置了跨域就这样写
	baseURL:'/api/yuling',//这里的/api是跨域的别名 yuling是后端设置的请求前缀
    //设置超时时间为5秒钟
    timeout: 50000,
});

// 请求拦截器
// 可以自请求发送前对请求做一些处理
// 比如统一加token，对请求参数统一加密
// 请求拦截器
request.interceptors.request.use(config => {
  // 检查是否是文件上传请求，如果是 FormData，则不手动设置 Content-Type
  if (!(config.data instanceof FormData)) {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
  }
  
  try {
    const userCookie = Vue.prototype.$cookies.get('user');
    if (userCookie) {
      const user = JSON.parse(userCookie);
      if (user.token) {
        config.headers['Authorization'] = `Bearer ${user.token}`;
      }
    }
  } catch (error) {
    console.error('设置认证头部时出现错误:', error);
  }
  return config;
}, error => {
  return Promise.reject('请求配置发生错误，请检查请求配置');
});



// response拦截器
//可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        // response.data即为后端返回的Result
        let res = response.data;
        // 兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        return res;
    }, error => {
          //  console.log('err'+error);
           return Promise.reject(error)
    });

// excel下载方法
export function download(url, params, filename) {
    const downloadLoadingInstance = Loading.service({
      text: "正在下载数据，请稍候",
      spinner: "el-icon-loading",
      background: "rgba(0, 0, 0, 0.7)"
    });
  
    return request.get(url, {
      params: params, // GET请求的参数
      responseType: 'blob' // 设置响应类型为blob，以便处理文件下载
    }).then(response => {
      if (!response || response.size === 0) {
        Message.error('下载文件失败，文件内容为空！');
        downloadLoadingInstance.close();
        return;
      }
      const blob = new Blob([response], { type: 'application/vnd.ms-excel' }); // 设置MIME类型为Excel
      saveAs(blob, filename); // 使用file-saver的saveAs函数下载文件
      setTimeout(() => {
        Message.success('下载成功！');
      }, 500);
      downloadLoadingInstance.close(); // 关闭加载提示
    }).catch(error => {
      console.error(error);
      Message.error('下载文件出现错误，请联系管理员！');
      downloadLoadingInstance.close();
    });
  }

export function getToken() {
    return Vue.prototype.$cookies.get('user').token;
}

export default request