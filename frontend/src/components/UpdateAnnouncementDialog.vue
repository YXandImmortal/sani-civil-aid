<template>
  <el-dialog
    v-model="visible"
    width="480px"
    :show-close="false"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    class="announcement-dialog"
    align-center
  >
    <div class="announcement-header">
      <div class="version-tag">
        <el-icon><Bell /></el-icon>
        <span>{{ announcement.currentVersion }}</span>
      </div>
      <h2 class="announcement-title">{{ announcement.title }}</h2>
    </div>

    <div class="announcement-body">
      <pre class="announcement-content">{{ announcement.content }}</pre>
    </div>

    <template #footer>
      <div class="announcement-footer">
        <el-button type="primary" class="confirm-btn" @click="handleConfirm">
          <span class="yi-bilingual">
            <span>我知道了</span>
            <span class="yi-placeholder">僔嚝圽乴</span>
          </span>
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch } from 'vue'
import { Bell } from '@element-plus/icons-vue'
import { getAnnouncement, markAnnouncementRead } from '@/api/system'
import { ElMessage } from 'element-plus'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:modelValue'])

const visible = ref(false)
const announcement = ref({
  currentVersion: '',
  title: '',
  content: '',
  needShow: false
})

watch(() => props.modelValue, (val) => {
  visible.value = val
})

watch(visible, (val) => {
  emit('update:modelValue', val)
})

/**
 * 拉取公告并决定是否展示
 */
async function fetchAndShow() {
  try {
    const data = await getAnnouncement()
    if (data && data.needShow) {
      announcement.value = data
      visible.value = true
    }
  } catch {
    // 静默失败，不影响用户正常使用
  }
}

/**
 * 确认已读
 */
async function handleConfirm() {
  try {
    await markAnnouncementRead()
  } catch {
    ElMessage.warning('标记已读失败，请稍后重试')
    return
  }
  visible.value = false
}

defineExpose({
  fetchAndShow
})
</script>

<style scoped lang="scss">
.announcement-header {
  text-align: center;
  padding: 8px 0 16px;

  .version-tag {
    display: inline-flex;
    align-items: center;
    gap: 6px;
    padding: 4px 12px;
    border-radius: 20px;
    background-color: var(--color-primary-subtle);
    color: var(--color-primary);
    font-size: 14px;
    font-weight: 600;
    margin-bottom: 12px;
  }

  .announcement-title {
    margin: 0;
    font-size: 20px;
    color: var(--color-text-primary);
  }
}

.announcement-body {
  padding: 0 8px;

  .announcement-content {
    margin: 0;
    padding: 16px;
    background-color: var(--color-bg-elevated);
    border-radius: 8px;
    color: var(--color-text-primary);
    font-size: 14px;
    line-height: 1.8;
    white-space: pre-wrap;
    word-break: break-word;
    font-family: inherit;
  }
}

.announcement-footer {
  display: flex;
  justify-content: center;
  padding-top: 8px;

  .confirm-btn {
    min-width: 120px;
  }
}
</style>

<style lang="scss">
/* 覆盖 Dialog 默认样式 */
.announcement-dialog {
  .el-dialog__header {
    display: none;
  }

  .el-dialog__body {
    padding: 24px 24px 8px;
  }

  .el-dialog__footer {
    padding: 8px 24px 24px;
    border-top: none;
  }
}
</style>
