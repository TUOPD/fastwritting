<template>
    <div style="width:100%;height:100%">
        <bird-vantas>
        </bird-vantas>
        <div class="Tittle">
            <div class="main-text">记录下你每一次的努力</div>
             <el-button type="primary" size="large" @click="jumped()">请开始创作</el-button>
        </div>
    </div>
</template>
<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()
// eslint-disable-next-line no-unused-vars
const jumped = () => {
  router.push('/mainsee')
}
</script>
<style scoped>
@font-face {
    font-family: 'AlimamaFangYuanTiVF';
    src: url('../assets/word/AlimamaFangYuanTiVF-Thin.ttf') format('woff');
    font-style: normal;
}
.Tittle {
    /* 1. 定位上下文 */
    position: absolute;
    top: 50%;
    left: 50%;

    /* 2. 使用 transform 将元素向左、上各自移动自身宽高的一半，实现完美居中 */
    transform: translate(-50%, -50%);

    /* 3. 使用 Flexbox 让内部元素垂直排列并居中 */
    display: flex;
    flex-direction: column; /* 垂直排列 */
    align-items: center;   /* 水平居中 */
    gap: 20px; /* 在标题和按钮之间增加一些间距 */

    /* 样式美化 */
    font-family: AlimamaFangYuanTiVF, sans-serif; /* 添加一个备用字体 */
    color: aliceblue;
    text-align: center; /* 确保文字本身是居中的 */
}

.main-text {
    font-size: 64px;
    /* 防止文字过长时换行影响布局 */
    white-space: nowrap;
}
</style>
