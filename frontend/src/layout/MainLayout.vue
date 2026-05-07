<template>
  <el-container class="layout-container">
    <el-aside width="240px">
      <div class="logo">
        <span>ꏓꂱꈄꏍ AID</span>
      </div>
      <el-menu default-active="1" class="el-menu-vertical">
        <el-menu-item index="1">
          <el-icon><House /></el-icon>
          <span>首页 / ꀒꇑ</span>
        </el-menu-item>
        <el-menu-item index="2">
          <el-icon><Search /></el-icon>
          <span>法条查询 / ꏓꂱꇈꑌ</span>
        </el-menu-item>
        <el-menu-item index="3">
          <el-icon><Reading /></el-icon>
          <span>法律术语 / ꄜꀋꇬꏠ</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header>
        <div class="header-left">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item>Civil Aid</el-breadcrumb-item>
            <el-breadcrumb-item>首页</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-button @click="appStore.toggleLang">
            {{ appStore.lang === 'zh' ? '切换为彝文' : 'ꆈꌠꉙꇬꄜ' }}
          </el-button>
          <el-dropdown>
            <span class="user-info">管理员 <el-icon><ArrowDown /></el-icon></span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>个人中心</el-dropdown-item>
                <el-dropdown-item divided @click="$router.push('/login')">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useAppStore } from '@/stores/app'
import { House, Search, Reading, ArrowDown } from '@element-plus/icons-vue'

const appStore = useAppStore()
</script>

<style scoped lang="scss">
.layout-container {
  height: 100vh;
  .el-aside {
    background-color: #304156;
    color: white;
    .logo {
      height: 60px;
      display: flex;
      align-items: center;
      justify-content: center;
      font-weight: bold;
      font-size: 1.2rem;
      background: #2b2f3a;
    }
    .el-menu { border-right: none; background: transparent; }
    :deep(.el-menu-item) {
      color: #bfcbd9;
      &:hover { background: #263445; }
      &.is-active { color: #409eff; }
    }
  }
  .el-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1px solid #dcdfe6;
    .header-right {
      display: flex;
      align-items: center;
      gap: 20px;
      .user-info { cursor: pointer; display: flex; align-items: center; gap: 5px; }
    }
  }
}
</style>