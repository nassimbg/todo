import { createWebHistory, createRouter } from "vue-router";
import Home from "@/components/Home.vue";
import Create from '@/components/create/Create.vue';
import Dashboard from '@/components/dashboard/Dashboard.vue';

const routes = [
  {
    path: "/",
    name: "Home",
    component: Home
  },
  {
    path: "/dashboard",
    name: "Dashboard",
    component: Dashboard
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
