<template>
  <div class="app-container">
    <div class="row" v-show="userNumber!=null">
      <div class="column">
        <infobox title="Projects" :info="projectNumber"></infobox>
      </div>
      <div class="column">
        <infobox title="Users" :info="userNumber"></infobox>
      </div>
      <div class="column">
        <infobox title="Issues" :info="issueNumber"></infobox>
      </div>
      <div class="column">
        <infobox class="last-infobox" title="Comments" :info="commentNumber"></infobox>
      </div>
    </div>
  </div>
</template>

<script>
import infobox from "../components/InfoBox.vue";
import http from "../http.js";
import config from "../config.js";
export default {
  components: { infobox }

 ,data(){
   return {
      userNumber: 0,
      commentNumber: 0,
      projectNumber: 0,
      issueNumber: 0
    };

 },
 created(){
   this.$store.commit('setSidebarTitle', 'Dashboard');
    http.get(config.getStatInfo).then(({ data }) => {
            this.userNumber = data.userNumber;
            this.commentNumber = data.commentNumber;
            this.issueNumber = data.issueNumber;
            this.projectNumber = data.projectNumber;
          });
 }
};
</script>

<style lang="scss" scoped>
.row {
  display: table;
  width: 100%;
  table-layout: fixed;
}

.column {
  display: table-cell;
  width: 25%;
  float: left;
}
</style>

