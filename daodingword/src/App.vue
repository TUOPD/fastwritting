<template>
 <div class="main">
    <router-view></router-view>
 </div>
</template>
<script setup>
// eslint-disable-next-line no-unused-vars
import EditorComponet from '@/components/EditorComponet.vue'
</script>
<style lang="scss">
html,body,#app{
  width:100vw;
  height: 100vh;
}
.main{
  width: 100%;
  height: 100%;
}
*{
  margin:0px;
  padding: 0px;
}
</style>
