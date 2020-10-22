<template>
  <Toolbar/>
  <router-view></router-view>
</template>

<script>
import Toolbar from '@/components/Toolbar.vue'
export default {
  name: 'App',
  components: {
    Toolbar
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}

@import'~bootstrap/dist/css/bootstrap.css';
</style>
