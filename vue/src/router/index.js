import Vue from 'vue'
import Router from 'vue-router'


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
    },
    {
      path: '/ImportFiles',
      name: 'ImportFiles',
      component:()=>import("../view/ImportFiles")
    },
    {
      path: '/ImportXML',
      name: 'ImportXML',
      component:()=>import("../view/ImportXML")
    }

  ]
})
