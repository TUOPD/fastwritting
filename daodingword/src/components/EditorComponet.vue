<template>
  <div style="border: 1px solid #ccc;height:99%">
    <Toolbar style="border-bottom: 1px solid #ccc;height:5%" :editor="editorRef" :defaultConfig="toolbarConfig"
      :mode="mode" />
    <Editor style="height:90%;" v-model="valueHtml" :defaultConfig="editorConfig" :mode="mode"
      @onCreated="handleCreated" />
    <div class="footer">
      <el-button type="primary" @click="sendImages()">存入</el-button>
      <el-button type="warning" @click="changeshow()">预览</el-button>
      <el-button type="primary" @click="changeimageshow()">流程图表</el-button>
    </div>
    <el-dialog v-model="show" title="Shipping address">
      <template #title>
        <h1>{{ title }}</h1>
      </template>
      <template #default>
        <div v-html="valueHtml"></div>
      </template>
    </el-dialog>
     <el-drawer v-model="drawershow" direction='ttb' size="800">
      <template #default>
        <vueflow-component></vueflow-component>
      </template>
     </el-drawer>
  </div>
</template>
<script>
import '@wangeditor/editor/dist/css/style.css' // 引入 css

import { onBeforeUnmount, ref, shallowRef, watch } from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import { ElMessage } from 'element-plus'
import VueflowComponent from '@/components/VueflowComponent.vue'
import SparkMD5 from 'spark-md5'
export default {
  components: { Editor, Toolbar, 'vueflow-component': VueflowComponent },
  props: {
    title: String,
    articleId: Number
  },
  setup (props) {
    // 编辑器实例，必须用 shallowRef
    const editorRef = shallowRef()
    const drawershow = ref(false)
    const changeshow = () => {
      show.value = true
    }
    const changeimageshow = () => {
      console.log('888')
      drawershow.value = true
    }
    // 内容 HTML
    const valueHtml = ref('<p>hello</p>')
    const show = ref(false)
    const sendarticleId = ref()
    const localFilesToUpload = new Map()
    const toolbarConfig = {}
    const editorConfig = {
      placeholder: '请输入内容...',
      MENU_CONF: {
        uploadImage: {
          customUpload: async (file, insertFn) => {
            const localId = await Md5(file)

            // 1. 生成唯一标识符 localId（这里使用 MD5）
            // 2. 将原始 File 对象存储起来，等待最终上传
            localFilesToUpload.set(localId, file)
            console.log('暂存本地文件:', localId, file.name)

            // 3. 使用 FileReader 将图片转为 Base64
            const reader = new FileReader()
            reader.onload = (e) => {
              const base64Url = e.target.result
              const editor = editorRef.value
              if (editor) {
                const imgHtml = `<img src="${base64Url}" alt="${file.name}" data-local-id="${localId}" style="max-width: 100%;"/>`
                editor.dangerouslyInsertHtml(imgHtml)
              }
            }
            reader.readAsDataURL(file)
          }
        }
      }
    }
    watch(
      // 1. 监听源：一个返回 props.articleId 的函数
      () => props.articleId,

      // 2. 回调函数：当 articleId 变化时执行
      (newId) => {
        // newId 就是 props.articleId 的最新值
        console.log(newId)
        // 3. 将您的 if/else 逻辑搬到这里
        if (newId === null || newId === undefined) {
          // 如果新的 ID 是 null，说明是新增模式或需要重置
          console.log('watch: articleId 为 null，进入新增模式或重置状态。')
          valueHtml.value = '' // 清空编辑器内容
        } else {
          // 如果新的 ID 是一个有效的数字
          console.log(`watch: articleId 变为 ${newId}，开始获取文章数据...`)
          sendarticleId.value = newId
          console.log(sendarticleId.value)
          fetch('http://localhost:9527/getid', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ id: newId }) // 使用 newId
          })
            .then(res => res.json())
            .then(res => {
              if (res && res.code === 200) {
                console.log('watch: 数据获取成功', res)
                // 请确认后端返回的字段是 context 还是 content
                valueHtml.value = res.object.content || res.object.context || ''
              } else {
                ElMessage('获取文章数据失败，请刷新重试')
              }
            })
        }
      },

      // 4. 配置对象
      {
        // immediate: true 确保 watch 在组件创建时就立即执行一次
        // 这对于处理初始状态至关重要
        immediate: true
      }
    )
    onBeforeUnmount(() => {
      const editor = editorRef.value
      if (editor == null) return
      editor.destroy()
    })

    const handleCreated = (editor) => {
      editorRef.value = editor // 记录 editor 实例，重要！
    }
    const sendImages = () => {
      if (localFilesToUpload.size === 0) {
        sendarticle()
        return
      }
      const formData = new FormData()

      // 2. 遍历 Map，将 file 和 localId 分别添加到 FormData 中
      localFilesToUpload.forEach((file, localId) => {
        console.log('准备上传文件:', localId, file.name)
        formData.append('files', file)
        formData.append('localIds', localId)
      })
      fetch('http://localhost:9527/upload_artile/artile_img', {
        method: 'POST',
        body: formData
      }).then(res => {
        return res.json()
      }).then(res => {
        const uploadedResults = res.object

        // 4. 进行一对一替换
        let tempHtml = valueHtml.value
        uploadedResults.forEach(({ localId, realUrl }) => {
          const imgRegex = new RegExp(`src="${localId}"`, 'g')
          tempHtml = tempHtml.replace(imgRegex, `src="${realUrl}"`)
        })
        valueHtml.value = tempHtml
        sendarticle()
      })
    }

    const sendarticle = () => {
      console.log(sendarticleId.value)
      if (!sendarticleId.value) {
        fetch('http://localhost:9527/insertarticle', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({

            title: props.title,
            context: valueHtml.value
          })
        }).then(res => {
          return res.json()
        }).then(res => {
          ElMessage(res.message)
        })
      } else {
        fetch('http://localhost:9527/updatearticle', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            id: sendarticleId.value,
            title: props.title,
            context: valueHtml.value
          })
        }).then(res => {
          return res.json()
        }).then(res => {
          ElMessage(res.message)
        })
      }
    }
    const Md5 = (file) => {
      return new Promise((resolve, reject) => {
        const reader = new FileReader()

        // 1. 先告诉 reader 读取完之后要做什么
        reader.onload = (e) => {
          // 这里的 e 是事件对象，e.target.result 就是读到的文件二进制数据
          const spark = new SparkMD5.ArrayBuffer()
          spark.append(e.target.result)
          const md5 = spark.end()
          resolve(md5) // 成功后，把结果“发货”出去
        }

        reader.onerror = (e) => {
          reject(e) // 失败了，把错误“发货”出去
        }

        // 3. 最后开始正式读取
        reader.readAsArrayBuffer(file)
      })
    }
    return {
      editorRef,
      valueHtml,
      mode: 'default', // 或 'simple'
      toolbarConfig,
      editorConfig,
      handleCreated,
      changeshow,
      show,
      sendarticle,
      sendImages,
      drawershow,
      changeimageshow
    }
  }
}
</script>
<style scoped>
.footer {
  display: flex;
  justify-content: center;
  align-items: center;
  border-top: 1.5px solid rgb(204, 204, 204);
  padding: 10px 0 10px 0;
  height: 3%;
}
</style>
