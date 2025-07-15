import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {path: '/', redirect :'/Login',},
    {path: '/manager', component: ()=>import('../views/Manager.vue'),
      children:[
        {path: 'home',name:'主页', component:()=> import('../views/Home.vue'),},
        {path: 'employee',name:'员工管理', component:()=> import('../views/Employee.vue'),},
        {path: 'attendance',name:'考勤打卡', component:()=> import('../views/Attendance.vue'),},
        {path: 'salary',name:'薪资发放', component:()=> import('../views/Salary.vue'),},
        {path: 'department',name:'部门管理', component:()=> import('../views/Department.vue'),},
        {path: 'contract',name:'合同管理', component:()=> import('../views/Contract.vue'),},
        {path: 'training',name:'培训管理', component:()=> import('../views/Training.vue'),},
        {path: 'leave',name:'请假管理', component:()=> import('../views/leaveRequest.vue'),},
        {path: 'Resignation',name:'离职管理', component:()=> import('../views/Resignation.vue'),},
        {path: 'Person',name:'个人信息', component:()=> import('../views/Person.vue'),},
        {path: 'updatePassword',name:'修改密码', component:()=> import('../views/UpdatePassword.vue'),},
      ]
    },
    {path: '/login', component: () => import('../views/LoginView.vue')},
    {path: '/notFound', component: import('../views/404.vue'),},
    {path: '/:pathMatch(.*)*', redirect: '/notFound' ,},
  ],
})


export default router
