<template>
    <el-menu background-color="#545c64"
             text-color="#fff"
             active-text-color="#ffd04b"
             style="height: 100vh"
             :collapse="isCollapsed"
             :collapse-transition="false"
             router>
        <el-menu-item :index="'/'+item.menuClick" v-for="(item,i) in menu" :key="i">
            <i :class="item.menuIcon"></i>
            <span slot="title">{{item.menuName}}</span>
        </el-menu-item>
<!--        <el-menu-item :index="'/'+item.menuClick" v-for="(item,i) in menu":key="i">-->
<!--            <i class="item.menuIcon"></i>-->
<!--            <span>{{ item.menuName }}</span>-->
<!--        </el-menu-item>-->
<!--        <el-menu-item index="3-1">-->
<!--            <i class="el-icon-setting"></i>-->
<!--            <span>指标优化</span>-->
<!--        </el-menu-item>-->
    </el-menu>
</template>

<script>

export default {
    name: "Aside",
    data(){
        return{
        }
    },
    computed:{
        "menu":{
            get(){
                return this.$store.state.menu
            }
        }
    },
    props:{
        isCollapsed:Boolean
    }
}
</script>

<style scoped>

</style>