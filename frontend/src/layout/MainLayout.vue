<template>
  <el-container class="layout-container">
    <!-- 侧边栏：采用彝族崇尚的黑色系 -->
    <el-aside width="240px" class="aside-menu">
      <div class="logo">
        <div class="logo-icon">[彝文占位符]</div>
        <span class="yi-bilingual">
          <span>典亮诺苏</span>
          <span class="yi-placeholder">[彝文占位符]</span>
        </span>
      </div>
      <el-menu :default-active="activeMenu" class="custom-menu" router>
        <el-menu-item index="/home">
          <el-icon><ChatDotRound /></el-icon>
          <span class="yi-bilingual">
            <span>法律咨询</span>
            <span class="yi-placeholder">[彝文占位符]</span>
          </span>
        </el-menu-item>
        <el-menu-item index="/vocabulary">
          <el-icon><Reading /></el-icon>
          <span class="yi-bilingual">
            <span>词汇对照</span>
            <span class="yi-placeholder">[彝文占位符]</span>
          </span>
        </el-menu-item>
        <el-menu-item index="/consultation">
          <el-icon><Search /></el-icon>
          <span class="yi-bilingual">
            <span>法条查询</span>
            <span class="yi-placeholder">[彝文占位符]</span>
          </span>
        </el-menu-item>
        <el-menu-item index="/font">
          <el-icon><Document /></el-icon>
          <span class="yi-bilingual">
            <span>字体管理</span>
            <span class="yi-placeholder">[彝文占位符]</span>
          </span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <!-- 顶部导航：背景色自适应主题 -->
      <el-header class="custom-header">
        <div class="header-left">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item>典亮诺苏</el-breadcrumb-item>
            <el-breadcrumb-item>
              <span class="yi-bilingual" v-if="breadcrumbName">
                <span>{{ breadcrumbName }}</span>
                <span class="yi-placeholder">[彝文占位符]</span>
              </span>
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        
        <div class="header-right">
          <!-- 主题切换：太阳/月亮 -->
          <el-tooltip>
            <template #content>
              <span class="yi-bilingual">
                <span>{{ appStore.theme === 'light' ? '暗色模式' : '明亮模式' }}</span>
                <span class="yi-placeholder">[彝文占位符]</span>
              </span>
            </template>
            <el-button 
              circle 
              class="theme-toggle"
              @click="appStore.toggleTheme"
            >
              <el-icon v-if="appStore.theme === 'light'"><Moon /></el-icon>
              <el-icon v-else><Sunny /></el-icon>
            </el-button>
          </el-tooltip>

          <el-dropdown v-if="userStore.userInfo">
            <span class="user-info-display">
              {{ userStore.userInfo.username }}
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="handleLogout">
                  <span class="yi-bilingual">
                    <span>退出登录</span>
                    <span class="yi-placeholder">[彝文占位符]</span>
                  </span>
                </el-dropdown-item>
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
import {House, Reading, ArrowDown, ChatDotRound, Sunny, Moon, Search, Document} from '@element-plus/icons-vue'

const userStore = useUserStore()
const appStore = useAppStore()
const router = useRouter()
const route = useRoute()

const activeMenu = computed(() => route.path)
const breadcrumbName = computed(() => {
  const titles = {
    '/home': '法律咨询',
    '/vocabulary': '词汇对照',
    '/consultation': '法条查询',
    '/font': '字体管理'
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
    background-color: var(--color-bg-elevated);
    border-right: 1px solid var(--color-border-default);
    
    .logo {
      height: 80px;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      background-color: var(--color-bg-elevated);
      color: var(--color-secondary); // 金色
      font-weight: bold;
      .logo-icon { font-size: 1.5rem; margin-bottom: 4px; border: 1px solid var(--color-secondary); padding: 0 8px; }
    }

    .custom-menu {
      border-right: none;
      background: transparent;
      :deep(.el-menu-item) {
        color: var(--color-silver);
        &:hover { background-color: var(--color-primary-subtle); color: var(--color-primary); }
        &.is-active { 
          color: var(--color-primary); 
          background-color: var(--color-primary-subtle);
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
      .theme-toggle { background-color: var(--color-bg-elevated); border-color: var(--color-border-default); color: var(--color-secondary); }
    }
  }

  .custom-main {
    background-color: var(--color-bg-base);
    padding: 24px;
  }
}
</style>
