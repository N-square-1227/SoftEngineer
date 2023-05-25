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
let name
let func
export default {
    name: "OptimizePage",
    data() {
        return {
            pageSize:3,
            pageNum:1,
            user:JSON.parse(sessionStorage.getItem('CurUser')),
            treeData: [],
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
    },
    methods: {
        //获取该用户上传的所有指标体系
        getAllSyms(){
            this.$axios({
                method:'get',
                url:'http://localhost:8877/import/getAllSyms/'+this.user.userName
            }).then(res=>res.data).then(res=>{
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
            sessionStorage.setItem("name", JSON.stringify(val1))
        },
        getValue2(val2) {
            let values2;
            values2 = val2;
            func = val2;
            sessionStorage.setItem("func", JSON.stringify(val2))
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
                this.treeData.push(res.data[0])
                // console.log(this.treeData)
                /* 优化结果 */
                sessionStorage.setItem("TreeData", JSON.stringify(this.treeData))
                /* 新指标体系的计算结果 */
                sessionStorage.setItem("newResult", JSON.stringify(res.data[1]))
                /* 旧指标体系的计算结果 */
                sessionStorage.setItem("originResult", JSON.stringify(res.data[2]))
                /* 加载指标体系使用的数据 */
                this.loadSampleData()
                /* 保存数据表的名字（优化后指标体系存储的数据表） */
                const newName = name + "_new_" + func;
                sessionStorage.setItem("newTableName", JSON.stringify(newName))
                this.$message({
                  message: '优化成功！',
                  type: 'success'
                });
                // this.$router.replace('/DrawTree');//跳转到可视化界面
                this.$router.replace('/OptimizeResultFrame');
              } else
                this.$message.error('优化失败！');
            })
          }
        },
        loadSampleData(){
          this.$axios.post(this.$httpUrl + '/indexsym/loadNewData', {
            pageSize: this.pageSize,
            pageNum : this.pageNum,
            param:{
              basicTableName: name, // 这是原始指标体系的表名,优化后的表名添加使用的函数，数据表名添加后缀
              func: func,
            }
          }).then(res => res.data).then(res => {
            // console.log(res);
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
    }
}


</script>

<style>

</style>

