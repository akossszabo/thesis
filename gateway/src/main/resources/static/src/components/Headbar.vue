<template>
  <div class="app-header unselectable">
    <div class="app-header-element">
      <button class="hamburger-button" @click="changeSidebarVisibility">☰</button>
    </div>
    <div class="app-header-element responsive">
      <b>{{appname}}</b>
    </div>
  <button id="editbtn" class="table-button1" @click="showModal=true">Change Password</button>
  <button id="editbtn" class="table-button1" @click="logout">Logout</button>
      <form_modal
      v-show="showModal" 
      :fields="formfields" 
      :formdata="formdata" 
      @onSubmitClick="submit" 
      @onCancelClick="showModal=false">
    </form_modal>

  </div>
</template>

<script>
import { mapMutations,mapState } from "vuex";
import config from "../config.js";
import form_modal from "../components/FormModal.vue";
import http from "../http.js";
export default {
  components: { form_modal,mapState },
  computed: {
    ...mapState(["account"])
  },
  data() {
    return {
      appname: config.appName,
      formfields: [
        { title: "Old Password", key: "oldPassword", type: "password", validate: "required" },
        { title: "New Password", key: "password", type: "password", validate: "required, min(5)" },
        { title: "Confirm Password", key: "confirm", type: "password", validate: "required, min(5)" }
      ],
      formdata: {},
      showModal: false
    };
  },
  methods: {
    
    ...mapMutations(["changeSidebarVisibility"]),
  submit() {
      var request = {
        pwRequest : this.formdata
      };
      request.pwRequest.userEmail = this.account.email;

      http.post(config.modifyPassword, request).then(({ data }) => {
        console.log(data);
      });
      this.showModal = false;
    },
    logout(){
      window.location.replace("/logout");
      console.log("logging out");
      /*http.get("/logout").then(({ data }) => {
          console.log("logging out")
    });*/
  }
  }
};
</script>

<style lang="scss" scoped>
@import "../styles/colors.scss";
.app-header {
  background-color: $header-background;
  color: $header-textcolor;
}
.app-header-element {
  padding: 1em;
  position: relative;
  float: left;
  top: 50%;
  transform: translateY(-50%);
}
.hamburger-button {
  margin: 0px;
  padding: 0px;
  font-size: 16px;
  outline: 0px;
  &:hover {
    color: white;
  }
}

.small-btn {
  font-size: 12px;
  padding: 3px 10px;
  border-radius: 2px;
  text-transform: uppercase;
  letter-spacing: .5px;
  color: #fff;
}

.blue-btn {
  background-color: lighten($blue, 10%);
  &:hover {
    background-color: $blue;
  }
}

.yellow-btn {
  background-color: $light-yellow;
  &:hover {
    background-color: darken($light-yellow, 10%);
  }
}

.table-button1 {
  @extend .small-btn;
  @extend .yellow-btn;
  margin-left: 10px;
  margin-bottom: 10px;
  margin-top: 10px;
}

@media screen and (max-width: 400px) {
  .responsive {
    display: none;
  }
}
</style>