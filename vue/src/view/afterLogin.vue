<template>
  <el-container>
    <el-header>欢迎使用指标管理系统</el-header>

  <el-container style="height: 500px; border: 1px solid #eee">

    <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
      <el-menu :default-openeds="['1', '3']"
               router
               :default-active="this.$route.path">
          <el-menu-item-group>
            <el-menu-item index="/ImportFiles"><i class="el-icon-menu"></i>导入数据test</el-menu-item>
          </el-menu-item-group>
        <el-menu-item-group>
          <el-menu-item index="3-1"><i class="el-icon-setting"></i>指标优化</el-menu-item>
        </el-menu-item-group>
      </el-menu>

    </el-aside>
    <keep-alive>
      <router-view></router-view>
    </keep-alive>
  </el-container>
  </el-container>
</template>

<script>
export default {
  data() {
    return {};
  },
  methods: {},
  activated() {
    this.initData();
  }
}
</script>

<style scoped>
.el-header {
  background-color: #B3C0D1;
  color: #2c3e50;
  line-height: 50px;

}

.el-aside {
  color: #2af598;
}
</style>
