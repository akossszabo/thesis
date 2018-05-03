<template>
  <div v-if="project != null" class="app-container">
    <div class="info-container" v-show="!hiddenTable">
      <div class="box">
        <div style="padding-bottom: 5px;">
          <span class="title">{{project.name}}</span>
        </div>
       <div>Lead:<span class="activity-name"> {{project.leader}} </span> </div>
       <div>Type:<span class="activity-name"> {{project.type}}</span> </div>
        <div>Adding date:<span class="activity-name"> {{project.creationDate}}</span> </div>
      </div>


      <div id="users" class="box">
        <div class="title"> Summary </div>
        <div><span class="activity-name">{{project.summary}}</span> </div>
        
      </div>

      <div id="act" class="box">
        <div class="title">Employees on {{project.name}}</div>
        <div class="activity" v-for="(u) in activeUsers" :key="u.id"><span class="activity-name">{{u.name}}</span></div>
      </div>
</div>
    <apptable class="apptable-smaller" 
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
      :showAddButton="true"
      @onRowClick="rowClick" 
      @onOpenClick="openClick" 
      @onEditClick="editClick"
      @onDeleteClick="deleteClick"
      @onAddClick="addClick"
      @onCellClick="cellClick">
    </apptable>

    <form_modal
      v-show="showModal" 
      :fields="formfields" 
      :formdata="formdata"
      :assignees="users"
      @onSubmitClick="submit" 
      @onCancelClick="showModal=false">
    </form_modal>
  
  <chat_window
  :projectId="projectId"
  :name="account.firstName + ' ' + account.lastName" 
  >
  </chat_window>
  
</div>
</template>

<script>
import { mapState } from "vuex";
import http from "../http.js";
import config from "../config.js";
import apptable from "../components/table/Table.vue";
import form_modal from "../components/FormModal.vue";

import chat_window from "../components/ChatWindow.vue";
export default {
  components: { apptable, form_modal, chat_window, mapState },
  computed: {
    ...mapState(["account"])
  },
  data() {
    return {
      projectId: null,
      project: null,
      summary: null,
      headers: [
        { title: "Name", key: "name", bclasses: "name-column" },
        { title: "Assignee", key: "assignee" },
        { title: "Priority", key: "priority" },
        { title: "Reporter", key: "reporter" },
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
      users: [],
      filters: [
        {
          title: "Priority",
          key: "priority",
          selects: ["Low", "Medium", "High"]
        },
        { title: "Type", key: "type", selects: ["Bug", "New Feature"] },
        {
          title: "Status",
          key: "status",
          selects: ["Todo", "In Progress", "In Review", "Done", "Cancelled"]
        }
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
      defaultSortKey: "name",
      hiddenTable: true,
      showModal: false,
      selectedRow: null,
      username: "",
      openedIssuesNum: 0,
      activeUsers: []
    };
  },
  created() {
    console.log("created");
    this.$store.commit("setSidebarTitle", "Projects");
    let path = window.location.hash.substring(1);
    let patharray = path.split("/");
    this.fetchUsers();
    this.buildPage(patharray[patharray.length - 1]);
  },
  beforeRouteUpdate(to, from, next) {
    console.log("before route update");
    this.projectId = to.params.id;
    this.buildPage(to.params.id);
    next();
  },
  beforeRouteLeave(to, from, next) {
    console.log("elhagytuk ezt a route-ot");
    next();
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
      http
        .get(config.getProjectDetails + "/" + this.projectId)
        .then(({ data }) => {
          console.log("datas arrived");
          this.datas = data.items;
          this.hiddenTable = false;
        });
    },
    fetchUsers() {
      http.get(config.getAccountsToSelectUrl).then(({ data }) => {
        this.users = data.items;
        console.log("users arrived: ", this.users);
      });
    },
    fetchActiveUsers() {
      http
        .get(config.getActiveUsersUrl + "/" + this.projectId)
        .then(({ data }) => {
          this.activeUsers = data.items;
          console.log("users arrived: ", this.users);
        });
    },
    calculateOpenedIssues(data) {
      if (data) {
        data.map(function(value, key) {
          if (value.status !== "Done") {
            ++this.openedIssuesNum;
          }
        });
      }
    },
    buildPage(id) {
      console.log("Build project page: " + id);
      this.hiddenTable = true;
      this.formdata = {};
      this.selectedRow = null;
      this.projectId = id;
      this.getProjectDetails();
      this.fetchActiveUsers();
      this.username = this.account.firstName + " " + this.account.lastName;
    },
    getProjectDetails() {
      console.log("getProjectDetails");
      http
        .get(config.getProjectDetails + "/" + this.projectId)
        .then(({ data }) => {
          console.log("datas arrived");
          this.datas = data.items;
          this.project = data;
          this.hiddenTable = false;
          //this.calculateOpenedIssues(this.datas);
        });
    },
    rowClick(row, selectedRows) {
      if (selectedRows.length === 0) {
        this.selectedRow = null;
      } else {
        this.selectedRow = selectedRows[selectedRows.length - 1];
      }
    },
    editClick() {
      this.formdata = JSON.parse(JSON.stringify(this.selectedRow));
      this.showModal = true;
    },
    openClick(selectedRows) {
      for (var i = 0; i < selectedRows.length; i++) {
        let row = selectedRows[i];
        this.$store.commit("pushLastOpenedIssues", {
          id: row.id,
          name: row.name
        });
      }
    },
    deleteClick(selectedRowIds) {
      console.log("delete issues request: ", selectedRowIds);
      var request = {
        issueIds: selectedRowIds
      };

      http.post(config.deleteIssuesUrl, request).then(({ data }) => {
        this.fetch();
      });
    },
    addClick() {
      this.formdata = {};
      this.showModal = true;
    },
    submitClick() {
      this.showModal = false;
    },
    cellClick(key, value, row) {
      if (key === "name") {
        this.$store.commit("pushLastOpenedIssues", {
          id: row.id,
          name: row.name
        });
        this.$router.push({ name: "toissue", params: { id: row.id } });
      }
    }
  }
};
</script>

<style lang="scss">
@import "../styles/colors.scss";
@import "../styles/buttons.scss";
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
.info-container {
  font-size: 12px;
  width: 30%;
  float: left;
  max-height: 98%;
  overflow: auto;
}
.box {
  margin-bottom: 10px;
  border: 1px solid $table-border-color;
  padding: 8px;
}
.title {
  font-size: 20px;
  font-weight: 500;
  padding-bottom: 5px;
}
.activity {
  padding-bottom: 10px;
}
.activity-name {
  font-weight: 500;
  color: $blue;
}
.activity-id {
  color: $blue;
}
.apptable-smaller {
  width: 70%;
  float: right;
  padding-left: 1em;
}
@media screen and (max-width: 1320px) {
  .info-container {
    width: 100%;
    padding-bottom: 1em;
  }
  .apptable-smaller {
    width: 100%;
    padding-left: 0em;
  }
}
@media screen and (max-width: 300px) {
  .info-container {
    display: none;
  }
}
/*@media screen and (max-height: 750px) {
  #datas1,
  #datas2 {
    display: none;
  }
}*/
@media screen and (max-height: 560px) {
  #act {
    display: none;
  }
}
@media screen and (max-height: 350px) {
  #users {
    display: none;
  }
}
@media screen and (max-height: 300px) {
  .info-container {
    display: none;
  }
  .apptable-smaller {
    width: 100%;
    padding-left: 0em;
  }
}
@media screen and (max-width: 992px) {
  .hide-date {
    display: none;
  }
}
</style>