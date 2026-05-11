import './assets/main.scss'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/theme-chalk/dark/css-vars.css'

import App from './App.vue'
import router from './router'
import { useFontStore } from '@/stores/font'

const app = createApp(App)

const pinia = createPinia()
app.use(pinia)
app.use(router)
app.use(ElementPlus)

// 初始化字体设置（确保在 mount 前完成 store 实例化）
const fontStore = useFontStore()
fontStore.applyFontToDocument()

app.mount('#app')