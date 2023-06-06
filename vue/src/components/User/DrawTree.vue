<template>
    <div id="container" style="width:100%; height: auto;display: flex">
        <div id="treeChart" style="height: 70vh;width: 70%"></div>
        <div id ='sslChart' style="height:70vh;width: 30%;float: right"></div>
    </div>
</template>

<script>
export default {
    name: "DrawTree",
    data() {
        return {
            treeData:[],
            ssl:[],
            xlist:[],
        }
    },
    // computed: {
    //
    // },
    created() {
        this.getData()
        // this.showChart()
    },
    mounted() {
        this.treeValue();
        this.showChart();
        this.showLine();
        this.resizefun = ()=>{
            this.$echarts.init(document.getElementById('treeChart')).resize();
            this.$echarts.init(document.getElementById('sslChart')).resize();
            //多个echarts则在此处添加
        };
        window.addEventListener('resize',this.resizefun);
    },
    watch: {
        treeData: {
            handler: function () {
                this.showChart();
                this.showLine();
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
        // function formatterHover(params) {
        // console.log(params);
        var id = params.data.id;
        var name = params.data.name;
        //图片地址截取，因为echarts修改图片的时候有一个------image://---前缀，前缀后面的才是图片真正的地址
        //var imgPathSrc = imgPath.split("image://")[1];
        // console.log('str',imgPathSrc);
        // this.treeValue();
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
        getData(){
            this.treeData = JSON.parse(sessionStorage.getItem('TreeData'))
            this.ssl = JSON.parse(sessionStorage.getItem('SSL'))
            for(var i=1;i<=this.ssl.length;i++)
              this.xlist.push(i);
        },
      // 展示树形图
        showChart() {
            console.log("eChart")
            // 基于准备好的dom，初始化echarts实例
            var myChart = this.$echarts.init(document.getElementById('treeChart'));

            // 指定图表的配置项和数据
            var option = {
                title: {
                    text: '优化结果'
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
      //展示折线图
      showLine(){
        //  重要：挂载节点
        let myEcharts = this.$echarts.init(document.getElementById('sslChart'));

        let options = {
          // 标题
          title: {
            text: '聚值k与轮廓系数关系图',
          },
          // 图例 注意： 需要与series里面的name的值向对应 否则不会出现
          // legend: {
          //   icon: 'rect',
          //   itemHeight: 1,
          //   data: ['日保费', '日平均保费'],
          // },
          // 提示框组件
          tooltip: {
            trigger: 'axis',
            textStyle: {
              // 设置文本相关属性
              color: 'white',
              fontSize: 12,
            },
            backgroundColor: 'rgba(50,50,50,0.7)', // 背景颜色
            // formatter: (params) => {
            //   let html = `<div>时间：${params[0].name}</div>` + `<div>${params[0].marker}日保费:${params[0].data}万元</div>` + `<div>${params[1].marker}日保费:${params[1].data}万元</div>`;
            //   return html;
            // },
          },

          // x轴
          xAxis: [
            {
              type: 'category',
              name:'k',
              data: this.xlist,
            },
          ],

          // y轴
          yAxis: {
            type: 'value',
            name: 'SSE',
            nameTextStyle: {
              fontSize: 12,
              color: '#111',
            },
            // 轴线设置
            axisLine: {
              show: true, // 是否显示刻度标签。
            },
            // 刻度显示
            axisTick: {
              show: true,
            },
            // 坐标轴在 grid 区域中的分隔线。
            splitLine: {
              show: true,
              lineStyle: {
                width: 1,
                type: 'dashed', //虚线
              },
            },
          },
          // 这里面的data是
          series: [
            // 第一个折线
            {
              id: 'current',
              // name: '日保费',
              type: 'line',
              data: this.ssl,
              // symbol: 'none', //折线图不显示转折点
              // 折线的相关样式
              lineStyle: {
                opacity: '0.5',
                smooth: true,
                // color: black,
                width: 1,
              },
              // itemStyle: { normal: { color: black } },
            },
          ],
        };
        myEcharts.setOption(options);
      }
    }
}
</script>

<style>

</style>

