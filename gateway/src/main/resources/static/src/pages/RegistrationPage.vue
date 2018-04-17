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
      @onDeleteClick="deleteAccounts"
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
      options: [
        { title: "Id" },
        { title: "Name" },
        { title: "Manufacturer" },
        { title: "Price" },
        { title: "Stock" },
        { title: "Status" },
        { title: "Adding date" }
      ],
      headers: [
        { title: "First name", key: "firstName" },
        { title: "Last name", key: "lastName" },
        { title: "Email address", key: "email" }
      ],
       formfields: [
        { title: "First name", key: "firstName", type: "text", validate: "required, min(2)" },
        { title: "Last name", key: "lastName", type: "text", validate: "required, min(2)" },
        { title: "Email address", key: "email", type: "text", validate: "required, email" }
      ],
      datas: [],
      defaultSortKey: "firstName",
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
                account : this.formdata
            }
        http.post(config.registrationUrl, request).then(({ data }) => {
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
    deleteAccounts(selectedRowIds){
        var request = {
                accountIds : selectedRowIds
            }
        console.log("delete accounts request: ",request);
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
      http.get(config.getAccountsUrl).then(({ data }) => {
        this.datas = data.items;
        this.hiddenTable = false;
      });
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
    }
  }
};
</script>

<style lang="scss" scoped>
@import "../styles/form-modal.scss";
</style>