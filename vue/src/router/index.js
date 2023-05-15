import VueRouter from "vue-router";

const routes = [
    {
        path:'/',
        name:'Login',
        component:()=>import('../components/Login')
    },
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
            }
        ]
    },
    {
        path:'/AdminHomePage',
        name:'AdminHomePage',
        component:()=>import('../components/AdminHomePage')
    },
    {
        path:'/Register',
        name:'Register',
        component:()=>import('../components/Register')
    },
    {
        path: '/ImportExcel',
        name: 'ImportExcel',
        component:()=>import("../components/User/ImportExcel")
    },
    {
        path: '/ImportXML',
        name: 'ImportXML',
        component:()=>import("../components/User/ImportXML")
    },
    {
        path: '/ImportJson',
        name: 'ImportJson',
        component:()=>import("../components/User/ImportJson")
    }
]

const router = new VueRouter({
    mode:'history',
    routes
})

const VueRouterPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (to) {
    return VueRouterPush.call(this, to).catch(err => err)
}

export default router;