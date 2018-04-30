<template>
<div>
  <div class="comment-wrapper">
    <div class="header">
      Comments
       <span style="float: right">
        <button class="small-btn blue-btn" @click="showModal=true">NEW</button>
      </span>
    </div>

    <div class="comment" v-if="comments" :key="index" v-for="(item, index) in comments">
      <div class="comment-title">
        <span class="title-name">{{item.user}} - </span>
        <span class="title-date">{{item.sendDate}}</span>
      </div>
      <div class="comment-content">
        {{item.message}}
      </div>
    </div>
  </div>

    <form_modal
      v-show="showModal" 
      :fields="formfields" 
      :formdata="formdata" 
      @onSubmitClick="submit" 
      @onCancelClick="showModal=false">
    </form_modal>
</div>
</template>
[]
<script>
import { mapState } from "vuex";
import moment from "moment";
import http from "../http.js";
import config from "../config.js";
import form_modal from "../components/FormModal.vue";
export default {
    components: {form_modal},
  props: {
    issueId: String,
    name: String,
    comments: Array,
    issue: Object
  },
  data() {
    return {
      showModal: false,
      formfields: [
        { title: "Comment", key: "comment", type: "text", validate: "required, min(3)" }
      ],
      formdata: {},
    };
  },
  created() {
  },
  methods: {
      fetch(){
      http.get(config.getIssueDetails + "/" + this.issueId).then(({ data }) => {
        this.comments = data.comments;
        console.log("issue comments: ", this.comments);
      });
    },
    submit() {
        var request = {
                message : this.formdata.comment,
                user :  this.name,
                issueId : this.issueId
            }
        http.post(config.addCommentUrl +"/"+ this.issueId, request).then(({ data }) => {
            this.hide = false;
            console.log("resp: ",data);
            this.fetch(); 
            this.showModal = false;
      });
    },
    scrollToBottom() {
      var element = document.getElementById("messages-area-id");
      element.scrollTop = element.scrollHeight;
    }
  }
};
</script>

<style lang="scss" scoped>
@import "../styles/colors.scss";
.comment-wrapper {
  float: right;
  font-size: 14px;
  border: 1px solid $table-border-color;
  border-bottom: 0;
  width: 100%;
}
.header {
  font-size: 16px;
  border-bottom: 1px solid $table-border-color;
  padding: 10px;
  font-weight: 600;
}
.comment {
  border-bottom: 1px solid $table-border-color;
  padding: 5px;
}
.comment-title {
  padding-bottom: 5px;
}
.title-name {
  font-weight: 600;
  color: $blue;
}
.title-date {
   color: $blue;
}
</style>