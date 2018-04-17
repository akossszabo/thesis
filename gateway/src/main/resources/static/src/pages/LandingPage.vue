<template>
    <form_modal 
      v-show="showModal"
      :fields="formfields"
      :formdata="formdata"
      @onSubmitClick="submit"
      @onCancelClick="showModal=false">
    </form_modal>

</template>

<script>
import infobox from "../components/InfoBox.vue";
import http from "../http.js";
import config from "../config.js";
import form_modal from "../components/FormModal.vue";
export default {
  components: { infobox, form_modal},

  data() {
    return {
      showModal: false,
      showMessage: false,      
       formfields: [
        { title: "name", key: "username", type: "text", validate: "required, min(2)" },
        { title: "pw", key: "password", type: "text", validate: "required, min(2)" }
      ],
      datas: [],
      formdata: {}
    };
  },
  created() {
        this.showModal = true;
  },
  methods: {
    submit() {
        let header = {
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'Authorization' : 'Negotiate'
        }
        }

        console.log("user: ", this.formdata.username , " pw: ", this.formdata.password);
        var request = {
                username : this.formdata.username,
                password : this.formdata.password
            }

        http.post(config.loginUrl+ "?username=" +  this.formdata.username +"&password=" + this.formdata.password, JSON.stringify(request),header).then(({ data }) => {
            this.hide = false;
            console.log("resp: ",data);
            this.serverMessage = data.message;
            this.showMessage = true;

            setTimeout(() => {
                this.showMessage = false;
            }, 5000);
      });

      this.showModal = false;
    }
  }
};
</script>

<style lang="scss" scoped>
.row {
  display: table;
  width: 100%;
  table-layout: fixed;
}

.column {
  display: table-cell;
  width: 25%;
  float: left;
}
</style>