<template>
  <router-view />
</template>

<script setup>
import { onMounted } from 'vue'
import { useAppStore } from '@/stores/app'

const appStore = useAppStore()

onMounted(() => {
  // 初始化时应用主题
  appStore.applyTheme()
})
</script>

<style>
/* App 级别样式可为空，逻辑均在 SCSS 中 */
</style>