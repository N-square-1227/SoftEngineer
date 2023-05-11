<template>
    <afterLogin/>
</template>

<script>

import Login from "./components/Login";
//import Main from "./components/Main";
import A from "./components/A";
import AfterLogin from "./view/afterLogin";
import afterLogin from "./view/afterLogin";
import ImportExcel from "./view/ImportExcel";
export default {
  name: 'App',
  components: {ImportExcel, afterLogin, A, Login}
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
