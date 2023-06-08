<template>
    <el-container style="height:100%;">
<!--        <el-aside width="200px" style="background-color: rgb(238,241,246);height: 100%;margin-left: -1px">-->
<!--            <Aside></Aside>-->
<!--        </el-aside>-->
        <el-container style="height: 100%;">
            <el-header style="font-size: 12px;height: 100%;border-bottom: rgb(168,168,168,0.5) 1px solid;">
                <Header></Header>
            </el-header>
            <el-main style="height: 100%;">
               <!-- <Main></Main> -->
                <router-view/>
            </el-main>
        </el-container>
    </el-container>
</template>

<script>
import Header from "@/components/Header";
export default {
    name: 'UserHomePage',
    components: { Header},

    methods: {},
    activated() {
        this.initData();
    }
}
</script>

<style scoped>

</style>
