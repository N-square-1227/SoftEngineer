<template>
  <div>
    <h1>指标体系优化系统</h1>
    <el-button @click="test1">弹窗</el-button><br><br>
<!--    双向的数据绑定，number或者input框变化，另一端也会变化-->
    <el-input v-model="number" style="width: 100px"></el-input>
    <div v-show="false">
      script
    </div>
<!--    <div v-for=" u in users" key="u.id">
      {{u.id}}{{u.name}}
    </div>-->
  </div>

</template>

<script>
import axios from "axios";

export default {
  name: "Main",
  data(){
    return {
      number:"xx",
      u:[],
      users:[{
        "id":1,
        "name":"x1",
      },{
        "id":2,
        "name":"x2",
      },{
        "id":3,
        "name":"x3",
      }]

    }
  },
  methods:{
    test1(){
      alert(this.number)
    },
    test2(){
      this.$axios(
        'http://localhost:8877/users/test',
      ).then(res=>{
        /*console.log(res.data);*/
        this.u=res.data;
        console.log(this.u);
      })

    }
  },
 beforeMount() {
    this.test2();
 }

}
</script>

<style scoped>

</style>
