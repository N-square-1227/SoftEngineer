<template>
  <div style="margin-right:0">
    <div style="margin-bottom: 5px;overflow: hidden">
      <div style="text-align: left;float: left">
        <el-input v-model="nodeName" placeholder="请输入指标体系名称" suffix-icon="el-icon-search" style="width: 200px;"
                  @keyup.enter.native="getAllSyms"></el-input>
        <el-button style="margin-left: 10px;" @click="search">查询</el-button>
        <el-button type="primary" plain style="margin-right: 10px;" @click="resetParam">重置</el-button>
      </div>
      <div style="text-align: right;float: right">
        <el-tooltip
            v-if="status===0"
            class="item" effect="dark"
            content="查看已删除指标体系"
            placement="bottom-start" style="margin-right: 10px">
          <el-button type="danger" icon="el-icon-delete" @click="getDelSyms" circle plain></el-button>
        </el-tooltip>
        <el-tooltip
            v-if="status===1"
            class="item" effect="dark" content="操作当前所有指标体系" placement="bottom-start" style="margin-right: 10px">
          <el-button type="primary" icon="el-icon-edit" @click="getAllSyms" circle plain></el-button>
        </el-tooltip>
        <el-select v-if="status===0"
            v-model="value" placeholder="选择导入指标体系数据方式" @change="getValue" style="width: 250px;margin-right: 10px">
          <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
          </el-option>
        </el-select>
        <el-tooltip v-if="status===0"
            class="item" effect="dark" content="指标数据仅支持excel导入">
          <el-button @click="sure" type="success">导入新数据</el-button>
        </el-tooltip>
      </div>
    </div>
    <div style="width: 100%">
      <el-table :data="symList"
                :header-cell-style="{ background:'#f2f5fc',color:'#555555'}">
        <el-table-column prop="id" label="ID" align="center">
        </el-table-column>
        <el-table-column prop="indexSymDTName" label="指标体系" align="center">
        </el-table-column>
        <el-table-column prop="time" :label=this.label align="center">
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button v-if="status===0"
                slot="reference" size="small" type="primary"
                style="margin-right:10px"
                @click="detail(scope.row)">详细信息
            </el-button>

            <el-popover v-if="status===0"
                placement="bottom"
                width="160"
                trigger="click">
              <el-select placeholder="请选择" v-model="algorithmValue" size="mini">
                <el-option
                    v-for="item in algorithmOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                    style="font-size: 10px">
                </el-option>
              </el-select>
              <el-button type="primary"
                         @click="chooseAlgorithm(scope.row)"
                         size="mini"
                         style="margin-top: 5px;float: right">确认</el-button>
              <template #reference>
                <el-button type="success" size="small">优化</el-button>
              </template>
            </el-popover>

            <el-popconfirm v-if="status===0"
                title="确定删除吗？"
                @confirm="del(scope.row)"
            >
              <el-button slot="reference" size="small" type="danger" style="margin-left: 10px">删除</el-button>
            </el-popconfirm>

            <el-button
                v-if="status===1"
                slot="reference" size="small" type="primary"
                style="margin-right:10px"
                @click="reduction(scope.row)">还原
            </el-button>

            <el-button
                v-if="status===1"
                slot="reference" size="small" type="danger"
                style="margin-left: 10px"
                @click="delforeverBox(scope.row)">永久删除
            </el-button>
          </template>

        </el-table-column>
      </el-table>
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[5, 10]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
let values;
let name  // 初始的指标体系数据名
let func
export default {
  name: "importFiles",

  data() {
    return {
      options: [{
        value: 'excel',
        label: 'excel'
      }, {
        value: 'xml',
        label: 'xml'
      }, {
        value: 'json',
        label: 'json'
      }],
      value: '',
      currentPage : 1,
      pageSize : 5,
      total:0,
      user:JSON.parse(sessionStorage.getItem('CurUser')),
      treeData: [],
      symList:[],
      sse: [],
      en: [],
      nodeName:"",
      algorithmOptions: [{
        value: 'kmeans',
        label: 'K-means聚类'
      }, {
        value: 'entropy',
        label: '熵权法调整指标权重'
      }, {
        value: 'pca',
        label: '主成分分析法'
      }],
      algorithmValue: '',
      label: '上传时间',  // 显示上传时间和删除时间默认是上传时间
      status: 0,  // 0表示现在是上传列表，1表示查看已删除列表（控制操作列的按钮显示）
    }
  },
  beforeMount() {
    this.getAllSyms()
    sessionStorage.removeItem("sample_result")
  },
  methods:{
    getValue(val){
      values=val;
      console.log(values)
    },
    sure(){
        if(values=="excel"){
            this.$router.push({name:'ImportExcel',params: {values}})
        }
        else if(values=="xml"){
            this.$router.push({name:'ImportXML',params: {values}})
        }
        else if(values=="json"){
            this.$router.push({name:'ImportJson',params: {values}})
        } else{
          this.$message.warning("请选择数据导入格式！")
        }
    },
    search() {
      if(this.status === 0)
        this.getAllSyms()
      else
        this.getDelSyms()
    },
    getAllSyms(){
      this.label = '上传时间';
      this.status = 0;
      console.log("staus:" + this.status)
      this.$axios.post(this.$httpUrl+'/import/indexSymListPage',{
        pageSize:this.pageSize,
        pageNum:this.currentPage,
        param:{
          table_name:this.user.userName+"_data",
          queryName:this.nodeName,
        }
      }).then(res=>res.data).then(res=>{
        if (res.code==200) {
          this.total = res.total;
          for(let i = 0; i < this.total; i ++)
            res.data[i].indexSymDTName = res.data[i].indexSymDTName.substring(this.user.userName.length + 1)
          // console.log(res.data)
          this.symList = res.data
        }
        else
          this.$message.error('数据加载失败！');
      })
    },
    /* 表结构是一样的，所以通用，只需要改个参数 */
    getDelSyms() {
      this.label = '删除时间';
      this.status = 1;
      console.log("staus:" + this.status)
      this.$axios.post(this.$httpUrl+'/import/indexSymListPage',{
        pageSize:this.pageSize,
        pageNum:this.currentPage,
        param:{
          table_name:this.user.userName+"_deleted",
          queryName:this.nodeName,
        }
      }).then(res=>res.data).then(res=>{
        if (res.code==200) {
          this.total = res.total;
          for(let i = 0; i < this.total; i ++)
            res.data[i].indexSymDTName = res.data[i].indexSymDTName.substring(this.user.userName.length + 1)
          // console.log(res.data)
          this.symList = res.data
        }
        else
          this.$message.error('数据加载失败！');
      })
    },
    resetParam(){
      this.nodeName=''
      if(this.status === 0)
        this.getAllSyms()
      else
        this.getDelSyms()
    },
    detail(row){
      this.$axios.get(this.$httpUrl + '/indexSymNode/getOriginalTreeData?tableName='+this.user.userName +"_"+ row.indexSymDTName).then(res => res.data).then(res => {
        console.log(res)
        if (res.code == 200) {
          this.treeData= res.data
          sessionStorage.setItem("OriginalTreeData",JSON.stringify(this.treeData))
          sessionStorage.setItem("IndexName",JSON.stringify(this.user.userName +"_"+row.indexSymDTName))
          this.$message({
            message: '成功！',
            type: 'success'
          });
          this.$router.replace('/IndexSymManage');//跳转到可视化界面
        }
        else
          this.$message.error('失败！');
      })
    },

    del(row){
      /**1.0
       * @author wxy
       * 修改删除逻辑：点击确定后先问要不要保留优化结果
       * 1. 不保留：一起删除
       * 2. 保留：把优化结果的表名写到用户所属的指标体系表里
       */
      /**2.0
       * @author wxy
       * 但是优化结果的数据是不保存的，如果要保存优化结果
       * 可以实现，但是好麻烦，不想做
       * 所以改成单纯的假删除了，就是删除的表放在“已删除"里可以还原。
       */
      this.$axios.post(this.$httpUrl+'/user/delTable',{
        param:{
          tableName: this.user.userName + "_" + row.indexSymDTName,
          user: this.user.userName,
        }
      }).then(res=>res.data).then(res=>{
        console.log(res)
        if(res.code==200){
          this.$message({
            message: '删除成功！\n数据记录在20天后永久删除！',
            type: 'success'
          });
          this.getAllSyms()
        }else{
          this.$message({
            message: '删除失败！',
            type: 'error'
          });
        }
      })
    },

    reduction(row) {
      // 就是把user_deleted表里的数据移到user_data里
      this.$axios.post(this.$httpUrl+'/user/reduction',{
        param:{
          tableName: this.user.userName + "_" + row.indexSymDTName,
          user: this.user.userName,
        }
      }).then(res=>res.data).then(res=>{
        console.log(res)
        if(res.code==200){
          this.$message({
            message: '已加入到指标体系列表!',
            type: 'success'
          });
          this.getAllSyms()
        }else{
          this.$message({
            message: '还原失败！',
            type: 'error'
          });
        }
      })
    },

    delforeverBox(row) {
      this.$confirm('注意!永久删除后指标体系和优化结果不可恢复!', '永久删除', {
        distinguishCancelAndClose: true,
        confirmButtonText: '永久删除',
        cancelButtonText: '放弃删除'
      })
        .then(() => {
          this.delforever(row);
        })
        .catch(action => {
          this.$message({
            type: 'info',
            message: '指标体系信息将在到期后自动删除'
          })
        });
    },

    delforever(row){

      this.$axios.post(this.$httpUrl+'/user/delForever',{
        param:{
          tableName: this.user.userName + "_" + row.indexSymDTName,
          user: this.user.userName,
        }
      }).then(res=>res.data).then(res=>{
        console.log(res)
        if(res.code==200){
          this.$message({
            message: '删除成功！',
            type: 'success'
          });
          this.getAllSyms()
        }else{
          this.$message({
            message: '删除失败！',
            type: 'error'
          });
        }
      })
    },

    setSession(tableName,alg){
      func=alg
      name=tableName
      sessionStorage.setItem("name", JSON.stringify(name))
      sessionStorage.setItem("func", JSON.stringify(func))
    },
    chooseAlgorithm(row) {
      if(this.algorithmValue === 'kmeans')
        this.KMeans(row);
      else if (this.algorithmValue === 'entropy')
        this.entropy(row);
      else if (this.algorithmValue === 'pca')
        this.pca(row);
      else {
        this.$message.warning("请选择优化算法！")
      }
    },
    KMeans(row){
      this.setSession(this.user.userName + "_" + row.indexSymDTName,"kmeans")
      // this.loadSampleData()
      this.$axios.get(this.$httpUrl + '/indexSymNode/getTreeData?tableName='
          + this.user.userName + "_" + row.indexSymDTName
          + "&func=kmeans").then(res => res.data).then(res => {
        // console.log(res)
        if (res.code == 200) {
          this.treeData= res.data.treeData
          this.sse = res.data.SSE
          /* 优化结果 */
          sessionStorage.setItem("TreeData", JSON.stringify(this.treeData))
          sessionStorage.setItem("SSE",JSON.stringify(this.sse))
          this.$message({
            message: '优化成功！',
            type: 'success'
          });
          this.$router.replace('/KmeansResultFrame');
        } else
          this.$message.error('优化失败！');
      })
    },
    entropy(row){
      this.setSession(this.user.userName + "_" + row.indexSymDTName,"entropy")
      // this.loadSampleData()
      this.$axios.get(this.$httpUrl + '/indexSymNode/getTreeData?tableName='
          + this.user.userName + "_" + row.indexSymDTName
          + "&func=entropy").then(res => res.data).then(res => {
        // console.log(res)
        if (res.code == 200) {
          this.treeData= res.data.treeData
          this.en=res.data.en
          /* 优化结果 */
          sessionStorage.setItem("TreeData", JSON.stringify(this.treeData))
          sessionStorage.setItem("entropy", JSON.stringify(this.en))
          this.$message({
            message: '优化成功！',
            type: 'success'
          });
          this.$router.replace('/OptimizeResultFrame');
        } else
          this.$message.error('优化失败！');
      })
    },

    pca(row){
      this.setSession(this.user.userName + "_" + row.indexSymDTName,"pca");
      this.$axios.get(this.$httpUrl + '/indexSymNode/getTreeData?tableName='
          + this.user.userName + "_" + row.indexSymDTName
          + "&func=pca").then(res => res.data).then(res => {
        console.log(res)
        if (res.code == 200) {
          this.treeData= res.data.treeData
          /* 优化结果 */
          sessionStorage.setItem("TreeData", JSON.stringify(this.treeData))
          sessionStorage.setItem("loadmatrix",JSON.stringify(res.data.loadmatrix))
          // console.log(res.data.loadmatrix)
          sessionStorage.setItem("threshold", JSON.stringify(res.data.threshold))
          sessionStorage.setItem("indicators", JSON.stringify(res.data.indicators))
          // console.log(res.data.indicators)
          this.$message({
            message: '优化成功！',
            type: 'success'
          });
          this.$router.replace('/PCAResultFrame');
        } else
          this.$message.error('优化失败！');
      })
      // this.loadSampleData()
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.currentPage=1
      this.pageSize=val
      if(this.status === 0)
        this.getAllSyms()
      else
        this.getDelSyms()
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.currentPage=val
      // this.getAllSyms()
      if(this.status === 0)
        this.getAllSyms()
      else
        this.getDelSyms()
    },
  }
}


</script>

<style scoped>

</style>
