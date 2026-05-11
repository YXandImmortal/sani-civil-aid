<template>
  <div class="login-wrapper">
    <el-card class="login-card">
      <div class="login-title">
        <h2 class="fire-text">
          <span class="yi-bilingual">
            <span>典亮诺苏</span>
            <span class="yi-placeholder">[彝文占位符]</span>
          </span>
        </h2>
        <p class="nuosu-sub yi-bilingual">
          <span>用户登录</span>
          <span class="yi-placeholder">[彝文占位符]</span>
        </p>
      </div>

      <el-form :model="loginForm" label-position="top" @keyup.enter="handleLogin">
        <el-form-item>
          <template #label>
            <span class="yi-bilingual">
              <span>用户名</span>
              <span class="yi-placeholder">[彝文占位符]</span>
            </span>
          </template>
          <el-input v-model="loginForm.username" placeholder="请输入用户名" />
        </el-form-item>

        <el-form-item>
          <template #label>
            <span class="yi-bilingual">
              <span>密码</span>
              <span class="yi-placeholder">[彝文占位符]</span>
            </span>
          </template>
          <el-input v-model="loginForm.password" type="password" show-password placeholder="请输入密码" />
        </el-form-item>

        <el-form-item>
          <template #label>
            <span class="yi-bilingual">
              <span>验证码</span>
              <span class="yi-placeholder">[彝文占位符]</span>
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
                  <span class="yi-placeholder">[彝文占位符]</span>
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
            <span class="yi-placeholder">[彝文占位符]</span>
          </span>
        </el-button>

        <div class="register-link">
          <span class="yi-bilingual">
            <span>还没有账号？</span>
            <span class="yi-placeholder">[彝文占位符]</span>
          </span>
          <el-link type="primary" @click="router.push('/register')">
            <span class="yi-bilingual">
              <span>去注册</span>
              <span class="yi-placeholder">[彝文占位符]</span>
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
