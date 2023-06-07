<template>
  <div>
    <el-main style="text-align: center">
      <el-steps :active="active" align-center finish-status="success">
        <el-step title="Step 1" description="下载模板文件" />
        <el-step title="Step 2" description="上传结点数据文件" />
        <el-step title="Step 3" description="上传指标数据文件" />
      </el-steps>

      <div v-if="active==1">

        <div style="width: 60%;margin-left: 20%" class="app-container">

          <template>
            <el-form ref="ruleForm" label-width="100px" :inline="true" class="demo-form-inline">
              <br><br>
              <el-button type="primary" plain @click="download1" style="text-align: center">节点数据模板下载</el-button>
              <el-button type="primary" plain @click="download2" style="text-align: center;margin-left: 10px">指标数据模板下载</el-button>
            </el-form>
          </template>
        </div>
        <br><br>
        <el-button type="primary"  @click="next">下一步</el-button>
      </div>

      <div v-if="active === 2">
        <br><br><br>
        <el-upload
            accept=".json"
            class="upload-demo"
            action="111"
            ref="nodefileList_ref"
            :http-request="jsonUpload"
            :on-change="handleChangeNode"
            :file-list="nodefileList"
            style="text-align: center"
        >
          <el-button type="primary" plain>选择节点数据文件</el-button><br><br>
        </el-upload>
        <br>

        <el-button  type="primary" @click="jumpBehind();clearnodefileList()">下一步</el-button>
      </div>
      <div v-if="active === 3">

        <br><br>
        <el-upload
            accept=".xlsx,.xls"
            class="upload-demo"
            ref="datafileList_ref"
            action="localhost:8877/import/excel"
            :http-request="excelUpload2"
            :on-change="handleChangeData"
            :file-list="datafileList"
            style="text-align: center"
        >
          <el-button  type="primary" plain>选择指标数据文件</el-button>
        </el-upload>
        <br>
        <el-button  type="submit" @click="jumpBehind2();cleardatafileList();insertUsersData()">确定</el-button>
      </div>

    </el-main>
  </div>
</template>

<script >
export default {
  name:"ImportJson",
  data() {
    return{
      user:JSON.parse(sessionStorage.getItem('CurUser')),
      active:1,
      file: {
        name: 'example.json'
      },
      nodefileList: [],
      datafileList: [],
    }
  },
  methods: {
    next() {
      if (this.active++ > 2) this.active = 0;
    },
    handleChangeNode(file, fileList) {
      this.nodefileList = fileList.slice(-1);
    },
    handleChangeData(file, fileList) {
      this.datafileList = fileList.slice(-1);
    },
    /*如果直接在el-upload写这一段url地址会出现跨域的问题，所以直接用表单*/
    jsonUpload(file) {
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
        // console.log("1111111111111");
      })
    },
    excelUpload2(file) {
      let fn=file.name
      console.log(file.file)
      // console.log("xxxxxxxx")
      const formData = new FormData()
      formData.append("file",file.file)
      //https://jsonplaceholder.typicode.com/posts/
      this.$axios({
        method:'post',
        data:formData,
        url:'http://localhost:8877/import/excel/indexdata',
        headers:{'Content-Type': 'multipart/form-data'}
      }).then(function (resp){
        // console.log("1111111111111");
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
        link.setAttribute('download', 'index.xlsx');
        document.body.appendChild(link);
        link.click();
      })
    },
    //点击上传跳转后端
    jumpBehind(){
      //this.$router.push("/keepExcel")
      if(this.active === 2 && this.nodefileList.length === 0) {
        this.$message.error("请上传指标体系！")
      }else {
        this.$axios.get(this.$httpUrl + '/import/keepJson?userName=' + this.user.userName).then(res => res.data).then(res => {
          console.log(res)
          if (res.code == 200) {
            this.$message({
              message: '上传成功！',
              type: 'success'
            });
            this.next();
          } else
            this.$message.error('上传失败！');
        })
      }
    },
    jumpBehind2(){
      //this.$router.push("/keepExcel")
      // this.$axios({
      //     method:'get',
      //     url:'http://localhost:8877/import/keepExcel/'+this.user.userName
      // })
      if(this.active === 3 && this.datafileList.length === 0) {
        this.$message.error("请上传样本数据！")
      }
      else {
        this.$axios.get(this.$httpUrl + '/import/keepExcel/' + this.user.userName).then(res => res.data).then(res => {
          console.log(res)
          if (res.code == 200) {
            this.$message({
              message: '上传成功！',
              type: 'success'
            });
            this.next();
          } else
            this.$message.error('上传失败！');
        })
      }
    },
    clearnodefileList() {
      // setTimeout(()=>{
      this.$refs.nodefileList_ref.clearFiles();
      // },0);
    },
    cleardatafileList() {
      // setTimeout(()=>{
      this.$refs.datafileList_ref.clearFiles();
      // },0);
    },
    insertUsersData(){
      //this.$router.push("/keepExcel")
      this.$axios.get(this.$httpUrl+'/import/insertUsersData/').then(res=>res.data).then(res=>{
        console.log(res)
        if (res.code==200) {
          this.$message({
            message: '成功！',
            type: 'success'
          });
          this.$router.replace("/ImportFiles");
        }
        else
          this.$message.error('失败！');
      })

    }
  }
}
</script>
<style scoped>

</style>
