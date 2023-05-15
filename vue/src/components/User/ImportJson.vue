<template>
    <div>
        <el-main style="text-align: center">
            <a  size="mini" class="el-upload__tip">请先下载模板文件，再按照规定格式上传</a>
            <br><br>
            <el-button type="primary" plain @click="download1" style="text-align: center">节点数据模板下载</el-button><br><br>
            <!--   上传文件 -->
            <el-upload
                    accept=".json"
                    class="upload-demo"
                    action="111"
                    :http-request="jsonUpload"
                    :on-change="handleChange"
                    style="text-align: center"
            >
                <el-button type="primary"  >点击上传结点数据</el-button><br>
                <div slot="tip" class="el-upload__tip">只能上传Json文件</div>
            </el-upload>
            <!--    </el-form-item>-->
            <!--  </el-form>-->
            <br>
            <el-button type="submit" @click="jumpBehind">上传</el-button>
            <el-button>取消</el-button>
        </el-main>
    </div>
</template>

<script >
export default {
    name:"ImportJson",
    data() {
        return{
            user:JSON.parse(sessionStorage.getItem('CurUser')),
            file: {
                name: 'example.json',
                url: 'http://localhost:8080/static'
            }
        }
    },
    methods: {
        handleChange(file, fileList) {
            this.fileList = fileList.slice(-3);
        },
        /*如果直接在el-upload写这一段url地址会出现跨域的问题，所以直接用表单*/
        jsonUpload(file) {
            let fn=file.name
            console.log(file.file)
            console.log("jsonUpload")
            const formData = new FormData()
            formData.append("file",file.file)
            this.$axios({
                method:'post',
                data:formData,
                url:this.$httpUrl+'/import/json',
                headers:{'Content-Type': 'multipart/form-data'}
            }).then(function (resp){
                console.log("1111111111111");
            })
        },
        download1() {
            const config = {
                method: 'get',
                url: this.$httpUrl+'/import/downloadJson',
                headers: {
                    //和后端设置的一样
                    'Content-Type': 'application/octet-stream;charset=UTF-8'
                },
                responseType: 'blob'
            };
            this.$axios(config).then(response => {
                const url = window.URL.createObjectURL(new Blob([response.data]));
                const link = document.createElement('a');
                link.href = url;
                link.setAttribute('download', 'example.json');
                document.body.appendChild(link);
                link.click();
            })
        },
        //点击上传跳转后端
        jumpBehind(){
            //this.$router.push("/keepExcel")
            this.$axios.get(this.$httpUrl+'/import/keepJson?userName='+this.user.userName).then(res=>res.data).then(res=>{
                console.log(res)
                if (res.code==200) {
                    this.$message({
                        message: '上传成功！',
                        type: 'success'
                    });
                }
                else
                    this.$message.error('上传失败！');
            })
        }
    },
}
</script>
<style scoped>

</style>
