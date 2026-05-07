import { defineStore } from 'pinia'
import service from '@/utils/request'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userInfo: null
  }),
  actions: {
    // 登录
    async login(loginData) {
      const data = await service.post('/auth/login', loginData)
      this.token = data.token // 后端 LoginVo 返回的 token
      localStorage.setItem('token', this.token)
    },
    // 获取用户信息
    async getInfo() {
      const data = await service.get('/auth/info')
      this.userInfo = data
      return data
    },
    // 退出
    logout() {
      this.token = ''
      this.userInfo = null
      localStorage.removeItem('token')
    }
  }
})