<template>
  <div>
    <div id="container" style="width:100%; height: auto;display: flex">
      <div id="treeChart" style="height: 70vh;width: 70%;overflow-x: visible;overflow-y: auto">
<!--        <div class="chart-title" @click="toggleDrawer">优化结果</div>-->
      </div>

<!--      <el-drawer-->
<!--          title="因子载荷矩阵"-->
<!--          :visible.sync="tablevisible"-->
<!--          :with-header="false">-->
        <div id ='loadmatrix' style="height:70vh;width: 30%;float: left;overflow-x: auto;overflow-y: auto">
          <div style="position: sticky; top: 0;">
            <p style="float: left;margin-left: 20px" >因子载荷矩阵</p>
            <el-tooltip
                placement="bottom"
                effect="light">
              <div slot="content">矩阵的每一个元素是原始变量与公共因子的相关系数<br><br>若载荷值越大,表明该变量与该公因子关系密切</div>
              <i class="el-icon-info" style="float: left;margin-left: 5px"></i>
            </el-tooltip>
          </div>
          <div style="margin-top: 30px">
            <el-table
                :data="loadmatrix"
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
              <el-table-column v-for="column in colNames"
                               :key="column.prop"
                               :label="column.label"
                               :prop="column.prop">
                <template slot-scope="scope">
                  <div :style="formatCell(scope.row[column.prop])">
                    {{ scope.row[column.prop] }}
                  </div>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
<!--      </el-drawer>-->
    </div>
  </div>
</template>

<script>
import {eventBus} from "@/main";

export default {
  name: "PCATree",
  data() {
    return {
      treeData:[],
      loadmatrix: [],
      raw_loadmatrix:[],
      total: 0,
      threshold: 0.0,
      colNames :[], // 表头
      indicators: [],
      tablevisible: false,
    }
  },
  created() {
    this.getTreeData()
    this.getLoadMatrix();
    eventBus.$on('updateTreeValue', (treeValue) => {
      this.showChart()
    });
  },
  mounted() {
    this.treeValue();
    this.showChart();
    this.resizefun = ()=>{
      this.$echarts.init(document.getElementById('treeChart')).resize();
      //多个echarts则在此处添加
    };
    window.addEventListener('resize',this.resizefun);
  },
  beforeDestroy() {
    window.removeEventListener('resize',this.resizefun);
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
    formatterHover(params) {
      var id = params.data.id;
      var name = params.data.name;
      if (id === 1) {
        return '<span style="position: relative;top: -10px;padding: 0 5px;">ID：' + params.data.id + '</span>' + '<br>'
            + '<span style="padding-left:5px;height:30px;line-height:30px;display: inline-block;">名称：' + params.data.name + '</span>' + '<br>';
      }
      else {
        return '<span style="position: relative;top: -10px;padding: 0 5px;">ID：' + params.data.id + '</span>' + '<br>'
            + '<span style="padding-left:5px;height:30px;line-height:30px;display: inline-block;">名称：' + params.data.name + '</span>' + '<br>'
            + '<span style="padding-left:5px;height:30px;line-height:30px;display: inline-block;">类型：' + (params.data.type === 0 ? "定量负向指标" : "定量正向指标")+ '</span>' + '<br>'
            + '<span style="padding-left:5px;height:30px;line-height:30px;display: inline-block;">权重：' + params.data.weight + '</span>' + '<br>'
            + '<span style="padding-left:5px;height:30px;line-height:30px;display: inline-block;">父节点ID：' + params.data.parentID + '</span>' + '<br>';
        // + '<span style="padding-left:5px;height:30px;line-height:30px;display: inline-block;">最后登录时间：' + params.data.lastLoginTime + '</span>';
      }
    },

    getTreeData(){
      this.treeData = JSON.parse(sessionStorage.getItem('TreeData'))

    },
    updateTreeValue(value) {
      console.log("监听有用？")
      this.treeValue = value; // 更新treeValue的值
      this.showChart();
    },
    formatCell(value) {
      if (Math.abs(value) > this.threshold) {
        return 'color: red;';
      } else {
        return '';
      }
    },
    getLoadMatrix() {
      this.raw_loadmatrix = JSON.parse(sessionStorage.getItem("loadmatrix"))
      this.indicators = JSON.parse(sessionStorage.getItem("indicators"))
      this.threshold = JSON.parse(sessionStorage.getItem("threshold"))    // 这里可能类型转换错误，我不知道
      /* 动态生成表头 */
      let colNum = this.raw_loadmatrix[0].length;
      this.loadmatrix = []
      for (let i = 1; i <= colNum; i++) {
        const column = {label: `factor${i}`, prop: `factor${i}`};
        this.colNames.push(column);
      }
      console.log(this.colNames)
      /* 给原本列表类型的loadmatrix加key*/
      // console.log(this.raw_loadmatrix)
      let indexNum = this.raw_loadmatrix.length;
      for (let i = 0; i < indexNum; i ++){
        const data = this.raw_loadmatrix[i];
        // console.log(data)
        const sample = {};
        sample[`idx_name`] = this.indicators[i];
        for(let j = 1; j <= colNum; j ++)
          sample[`factor${j}`] = parseFloat(data[j - 1]).toFixed(4);
        this.loadmatrix.push(sample)
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
              formatter: function(params) {
                var originalLabel = params.name; // 获取原始的节点标签内容
                var newLabel = originalLabel + ' \n(' + parseFloat(this.treeValue()[params.data.id]).toFixed(4) + ')'; // 修改标签内容
                return newLabel;
              }.bind(this)
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