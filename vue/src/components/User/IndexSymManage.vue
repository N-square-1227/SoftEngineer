<template>
  <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
    <el-tab-pane label="指标体系树" name="first">指标体系树</el-tab-pane>
    <el-tab-pane label="节点信息" name="second">节点信息</el-tab-pane>
    <el-tab-pane label="节点数据" name="third">节点数据</el-tab-pane>
  </el-tabs>
</template>

<script>
export default {
  data() {
    return {
      activeName: 'first'
    };
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    }
  }
};
</script>

<style scoped>

</style>