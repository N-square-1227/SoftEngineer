<template>
  <div>
    <div id="container" style="width:100%; height: auto;display: flex">
      <div id="treeChart" style="height: 75vh;width: 70%;overflow-x: visible;overflow-y: auto">
        <!--        <div class="chart-title" @click="toggleDrawer">优化结果</div>-->
      </div>

      <!--      <el-drawer-->
      <!--          title="因子载荷矩阵"-->
      <!--          :visible.sync="tablevisible"-->
      <!--          :with-header="false">-->
      <div style="height:75vh;width: 30%;float: left;overflow-x: auto;overflow-y: auto">
        <div style="position: sticky; top: 0;">
          <p style="float: left;margin-left: 20px" >指标权重表</p>
          <el-tooltip
              placement="bottom"
              effect="light">
            <div slot="content">指标权重越大，其在整个体系中重要性越大<br></div>
            <i class="el-icon-info" style="float: left;margin-left: 5px"></i>
          </el-tooltip>
        </div>
        <div style="margin-top: 30px">
          <el-table
              :data="en"
              border
              style="width: 100%"
              :cell-style="{ fontSize: '10px',height: '20px'}"
              :header-cell-style="{ fontSize: '14px' }">
            <el-table-column
                fixed
                prop="idx_name"
                label="指标名称"
                show-overflow-tooltip>
            </el-table-column>
            <el-table-column label="指标权重" prop="weight">
            </el-table-column>
          </el-table>
        </div>
      </div>
      <!--      </el-drawer>-->
    </div>
  </div>
</template>

<script>
export default {
  name: "PCATree",
  data() {
    return {
      treeData:[],
      en: [],
      enData:[],
      // name:[],
      // weight:[],
      total: 0,
      // threshold: 0.0,
      colNames :[], // 表头
      // indicators: [],
      tablevisible: false,
    }
  },
  // computed: {
  //
  // },
  created() {
    this.getTreeData()
    this.getLoadMatrix();
  },
  mounted() {
    this.treeValue();
    this.showChart();
    this.resizefun = ()=>{
      this.$echarts.init(document.getElementById('treeChart')).resize();
      //多个 echarts 则在此处添加
    };
    window.addEventListener('resize',this.resizefun);
  },
  watch: {
    treeData: {
      handler: function () {
        this.showChart();
      },
      deep: true,
    },
  },
  methods: {
    treeValue() {
      if(sessionStorage.getItem('sample_result') != null) {
        // console.log(sessionStorage.getItem('sample_result'))
        return JSON.parse(sessionStorage.getItem('sample_result'))
      }
      else
        return {};  // return 一个空列表不知道行不行啊
    },
    // 这是鼠标悬停的时候展示的节点信息
    formatterHover(params) {
      var id = params.data.id;
      var name = params.data.name;
      if (id === 1) {
        return '<span style="position: relative;top: -10px;padding: 0 5px;">ID：' + params.data.id + '</span>' + '<br>'
            + '<span style="padding-left:5px;height:30px;line-height:30px;display: inline-block;">名称：' + params.data.name + '</span>' + '<br>'
            + '<span style="padding-left:5px;height:30px;line-height:30px;display: inline-block;">指标值：' + this.treeValue()[params.data.id] + '</span>';
      }
      else {
        return '<span style="position: relative;top: -10px;padding: 0 5px;">ID：' + params.data.id + '</span>' + '<br>'
            + '<span style="padding-left:5px;height:30px;line-height:30px;display: inline-block;">名称：' + params.data.name + '</span>' + '<br>'
            + '<span style="padding-left:5px;height:30px;line-height:30px;display: inline-block;">类型：' + (params.data.type === 0 ? "定量负向指标" : "定量正向指标")+ '</span>' + '<br>'
            + '<span style="padding-left:5px;height:30px;line-height:30px;display: inline-block;">权重：' + params.data.weight + '</span>' + '<br>'
            + '<span style="padding-left:5px;height:30px;line-height:30px;display: inline-block;">父节点ID：' + params.data.parentID + '</span>' + '<br>'
            + '<span style="padding-left:5px;height:30px;line-height:30px;display: inline-block;">指标值：' + this.treeValue()[params.data.id] + '</span>';
        // + '<span style="padding-left:5px;height:30px;line-height:30px;display: inline-block;">最后登录时间：' + params.data.lastLoginTime + '</span>';
      }
    },

    getTreeData(){
      this.treeData = JSON.parse(sessionStorage.getItem('TreeData'))
    },
    // when_overflow(row, column) {
    //   const value = row[column.property];
    //   if (!isNaN(value)) {
    //     return `<span title="${formattedValue}">${formattedValue}</span>`;
    //   }
    //   return '';
    // },
    // toggleDrawer() {
    //   this.tablevisible = true;
    // },
    // 这是控制单元格内颜色的代码
    formatCell(value) {
      if (Math.abs(value) > this.threshold) {
        return 'color: red;';
      } else {
        return '';
      }
    },
    getLoadMatrix() {
      // this.$axios.get(this.$httpUrl + '/indexSymNode/getTreeData').then(res => res.data).then(res => {
      //   // console.log(res)
      //   if (res.code === 200) {
      //     this.loadmatrix = res.data.loadmatrix;
      //   } else {
      //     this.$message.error('失败！');
      //   }
      // });
      this.enData = JSON.parse(sessionStorage.getItem('entropy'))
      //this.en = []
      // const column = {label: `权重`, prop: `w`};
      // this.colNames.push(column);

      console.log("halo");// 有
      console.log(this.enData);
      /* 给原本列表类型的loadmatrix加key*/
      // console.log(this.raw_loadmatrix)
      let indexNum = this.enData.length;
      console.log(indexNum);

      for (let i = 0; i < indexNum; i ++){
        const data = this.enData[i];
        for(const key in data){
          console.log("key名称是："+key+",key的值是："+data[key]);
          const sample = {};
          sample[`idx_name`] = key;
          sample[`weight`] = data[key].toFixed(4);
          this.en.push(sample)
        }
        //console.log(i);// ok
      }
      this.total = indexNum;
    },
    showChart() {
      console.log("eChart")
      // 基于准备好的dom，初始化echarts实例
      var myChart = this.$echarts.init(document.getElementById('treeChart'));

      // 指定图表的配置项和数据
      var option = {
        title: {
          text: '优化结果',
        },
        tooltip: {
          trigger: 'item',
          triggerOn: 'mousemove',
          formatter:this.formatterHover,   //修改鼠标悬停显示的内容
        },
        series: [
          {
            type: 'tree',
            //orient:'TB',
            data: this.treeData,

            top: '1%',
            left: '10%',
            bottom: '1%',
            right: '32%',

            symbolSize: 7,

            label: {
              position: 'left',
              verticalAlign: 'middle',
              align: 'right',
              fontSize: 13,
            },

            leaves: {
              label: {
                position: 'right',
                verticalAlign: 'middle',
                align: 'left'
              }
            },

            expandAndCollapse: true,
            animationDuration: 550,
            animationDurationUpdate: 750,
          }
        ]
      };
      myChart.setOption(option);
    },
  }
}
</script>

<style>
/*修改表格的滚动条*/
#treeChart::-webkit-scrollbar {
  width: 10px;
  height: 10px;
}
/*滚动条的滑块*/
#treeChart::-webkit-scrollbar-thumb {
  background-color: #a1a3a9;
  border-radius: 3px;
}
</style>