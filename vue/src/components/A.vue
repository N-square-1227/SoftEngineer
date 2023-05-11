<template>
<!--  向B组件传参-->
  <B :test="name"></B>
</template>

<script>
import B from "./B";
export default {
  name: "A",
  components: {B},
  data(){
    return{
      name:"SoftEngineering"
    }

  }
}
</script>

<style scoped>

</style>
