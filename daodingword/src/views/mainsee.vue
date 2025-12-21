<!-- eslint-disable object-curly-spacing -->
<template>
  <div class="common-layout">
    <el-container class="app-container">
      <!-- 左侧笔记列表 -->
      <el-aside width="260px" class="sidebar">
        <div class="sidebar-header">
          <span>📝 笔记列表</span>
        </div>
        <el-scrollbar class="note-list-scrollbar">
          <div class="sidebar-action-card " v-for="(item, index) in articlelist" :key="index"
            @click="jumparticle(item.id)" @contextmenu.prevent="showContextMenu(item, $event)">
            <p class="title-ellipsis">{{ item.title }}</p>
            <p>{{ item.updatetime }}</p>
          </div>
          <div v-if="menuVisible" class="custom-context-menu" :style="menuStyle" @click.stop>
            <div class="menu-item" @click="handleEdit()">
              <span>修改</span>
            </div>
            <div class="menu-item" @click="handleDelete()">
              <span>删除</span>
            </div>
          </div>
        </el-scrollbar>
        <div class="sidebar-footer">
          <p>众人之上，强者之列</p>
        </div>
      </el-aside>

      <!-- 右侧主内容区 -->
      <el-main class="main-content">
        <div class="main-header">
          <span>快记1.0.2测试版</span>
          <span>
            <el-button :icon="Setting" circle @click="setting()"></el-button>
            <el-button :icon="Edit" circle @click="Eidtor()" />
            <el-button :icon="Discount" circle @click="message()"></el-button>
          </span>
        </div>
        <div class="welcome-center" v-if="mainshow">
          <div class="welcome-title">欢迎回来</div>
        </div>
        <div style="flex:1;background-color:aliceblue" v-else>
          <router-view></router-view>
        </div>
      </el-main>

    </el-container>
    <el-dialog v-model="isshow" width="500" center>
      <template #title>
        请配置你的数据库
      </template>
      <template #default>
        <el-form>
          <el-form-item label="数据库名称">
            <el-input v-model="dburl" type="password" placeholder="请输入" show-password :disabled="iswrite"
              size="default" />
          </el-form-item>
          <el-form-item label="用户名">
            <el-input v-model="root" type="password" placeholder="请输入" show-password :disabled="iswrite"
              size="default" />
          </el-form-item>
          <el-form-item label="数据库密码">
            <el-input v-model="password" type="password" placeholder="请输入" show-password :disabled="iswrite"
              size="default" />
          </el-form-item>
        </el-form>
      </template>
      <template #footer>
        <el-button type="warning">重写</el-button>
        <el-button type="primary" @click="setdatabase()">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import htmlDocx from 'html-docx-js/dist/html-docx'
import { saveAs } from 'file-saver'
// eslint-disable-next-line no-unused-vars
import { Setting, Edit, Discount } from '@element-plus/icons-vue'
// --- 路由和基础状态 ---
const router = useRouter()
const isshow = ref(false)
const iswrite = ref(false)
const mainshow = ref(true)

// --- 数据库配置相关状态 ---
const root = ref('')
const password = ref('')
const dburl = ref('')

// --- 文章列表和右键菜单状态 ---
const articlelist = ref([])
const menuVisible = ref(false)
const menuStyle = ref({})
const currentItem = ref(null)
// eslint-disable-next-line camelcase
const article_id = ref(null)
// --- onMounted (组件挂载时执行) ---
onMounted(() => {
  fetch('http://localhost:9527/api/status').then(res => {
    if (!res.ok) {
      throw new Error(`HTTP 错误! 状态: ${res.status}`)
    }
    return res.json()
  }).then(res => {
    console.log('未配置，显示设置界面', res)
    isshow.value = true // 修正：使用 .value
  }).catch(error => {
    console.log('已配置或接口异常，尝试获取配置:', error)
    fetch('http://localhost:9527/getConfig')
      .then(res => res.json())
      .then(res => {
        console.log('获取配置成功', res)
        root.value = res.object.username
        password.value = res.object.password
        dburl.value = res.object.dbUrl
        iswrite.value = true
        // 获取文章列表
        return fetch('http://localhost:9527/getallarticleNote')
      })
      .then(res => res.json())
      .then(res => {
        console.log('获取文章列表成功', res)
        articlelist.value = res // 修正：使用 .value
      })
      .catch(getConfigError => {
        console.error('获取配置或文章列表失败:', getConfigError)
      })
  })
})

// --- 方法定义 ---

// 切换设置显示 (您的 setting 函数)
// eslint-disable-next-line no-unused-vars
const setting = () => {
  isshow.value = !isshow.value
}

// 设置数据库
// eslint-disable-next-line no-unused-vars
const setdatabase = () => {
  fetch('http://localhost:9527/api/setup', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      username: root.value,
      password: password.value,
      dbUrl: dburl.value
    })
  }).then(res => res.json())
    .then(res => {
      ElMessage(res.message)
      if (res.success) {
        window.close()
      }
    })
}

// 跳转到编辑器
// eslint-disable-next-line no-unused-vars
function Eidtor () {
  router.push('/write')
}

// 跳转到文章详情
// eslint-disable-next-line no-unused-vars
const jumparticle = (id) => {
  if (menuVisible.value) { // 如果菜单开着，单击只关闭菜单
    hideContextMenu()
    return
  }
  console.log(id)
  mainshow.value = false
  router.push({
    name: 'article',
    params: { id: id }
  })
  article_id.value = id
}

// ---- 右键菜单核心方法 (已全部修正) ----

// 显示右键菜单
// eslint-disable-next-line no-unused-vars
const showContextMenu = (item, event) => {
  menuVisible.value = true // 修正：使用 .value
  currentItem.value = item // 修正：使用 .value
  menuStyle.value = { // 修正：使用 .value
    top: `${event.clientY}px`,
    left: `${event.clientX}px`
  }
}

// 隐藏菜单
const hideContextMenu = () => {
  menuVisible.value = false // 修正：使用 .value
  currentItem.value = null // 修正：使用 .value
}

// 处理"修改"点击
// eslint-disable-next-line no-unused-vars
const handleEdit = () => {
  if (currentItem.value) { // 修正：使用 .value
    console.log(`准备修改 ID: ${currentItem.value.id} 的文章`)
    router.push({
      name: 'write',
      query: {
        id: currentItem.value.id,
        articleTitle: currentItem.value.title
      }
    })
  }
  hideContextMenu() // 修正：直接调用函数，不需要 this
}

// 处理"删除"点击
// eslint-disable-next-line no-unused-vars
const handleDelete = () => {
  if (currentItem.value) { // 修正：使用 .value
    if (confirm(`确定要删除文章《${currentItem.value.title}》吗？`)) {
      console.log(`准备删除 ID: ${currentItem.value.id} 的文章`)
      // 修正：操作 articlelist.value
      articlelist.value = articlelist.value.filter(article => article.id !== currentItem.value.id)
      fetch('http://localhost:9527/deleteid', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          id: currentItem.value.id
        })
      }).then(res => {
        return res.json()
      }).then(res => {
        ElMessage(res.message)
      })
    }
  }
  hideContextMenu() // 修正：直接调用函数，不需要 this
}
// eslint-disable-next-line no-unused-vars
const message = () => {
  console.log('导出为Word文档')
  fetch('http://localhost:9527/getid', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ id: article_id.value })
  })
    .then(res => res.json())
    .then(res => {
      if (res && res.code === 200) {
        const blob = htmlDocx.asBlob(res.object.context)
        saveAs(blob, `${res.object.title}.docx`)
      }
    })
}
</script>

<style scoped>
/* 全局布局 */
.common-layout,
.app-container {
  height: 100vh;
  /* 占满整个视口高度 */
  font-family: AlimamaFangYuanTiVF;
}

/* 左侧边栏 */
.sidebar {
  background-color: #f7f7f7;
  border-right: 1px solid #e8e8e8;
  display: flex;
  flex-direction: column;
  padding: 15px;
}

.sidebar-header {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 20px;
}

.sidebar-actions {
  width: 100%;
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.sidebar-actions .el-button {
  width: 48%;
}

/* 笔记列表 */
.note-list-scrollbar {
  flex-grow: 1;
  /* 占据剩余所有空间 */
}

.note-group .group-title {
  font-size: 12px;
  color: #999;
  margin: 15px 0 5px;
}

.note-group ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.note-item {
  padding: 10px;
  border-radius: 5px;
  cursor: pointer;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  font-size: 14px;
}

.note-item:hover {
  background-color: #e9e9e9;
}

.sidebar-footer {
  padding-top: 15px;
  border-top: 1px solid #e8e8e8;
  font-size: 14px;
}

/* 右侧主内容区 */
.main-content {
  position: relative;
  /* 为内部绝对定位的元素提供参照 */
  background-color: #fff;
  display: flex;
  flex-direction: column;
}

.main-header {
  position: sticky;
  /* 1. 将定位方式设置为 sticky */
  top: 0;
  /* 2. 当元素滚动到距离视口顶部 0px 时，开始“吸附” */
  /* --- 核心代码结束 --- */

  /* --- 以下是美化样式，确保吸顶后效果良好 --- */
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 24px;
  background-color: #ffffff;
  /* 3. 必须设置背景色，否则下方内容会透过来 */
  border-bottom: 1px solid #e0e2e5;
  z-index: 100;
  /* 4. 提高层级，确保它在其他元素之上 */
  transition: box-shadow 0.3s;
  /* (可选)平滑过渡效果 */
}

/* 欢迎语居中 */
.welcome-center {
  position: absolute;
  top: 45%;
  /* 稍微向上偏移一点，视觉效果更好 */
  left: 50%;
  transform: translate(-50%, -50%);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 30px;
  /* 标题和按钮的间距 */
}

.welcome-title {
  font-size: 48px;
  color: #ccc;
  font-weight: 300;
}

@font-face {
  font-family: 'AlimamaFangYuanTiVF';
  src: url('../assets/word/AlimamaFangYuanTiVF-Thin.ttf') format('woff');
  font-style: normal;
}

.sidebar-action-card {
  /* 1. 好看的渐变色背景 */
  background: linear-gradient(135deg, #5c6bc0, #26a69a);
  /* 靛蓝到青色的渐变 */
  color: white;
  /* 文字颜色设为白色以保证清晰 */

  /* 2. 核心：使用 Flexbox 实现左右对齐 */
  display: flex;
  justify-content: space-between;
  /* 关键：让子元素两端对齐 */
  align-items: center;
  /* 让子元素在垂直方向上居中 */
  width: 80%;
  /* 3. 其他美化样式 */
  padding: 10px 15px;
  /* 增加内边距 */
  border-radius: 6px;
  /* 添加圆角 */
  margin-top: 10px;
  /* 和上方元素增加一些间距 */
  font-family: sans-serif;
  /* 使用一个更现代的字体 */
}

/* 也可以为 p 标签单独设置样式，防止它们有默认的外边距影响布局 */
.sidebar-action-card p {
  margin: 0;
  font-size: 14px;
}

/* 让日期文字稍微小一点，颜色淡一点 */
.sidebar-action-card p:last-child {
  font-size: 12px;
  opacity: 0.8;
}

/* 3. 定义鼠标悬停时的样式 */
.sidebar-action-card:hover {
  /* 提高层级，确保它能“浮”在相邻元素的上面 */
  z-index: 10;

  /* 放大效果：放大到原来的 1.03 倍 */
  transform: scale(1.03);

  /* 阴影效果：添加一个更深、更立体的阴影 */
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.2);
}

:deep(.el-main) {
  --el-main-padding: 0px;
}

/* 卡片样式 */
.sidebar-action-card {
  padding: 10px 15px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background-color 0.2s;
}

.sidebar-action-card:hover {
  background-color: #f9f9f9;
}

.title-ellipsis {
  font-size: 15px;
  color: #333;
  margin: 0 0 4px 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.update-time {
  font-size: 12px;
  color: #999;
  margin: 0;
}

/* 自定义右键菜单的样式 */
.custom-context-menu {
  position: fixed;
  /* 使用 fixed 定位，相对于浏览器窗口 */
  z-index: 9999;
  /* 确保在最顶层 */
  width: 120px;
  background-color: #ffffff;
  border-radius: 6px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  border: 1px solid #e7e7e7;
  padding: 5px 0;
  font-size: 14px;
}

.menu-item {
  padding: 8px 16px;
  color: #333;
  cursor: pointer;
}

.menu-item:hover {
  background-color: #f0f0f0;
}
</style>
