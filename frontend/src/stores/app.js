import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAppStore = defineStore('app', () => {
  const lang = ref(localStorage.getItem('lang') || 'zh')
  const theme = ref(localStorage.getItem('theme') || 'light')

  const toggleLang = () => {
    lang.value = lang.value === 'zh' ? 'nuosu' : 'zh'
    localStorage.setItem('lang', lang.value)
  }

  const toggleTheme = () => {
    theme.value = theme.value === 'light' ? 'dark' : 'light'
    localStorage.setItem('theme', theme.value)
    applyTheme()
  }

  const applyTheme = () => {
    // 关键点：将属性挂载到 documentElement
    document.documentElement.setAttribute('data-theme', theme.value)
    // 强制同步 Element Plus 暗黑模式
    if (theme.value === 'dark') {
      document.documentElement.classList.add('dark')
    } else {
      document.documentElement.classList.remove('dark')
    }
  }

  return { lang, theme, toggleLang, toggleTheme, applyTheme }
})