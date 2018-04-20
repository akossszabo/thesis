<template>
  <div v-show="wsconnected" class="chat-wrapper" :class="{ 'shorter-wrapper': !showContent }">
    <div class="chat-header unselectable" @click="showContent=!showContent">
      {{headerText}} {{projectId}}
    </div>
    <div class="chat-content" v-show="showContent">
      <div class="messages-area" id="messages-area-id">
        <div class="message" :key="index" v-for="(item, index) in msg">
          <strong>{{item.user}}</strong>
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
    projectId: String
  },
  data() {
    return {
      showContent: false,
      headerText: "chat",
      actual: "",
      msg: [],
      username: "Harry",
      chatInstance: "",
      wsconnected: false
    };
  },
  created() {
    this.connect();
  },
  methods: {
    submit(event) {
      var now = new Date();
      var textTrimmed = event.target.innerText.trim();
      var message = {
        text: textTrimmed,
        username: this.username,
        time: now
      };
      console.log(message);
      this.stompClient.send("/message/1", {}, JSON.stringify(message));
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
      console.log("try to connect...")
      this.stompClient = Stomp.over(this.socket);
      this.stompClient.connect({}, this.subscribe, this.onError);
    },
    subscribe(frame) {
      console.log("try to subscribe...");
      let pid = this.projectId;
      this.stompClient.subscribe("/topic/1",this.addMessage);
      this.wsconnected = true;
    },
    onError(frame) {
      console.log(frame);
      console.log("connect failed, try to reconnect...");
      this.connectWs();
    },

    addMessage(frame) {
      let message = JSON.parse(frame.body);
      console.log("megjött: ", message.username);
      this.msg.push({
        user: message.username,
        text: message.text,
        time: moment(message.time).format()
      });

      console.log(this.msg);
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