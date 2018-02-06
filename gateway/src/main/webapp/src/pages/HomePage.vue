<template>
  <div class="app-container">
    <div class="row">
      <div class="column">
        <infobox title="New Orders" info="112"></infobox>
      </div>
      <div class="column">
        <infobox title="Today's Sales" info="223"></infobox>
      </div>
      <div class="column">
        <infobox title="Today's Visits" info="12345"></infobox>
      </div>
      <div class="column">
        <infobox class="last-infobox" title="Messages" info="210"></infobox>
      </div>
    </div>
  </div>
</template>

<script>
import infobox from "../components/InfoBox.vue";

export default {
  components: { infobox }
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

