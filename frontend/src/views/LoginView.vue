<template>
  <div class="login-wrapper">
    <el-card class="login-card">
      <div class="login-title">
        <h2>nuosu-civil-aid</h2>
        <p>ꆈꌠꏓꂱꈄꏍꇈꑌꄜꄉꐥ</p>
      </div>

      <el-form :model="loginForm" label-position="top" @keyup.enter="handleLogin">
        <el-form-item label="用户名 / ꑱꆹꑼꂠ">
          <el-input v-model="loginForm.username" placeholder="请输入用户名" />
        </el-form-item>

        <el-form-item label="密码 / ꇬꑼ">
          <el-input v-model="loginForm.password" type="password" show-password placeholder="请输入密码" />
        </el-form-item>

        <el-form-item label="验证码">
          <div class="captcha-container">
            <el-input v-model="loginForm.captcha" placeholder="输入验证码" style="flex: 1" />
            <div class="captcha-img-box">
              <img 
                v-if="captchaImg" 
                :src="captchaImg" 
                @click="getCaptcha" 
                class="captcha-img" 
                title="点击刷新"
              />
              <el-button v-else :loading="true" text>加载中</el-button>
            </div>
          </div>
        </el-form-item>

        <el-button 
          type="primary" 
          :loading="loading" 
          @click="handleLogin" 
          style="width: 100%; margin-top: 10px"
        >
          进入系统 / ꇩꄜ
        </el-button>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const userStore = useUserStore()
const router = useRouter()

const loading = ref(false)
const captchaImg = ref('')

const loginForm = reactive({
  username: '',
  password: '',
  captcha: '',
  captchaId: ''
})

// 获取验证码 (对应后端 GET /captcha/generate)
const getCaptcha = async () => {
  captchaImg.value = '' // 先清空旧图，视觉上有刷新感
  try {
    // 此时 request 已经配置了 baseURL: '/api'
    const data = await request.get('/captcha/generate')
    console.log('验证码数据获取成功:', data)
    
    // 对应后端字段：imageBase64 和 captchaId
    captchaImg.value = data.imageBase64
    loginForm.captchaId = data.captchaId
  } catch (err) {
    console.error('LoginView 获取验证码失败:', err)
  }
}

// 登录提交
const handleLogin = async () => {
  if (!loginForm.username || !loginForm.password) {
    return ElMessage.warning('请输入用户名和密码')
  }
  if (!loginForm.captcha) {
    return ElMessage.warning('请输入验证码')
  }

  loading.value = true
  try {
    // 调用 store 中的登录 actions
    await userStore.login(loginForm)
    ElMessage.success('登录成功')
    
    // 登录成功后跳转到首页
    router.push('/')
  } catch (err) {
    // 登录失败（密码错或验证码错）自动刷新验证码
    getCaptcha()
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  getCaptcha()
})
</script>

<style scoped lang="scss">
.login-wrapper {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #1d2b64 0%, #f8f9fa 100%);

  .login-card {
    width: 420px;
    padding: 10px;

    .login-title {
      text-align: center;
      margin-bottom: 30px;
      h2 { margin: 0; color: #409eff; }
      p { color: #909399; font-size: 0.85rem; margin-top: 8px; }
    }

    .captcha-container {
      display: flex;
      gap: 12px;
      align-items: center;
      .captcha-img-box {
        width: 130px;
        height: 48px;
        display: flex;
        align-items: center;
        justify-content: center;
        .captcha-img {
          width: 100%;
          height: 100%;
          cursor: pointer;
          border-radius: 4px;
          border: 1px solid #dcdfe6;
        }
      }
    }
  }
}
</style>