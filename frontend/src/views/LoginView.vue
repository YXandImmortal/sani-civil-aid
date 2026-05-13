<template>
  <div class="login-wrapper">
    <img src="/logo.svg" alt="" class="login-bg-logo" />
    <div class="theme-toggle-wrapper">
      <el-tooltip>
        <template #content>
          <span class="yi-bilingual">
            <span>{{ appStore.theme === 'light' ? '暗色模式' : '明亮模式' }}</span>
            <span class="yi-placeholder">{{ appStore.theme === 'light' ? '乊仏埾厹' : '勍倵埾厹' }}</span>
          </span>
        </template>
        <el-button
          circle
          class="theme-toggle"
          @click="appStore.toggleTheme"
        >
          <el-icon v-if="appStore.theme === 'light'"><Moon /></el-icon>
          <el-icon v-else><Sunny /></el-icon>
        </el-button>
      </el-tooltip>
    </div>
    <el-card class="login-card">
      <div class="login-title">
        <h2 class="fire-text">
          <span class="yi-bilingual">
            <span>典亮撒尼</span>
            <span class="yi-placeholder">凲凴嘍勍</span>
          </span>
        </h2>
        <p class="nuosu-sub yi-bilingual">
          <span>用户登录</span>
          <span class="yi-placeholder">咡堅刱圽</span>
        </p>
      </div>

      <el-form :model="loginForm" label-position="top" @keyup.enter="handleLogin">
        <el-form-item>
          <template #label>
            <span class="yi-bilingual">
              <span>用户名</span>
              <span class="yi-placeholder">咡堅啔</span>
            </span>
          </template>
          <el-input v-model="loginForm.username" placeholder="请输入用户名" />
        </el-form-item>

        <el-form-item>
          <template #label>
            <span class="yi-bilingual">
              <span>密码</span>
              <span class="yi-placeholder">伱匃</span>
            </span>
          </template>
          <el-input v-model="loginForm.password" type="password" show-password placeholder="请输入密码" />
        </el-form-item>

        <el-form-item>
          <template #label>
            <span class="yi-bilingual">
              <span>验证码</span>
              <span class="yi-placeholder">偓乿匃</span>
            </span>
          </template>
          <div class="captcha-container">
            <el-input v-model="loginForm.captcha" placeholder="输入验证码" class="captcha-input" />
            <div class="captcha-img-box">
              <img 
                v-if="captchaImg" 
                :src="captchaImg" 
                @click="getCaptcha" 
                class="captcha-img" 
                title="点击刷新"
                alt="验证码图片"/>
              <el-button v-else :loading="true" text>
                <span class="yi-bilingual">
                  <span>加载中</span>
                  <span class="yi-placeholder">剓凓儶</span>
                </span>
              </el-button>
            </div>
          </div>
        </el-form-item>

        <el-button 
          type="primary" 
          :loading="loading" 
          @click="handleLogin" 
          class="submit-btn"
        >
          <span class="yi-bilingual">
            <span>进入系统</span>
            <span class="yi-placeholder">嚸偒嘫圽</span>
          </span>
        </el-button>

        <div class="register-link">
          <span class="yi-bilingual">
            <span>还没有账号？</span>
            <span class="yi-placeholder">偁垽儔儶俬？</span>
          </span>
          <el-link type="primary" @click="router.push('/register')">
            <span class="yi-bilingual">
              <span>去注册</span>
              <span class="yi-placeholder">両吋坖</span>
            </span>
          </el-link>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { useAppStore } from '@/stores/app'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Sunny, Moon } from '@element-plus/icons-vue'
import request from '@/utils/request'

const userStore = useUserStore()
const appStore = useAppStore()
const router = useRouter()
const loading = ref(false)
const captchaImg = ref('')
const loginForm = reactive({ username: '', password: '', captcha: '', captchaId: '' })

const getCaptcha = async () => {
  captchaImg.value = ''
  try {
    const data = await request.get('/captcha/generate')
    console.log('验证码数据获取成功:', data)
    
    // 对应后端字段：imageBase64 和 captchaId
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
    
    // 登录成功后跳转到首页
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

  // 半透明大号 Logo 背景
  .theme-toggle-wrapper {
    position: absolute;
    top: 20px;
    right: 20px;
    z-index: 10;
  }
  .theme-toggle {
    background-color: var(--color-bg-elevated);
    border-color: var(--color-border-default);
    color: var(--color-secondary);
  }

  .login-bg-logo {
    position: absolute;
    width: 1200px;
    height: 1200px;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    opacity: 0.06;
    z-index: 0;
    pointer-events: none;
    user-select: none;
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

    .captcha-container {
      display: flex; align-items: center; justify-content: space-between; gap: 12px;
      width: 380px;
      .captcha-input {
        flex: 1;
      }
      .captcha-img-box {
        width: 130px; height: 32px; border: 1px solid var(--color-border-default);
        cursor: pointer; border-radius: var(--radius-sm); overflow: hidden;
        flex-shrink: 0;
        img { width: 100%; height: 100%; display: block; }
      }
    }

    .submit-btn {
      width: 100%; height: 45px; font-size: 1.1rem;
      background-color: var(--color-primary);
      border: none;
      box-shadow: var(--shadow-glow);
      &:hover { background-color: var(--color-primary-hover); transform: translateY(-1px); }
    }

    .register-link {
      margin-top: 16px;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 4px;
      font-size: 0.9rem;
      color: var(--color-text-secondary);
    }
  }
}
</style>
