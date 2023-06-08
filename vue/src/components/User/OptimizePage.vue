<template>
  <div style="position: fixed">
    <div style="margin-bottom: 5px;text-align: left">
      <el-input v-model="nodeName" placeholder="请输入指标体系名称" suffix-icon="el-icon-search" style="width: 200px;"
                @keyup.enter.native="getAllSyms"></el-input>
      <el-button style="margin-left: 10px;" @click="getAllSyms">查询</el-button>
      <el-button type="primary" plain @click="resetParam">重置</el-button>
    </div>
    <el-table :data="symList"
              :header-cell-style="{ background:'#f2f5fc',color:'#555555'}">
      <el-table-column prop="id" label="ID" width="200">
      </el-table-column>
      <el-table-column prop="indexSymDTName" label="指标体系" width="400">
      </el-table-column>
      <el-table-column label="选择优化算法" width="600">
        <template slot-scope="scope">
          <el-popover
              placement="bottom"
              width="160"
              trigger="click">
            <el-select placeholder="请选择" v-model="algorithmValue" size="mini">
              <el-option
                  v-for="item in algorithmOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                  style="font-size: 10px">
              </el-option>
            </el-select>
            <el-button type="primary"
                       @click="chooseAlgorithm(scope.row)"
                       size="mini"
                       style="margin-top: 5px;float: right">确认</el-button>
            <template #reference>
              <el-button type="success" size="mini">优化</el-button>
            </template>
          </el-popover>
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
let name  // 初始的指标体系数据名
let func
export default {
    name: "OptimizePage",
    data() {
        return {
          currentPage : 1,
          pageSize : 5,
          total:0,
          user:JSON.parse(sessionStorage.getItem('CurUser')),
          treeData: [],
          sse: [],
          symList:[],
          nodeName:"",
          algorithmOptions: [{
            value: 'kmeans',
            label: 'K-means聚类'
          }, {
            value: 'entropy',
            label: '熵权法调整指标权重'
          }, {
            value: 'pca',
            label: '主成分分析法'
          }],
          algorithmValue: ''
        }
    },
    beforeMount() {
        this.getAllSyms()
        sessionStorage.removeItem("sample_result")
    },
    methods: {
        //获取该用户上传的所有指标体系
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
      setSession(tableName,alg){
          func=alg
          name=tableName
          sessionStorage.setItem("name", JSON.stringify(name))
          sessionStorage.setItem("func", JSON.stringify(func))
      },
      chooseAlgorithm(row) {
        if(this.algorithmValue === 'kmeans')
          this.KMeans(row);
        else if (this.algorithmValue === 'entropy')
          this.entropy(row);
        else if (this.algorithmValue === 'pca')
          this.pca(row);
        else {
          this.$message.warning("请选择优化算法！")
        }
      },
      KMeans(row){
          this.setSession(this.user.userName + "_" + row.indexSymDTName,"kmeans")
          // this.loadSampleData()
          this.$axios.get(this.$httpUrl + '/indexSymNode/getTreeData?tableName='
                              + this.user.userName + "_" + row.indexSymDTName
                              + "&func=kmeans").then(res => res.data).then(res => {
              // console.log(res)
              if (res.code == 200) {
                  this.treeData= res.data.treeData
                  this.sse = res.data.SSE
                  /* 优化结果 */
                  sessionStorage.setItem("TreeData", JSON.stringify(this.treeData))
                  sessionStorage.setItem("SSE",JSON.stringify(this.sse))
                  this.$message({
                      message: '优化成功！',
                      type: 'success'
                  });
                  this.$router.replace('/KmeansResultFrame');
              } else
                  this.$message.error('优化失败！');
          })
      },
      entropy(row){
          this.setSession(this.user.userName + "_" + row.indexSymDTName,"entropy")
          // this.loadSampleData()
          this.$axios.get(this.$httpUrl + '/indexSymNode/getTreeData?tableName='
                              + this.user.userName + "_" + row.indexSymDTName
                              + "&func=entropy").then(res => res.data).then(res => {
              // console.log(res)
              if (res.code == 200) {
                this.treeData= res.data.treeData
                  /* 优化结果 */
                  sessionStorage.setItem("TreeData", JSON.stringify(this.treeData))
                  this.$message({
                      message: '优化成功！',
                      type: 'success'
                  });
                  this.$router.replace('/OptimizeResultFrame');
              } else
                  this.$message.error('优化失败！');
          })
      },
      pca(row){
          this.setSession(this.user.userName + "_" + row.indexSymDTName,"pca");
          this.$axios.get(this.$httpUrl + '/indexSymNode/getTreeData?tableName='
                              + this.user.userName + "_" + row.indexSymDTName
                              + "&func=pca").then(res => res.data).then(res => {
              // console.log(res)
              if (res.code == 200) {
                this.treeData= res.data.treeData
                  /* 优化结果 */
                  sessionStorage.setItem("TreeData", JSON.stringify(this.treeData))
                  sessionStorage.setItem("loadmatrix",JSON.stringify(this.loadmatrix))
                  this.$message({
                      message: '优化成功！',
                      type: 'success'
                  });
                  this.$router.replace('/OptimizeResultFrame');
              } else
                  this.$message.error('优化失败！');
          })
        // this.loadSampleData()
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

<style>

</style>
