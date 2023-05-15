<template>

  <el-main style="text-align: center">
    <a  size="mini" class="el-upload__tip">请先下载模板文件，再按照规定格式上传</a>
    <br><br>
    <el-button type="primary" plain size="mini" @click="download1" style="text-align: center">节点数据模板下载</el-button><br><br>
    <el-button type="primary" plain size="mini" @click="download2" style="text-align: center">指标数据模板下载</el-button><br><br>
    <!--    <a  size="mini" class="el-upload__tip">请先下载模板文件，再按照规定格式上传</a>
        <br><br>-->
    <!--   上传文件 -->
    <el-upload
      accept=".xlsx,.xls"
      class="upload-demo"
      action="111"
      ref="clearAll1"
      :http-request="excelUpload2"
      :on-change="handleChange"
      style="text-align: center"
    >

      <el-button size="mini" type="primary"  >选择结点数据文件</el-button>
    </el-upload>
    <br>

    <el-tooltip class="item" effect="dark" content="结点数据仅支持excel导入" placement="top-start">
      <el-button type="submit" size="mini" @click="jumpBehind" >上传</el-button>
    </el-tooltip>


    <!--    <el-form-item>-->
    <br><br>
    <el-upload
      accept=".xlsx,.xls"
      class="upload-demo"
      ref="clearAll2"
      action="localhost:8877/import/excel"
      :http-request="excelUpload1"
      :on-change="handleChange"
      style="text-align: center"
    >
      <el-button size="small" type="primary" >选择指标数据文件</el-button>
    </el-upload>
    <br>

    <el-tooltip class="item" effect="dark" content="指标数据仅支持excel导入" placement="top-start">
      <el-button type="submit" size="mini" @click="jumpBehind">上传</el-button>
    </el-tooltip>
    <br><br>
    <el-button type="submit" size="mini" @click="clearAll1();clearAll2()">再次上传(reset)</el-button>
<!--    <el-button type="submit" size="mini" @click="jumpBehind">上传</el-button>
    <el-button size="mini">取消</el-button>-->
    <!--    </el-form-item>-->
    <!--  </el-form>-->

  </el-main>
</template>

<script >
export default {

  data() {

    return{
      file: {
        name: 'f1.xlsx',
        url: 'http://localhost:8080/static'
      }
    }
  },
  methods: {
    handleChange(file, fileList) {
      this.fileList = fileList.slice(-3);
    },
    /*如果直接在el-upload写这一段url地址会出现跨域的问题，所以直接用表单*/
    excelUpload1(file) {
      let fn=file.name
      console.log(file.file)
      console.log("xxxxxxxx")
      const formData = new FormData()
      formData.append("file",file.file)
      this.$axios({
        method:'post',
        data:formData,
        url:'http://localhost:8877/import/excel/indexdata',
        headers:{'Content-Type': 'multipart/form-data'}
      }).then(function (resp){
        console.log("1111111111111");
      })

    },
    excelUpload2(file) {
      let fn=file.name
      console.log(file.file)
      console.log("xxxxxxxx")
      const formData = new FormData()
      formData.append("file",file.file)
      this.$axios({
        method:'post',
        data:formData,
        url:'http://localhost:8877/import/excel/indexSym',
        headers:{'Content-Type': 'multipart/form-data'}
      }).then(function (resp){
        console.log("1111111111111");
      })

    },
/*    upload(file) {
      console.log(file.file)
      let fd = new FormData()
      fd.append('file', this.mode)
      this.$axios.post('http://localhost:8877/import/excel', fd, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }).then(response => {
        console.log(response.data);
      })
    },*/
    download1() {
      let a = document.createElement('a');
      //跳转到github上的页面，再自行下载
      //a.href = 'https://github.com/staticpublic/SE/blob/xx/data';
      a.href = 'http://localhost:8080/static/indexSym.xlsx';
      //路径中'/'为根目录，即index.html所在的目录
      a.download = "节点数据模板";
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
      this.$axios({
        method:'get',
        url:'http://localhost:8877/import/keepExcel'
      })
    },
    clearAll1(){
      this.$refs.clearAll1.clearFiles();
      //this.$router.push('/ImportExcel')
    },
    clearAll2(){
      this.$refs.clearAll2.clearFiles();
      //this.$router.push('/ImportExcel')
    }
  },

}
</script>

<style scoped>

</style>
