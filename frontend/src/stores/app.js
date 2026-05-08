import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAppStore = defineStore('app', () => {
  // lang: 'zh' 代表中文, 'nuosu' 代表彝文
  const lang = ref(localStorage.getItem('lang') || 'zh')
  
  const toggleLang = () => {
    lang.value = lang.value === 'zh' ? 'nuosu' : 'zh'
    localStorage.setItem('lang', lang.value)
  }

  return { lang, toggleLang }
})