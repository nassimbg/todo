<template>
  <div class="filterContainer">
    <div class="card text-center filter">
      <div class="card-header navbar-dark bg-dark text-light">{{ title }}</div>
      <div class="card-body overflow-auto">
        <div class="list-group overflow-auto">
          <div v-for="item in values" :key="item.id">
            <input type="radio" :name="'RadioInputName' + title" value="{{item.id}}" :id="item.id"/>
            <label class="list-group-item" @click="_filterValueClicked(item.id)">{{item.name}}</label>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Filter",
  props: ["title", "values"],
  methods: {
    _filterValueClicked(id) {
     let option = document.getElementById(id);
     
     let emittingId = null;
     if (!option.checked) {
       emittingId = id;
     }

     this.$emit(`filtered`, emittingId);

     option.checked = !option.checked;
    }
  }

  
};
</script>

<style scoped>

.filter {
  max-height: 100%;
}
.filterContainer {
  width: 300px;
  margin: 10px;

}

.list-group {
height: 300px
}

.list-group-item {
  user-select: none;
}


.list-group input[type="radio"] {
  display: none;
  
}

.list-group input[type="radio"] + .list-group-item {
  cursor: pointer;
}

.list-group input[type="radio"] + .list-group-item:before {
  content: "\2022";
  color: transparent;
  font-weight: bold;
  margin-right: 1em;
}

.list-group input[type="radio"]:checked + .list-group-item {
  background-color: var(--green);
  color: var(--light);
}

.list-group input[type="radio"]:checked + .list-group-item:before {
  color: inherit;
}

</style>
