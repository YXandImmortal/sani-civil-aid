import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAppStore = defineStore('app', () => {
  // language: 'zh' 为汉文, 'nuosu' 为彝文
  const lang = ref('zh')
  
  const toggleLang = () => {
    lang.value = lang.value === 'zh' ? 'nuosu' : 'zh'
  }

  return { lang, toggleLang }
})