<template>
  <div>
    <div style="margin-bottom: 5px;text-align: left">
      <el-input v-model="name" placeholder="请输入节点名" suffix-icon="el-icon-search" style="width: 200px;"
                @keyup.enter.native="loadPost"></el-input>
      <el-button type="primary" style="margin-left: 10px;" @click="loadPost">查询</el-button>
      <el-button type="success" @click="resetParam">重置</el-button>
    </div>
    <el-table :data="tableData"
              :header-cell-style="{ background:'#f2f5fc',color:'#555555'}">
      <el-table-column prop="nodeID" label="ID" width="180">
      </el-table-column>
      <el-table-column prop="nodeName" label="节点名称" width="300">
      </el-table-column>
      <el-table-column
          prop="nodeType" label="节点类型" width="180"
          :filters="[{ text: '正指标', value: '1' }, { text: '负指标', value: '0' }]"
          :filter-method="filterRole" filter-placement="bottom-end">
        <template slot-scope="scope">
         <span v-if="scope.row.nodeType === 0">负指标</span >
          <span v-if="scope.row.nodeType === 1">正指标</span >
        </template>
      </el-table-column>
      <el-table-column prop="nodeWeight" label="节点权重" width="180">
      </el-table-column>
      <el-table-column prop="parentID" label="父节点">
      </el-table-column>
    </el-table>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[3, 5, 10, 20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>
  </div>
</template>

<script>
export default {
  name: "IndexNode",
  data(){
    return {
    tableData: [],
    pageSize:3,
    pageNum:1,
    total:0,
    name:'',
    centerDialogVisible: false,
    form:{
      nodeID:'',
      nodeName:'',
      nodeWeight:'',
      nodeType:'',
      parentID:'',
      },
    }
  },
  methods:{
    loadPost(){
      this.$axios.post(this.$httpUrl+'/import/nodeQuery',{
        pageSize:this.pageSize,
        pageNum:this.pageNum,
        param:{
          name:this.name,
        }
      }).then(res=>res.data).then(res=>{
        console.log(res)
        if (res.code==200) {
          this.tableData = res.data,
              this.total = res.total
        }
        else
          this.$message.error('无相关数据');
      })
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.pageNum=1
      this.pageSize=val
      this.loadPost()
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.pageNum=val
      this.loadPost()
    },
    resetParam(){
      this.name=''
      this.loadPost()
    },
    filterRole(value, row) {
      return row.role === value;
    },
    handleEdit(row){
      //赋值数据到表单
      this.form=row
      //展示表单
      this.centerDialogVisible=true
    },
  },
  beforeMount() {
    this.loadPost()
  }
}
</script>

<style scoped>

</style>