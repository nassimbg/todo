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
          v-model="buildingContainer.name"
          required
        />
        <div class="invalid-feedback">
        Please provide a valid name.
       </div>
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
      buildingContainer: new BuildingContainer(),
    };
  },
  methods: {
    _create(e) {
      let form = e.srcElement;
      form.classList.add('was-validated');

      if (form.checkValidity()) {
        Fetcher.createBuilding(this.buildingContainer)
        .then(() => {
          return {
            createdSuccessfully : true,
            notificationMsg : 'Building Created!'
          }
        })
        .catch(() => {
          return {
            createdSuccessfully : false,
            notificationMsg : 'Unable to Create Building'
          }
        })
        .then((v) => {
          this.$emit(`notification`, v)
        });
      }
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
</style>