<template>
  <div>
    <el-table
        :data="pagedData"
        border
        style="width: 100%"
        :row-style="{ height: '20px' }">
      <el-table-column
          fixed
          prop="id"
          label="样本序号"
          width="80">
      </el-table-column>
      <el-table-column v-for="column in colNames"
                       :key="column.prop"
                       :label="column.label"
                       :prop="column.prop">
      </el-table-column>

      <el-table-column
          fixed="right"
          label="操作"
          width="100">
        <template slot-scope="scope">
          <el-button @click="handleClick(scope.row)" type="text" size="small">查看</el-button>
          <el-button type="text" size="small">编辑</el-button>
        </template>
      </el-table-column>

    </el-table>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[3, 5, 10, 20]"
        :page-size="pageSize"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
  </div>
</template>

<script>
export default {
  data() {
    return {
      colNames:[],
      data:[],
      sampleNum: 0,
      currentPage : 1,
      pageSize : 3,
      total: 1,
    }
  },
  computed:{
    pagedData() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.data.slice(start, end);
    },
  },
  created() {
    this.setTableData();
  },
  methods: {
    handleClick(row) {
      console.log(row);
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.currentPage=1
      this.pageSize=val
      this.setTableData()
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.currentPage=val
      this.setTableData()
    },
    setTableData() {
      /* 先动态生成表头 */
      const colNum = sessionStorage.getItem("colNum");
      const tableHeader = [];
      for (let i = 1; i <= colNum; i++) {
        const column = { label: `X${i}`, prop: `column${i}` };
        tableHeader.push(column);
      }
      this.colNames = tableHeader;

      // console.log(this.colNames)

      /* 然后填充数据 */
      const dataList = JSON.parse(sessionStorage.getItem("data"));
      this.sampleNum = sessionStorage.getItem("sampleNum");
      console.log(this.sampleNum)
      this.data = []
      for (let i = 0; i < this.sampleNum; i ++) {
        const data = dataList[i].data;
        const sample = {};
        // const number = {}
        sample['id'] =  i + 1;
        // sample.push({'id' : i + 1})
        // sample.push(number)
        for (let j = 1; j <= colNum; j ++) {
          // sample.push({[`column${j}`] : data[j - 1]})
          sample[`column${j}`] = data[j - 1];
          // sample.push(number)
        }
        // console.log(sample)
        this.data.push(sample);
      }
      // console.log(this.data)
      this.total = this.sampleNum - 0;
    },
  },

}
</script>