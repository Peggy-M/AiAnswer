import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Courses from '../views/Courses.vue'
import Exams from '../views/Exams.vue'
import AIAnalysis from '../views/AIAnalysis.vue'
import ExamList from '../views/exam/ExamList.vue'
import Layout from '../layout/index.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/courses',
    name: 'Courses',
    component: () => import('@/views/Courses.vue')
  },
  {
    path: '/exams',
    name: 'ExamList',
    component: ExamList
  },
  {
    path: '/exams/:id',
    name: 'ExamDetail',
    component: () => import('../views/exam/ExamDetail.vue')
  },
  {
    path: '/ai-analysis',
    name: 'AIAnalysis',
    component: AIAnalysis
  },
  {
    path: '/course',
    component: Layout,
    children: [
      {
        path: 'list',
        name: 'CourseList',
        component: () => import('@/views/course/CourseList.vue'),
        meta: { title: '全部课程', icon: 'education' }
      },
      {
        path: ':id',
        name: 'CourseDetail',
        component: () => import('@/views/course/CourseDetail.vue'),
        meta: { title: '课程详情' },
        hidden: true
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  console.log('Route change:', to.path)
  next()
})

export default router 