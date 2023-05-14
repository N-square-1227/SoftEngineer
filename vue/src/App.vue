<template>
    <afterLogin/>
</template>

<script>

import Login from "./components/Login";
//import Main from "./components/Main";
import afterLogin from "./view/afterLogin";
import ImportExcel from "./view/ImportExcel";
import ImportXML from "./view/ImportXML";
import ImportMysql from "./view/ImportMysql";
export default {
  name: 'App',
  components: {ImportExcel, afterLogin,  Login,ImportXML,ImportMysql}
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
