<template>
  <div class="app-container">
    <progressbar v-show="hiddenTable"></progressbar>

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
      @onRowClick="rowClick"
      @onEditClick="editClick"
      @onDeleteClick="deleteClick"
      @onAddClick="addClick">
      </apptable>

    <form_modal 
      v-show="show_modal"
      :fields="formfields"
      :formdata="formdata"
      @onSubmitClick="submitClick"
      @onCancelClick="show_modal=false">
      </form_modal>

      <button @click="subscribe">subscribe</button>
  </div>
</template>

<script>
import http from "../http.js";
import config from "../config.js";
import progressbar from "../components/ProgressBar.vue";
import apptable from "../components/table/Table.vue";
import form_modal from "../components/FormModal.vue";
import SockJS from "sockjs-client";
import Stomp from "webstomp-client";

export default {
  components: { progressbar, apptable, form_modal,SockJS,Stomp },

  data() {
    return {
      headers: [
        { title: "Id", key: "id", width: "50px" },
        { title: "Name", key: "name", bclasses: 'lbold'},
        { title: "Summary", key: "summary", width: "30%", bclasses: 'bold' },
        { title: "Priority", key: "priority" },
        { title: "Type", key: "type" },
        { title: "Status", key: "status" },
        { title: "Project", key: "project" },
        { title: "Adding Date", key: "addDate" }
      ],
      datas: [],
      filters: [
        { title: "Priority", key: "priority", selects: ["Low", "Medium", "High"] },
        { title: "Status", key: "status", selects: ["Todo", "In Progress", "Under Review", "Done", "Cancelled"] }
      ],
      formfields: [
        { title: "Id", key: "id", type: "text", validate: "required" },
        { title: "Name", key: "name", type: "text", validate: "required, min(3)" },
        { title: "Summary", key: "summary", type: "text", validate: "required, max(100)" },
        { title: "Priority", key: "priority", type: "text" },
        { title: "Type", key: "type", type: "text" },
        { title: "Status", key: "status", type: "text" },
        { title: "Project", key: "project", type: "text" },
        { title: "Adding Date", key: "addDate", type: "text"}
      ],
      formdata: {},
      defaultSortKey: "id",
      hiddenTable: true,
      show_modal: false,
      selectedRow: null,
      stompClient: null,
      socket : null,
      connected: false,
    };
  },

  created() {
   // this.fetch();
    this.connect();
   
  },

  methods: {
    fetch() {
      http.get(config.getAllIssuesUrl).then(({ data }) => {
        this.datas = data.items;
        this.hiddenTable = false;
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
    deleteClick() {
    },
    addClick() {
      this.formdata = {};
      this.show_modal = true;
    },
    submitClick() {
      this.show_modal = false;
    },
    connect(){
      this.socket = new SockJS('http://localhost:5050/chatservice');
      this.stompClient = Stomp.over(this.socket);

      this.stompClient.connect({}, function(frame) {
         console.log('Connected: ' + frame);
            
      });
      this.connected = true;
      console.log('beállítva: ' + this.connected);
      console.log("connected-e : " + this.connected);
    },
    subscribe(){
      console.log("connected-e ittis : " + this.connected);
      if (this.connected) {
            console.log("try to subscribe.")
          this.stompClient.subscribe('/topic/1', function(greeting) {
            console.log("subscribed");
            alert(greeting);
          });
          }

      if (this.connected) {
            this.stompClient.send("/message/1", {},"hello project" );
              console.log("message send");
      }
    }

  }
};
</script>

<style>
.lbold {
    font-weight: 500;
}
.bold {
    font-weight: 700;
}
</style>
