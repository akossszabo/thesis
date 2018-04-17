<template>
  <div class="app-container">
    <p>Issue: {{issueId}}</p>
  </div>
</template>

<script>
export default {
  data() {
    return {
      issueId: null
    };
  },
  created() {
    this.$store.commit('setSidebarTitle', 'Issues');
  },
  mounted() {
    let path = window.location.hash.substring(1);
    let patharray = path.split("/");
    this.buildPage(patharray[patharray.length-1]);
  },
  beforeRouteUpdate (to, from, next) {
    this.buildPage(to.params.id);
    next();
  },
  methods: {
    buildPage(id) {
      this.issueId = id;
    }
  }
};
</script>