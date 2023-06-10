import vue from 'vue'
import Vuex from 'vuex'
import router, {resetRouter} from "@/router";
import createPersistedState from 'vuex-persistedstate'

vue.use(Vuex)

function addNewRoute(menuList) {
    let routes = router.options.routes
    console.log(menuList)
    console.log(routes)
    routes.forEach(routeItem=>{
        if(routeItem.path=="/UserHomePage"){
            menuList.forEach(menu=>{
                let childRoute = {
                    path:'/'+menu.menuClick,
                    name:menu.menuName,
                    component:()=>import('../components/'+menu.menuComponent)
                }
                   routeItem.children.push(childRoute)
            })
        }
    })
    resetRouter()
    router.addRoutes(routes)
}

export default new Vuex.Store({
    state: {
        menu: []
    },
    mutations: {
        setMenu(state, menuList) {
            state.menu = menuList
            //添加路由
            addNewRoute(menuList)
        }
    },
    getters: {
        getMenu(state) {
            return state.menu
        }
    },
    plugins:[createPersistedState()]
})
