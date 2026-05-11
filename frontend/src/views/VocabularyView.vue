<template>
  <div class="vocabulary-container">
    <div class="page-header">
      <h2 class="main-title">
        <span class="yi-bilingual">
          <span>民法典常用词汇对照表</span>
          <span class="yi-placeholder">[彝文占位符]</span>
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

    <div class="table-wrapper">
      <el-table
          :data="termList"
          v-loading="loading"
          class="custom-table"
          height="700px"
      >
        <el-table-column width="180">
          <template #header>
            <span class="yi-bilingual">
              <span>汉语词汇</span>
              <span class="yi-placeholder">[彝文占位符]</span>
            </span>
          </template>
          <template #default="scope">
            <span class="term-name-cn">{{ scope.row.termCn }}</span>
          </template>
        </el-table-column>
        
        <el-table-column min-width="280">
          <template #header>
            <span class="yi-bilingual">
              <span>彝语对照</span>
              <span class="yi-placeholder">[彝文占位符]</span>
            </span>
          </template>
          <template #default="scope">
            <span class="nuosu-font term-name-nuosu">{{ scope.row.termNuosu }}</span>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useAppStore } from '@/stores/app'
import { Search } from '@element-plus/icons-vue'
import request from '@/utils/request'

const appStore = useAppStore()
const searchKeyword = ref('')
const termList = ref([])
const loading = ref(false)

const fetchTerms = async () => {
  loading.value = true
  try {
    const data = await request.get('/civil/term/list', {
      params: { keyword: searchKeyword.value }
    })
    termList.value = data
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

  // 表格深度定制
  .table-wrapper {
    background-color: var(--color-bg-elevated);
    border-radius: var(--radius-md);
    border: 1px solid var(--color-border-default);
    overflow: hidden;
  }

  :deep(.custom-table) {
    --el-table-border-color: var(--color-border-subtle);
    --el-table-header-bg-color: var(--color-bg-inset);
    --el-table-row-hover-bg-color: var(--color-primary-light);
    background-color: transparent;

    th.el-table__cell {
      color: var(--color-text-secondary);
      font-weight: bold;
      border-bottom: 2px solid var(--color-border-default);
    }

    .term-name-cn { color: var(--color-primary); font-weight: 600; white-space: nowrap; }
    .term-name-nuosu { color: var(--color-secondary); font-size: 1.2rem; font-family: var(--yi-font-family); }
  }
}
</style>
