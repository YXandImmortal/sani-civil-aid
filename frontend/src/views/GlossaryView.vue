<template>
  <div class="glossary-container">
    <div class="page-header">
      <h2 class="nuosu-font">{{ appStore.lang === 'zh' ? '法律术语词典' : 'ꄜꀋꇬꏠꇈꑌ' }}</h2>
      <el-input
        v-model="searchKeyword"
        :placeholder="appStore.lang === 'zh' ? '搜索术语...' : 'ꇬꏠꇈꑌ...'"
        style="width: 300px"
        clearable
        @input="fetchTerms"
      >
        <template #prefix><el-icon><Search /></el-icon></template>
      </el-input>
    </div>

    <el-table :data="termList" v-loading="loading" style="width: 100%; border-radius: 8px">
      <el-table-column :label="appStore.lang === 'zh' ? '汉文术语' : 'ꇩꉙꇬꏠ'" width="180">
        <template #default="scope">
          <b style="color: #409eff">{{ scope.row.termCn }}</b>
        </template>
      </el-table-column>
      <el-table-column :label="appStore.lang === 'zh' ? '彝文术语' : 'ꆈꌠꉙꇬꏠ'" width="200">
        <template #default="scope">
          <span class="nuosu-text">{{ scope.row.termNuosu }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="appStore.lang === 'zh' ? '术语解释' : 'ꇬꏠꄜꅉ'">
        <template #default="scope">
          <div class="definition-box">
            <p class="cn-def">{{ scope.row.definitionCn }}</p>
            <p class="nuosu-def">{{ scope.row.definitionNuosu }}</p>
          </div>
        </template>
      </el-table-column>
    </el-table>
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
.glossary-container {
  padding: 20px;
  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 30px;
    h2 { margin: 0; color: #303133; }
  }
  .nuosu-text { font-size: 1.2rem; color: #606266; }
  .definition-box {
    .cn-def { color: #303133; margin-bottom: 8px; line-height: 1.6; }
    .nuosu-def { color: #909399; font-size: 1.1rem; line-height: 1.8; }
  }
}
</style>