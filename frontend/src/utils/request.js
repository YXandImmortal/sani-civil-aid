import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'
import { useUserStore } from '@/stores/user'

const service = axios.create({
  // 使用代理前缀
  baseURL: '/api',
  timeout: 10000
})

// 防止多个并发请求同时 401 时重复跳转
let isRedirecting = false

service.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    // 强制刷新，防止 304 缓存
    if (config.method === 'get') {
      config.params = { ...config.params, _t: Date.now() }
    }
    return config
  },
  error => Promise.reject(error)
)

service.interceptors.response.use(
  response => {
    const res = response.data

    // 【防御逻辑】检查是否误收到了 Vite 的 index.html (说明代理转发失败)
    if (typeof res === 'string' && res.includes('<!DOCTYPE html>')) {
      const errorMsg = '代理转发失败：请检查后端 8080 是否启动，或重启 Vite 服务。'
      console.error(errorMsg)
      ElMessage.error(errorMsg)
      return Promise.reject(new Error(errorMsg))
    }

    // 此时 res 应该是 JSON 对象
    if (res.code !== 200) {
      ElMessage.error(res.message || '系统错误')
      return Promise.reject(new Error(res.message || 'Error'))
    }

    return res.data
  },
  error => {
    // 处理 Token 过期或认证失效（后端返回 401 或 403 均视为需要重新登录）
    const status = error.response?.status
    if (status === 401 || status === 403) {
      if (!isRedirecting) {
        isRedirecting = true
        const userStore = useUserStore()
        userStore.logout()
        ElMessage.error('登录已过期，请重新登录')

        if (router.currentRoute.value.path !== '/login') {
          router.push('/login').finally(() => {
            isRedirecting = false
          })
        } else {
          isRedirecting = false
        }
      }
      return Promise.reject(error)
    }

    console.error('网络错误详情:', error)
    const message = error.response?.data?.message || '无法连接后端接口(8080)'
    ElMessage.error(message)
    return Promise.reject(error)
  }
)

export default service