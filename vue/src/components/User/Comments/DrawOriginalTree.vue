<template>
    <div style="width:100%; height: auto">
        <div id="treeChart" style="height: 70vh;width: 100%"></div>
    </div>
</template>

<script>
export default {
    name: "DrawOriginalTree",
    data() {
        return {
            treeData:[]
        }
    },
    created() {
        this.getTreeData()
    },
    mounted() {
        this.showChart();
        this.resizefun = ()=>{
            this.$echarts.init(document.getElementById('treeChart')).resize();
            //多个echarts则在此处添加
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
        getTreeData(){
            this.treeData = JSON.parse(sessionStorage.getItem('OriginalTreeData'))
        },
        showChart() {
            //console.log("sss")
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
                    formatter:formatterHover,   //修改鼠标悬停显示的内容
                },
                series: [
                    {
                        type: 'tree',
                        //orient:'TB',
                        data: this.treeData,

                        top: '1%',
                        left: '7%',
                        bottom: '1%',
                        right: '20%',

                        symbolSize: 5,

                        label: {
                            position: 'left',
                            verticalAlign: 'middle',
                            align: 'right',
                            fontSize: 14
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

            /**
             * 鼠标悬停时显示详情
             */
            function formatterHover(params) {
                // console.log(params);
                var id = params.data.id;
                var name = params.data.name;
                //图片地址截取，因为echarts修改图片的时候有一个------image://---前缀，前缀后面的才是图片真正的地址
                //var imgPathSrc = imgPath.split("image://")[1];
                // console.log('str',imgPathSrc);
                if (id === 1) {
                    return '<span style="position: relative;top: -10px;padding: 0 5px;">ID：' + params.data.id + '</span>' + '<br>'
                            + '<span style="padding-left:5px;height:30px;line-height:30px;display: inline-block;">名称：' + params.data.name + '</span>';
                }
                else {
                    return '<span style="position: relative;top: -10px;padding: 0 5px;">ID：' + params.data.id + '</span>' + '<br>'
                            + '<span style="padding-left:5px;height:30px;line-height:30px;display: inline-block;">名称：' + params.data.name + '</span>' + '<br>'
                            + '<span style="padding-left:5px;height:30px;line-height:30px;display: inline-block;">类型：' + params.data.type + '</span>' + '<br>'
                            + '<span style="padding-left:5px;height:30px;line-height:30px;display: inline-block;">权重：' + params.data.weight + '</span>' + '<br>'
                            + '<span style="padding-left:5px;height:30px;line-height:30px;display: inline-block;">父节点ID：' + params.data.parentID + '</span>' + '<br>';
                    // + '<span style="padding-left:5px;height:30px;line-height:30px;display: inline-block;">最后登录时间：' + params.data.lastLoginTime + '</span>';
                }
            }
            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
        }
    }
}
</script>

<style>

</style>
