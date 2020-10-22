<template>
  <div id="container">
    <form @submit.prevent="_create()">
      <div class="form-group">
        <label for="nameInput">Name</label>
        <input
          type="text"
          class="form-control"
          id="nameInput"
          placeholder="Enter name"
          v-model="taskContainer.name"
        />

        <select
          id="assignee"
          ref="assignee"
          class="browser-default custom-select form-element "
          @change="_selectedAssignee($event)"
        >
          <option selected :value="null">Choose Assignee...</option>
          <option
            v-for="person in persons"
            :value="person.id"
            v-bind:key="person.id"
          >
            {{ person.name }}
          </option>
        </select>

    
        <select
          id="building"
          ref="building"
          class="browser-default custom-select form-element "
          @change="_selectedBuilding($event)"
        >
          <option selected :value="null">Choose Building...</option>
          <option
            v-for="building in buildings"
            :value="building.id"
            v-bind:key="building.id"
          >
            {{ building.name }}
          </option>
        </select>
      </div>

      <button type="submit" class="btn btn-dark">Create</button>
    </form>
  </div>
</template>

<script>
import Fetcher from "@/fetch/fetcher";
import TaskContainer from "@/container/task-container";

export default {
  name: "CreateTask",
  data() {
    return {
      taskContainer: new TaskContainer(),
      persons: [],
      buildings: [],
    };
  },
  created() {
    this._getPersons().then((persons) => (this.persons = persons));
    this._getBuildings().then((buildings) => (this.buildings = buildings));
  },

  methods: {
    _create() {
      Fetcher.createTask(this.taskContainer);
    },
    _getPersons() {
      return Fetcher.getAllPersons();
    },
    _getBuildings() {
      return Fetcher.getAllBuildings();
    },
    _selectedAssignee(event) {
      this.taskContainer.assigneeId = event.target.value;
    },
    _selectedBuilding(event) {
      this.taskContainer.buildingId = event.target.value;
    },
  },
};
</script>

<style scoped>
#container {
  padding: 1.5rem;
}

.btn:hover {
  color: var(--green);
}

.form-element {
  margin-top: 1rem;
}
</style>