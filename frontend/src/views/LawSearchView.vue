<template>
  <div class="home-container">
    <!-- 1. 核心搜索区 -->
    <div class="search-section">
      <h1 class="sani-title main-title-text">
        <span class="yi-bilingual">
          <span>民法典查询</span>
          <span class="yi-placeholder">伱亞倫仴匢</span>
        </span>
      </h1>
      <div class="search-input-wrapper">
        <el-input
          v-model="searchQuery"
          placeholder="输入法条编号或关键词..."
          size="large"
          clearable
          class="main-search-input"
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button :icon="Search" @click="handleSearch" :loading="loading || aiLoading" />
          </template>
        </el-input>
      </div>
    </div>

    <!-- 2. 分类卡片区 -->
    <div v-if="!hasSearched" class="recommend-section">
      <el-row :gutter="20">
        <el-col :span="6" v-for="item in categories" :key="item.id">
          <el-card 
            shadow="hover" 
            :class="['cate-card', { 'is-active': currentCategoryId === item.id }]" 
            @click="fetchByCategory(item.id)"
          >
            <div class="card-content">
              <h3>{{ item.name_cn }}</h3>
              <p class="sani-sub-text yi-placeholder">{{ item.name_sani }}</p>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 3. 本地搜索结果 -->
    <div v-if="articleList.length > 0" class="result-section">
      <div class="result-header">
        <span class="count-tip">
          <span class="yi-bilingual">
            <span>找到</span>
            <span class="yi-placeholder">儺厫</span>
          </span>
          <span class="highlight-num">{{ articleList.length }}</span>
          <span class="yi-bilingual">
            <span>条官方收录结果</span>
            <span class="yi-placeholder">倱乻嗠冃圽劼凴</span>
          </span>
        </span>
        <el-button link class="clear-btn" @click="resetSearch">
          <span class="yi-bilingual">
            <span>清空</span>
            <span class="yi-placeholder">哃凖</span>
          </span>
        </el-button>
      </div>
      <el-card v-for="item in articleList" :key="item.id" class="article-card" @click="showDetail(item)">
        <template #header>
          <el-tag class="article-tag">
            <span class="yi-bilingual">
              <span>第 {{ item.articleNum }} 条</span>
              <span class="yi-placeholder">喕 {{ item.articleNum }} 倱</span>
            </span>
          </el-tag>
        </template>
        <div class="content-cn">{{ item.contentCn }}</div>
        <el-divider border-style="dashed" />
        <div class="content-sani">{{ item.contentSani }}</div>
      </el-card>
    </div>

    <!-- 4. AI 搜索结果区 (核心新增) -->
    <div v-if="aiArticle" class="ai-section">
      <el-alert
        type="warning"
        show-icon
        :closable="false"
        class="ai-alert"
      >
        <span class="yi-bilingual">
          <span>此法条当前未收录，正在进行AI检索，内容仅供参考</span>
          <span class="yi-placeholder">凎亞倱囒冮勦冃儔圽劼，AI僎儺俷儶，佈儶凴匢僢喞圱圐</span>
        </span>
      </el-alert>
      <el-card class="article-card ai-card" v-loading="aiLoading">
        <template #header>
          <div class="card-header-flex">
            <el-tag type="danger" effect="dark">
              <span class="yi-bilingual">
                <span>DeepSeek AI 检索结果</span>
                <span class="yi-placeholder">DeepSeek AI 僎儺兌凴</span>
              </span>
            </el-tag>
            <span class="ai-tip-text">
              <span class="yi-bilingual">
                <span>智能生成</span>
                <span class="yi-placeholder">佱厡喞圇冝</span>
              </span>
            </span>
          </div>
        </template>
        <div class="content-cn">{{ aiArticle.contentCn }}</div>
        <el-divider border-style="dashed" />
        <div class="content-sani">{{ aiArticle.contentSani }}</div>
      </el-card>
    </div>

    <!-- 5. 详情对话框 -->
    <el-dialog v-model="detailVisible" width="60%" class="custom-dialog">
      <template #header>
        <span class="yi-bilingual">
          <span>法条详情</span>
          <span class="yi-placeholder">亞倱嚜係</span>
        </span>
      </template>
      <div v-if="selectedArticle" class="detail-body">
        <el-tag size="large" class="article-tag-dark">
          <span class="yi-bilingual">
            <span>第 {{ selectedArticle.articleNum }} 条</span>
            <span class="yi-placeholder">喕 {{ selectedArticle.articleNum }} 倱</span>
          </span>
        </el-tag>
        <p class="dialog-text-cn">{{ selectedArticle.contentCn }}</p>
        <el-divider class="dialog-divider"><el-icon><Reading /></el-icon></el-divider>
        <p class="dialog-text-sani">{{ selectedArticle.contentSani }}</p>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref} from 'vue'
import {useAppStore} from '@/stores/app'
import {Reading, Search} from '@element-plus/icons-vue'
import request from '@/utils/request'
import {ElMessage} from 'element-plus'

const appStore = useAppStore()
const searchQuery = ref('')
const loading = ref(false)
const aiLoading = ref(false)
const hasSearched = ref(false)
const articleList = ref([])
const aiArticle = ref(null) // 存储 AI 结果
const currentCategoryId = ref(null)
const detailVisible = ref(false)
const selectedArticle = ref(null)

const categories = [
  { id: 1, name_cn: '总则编', name_sani: '冭偯坸' },
  { id: 2, name_cn: '物权编', name_sani: '僌劰坸' },
  { id: 3, name_cn: '合同编', name_sani: '哛儜坸' },
  { id: 4, name_cn: '婚姻家庭', name_sani: '嘕亸厛偔' }
]

// 核心搜索逻辑：先本地，后AI
const handleSearch = async () => {
  if (!searchQuery.value) return
  
  // 重置状态
  resetSearchState()
  loading.value = true
  hasSearched.value = true

  try {
    // 1. 尝试本地检索
    const localData = await request.get('/civil/article/search', { params: { keyword: searchQuery.value } })
    
    if (localData && localData.length > 0) {
      articleList.value = localData
    } else {
      // 2. 本地无内容，启动 AI 检索
      callAiSearch()
    }
  } catch (err) {
    ElMessage.error('搜索请求失败')
  } finally {
    loading.value = false
  }
}

const callAiSearch = async () => {
  aiLoading.value = true
  aiArticle.value = { contentCn: '正在分析法律条文...', contentSani: '...' }
  
  try {
    const data = await request.get('/civil/article/ai-search', { params: { keyword: searchQuery.value } })
    // 解析后端传回的 JSON 字符串
    aiArticle.value = JSON.parse(data.aiResult)
  } catch (err) {
    aiArticle.value = null
    ElMessage.error('AI 检索超时，请稍后再试')
  } finally {
    aiLoading.value = false
  }
}

const fetchByCategory = async (id) => {
  resetSearchState()
  loading.value = true
  hasSearched.value = true
  currentCategoryId.value = id
  try {
    articleList.value = await request.get(`/civil/article/category/${id}`)
  } finally { loading.value = false }
}

const resetSearchState = () => {
  articleList.value = []
  aiArticle.value = null
  currentCategoryId.value = null
}

const resetSearch = () => {
  resetSearchState()
  hasSearched.value = false
  searchQuery.value = ''
}

const showDetail = (article) => {
  selectedArticle.value = article
  detailVisible.value = true
}
</script>

<style scoped lang="scss">
.home-container {
  max-width: 1000px;
  margin: 30px auto;
  
  .search-section { text-align: center; margin-bottom: 50px; .sani-title { font-size: 2.5rem; margin-bottom: 30px; } }

  .main-search-input {
    :deep(.el-input__wrapper) { background-color: var(--color-bg-inset); box-shadow: 0 0 0 1px var(--color-border-default) inset; &.is-focus { box-shadow: 0 0 0 1px var(--color-primary) inset !important; } }
    :deep(.el-input-group__append) { background-color: var(--color-primary); color: var(--color-text-inverse); border-color: var(--color-primary); }
  }

  .recommend-section { margin-bottom: 50px; .cate-card { text-align: center; cursor: pointer; transition: all 0.3s; h3 { color: var(--color-secondary); margin: 0; } .sani-sub-text { margin-top: 10px; color: var(--color-text-secondary); font-family: var(--yi-font-family); } &.is-active { border-color: var(--color-primary) !important; background-color: var(--color-primary-light); } &:hover { border-color: var(--color-secondary); transform: translateY(-5px); } } }

  .result-section, .ai-section {
    .result-header { margin-bottom: 15px; display: flex; justify-content: space-between; .highlight-num { color: var(--color-primary); font-weight: bold; } .clear-btn { color: var(--color-silver-dark); } }
    .ai-alert { margin-bottom: 20px; border: 1px solid var(--color-warning); background-color: var(--color-secondary-light); color: var(--color-secondary-hover); }
  }

  .article-card {
    background-color: var(--color-bg-elevated); border: 1px solid var(--color-border-default); margin-bottom: 16px; cursor: pointer;
    &:hover { border-color: var(--color-primary); }
    .article-tag { background-color: var(--color-secondary-light); color: var(--color-secondary-hover); border-color: var(--color-secondary); }
    .content-cn { font-size: 1.05rem; line-height: 1.6; color: var(--color-text-primary); }
    .content-sani { font-size: calc(var(--yi-font-size) * 1.2); line-height: var(--yi-line-height); color: var(--color-text-secondary); font-family: var(--yi-font-family); }
  }

  .ai-card { border: 2px solid var(--color-primary); .card-header-flex { display: flex; justify-content: space-between; align-items: center; .ai-tip-text { font-size: 0.8rem; color: var(--color-text-tertiary); } } .content-sani { font-family: var(--yi-font-family); } }

  :deep(.custom-dialog) {
    background-color: var(--color-bg-elevated); .el-dialog__title { color: var(--color-primary); font-weight: bold; }
    .dialog-text-cn { font-size: 1.1rem; line-height: 1.8; color: var(--color-text-primary); }
    .dialog-text-sani { font-size: calc(var(--yi-font-size) * 1.4); line-height: var(--yi-line-height); color: var(--color-text-secondary); font-family: var(--yi-font-family); }
    .article-tag-dark { background-color: var(--color-primary); border: none; margin-bottom: 20px; }
  }
}
</style>
