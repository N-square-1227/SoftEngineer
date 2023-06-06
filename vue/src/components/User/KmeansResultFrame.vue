<template>
  <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
    <el-tab-pane label="指标体系优化结果" name="first">
      <DrawTree></DrawTree>
    </el-tab-pane>
    <el-tab-pane label="指标列表" name="second">
      <OptimizeNodeList></OptimizeNodeList>
    </el-tab-pane>
    <el-tab-pane label="指标数据" name="third">
      <OptimizeData></OptimizeData>
    </el-tab-pane>
    <el-tab-pane label="对比结果" name="forth">
      <OptimizeResult></OptimizeResult>
    </el-tab-pane>
  </el-tabs>
</template>

<script>
import OptimizePage from "@/components/User/OptimizePage";
import DrawTree from "@/components/User/KmeansTree";
import OptimizeResult from "@/components/User/OptimizeResult";
import OptimizeData from "@/components/User/OptimizeData";
import OptimizeNodeList from "@/components/User/OptimizeNodeList";
export default {
  name: "KmeansResultFrame",
  components: {OptimizeNodeList, OptimizeData, OptimizeResult, DrawTree, OptimizePage},
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
}
</script>

<style scoped>

</style>