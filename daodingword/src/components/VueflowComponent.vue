<template>
  <!-- 1. 准备一个容器 div，React 组件将被渲染到这里 -->
  <div ref="containerRef" class="excalidraw-wrapper"></div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch } from 'vue'

// 2. 引入 React 和 ReactDOM
import React from 'react'
import ReactDOM from 'react-dom'

// 3. 引入 Excalidraw 组件和它的类型（如果使用 TypeScript）
import { Excalidraw } from '@excalidraw/excalidraw'
import '@excalidraw/excalidraw/index.css'

// 4. 定义 props 和 emits，用于与父 Vue 组件通信
// eslint-disable-next-line no-undef
const props = defineProps({
  // 用于加载初始数据
  initialData: {
    type: Object,
    default: null
  },
  // 用于控制只读模式
  viewMode: {
    type: Boolean,
    default: false
  }
})

// eslint-disable-next-line no-undef
const emit = defineEmits(['change']) // 当白板内容改变时，向外触发事件

// 5. 创建一个 ref 来引用容器 div
const containerRef = ref(null)

// 定义一个函数来渲染/更新 React 组件
const renderExcalidraw = () => {
  if (containerRef.value) {
    // 准备要传递给 Excalidraw 的 props
    const excalidrawProps = {
      initialData: props.initialData,
      viewModeEnabled: props.viewMode,
      langCode: 'zh-CN', // 设置语言为中文
      onChange: (elements, appState, files) => {
        // 当 React 组件内部内容变化时，通过 emit 将数据传回给 Vue 父组件
        emit('change', { elements, appState, files })
      }
      // ... 你可以在这里传递任何 Excalidraw 支持的 prop
    }

    // 使用 React.createElement 创建组件实例，并用 ReactDOM.render 渲染它
    ReactDOM.render(React.createElement(Excalidraw, excalidrawProps), containerRef.value)
  }
}

// 6. 在组件挂载后，首次渲染 React 组件
onMounted(() => {
  renderExcalidraw()
})

// 7. 在组件卸载前，清理 React 组件，防止内存泄漏
onUnmounted(() => {
  if (containerRef.value) {
    ReactDOM.unmountComponentAtNode(containerRef.value)
  }
})

// 8. (重要) 监听 props 的变化，当父组件传递的数据更新时，重新渲染 React 组件
watch(() => props.initialData, () => {
  console.log('Initial data changed, re-rendering Excalidraw...')
  renderExcalidraw()
})
watch(() => props.viewMode, () => {
  console.log('View mode changed, re-rendering Excalidraw...')
  renderExcalidraw()
})
</script>

<style scoped>
/* 确保包装器和它的容器有明确的高度，否则 Excalidraw 将无法显示 */
.excalidraw-wrapper {
  width: 100%;
  height: 100%; /* 或者一个具体的像素值，如 800px */
}
</style>
