import VueRouter from "vue-router";

const routes = [
    {
        path:'/',
        name:'Login',
        component:()=>import('../components/Login')
    },
    {
        path:'/Synthesis',
        name:'Synthesis',
        component:()=>import('../components/Synthesis')
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
            {
                path: "/userManage",
                name: 'userManage',
                component:()=>import('../components/Admin/userManage')
            },
            {
                path: '/ImportExcel',
                name: 'ImportExcel',
                component:()=>import("../components/User/ImportExcel")
            },
            {
                path: '/ImportFiles',
                name: 'ImportFiles',
                component:()=>import("../components/User/ImportFiles")
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
            },
            {
                path: '/OptimizeResultFrame',
                name: 'OptimizeResultFrame',
                component:()=>import("../components/User/OptimizeResultFrame")
            },
            {
                path: '/KmeansResultFrame',
                name: 'KmeansResultFrame',
                component:()=>import("../components/User/KmeansResultFrame")
            },
            {
                path: '/PCAResultFrame',
                name: 'PCAResultFrame',
                component:()=>import("../components/User/PCAResultFrame")
            },
            {
                path :'/IndexSymManage',
                name: 'IndexSymManage',
                component:()=>import("../components/User/IndexSymManage"),
                children:[
                    {
                        path: '/DrawOriginalTree',
                        name: 'DrawOriginalTree',
                        component:()=>import("../components/User/Comments/DrawOriginalTree")
                    },
                    {
                        path: '/IndexNode',
                        name: 'IndexNode',
                        component:()=>import("../components/User/Comments/IndexNode")
                    },
                    {
                        path: '/IndexNodeData',
                        name: 'IndexNodeData',
                        component:()=>import("../components/User/Comments/IndexNodeData")
                    }
                ]
            }
        ]
    },
    {
        path:'/Register',
        name:'Register',
        component:()=>import('../components/Register')
    },
]

const router = new VueRouter({
    mode:'history',
    routes
})

export function resetRouter() {
    router.matcher = new VueRouter({
        mode:'history',
        routes: []
    }).matcher
}

const VueRouterPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (to) {
    return VueRouterPush.call(this, to).catch(err => err)
}

export default router;