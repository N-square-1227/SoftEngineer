<template>

    <el-main style="text-align: center">
        <a  size="mini" class="el-upload__tip">请先下载模板文件，再按照规定格式上传</a>
        <br><br>
      <el-button type="primary" plain size="mini" @click="download1" style="text-align: center">节点数据模板下载</el-button><br><br>
      <el-button type="primary" plain size="mini" @click="download2" style="text-align: center">指标数据模板下载</el-button><br><br>
<br>
      <el-upload
        accept=".json"
        class="upload-demo"
        ref="clearAll1"
        action="111"
        :http-request="jsonUpload"
        :on-change="handleChange"
        style="text-align: center"
      >
        <el-button type="primary"  size="mini">选择结点数据文件</el-button><br><br>
      </el-upload>
      <el-tooltip class="item" effect="dark" content="结点数据仅支持json导入" placement="top-start">
        <el-button type="submit" size="mini" @click="jumpBehind">上传</el-button>
      </el-tooltip>
      <br><br>

      <el-upload
        accept=".xlsx,.xls"
        class="upload-demo"
        ref="clearAll2"
        action="localhost:8877/import/excel"
        :http-request="excelUpload"
        :on-change="handleChange"
        style="text-align: center"
      >
        <el-button  type="primary" size="mini" >选择指标数据文件</el-button><br><br>
      </el-upload>
      <el-tooltip class="item" effect="dark" content="指标数据仅支持excel导入" placement="top-start">
        <el-button type="submit" size="mini" @click="jumpBehind">上传</el-button>
      </el-tooltip>
      <br><br>
        <br>
      <el-button type="submit" size="mini" @click="clearAll1();clearAll2()">再次上传(reset)</el-button>
      <el-button type="submit" size="mini" @click="insertUsersData">确认</el-button>
    </el-main>
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
        excelUpload(file) {
            let fn=file.name
            console.log(file.file)
            console.log("excelUpload")
            const formData = new FormData()
            formData.append("file",file.file)
            //https://jsonplaceholder.typicode.com/posts/
            this.$axios({
                method:'post',
                data:formData,
                url:this.$httpUrl+'/import/xml',
                headers:{'Content-Type': 'multipart/form-data'}
            }).then(function (resp){
                console.log("1111111111111");
            })
        },
        download1() {
/*            const config = {
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
            })*/
          let a = document.createElement('a');
          //跳转到github上的页面，再自行下载
          //a.href = 'https://github.com/staticpublic/SE/blob/xx/data';
          a.href = 'http://localhost:8080/static/example.json';
          //路径中'/'为根目录，即index.html所在的目录
          a.download = "指标数据模板";
          console.log(a.href);
          a.click();
        },
        download2() {
            let a = document.createElement('a');
            //跳转到github上的页面，再自行下载
            //a.href = 'https://github.com/staticpublic/SE/blob/xx/data';
            a.href = 'http://localhost:8080/static/index.xlsx';
            //路径中'/'为根目录，即index.html所在的目录
            a.download = "指标数据模板";
            console.log(a.href);
            a.click();
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
      ,
      clearAll1(){
        this.$refs.clearAll1.clearFiles();
        //this.$router.push('/ImportExcel')
      },
      clearAll2(){
        this.$refs.clearAll2.clearFiles();
        //this.$router.push('/ImportExcel')
      },
      insertUsersData(){
        //this.$router.push("/keepExcel")
        this.$axios({
          method:'get',
          url:'http://localhost:8877/import/insertUsersData'
        })

      },
    },
}
</script>
<style scoped>

</style>
