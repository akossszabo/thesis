<template>
  <div class="app-container">
    <form_modal 
      v-show="showModal"
      :fields="formfields"
      :formdata="formdata"
      @onSubmitClick="submit"
      @onCancelClick="showModal=false">
    </form_modal>

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
      @onRowClick="rowClick"
      @onAddClick="add"
      @onOpenClick="openClick"
      @onDeleteClick="deleteProject"
      @onEditClick="edit">
      </apptable>
	  
  </div>
</template>

<script>
import http from "../http.js";
import config from "../config.js";
import apptable from "../components/table/Table.vue";
import form_modal from "../components/FormModal.vue";

export default {
  components: { apptable,form_modal },

  data() {
    return {
      showModal: false,
      showMessage: false,
      headers: [
        { title: "Name", key: "name" },
        { title: "Leader", key: "leader" },
        { title: "Type", key: "type" }
      ],
      formfields: [
        { title: "Name", key: "name", type: "text", validate: "required, min(2)" },
        { title: "Project Leader", key: "leader", type: "text", validate: "required, min(2)" },
        { title: "Type", key: "type", type: "text", validate: "required, min(3)" }
      ],
      datas: [],
      account: {},
      defaultSortKey: "name",
      hiddenTable: true,
      formdata: {},
      selectedRow: null,
      serverMessage: null
    };
  },

  created() {
    this.fetch();
  },

  methods: {
    submit() {
        var request = {
                project : this.formdata
            }
            request.project.leader = this.account.email;
        http.post(config.createProjectUrl, request).then(({ data }) => {
            this.hide = false;
            console.log("resp: ",data);
            this.serverMessage = data.message;
            this.showMessage = true;
            this.fetch();

            setTimeout(() => {
                this.showMessage = false;
            }, 5000);
      });

      this.showModal = false;
    },
    add(){
        this.formdata = {};
      this.showModal = true;
    },
    deleteProject(selectedRowIds){
        var request = {
                accountIds : selectedRowIds
            }
        console.log("delete projects request: ",request);
        http.post(config.deleteAccountsUrl, request).then(({ data }) => {
            this.fetch();
            this.serverMessage = data.message;
            this.showMessage = true;
            setTimeout(() => {
                this.showMessage = false;
            }, 5000);
      });
    },
    fetch() {
      http.get(config.getAllProjectsUrl).then(({ data }) => {
        this.datas = data.items;
        this.hiddenTable = false;
      });
     /* http.get(config.getPrincipal).then(({ data }) => {
        this.account = data;
      });*/
    },
    rowClick(row,selectedRows) { 
        if(selectedRows.length === 0) {
            this.selectedRow = null;
        } else {
            this.selectedRow = selectedRows[selectedRows.length-1];
        }
    },
    edit(){
        
        this.formdata = this.selectedRow;
         this.show_modal = true;
        this.showModal=true;
    },
    cellClick(key,value,row) {
      if(key==='name') {
        this.$store.commit('pushLastOpenedProjects', {id:row.id, name:row.name});
        this.$router.push({ name: 'toproject', params: { id: row.id }});
      }
    },
    openClick(selectedRows) {
      for (var i = 0; i < selectedRows.length; i++) {
        let row = selectedRows[i];
        this.$store.commit('pushLastOpenedProjects', {id:row.id, name:row.name});
      }
    }
  }
};
</script>

<style>

</style>
