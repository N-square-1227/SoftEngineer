
<template>
  <div>
    <div style="margin-bottom: 5px;text-align: left">
      <el-input v-model="nodeName" placeholder="请输入节点名称" suffix-icon="el-icon-search" style="width: 200px;"
                @keyup.enter.native="loadPost"></el-input>
      <el-button type="primary" style="margin-left: 10px;" @click="loadPost">查询</el-button>
      <el-button type="success" @click="resetParam">重置</el-button>
    </div>
    <el-table :data="tableData"
              :header-cell-style="{ background:'#f2f5fc',color:'#555555'}">
      <el-table-column prop="nodeID" label="ID" width="100">
      </el-table-column>
      <el-table-column prop="nodeName" label="节点名称" width="300">
      </el-table-column>
      <el-table-column
          prop="nodeType" label="节点类型" width="200">
        <template slot-scope="scope">
          <span v-if="scope.row.nodeType === 0" >定量负向指标</span>
          <span v-if="scope.row.nodeType === 1" >定量正向指标</span >
          <span v-if="scope.row.nodeType === 2" >定性正向指标</span >
          <span v-if="scope.row.nodeType === 3" >定性负向指标</span >
        </template>
      </el-table-column>
      <el-table-column prop="nodeWeight" label="节点权重" width="200">
      </el-table-column>
      <el-table-column prop="parentID" label="父节点ID">
      </el-table-column>
    </el-table>

    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[5, 7, 10, 20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>

  </div>
</template>

<script>
import OptimizePage from "@/components/User/OptimizePage";

export default {
  name: "OptimizeNodeList",

  data() {
    return {
      tableData:[],
      currentPage : 1,
      pageSize : 5,
      total: 1,
      tableName: "",

      nodeName: "",
    }
  },
  created() {
    this.tableName = JSON.parse(sessionStorage.getItem("newTableName"));
    this.loadPost();
  },
  methods:{
    loadPost() {
      // console.log(this.tableName)
      this.$axios.post(this.$httpUrl+'/indexsym/nodeListPage',{
        pageSize:this.pageSize,
        pageNum:this.currentPage,
        param:{
          table_name:this.tableName,
          query_nodeName : this.nodeName,
        }
      }).then(res=>res.data).then(res=>{
        // console.log(res)
        if (res.code==200) {
          // console.log(res.data)
          // console.log(res.total)
          this.tableData = res.data
          this.total = res.total;
        }
        else
          this.$message.error('数据载入失败！');
      })
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.currentPage=1
      this.pageSize=val
      this.loadPost()
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.currentPage=val
      this.loadPost()
    },
    resetParam(){
      this.nodeName=''
      this.loadPost()
    },
  }
}
</script>