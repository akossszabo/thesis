<template>
  <div class="app-container">
    <apptable 
      v-show="!hiddenTable" 
      :headers="headers" 
      :datas="datas" 
      :filters="filters"
      :defaultSortKey="defaultSortKey" 
      :defaultPageSize="50"
      :selectable="true"
      :deletable="true"
      :editable="true"
      :coloredStatus="true"
      :clickableCells="true"
      :showAddButton="false"
      @onRowClick="rowClick"
      @onOpenClick="openClick"
      @onEditClick="editClick"
      @onDeleteClick="deleteClick"
      @onCellClick="cellClick">
      </apptable>

    <form_modal 
      v-show="show_modal"
      :fields="formfields"
      :formdata="formdata"
      :assignees="users"
      @onSubmitClick="submitClick"
      @onCancelClick="show_modal=false">
      </form_modal>
  </div>
</template>

<script>
import http from "../http.js";
import config from "../config.js";
import { mapState } from "vuex";
import apptable from "../components/table/Table.vue";
import form_modal from "../components/FormModal.vue";
export default {
  components: { apptable, form_modal },
  computed: {
    ...mapState(["account"])
  },
  data() {
    return {
       headers: [
        { title: "Id", key: "id"}, 
        { title: "Name", key: "name", bclasses: "name-column" },
        { title: "Assignee", key: "assignee"},
        { title: "Priority", key: "priority" },
        { title: "Reporter", key: "reporter"},
        { title: "Type", key: "type" },
        { title: "Status", key: "status" },
        {
          title: "Adding Date",
          key: "creationDate",
          hclasses: "hide-date",
          bclasses: "hide-date"
        }
      ],
      datas: [],
      users:[],
      filters: [
        { title: "Priority", key: "priority", selects: ["Low", "Medium", "Top"] },
        { title: "Type", key: "type", selects: ["Bug", "New Feature"] },
        { title: "Status", key: "status", selects: ["Todo", "In Progress", "In Review", "Done", "Cancelled"] },
      ],
      formfields: [
        {
          title: "Name",
          key: "name",
          type: "text",
          validate: "required, min(3)"
        },
        {
          title: "Summary",
          key: "summary",
          type: "text",
          validate: "required, max(100)"
        },
        {
          title: "Priority",
          key: "priority",
          type: "select",
          selects: ["Top", "Low"]
        },
        {
          title: "Type",
          key: "type",
          type: "select",
          selects: ["Bug", "New Feature"]
        },
        {
          title: "Assignee",
          key: "assignee",
          type: "objSelect",
          selects: this.users
        },
        {
          title: "Status",
          key: "status",
          type: "select",
          selects: ["Todo", "In Progress", "In Review", "Done", "Cancelled"]
        }
      ],
      formdata: {},
      defaultSortKey: "id",
      hiddenTable: true,
      show_modal: false,
      selectedRow: null
    };
  },
  created() {
    this.$store.commit('setSidebarTitle', 'Issues');
    this.fetch();
  },
  methods: {
    submit() {
      var request = {
        issue: this.formdata
      };
      request.issue.projectId = this.projectId;
      request.issue.reporter = this.account.email;
      http.post(config.createIssueUrl, request).then(({ data }) => {
        this.hide = false;
        console.log("resp: ", data);
        this.serverMessage = data.message;
        this.showMessage = true;
        this.fetch();
      });
      this.showModal = false;
    },
    fetch() {
      http.get(config.getAllIssuesUrl).then(({ data }) => {
        this.datas = data.items;
        this.hiddenTable = false;
      });
    },
    fetchUsers(){
       http.get(config.getAccountsToSelectUrl).then(({ data }) => {
        this.users = data.items;
        console.log("users arrived: ",this.users);
      });
    },
    rowClick(row, selectedRows) {
      if(selectedRows.length === 0) {
        this.selectedRow = null;
      } else {
        this.selectedRow = selectedRows[selectedRows.length-1];
      }
    },
    editClick() {
      this.formdata = JSON.parse(JSON.stringify(this.selectedRow));
      this.show_modal = true;
    },
    openClick(selectedRows) {
      for (var i = 0; i < selectedRows.length; i++) {
        let row = selectedRows[i];
        this.$store.commit('pushLastOpenedIssues', {id:row.id, name:row.name});
      }
    },
    deleteClick(selectedRowIds) {
        var request = {
                issueIds : selectedRowIds
            }
        console.log("delete issues request: ",request);
        http.post(config.deleteIssuesUrl, request).then(({ data }) => {
            this.fetch();
            
      });
    },
    addClick() {
      this.formdata = {};
      this.show_modal = true;
    },
    submitClick() {
      this.show_modal = false;
    },
    cellClick(key,value,row) {
      if(key==='name') {
        this.$store.commit('pushLastOpenedIssues', {id:row.id, name:row.name});
        this.$router.push({ name: 'toissue', params: { id: row.id }});
      }
    }
  }
};
</script>

<style lang="scss">
@import "../styles/colors.scss";
.name-column {
    font-weight: 500;
    color: $blue;
    &:hover {
    text-decoration: underline;
    }
}
.bold {
    font-weight: 500;
}
</style>