<template>
  <div class="app-container">
    <div v-show="showModal" id="myModal" class="modal">
      <div class="modal-content">
        <div class="modal-header">
          <span class="close" @click="showModal=false">&times;</span>
          <h2>Create/Update account</h2>
        </div>

        <form>
          <div class="form-group">
            <input type="text" required="required" v-model="firstName" />
            <label class="control-label" for="input">First Name</label><i class="bar"></i>
          </div>

          <div class="form-group">
            <input type="text" required="required" v-model="lastName"/>
            <label class="control-label" for="input">Last Name</label><i class="bar"></i>
          </div>

          <div class="form-group">
            <input type="text" required="required" v-model="email"/>
            <label class="control-label" for="input">Email</label><i class="bar"></i>
          </div>

          <div class="form-group">
            <select>
              <option v-for="o in options" :key="o.title"> {{ o.title }}</option>
            </select>
            <label class="control-label" for="select">Selectbox</label><i class="bar"></i>
          </div>
        </form>

        <div class="button-container">
          <button class="button" type="button" @click="submit"><span>Submit</span></button>
        </div>
      </div>
      </div>
      <div :class="{'error-message' : showMessage, 'error-message-hide' : !showMessage}" v-show="showMessage" @click="hide=true;showMessage=false">
        {{this.serverMessage}}
      </div>
      
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
      @onNewClick="showModal=true; email='';firstName='';lastName=''; id=null"
      @onEditClick="edit">
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
      datas: [],
      defaultSortKey: "firstname",
      hiddenTable: true,
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
                account: {
                    id: this.id,
                    lastName:   this.lastName,
                    firstName:  this.firstName,
                    email:      this.email 
                }
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
    fetch() {
      http.get(config.getAccountsUrl).then(({ data }) => {
        this.datas = data.items;
        this.hiddenTable = false;
      });
    },
    rowClick(row) {
      console.log(row);
      this.selectedRow = row;
    },
    edit(){
        
        this.lastName = this.selectedRow.lastName;
        this.firstName = this.selectedRow.firstName;
        this.id= this.selectedRow.id;
        this.email= this.selectedRow.email;
        this.showModal=true;
        console.log("ezvan itt: ", this.selectedRow);

    }
  }
};
</script>

<style lang="scss" scoped>
@import "../styles/modal_form.scss";
</style>