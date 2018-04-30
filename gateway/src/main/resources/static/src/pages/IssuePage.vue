<template>
  <div v-if="issue != null" class="app-container">
   <div class="info-container" v-show="!hiddenTable">
      <div class="box">
        <div style="padding-bottom: 5px;">
          <span class="title">Issue: {{issue.name}}</span>
        </div>
        Assignee : {{issue.assignee}} 
      </div>

       <div class="box">
        <div>type:  {{issue.type}}</div>
        <div>reporter: {{issue.reporter}}</div>
        <div>status: {{issue.status}}</div>
        <div>assignee: {{issue.assignee}}</div>
        <div>adding date: {{issue.creationDate}}</div>
        <div>priority: {{issue.priority}}</div>
      </div>
        <div>
        <span style="float: right; margin-left: 5px;">
          <button class="small-btn blue-btn" >Edit</button>
        </span>
      </div>

      </div>
      <div id="summary" class="summary">
        <div class="title"> Summary </div>
        <div><span class="activity-name">{{issue.summary}}</span> </div>
        
      </div>

   <comment_section
    :comments="comments"
    :issueId="issueId"
    :issue="issue"
    :name="account.firstName + ' ' + account.lastName">
   </comment_section>  
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
import { mapState } from "vuex";
import http from "../http.js";
import config from "../config.js";
import form_modal from "../components/FormModal.vue";
import comment_section from "../components/CommentSection.vue";
export default {
  components: { form_modal, mapState, comment_section },
  computed: {
    ...mapState(["account"])
  },
  data() {
    return {
      issueId: null,
      username: "",
      comments: [],
      issue: null,
      showModal: false
    };
  },
  created() {
    this.$store.commit("setSidebarTitle", "Issues");
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
    submit() {
      var request = {
        comment: this.formdata,
        user: this.account.firstName + " " + this.account.lastName
      };
      request.comment.issueId = this.issueId;
      request.issue.reporter = this.account.email;
      http
        .post(config.addCommentUrl + "/" + this.issueId, request)
        .then(({ data }) => {
          this.hide = false;
          console.log("resp: ", data);
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
        console.log("issue comments: ", this.comments);
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
  width: 40%;
  float: left;
  max-height: 98%;
  overflow: auto;
}
.box {
  margin-bottom: 10px;
  border: 1px solid $table-border-color;
  padding: 8px;
}
.summary {
  margin-bottom: 10px;
  border: 1px solid $table-border-color;
  padding: 8px;
  float: right;
  width: 50%;
  font-size: 12px;
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
  #datas1,
  #datas2 {
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