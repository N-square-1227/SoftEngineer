<template>
    <div>
        <div id="treeChart" :style="{width: '100%', height: '75vh'}"></div>
    </div>
</template>

<script>
export default {
    name: "OptimizePage",
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
            this.$axios.get(this.$httpUrl+'/indexSym/getTreeData?tableName=indexsym').then(res=>res.data).then(res=>{
                //console.log(res)
                if (res.code==200) {
                    for(let i=0;i<res.data.length;i++){
                        this.treeData.push(res.data[i])
                        console.log(this.treeData)
                    }
                    this.$message({
                        message: '优化成功！',
                        type: 'success'
                    });
                }
                else
                    this.$message.error('优化失败！');
            })
        },
        showChart() {
            //console.log("sss")
            // 基于准备好的dom，初始化echarts实例
            var myChart = this.$echarts.init(document.getElementById('treeChart'));

            // 指定图表的配置项和数据
            var option = {
                title: {
                    text: 'ECharts 入门示例'
                },
                tooltip: {
                    trigger: 'item',
                    triggerOn: 'mousemove'
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

                        symbolSize: 7,

                        label: {
                            position: 'left',
                            verticalAlign: 'middle',
                            align: 'right',
                            fontSize: 13
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

