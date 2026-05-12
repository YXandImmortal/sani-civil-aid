<template>
  <div class="profile-page">
    <el-row :gutter="24">
      <!-- 左侧：基本信息卡片 -->
      <el-col :xs="24" :md="14">
        <el-card class="profile-card">
          <template #header>
            <div class="card-header">
              <el-icon><User /></el-icon>
              <span class="yi-bilingual">
                <span>基本信息</span>
                <span class="yi-placeholder">[彝文占位符]</span>
              </span>
            </div>
          </template>

          <el-form
            ref="infoFormRef"
            :model="infoForm"
            :rules="infoRules"
            label-position="top"
          >
            <el-form-item prop="username">
              <template #label>
                <span class="yi-bilingual">
                  <span>用户名</span>
                  <span class="yi-placeholder">[彝文占位符]</span>
                </span>
              </template>
              <el-input v-model="infoForm.username" placeholder="请输入用户名" />
            </el-form-item>

            <el-form-item prop="mobile">
              <template #label>
                <span class="yi-bilingual">
                  <span>手机号</span>
                  <span class="yi-placeholder">[彝文占位符]</span>
                </span>
              </template>
              <el-input v-model="infoForm.mobile" placeholder="请输入手机号" />
            </el-form-item>

            <el-form-item>
              <template #label>
                <span class="yi-bilingual">
                  <span>角色</span>
                  <span class="yi-placeholder">[彝文占位符]</span>
                </span>
              </template>
              <el-input :model-value="userStore.userInfo?.roleName" disabled />
            </el-form-item>

            <el-button
              type="primary"
              :loading="infoLoading"
              @click="handleUpdateInfo"
              class="submit-btn"
            >
              <span class="yi-bilingual">
                <span>保存修改</span>
                <span class="yi-placeholder">[彝文占位符]</span>
              </span>
            </el-button>
          </el-form>
        </el-card>
      </el-col>

      <!-- 右侧：修改密码卡片 -->
      <el-col :xs="24" :md="10">
        <el-card class="profile-card">
          <template #header>
            <div class="card-header">
              <el-icon><Lock /></el-icon>
              <span class="yi-bilingual">
                <span>修改密码</span>
                <span class="yi-placeholder">[彝文占位符]</span>
              </span>
            </div>
          </template>

          <el-form
            ref="pwdFormRef"
            :model="pwdForm"
            :rules="pwdRules"
            label-position="top"
          >
            <el-form-item prop="oldPassword">
              <template #label>
                <span class="yi-bilingual">
                  <span>原密码</span>
                  <span class="yi-placeholder">[彝文占位符]</span>
                </span>
              </template>
              <el-input
                v-model="pwdForm.oldPassword"
                type="password"
                show-password
                placeholder="请输入原密码"
              />
            </el-form-item>

            <el-form-item prop="newPassword">
              <template #label>
                <span class="yi-bilingual">
                  <span>新密码</span>
                  <span class="yi-placeholder">[彝文占位符]</span>
                </span>
              </template>
              <el-input
                v-model="pwdForm.newPassword"
                type="password"
                show-password
                placeholder="请输入新密码"
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
                v-model="pwdForm.confirmPassword"
                type="password"
                show-password
                placeholder="请再次输入新密码"
              />
            </el-form-item>

            <el-button
              type="primary"
              :loading="pwdLoading"
              @click="handleChangePassword"
              class="submit-btn"
            >
              <span class="yi-bilingual">
                <span>确认修改</span>
                <span class="yi-placeholder">[彝文占位符]</span>
              </span>
            </el-button>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'

const userStore = useUserStore()

/* ================= 基本信息 ================= */
const infoFormRef = ref()
const infoLoading = ref(false)
const infoForm = reactive({
  username: '',
  mobile: ''
})

const infoRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  mobile: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ]
}

const handleUpdateInfo = async () => {
  const valid = await infoFormRef.value.validate().catch(() => false)
  if (!valid) return

  infoLoading.value = true
  try {
    await userStore.updateInfo(infoForm)
    ElMessage.success('个人信息修改成功')
  } catch (err) {
    // request.js 已统一处理错误提示
  } finally {
    infoLoading.value = false
  }
}

/* ================= 修改密码 ================= */
const pwdFormRef = ref()
const pwdLoading = ref(false)
const pwdForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== pwdForm.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const pwdRules = {
  oldPassword: [
    { required: true, message: '请输入原密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const handleChangePassword = async () => {
  const valid = await pwdFormRef.value.validate().catch(() => false)
  if (!valid) return

  pwdLoading.value = true
  try {
    await userStore.changePassword(pwdForm)
    ElMessage.success('密码修改成功，请重新登录')
    // 清空密码表单
    pwdFormRef.value.resetFields()
  } catch (err) {
    // request.js 已统一处理错误提示
  } finally {
    pwdLoading.value = false
  }
}

/* ================= 初始化 ================= */
onMounted(() => {
  if (userStore.userInfo) {
    infoForm.username = userStore.userInfo.username || ''
    infoForm.mobile = userStore.userInfo.mobile || ''
  }
})
</script>

<style scoped lang="scss">
.profile-page {
  .profile-card {
    border: 1px solid var(--color-border-default);
    box-shadow: var(--shadow-sm);

    .card-header {
      display: flex;
      align-items: center;
      gap: 8px;
      font-weight: 600;
      font-size: 1.05rem;
      color: var(--color-text-primary);

      .el-icon {
        color: var(--color-primary);
      }
    }

    .submit-btn {
      width: 100%;
      height: 40px;
      margin-top: 8px;
      background-color: var(--color-primary);
      border: none;

      &:hover {
        background-color: var(--color-primary-hover);
      }
    }
  }
}
</style>
