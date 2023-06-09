<template>
    <div style="text-align: center;height:100%;background-color: #f1f1f3;margin: 0px;padding-bottom: 20px">
        <h1 style="font-size: 30px;margin-bottom: 20px">个人中心</h1>
        <el-descriptions :column="2" size="40" border style="height:100%">
            <el-descriptions-item>
                <template slot="label">
                    <i class="el-icon-suitcase"></i>
                    用户ID
                </template>
                {{user.userID}}
            </el-descriptions-item>
            <el-descriptions-item>
                <template slot="label">
                    <i class="el-icon-user"></i>
                    用户名
                </template>
                {{user.userName}}
            </el-descriptions-item>
            <el-descriptions-item>
                <template slot="label">
                    <i class="el-icon-tickets"></i>
                    角色
                </template>
                <el-tag
                        type="success"
                        disable-transitions>{{user.role==1?"管理员":"用户"}}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item>
                <template slot="label">
                    <i class="el-icon-message"></i>
                    邮箱
                </template>
                {{user.userEmail}}
            </el-descriptions-item>
        </el-descriptions>

        <div style="margin-top: 20px">
            <el-button v-if="user.role===1" @click="toUser" type="success" size="big">用户管理</el-button>
            <el-button v-else type="success" @click="toIndex" size="big">指标体系管理</el-button>

            <el-button type="primary" @click="modify" size="big">修改个人信息</el-button>
            <el-button type="danger" @click="changePwd" size="big">修改密码</el-button>
        </div>

        <!--    修改个人信息-->
        <el-dialog
                title="修改个人信息"
                :visible.sync="ModifyInfoDialog"
                width="30%"
                center>
            <el-form ref="form" :rules="rulesModify" :model="form" label-width="80px">
                <el-form-item prop="userName" label="用户名">
                    <el-input :disabled="true" v-model="form.userName"></el-input>
                </el-form-item>
                <el-form-item prop="userEmail" label="邮箱">
                    <el-input v-model="form.userEmail"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="ModifyInfoDialog = false">取 消</el-button>
                <el-button type="primary" @click="modifySave()">确 定</el-button>
            </span>
        </el-dialog>
        <!--   修改密码-->
      <el-dialog
          title="修改密码"
          :visible.sync="ModifyPwdDialog"
          width="30%"
          center>
        <el-form ref="pwdForm" :rules="rulesModifyPwd" :model="pwdForm" label-width="100px">
          <el-form-item label="旧密码" prop="oldUserPassword">
            <el-input v-model="pwdForm.oldUserPassword"></el-input>
          </el-form-item>
          <el-form-item label="新密码" prop="newPwd">
            <el-input v-model="pwdForm.newPwd"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
                <el-button @click="ModifyPwdDialog = false">取 消</el-button>
                <el-button type="primary" @click="modifyPwdSave()" >确 定</el-button>
            </span>
      </el-dialog>

    </div>
</template>

<script>
export default {
    // eslint-disable-next-line vue/multi-word-component-names
    name: "Home",

    data() {
        return {
            user:{},
            ModifyInfoDialog: false,
            ModifyPwdDialog:false,
            form:{
                userName:'',
                userEmail:''
            },
            pwdForm:{
                oldUserPassword:'',
                newPwd: ''
            },
            rulesModify:{
                userName: [
                    { required: true, message: '请输入用户名', trigger: 'blur' },
                    { max: 10, message: '长度不超过 10 字符', trigger: 'blur' }
                ],
                userEmail:[
                    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
                    { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
                ],
            },
            rulesModifyPwd:{
                newPwd: [
                    { required: true, message: '请输入密码', trigger: 'blur'}
                    ,{ pattern: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{5,20}$/, message: '密码必须同时包含数字与字母,且长度为 5-20位' }
                ]
            }
        }
    },
    methods:{
        init(){
            this.user = JSON.parse(sessionStorage.getItem('CurUser'))
        },
        toUser(){
          this.$router.replace("/userManage")
        },
        toIndex(){
          this.$router.replace("/ImportFiles")
        },
        modify() {
            this.form = this.user;
            this.ModifyInfoDialog = true;
        },
        resetFrom(){
            this.form.userName=''
            this.form.userEmail=''
            this.pwdForm.newPwd=''
        },
        modifySave(){
            this.$refs.form.validate((valid) => {
                if (valid) {
                    this.$axios.post(this.$httpUrl + '/user/updateInfo', {
                        param: {
                            userName: this.form.userName,
                            userEmail:this.form.userEmail,
                            userID: this.user.userID
                        }
                    }).then(res=>res.data).then(res=>{
                        console.log(res)
                        if(res.code === 200) {
                            this.$message({
                                showClose: true,
                                message: '操作成功',
                                type: 'success'
                            });
                            this.ModifyInfoDialog=false;
                            this.resetFrom();
                            this.reloadUser();
                        }else {
                            this.$message({
                                showClose: true,
                                message: '操作失败!',
                                type: 'error'
                            });
                        }
                    })
                } else {
                    console.log('error submit!!');
                    return false;
                }
            });
        },
        reloadUser() {
            this.$axios.get(this.$httpUrl + '/user/userDetail?userID=' +this.user.userID ).then(res=>res.data).then(res=>{
                console.log(res);
                if(res.code==200){
                    sessionStorage.setItem("CurUser",JSON.stringify(res.data))
                    this.init();
                }else {
                    this.$message.error("上传失败");
                }
            })
        },
        changePwd() {
            this.ModifyPwdDialog = true;
            this.pwdForm.userPassword = '';
            this.pwdForm.oldUserPassword = '';
        },
        modifyPwdSave() {
          this.$refs.pwdForm.validate((valid) => {
            if (valid) {
              if (this.pwdForm.oldUserPassword == this.user.userPassword) {
                this.$axios.post(this.$httpUrl + '/user/updatePwd', {
                  param: {
                    newPwd: this.pwdForm.newPwd,
                    userID: this.user.userID,
                  }
                }).then(res => res.data).then(res => {
                  console.log(res);
                  if (res.code == 200) {
                    this.ModifyPwdDialog = false;
                    this.$message.success("修改成功");
                  } else {
                    this.$message.error("修改失败");
                  }
                  this.resetFrom();
                  this.reloadUser();
                })
              } else
                this.$message.error("修改失败");
            }
          })
        },
    },
    created(){
        this.init()
    }
}
</script>

<style>
.el-descriptions{
    width:90%;
    height:100%;
    margin: 0 auto;
    text-align: center;
}
</style>