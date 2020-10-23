<template>
  <div id="container">

    <form @submit.prevent="_create($event)" class="needs-validation" novalidate>
      <div class="form-group">
        <label for="nameInput">Name</label>
        <input
          type="text"
          class="form-control"
          id="nameInput"
          placeholder="Enter name"
          v-model="taskContainer.name"
          required
        />
        <div class="invalid-feedback">Please provide a valid name.</div>
      </div>

      <div class="form-group">
        <select
          id="assignee"
          ref="assignee"
          class="browser-default custom-select form-element"
          @change="_selectedAssignee($event)"
          required
        >
          <option selected value="">Choose Assignee...</option>
          <option
            v-for="person in persons"
            :value="person.id"
            v-bind:key="person.id"
          >
            {{ person.name }}
          </option>
        </select>
        <div class="invalid-feedback">Please select Assignee</div>
      </div>

      <div class="form-group">
        <select
          id="building"
          ref="building"
          class="browser-default custom-select form-element"
          @change="_selectedBuilding($event)"
          required
        >
          <option selected value="">Choose Building...</option>
          <option
            v-for="building in buildings"
            :value="building.id"
            v-bind:key="building.id"
          >
            {{ building.name }}
          </option>
        </select>
        <div class="invalid-feedback">Please select Building</div>
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
    _create(e) {
      let form = e.srcElement;
      form.classList.add("was-validated");

      if (form.checkValidity()) {
        Fetcher.createTask(this.taskContainer)
        .then(() => {
          return {
            createdSuccessfully : true,
            notificationMsg : 'Building Task!'
          }
        })
        .catch(() => {
          return {
            createdSuccessfully : false,
            notificationMsg : 'Unable to Create Task'
          }
        })
        .then((v) => {
          this.$emit(`notification`, v)
        });
      }
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