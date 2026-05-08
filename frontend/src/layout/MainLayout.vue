<template>
  <el-container class="layout-container">
    <el-aside width="240px">
      <div class="logo"><span>ꏓꂱꈄꏍ AID</span></div>
      <el-menu :default-active="activeMenu" class="el-menu-vertical" router>
        <el-menu-item index="/home">
          <el-icon><House /></el-icon>
          <span>{{ appStore.lang === 'zh' ? '首页' : 'ꀒꇑ' }}</span>
        </el-menu-item>
        <el-menu-item index="/home">
          <el-icon><Search /></el-icon>
          <span>{{ appStore.lang === 'zh' ? '法条查询' : 'ꏓꂱꇈꑌ' }}</span>
        </el-menu-item>
        <el-menu-item index="/glossary">
          <el-icon><Reading /></el-icon>
          <span>{{ appStore.lang === 'zh' ? '法律术语' : 'ꄜꀋꇬꏠ' }}</span>
        </el-menu-item>
        <!-- 新增咨询菜单 -->
        <el-menu-item index="/consultation">
          <el-icon><ChatDotRound /></el-icon>
          <span>{{ appStore.lang === 'zh' ? '法律咨询' : 'ꊇꇅꇉꄧ' }}</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <!-- 其余部分保持不变 -->
    <el-container>
      <el-header>
        <div class="header-left">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item>Civil Aid</el-breadcrumb-item>
            <el-breadcrumb-item>{{ breadcrumbName }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-button size="small" @click="appStore.toggleLang">
            {{ appStore.lang === 'zh' ? '切换为彝文' : 'ꆈꌠꉙꇬꄜ' }}
          </el-button>
          <el-dropdown v-if="userStore.userInfo">
            <span class="user-info">
              {{ userStore.userInfo.username }}
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main><router-view /></el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useUserStore } from '@/stores/user'
import { useAppStore } from '@/stores/app'
import { useRouter, useRoute } from 'vue-router'
import { House, Search, Reading, ArrowDown, ChatDotRound } from '@element-plus/icons-vue'

const userStore = useUserStore()
const appStore = useAppStore()
const router = useRouter()
const route = useRoute()

const activeMenu = computed(() => route.path)
const breadcrumbName = computed(() => {
  const titles = {
    '/home': appStore.lang === 'zh' ? '首页' : 'ꀒꇑ',
    '/glossary': appStore.lang === 'zh' ? '法律术语' : 'ꄜꀋꇬꏠ',
    '/consultation': appStore.lang === 'zh' ? '法律咨询' : 'ꊇꇅꇉꄧ'
  }
  return titles[route.path] || ''
})

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}
</script>

<style scoped lang="scss">
/* 样式保持不变 */
.layout-container {
  height: 100vh;
  .el-aside {
    background-color: #304156;
    .logo { height: 60px; display: flex; align-items: center; justify-content: center; color: white; font-weight: bold; background: #2b2f3a; }
    .el-menu { border-right: none; background: transparent; }
    :deep(.el-menu-item) { color: #bfcbd9; &.is-active { color: #409eff; } }
  }
  .el-header { display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid #dcdfe6; padding: 0 20px; }
  .el-main { background-color: #f0f2f5; }
  .user-info { cursor: pointer; display: flex; align-items: center; gap: 5px; }
}
</style>