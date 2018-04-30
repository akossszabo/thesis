<template>
  <div v-show="showChat" class="chat-wrapper" :class="{ 'shorter-wrapper': !showContent }">
    <div class="chat-header unselectable" @click="showContent=!showContent">
      {{headerText}} {{projectId}}
    </div>
    <div class="chat-content" v-show="showContent">
      <div class="messages-area" id="messages-area-id">
        <div class="message" :key="index" v-for="(item, index) in msg">
          <strong>{{item.username}}</strong>
          <span class="time-span">{{item.time}}</span>
          <br/> {{item.text}} </div>
      </div>
      <div class="textbox-area" contenteditable="true" @keyup.enter="submit"></div>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import moment from "moment";
import http from "../http.js";
import config from "../config.js";
export default {
  props: {
    projectId: String,
    name: String
  },
  beforeDestroy: function() {
  },
  data() {
    return {
      showContent: false,
      headerText: "chat",
      actual: "",
      msg: [],
      showChat: false,
      wsConnected: false,
      interval: null,
      islastArrived: true
    };
  },
  created() {
    this.initMessages();
    this.connect();
  },
  methods: {
    submit(event) {
      var now = new Date();
      var textTrimmed = event.target.innerText.trim();
      var message = {
        text: textTrimmed,
        username: this.name,
        time: now
      };
      console.log(message);
      let pid = this.projectId;
      if (this.wsConnected) {
        this.stompClient.send("/message/" + pid, {}, JSON.stringify(message));
      } else {
        this.postMessage(message);
      }
      event.target.innerText = "";
      this.scrollToBottom();
    },
    scrollToBottom() {
      var element = document.getElementById("messages-area-id");
      element.scrollTop = element.scrollHeight;
    },
    connect() {
      this.socket = new SockJS("/chat-service/chat-service");

      this.connectWs();
    },
    connectWs() {
      console.log("try to connect...");
      this.stompClient = Stomp.over(this.socket);
      let pid = this.projectId;
      //this.getFormerMessages(pid);
      this.stompClient.connect({}, this.subscribe, this.onError);
    },
    subscribe(frame) {
      console.log("try to subscribe...");
      let pid = this.projectId;
      this.stompClient.subscribe("/topic/" + pid, this.addMessage);
      this.showChat = true;
      this.wsConnected = true;
    },
    onError(frame) {
      console.log(frame);
      console.log("connect failed, polling started");
      const self = this;

      //this.interval = setInterval(
        //function() {self.getFormerMessages(self.projectId);},1000);
      //console.log("interval after set: " + this.interval);
      this.connectWs();
      //this.showChat = true;
    },
    addMessage(frame) {
      let message = JSON.parse(frame.body);
      console.log("megjött: ", message.username);
      this.msg.push({
        username: message.username,
        text: message.text,
        time: moment(message.time).format()
      });

      console.log(this.msg);
    },
    initMessages(){
      http.get(config.getFormerMessages + "/" + this.projectId).then(({ data }) => {
          console.log(data);
          this.msg = data;
        }).catch(function (error) {
          console.log("hiba történt: ", error);
      });
    },
    getFormerMessages(pid) {
      if(this.islastArrived){
        this.islastArrived = false;
        http.get(config.getFormerMessages + "/" + pid).then(({ data }) => {

          console.log(data);
          this.msg = data;
          this.islastArrived = true;
        }).catch(function (error) {
          console.log("hiba történt: ", error);
      });
      }
    },
    postMessage(request) {
      //this.msg.push(request);
      http
        .post(config.postMessage + "/" + this.projectId, request)
        .then(({ data }) => {
          console.log("message send: ".request);
        });
    }
  }
};
</script>

<style lang="scss" scoped>
@import "../styles/colors.scss";
.chat-wrapper {
  position: fixed;
  width: 400px;
  font-size: 12px;
  right: 50px;
  bottom: 0px;
  z-index: 8888;
  border: 1px solid $light-grey;
  border-radius: 4px 4px 0px 0px;
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
  background-color: $blue;
  padding: 2px;
  padding-left: 5px;
  border-radius: 4px 4px 0px 0px;
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