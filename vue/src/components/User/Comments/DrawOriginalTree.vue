<template>
    <div style="width:100%; height: auto">
        <div id="treeChart" :style="{ height: treeHeight + 'px',overflowX: 'auto',overflowY: 'auto', width: '100%',}"></div>
    </div>
</template>

<script>
import {eventBus} from "@/main";

export default {
    name: "DrawOriginalTree",
    data() {
        return {
            treeData:[],
            leaf_num: sessionStorage.getItem("colNum"),
        }
    },
    computed: {
      treeHeight() {
        console.log(this.leaf_num)
        if(this.leaf_num == null) return 690
        return this.leaf_num * 30;
      }
    },
    created() {
        this.getTreeData();
        eventBus.$on('updateTreeValue', (treeValue) => {
          this.showChart()
        });
        // eventBus.$on('colNum', (colNum) => {
        //   this.leaf_num = sessionStorage.getItem("colNum")
        //   console.log(this.treeHeight)
        //   this.$echarts.init(document.getElementById('treeChart')).resize();
        // });
        // console.log(this.treeHeight)
    },
    mounted() {
        this.treeValue();
        this.showChart();
        this.resizefun = ()=>{
          // console.log("这里？")
          // this.leaf_num = sessionStorage.getItem("colNum")
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
        getTreeData(){
            this.treeData = JSON.parse(sessionStorage.getItem('OriginalTreeData'))
        },
        treeValue() {
          if(sessionStorage.getItem('sample_result') != null) {
            // console.log(sessionStorage.getItem('sample_result'))
            return JSON.parse(sessionStorage.getItem('sample_result'))
          }
          else
            return {};  // return 一个空列表不知道行不行啊
        },
        /**
         * 鼠标悬停时显示详情
        */
        formatterHover(params) {
          // console.log(params);
          var id = params.data.id;
          var name = params.data.name;
          if (id === 1) {
            return '<span style="position: relative;top: -10px;padding: 0 5px;">ID：' + params.data.id + '</span>' + '<br>'
                + '<span style="padding-left:5px;height:30px;line-height:30px;display: inline-block;">名称：' + params.data.name + '</span>' + '<br>';
          }
          else {
            return '<span style="position: relative;top: -10px;padding: 0 5px;">ID：' + params.data.id + '</span>' + '<br>'
                + '<span style="padding-left:5px;height:30px;line-height:30px;display: inline-block;">名称：' + params.data.name + '</span>' + '<br>'
                + '<span style="padding-left:5px;height:30px;line-height:30px;display: inline-block;">类型：' + params.data.type + '</span>' + '<br>'
                + '<span style="padding-left:5px;height:30px;line-height:30px;display: inline-block;">权重：' + params.data.weight + '</span>' + '<br>'
                + '<span style="padding-left:5px;height:30px;line-height:30px;display: inline-block;">父节点ID：' + params.data.parentID + '</span>' + '<br>';
            // + '<span style="padding-left:5px;height:30px;line-height:30px;display: inline-block;">最后登录时间：' + params.data.lastLoginTime + '</span>';
          }
        },
        updateTreeValue(value) {
          // console.log("监听有用？")
          this.treeValue = value; // 更新treeValue的值
          this.showChart();
        },
        showChart() {
            //console.log("sss")
            // 基于准备好的dom，初始化echarts实例
            var myChart = this.$echarts.init(document.getElementById('treeChart'));

            // 指定图表的配置项和数据
            var option = {
                title: {
                    text: '指标体系树'
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
                        right: '25%',

                        symbolSize: 5,

                        label: {
                            position: 'left',
                            verticalAlign: 'middle',
                            align: 'right',
                            fontSize: 12,
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
                        animationDurationUpdate: 750
                    }
                ]
            };
            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
        }
    }
}
</script>

<style>

</style>
