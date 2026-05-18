<template>
  <div class="vocabulary-container">
    <div class="page-header">
      <h2 class="main-title">
        <span class="yi-bilingual">
          <span>民法典常用词汇对照表</span>
          <span class="yi-placeholder">伱亞倫嚱侙儔勦咡儶啗厡匢僢垎</span>
        </span>
      </h2>
      <el-input
        v-model="searchKeyword"
        placeholder="搜索词汇..."
        class="custom-search"
        clearable
        @input="fetchTerms"
      >
        <template #prefix><el-icon><Search /></el-icon></template>
      </el-input>
    </div>

    <div class="grid-wrapper" v-loading="loading">
      <el-empty v-if="!loading && termList.length === 0">
        <span class="yi-bilingual">
          <span>暂无词汇数据</span>
          <span class="yi-placeholder">圐儔冨</span>
        </span>
      </el-empty>

      <div v-else class="vocab-grid">
        <div
          v-for="(item, index) in termList"
          :key="index"
          class="vocab-card"
        >
          <div class="term-name-cn">{{ item.termCn }}</div>
          <el-divider border-style="dashed" class="card-divider" />
          <div class="term-name-sani">{{ item.termSani }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue'
import {useAppStore} from '@/stores/app'
import {Search} from '@element-plus/icons-vue'
import request from '@/utils/request'

const appStore = useAppStore()
const searchKeyword = ref('')
const termList = ref([])
const loading = ref(false)

const fetchTerms = async () => {
  loading.value = true
  try {
    termList.value = await request.get('/civil/term/list', {
      params: {keyword: searchKeyword.value}
    })
  } finally {
    loading.value = false
  }
}

onMounted(fetchTerms)
</script>

<style scoped lang="scss">
.vocabulary-container {
  padding: 10px;
  color: var(--color-text-primary);

  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 24px;
    .main-title { color: var(--color-text-primary); margin: 0; }
  }

  // 搜索框去蓝
  .custom-search {
    width: 320px;
    :deep(.el-input__wrapper) {
      background-color: var(--color-bg-elevated);
      box-shadow: 0 0 0 1px var(--color-border-default) inset;
      &.is-focus { box-shadow: 0 0 0 1px var(--color-primary) inset !important; }
    }
    :deep(.el-input__icon) { color: var(--color-secondary); }
  }

  // 网格容器
  .grid-wrapper {
    background-color: var(--color-bg-elevated);
    border-radius: var(--radius-md);
    border: 1px solid var(--color-border-default);
    padding: 20px;
    min-height: 300px;
  }

  .vocab-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 16px;
  }

  .vocab-card {
    background-color: var(--color-bg-elevated);
    border: 1px solid var(--color-border-default);
    border-radius: var(--radius-md);
    padding: 20px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    text-align: center;
    gap: 10px;
    transition: all 0.3s ease;
    cursor: default;

    &:hover {
      border-color: var(--color-primary);
      transform: translateY(-4px);
      box-shadow: var(--shadow-md);
    }

    .term-name-cn {
      color: var(--color-primary);
      font-weight: 600;
      font-size: 1.1rem;
      line-height: 1.4;
      word-break: break-all;
    }

    .card-divider {
      margin: 0;
      width: 40%;
      :deep(.el-divider__text) { display: none; }
    }

    .term-name-sani {
      color: var(--color-secondary);
      font-size: calc(var(--yi-font-size) * 1.2);
      font-family: var(--yi-font-family);
      line-height: var(--yi-line-height);
      word-break: break-all;
    }
  }

  // 响应式：大屏 3 列
  @media (max-width: 1200px) {
    .vocab-grid { grid-template-columns: repeat(3, 1fr); }
  }

  // 响应式：平板 2 列
  @media (max-width: 768px) {
    .vocab-grid { grid-template-columns: repeat(2, 1fr); }
    .custom-search { width: 220px; }
  }

  // 响应式：移动端 1 列
  @media (max-width: 480px) {
    .vocab-grid { grid-template-columns: 1fr; }
    .page-header {
      flex-direction: column;
      align-items: flex-start;
      gap: 12px;
      .custom-search { width: 100%; }
    }
  }
}
</style>
