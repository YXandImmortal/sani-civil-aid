<template>
  <div class="login-wrapper">
    <div class="login-decoration left">ꏓꂱ</div>
    <div class="login-decoration right">ꈄꏍ</div>
    
    <el-card class="login-card">
      <div class="login-title">
        <h2 class="fire-text">nuosu-civil-aid</h2>
        <p class="nuosu-sub">ꆈꌠꏓꂱꈄꏍꇈꑌꄜꄉꐥ</p>
      </div>

      <el-form :model="loginForm" label-position="top" @keyup.enter="handleLogin">
        <el-form-item label="用户名">
          <el-input v-model="loginForm.username" placeholder="请输入用户名" />
        </el-form-item>

        <el-form-item label="密码">
          <el-input v-model="loginForm.password" type="password" show-password placeholder="请输入密码" />
        </el-form-item>

        <el-form-item label="验证码">
          <div class="captcha-box">
            <el-input v-model="loginForm.captcha" placeholder="输入验证码" />
            <div class="img-wrapper">
              <img v-if="captchaImg" :src="captchaImg" @click="getCaptcha" title="点击刷新" />
              <div v-else class="loading-text">...</div>
            </div>
          </div>
        </el-form-item>

        <el-button 
          type="primary" 
          class="submit-btn"
          :loading="loading" 
          @click="handleLogin"
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
const loginForm = reactive({ username: '', password: '', captcha: '', captchaId: '' })

const getCaptcha = async () => {
  captchaImg.value = ''
  try {
    const data = await request.get('/captcha/generate')
    captchaImg.value = data.imageBase64
    loginForm.captchaId = data.captchaId
  } catch (err) {}
}

const handleLogin = async () => {
  if (!loginForm.captcha) return ElMessage.warning('请输入验证码')
  loading.value = true
  try {
    await userStore.login(loginForm)
    ElMessage.success('登录成功')
    router.push('/')
  } catch (err) {
    getCaptcha()
  } finally {
    loading.value = false
  }
}

onMounted(getCaptcha)
</script>

<style scoped lang="scss">
.login-wrapper {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: var(--color-bg-base);
  position: relative;
  overflow: hidden;

  // 彝族纹饰风格背景
  .login-decoration {
    position: absolute;
    font-size: 15rem;
    color: var(--color-primary-subtle);
    font-family: "Microsoft Yi Baiti";
    opacity: 0.5;
    z-index: 0;
    &.left { left: -50px; bottom: -50px; }
    &.right { right: -50px; top: -50px; }
  }

  .login-card {
    width: 420px;
    z-index: 1;
    border: 2px solid var(--color-border-default);
    box-shadow: var(--shadow-lg);
    
    .login-title {
      text-align: center;
      margin-bottom: 30px;
      .fire-text { color: var(--color-primary); margin: 0; font-size: 1.8rem; }
      .nuosu-sub { color: var(--color-secondary); margin-top: 10px; font-weight: 500; }
    }

    .captcha-box {
      display: flex; gap: 12px;
      .img-wrapper {
        width: 130px; height: 40px; border: 1px solid var(--color-border-default);
        cursor: pointer; border-radius: var(--radius-sm); overflow: hidden;
        img { width: 100%; height: 100%; }
      }
    }

    .submit-btn {
      width: 100%; height: 45px; font-size: 1.1rem;
      background-color: var(--color-primary);
      border: none;
      box-shadow: var(--shadow-glow);
      &:hover { background-color: var(--color-primary-hover); transform: translateY(-1px); }
    }
  }
}
</style>