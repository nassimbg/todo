<template>
    <div
      :id="id"
      class="toast toastPosition"
      role="alert"
      aria-live="assertive"
      aria-atomic="true"
    >
      <div class="toast-body" v-bind:class="[success ? 'alert-success' : 'alert-danger']">{{message}}</div>
    </div>
</template>

<script>
import $ from "jquery";

export default {
    name: 'Notification',
    props: ["message", "show", "success", "id"],
    watch: {
        /* eslint-disable no-unused-vars */
        show(newVal, oldVal) {
            $(`#${this.id}`).toast('show');
        }
        /* eslint-enable no-unused-vars */
    }
}
</script>

<style>
.toastPosition {
  position: absolute; 
  top: 20px; 
  right: 50%;
}
</style>