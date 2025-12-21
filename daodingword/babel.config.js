module.exports = {
  presets: [
    '@vue/cli-plugin-babel/preset'
  ],
  plugins: [
    // 告诉 Babel 使用这个插件来转换静态类块语法
    '@babel/plugin-transform-class-static-block'
  ]
}
