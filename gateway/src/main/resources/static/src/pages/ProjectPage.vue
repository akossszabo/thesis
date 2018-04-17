<template>
  <div class="app-container">
    <div class="info-container" v-show="!hiddenTable">
      <div class="box">
        <div style="padding-bottom: 5px;">
          <span class="title">{{projectName}}</span>
          ( id: {{projectId}} )
        </div>
        {{summary}}
      </div>

      <div id="datas1" class="box">
        <div>type: Business</div>
        <div>client: Apple</div>
        <div>status: Active</div>
        <div>adding date: 2016/11/11</div>
      </div>

      <div id="datas2" class="box">
        <div>issues: 200</div>
        <div>opened issues: 33</div>
        <div>active bug: 12</div>
      </div>

      <div id="users" class="box">
        <div class="title"> Users </div>
        <div><span class="activity-name">Harry Potter</span> (Lead)</div>
        <div class="activity-name">Hermione Granger</div>
        <div class="activity-name">Ronald Weasley</div>
      </div>

      <div id="act" class="box">
        <div class="title"> Activity </div>
        <div class="activity"><span class="activity-name">Harry Potter</span> started progress on <span class="activity-id">UNEDM-135</span></div>
        <div class="activity"><span class="activity-name">Hermione Granger</span> started progress on <span class="activity-id">UNEDM-134</span></div>
        <div class="activity"><span class="activity-name">Ronald Weasley</span> changed the Assignee to 'Kaszányi Dávid' on <span class="activity-id">UNEDM-136</span></div>
        <div class="activity"><span class="activity-name">Harry Potter</span> started progress on <span class="activity-id">UNEDM-135</span></div>
        <div class="activity"><span class="activity-name">Hermione Granger</span> started progress on <span class="activity-id">UNEDM-134</span></div>
      </div>

      <div>
        <span style="float: right; margin-left: 5px;">
          <button class="small-btn blue-btn">Edit</button>
        </span>
        <span style="float: right">
          <button class="small-btn blue-btn">Add Note</button>
        </span>
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
      @onRowClick="rowClick" 
      @onOpenClick="openClick" 
      @onEditClick="editClick" 
      @onDeleteClick="deleteClick"
      @onAddClick="addClick"
      @onCellClick="cellClick">
    </apptable>

    <form_modal
      v-show="show_modal" 
      :fields="formfields" 
      :formdata="formdata" 
      @onSubmitClick="submitClick" 
      @onCancelClick="show_modal=false">
    </form_modal>
  
  <chat_window
  :projectId="projectId"
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
  components: { apptable, form_modal,chat_window },
  data() {
    return {
      projectId: null,
      projectName: null,
      summary: null,
      headers: [
        { title: "Name", key: "name", bclasses: "name-column" },
        { title: "Summary", key: "summary", width: "30%", bclasses: "bold" },
        { title: "Priority", key: "priority" },
        { title: "Type", key: "type" },
        { title: "Status", key: "status" },
        { title: "Adding Date", key: "addDate", hclasses: "hide-date", bclasses: "hide-date" }
      ],
      datas: [],
      filters: [
        { title: "Priority", key: "priority", selects: ["Low", "Medium", "High"] },
        { title: "Type", key: "type", selects: ["Bug", "New Feature"] },
        { title: "Status", key: "status", selects: ["Todo", "In Progress", "Under Review", "Done", "Cancelled"] },
      ],
      formfields: [
        { title: "Id", key: "id", type: "text", validate: "required" },
        { title: "Name", key: "name", type: "text", validate: "required, min(3)" },
        { title: "Summary", key: "summary", type: "text", validate: "required, max(100)" },
        { title: "Priority", key: "priority", type: "text" },
        { title: "Type", key: "type", type: "text" },
        { title: "Status", key: "status", type: "text" },
        { title: "Project", key: "project", type: "text" },
        { title: "Adding Date", key: "addDate", type: "text" }
      ],
      formdata: {},
      defaultSortKey: "id",
      hiddenTable: true,
      show_modal: false,
      selectedRow: null
    };
  },
  created() {
    this.$store.commit('setSidebarTitle', 'Projects');
    
  },
  mounted() {
    let path = window.location.hash.substring(1);
    let patharray = path.split("/");
    this.buildPage(patharray[patharray.length - 1]);
  },
  beforeRouteUpdate(to, from, next) {
    this.buildPage(to.params.id);
    next();
  },
  methods: {
    buildPage(id) {
      console.log("Build project page: " + id);
      this.hiddenTable = true;
      this.formdata = {};
      this.selectedRow = null;
      this.projectId = id;
      
      http.get(config.getAllIssuesUrl).then(({ data }) => {
        this.datas = data.items;
        this.hiddenTable = false;
        this.projectName = "UNI-Disposal";
        this.summary =
          "A Unicredit Bank EDocManagement projektje során bevezetett imaging megoldás hatására a megfelelően digitalizált, "
          + "és hitelesen archivált dokumentumok papír alapú példányai feleslegessé válnak. "
          + "Az ilyen papír alapú dokumentumok megsemmisítéséhez igény keletkezett egy olyan támogató rendszerre, "
          + "ami a szóban forgó papír alapú dokumentumok selejtezését támogatja. Ez a Disposal alkalmazás.";
          // TODO : this.$store.commit('pushLastOpenedProjects', {id:row.id, name:row.name});
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
      this.show_modal = true;
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
    deleteClick() {
      this.$alertError("A törlés még nem működik!");
    },
    addClick() {
      this.formdata = {};
      this.show_modal = true;
    },
    submitClick() {
      this.show_modal = false;
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
@media screen and (max-height: 750px) {
  #datas1, #datas2 {
    display: none;
  }
}
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