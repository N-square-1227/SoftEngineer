<template>
  <el-row :gutter="20">
    <el-col :span="12" align="center" justify="center">
      <div class="grid-content bg-purple">
        <el-tag type="info">优化前各样本总指标计算值排序</el-tag>
        <el-table
            :data="originResult"
            style="width: 100%">
          <el-table-column
              prop="value"
              label="计算值"
              width="200"
              align="center">
          </el-table-column>
          <el-table-column
              prop="sampleId"
              label="样本编号"
              width="200"
              align="center">
          </el-table-column>
        </el-table>
      </div>
    </el-col>
    <el-col :span="12" align="center" justify="center">
      <div class="grid-content bg-purple">
        <el-tag type="success">优化后各样本总指标计算值排序</el-tag>
        <el-table
            :data="newResult"
            style="width: 100%">
          <el-table-column
              prop="value"
              label="计算值"
              width="200"
              align="center">
          </el-table-column>
          <el-table-column
              prop="sampleId"
              label="样本编号"
              width="200"
              align="center">
          </el-table-column>
        </el-table>
      </div>
    </el-col>
  </el-row>
</template>

<script>
export default {
  data() {
    return {
      origin_tableName: "",
      func_code: "",
      originResult: [],
      newResult:[],
    }
  },
  created() {
    this.origin_tableName = JSON.parse(sessionStorage.getItem("name"));
    this.func_code = JSON.parse(sessionStorage.getItem("func"));
    this.getResultData();
  },
  mounted() {
    // console.log(sessionStorage.getItem("newResult"))
  },
  methods: {
    getResultData() {
      this.$axios.post(this.$httpUrl + '/indexsym/caculateResult', {
        param:{
          dataName: this.origin_tableName + "_data", // 这是原始指标体系的表名,优化后的表名添加使用的函数，数据表名添加后缀
          indexName: this.origin_tableName,
          newindexName: this.origin_tableName + "_new_" + this.func_code,
        }
      }).then(res => res.data).then(res => {
        console.log(res.data)
        this.newResult = res.data;
      })
      this.$axios.post(this.$httpUrl + '/indexsym/caculateResult', {
        param:{
          dataName: this.origin_tableName + "_data", // 这是原始指标体系的表名,优化后的表名添加使用的函数，数据表名添加后缀
          indexName: this.origin_tableName,
          newindexName: this.origin_tableName,
        }
      }).then(res => res.data).then(res => {
        console.log(res.data)
        this.originResult = res.data;
      })
      // this.originResult = JSON.parse(sessionStorage.getItem('originResult'));
      // this.newResult = JSON.parse(sessionStorage.getItem('newResult'));
      // console.log(sessionStorage.getItem('originResult'));
      // console.log(this.newResult);
    },
  }
}
</script>