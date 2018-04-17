import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    sidebarTitle: '',
    isSidebarVisible: true,
    notifications: [],
    lastOpenedProjects: [],
    lastOpenedIssues: [],
  },
  
  mutations: {
    setSidebarTitle(state, title) {
      state.sidebarTitle = title;
    },
    changeSidebarVisibility(state) {
      state.isSidebarVisible = !state.isSidebarVisible
    },
    pushNotification(state, msg) {
      state.notifications.push(msg);
      setTimeout(() => {
        for (let i = 0; i < state.notifications.length; i++) {
          if (state.notifications[i] === msg) {
            state.notifications.splice(i, 1)
          }
        }
      }, 5000);
    },
    pushLastOpenedProjects(state, project) {
      var temp = [];
      temp.push(project);
      for (let i = 0; i < state.lastOpenedProjects.length; i++) {
        if(state.lastOpenedProjects[i].id != project.id) {
          temp.push(state.lastOpenedProjects[i]);
        }
        if(temp.length > 5) {
          break;
        }
      }
      state.lastOpenedProjects = temp;
    },
    pushLastOpenedIssues(state, project) {
      var temp = [];
      temp.push(project);
      for (let i = 0; i < state.lastOpenedIssues.length; i++) {
        if(state.lastOpenedIssues[i].id != project.id) {
          temp.push(state.lastOpenedIssues[i]);
        }
        if(temp.length > 5) {
          break;
        }
      }
      state.lastOpenedIssues = temp;
    }
  }
})
