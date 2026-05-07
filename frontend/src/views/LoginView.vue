<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2>nuosu-civil-aid</h2>
      <el-form :model="form" ref="loginFormRef">
        <el-form-item>
          <el-input v-model="form.username" placeholder="用户名 / ꑱꆹꑼꂠ" />
        </el-form-item>
        <el-form-item>
          <el-input v-model="form.password" type="password" placeholder="密码 / ꇬꑼ" />
        </el-form-item>
        
        <!-- 验证码区域 -->
        <el-form-item>
          <div class="captcha-box">
            <el-input v-model="form.captchaCode" placeholder="验证码" style="width: 200px" />
            <img :src="captchaUrl" @click="refreshCaptcha" class="captcha-img" alt="验证码" />
          </div>
        </el-form-item>

        <el-button type="primary" @click="handleLogin" :loading="loading" style="width:100%">
          登录 / ꇩꄜ
        </el-button>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'
import axios from 'axios' // 这里直接用 axios 请求验证码 Base64

const userStore = useUserStore()
const router = useRouter()
const loading = ref(false)
const captchaUrl = ref('')

const form = ref({
  username: '',
  password: '',
  captchaCode: '',
  captchaKey: '' // 对应后端 CaptchaVO 的 key
})

// 刷新验证码
const refreshCaptcha = async () => {
  // 对接后端 CaptchaController /captcha/generate
  const res = await axios.get('http://localhost:8080/captcha/generate')
  if (res.data.code === 200) {
    captchaUrl.value = res.data.data.imageBase64 // Base64字符串
    console.log(res.data.data)
    form.value.captchaKey = res.data.data.captchaId
  }
}

const handleLogin = async () => {
  loading.value = true
  try {
    await userStore.login(form.value)
    await userStore.getInfo() // 获取用户信息（角色、权限）
    router.push('/')
  } catch (err) {
    refreshCaptcha() // 登录失败刷新验证码
  } finally {
    loading.value = false
  }
}

onMounted(refreshCaptcha)
</script>

<style scoped>
.captcha-box { display: flex; gap: 10px; align-items: center; }
.captcha-img { height: 40px; cursor: pointer; border-radius: 4px; }
/* 其余样式同前 ... */
</style>