import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import AIAnalysis from '../views/AIAnalysis.vue'
import Layout from '../layout/index.vue'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import AdminLayout from '@/layout/AdminLayout.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/course',
    component: Layout,
    children: [
      {
        path: 'list',
        name: 'CourseList',
        component: () => import('@/views/Courses.vue'),
        meta: { title: '全部课程' }
      },
      {
        path: ':id',
        name: 'CourseDetail',
        component: () => import('@/views/course/CourseDetail.vue'),
        meta: { title: '课程详情' }
      },
      {
        path: 'learning/:id',
        name: 'CourseLearning',
        component: () => import('@/views/course/CourseLearning.vue'),
        meta: { title: '课程学习' }
      }
    ]
  },
  {
    path: '/exam',
    component: Layout,
    children: [
      {
        path: 'list',
        name: 'ExamList',
        component: () => import('@/views/exam/ExamList.vue'),
        meta: { title: '考试中心' }
      },
      {
        path: ':id',
        name: 'ExamDetail',
        component: () => import('@/views/exam/ExamDetail.vue'),
        meta: { title: '考试详情' }
      }
    ]
  },
  {
    path: '/ai-analysis',
    name: 'AIAnalysis',
    component: AIAnalysis
  },
  {
    path: '/admin',
    component: AdminLayout,
    meta: { requiresAdmin: true },
    redirect: '/admin/courses',
    children: [
      {
        path: 'courses',
        name: 'AdminCourses',
        component: () => import('@/views/admin/CourseManagement.vue'),
        meta: { title: '课程管理' }
      },
      {
        path: 'courses/create',
        name: 'CreateCourse',
        component: () => import('@/views/admin/CourseForm.vue'),
        meta: { title: '创建课程' }
      },
      {
        path: 'courses/edit/:id',
        name: 'EditCourse',
        component: () => import('@/views/admin/CourseForm.vue'),
        meta: { title: '编辑课程' }
      },
      {
        path: 'courses/:id/content',
        name: 'CourseContent',
        component: () => import('@/views/admin/CourseContent.vue'),
        meta: { title: '课程内容管理' }
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/user',
    component: Layout,
    children: [
      {
        path: 'profile',
        name: 'UserProfile',
        component: () => import('@/views/user/Profile.vue'),
        meta: { title: '个人中心' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  
  // 如果是登录页面，直接放行
  if (to.path === '/login') {
    next()
    return
  }
  
  // 检查是否已登录
  if (!userStore.userInfo.id) {
    next('/login')
    return
  }
  
  // 检查管理员权限
  if (to.matched.some(record => record.meta.requiresAdmin)) {
    if (!userStore.isAdmin) {
      ElMessage.error('需要管理员权限')
      next('/')
      return
    }
  }
  
  next()
})

export default router 