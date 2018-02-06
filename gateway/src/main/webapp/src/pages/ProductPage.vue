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
        { title: "Id", key: "id", width: "50px" },
        { title: "Name", key: "name", width: "200px" },
        { title: "Manufacturer", key: "manufacturer", width: "200px" },
        { title: "Price", key: "price", width: "15%" },
        { title: "Stock", key: "stock", width: "15%" },
        { title: "Status", key: "status" },
        { title: "Adding date", key: "addDate" }
      ],
      datas: [],
      defaultSortKey: "id",
      hiddenTable: true
    };
  },

  created() {
    this.fetch();
  },

  methods: {
    fetch() {
      http.get(config.getAllProductsUrl).then(({ data }) => {
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
