import { defineStore } from 'pinia'
import request from '@/utils/request'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    // 初始从本地加载用户信息
    userInfo: JSON.parse(localStorage.getItem('userInfo')) || null
  }),
  
  actions: {
    /**
     * 登录
     * @param {Object} loginForm {username, password, captcha, captchaId}
     */
    async login(loginForm) {
      // 对应后端 POST /auth/login
      const data = await request.post('/auth/login', loginForm)
      
      this.token = data.token
      this.userInfo = data.userInfo
      
      // 持久化到本地存储
      localStorage.setItem('token', data.token)
      localStorage.setItem('userInfo', JSON.stringify(data.userInfo))
      
      return data
    },

    /**
     * 获取当前用户信息
     */
    async getInfo() {
      // 对应后端 GET /auth/info
      const data = await request.get('/auth/info')
      this.userInfo = data
      localStorage.setItem('userInfo', JSON.stringify(data))
      return data
    },

    /**
     * 修改当前用户信息
     * @param {Object} infoForm {username, mobile}
     */
    async updateInfo(infoForm) {
      const data = await request.put('/auth/info', infoForm)
      // 更新 token 和用户信息
      this.token = data.token
      this.userInfo = data.userInfo
      localStorage.setItem('token', data.token)
      localStorage.setItem('userInfo', JSON.stringify(data.userInfo))
      return data
    },

    /**
     * 修改当前用户密码
     * @param {Object} pwdForm {oldPassword, newPassword, confirmPassword}
     */
    async changePassword(pwdForm) {
      await request.post('/auth/change-password', pwdForm)
    },

    /**
     * 退出登录
     */
    logout() {
      this.token = ''
      this.userInfo = null
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
    }
  }
})