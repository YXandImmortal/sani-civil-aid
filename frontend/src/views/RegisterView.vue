<template>
  <div class="register-wrapper">
    <div class="register-decoration left">[彝文占位符]</div>
    <div class="register-decoration right">[彝文占位符]</div>

    <el-card class="register-card">
      <div class="register-title">
        <h2 class="fire-text">
          <span class="yi-bilingual">
            <span>典亮诺苏</span>
            <span class="yi-placeholder">[彝文占位符]</span>
          </span>
        </h2>
        <p class="nuosu-sub yi-bilingual">
          <span>用户注册</span>
          <span class="yi-placeholder">[彝文占位符]</span>
        </p>
      </div>

      <el-form
        ref="registerFormRef"
        :model="registerForm"
        :rules="registerRules"
        label-position="top"
        @keyup.enter="handleRegister"
      >
        <el-form-item prop="username">
          <template #label>
            <span class="yi-bilingual">
              <span>用户名</span>
              <span class="yi-placeholder">[彝文占位符]</span>
            </span>
          </template>
          <el-input v-model="registerForm.username" placeholder="请输入用户名（3-20位）" />
        </el-form-item>

        <el-form-item prop="password">
          <template #label>
            <span class="yi-bilingual">
              <span>密码</span>
              <span class="yi-placeholder">[彝文占位符]</span>
            </span>
          </template>
          <el-input
            v-model="registerForm.password"
            type="password"
            show-password
            placeholder="请输入密码（6-20位）"
          />
        </el-form-item>

        <el-form-item prop="confirmPassword">
          <template #label>
            <span class="yi-bilingual">
              <span>确认密码</span>
              <span class="yi-placeholder">[彝文占位符]</span>
            </span>
          </template>
          <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            show-password
            placeholder="请再次输入密码"
          />
        </el-form-item>

        <el-form-item prop="mobile">
          <template #label>
            <span class="yi-bilingual">
              <span>手机号</span>
              <span class="yi-placeholder">[彝文占位符]</span>
            </span>
          </template>
          <el-input v-model="registerForm.mobile" placeholder="请输入手机号（选填）" />
        </el-form-item>

        <el-form-item prop="captcha">
          <template #label>
            <span class="yi-bilingual">
              <span>验证码</span>
              <span class="yi-placeholder">[彝文占位符]</span>
            </span>
          </template>
          <div class="captcha-container">
            <el-input
              v-model="registerForm.captcha"
              placeholder="输入验证码"
              class="captcha-input"
            />
            <div class="captcha-img-box">
              <img
                v-if="captchaImg"
                :src="captchaImg"
                @click="getCaptcha"
                class="captcha-img"
                title="点击刷新"
                alt="验证码图片"
              />
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
          @click="handleRegister"
          class="submit-btn"
        >
          <span class="yi-bilingual">
            <span>注册</span>
            <span class="yi-placeholder">[彝文占位符]</span>
          </span>
        </el-button>

        <div class="login-link">
          <span class="yi-bilingual">
            <span>已有账号？</span>
            <span class="yi-placeholder">[彝文占位符]</span>
          </span>
          <el-link type="primary" @click="router.push('/login')">
            <span class="yi-bilingual">
              <span>去登录</span>
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
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const router = useRouter()
const loading = ref(false)
const captchaImg = ref('')
const registerFormRef = ref()

const registerForm = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  mobile: '',
  captcha: '',
  captchaId: ''
})

// 手机号校验规则
const validateMobile = (rule, value, callback) => {
  if (!value || value === '') {
    callback()
    return
  }
  const mobileReg = /^1[3-9]\d{9}$/
  if (!mobileReg.test(value)) {
    callback(new Error('请输入正确的中国大陆手机号'))
  } else {
    callback()
  }
}

// 确认密码校验规则
const validateConfirmPassword = (rule, value, callback) => {
  if (value !== registerForm.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const registerRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度应为 3-20 位', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度应为 6-20 位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ],
  mobile: [{ validator: validateMobile, trigger: 'blur' }],
  captcha: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
}

const getCaptcha = async () => {
  captchaImg.value = ''
  try {
    const data = await request.get('/captcha/generate')
    captchaImg.value = data.imageBase64
    registerForm.captchaId = data.captchaId
  } catch (err) {}
}

const handleRegister = async () => {
  const valid = await registerFormRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    await request.post('/auth/register', registerForm)
    ElMessage.success('注册成功')
    router.push('/login')
  } catch (err) {
    getCaptcha()
  } finally {
    loading.value = false
  }
}

onMounted(getCaptcha)
</script>

<style scoped lang="scss">
.register-wrapper {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: var(--color-bg-base);
  position: relative;
  overflow: hidden;

  // 彝族纹饰风格背景
  .register-decoration {
    position: absolute;
    font-size: 15rem;
    color: var(--color-primary-subtle);
    font-family: 'Microsoft Yi Baiti';
    opacity: 0.5;
    z-index: 0;
    &.left {
      left: -50px;
      bottom: -50px;
    }
    &.right {
      right: -50px;
      top: -50px;
    }
  }

  .register-card {
    width: 420px;
    z-index: 1;
    border: 2px solid var(--color-border-default);
    box-shadow: var(--shadow-lg);

    .register-title {
      text-align: center;
      margin-bottom: 30px;
      .fire-text {
        color: var(--color-primary);
        margin: 0;
        font-size: 1.8rem;
      }
      .nuosu-sub {
        color: var(--color-secondary);
        margin-top: 10px;
        font-weight: 500;
      }
    }

    .captcha-container {
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: 12px;
      width: 380px;
      .captcha-input {
        flex: 1;
      }
      .captcha-img-box {
        width: 130px;
        height: 32px;
        border: 1px solid var(--color-border-default);
        cursor: pointer;
        border-radius: var(--radius-sm);
        overflow: hidden;
        flex-shrink: 0;
        img {
          width: 100%;
          height: 100%;
          display: block;
        }
      }
    }

    .submit-btn {
      width: 100%;
      height: 45px;
      font-size: 1.1rem;
      background-color: var(--color-primary);
      border: none;
      box-shadow: var(--shadow-glow);
      &:hover {
        background-color: var(--color-primary-hover);
        transform: translateY(-1px);
      }
    }

    .login-link {
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
