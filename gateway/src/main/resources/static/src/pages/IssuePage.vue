<template>
  <div class="app-container">
    <p>Issue: {{issue.issueName}}</p>

    {{issue.comments}}
    
    {{issue.summary}}
    
    
    
   

   <div id="datas1" class="box">
        <div>type:  {{issue.type}}</div>
        <div>reporter: {{issue.reporter}}</div>
        <div>status: {{issue.status}}</div>
        <div>assignee: {{issue.assignee}}</div>
        <div>adding date: {{project.creationDate}}</div>
        <div>priority: {{issue.priority}}</div>
      </div>

  <div>
        <span style="float: right; margin-left: 5px;">
          <button class="small-btn blue-btn">Edit</button>
        </span>
        <span style="float: right">
          <button class="small-btn blue-btn">Add Note</button>
        </span>
      </div>

 <form_modal
      v-show="show_modal" 
      :fields="formfields" 
      :formdata="formdata" 
      @onSubmitClick="submit" 
      @onCancelClick="show_modal=false">
    </form_modal>
  

  </div>
</template>

<script>
import { mapState } from "vuex";
import http from "../http.js";
import config from "../config.js";
import form_modal from "../components/FormModal.vue";

export default {
  components: {  form_modal,mapState },
   computed: {
    ...mapState(["account"])
   },
  data() {
    return {
      issueId: null,
      username: "",
      formfields: [
        { title: "Comment", key: "comment", type: "text", validate: "required, min(3)" }
      ],
      formdata: {},
      comments: [],
      // issueName: "",
      // summary: "",
      // assignee: "",
      // reporter: "",
      // priority: "",
      // type: "",
      issue: null,
      show_modal:false
    };
  },
  created() {
    this.$store.commit('setSidebarTitle', 'Issues');

  },
  mounted() {
    let path = window.location.hash.substring(1);
    let patharray = path.split("/");
    this.buildPage(patharray[patharray.length-1]);
  },
  beforeRouteUpdate (to, from, next) {
    this.buildPage(to.params.id);
    next();
  },
  methods: {
    submit() {
        var request = {
                comment : this.formdata,
                user : this.account.firstName + " " + this.account.lastName
                
            }
            request.comment.issueId = this.issueId;
            request.issue.reporter = this.account.email;
        http.post(config.addCommentUrl +"/"+ this.issueId, request).then(({ data }) => {
            this.hide = false;
            console.log("resp: ",data);
            this.serverMessage = data.message;
            this.showMessage = true;
            this.buildPage(this.projectId);

            setTimeout(() => {
                this.showMessage = false;
            }, 5000);
      });
      this.showModal = false;
    },
    buildPage(id) {
      this.issueId = id;
       http.get(config.getIssueDetails + "/" + this.issueId).then(({ data }) => {
        this.comments = data.comments;
        this.issue = data;
      });
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