<template>
  <div class="app-container">
    <p>Project: {{projectId}}</p>
	<chat_window></chat_window>
  </div>
</template>

<script>
import chat_window from "../components/ChatWindow.vue";
export default {
  components: {chat_window},
  data() {
    return {
      projectId: null
    };
  },

  mounted () {
  },

  methods: {
  }
};
</script>