
import Vue from 'vue'
import Vuex from 'vuex'
import request from '@/utils/request'
//vue持久化插件
import createPersistedState from 'vuex-persistedstate';
Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    // 初始化 state
    token: '',
    avatar: '',
    avatarName: '',
    activeIndex: "/home",
    openTab: [
      { title: "首页", index: "/home" },
    ],
    isCollapse: false  // 默认值
  },
  getters: {
    // 可以添加用于访问 state 的 getters
    avatar: state => state.avatar,
    avatarName: state => state.avatarName,
    isCollapse: state => state.isCollapse,
    activeIndex: state => state.activeIndex,

  },
  mutations: {
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_AVATARNAME: (state, avatarName) => {
      state.avatarName = avatarName
    },
    SET_ISCOLLAPSE: (state) => {
      state.isCollapse = !state.isCollapse
    },
    SETINISCOLLAPSE: (state, isCollapse) => {
      state.isCollapse = isCollapse
    },
    CLEAR_AVATAR(state) {
      state.avatar = '';
    },
    CLEAR_AVATARNAME(state) {
      state.avatarName = '';
    },
    CLEAR_ISCOLLAPSE(state) {
      state.isCollapse = false;
    },
    RESET_TO_DEFAULTS(state) {
      state.activeIndex = "/home";
      state.openTab = [
        { title: "首页", index: "/home" },
      ];
      state.isCollapse = false;
    },
    // 单机导航栏时添加标签页
    addTab(state, data) {
      if (!state.openTab.some(tab => tab.index === data.index)) {
        state.openTab.push(data);
        state.activeIndex = data.index;
      }
    },
    REMOVE_TAB(state, targetIndex) {
      const tabs = state.openTab;
      if (state.activeIndex === targetIndex) {
        tabs.forEach((tab, index) => {
          if (tab.index === targetIndex) {
            const nextTab = tabs[index + 1] || tabs[index - 1];
            if (nextTab) {
              state.activeIndex = nextTab.index;
            }
          }
        });
      }
      state.openTab = tabs.filter(tab => tab.index !== targetIndex);
    },
    // 根据 title 更新 index
    MODIFY_INDEX_BY_TITLE(state, { title, newIndex }) {
      const tab = state.openTab.find(tab => tab.title === title);
      if (tab) {
        tab.index = newIndex;
      }
    },
    // 根据 index 更新 title
    MODIFY_TITLE_BY_INDEX(state, { index, newTitle }) {
      const tab = state.openTab.find(tab => tab.index === index);
      if (tab) {
        tab.title = newTitle;
      }
    },
  },
  actions: {
    // 添加 GetInfo 和其他 actions
    GetInfo({ commit }) {
      return new Promise((resolve, reject) => {
        const jobNumber = JSON.parse(Vue.prototype.$cookies.get('user')).jobNumber;
        request.get('/employee/getAvatar?jobNumber=' + jobNumber).then((res) => {
          if (res.code === "200") {
            const user = res.data;
            const avatar = (user == null || user == "") ? require("@/assets/images/profile.jpg") : `${process.env.VUE_APP_API_URL}/yuling/files/getAvatar/` + res.data;
            const avatarName = (user == null || user == "") ? "profile" : res.data.split('-').pop().split('.').shift();
            localStorage.setItem("avatar", avatar);
            localStorage.setItem("avatarName", avatarName);
            commit('SET_AVATAR', avatar);
            commit('SET_AVATARNAME', avatarName);
            resolve(res.data);
          } else {
            reject(`获取头像失败: ${res.message}`);
          }
        });
      });
    },
    modifyIndexByTitle({ commit }, payload) {
      commit('MODIFY_INDEX_BY_TITLE', payload);
    },
    modifyTitleByIndex({ commit }, payload) {
      commit('MODIFY_TITLE_BY_INDEX', payload);
    },
    removeInvalidTabs({ commit }) {
      commit('REMOVE_INVALID_TABS');
    }
  },
  plugins: [createPersistedState({
    paths: ['isCollapse', 'openTab', 'activeIndex']
  })]
  ,
  modules: {
    // 如果有其他模块也可以在这里添加
  }
})
