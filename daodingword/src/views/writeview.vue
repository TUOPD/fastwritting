<template>
    <div class="app-container">
        <!-- ==================== 左侧边栏 ==================== -->
        <aside class="sidebar">
            <header class="sidebar-header">
                <!-- 1. 顶部标题，使用 h1 更加语义化 -->
                <h1>标题</h1>
            </header>

            <footer class="sidebar-footer">
                <!-- 3. 底部的输入框，使用 v-model 双向绑定标题 -->
                <el-input v-model="articleTitle" class="frameless-title-input" placeholder="请输入文章标题"
                    size="large" maxlength="20" show-word-limit />
            </footer>
        </aside>

        <main class="main-content">
            <EditorComponet :title="articleTitle" :articleId="articleId"></EditorComponet>
        </main>
    </div>
</template>

<script setup>
// eslint-disable-next-line no-unused-vars
import EditorComponet from '@/components/EditorComponet.vue'
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
// eslint-disable-next-line no-unused-vars
const articleTitle = ref('')
const route = useRoute()
const articleId = ref()
onMounted(() => {
  articleId.value = route.query.id
  console.log(articleId.value)
  if (articleId.value) {
    articleTitle.value = route.query.articleTitle
  } else {
    articleTitle.value = ''
  }
})
</script>

<style scoped>
/*
  `scoped` 属性确保这些样式只作用于当前组件，不会影响到项目中的其他组件。
  这对于组件化开发至关重要。
*/
:root {
    --bg-sidebar: #f7f8fa;
    --bg-main: #ffffff;
    --bg-hover: #eff0f1;
    --bg-active: #e8f2ff;
    --border-color: #e0e2e5;
    --text-primary: #1f2329;
    --text-secondary: #585a5f;
    --text-placeholder: #b8babf;
    --accent-color: #007bff;
}

.app-container {
    display: flex;
    height: 100vh;
    width: 100vw;
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
    font-size: 14px;
    color: var(--text-primary);
    overflow: hidden;
}

/* --- 左侧边栏 --- */
.sidebar {
    flex: 0 0 260px;
    background-color: var(--bg-sidebar);
    border-right: 1px solid var(--border-color);
    display: flex;
    flex-direction: column;
    padding: 12px;
}

.sidebar-header {
    padding: 8px;
    font-size: 16px;
    font-weight: 600;
    display: flex;
    align-items: center;
    gap: 8px;
}

.sidebar-header .fa-angle-down {
    color: var(--text-secondary);
}

.search-bar {
    position: relative;
    margin: 12px 0;
}

.search-bar input {
    width: 100%;
    padding: 8px 12px 8px 30px;
    border-radius: 6px;
    border: 1px solid var(--border-color);
    background-color: #fff;
}

.search-bar .fa-magnifying-glass {
    position: absolute;
    left: 10px;
    top: 50%;
    transform: translateY(-50%);
    color: var(--text-secondary);
}

.sidebar-nav ul,
.note-list ul {
    list-style: none;
    padding: 0;
}

.sidebar-nav a,
.note-list a {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 8px 12px;
    text-decoration: none;
    color: var(--text-primary);
    border-radius: 6px;
    font-weight: 500;
    cursor: pointer;
}

.sidebar-nav a:hover,
.note-list a:hover {
    background-color: var(--bg-hover);
}

.note-list {
    flex-grow: 1;
    overflow-y: auto;
    margin-top: 10px;
}

.note-list .active a {
    background-color: var(--bg-active);
    color: var(--accent-color);
}

.sidebar-footer {
    padding: 12px 8px;
}

.sidebar-footer a {
    color: var(--text-secondary);
    font-size: 13px;
    text-decoration: none;
}

/* --- 主内容区 --- */
.main-content {
    height: 100%;
    flex-direction: column;
    background-color: var(--bg-main);
}

.main-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12px 24px;
    border-bottom: 1px solid var(--border-color);
    flex-shrink: 0;
}

:deep(.frameless-title-input .el-input__wrapper) {
    /* 移除边框、背景和阴影，让它看起来是“无框”的 */
    background-color: transparent;
    box-shadow: none !important;
    /* 使用 !important 确保覆盖默认样式 */
    border: none;
    padding: 0;
    /* 移除默认的内边距 */
}

/* 2. 修改输入框本身 .el-input__inner */
:deep(.frameless-title-input .el-input__inner) {
    /* 设置输入文字的样式 */
    font-size: 30px;
    /* 大字体 */
    font-weight: 600;
    /* 加粗 */
    color: #303133;
    /* 默认文字颜色 */
    height: auto;
    /* 高度自适应 */
    line-height: 1.5;
}

/* 3. 修改占位符 (Placeholder) 的样式 */
:deep(.frameless-title-input .el-input__inner::placeholder) {
    color: #a8abb2;
    /* 占位符颜色，比默认的更浅一些 */
    font-size: 30px;
    font-weight: 600;
}

/* 4. (可选) 调整字数统计的样式 */
:deep(.frameless-title-input .el-input__count) {
    /* 将字数统计移到右下角，不遮挡文字 */
    position: absolute;
    right: 10px;
    bottom: -20px;
    font-size: 14px;
    color: #909399;
}
</style>
