<template>
  <div style="position: fixed">
    <div style="margin-bottom: 5px;overflow: hidden">
      <div style="text-align: left;float: left">
        <el-input v-model="nodeName" placeholder="请输入指标体系名称" suffix-icon="el-icon-search" style="width: 200px;"
                  @keyup.enter.native="getAllSyms"></el-input>
        <el-button style="margin-left: 10px;" @click="getAllSyms">查询</el-button>
        <el-button type="primary" plain style="margin-right: 10px;" @click="resetParam">重置</el-button>
      </div>
      <div style="text-align: right;float:right;">
        <el-select v-model="value" placeholder="选择导入指标体系数据方式" @change="getValue" style="width: 250px;margin-right: 10px">
          <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
          </el-option>
        </el-select>
        <el-tooltip class="item" effect="dark" content="指标数据仅支持excel导入">
          <el-button @click="sure" type="success">导入新数据</el-button>
        </el-tooltip>
      </div>
    </div>
    <el-table :data="symList"
              :header-cell-style="{ background:'#f2f5fc',color:'#555555'}">
      <el-table-column prop="id" label="ID" width="200">
      </el-table-column>
      <el-table-column prop="indexSymDTName" label="指标体系" width="400">
      </el-table-column>
      <el-table-column label="操作" width="600">
        <template slot-scope="scope">
          <el-button slot="reference" size="mini" type="primary" style="margin-right:10px" @click="detail(scope.row)">详细信息</el-button>
          <el-popconfirm
              title="确定删除吗？"
              @confirm="del(scope.row)"
          >
            <el-button slot="reference" size="mini" type="danger">删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[5, 10]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>
  </div>
</template>

<script>
let values;
let name  // 初始的指标体系数据名
export default {
  name: "importFiles",

  data() {
    return {
      options: [{
        value: 'excel',
        label: 'excel'
      }, {
        value: 'xml',
        label: 'xml'
      }, {
        value: 'json',
        label: 'json'
      }],
      value: '',
      currentPage : 1,
      pageSize : 5,
      total:0,
      user:JSON.parse(sessionStorage.getItem('CurUser')),
      treeData: [],
      symList:[],
      nodeName:"",
    }
  },
  beforeMount() {
    this.getAllSyms()
    sessionStorage.removeItem("sample_result")
  },
  methods:{
    getValue(val){
      values=val;
      console.log(values)
    },
    sure(){
        if(values=="excel"){
            this.$router.push({name:'ImportExcel',params: {values}})
        }
        else if(values=="xml"){
            this.$router.push({name:'ImportXML',params: {values}})
        }
        else if(values=="json"){
            this.$router.push({name:'ImportJson',params: {values}})
        } else{
          this.$message.warning("请选择数据导入格式！")
        }
    },
    getAllSyms(){
      this.$axios.post(this.$httpUrl+'/import/indexSymListPage',{
        pageSize:this.pageSize,
        pageNum:this.currentPage,
        param:{
          table_name:this.user.userName+"_data",
          queryName:this.nodeName,
        }
      }).then(res=>res.data).then(res=>{
        if (res.code==200) {
          this.total = res.total;
          for(let i = 0; i < this.total; i ++)
            res.data[i].indexSymDTName = res.data[i].indexSymDTName.substring(this.user.userName.length + 1)
          // console.log(res.data)
          this.symList = res.data
        }
        else
          this.$message.error('数据加载失败！');
      })
    },
    resetParam(){
      this.nodeName=''
      this.getAllSyms()
    },
    detail(row){
      this.$axios.get(this.$httpUrl + '/indexSymNode/getOriginalTreeData?tableName='
                      + this.user.userName + "_" + row.indexSymDTName).then(res => res.data).then(res => {
        // console.log(res)
        if (res.code == 200) {
          this.treeData= res.data
          sessionStorage.setItem("OriginalTreeData",JSON.stringify(this.treeData))
          sessionStorage.setItem("IndexName",JSON.stringify(this.user.userName + "_" + row.indexSymDTName))
          this.$message({
            message: '成功！',
            type: 'success'
          });
          this.$router.replace('/IndexSymManage');//跳转到可视化界面
        }
        else
          this.$message.error('失败！');
      })
    },
    del(row){
      this.$axios.get(this.$httpUrl+'/user/delTable?tableName='+ row.indexSymDTName+"&user="+this.user.userName).then(res=>res.data).then(res=>{
        console.log(res)
        if(res.code==200){
          this.$message({
            message: '删除成功！',
            type: 'success'
          });
          this.getAllSyms()
        }else{
          this.$message({
            message: '删除失败！',
            type: 'error'
          });
        }
      })
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.currentPage=1
      this.pageSize=val
      this.getAllSyms()
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.currentPage=val
      this.getAllSyms()
    },
  }
}


</script>

<style scoped>

</style>
