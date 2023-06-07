<template>
  <div>
    <el-main style="text-align: center">
      <el-steps :active="1" align-center :aria-current="current">
        <el-step title="Step 1" description="下载模板文件" />
        <el-step title="Step 2" description="上传结点数据文件" />
        <el-step title="Step 3" description="上传指标数据文件" />
      </el-steps>


      <div v-if="active==0">

        <div style="width: 60%;margin-left: 20%" class="app-container">

          <template>
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" :inline="true" class="demo-form-inline">
              <br><br>
              <el-button type="primary" plain size="mini" @click="download1" style="text-align: center">节点数据模板下载</el-button><br><br>
              <el-button type="primary" plain size="mini" @click="download2" style="text-align: center">指标数据模板下载</el-button><br><br>
            </el-form>
          </template>
        </div>

      </div>

      <div v-if="active==1">
        <br><br><br>
        <el-upload
            accept=".xml"
            className="upload-demo"
            action="111"
            ref="clearAll1"
            :http-request="excelUpload1"
            :on-change="handleChange"
            style="text-align: center"
        >
          <el-button size="mini" type="primary">选择指标体系文件</el-button>
          <br><br>
        </el-upload>
        <br>

        <el-button  type="primary" @click="jumpBehind();clearAll1();clearAll2()">上传</el-button>
      </div>


      <div v-if="active==2">

        <br><br>
        <el-upload
            accept=".xlsx,.xls"
            className="upload-demo"
            ref="clearAll2"
            action="localhost:8877/import/excel"
            :http-request="excelUpload2"
            :on-change="handleChange"
            style="text-align: center"
        >
          <el-button size="small" type="primary">选择指标数据文件</el-button>
          <br><br>
        </el-upload>
        <br>
        <br><br>
        <el-button  type="submit" @click="jumpBehind();insertUsersData()">上传</el-button>
      </div>

      <el-button type="success" style="margin-top: 10px" @click="next">下一步</el-button>

    </el-main>
  </div>
</template>

<script>
export default {

  data() {

    return {
      user: JSON.parse(sessionStorage.getItem('CurUser')),
      active:0,
    }
  },
  methods: {
    next() {
      this.active+=1;
      if (this.active > 2) this.active = 0;

    },
    handleChange(file, fileList) {
      this.fileList = fileList.slice(-3);
    },
    /*如果直接在el-upload写这一段url地址会出现跨域的问题，所以直接用表单*/
    excelUpload1(file) {
      let fn = file.name
      console.log(file.file)
      console.log("xxxxxxxx")
      const formData = new FormData()
      formData.append("file", file.file)
      //https://jsonplaceholder.typicode.com/posts/
      this.$axios({
        method: 'post',
        data: formData,
        url: 'http://localhost:8877/import/xml/indexSym',
        headers: {'Content-Type': 'multipart/form-data'}
      }).then(function (resp) {
        console.log("1111111111111");
      })

    },
    excelUpload2(file) {
      let fn = file.name
      console.log(file.file)
      console.log("xxxxxxxx")
      const formData = new FormData()
      formData.append("file", file.file)
      //https://jsonplaceholder.typicode.com/posts/
      this.$axios({
        method: 'post',
        data: formData,
        url: 'http://localhost:8877/import/excel/indexdata',
        headers: {'Content-Type': 'multipart/form-data'}
      }).then(function (resp) {
        console.log("1111111111111");
      })

    },
    download1() {
      const config = {
        method: 'get',
        url: this.$httpUrl+'/import/downloadXml',
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
        link.setAttribute('download', 'temp.xml');
        document.body.appendChild(link);
        link.click();
      })

    },
    download2() {
      const config = {
        method: 'get',
        url: this.$httpUrl+'/import/downloadExcel2',
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
        link.setAttribute('download', 'indexSym.xlsx');
        document.body.appendChild(link);
        link.click();
      })
    },
    jumpBehind() {
      //this.$router.push("/keepExcel")
      this.$axios.get(this.$httpUrl+'/import/keepExcel/'+this.user.userName).then(res=>res.data).then(res=>{
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
    },
    clearAll1() {
      this.$refs.clearAll1.clearFiles();
      //this.$router.push('/ImportExcel')
    },
    clearAll2() {
      this.$refs.clearAll2.clearFiles();
      //this.$router.push('/ImportExcel')
    },
    insertUsersData() {
      //this.$router.push("/keepExcel")
      this.$axios.get(this.$httpUrl+'/import/insertUsersData/').then(res=>res.data).then(res=>{
        console.log(res)
        if (res.code==200) {
          this.$message({
            message: '成功！',
            type: 'success',
          });
          this.$router.replace('/ImportFiles');
        }
        else
          this.$message.error('失败！');
      })

    },
  },


}


</script>

<style scoped>

</style>
