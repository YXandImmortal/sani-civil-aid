<template>
  <div class="home-container">
    <!-- 1. 核心搜索区 -->
    <div class="search-section">
      <h1 class="nuosu-title">
        {{ appStore.lang === 'zh' ? '民法典查询助手' : 'ꏓꂱꈄꏍꇈꑌꄜꄉ' }}
      </h1>
      <div class="search-input-wrapper">
        <el-input
          v-model="searchQuery"
          :placeholder="appStore.lang === 'zh' ? '输入法条编号或关键词...' : 'ꇬꏠꇈꑌ...'"
          size="large"
          clearable
          class="main-search-input"
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button :icon="Search" @click="handleSearch" :loading="loading" />
          </template>
        </el-input>
      </div>
    </div>

    <!-- 2. 分类卡片区 -->
    <div class="recommend-section">
      <el-row :gutter="20">
        <el-col :span="6" v-for="item in categories" :key="item.id">
          <el-card 
            shadow="hover" 
            :class="['cate-card', { active: currentCategoryId === item.id }]" 
            @click="fetchByCategory(item.id)"
          >
            <div class="card-content">
              <h3>{{ item.name_cn }}</h3>
              <p class="nuosu-sub">{{ item.name_nuosu }}</p>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 3. 搜索结果展示区 -->
    <div v-if="articleList.length > 0" class="result-section">
      <div class="result-header">
        <span class="count-tip">
          {{ appStore.lang === 'zh' ? '找到' : 'ꇈꑌꄜꄉ' }} 
          <b style="color: #409eff">{{ articleList.length }}</b> 
          {{ appStore.lang === 'zh' ? '条相关结果' : 'ꏠ' }}
        </span>
        <el-button link type="primary" @click="resetSearch">
          {{ appStore.lang === 'zh' ? '清空结果' : 'ꀋꄜꀐ' }}
        </el-button>
      </div>
      
      <!-- 修改为可点击的卡片 -->
      <el-card 
        v-for="item in articleList" 
        :key="item.id" 
        class="article-card clickable" 
        shadow="hover"
        @click="showDetail(item)"
      >
        <template #header>
          <div class="card-header-flex">
            <el-tag effect="plain">第 {{ item.articleNum }} 条</el-tag>
            <el-icon class="view-more"><Right /></el-icon>
          </div>
        </template>
        <div class="content-cn text-ellipsis">{{ item.contentCn }}</div>
        <el-divider border-style="dashed" />
        <div class="content-nuosu text-ellipsis">{{ item.contentNuosu }}</div>
      </el-card>
    </div>

    <!-- 4. 空状态 -->
    <div v-else-if="hasSearched" class="no-result">
      <el-empty :description="appStore.lang === 'zh' ? '未找到相关内容' : 'ꀋꄜꇈꑌꀐ'" />
    </div>

    <!-- 5. 优化 A：法条详情对话框 -->
    <el-dialog
      v-model="detailVisible"
      :title="appStore.lang === 'zh' ? '法条详情' : 'ꏓꂱꄜꄉ'"
      width="60%"
      destroy-on-close
      center
      class="article-detail-dialog"
    >
      <div v-if="selectedArticle" class="detail-body">
        <div class="detail-tag">
          <el-tag size="large" effect="dark">第 {{ selectedArticle.articleNum }} 条</el-tag>
        </div>
        
        <div class="section-title">【 {{ appStore.lang === 'zh' ? '汉文原文' : 'ꇩꉙ' }} 】</div>
        <div class="full-content-cn">{{ selectedArticle.contentCn }}</div>
        
        <el-divider>
          <el-icon><Reading /></el-icon>
        </el-divider>
        
        <div class="section-title">【 {{ appStore.lang === 'zh' ? '彝文原文' : 'ꆈꌠꉙ' }} 】</div>
        <div class="full-content-nuosu">{{ selectedArticle.contentNuosu }}</div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useAppStore } from '@/stores/app'
import { Search, Right, Reading } from '@element-plus/icons-vue'
import request from '@/utils/request'

const appStore = useAppStore()
const searchQuery = ref('')
const loading = ref(false)
const hasSearched = ref(false)
const articleList = ref([])
const currentCategoryId = ref(null)

// 详情弹窗相关
const detailVisible = ref(false)
const selectedArticle = ref(null)

const categories = [
  { id: 1, name_cn: '总则编', name_nuosu: 'ꏓꂱꈄꏍꑼꂠ' },
  { id: 2, name_cn: '物权编', name_nuosu: 'ꏇꃅꑼꂠ' },
  { id: 3, name_cn: '合同编', name_nuosu: 'ꄧꄉꑼꂠ' },
  { id: 4, name_cn: '婚姻家庭', name_nuosu: 'ꊇꇅꇉꄧ' }
]

const handleSearch = async () => {
  if (!searchQuery.value) return
  loading.value = true
  currentCategoryId.value = null
  try {
    const data = await request.get('/civil/article/search', {
      params: { keyword: searchQuery.value }
    })
    articleList.value = data
    hasSearched.value = true
  } finally {
    loading.value = false
  }
}

const fetchByCategory = async (id) => {
  loading.value = true
  currentCategoryId.value = id
  searchQuery.value = ''
  try {
    const data = await request.get(`/civil/article/category/${id}`)
    articleList.value = data
    hasSearched.value = true
  } finally {
    loading.value = false
  }
}

const showDetail = (article) => {
  selectedArticle.value = article
  detailVisible.value = true
}

const resetSearch = () => {
  articleList.value = []
  hasSearched.value = false
  currentCategoryId.value = null
  searchQuery.value = ''
}
</script>

<style scoped lang="scss">
.home-container {
  max-width: 1000px;
  margin: 30px auto;
  .nuosu-title { text-align: center; font-size: 2.5rem; margin-bottom: 40px; }
  
  .search-input-wrapper {
    display: flex; justify-content: center; margin-bottom: 40px;
    .main-search-input { width: 600px; }
  }

  .recommend-section {
    margin-bottom: 50px;
    .cate-card {
      text-align: center; cursor: pointer; transition: all 0.3s;
      &.active { border-color: #409eff; background-color: #f0f7ff; }
      h3 { margin: 0; color: #409eff; font-size: 1rem; }
      .nuosu-sub { margin-top: 8px; color: #909399; font-size: 0.8rem; }
    }
  }

  .result-section {
    .result-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; }
    .article-card {
      margin-bottom: 15px;
      &.clickable { cursor: pointer; transition: transform 0.2s; &:hover { transform: scale(1.01); border-color: #409eff; } }
      .card-header-flex { display: flex; justify-content: space-between; align-items: center; .view-more { color: #c0c4cc; } }
      .text-ellipsis { overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
      .content-cn { font-size: 1.05rem; color: #303133; }
      .content-nuosu { font-size: 1.25rem; color: #606266; }
    }
  }

  // 详情弹窗样式
  .detail-body {
    padding: 10px 20px;
    .detail-tag { margin-bottom: 25px; text-align: center; }
    .section-title { font-weight: bold; color: #909399; margin-bottom: 10px; font-size: 0.9rem; }
    .full-content-cn { font-size: 1.2rem; line-height: 1.8; color: #2c3e50; margin-bottom: 30px; text-align: justify; }
    .full-content-nuosu { font-size: 1.5rem; line-height: 2.2; color: #34495e; text-align: justify; font-family: "Microsoft Yi Baiti", serif; }
  }
}
</style>