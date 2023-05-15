<<<<<<< HEAD
// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from "element-ui";
import 'element-ui/lib/theme-chalk/index.css';
import axios from 'axios';
import './assets/test.css';
import Message  from 'element-ui';
Vue.prototype.$http = axios
Vue.prototype.$axios=axios;
Vue.prototype.$Message=Message;
Vue.config.productionTip = false
Vue.use(ElementUI);
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
=======
import Vue from 'vue'
import App from './App.vue'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import axios from 'axios';
import VueRouter from "vue-router";
import router from "./router";
import './assets/global.css'
Vue.prototype.$axios =axios;
Vue.config.productionTip = false
Vue.prototype.$httpUrl='http://localhost:8877'

Vue.use(VueRouter);
Vue.use(ElementUI);
new Vue({
  router,
  render: h => h(App),
}).$mount('#app')
>>>>>>> ae64deceea9317932cffef9f3ca9df382eda48db
