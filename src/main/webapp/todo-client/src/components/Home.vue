<template>
  <div class="container homeContainer">
    <div class="row">
      <div class="col col-lg-4 align-self-center">
        <h1 class="heading"> The #1 planning tool used by your team</h1>
      </div>
      <div class="col center-align">
        <img src="@/assets/images/planning.png" class="img shadow-lg"/>
      </div>
    </div> 

    <div class="row topPadding">
      <div class="col align-self-center">
        <h2 class="heading center-align"> The best software to plan and track your tasks</h2>
      </div>
    </div> 

    <div class="row topPadding">
      <div class="col">
        <img src="@/assets/images/createTask.png" class="img creatingTaskImg shadow-lg"/>
      </div>
      <div class="col col-lg-4 align-self-center">
          <h3>Create</h3>
          <div class="text">Create persons, buildings, and tasks</div>
      </div>
    </div>

     <div class="row topPadding">
      <div class="col col-lg-4 align-self-center">
          <h3>Track</h3>
          <div class="text">Track your team's work per person or even per building</div>
      </div>
      <div class="col">
        <img src="@/assets/images/dashboard.png" class="img creatingTaskImg shadow-lg"/>
      </div>
    </div>

    <div class="topPadding"></div>
  </div>
</template>

<script>
export default {
  name: "Home",
};
</script>

<style>

.img {  
  height: 400px;
  border-radius: 6px;

}

.creatingTaskImg {
  width: 700px;
}

.center-align {
  text-align: center;

}
.homeContainer {
  margin-top: 40px
}

.topPadding {
  padding-top: 65px;
}

.text {
  font-size: 1.25rem;
    letter-spacing: .3px;
    line-height: 1.4;
}
</style>
