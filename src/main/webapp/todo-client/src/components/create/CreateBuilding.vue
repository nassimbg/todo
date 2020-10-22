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
          v-model="buildingContainer.name"
        />
      </div>

      <button type="submit" class="btn btn-dark">Create</button>
    </form>
  </div>
</template>

<script>
import Fetcher from "@/fetch/fetcher";
import BuildingContainer from "@/container/building-container";

export default {
  name: "CreateBuilding",
  data() {
    return {
      buildingContainer: new BuildingContainer()
    }
  },
  methods: {
    _create() {
      Fetcher.createBuilding(this.buildingContainer);
    }
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
</style>