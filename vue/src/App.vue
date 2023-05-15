<template>

  <div id="app">
    <router-view/>
  </div>

</template>

<script>

//import Main from "./components/Main";
import afterLogin from "./view/afterLogin";
import Login from "./components/Login";
import ImportExcel from "./view/ImportExcel";
import ImportXML from "./view/ImportXML";
export default {
  name: 'App',
  components: {ImportExcel, afterLogin, Login, ImportXML}
}

</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>
