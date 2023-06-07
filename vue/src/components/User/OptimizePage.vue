<template>
  <div style="position: fixed">
    <div style="margin-bottom: 5px;text-align: left">
      <el-input v-model="nodeName" placeholder="请输入节点名称" suffix-icon="el-icon-search" style="width: 200px;"
                @keyup.enter.native="getAllSyms"></el-input>
      <el-button type="primary" style="margin-left: 10px;" @click="getAllSyms">查询</el-button>
      <el-button type="success" @click="resetParam">重置</el-button>
    </div>
    <el-table :data="symList"
              :header-cell-style="{ background:'#f2f5fc',color:'#555555'}">
      <el-table-column prop="id" label="ID" width="200">
      </el-table-column>
      <el-table-column prop="indexSymDTName" label="指标体系" width="400">
      </el-table-column>
      <el-table-column label="选择优化算法" width="600">
        <template slot-scope="scope">
          <el-button
              slot="reference"
              size="mini"
              type="primary"
              @click="KMeans(scope.row)">K-Means聚类算法</el-button>
          <el-button
              slot="reference"
              size="mini"
              type="danger"
              @click="entropy(scope.row)">熵权法调整指标权重</el-button>
          <el-button
              slot="reference"
              size="mini"
              type="success"
              @click="pca(scope.row)">主成分分析法</el-button>
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
                    this.symList = res.data
                    this.total = res.total;
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
            func=alg,
                    name=tableName,
                    sessionStorage.setItem("name", JSON.stringify(name))
            sessionStorage.setItem("func", JSON.stringify(func))
        },
        KMeans(row){
            this.setSession(row.indexSymDTName,"kmeans")
            this.loadSampleData()
            this.$axios.get(this.$httpUrl + '/indexSymNode/getTreeData?tableName=' + row.indexSymDTName + "&func=kmeans").then(res => res.data).then(res => {
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
            this.setSession(row.indexSymDTName,"entropy")
            this.loadSampleData()
            this.$axios.get(this.$httpUrl + '/indexSymNode/getTreeData?tableName=' + row.indexSymDTName + "&func=entropy").then(res => res.data).then(res => {
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
            this.setSession(row.indexSymDTName,"pca")
            this.loadSampleData()
            this.$axios.get(this.$httpUrl + '/indexSymNode/getTreeData?tableName=' + row.indexSymDTName + "&func=pca").then(res => res.data).then(res => {
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
        loadSampleData(){
            this.$axios.post(this.$httpUrl + '/indexsym/loadNewData', {
                pageSize:this.pageSize,
                pageNum:this.currentPage,
                param:{
                    basicTableName: name, // 这是原始指标体系的表名,优化后的表名添加使用的函数，数据表名添加后缀
                    func: func,
                }
            }).then(res => res.data).then(res => {
                console.log(res);
                if(res.code == 200) {
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
