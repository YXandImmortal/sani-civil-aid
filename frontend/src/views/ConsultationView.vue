<template>
  <div class="consultation-container">
    <div class="page-header">
      <h2 class="main-title">
        <span class="yi-bilingual">
          <span>智能法律咨询</span>
          <span class="yi-placeholder">亞伿嚳侼佱厡</span>
        </span>
      </h2>
      <el-button type="primary" class="ask-btn" @click="openNewConsult">
        <el-icon style="margin-right: 5px"><ChatDotRound /></el-icon>
        <span class="yi-bilingual">
          <span>我要提问</span>
          <span class="yi-placeholder">哋儔侼嚳</span>
        </span>
      </el-button>
    </div>

    <!-- 咨询历史列表 -->
    <div class="history-list" v-loading="loading">
      <!-- 未登录提示 -->
      <el-empty v-if="!userStore.token" class="empty-not-login">
        <span class="yi-bilingual">
          <span>登录后可查看咨询历史</span>
          <span class="yi-placeholder">[彝文占位符]</span>
        </span>
        <el-button type="primary" class="login-btn" @click="router.push('/login')">
          <span class="yi-bilingual">
            <span>去登录</span>
            <span class="yi-placeholder">刱圽坖</span>
          </span>
        </el-button>
      </el-empty>

      <el-empty v-else-if="historyList.length === 0">
        <span class="yi-bilingual">
          <span>暂无咨询记录</span>
          <span class="yi-placeholder">嚳侼嗃僘儔儶</span>
        </span>
      </el-empty>
      
      <el-card v-for="item in historyList" :key="item.id" class="history-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <el-tag :class="item.status === 1 ? 'status-tag-done' : 'status-tag-pending'">
              <span v-if="item.status === 1" class="yi-bilingual">
                <span>已回答</span>
                <span class="text-sani font-yi-script">冨劼</span>
              </span>
              <span v-else class="yi-bilingual">
                <span>未回答</span>
                <span class="text-sani font-yi-script">圐儔冨</span>
              </span>
            </el-tag>
            <div class="header-right">
              <span class="time-text">{{ formatDate(item.createTime) }}</span>
              <el-button
                type="danger"
                link
                size="small"
                class="delete-btn"
                @click="handleDelete(item.id)"
              >
                <el-icon><Delete /></el-icon>
              </el-button>
            </div>
          </div>
        </template>
        
        <!-- 问题 -->
        <div class="chat-row question-row">
          <div class="avatar q-avatar font-yi-script">嚳</div>
          <div class="content-body">
            <p class="text-cn">{{ item.questionCn }}</p>
            <p :class="['text-sani', getSaniFontClass(item.saniFont)]">{{ item.questionSani }}</p>
          </div>
        </div>

        <!-- 回答 -->
        <div v-if="item.status === 1" class="answer-area">
          <el-divider class="custom-divider" />
          <div class="chat-row answer-row">
            <div class="avatar a-avatar font-yi-script">叀</div>
            <div class="content-body">
              <p class="text-cn">{{ item.answerCn }}</p>
              <p :class="['text-sani', getSaniFontClass(item.saniFont)]">{{ item.answerSani }}</p>
            </div>
          </div>
        </div>

        <!-- 未回答时显示重新咨询按钮 -->
        <div v-else class="reconsult-area">
          <el-divider class="custom-divider" />
          <el-button type="primary" link class="reconsult-btn" @click="openReConsult(item)">
            <el-icon style="margin-right: 4px"><RefreshRight /></el-icon>
            <span class="yi-bilingual">
              <span>重新咨询</span>
              <span class="yi-placeholder">嗢塿嚳侼</span>
            </span>
          </el-button>
        </div>
      </el-card>
    </div>

    <!-- 提问弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      width="550px"
      class="custom-dialog"
    >
      <template #header>
        <span class="yi-bilingual">
          <span>{{ dialogTitle }}</span>
          <span class="yi-placeholder">{{ dialogTitleSani }}</span>
        </span>
      </template>
      <el-form :model="form" label-position="top">
        <el-form-item>
          <template #label>
            <span class="yi-bilingual">
              <span>汉文问题描述</span>
              <span class="yi-placeholder">儔侼呅噅啗坸</span>
            </span>
          </template>
          <el-input v-model="form.questionCn" type="textarea" :rows="4" class="custom-input" />
        </el-form-item>
        <el-form-item>
          <template #label>
            <div class="label-with-action">
              <span class="yi-bilingual">
                <span>彝文问题描述</span>
                <span class="yi-placeholder">儔侼呅凲啗坸</span>
              </span>
              <el-button
                link
                type="primary"
                size="small"
                class="font-switch-btn"
                @click="toggleSaniFont"
              >
                <el-icon style="margin-right: 3px"><Switch /></el-icon>
                <span>{{ form.saniFont === 'Yi Script' ? 'Microsoft Yi Baiti' : 'Yi Script' }}</span>
              </el-button>
            </div>
          </template>
          <el-input
            v-model="form.questionSani"
            type="textarea"
            :rows="4"
            :class="['custom-input', 'sani-font', getSaniFontClass(form.saniFont)]"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false" class="cancel-btn">
          <span class="yi-bilingual">
            <span>取消</span>
            <span class="yi-placeholder">儔埇圌</span>
          </span>
        </el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting" class="submit-btn">
          <span class="yi-bilingual">
            <span>提交咨询</span>
            <span class="yi-placeholder">嚳侼剓叇</span>
          </span>
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {computed, onMounted, reactive, ref} from 'vue'
import {useUserStore} from '@/stores/user'
import request from '@/utils/request'
import {ElMessage, ElMessageBox} from 'element-plus'
import {ChatDotRound, Delete, RefreshRight, Switch} from '@element-plus/icons-vue'
import {useRouter} from 'vue-router'

const userStore = useUserStore()
const router = useRouter()
const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const historyList = ref([])
const form = reactive({ questionCn: '', questionSani: '', saniFont: 'Yi Script' })
const dialogTitle = ref('提交新咨询')
const dialogTitleSani = computed(() => isReConsult.value ? '嗢塿嚳侼' : '嚳侼嚔厡剓叇')
const isReConsult = ref(false)

const fetchHistory = async () => {
  if (!userStore.token) return
  loading.value = true
  try {
    historyList.value = await request.get('/civil/consultation/my-list')
  } finally { loading.value = false }
}

const getSaniFontClass = (font) => {
  if (font === 'Yi Script') return 'font-yi-script'
  if (font === 'Microsoft Yi Baiti') return 'font-microsoft-yi-baiti'
  return ''
}

const toggleSaniFont = () => {
  form.saniFont = form.saniFont === 'Yi Script' ? 'Microsoft Yi Baiti' : 'Yi Script'
}

const handleSubmit = async () => {
  if (!form.questionCn && !form.questionSani) return ElMessage.warning('请填写内容')
  submitting.value = true
  try {
    const data = await request.post('/civil/consultation/submit', form, { timeout: 60000 })
    // 已登录用户：将返回的数据插入列表前端
    if (data && userStore.token) {
      historyList.value.unshift(data)
    }
    if (!userStore.token) {
      ElMessage.success('咨询已提交，登录后可查看历史记录')
    } else {
      ElMessage.success(isReConsult.value ? '重新咨询已提交' : '咨询已提交')
    }
    dialogVisible.value = false
    form.questionCn = ''; form.questionSani = '';
    isReConsult.value = false
    dialogTitle.value = '提交新咨询'
  } finally { submitting.value = false }
}

const openNewConsult = () => {
  form.questionCn = ''
  form.questionSani = ''
  form.saniFont = 'Yi Script'
  isReConsult.value = false
  dialogTitle.value = '提交新咨询'
  dialogVisible.value = true
}

const openReConsult = (item) => {
  form.questionCn = item.questionCn || ''
  form.questionSani = item.questionSani || ''
  form.saniFont = item.saniFont || 'Yi Script'
  isReConsult.value = true
  dialogTitle.value = '重新咨询'
  dialogVisible.value = true
}

const formatDate = (dateStr) => dateStr ? new Date(dateStr).toLocaleString() : ''

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这条咨询记录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.delete(`/civil/consultation/delete/${id}`)
    historyList.value = historyList.value.filter(item => item.id !== id)
    ElMessage.success('删除成功')
  } catch (error) {
    if (error !== 'cancel') {
      // 错误已由 request 拦截器自动提示
    }
  }
}

onMounted(fetchHistory)
</script>

<style scoped lang="scss">
.consultation-container {
  padding: 10px;
  max-width: 1000px;
  margin: 0 auto;

  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 24px;
    .main-title { color: var(--color-text-primary); }
    .ask-btn { background-color: var(--color-primary); border: none; &:hover { background-color: var(--color-primary-hover); } }
  }

  .history-card {
    background-color: var(--color-bg-elevated);
    border: 1px solid var(--color-border-default);
    margin-bottom: 20px;
    border-radius: var(--radius-md);

    .card-header {
      display: flex; justify-content: space-between; align-items: center;
      .header-right {
        display: flex; align-items: center; gap: 8px;
      }
      .time-text { color: var(--color-text-tertiary); font-size: 0.85rem; }
      .delete-btn {
        color: var(--color-danger);
        &:hover { color: var(--color-danger-hover); }
      }
    }

    .reconsult-area {
      display: flex; justify-content: center; margin-top: 12px;
      .reconsult-btn {
        color: var(--color-primary);
        &:hover { color: var(--color-primary-hover); }
      }
    }

    .status-tag-done { background-color: var(--color-success); color: var(--color-text-inverse); border: none; height: auto; padding: 6px 12px; line-height: 1.4; }
    .status-tag-pending { background-color: var(--color-secondary); color: var(--color-text-inverse); border: none; height: auto; padding: 6px 12px; line-height: 1.4; }

    .chat-row {
      display: flex; gap: 16px; margin: 12px 0;
      .avatar {
        width: 32px; height: 32px; border-radius: 50%; display: flex;
        align-items: center; justify-content: center; font-weight: bold; color: var(--color-text-inverse); flex-shrink: 0;
      }
      .q-avatar { background-color: var(--color-primary); }
      .a-avatar { background-color: var(--color-secondary); }
      
      .content-body {
        flex: 1;
        .text-cn { color: var(--color-text-primary); line-height: 1.6; margin: 0 0 6px; }
        .text-sani { color: var(--color-text-secondary); line-height: 1.8; font-size: 1.2rem; margin: 0; }
      }
    }

    .custom-divider { border-top-color: var(--color-border-subtle); margin: 20px 0; }
  }

  // 弹窗与输入框深度定制
  :deep(.custom-dialog) {
    background-color: var(--color-bg-elevated);
    .el-dialog__title { color: var(--color-primary); font-weight: bold; }
    
    .el-form-item__label {
      display: flex !important;
      width: 100% !important;
      color: var(--color-text-secondary);
      font-weight: 500;
    }
    
    .custom-input .el-textarea__inner {
      background-color: var(--color-bg-inset);
      border: 1px solid var(--color-border-default);
      color: var(--color-text-primary);
      &:focus { border-color: var(--color-primary); }
    }
  }

  .cancel-btn { border-color: var(--color-border-default); color: var(--color-text-secondary); }
  .submit-btn { background-color: var(--color-primary); border: none; &:hover { background-color: var(--color-primary-hover); } }

  .label-with-action {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
  }

  .font-switch-btn {
    padding: 2px 6px;
    font-size: 0.8rem;
  }
}

:deep(.empty-not-login) {
  .el-empty__bottom {
    display: flex; flex-direction: column; gap: 12px; align-items: center;
  }
}
</style>
