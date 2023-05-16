<template>
    <el-menu background-color="#545c64"
             text-color="#fff"
             active-text-color="#ffd04b"
             style="height: 100vh"
             router>
        <el-menu-item index="userManage">
            <i class="el-icon-menu"></i>
            <span>用户管理</span>
        </el-menu-item>
        <el-menu-item index="ImportFiles">
            <i class="el-icon-menu"></i>
            <span>导入数据</span>
        </el-menu-item>
        <el-menu-item index="3-1">
            <i class="el-icon-setting"></i>
            <span>指标优化</span>
        </el-menu-item>
    </el-menu>

</template>

<script>
export default {
    name: "Aside"
}
</script>

<style scoped>

</style>
