import { createWebHistory, createRouter } from "vue-router";
import Home from "@/components/Home.vue";
import Create from '@/components/create/Create.vue';

const routes = [
  {
    path: "/",
    name: "Home",
    component: Home
  },
  {
    path: "/create/:type",
    name: "Create",
    component: Create
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;