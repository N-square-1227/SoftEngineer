<template>
  <div>
    B组件
    {{test}}
  </div>
</template>

<script>
export default {
  name: "B",
  /*接受A传来的属性*/
  props:["test"]
}
</script>

<style scoped>

</style>
