<template>
  <div class="dashboardContainer">
    <button class="btn btn-dark position" type="button" data-toggle="collapse" data-target="#filtersContainer" aria-expanded="false" aria-controls="collapseExample">
      <svg width="1em" height="1em" viewBox="0 0 16 16" class="bi bi-filter" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
  <path fill-rule="evenodd" d="M6 10.5a.5.5 0 0 1 .5-.5h3a.5.5 0 0 1 0 1h-3a.5.5 0 0 1-.5-.5zm-2-3a.5.5 0 0 1 .5-.5h7a.5.5 0 0 1 0 1h-7a.5.5 0 0 1-.5-.5zm-2-3a.5.5 0 0 1 .5-.5h11a.5.5 0 0 1 0 1h-11a.5.5 0 0 1-.5-.5z"/>
</svg>
    </button>
    <div class="collapse" id="filtersContainer">
      <div class="filtersContainer shadow-lg">
        <Filter v-bind:values="persons" title="Assignee" @filtered="_assigneeFiltered($event)"/>
        <Filter v-bind:values="buildings" title="Buildings" @filtered="_buildingFiltered($event)"/>
      </div>
    </div>

    <div class="container">
      <div class="row">
        <div class="col-3" v-for="task in tasks" :key="task.getId()">
        <div class="card text-center task" >
        <div class="card-header navbar-dark bg-dark text-light">{{ task.getName() }}</div>
        <div class="card-body">
          <span class="dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">{{task.getStatusDescription()}}</span>
          <div class="dropdown-menu" aria-labelledby="navbarDropdown">
              <span class="dropdown-item" v-for="status in statuses" :key="status.code" @click="_statusChanged($event)" :task-id="task.getId()" :status-code="status.code">{{status.description}}</span>
          </div>
        </div>
      </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Filter from "./Filter";
import Fetcher from "@/fetch/fetcher.js";
import TaskContainer from '@/container/task-container.js';
export default {
  components: { Filter },
  data() {
    return {
      persons: [],
      buildings: [],
      tasks: [],
      statuses:[],
      filteredPerson: "",
      filteredBuilding: ""
    };
  },
  created() {
    Fetcher.getAllPersons().then((p) => (this.persons = p));
    Fetcher.getAllBuildings().then((b) => (this.buildings = b));
    this.statuses = TaskContainer.getStatues();
    this._fetchFilteredTasks();
  },
  methods: {
      _assigneeFiltered(e) {
          this.filteredPerson = e;
          this._fetchFilteredTasks();
      },
      _buildingFiltered(e) {
          this.filteredBuilding = e;
          this._fetchFilteredTasks();
      },
      _fetchFilteredTasks() {
          Fetcher.getTasks(this.filteredPerson, this.filteredBuilding)
          .then (tasks => this.tasks = tasks);
      },
      _statusChanged(e) {
          const elementAttributes = e.currentTarget.attributes;
          const taskId = elementAttributes["task-id"].value
          const statusCode = parseInt(elementAttributes["status-code"].value);

          const currentTask = this.tasks.find(t => t.getId() === taskId);

          if (currentTask.getStatusCode() !== statusCode) {
              currentTask.setStatus(statusCode);
              Fetcher.putTask(currentTask);
          }
      }

  }
};
</script>

<style scoped>

.filtersContainer {
    display: flex;
    flex-direction: row;
    max-height: 30%;
    border-color: var(--green);
    background-color: #f0f1f2;
    
}

.task {
    margin: 20px auto;
}

.position{
  position: absolute;
  right: 10px;
  top: 10px;
}

.dashboardContainer {
  height: 100%;
  position: relative;
}

.tasksContainer {
  padding: 20px;
}
</style>
