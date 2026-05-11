import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

const STORAGE_KEY = 'font-settings'

const defaultSettings = {
  useLocalYiFont: true,
  fontSize: 16,
  lineHeight: 1.6,
  previewText: '哋喸俛傆儶呩'
}

function loadSettings() {
  try {
    const raw = localStorage.getItem(STORAGE_KEY)
    return raw ? { ...defaultSettings, ...JSON.parse(raw) } : { ...defaultSettings }
  } catch {
    return { ...defaultSettings }
  }
}

function saveSettings(settings) {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(settings))
}

export const useFontStore = defineStore('font', () => {
  const settings = ref(loadSettings())

  const useLocalYiFont = computed({
    get: () => settings.value.useLocalYiFont,
    set: (v) => updateSetting('useLocalYiFont', v)
  })

  const fontSize = computed({
    get: () => settings.value.fontSize,
    set: (v) => updateSetting('fontSize', Number(v))
  })

  const lineHeight = computed({
    get: () => settings.value.lineHeight,
    set: (v) => updateSetting('lineHeight', Number(v))
  })

  const previewText = computed({
    get: () => settings.value.previewText,
    set: (v) => updateSetting('previewText', v)
  })

  const yiFontFamily = computed(() => {
    return useLocalYiFont.value
      ? '"Yi Script", "Microsoft Yi Baiti", "Nuosu SIL", serif'
      : '"Microsoft Yi Baiti", "Nuosu SIL", serif'
  })

  function updateSetting(key, value) {
    settings.value[key] = value
    saveSettings(settings.value)
    applyFontToDocument()
  }

  function applyFontToDocument() {
    const root = document.documentElement
    root.style.setProperty('--yi-font-family', yiFontFamily.value)
    root.style.setProperty('--yi-font-size', `${fontSize.value}px`)
    root.style.setProperty('--yi-line-height', lineHeight.value)
  }

  function resetSettings() {
    settings.value = { ...defaultSettings }
    saveSettings(settings.value)
    applyFontToDocument()
  }

  // 初始化时应用一次
  applyFontToDocument()

  return {
    settings,
    useLocalYiFont,
    fontSize,
    lineHeight,
    previewText,
    yiFontFamily,
    updateSetting,
    applyFontToDocument,
    resetSettings
  }
})
