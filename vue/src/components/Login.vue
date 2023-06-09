/**
* @author lmy
*/

<template>
    <div class="login-page">
        <el-form :model="form" :rules="loginRules" ref="loginForm" class="login-container" @keyup.enter.native="doLogin">
            <h1 class="title">🔐指标优化系统</h1>
            <el-form-item prop="userName">
                <el-input type="text" v-model="form.userName" auto-complete="off" placeholder="请输入用户名">
                    <template slot="prepend"><i style="font-size:20px" class="el-icon-user"></i></template>
                </el-input>
            </el-form-item>
            <el-form-item prop="userPassword">
                <el-input type="text" v-model="form.userPassword" auto-complete="off" placeholder="请输入密码" show-password>
                    <template slot="prepend"><i style="font-size:20px" class="el-icon-key"></i></template>
                </el-input>
            </el-form-item>
            <el-form-item>
                <el-button style="width:100%;" type="primary" @click="doLogin" :disabled="disabled ">登录</el-button>
            </el-form-item>
            <el-row style="text-align: right;margin-top: -10px;">
                <el-link type="primary" @click="toRegister">用户注册</el-link>
            </el-row>
        </el-form>
    </div>
</template>

<script>
export default {
    name: 'Login',
    data(){
        return {
            //让登录按钮失效
            disabled: false,
            form:{
                userName: '',
                userPassword: ''
            },
            loginRules:{
                userName: [
                    { required: true, message: '请输入账户', trigger: 'blur' }
                ],
                userPassword: [
                    { required: true, message: '请输入密码', trigger: 'blur'}
                ]
            }
        }
    },
    methods:{
        doLogin(){
            this.disabled=true;
            this.$refs.loginForm.validate((valid)=>{
                if(valid){    //valid成功为true 失败为false
                    //后端验证用户名密码
                    this.$axios.post(this.$httpUrl+'/user/login',this.form).then(res=>res.data).then(res=>{
                        console.log(res.code)
                        //成功
                        if(res.code==200){
                            //存储当前用户
                            sessionStorage.setItem("CurUser",JSON.stringify(res.data.user))
                            console.log(res.data.menu)
                            this.$store.commit("setMenu",res.data.menu)
                            this.$message({
                                message: '登录成功！',
                                type: 'success'
                            });
                            this.$router.replace('/UserHomePage');//跳转到用户主页
                        }
                        //失败
                        else{
                            this.disabled=false;
                            this.$message.error('用户名或密码错误！');
                            return false;
                        }
                    });
                }
                else{
                    this.disabled=false;
                    console.log('校验失败');
                    return false;
                }
            });
        },

        toRegister(){
            this.$router.replace('/Register');
        }
    }
}
</script>

<style scoped>
.login-page{
    background-image: linear-gradient(180deg, #2af598 0%, #009efd 100%);
    height: 100vh;
    display: flex;
    justify-content: center;
    align-items: center;
}
.login-container {
    border-radius: 10px;
    margin: 0px auto;
    width: 350px;
    padding: 30px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    text-align: left;
    box-shadow: 0 0 20px 2px rgba(0, 0, 0, 0.1);
}

.title {
    margin: 0px auto 40px auto;
    text-align: center;
    color: #505458;
    font-size: 20px;
}
</style>

