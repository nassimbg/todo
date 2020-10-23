<template>
  <section class="h-90">
    <nav class="nav nav-pills nav-fill navbar-dark bg-dark">
      <a class="nav-item nav-link tab" data-toggle="tab" href="#person" aria-controls="person" aria-selected="true" v-bind:class="{ 'active': _isActive(1) }">Person</a>
      <a class="nav-item nav-link tab" data-toggle="tab" href="#building" aria-controls="building" aria-selected="false" v-bind:class="{ 'active': _isActive(2) }">Building</a>
      <a class="nav-item nav-link tab" data-toggle="tab" href="#task" aria-controls="task" aria-selected="false" v-bind:class="{ 'active': _isActive(3)}">Task</a>
    </nav>
    <div class="tab-content h-100 row relativeParent" id="CreateTabs">
      <div class="tab-pane fade align-self-center w-100" id="person" role="tabpanel" aria-labelledby="person-tab" v-bind:class="{ 'active': _isActive(1),  'show': _isActive(1)}">
        <CreatePerson @notification="_activateNotification($event)"/>
      </div>
      <div class="tab-pane fade align-self-center w-100" id="building" role="tabpanel" aria-labelledby="building-tab" v-bind:class="{ 'active': _isActive(2), 'show': _isActive(2)}">
        <CreateBuilding @notification="_activateNotification($event)"/>
      </div>
      <div class="tab-pane fade align-self-center w-100" id="task" role="tabpanel" aria-labelledby="task-tab" v-bind:class="{ 'active': _isActive(3),  'show': _isActive(3)}">
        <CreateTask @notification="_activateNotification($event)"/>
      </div>
    </div>

    <Notification id="personNotification" :show="triggerNotification" :message="notificationMsg" :success="createdSuccessfully"/>
  </section>
</template>

<script>
import CreatePerson from './CreatePerson';
import CreateBuilding from './CreateBuilding';
import CreateTask from './CreateTask';
import Notification from './Notification';

export default {
  name: "Create",
  components: {
    CreatePerson, CreateBuilding, CreateTask, Notification
  },
  data() {
    return {
      intiallySelected: parseInt(this.$route.params.type),
      triggerNotification: false,
      createdSuccessfully: false,
      notificationMsg: ""
    }
  },
  methods: {
    _isActive(val) {
      return this.intiallySelected === val;
    },
    _activateNotification(e) {
      this.createdSuccessfully = e.createdSuccessfully;
      this.notificationMsg = e.notificationMsg;
      this.triggerNotification = !this.triggerNotification
      
    }
  },
  watch: {
    /* eslint-disable no-unused-vars */
    $route(to, from) {
      this.intiallySelected = parseInt(to.params.type);
    }
    /* eslint-enable no-unused-vars */
  }
};
</script>

<style>

.h-90{
  height: 90%;
}
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

.relativeParent {
  position: relative;
}
</style>
