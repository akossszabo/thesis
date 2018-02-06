import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    isSidebarVisible: true,
  },
  
  mutations: {
    changeSidebarVisibility(state) {
      state.isSidebarVisible = !state.isSidebarVisible
    },
  }
})