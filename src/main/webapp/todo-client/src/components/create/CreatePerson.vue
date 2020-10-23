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
          v-model="personContainer.name"
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
import PersonContainer from "@/container/person-container";

export default {
  name: "CreatePerson",

  data() {
    return {
      personContainer: new PersonContainer(),
    }
  },
  methods: {
    _create(e) {
      let form = e.srcElement;
      form.classList.add('was-validated');

      if (form.checkValidity()) {
        Fetcher.createPerson(this.personContainer)
        .then(() => {
          return {
            createdSuccessfully : true,
            notificationMsg : 'Person Created!'
          }
        })
        .catch(() => {
          return {
            createdSuccessfully : false,
            notificationMsg : 'Unable to Create Person'
          }
        })
        .then((v) => this.$emit(`notification`, v));
      }
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