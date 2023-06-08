<template>
    <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
      <el-page-header @back="goBack" content="详情页面" style="margin-bottom: 5px"> </el-page-header>
      <el-tab-pane label="指标体系树" name="first">
            <drawOriginalTree></drawOriginalTree>
        </el-tab-pane>
        <el-tab-pane label="节点信息" name="second">
            <indexNode></indexNode>
        </el-tab-pane>
        <el-tab-pane label="节点数据" name="third">
            <indexNodeData></indexNodeData>
        </el-tab-pane>
    </el-tabs>
</template>

<script>
import drawOriginalTree from "@/components/User/Comments/DrawOriginalTree";
import indexNode from "@/components/User/Comments/IndexNode";
import indexNodeData from "@/components/User/Comments/IndexNodeData";
export default {
  data() {
    return {
      activeName: 'first'
    };
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    },
    goBack() {
      this.$router.replace("/ImportFiles")
    }
  },
  beforeMount() {
    sessionStorage.removeItem("sample_result")
  },
  components:{
    drawOriginalTree,
    indexNode,
    indexNodeData,
  }
};
</script>

<style scoped>

</style>