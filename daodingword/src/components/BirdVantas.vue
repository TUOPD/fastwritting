<!-- src/components/VantaBirds.vue -->
<template>
  <div ref="vantaRef" class="vanta-background-container"></div>
</template>

<script setup>
// 1. --- 引入所有必要的模块 ---
import { ref, onMounted, onBeforeUnmount, watch, defineExpose } from 'vue'
import * as THREE from 'three'
// 明确引入 BIRDS 效果文件
import birds from 'vanta/dist/vanta.birds.min.js'

// 2. --- 定义组件的 Props (API) for BIRDS ---
// 这些 props 是根据 Vanta.js 官网 BIRDS 效果的文档来定义的
// eslint-disable-next-line no-undef
const props = defineProps({
  // --- Vanta 通用控制选项 ---
  mouseControls: { type: Boolean, default: true },
  touchControls: { type: Boolean, default: true },
  gyroControls: { type: Boolean, default: false },
  minHeight: { type: Number, default: 200.00 },
  minWidth: { type: Number, default: 200.00 },
  scale: { type: Number, default: 1.00 },
  scaleMobile: { type: Number, default: 1.00 },

  // --- BIRDS 效果专属选项 ---
  backgroundColor: { type: [Number, String], default: 0x7192f }, // 深色背景
  color1: { type: [Number, String], default: 0xff0000 }, // 鸟群颜色1 (渐变)
  color2: { type: [Number, String], default: 0xd1ff }, // 鸟群颜色2 (渐变)
  birdSize: { type: [Number, String], default: 1.5 }, // 鸟的大小
  wingSpan: { type: [Number, String], default: 20.0 }, // 翅膀展开幅度
  speedLimit: { type: [Number, String], default: 5.0 }, // 飞行速度限制
  separation: { type: [Number, String], default: 20.0 }, // 鸟之间的最小距离
  alignment: { type: [Number, String], default: 20.0 }, // 鸟群队列对齐的趋势
  cohesion: { type: [Number, String], default: 20.0 }, // 鸟群聚集的趋势
  quantity: { type: [Number, String], default: 4.0 }, // 鸟的数量
  backgroundAlpha: { type: [Number, String], default: 0.6 } // 背景不透明度
})

// 3. --- 组件内部状态和引用 ---
const vantaRef = ref(null)
let vantaEffect = null
const createVantaOptions = () => ({
  THREE,
  el: vantaRef.value,
  ...props

})

onMounted(() => {
  if (vantaRef.value) {
    vantaEffect = birds(createVantaOptions())
  }
})

onBeforeUnmount(() => {
  // 在销毁 vantaEffect 之前，先把它赋值为 null
  // 这是一个关键的步骤，可以防止后续的 watch 或 resize 回调再次操作一个即将被销毁的对象
  if (vantaEffect) {
    const effectToDestroy = vantaEffect
    vantaEffect = null // 关键：立即切断引用
    effectToDestroy.destroy()
  }
})

// 5. --- 响应式更新 ---
watch(props, () => {
  if (vantaEffect && typeof vantaEffect.setOptions === 'function') {
    vantaEffect.setOptions(createVantaOptions())
  }
}, { deep: true })

// 6. --- 暴露调试接口 ---
defineExpose({
  vantaEffect,
  refresh: () => {
    if (vantaEffect) vantaEffect.setOptions(createVantaOptions())
  }
})
</script>

<style scoped>
.vanta-background-container {
  width: 100%;
  height: 100%;
}
</style>
