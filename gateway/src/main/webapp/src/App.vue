<template>
  <div class="app-main">
    <headbar>
      
    </headbar>
    <div class="app-content">
      <sidebar></sidebar>
      <div class="app-page" :class="{'fullpage': !isSidebarVisible}">
        <router-view></router-view>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
import headbar from "./components/Headbar.vue";
import sidebar from "./components/Sidebar.vue";
export default {
  components: { headbar, sidebar },

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

@media screen and (max-width: 800px) {
  .app-sidebar {
    width: 0;
  }
  .app-page {
    margin-left: 0;
  }
}
</style>
<style lang="css">
@import "./styles/vue-material.min.css";
</style>