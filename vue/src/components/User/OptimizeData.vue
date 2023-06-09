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
                    <el-button @click="handleClick(scope.row)" type="success" size="small">计算</el-button>
                </template>
            </el-table-column>

        </el-table>
        <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="currentPage"
                :page-sizes="[5, 7, 10, 20]"
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
            pageSize : 5,
            total: 1,

            table_name: "",
            colNum: 0,
            raw_data:[],
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
        this.table_name = JSON.parse(sessionStorage.getItem("name")) + "_new_" + JSON.parse(sessionStorage.getItem("func"));
        // this.loadSampleData()
        // this.raw_data = JSON.parse(sessionStorage.getItem("data"));
        // this.colNum = JSON.parse(sessionStorage.getItem("colNum"));
        // this.sampleNum = JSON.parse(sessionStorage.getItem("sampleNum"))
        this.setTableData();
    },
    methods: {
        handleClick(row) {  // 计算并在树形结构中显示
            console.log(row);
            this.$axios.post(this.$httpUrl+'/indexsym/caculateSample',{
                param: {
                    table_name: this.table_name,
                    sample: row,
                }
            }).then(res=>res.data).then(res=> {
                if(res.code === 200) {
                    // console.log(res.data)
                    sessionStorage.setItem("sample_result", JSON.stringify(res.data));
                    // console.log(sessionStorage.getItem("sample_result"))
                    this.$message.success("计算完成！\n您可以在指标体系树型结构中查看！")
                }else{
                    this.$message.error('数据计算出错！');
                }
            })
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
        async setTableData() {
            try {
                await this.$axios.post(this.$httpUrl + '/indexsym/loadNewData', {
                    pageSize: this.pageSize,
                    pageNum: this.pageNum,
                    param: {
                        // basicTableName: name, // 这是原始指标体系的表名,优化后的表名添加使用的函数，数据表名添加后缀
                        // func: func,
                        basicTableName: JSON.parse(sessionStorage.getItem("name")),
                        func: JSON.parse(sessionStorage.getItem("func"))
                    }
                }).then(res => res.data).then(res => {
                    console.log(res);
                    if (res.code === 200) {
                        // sessionStorage.setItem("data", JSON.stringify(res.data.sampleData));
                        // // console.log(res.data.sampleData)
                        // sessionStorage.setItem("colNum", res.data.colNum);
                        // sessionStorage.setItem("sampleNum", res.data.sampleNum);
                        this.raw_data = res.data.sampleData;
                        this.colNum = res.data.colNum
                        this.sampleNum = res.data.sampleNum
                    } else {
                        this.$message.error('数据加载出错！');
                    }
                })
                // this.loadSampleData();

                // console.log(this.raw_data)
                /* 先动态生成表头 */
                const tableHeader = [];
                for (let i = 1; i <= this.colNum; i++) {
                    const column = {label: `X${i}`, prop: `column${i}`};
                    tableHeader.push(column);
                }
                this.colNames = tableHeader;

                console.log(this.colNum)

                /* 然后填充数据 */
                const dataList = this.raw_data;
                console.log(this.raw_data)
                // console.log(this.sampleNum)
                this.data = []
                for (let i = 0; i < this.sampleNum; i++) {
                    const data = dataList[i].data;
                    const sample = {};
                    // const number = {}
                    sample['id'] = i + 1;
                    // sample.push({'id' : i + 1})
                    // sample.push(number)
                    for (let j = 1; j <= this.colNum; j++) {
                        // sample.push({[`column${j}`] : data[j - 1]})
                        sample[`column${j}`] = data[j - 1];
                        // sample.push(number)
                    }
                    // console.log(sample)
                    this.data.push(sample);
                }
                // console.log(this.data)
                /* 把默认的string变成数字 */
                this.total = this.sampleNum;
            }catch (error) {
                // console.log(error)
                this.$message.error('数据加载出错！');
            }
        },

    },

}
</script>