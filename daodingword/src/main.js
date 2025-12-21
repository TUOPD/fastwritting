import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import EditorComponet from './components/EditorComponet.vue'
import BirdVantas from './components/BirdVantas.vue'
import ElementPlus from 'element-plus'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import 'element-plus/dist/index.css'
import VueflowComponent from '@/components/VueflowComponent.vue'
const app = createApp(App)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}
app.use(store).component('EditorComponet', EditorComponet).use(ElementPlus).component('BirdVantas', BirdVantas).use('vueflow-component', VueflowComponent).use(router).mount('#app')
