<template>
    <div>
        <div style="margin-bottom: 5px;text-align: left">
            <el-input v-model="name" placeholder="请输入名字" suffix-icon="el-icon-search" style="width: 200px;"
                      @keyup.enter.native="loadPost"></el-input>
            <el-button type="primary" style="margin-left: 10px;" @click="loadPost">查询</el-button>
            <el-button type="success" @click="resetParam">重置</el-button>
        </div>
        <el-table :data="tableData"
                  :header-cell-style="{ background:'#f2f5fc',color:'#555555'}">
            <el-table-column prop="userID" label="ID" width="100">
            </el-table-column>
            <el-table-column prop="userName" label="用户名" width="180">
            </el-table-column>
            <el-table-column prop="userPassword" label="密码" width="180">
            </el-table-column>
            <el-table-column prop="userEmail" label="邮箱" width="180">
            </el-table-column>
            <el-table-column
                    prop="role" label="角色" width="180"
                    :filters="[{ text: '管理员', value: '1' }, { text: '用户', value: '2' }]"
                    :filter-method="filterRole" filter-placement="bottom-end">
                <template slot-scope="scope">
                    <el-tag
                            :type="scope.row.role == 1 ? 'primary' : 'success'"
                            disable-transitions>{{scope.row.role == 1 ? '管理员' : '用户'}}</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="操作">
                <template slot-scope="scope">
                    <el-button
                            size="mini"
                            type="primary"
                            @click="handleEdit(scope.row)">编辑</el-button>
                    <el-popconfirm title="确定删除吗？"
                                   @confirm="handleDelete(scope.row.userID)"
                                   style="margin-left: 5px">
                        <el-button slot="reference" size="mini" type="danger">删除</el-button>
                    </el-popconfirm>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="pageNum"
                :page-sizes="[3, 5, 10, 20]"
                :page-size="pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="total">
        </el-pagination>
        <el-dialog
                title="用户信息"
                :visible.sync="centerDialogVisible"
                width="30%"
                center>
            <el-form :model="form" :rules="rules" ref="registerForm" class="register-container">
                <el-form-item prop="userName">
                    <el-input :disabled="true" type="text" v-model="form.userName" auto-complete="off" placeholder="用户名">
                        <template slot="prepend"><i style="font-size:20px" class="el-icon-user"></i></template>
                    </el-input>
                </el-form-item>
                <el-form-item prop="userPassword">
                    <el-input type="password" v-model="form.userPassword" auto-complete="off" placeholder="密码" show-password>
                        <template slot="prepend"><i style="font-size:20px" class="el-icon-key"></i></template>
                    </el-input>
                </el-form-item>
                <el-form-item prop="userEmail">
                    <el-input type="text" v-model="form.userEmail" auto-complete="off" placeholder="邮箱">
                        <template slot="prepend"><i style="font-size:20px" class="el-icon-message"></i></template>
                    </el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="update">确 定</el-button>
                <el-button @click="centerDialogVisible = false">取 消</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
export default {
    name: "Main",
    data() {
        return {
            tableData: [],
            pageSize:3,
            pageNum:1,
            total:0,
            name:'',
            centerDialogVisible: false,
            form:{
                userID:'',
                userName:'',
                userPassword:'',
                userEmail:''
            },
            rules:{
                userName: [
                    { required: true, message: '请输入用户名', trigger: 'blur' },
                ],
                userPassword: [
                    { required: true, message: '请输入密码', trigger: 'blur'}
                    ,{ pattern: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{5,20}$/, message: '密码必须同时包含数字与字母,且长度为 5-20位' }
                ],
                userEmail: [
                    { required: true, message: "请输入邮箱", trigger: "blur", type: 'email'},
                ]
            }
        }
    },
    methods:{
        loadPost(){
            this.$axios.post(this.$httpUrl+'/user/userListPage',{
                pageSize:this.pageSize,
                pageNum:this.pageNum,
                param:{
                    name:this.name,
                }
            }).then(res=>res.data).then(res=>{
                console.log(res)
                if (res.code==200) {
                    this.tableData = res.data,
                            this.total = res.total
                }
                else
                    this.$message.error('用户名或密码错误！');
            })
        },
        handleSizeChange(val) {
            console.log(`每页 ${val} 条`);
            this.pageNum=1
            this.pageSize=val
            this.loadPost()
        },
        handleCurrentChange(val) {
            console.log(`当前页: ${val}`);
            this.pageNum=val
            this.loadPost()
        },
        resetParam(){
            this.name=''
            this.loadPost()
        },
        filterRole(value, row) {
            return row.role === value;
        },
        handleEdit(row){
            //赋值数据到表单
            this.form=row
            //展示表单
            this.centerDialogVisible=true
        },
        update(){
            this.$axios.post(this.$httpUrl+'/user/update',this.form).then(res=>res.data).then(res=>{
                console.log(res)
                if (res.code==200) {
                    this.$message({
                        message: '修改成功！',
                        type: 'success'
                    });
                    this.centerDialogVisible=false
                }
                else
                    this.$message.error('修改失败！');
            })
        },
        handleDelete(userID){
            this.$axios.get(this.$httpUrl+'/user/delete?userID='+userID).then(res=>res.data).then(res=>{
                console.log(res)
                if (res.code==200) {
                    this.$message({
                        message: '删除成功！',
                        type: 'success'
                    });
                    this.centerDialogVisible=false
                    this.loadPost()
                }
                else
                    this.$message.error('删除失败！');
            })
        }
    },
    beforeMount() {
        this.loadPost()
    }
}
</script>

<style scoped>

</style>