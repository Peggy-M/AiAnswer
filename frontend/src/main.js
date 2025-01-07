import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import 'element-plus/dist/index.css'
import App from './App.vue'
import router from './router'
import './styles/index.scss'

const app = createApp(App)

// 注册所有图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(ElementPlus, {
  size: 'default'
})
app.use(router)

app.mount('#app')

// 开发环境下的路由日志
if (import.meta.env.DEV) {
  router.beforeEach((to, from) => {
    console.log(`Route change: ${from.path} -> ${to.path}`)
  })
} 