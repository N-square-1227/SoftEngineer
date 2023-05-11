<template>
  <div id="app">
    <Main/>
  </div>
</template>

<script>

import Login from "./components/Login";
import Main from "./components/Main";
import A from "./components/A";
export default {
  name: 'App',
  components: {A, Main, Login}
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
