import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginView.vue')
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../views/RegisterView.vue')
    },
    {
      path: '/',
      component: () => import('../layout/MainLayout.vue'),
      redirect: '/home',
      children: [
        {
          path: 'home',
          name: 'home',
          component: () => import('../views/ConsultationView.vue')
        },
        {
          path: 'glossary',
          name: 'glossary',
          component: () => import('../views/GlossaryView.vue')
        },
        // ... 其他导入保持不变
        {
          path: 'glossary',
          name: 'glossary',
          component: () => import('../views/GlossaryView.vue')
        },
        {
          path: 'consultation',
          name: 'consultation',
          component: () => import('../views/LawSearchView.vue')
        }
// ...
        
      ]
    }
  ]
})

router.beforeEach((to) => {
  const userStore = useUserStore()
  const publicPages = ['/login', '/register']
  const authRequired = !publicPages.includes(to.path)

  if (authRequired && !userStore.token) {
    return '/login'
  }
  if (to.path === '/login' && userStore.token) {
    return '/'
  }
  return true
})

export default router