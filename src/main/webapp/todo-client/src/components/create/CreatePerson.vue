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
          v-model="personContainer.name"
        />
      </div>

      <button type="submit" class="btn btn-dark">Create</button>
    </form>
  </div>
</template>

<script>
import Fetcher from "@/fetch/fetcher";
import PersonContainer from "@/container/person-container";

export default {
  name: "CreatePerson",
  data() {
    return {
      personContainer: new PersonContainer()
    }
  },
  methods: {
    _create() {
      Fetcher.createPerson(this.personContainer);
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