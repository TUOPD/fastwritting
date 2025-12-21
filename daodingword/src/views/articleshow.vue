<template>
  <!-- 使用一个外层容器来控制整体布局 -->
  <div class="article-container">
    <!-- v-if: 只有在 article 数据加载完成后才显示文章内容 -->
    <div v-if="article" class="article-content">
      <h1 class="article-title">{{ article.title }}</h1>

      <!--
        这个 div 用来包裹 v-html 渲染出的内容，
        方便我们通过 CSS 为其内部的元素设置样式。
      -->
      <div v-html="article.context" class="content-body"></div>
    </div>

    <!-- v-else: 在数据加载时显示一个加载动画 -->
    <div v-else class="loading-container">
      <div class="spinner"></div>
      <p>正在加载文章内容...</p>
    </div>
  </div>
</template>

<script setup>
// ... 你的 <script setup> 逻辑保持不变 ...
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const article = ref(null)

watch(
  () => route.params.id,
  (newId) => {
    console.log(newId)
    fetch('http://localhost:9527/getid', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ id: newId })
    })
      .then(res => res.json())
      .then(res => {
        if (res && res.code === 200) {
          article.value = res.object
        }
      })
  },
  { immediate: true }
)
</script>

<style scoped>
/* --- 整体布局容器 --- */
.article-container {
  display: flex;
  justify-content: center;
  padding: 40px 24px;
  background-color: #f9f9f9; /* 添加一个浅灰色背景，突出内容区 */
  min-height: 100vh;
}

/* --- 文章内容区域 --- */
.article-content {
  background-color: #fff;
  width: 100%;
  max-width: 800px; /* 设定最佳阅读宽度 */
  padding: 40px 60px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08); /* 添加一点阴影，提升质感 */
}

/* --- 文章主标题 --- */
.article-title {
  font-size: 2.5rem; /* 增大标题字号 */
  font-weight: 700;
  line-height: 1.3;
  margin-bottom: 2rem;
  color: #2c3e50;
  border-bottom: 2px solid #f0f0f0;
  padding-bottom: 1rem;
}

/* ---
  v-html 内容的样式
  使用 :deep() 伪选择器来为 v-html 动态插入的子元素设置样式
--- */
.content-body {
  font-family: 'AlimamaFangYuanTiVF'; /* 使用衬线字体，提升长文阅读体验 */
  font-size: 1.1rem;
  line-height: 1.8;
  color: #34495e;
}

.content-body :deep(p) {
  margin-bottom: 1.5em; /* 增加段落间距 */
}

.content-body :deep(h2) {
  font-size: 2rem;
  font-weight: 600;
  margin-top: 2.5em;
  margin-bottom: 1em;
  border-bottom: 1px solid #eaecef;
  padding-bottom: 0.5em;
}

.content-body :deep(h3) {
  font-size: 1.5rem;
  font-weight: 600;
  margin-top: 2em;
  margin-bottom: 1em;
}

.content-body :deep(img) {
  max-width: 100%; /* 图片自适应宽度 */
  height: auto;
  display: block; /* 避免图片下方有空隙 */
  margin: 2em auto; /* 图片居中显示 */
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.content-body :deep(blockquote) {
  border-left: 4px solid #42b983; /* Vue 绿色作为引用边框颜色 */
  padding-left: 1.5em;
  margin: 2em 0;
  color: #666;
  font-style: italic;
}

.content-body :deep(pre) {
  background-color: #2d2d2d;
  color: #ccc;
  padding: 1.5em;
  border-radius: 8px;
  overflow-x: auto; /* 代码过长时可以横向滚动 */
  margin: 2em 0;
}

.content-body :deep(code) {
  font-family: 'Fira Code', 'Courier New', monospace; /* 使用等宽字体显示代码 */
  font-size: 0.9em;
}

/* 1. 为列表恢复内边距，并设置基本样式 */
.content-body :deep(ul),
.content-body :deep(ol) {
  /* 恢复默认的内边距，这是放置列表标记（数字/圆点）的地方 */
  /* 这个值可以根据你的视觉需求调整，2em 是一个常用的值 */
  padding-left: 2em;
  /* 你可以保留你喜欢的行间距和分割线，但最好把它们放到 li 上 */
}

/* 2. 明确指定有序列表为数字标记 */
.content-body :deep(ol) {
  list-style-type: decimal;
}

/* 3. 明确指定无序列表为圆点标记 */
.content-body :deep(ul) {
  list-style-type: disc;
}

/* 4. 恢复 li 的默认显示方式，并添加你想要的间距和边框 */
.content-body :deep(li) {
  /*
    关键：不要使用 display: flex。
    让 li 保持默认的 display: list-item，浏览器会自动处理好对齐。
  */
  margin-bottom: 0.8em;
  padding-bottom: 0.8em;
  border-bottom: 1px solid #eee;
}

/* 5. (可选优化) 移除最后一个列表项的边框和底部外边距，看起来更干净 */
.content-body :deep(li:last-child) {
  border-bottom: none;
  margin-bottom: 0;
}
/* 针对 li 的伪元素，这将成为我们的第一列 */
.content-body ::v-deep table {
  border-collapse: collapse;
  width: 100%;
  margin: 15px 0;
}

.content-body ::v-deep th,
.content-body ::v-deep td {
  border: 1px solid #ddd;
  padding: 8px;
}

.content-body ::v-deep .text-red {
  color: red;
}

/* 你甚至可以为所有 h1, h2, p 等标签设置通用样式 */
.content-body ::v-deep h1 {
  font-size: 2em;
  margin-bottom: 0.5em;
}
/* --- 加载状态样式 --- */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100px 0;
  color: #999;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #42b983; /* Vue 绿色 */
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 16px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
@font-face {
    font-family: 'AlimamaFangYuanTiVF';
    src: url('../assets/word/AlimamaFangYuanTiVF-Thin.ttf') format('woff');
    font-style: normal;
}
/* --- 移动端适配 --- */
@media (max-width: 768px) {
  .article-content {
    padding: 24px;
  }

  .article-title {
    font-size: 2rem;
  }

  .content-body {
    font-size: 1rem;
  }
}
</style>
