<template>
  <el-main style="text-align: center" >
    <el-select v-model="value" placeholder="选择导入指标体系数据方式" @change="getValue" style="text-align: center;width: 250px">
      <el-option
        v-for="item in options"
        :key="item.value"
        :label="item.label"
        :value="item.value">
      </el-option>
    </el-select>

    <el-tooltip class="item" effect="dark" content="指标数据仅支持excel导入" placement="top-start">
      <el-button @click="sure">确定</el-button>
    </el-tooltip>

  </el-main>
</template>

<script>
let values;
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
      value: ''
    }
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
      if(values=="xml"){
        this.$router.push({name:'ImportXML',params: {values}})
      }
    }
  }
}


</script>

<style scoped>

</style>
