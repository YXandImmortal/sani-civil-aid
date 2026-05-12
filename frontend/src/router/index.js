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
          path: 'vocabulary',
          name: 'vocabulary',
          component: () => import('../views/VocabularyView.vue')
        },
        {
          path: 'consultation',
          name: 'consultation',
          component: () => import('../views/LawSearchView.vue')
        },
        {
          path: 'font',
          name: 'font',
          component: () => import('../views/FontManagementView.vue')
        },
        {
          path: 'profile',
          name: 'profile',
          component: () => import('../views/ProfileView.vue')
        },
        {
          path: 'about',
          name: 'about',
          component: () => import('../views/AboutView.vue')
        }
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