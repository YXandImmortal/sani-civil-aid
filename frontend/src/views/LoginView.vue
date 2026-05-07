<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="login-header">
          <h2>nuosu-civil-aid</h2>
          <p>ꆈꌠꏓꂱꈄꏍꇈꑌꄜꄉꐥ</p>
        </div>
      </template>
      <el-form :model="loginForm" label-position="top">
        <el-form-item label="用户名 / ꑱꆹꑼꂠ">
          <el-input v-model="loginForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码 / ꇬꑼ">
          <el-input v-model="loginForm.password" type="password" show-password />
        </el-form-item>
        <el-button type="primary" class="login-btn" @click="handleLogin">登录 / ꇩꄜ</el-button>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const loginForm = reactive({ username: '', password: '' })

const handleLogin = () => {
  // 这里暂时做简单的跳转逻辑
  router.push('/home')
}
</script>

<style scoped lang="scss">
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #1d2b64 0%, #f8f9fa 100%);
  
  .login-card {
    width: 400px;
    .login-header {
      text-align: center;
      h2 { margin: 0; color: #409eff; }
      p { color: #909399; font-size: 0.9rem; margin-top: 5px; }
    }
    .login-btn { width: 100%; margin-top: 20px; }
  }
}
</style>