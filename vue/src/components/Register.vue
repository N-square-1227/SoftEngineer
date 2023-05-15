/**
 * @author lmy
 */

<template>
  <div class="register-page">
    <el-form :model="form" :rules="registerRules" ref="registerForm" class="register-container">
      <h1 class="title">🔐后台管理系统</h1>
      <el-form-item prop="userName">
        <el-input type="text" v-model="form.userName" auto-complete="off" placeholder="用户名">
          <template slot="prepend"><i style="font-size:20px" class="el-icon-user"></i></template>
        </el-input>
      </el-form-item>
      <el-form-item prop="userPassword">
        <el-input type="password" v-model="form.userPassword" auto-complete="off" placeholder="密码" show-password>
          <template slot="prepend"><i style="font-size:20px" class="el-icon-key"></i></template>
        </el-input>
      </el-form-item>
      <el-form-item prop="cpwd">
        <el-input type="password" v-model="form.cpwd" auto-complete="off" placeholder="确认密码" show-password>
          <template slot="prepend"><i style="font-size:20px" class="el-icon-key"></i></template>
        </el-input>
      </el-form-item>
      <el-form-item prop="userEmail">
        <el-input type="text" v-model="form.userEmail" auto-complete="off" placeholder="邮箱">
          <template slot="prepend"><i style="font-size:20px" class="el-icon-message"></i></template>
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-button style="width:48%;" type="primary" @click="doRegister" :disabled="disabled">注册</el-button>
        <el-button style="width:48%;" type="primary" @click="toLogin">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>

export default {
  name: 'Register',
  data(){
    return {
      //让登录按钮失效
      disabled: false,
      form:{
        userName: '',
        userPassword: '',
        userEmail: '',
        role:2
      },
      registerRules:{
        userName: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
        ],
        userPassword: [
          { required: true, message: '请输入密码', trigger: 'blur'}
          ,{ pattern: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{5,20}$/, message: '密码必须同时包含数字与字母,且长度为 5-20位' }
        ],
        userEmail: [
          { required: true, message: "请输入邮箱", trigger: "blur", type: 'email'},
          //{ validator: checkEmail, trigger: "blur" }
        ],
        cpwd: [{
          required: true, message: '确认密码', trigger: 'blur'}
          , {
            validator: (rule, value, callback) => {
            if (value === '') {
              callback(new Error('请再次输入密码'))
            } else if (value !== this.form.userPassword) {
              callback(new Error('两次输入密码不一致'))
            } else {
              callback()
            }
          },
          trigger: 'blur'
        }]
      }
    }
  },
  methods:{
    toLogin(){
      this.$router.replace('/');
    },

    doRegister() {
      this.disabled = true;
      this.$refs.registerForm.validate((valid) => {
        if (valid) {    //valid成功为true 失败为false
          //后端验证用户名密码
          this.$axios.post(this.$httpUrl+'/user/register',this.form).then(res=>res.data).then(res=>{
            console.log(res)
            //成功
            if (res.code == 200) {
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
            else {
              this.disabled = false;
              this.$message({
                message: '用户名已存在！',
                type: 'warning'
              });
              return false;
            }
          });
        } else {
          this.disabled = false;
          console.log('校验失败');
          return false;
        }
      });
    }
  }
}
</script>

<style scoped>
.register-page{
  background-image: linear-gradient(180deg, #2af598 0%, #009efd 100%);
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
}
.register-container {
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

