<template>
    <div class="infobox">
        <div class="title">{{title}}</div>
        <div class="info">{{info}}</div>
    </div>
</template>

<script>
export default {
  props: {
    title: String,
    info: String
  },

  data() {
    return {};
  }
};
</script>


<style lang="scss" scoped>
@import "../styles/colors.scss";

.infobox {
  background-color: $vlight-grey;
  border: 1px solid $light-grey;
  padding: 20px;
  margin-right: 20px;
}

.last-infobox {
  margin-right: 0px;
}

.title {
  font-size: 20px;
  color: black;
  font-weight: 300;
}

.info {
  font-size: 50px;
  color: black;
  font-weight: 500;
}
</style>
