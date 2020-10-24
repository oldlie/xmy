import Vue from 'vue'
import VueRouter from 'vue-router'

import Appointment from '../views/Appointment.vue'
import AppointmentForm from '../views/AppointmentForm.vue'
import BookInfoList from '../views/BookInfoList.vue'
import ExportBookInfo from '../views/ExportBookInfo.vue'
import Login from '../views/Login.vue'
import Dashboard from '../views/Dashboard.vue'
import Doctor from '../views/Doctor.vue'
import DoctorForm from '../views/DoctorForm.vue'
import ModifyPassword from '../views/ModifyPassword.vue'
import SmsSetting from '../views/SmsSetting.vue'
import System from '../views/System.vue'
import SystemSetting from '../views/SystemSetting.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Login',
    component: Login
  },
  {
    path: '/appointment',
    name: 'Appointment',
    component: Appointment
  },
  {
    path: '/appointment-form/:id',
    name: 'AppointmentForm',
    component: AppointmentForm
  },
  {
    path: '/book-info',
    name: 'BookInfoList',
    component: BookInfoList
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
    path: '/doctor-form/:id',
    name: 'DoctorForm',
    component: DoctorForm
  },
  {
    path: '/export/book-info',
    name: 'ExportBookInfo',
    component: ExportBookInfo
  },
  {
    path: '/modify-pass',
    name: 'ModifyPassword',
    component: ModifyPassword
  },
  {
    path: '/sms-setting',
    name: 'SmsSetting',
    component: SmsSetting
  },
  {
    path: '/system',
    name: 'System',
    component: System 
  },
  {
    path: '/system-setting',
    name: 'SystemSetting',
    component: SystemSetting
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
