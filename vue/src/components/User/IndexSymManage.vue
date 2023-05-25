<template>
    <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
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
  beforeMount() {
    this.loadPost();
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    },
    loadPost(){
      this.$axios.post(this.$httpUrl + '/import/loadData', {
        pageSize: this.pageSize,
        pageNum : this.pageNum,
      }).then(res => res.data).then(res => {
        // console.log(res);
        if(res.code == 200) {
          const colNum = res.data.colNum;
          sessionStorage.setItem("data", JSON.stringify(res.data.sampleData));
          // console.log(res.data.sampleData)
          sessionStorage.setItem("colNum", res.data.colNum);
          sessionStorage.setItem("sampleNum", res.data.sampleNum);
        }
        else {
          this.$message.error('数据加载出错！');
        }
      })
    },
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