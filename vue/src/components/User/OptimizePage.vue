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

            <el-tooltip class="item" effect="dark" content="指标数据仅支持excel导入" placement="top-start">
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
            user:JSON.parse(sessionStorage.getItem('CurUser')),
            treeData: [],
            OP,
            //options1: [option],
            options2: [{
                value: '1',
                label: '1'
            }, {
                value: '2',
                label: '2'
            }, {
                value: '3',
                label: '3'
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
            //console.log("getAllSyms")
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
            this.$axios.get(this.$httpUrl+'/indexSymNode/getTreeData?tableName='+name+"&func="+func).then(res=>res.data).then(res=>{
                console.log(res)
                if (res.code==200) {
                    for(let i=0;i<res.data.length;i++){
                        this.treeData.push(res.data[i])
                        console.log(this.treeData)
                    }
                    sessionStorage.setItem("TreeData",JSON.stringify(this.treeData))
                    this.$message({
                        message: '优化成功！',
                        type: 'success'
                    });
                    this.$router.replace('/DrawTree');//跳转到可视化界面
                }
                else
                    this.$message.error('优化失败！');
            })
        }
    }
}


</script>

<style>

</style>

