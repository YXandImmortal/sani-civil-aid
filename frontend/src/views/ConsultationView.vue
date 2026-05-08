<template>
  <div class="consultation-container">
    <div class="page-header">
      <h2 class="nuosu-font">{{ appStore.lang === 'zh' ? '法律咨询' : 'ꊇꇅꇉꄧ' }}</h2>
      <el-button type="primary" @click="dialogVisible = true">
        {{ appStore.lang === 'zh' ? '我要提问' : 'ꄜꀋꄧꄉ' }}
      </el-button>
    </div>

    <!-- 咨询历史列表 -->
    <div class="history-list" v-loading="loading">
      <el-empty v-if="historyList.length === 0" :description="appStore.lang === 'zh' ? '暂无咨询记录' : 'ꀋꄜꇅꇈꀐ'" />
      
      <el-card v-for="item in historyList" :key="item.id" class="history-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <el-tag :type="item.status === 1 ? 'success' : 'warning'" effect="dark">
              {{ item.status === 1 ? (appStore.lang === 'zh' ? '已回答' : 'ꇅꇈꀐ') : (appStore.lang === 'zh' ? '待回答' : 'ꇬꄜꀐ') }}
            </el-tag>
            <span class="time">{{ formatDate(item.createTime) }}</span>
          </div>
        </template>
        
        <!-- 问题区 -->
        <div class="chat-item question">
          <div class="symbol q-symbol">Q</div>
          <div class="content">
            <p class="cn">{{ item.questionCn }}</p>
            <p class="nuosu">{{ item.questionNuosu }}</p>
          </div>
        </div>

        <!-- 回答区 (修正后的布局) -->
        <div v-if="item.status === 1" class="answer-wrapper">
          <el-divider border-style="dashed" />
          <div class="chat-item answer">
            <div class="symbol a-symbol">A</div>
            <div class="content">
              <p class="cn">{{ item.answerCn }}</p>
              <p class="nuosu">{{ item.answerNuosu }}</p>
            </div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 提问对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="appStore.lang === 'zh' ? '提交咨询' : 'ꊇꇅꇉꄧꄧꄉ'"
      width="550px"
      destroy-on-close
    >
      <el-form :model="form" label-position="top">
        <el-form-item :label="appStore.lang === 'zh' ? '汉文描述' : 'ꇩꉙꊇꇅ'">
          <el-input 
            v-model="form.questionCn" 
            type="textarea" 
            :rows="4" 
            placeholder="请详细描述您的问题..." 
          />
        </el-form-item>
        <el-form-item :label="appStore.lang === 'zh' ? '彝文描述' : 'ꆈꌠꉙꊇꇅ'">
          <el-input 
            v-model="form.questionNuosu" 
            type="textarea" 
            :rows="4" 
            placeholder="请使用规范彝文输入..." 
            class="nuosu-textarea"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">{{ appStore.lang === 'zh' ? '取消' : 'ꀋꄜ' }}</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">
          {{ appStore.lang === 'zh' ? '提交' : 'ꄧꄉ' }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { useAppStore } from '@/stores/app'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

const appStore = useAppStore()
const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const historyList = ref([])

const form = reactive({
  questionCn: '',
  questionNuosu: ''
})

const fetchHistory = async () => {
  loading.value = true
  try {
    const data = await request.get('/civil/consultation/my-list')
    historyList.value = data
  } catch (err) {
    console.error('获取历史失败', err)
  } finally {
    loading.value = false
  }
}

const handleSubmit = async () => {
  if (!form.questionCn && !form.questionNuosu) {
    ElMessage.warning('请至少填写一种语言的问题')
    return
  }
  submitting.value = true
  try {
    await request.post('/civil/consultation/submit', form)
    ElMessage.success('咨询已提交')
    dialogVisible.value = false
    form.questionCn = ''
    form.questionNuosu = ''
    fetchHistory()
  } finally {
    submitting.value = false
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleString()
}

onMounted(fetchHistory)
</script>

<style scoped lang="scss">
.consultation-container {
  padding: 20px;
  max-width: 1000px;
  margin: 0 auto;

  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 25px;
    h2 { font-size: 1.8rem; color: #303133; }
  }

  .history-card {
    margin-bottom: 25px;
    border-radius: 10px;
    
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      .time { color: #909399; font-size: 0.85rem; }
    }

    .chat-item {
      display: flex;
      gap: 20px;
      margin: 15px 0;

      .symbol {
        width: 32px;
        height: 32px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-weight: bold;
        color: white;
        flex-shrink: 0; // 防止符号被挤压
      }
      
      .q-symbol { background-color: #409eff; }
      .a-symbol { background-color: #67c23a; }

      .content {
        flex: 1;
        .cn { 
          font-size: 1.05rem; 
          color: #303133; 
          margin-bottom: 8px; 
          line-height: 1.6;
        }
        .nuosu { 
          font-size: 1.25rem; 
          color: #606266; 
          line-height: 1.8;
          font-family: "Microsoft Yi Baiti", serif;
        }
      }
    }

    .answer-wrapper {
      .el-divider { margin: 20px 0; }
    }
  }
}

// 针对弹窗中输入框的样式优化
:deep(.nuosu-textarea .el-textarea__inner) {
  font-family: "Microsoft Yi Baiti", serif;
  font-size: 1.2rem;
  line-height: 1.6;
}
</style>