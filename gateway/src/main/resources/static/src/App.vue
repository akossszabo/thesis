<template>
  <div class="app-main-container">
    <headbar></headbar>
    <div class="app-content">
      <sidebar></sidebar>
      <div class="app-page" :class="{'fullpage': !isSidebarVisible}">
        <router-view></router-view>
      </div>
    </div>
    <notifications></notifications>
  </div>
</template>

<script>
import { mapState } from "vuex";
import headbar from "./components/HeadBar.vue";
import sidebar from "./components/SideBar.vue";
import notifications from "./components/NotificationsArea.vue";
import http from "./http.js";
import config from "./config.js";
import store from "./store.js";
export default {
  components: { headbar, sidebar, notifications },
  computed: {
    ...mapState(["isSidebarVisible"])
  },
  created() {
    http.get(config.getPrincipal).then(({ data }) => {
      
        console.log(data);
         this.$store.commit('saveCurrentAccount', data);
      });
    
    
  }
};
</script>

<style lang="scss">
@import "./styles/basics.scss";
@import "./styles/layout.scss";
.fullpage {
  margin-left: 0;
}
@media screen and (max-width: 1230px) {
  .app-page {
    margin-left: 0;
  }
  .app-sidebar {
    width: 0;
    //display: block;
    //position: absolute;
    //z-index: 1000;
  }
}
</style>