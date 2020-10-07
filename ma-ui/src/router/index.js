import Vue from 'vue'
import VueRouter from 'vue-router'

import Appointment from '../views/Appointment.vue'
import BookInfo from '../views/BookInfo.vue'
import BookResult from '../views/BookResult.vue'
import Doctor from '../views/Doctor.vue'
import Dashboard from '../views/Dashboard.vue'
import Login from '../views/Login.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Dashboard
  },
  {
    path: '/appointment/:id',
    name: 'Appointment',
    component: Appointment
  },
  {
    path: '/book-info',
    name: 'BookInfo',
    component: BookInfo
  },
  {
    path: '/book-result/:did/:aid',
    name: 'BookResult',
    component: BookResult
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: Dashboard
  },
  {
    path: '/doctor',
    name: 'Doctor',
    component: Doctor
  },
  {
    path: '/Login',
    name: 'Login',
    component: Login
  },
  {
    path: '/about',
    name: 'About',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route

    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
