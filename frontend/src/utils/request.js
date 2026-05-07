import axios from 'axios'
import { ElMessage } from 'element-plus'

const service = axios.create({
  baseURL: import.meta.env.VITE_APP_BASE_API,
  timeout: 10000
})

// 请求拦截器：自动注入 JWT Token
service.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  error => Promise.reject(error)
)

// 响应拦截器：处理后端 Result 封装
service.interceptors.response.use(
  response => {
    const res = response.data
    // 根据后端 Result 类的逻辑，假设 code 为 200 是成功
    if (res.code !== 200) {
      ElMessage.error(res.message || '系统错误')
      return Promise.reject(new Error(res.message || 'Error'))
    }
    return res.data // 直接返回 Result 中的 data 部分
  },
  error => {
    ElMessage.error(error.response?.data?.message || '网络连接异常')
    return Promise.reject(error)
  }
)

export default service