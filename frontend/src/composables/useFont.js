import { computed } from 'vue'
import { useFontStore } from '@/stores/font'

/**
 * 字体管理 Composable
 * 提供便捷的字体相关响应式数据和工具方法
 */
export function useFont() {
  const fontStore = useFontStore()

  /** 彝文字体栈（含本地 Yi Script） */
  const yiFontFamily = computed(() => fontStore.yiFontFamily)

  /** 是否启用本地 Yi Script 字体 */
  const useLocalYiFont = computed({
    get: () => fontStore.useLocalYiFont,
    set: (v) => { fontStore.useLocalYiFont = v }
  })

  /** 全局彝文字号 */
  const fontSize = computed({
    get: () => fontStore.fontSize,
    set: (v) => { fontStore.fontSize = v }
  })

  /** 全局彝文行高 */
  const lineHeight = computed({
    get: () => fontStore.lineHeight,
    set: (v) => { fontStore.lineHeight = v }
  })

  /** 预览文本 */
  const previewText = computed({
    get: () => fontStore.previewText,
    set: (v) => { fontStore.previewText = v }
  })

  /** 生成带彝文字体的样式对象（用于内联样式） */
  const yiStyle = computed(() => ({
    fontFamily: yiFontFamily.value,
    fontSize: `${fontSize.value}px`,
    lineHeight: fontStore.lineHeight
  }))

  /** 一键切换本地字体启用状态 */
  const toggleLocalFont = () => {
    fontStore.useLocalYiFont = !fontStore.useLocalYiFont
  }

  /** 重置为默认设置 */
  const resetFontSettings = () => {
    fontStore.resetSettings()
  }

  return {
    yiFontFamily,
    useLocalYiFont,
    fontSize,
    lineHeight,
    previewText,
    yiStyle,
    toggleLocalFont,
    resetFontSettings
  }
}
