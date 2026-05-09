<template>
  <el-container class="layout-container">
    <!-- 侧边栏：采用彝族崇尚的黑色系 -->
    <el-aside width="240px" class="aside-menu">
      <div class="logo">
        <div class="logo-icon">ꏓ</div>
        <span>ꏓꂱꈄꏍ AID</span>
      </div>
      <el-menu :default-active="activeMenu" class="custom-menu" router>
        <el-menu-item index="/home">
          <el-icon><House /></el-icon>
          <span>{{ appStore.lang === 'zh' ? '首页' : 'ꀒꇑ' }}</span>
        </el-menu-item>
        <el-menu-item index="/glossary">
          <el-icon><Reading /></el-icon>
          <span>{{ appStore.lang === 'zh' ? '法律术语' : 'ꄜꀋꇬꏠ' }}</span>
        </el-menu-item>
        <el-menu-item index="/consultation">
          <el-icon><ChatDotRound /></el-icon>
          <span>{{ appStore.lang === 'zh' ? '法律咨询' : 'ꊇꇅꇉꄧ' }}</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <!-- 顶部导航：背景色自适应主题 -->
      <el-header class="custom-header">
        <div class="header-left">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item>Civil Aid</el-breadcrumb-item>
            <el-breadcrumb-item>{{ breadcrumbName }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        
        <div class="header-right">
          <!-- 主题切换：太阳/月亮 -->
          <el-tooltip :content="appStore.theme === 'light' ? '暗色模式' : '明亮模式'">
            <el-button 
              circle 
              class="theme-toggle"
              @click="appStore.toggleTheme"
            >
              <el-icon v-if="appStore.theme === 'light'"><Moon /></el-icon>
              <el-icon v-else><Sunny /></el-icon>
            </el-button>
          </el-tooltip>

          <!-- 语种切换 -->
          <el-button size="small" class="lang-btn" @click="appStore.toggleLang">
            {{ appStore.lang === 'zh' ? '切换为彝文' : 'ꆈꌠꉙꇬꄜ' }}
          </el-button>

          <el-dropdown v-if="userStore.userInfo">
            <span class="user-info-display">
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
      
      <el-main class="custom-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useUserStore } from '@/stores/user'
import { useAppStore } from '@/stores/app'
import { useRouter, useRoute } from 'vue-router'
import { House, Reading, ArrowDown, ChatDotRound, Sunny, Moon } from '@element-plus/icons-vue'

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
.layout-container {
  height: 100vh;

  .aside-menu {
    background-color: #000000; // 强制彝族黑
    border-right: 1px solid var(--color-border-default);
    
    .logo {
      height: 80px;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      background-color: #000;
      color: var(--color-secondary); // 金色
      font-weight: bold;
      .logo-icon { font-size: 1.5rem; margin-bottom: 4px; border: 1px solid var(--color-secondary); padding: 0 8px; }
    }

    .custom-menu {
      border-right: none;
      background: transparent;
      :deep(.el-menu-item) {
        color: var(--color-silver);
        &:hover { background-color: #1a1714; color: var(--color-primary); }
        &.is-active { 
          color: var(--color-primary); 
          background-color: #1a1714;
          &::before { content: ""; position: absolute; left: 0; width: 4px; height: 100%; background: var(--color-primary); }
        }
      }
    }
  }

  .custom-header {
    background-color: var(--color-bg-elevated);
    border-bottom: 1px solid var(--color-border-default);
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 24px;
    
    .header-right {
      display: flex;
      align-items: center;
      gap: 20px;
      .user-info-display { cursor: pointer; color: var(--color-text-primary); font-weight: 500; }
      .theme-toggle { border-color: var(--color-border-default); color: var(--color-secondary); }
    }
  }

  .custom-main {
    background-color: var(--color-bg-base);
    padding: 24px;
  }
}
</style>