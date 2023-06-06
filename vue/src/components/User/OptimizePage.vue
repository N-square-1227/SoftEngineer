<template>
  <div style="position: fixed">
    <el-main style="text-align: center" >
      <el-select v-model="value1" placeholder="选择需要优化的指标体系" @change="getValue1" style="text-align: center;width: 200px">
        <el-option
            v-for="item1 in this.symList"
            :key=item1
            :value=item1>
        </el-option>
      </el-select>

      <el-select v-model="value2" placeholder="选择优化算法" @change="getValue2" style="text-align: center;width: 200px">
        <el-option
            v-for="item in options2"
            :key="item.value"
            :label="item.label"
            :value="item.value">
        </el-option>
      </el-select>

      <el-tooltip class="item" effect="dark" placement="top-start" content="确定">
        <el-button @click="toDrawTree">确定</el-button>
      </el-tooltip>

    </el-main>
  </div>
</template>

<script>
let OP
let name  // 初始的指标体系数据名
let func
export default {
    name: "OptimizePage",
    data() {
        return {
            pageSize:3,
            pageNum:1,
            user:JSON.parse(sessionStorage.getItem('CurUser')),
            treeData: [],
            ssl: [],
            OP,
            //options1: [option],
            /* 算法名应该是改这里吧, by wxy */
            options2: [{
                value: 'kmeans',
                label: 'K-Means聚类算法'
            }, {
                value: 'entropy',
                label: '熵权法调整指标权重'
            }, {
                value: 'pca',
                label: '主成分分析法'
            }],
            value1: '',
            value2: '',
            symList:[]
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
                    this.treeData.push(res.data[0])
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
        entropy(row){
            this.setSession(row.indexSymDTName,"entropy")
            this.loadSampleData()
            this.$axios.get(this.$httpUrl + '/indexSymNode/getTreeData?tableName=' + row.indexSymDTName + "&func=entropy").then(res => res.data).then(res => {
                // console.log(res)
                if (res.code == 200) {
                    this.treeData.push(res.data[0])
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
                    this.treeData.push(res.data[0])
                    /* 优化结果 */
                    sessionStorage.setItem("TreeData", JSON.stringify(this.treeData))
                    this.$message({
                        message: '优化成功！',
                        type: 'success'
                    });
                    this.$router.replace('/OptimizeResultFrame');
                } else
                    this.$message.error('优化失败！');
                this.symList = res.allSym;
                for(var i = 0; i <this.symList.length; i++) {
                    console.log(this.symList[i]);
                }
            })
            console.log("getAllSyms")
            //这是关键代码
        },
        getValue1(val1) {
            let values1;
            values1 = val1;
            name = val1
            sessionStorage.setItem("name", JSON.stringify(name))
        },
        getValue2(val2) {
            let values2;
            values2 = val2;
            func = val2;
            sessionStorage.setItem("func", JSON.stringify(func))
        },
        // sure() {
        //     this.$router.push("/DrawTree")
        //     /*      //跳转算法页面，修改对应的url
        //           if (values == "1") {
        //             this.$router.push({name: 'ImportExcel', params: {name,func}})
        //           }
        //           if (values == "2") {
        //             this.$router.push({name: 'ImportXML', params: {name,fun}})
        //           }
        //           if (values == "3") {
        //             this.$router.push({name: '/ImportJson', params: {name,fun}})
        //           }*/
        // },
        toDrawTree(){
          if(name == null) {
            this.$message({
              message: '未选择指标体系！',
              type: 'warning'
            });
          }
          else if(func == null) {
            this.$message({
              message: '未选择优化算法！',
              type: 'warning'
            });
          }
          else {
            this.$axios.get(this.$httpUrl + '/indexSymNode/getTreeData?tableName=' + name + "&func=" + func).then(res => res.data).then(res => {
              // console.log(res)
              if (res.code == 200) {
                // for (let i = 0; i < res.data.length; i++) {
                //   this.treeData.push(res.data[i])
                //   console.log(this.treeData)
                // }
                this.treeData = res.data.treeData;
                this.ssl = res.data.SSL;

                // console.log(this.treeData)
                /* 优化结果 */
                sessionStorage.setItem("TreeData", JSON.stringify(this.treeData));
                sessionStorage.setItem("SSL",JSON.stringify(this.ssl));
                /* 新指标体系的计算结果 */
                // sessionStorage.setItem("newResult", JSON.stringify(res.data[1]))
                /* 旧指标体系的计算结果 */
                // sessionStorage.setItem("originResult", JSON.stringify(res.data[2]))
                /* 加载指标体系使用的数据 */
                // this.loadSampleData()
                this.$message({
                  message: '优化成功！',
                  type: 'success'
                });
                // this.$router.replace('/DrawTree');//跳转到可视化界面
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
        },

    }
}


</script>

<style>

</style>
