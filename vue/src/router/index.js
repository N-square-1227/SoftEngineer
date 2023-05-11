import Vue from 'vue'
import Router from 'vue-router'

/*const routes = [
  {
    path: '/afterLogin',
    name: 'afterLogin',
    component:()=>import("../view/afterLogin")
  },{
    path: '/ImportExcel',
    name: 'ImportExcel',
    component:()=>import("../view/ImportExcel")
  }

]*/


Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/afterLogin',
      name: 'afterLogin',
      component:()=>import("../view/afterLogin")
    },
    {
      path: '/ImportExcel',
      name: 'ImportExcel',
      component:()=>import("../view/ImportExcel")
    }
  ]
})
