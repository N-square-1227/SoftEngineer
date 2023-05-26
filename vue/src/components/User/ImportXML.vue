<template>
    <div>
        <el-main style="text-align: center">
            <a size="mini" className="el-upload__tip">请先下载模板文件，再按照规定格式上传</a>
            <br><br>
            <el-button type="primary" plain size="medium" @click="download1" style="text-align: center">节点数据模板下载
            </el-button>
            <el-button type="primary" plain size="medium" @click="download2" style="text-align: center">指标数据模板下载
            </el-button>
            <br><br>
            <!--    <a  size="mini" class="el-upload__tip">请先下载模板文件，再按照规定格式上传</a>
                <br><br>-->
            <!--   上传文件 -->
            <el-upload
                    accept=".xml"
                    className="upload-demo"
                    action="111"
                    ref="nodeFileList"
                    :http-request="excelUpload1"
                    :on-change="nodehandleChange"
                    :file-list="nodefileList"
                    style="text-align: center"
            >
                <el-button size="medium" type="primary">选择节点数据文件</el-button>
                <br><br>
            </el-upload>
            <el-tooltip className="item" effect="dark" content="节点数据仅支持XML导入" placement="top-start">
                <el-button type="submit" size="mini" @click="jumpBehind">上传</el-button>
            </el-tooltip>
            <br><br>
            <el-upload
                    accept=".xlsx,.xls"
                    className="upload-demo"
                    ref="dataFileList"
                    action="localhost:8877/import/excel"
                    :http-request="excelUpload2"
                    :on-change="datahandleChange"
                    :file-list="datafileList"
                    style="text-align: center"
            >
                <el-button size="medium" type="primary">选择指标数据文件</el-button>
                <br><br>
            </el-upload>
            <el-tooltip className="item" effect="dark" content="指标数据仅支持excel导入" placement="top-start">
                <el-button type="submit" size="mini" @click="jumpBehind">上传</el-button>
            </el-tooltip>
            <br><br>
            <el-button type="submit" size="small" @click="clearAll1();clearAll2()">重置</el-button>
            <el-button type="submit" size="small" @click="insertUsersData">确认</el-button>
            <br><br>
            <!--    </el-form-item>-->
            <!--  </el-form>-->

        </el-main>
    </div>
</template>

<script>
export default {

    data() {
        return {
            user: JSON.parse(sessionStorage.getItem('CurUser')),
            treeData: [],
            nodefileList:[],
            datafileList:[],
        }
    },
    methods: {
        nodehandleChange(file, fileList) {
            this.nodefileList = fileList.slice(-1);
        },
        datahandleChange(file, fileList) {
          this.datafileList = fileList.slice(-1);
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
                url: this.$httpUrl+'/import/indexSym.xlsx',
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
                link.setAttribute('download', 'indexsymOrigin.xlsx');
                document.body.appendChild(link);
                link.click();
            })
        },
        jumpBehind() {
          if(this.$refs.nodeFileList.uploadFiles.length === 0)
            this.$message.warning("请选择指标信息文件！")
          else if(this.$refs.dataFileList.uploadFiles.length === 0)
            this.$message.warning("请选择数据文件！")
          else {
            //this.$router.push("/keepExcel")
            this.$axios.get(this.$httpUrl + '/import/keepExcel/' + this.user.userName).then(res => res.data).then(res => {
              console.log(res)
              if (res.code === 200) {
                this.$message({
                  message: '上传成功！',
                  type: 'success'
                });
              } else
                this.$message.error('上传失败！指标信息未上传或数据与指标不对应！');
            })
          }
        },
        clearAll1() {
            this.$refs.nodeFileList.clearFiles();
            //this.$router.push('/ImportExcel')
        },
        clearAll2() {
            this.$refs.dataFileList.clearFiles();
            //this.$router.push('/ImportExcel')
        },
        insertUsersData(){
            this.$axios.get(this.$httpUrl+'/import/insertUsersData/').then(res=>res.data).then(res=>{
                console.log(res)
                if (res.code==200) {
                    this.$axios.get(this.$httpUrl+'/import/getOrigTreeData/').then(res=>res.data).then(res=>{
                        console.log(res)
                        if (res.code==200) {
                            for(let i=0;i<res.data.length;i++){
                                this.treeData.push(res.data[i])
                                console.log(this.treeData)
                            }
                            sessionStorage.setItem("OriginalTreeData",JSON.stringify(this.treeData))
                            this.$message({
                                message: '成功！',
                                type: 'success'
                            });
                            this.$router.replace('/IndexSymManage');//跳转到可视化界面
                        }
                        else
                            this.$message.error('失败！');
                    })
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
