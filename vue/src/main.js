import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from "element-ui";
import 'element-ui/lib/theme-chalk/index.css';
import axios from 'axios';
import './assets/test.css';
import './assets/global.css'
import Message  from 'element-ui';
import VueRouter from "vue-router";
Vue.prototype.$http = axios
Vue.prototype.$axios=axios;
Vue.prototype.$Message=Message;
Vue.config.productionTip = false
Vue.prototype.$httpUrl='http://localhost:8877'

Vue.use(VueRouter);
Vue.use(ElementUI);
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>',
  render: h => h(App),
}).$mount('#app')


