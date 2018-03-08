<template>
  <div class="app-container">
    <apptable 
      v-show="!hiddenTable" 
      :headers="headers" 
      :datas="datas" 
      :defaultSortKey="defaultSortKey" 
      :defaultPageSize="50"
      :selectable="true"
      :deletable="true"
      :editable="true"
      :coloredStatus="true"
      @onRowClick="rowClick">
      </apptable>
  </div>
</template>

<script>
import http from "../http.js";
import config from "../config.js";
import apptable from "../components/table/Table.vue";

export default {
  components: { apptable },

  data() {
    return {
      headers: [
        { title: "Name", key: "name" },
        { title: "Company", key: "company" }
      ],
      datas: [],
      defaultSortKey: "name",
      hiddenTable: true
    };
  },

  created() {
    this.fetch();
  },

  methods: {
    fetch() {
      http.get(config.getAllProjects).then(({ data }) => {
        this.datas = data.items;
        this.hiddenTable = false;
      });
    },
    rowClick(row) {
      console.log(row);
    }
  }
};
</script>

<style>

</style>
