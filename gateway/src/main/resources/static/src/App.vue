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
export default {
  components: { headbar, sidebar, notifications },
  computed: {
    ...mapState(["isSidebarVisible"])
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