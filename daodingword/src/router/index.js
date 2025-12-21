import { createRouter, createWebHistory } from 'vue-router'
import Welcome from '@/views/Welcome.vue'
import Mainsee from '@/views/mainsee.vue'
import Writeview from '@/views/writeview.vue'
import Articleshow from '@/views/articleshow.vue'
const routes = [
  {
    path: '/',
    component: Welcome
  }, {
    path: '/mainsee',
    component: Mainsee,
    children: [
      {
        path: '/mainsee/:id',
        name: 'article',
        component: Articleshow
      }
    ]
  }, {
    path: '/write',
    name: 'write',
    component: Writeview
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
