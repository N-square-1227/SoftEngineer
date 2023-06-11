<template>
    <div style="display: flex;line-height: 60px;">
        <div style="flex: 1;text-align: center;font-size: 25px;">
            <span>欢迎使用指标优化系统</span>
        </div>
        <el-dropdown>
          <span style="font-size: 15px">用户： {{user.userName}}</span>
          <i class="el-icon-arrow-down" style="margin-left: 8px;font-size: 10px"></i>
          <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="toUser">个人中心</el-dropdown-item>
              <el-dropdown-item @click.native="toLeft">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
    </div>
</template>

<script>
export default {
    name: "Header",
    data(){
        return{
            //转换一下
            user:JSON.parse(sessionStorage.getItem('CurUser'))
        }
    },
    methods:{
        toUser(){
            console.log("toUser")
            this.$router.push("/Home")
        },
        toLeft(){
            console.log("toLeft")

            this.$confirm('确定要退出登录吗?','提示',{
                confirmButtonText:'确定',   //确认按钮的文字显示
                type:'warning',
                center:true
            }).then(()=>{
                this.$message({
                    type:'success',
                    message:'退出登录成功'
                })
                this.$router.push("/")
                //清空数据
                sessionStorage.clear()
            }).catch(()=>{
                this.$message({
                    type:'info',
                    message:'已取消退出登录'
                })
            })
        }
    },
    created(){
        console.log("toHome")
        this.$router.push("/Home")
    }
}
</script>

<style scoped>
.body{
    margin: 0;
}
</style>