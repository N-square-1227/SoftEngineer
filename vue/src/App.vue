<template>
    <div id="app">
        <router-view/>
    </div>
</template>

<script>

export default {
    name: 'App',
    components: {
    },
    data(){
        return {
            user:JSON.parse(sessionStorage.getItem('CurUser'))
        }
    },
    watch:{
        //监视store.state.menu
        '$store.state.menu':{
            handler(val,old){
                //旧数据丢失 但是用户和id存在
                if(!old && this.user && this.user.userID)
                    this.$store.commit("setMenu",val)
            },
            immediate:true
        }
    }
}
</script>

<style>
#app {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
}
</style>
