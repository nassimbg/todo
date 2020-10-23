<template>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <a class="navbar-brand" href="#">TODO</a>
    <button
      class="navbar-toggler"
      type="button"
      data-toggle="collapse"
      data-target="#navbarSupportedContent"
      aria-controls="navbarSupportedContent"
      aria-expanded="false"
      aria-label="Toggle navigation"
    >
      <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <ul class="navbar-nav mr-auto">
        <li class="nav-item active">
          <a class="nav-link" href="/">Home<span class="sr-only">(current)</span></a>
        </li>

        <li class="nav-item">
          <router-link  class="nav-link" :to="{ name: 'Dashboard'}">Dashboard</router-link>
        </li>

        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
            Create
          </a>
          <div class="dropdown-menu" aria-labelledby="navbarDropdown">
            <router-link class="dropdown-item" :to="{ name: 'Create', params: { type: 1 }}">Person</router-link> 
            <router-link class="dropdown-item" :to="{ name: 'Create', params: { type: 2 }}">Building</router-link> 
            <router-link class="dropdown-item" :to="{ name: 'Create', params: { type: 3 }}">Task</router-link> 
          </div>
        </li>
      </ul>
    </div>
  </nav>
</template>

<script>
export default {
    name: 'Toolbar'
}
</script>
