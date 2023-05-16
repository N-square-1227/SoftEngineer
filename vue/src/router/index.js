import Vue from 'vue'
import Router from 'vue-router'


Vue.use(Router)

export default new Router({
  routes: [
    {
      path:'/UserHomePage',
      name:'UserHomePage',
      component:()=>import('../components/UserHomePage'),
      children:[
        {
          path:'/Home',
          name:'Home',
          component:()=>import('../components/Home')
        },
        //导航跳转
        {
          path:'/userManage',
          name:'userManage',
          component:()=>import('../components/Main.vue')
        },
        {
          path: '/ImportFiles',
          name: 'ImportFiles',
          component:()=>import("../components/User/ImportFiles.vue")
        },{
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
        },
        {
          path: '/ImportJson',
          name: 'ImportJson',
          component:()=>import("../components/User/ImportJson")
        },

      ]
    },
    {
      path:'/Register',
      name:'Register',
      component:()=>import('../components/Register')
    } ,
    {
      path:'/',
      name:'Login',
      component:()=>import('../components/Login')
    },

  ]
})

