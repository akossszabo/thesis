<template>
<div>
  <div class="chat-wrapper">
      <div class="chat-header" @click="showContent=!showContent">
        Comments
      </div>
    <div class="chat-content">
      <div class="messages-area" id="messages-area-id">
        <div v-if="comments" class="message" :key="index" v-for="(item, index) in comments">
          <strong>{{item.user}}</strong>
          <span class="time-span">{{item.sendDate}}</span>
          <br/> {{item.message}} 
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
     <span style="float: right">
          <button class="small-btn blue-btn" @click="showModal=true">Add Comment</button>
        </span>

    
    </div>
</template>

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
.chat-wrapper {
  position: fixed;
  width: 700px;
  font-size: 12px;
  right: 80px;
  bottom: 80px;
  z-index: 8888;
  border: 1px solid $light-grey;
  border-radius: 0px 0px 0px 0px;
}
@media screen and (max-width: 600px) {
  .chat-wrapper {
    width: 300px;
  }
}
@media screen and (max-width: 400px) {
  .chat-wrapper {
    width: 250px;
  }
}
@media screen and (max-width: 325px) {
  .chat-wrapper {
    display: none;
  }
}
.shorter-wrapper {
  width: 150px;
}
.chat-header {
  background-color: lighten($blue, 10%);
  padding: 2px;
  padding-left: 5px;
  border-radius: 0px 0px 0px 0px;
  cursor: pointer;
  color: white;
  font-size: 13px;
  font-weight: 500;
}
.chat-content {
  background-color: white;
}
.messages-area {
  padding: 2px;
  height: 360px;
  overflow-y: auto;
  overflow-x: hidden;
}
.message {
  word-wrap: break-word;
  padding: 2px;
}
.time-span {
  font-size: 10px;
  color: $grey;
}
.textbox-area {
  border-top: 1px solid $light-grey;
  padding: 2px;
  height: 40px;
}
</style>