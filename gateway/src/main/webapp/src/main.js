import Vue from 'vue'
import VueRouter from 'vue-router'
import App from './App.vue'
import routes from './routes.js'
import store from './store/store.js'
import Vuetify from 'vuetify'
Vue.use(VueRouter)
Vue.use(Vuetify)


new Vue({
  el: '#app',
  store,
  router: new VueRouter({
    routes: routes
  }),
  template: '<App/>',
  components: { App }
})
