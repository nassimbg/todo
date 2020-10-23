<template>
  <section class="h-90">
    <nav class="nav nav-pills nav-fill navbar-dark bg-dark">
      <a class="nav-item nav-link tab" data-toggle="tab" href="#person" aria-controls="person" aria-selected="true" v-bind:class="{ 'active': _isTabActive(1) }" @click="_isActiveTab(1)">Person</a>
      <a class="nav-item nav-link tab" data-toggle="tab" href="#building" aria-controls="building" aria-selected="false" v-bind:class="{ 'active': _isTabActive(2) }" @click="_isActiveTab(2)">Building</a>
      <a class="nav-item nav-link tab" data-toggle="tab" href="#task" aria-controls="task" aria-selected="false" v-bind:class="{ 'active': _isTabActive(3)}" @click="_isActiveTab(3)">Task</a>
    </nav>
    <div class="tab-content h-100 row relativeParent" id="CreateTabs">
      <div class="tab-pane fade align-self-center w-100" id="person" role="tabpanel" aria-labelledby="person-tab" v-bind:class="{ 'active': _isTabActive(1),  'show': _isTabActive(1)}">
        <CreatePerson v-if="_isTabActive(1)" @notification="_activateNotification($event)"/>
      </div>
      <div class="tab-pane fade align-self-center w-100" id="building" role="tabpanel" aria-labelledby="building-tab" v-bind:class="{ 'active': _isTabActive(2), 'show': _isTabActive(2)}">
        <CreateBuilding v-if="_isTabActive(2)" @notification="_activateNotification($event)"/>
      </div>
      <div class="tab-pane fade align-self-center w-100" id="task" role="tabpanel" aria-labelledby="task-tab" v-bind:class="{ 'active': _isTabActive(3),  'show': _isTabActive(3)}">
        <CreateTask v-if="_isTabActive(3)" @notification="_activateNotification($event)"/>
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
      selectedTab: parseInt(this.$route.params.type),
      triggerNotification: false,
      createdSuccessfully: false,
      notificationMsg: ""
    }
  },
  methods: {
    _isTabActive(val) {
      return this.selectedTab === val;
    },
    _isActiveTab(tabId) {
      this.selectedTab = tabId;
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
      this.selectedTab = parseInt(to.params.type);
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
