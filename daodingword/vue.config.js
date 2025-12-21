const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: [
    // 将需要被 Babel 编译的第三方依赖包的名称添加进来
    '@excalidraw/excalidraw',
    '@excalidraw/laser-pointer', // 明确指定出问题的包
    '@excalidraw/perfect-freehand' // 通常 Excalidraw 的核心依赖都需要编译
  ]
})
