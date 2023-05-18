<template>
    <div>
            <el-button style="width:100%;" type="primary" @click="toDrawTree">登录</el-button>
    </div>
</template>

<script>
export default {
    name: "OptimizePage",
    data(){
        return{
            treeData: []
        }
    },
    methods:{
        toDrawTree(){
            // this.$axios.get(this.$httpUrl+'/indexSymNode/getTreeData?tableName=indexsym').then(res=>res.data).then(res=>{
            //     console.log(res)
            //     //成功
            //     if(res.code==200){
            //         //存储当前用户
            //         sessionStorage.setItem("TreeData",JSON.stringify(res.data))
            //
            //         this.$message({
            //             message: '优化成功！',
            //             type: 'success'
            //         });
            //         this.$router.replace('/DrawTree');//跳转到可视化界面
            //     }
            //     //失败
            //     else{
            //         this.disabled=false;
            //         this.$message.error('失败！');
            //         return false;
            //     }
            // });
            this.$axios.get(this.$httpUrl+'/indexSymNode/getTreeData?tableName=indexsym').then(res=>res.data).then(res=>{
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

