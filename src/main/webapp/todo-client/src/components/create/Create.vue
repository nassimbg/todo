<template>
  <section>
    <nav class="nav nav-pills nav-fill navbar-dark bg-dark">
      <a class="nav-item nav-link tab" data-toggle="tab" href="#person" aria-controls="person" aria-selected="true" v-bind:class="{ 'active': _isActive(1) }">Person</a>
      <a class="nav-item nav-link tab" data-toggle="tab" href="#building" aria-controls="building" aria-selected="false" v-bind:class="{ 'active': _isActive(2) }">Building</a>
      <a class="nav-item nav-link tab" data-toggle="tab" href="#task" aria-controls="task" aria-selected="false" v-bind:class="{ 'active': _isActive(3)}">Task</a>
    </nav>
    <div class="tab-content" id="CreateTabs">
      <div class="tab-pane fade" id="person" role="tabpanel" aria-labelledby="person-tab" v-bind:class="{ 'active': _isActive(1),  'show': _isActive(1)}">
        <CreatePerson/>
      </div>
      <div class="tab-pane fade" id="building" role="tabpanel" aria-labelledby="building-tab" v-bind:class="{ 'active': _isActive(2), 'show': _isActive(2)}">
        <CreateBuilding/>
      </div>
      <div class="tab-pane fade" id="task" role="tabpanel" aria-labelledby="task-tab" v-bind:class="{ 'active': _isActive(3),  'show': _isActive(3)}">
        <CreateTask/>
      </div>
    </div>
  </section>
</template>

<script>
import CreatePerson from './CreatePerson'
import CreateBuilding from './CreateBuilding'
import CreateTask from './CreateTask'

export default {
  name: "Create",
  components: {
    CreatePerson, CreateBuilding, CreateTask
  },
  data() {
    return {
      intiallySelected: parseInt(this.$route.params.type)
    }
  },
  methods: {
    _isActive(val) {
      return this.intiallySelected === val;
    }
  },
  watch: {
    /* eslint-disable no-unused-vars */
    $route(to, from) {
          /* eslint-disable no-debugger */
debugger
/* eslint-enable no-debugger */

      this.intiallySelected = parseInt(to.params.type);
    }
    /* eslint-enable no-unused-vars */
  }
};
</script>

<style>

.tab-content {
    width: 50%;
    margin: auto;
}

.tab {
    color: var(--light);
}

.tab:hover {
    color: var(--green);
}

.tab.active{
    color: var(--dark)!important;
    background-color: var(--light)!important;
}
</style>
