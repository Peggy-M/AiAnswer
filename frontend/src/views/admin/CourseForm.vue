<template>
  <div class="course-form">
    <el-card>
      <template #header>
        <div class="header">
          <h2>{{ isEdit ? '编辑课程' : '创建课程' }}</h2>
          <el-button link @click="router.back()">返回</el-button>
        </div>
      </template>

      <el-form 
        :model="form" 
        :rules="rules"
        ref="formRef"
        label-width="100px"
      >
        <el-form-item label="课程名称" prop="title">
          <el-input v-model="form.title" placeholder="请输入课程名称" />
        </el-form-item>
        
        <el-form-item label="课程类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择课程类型">
            <el-option
              v-for="item in courseTypes"
              :key="item.value"
              :label="item.label"
              :value="item.value">
              <el-icon class="type-icon">
                <VideoCamera v-if="item.value === 'VIDEO'" />
                <Reading v-if="item.value === 'TEXT'" />
                <VideoCameraFilled v-if="item.value === 'LIVE'" />
              </el-icon>
              {{ item.label }}
            </el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="科目" prop="subject">
          <el-select v-model="form.subject" placeholder="请选择科目">
            <el-option
              v-for="item in subjects"
              :key="item.value"
              :label="item.label"
              :value="item.value">
              {{ item.label }}
            </el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="课程封面">
          <el-upload
            class="cover-uploader"
            action="/api/upload/image"
            :headers="uploadHeaders"
            :show-file-list="false"
            :on-error="handleUploadError"
            :before-upload="beforeCoverUpload"
            :on-success="handleCoverSuccess">
            <img v-if="form.coverImage" :src="form.coverImage" class="cover" />
            <el-icon v-else class="cover-uploader-icon"><Plus /></el-icon>
          </el-upload>
          <div class="upload-tip">建议尺寸：800x450px，支持 jpg、png 格式</div>
        </el-form-item>
        
        <el-form-item label="课程简介" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="4"
            placeholder="请输入课程简介" />
        </el-form-item>
        
        <el-form-item label="课程价格" prop="price">
          <el-input-number
            v-model="form.price"
            :min="0"
            :precision="2"
            :step="10" />
        </el-form-item>
        
        <el-form-item label="发布状态">
          <el-switch
            v-model="form.isPublished"
            :active-text="form.isPublished ? '已发布' : '草稿'"
            :inactive-text="form.isPublished ? '已发布' : '草稿'" />
        </el-form-item>
        
        <el-form-item 
          v-if="form.type === 'TEXT'"
          label="课程内容"
          prop="content"
        >
          <div class="editor-container">
            <Toolbar
              style="border-bottom: 1px solid #ccc"
              :editor="editorRef"
              :defaultConfig="toolbarConfig"
              :mode="mode"
            />
            <Editor
              style="height: 500px"
              v-model="form.content"
              :defaultConfig="editorConfig"
              :mode="mode"
              @onCreated="handleCreated"
            />
          </div>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="submitForm" :loading="loading">
            {{ isEdit ? '保存修改' : '创建课程' }}
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, shallowRef, onBeforeUnmount } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { courseApi } from '@/api/course'
import '@wangeditor/editor/dist/css/style.css'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import { 
  VideoCamera, 
  Reading, 
  VideoCameraFilled, 
  Plus 
} from '@element-plus/icons-vue'
import { editorConfig, toolbarConfig } from '@/config/editor'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const route = useRoute()
const formRef = ref(null)
const submitting = ref(false)

const isEdit = computed(() => route.name === 'EditCourse')

// 表单数据
const form = ref({
  title: '',
  type: '',
  subject: '',
  description: '',
  coverImage: '',
  price: 0,
  isPublished: false,
  content: ''
})

// 课程类型选项
const courseTypes = [
  { label: '视频课程', value: 'VIDEO' },
  { label: '图文课程', value: 'TEXT' },
  { label: '直播课程', value: 'LIVE' }
]

// 科目选项
const subjects = [
  { label: '数学', value: 'MATH' },
  { label: '英语', value: 'ENGLISH' },
  { label: '物理', value: 'PHYSICS' },
  { label: '化学', value: 'CHEMISTRY' },
  { label: '生物', value: 'BIOLOGY' },
  { label: '编程', value: 'PROGRAMMING' }
]

// 表单验证规则
const rules = {
  title: [
    { required: true, message: '请输入课程名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请选择课程类型', trigger: 'change' }
  ],
  subject: [
    { required: true, message: '请选择科目', trigger: 'change' }
  ],
  description: [
    { required: true, message: '请输入课程简介', trigger: 'blur' },
    { min: 10, max: 500, message: '长度在 10 到 500 个字符', trigger: 'blur' }
  ],
  content: [
    { 
      required: true, 
      message: '请输入课程内容', 
      trigger: 'blur',
      validator: (rule, value, callback) => {
        if (form.value.type === 'TEXT' && !value) {
          callback(new Error('请输入课程内容'))
        } else {
          callback()
        }
      }
    }
  ]
}

// 获取课程详情
const fetchCourseDetail = async (id) => {
  try {
    const data = await courseApi.getCourseDetail(id)
    form.value = {
      ...data,
      price: data.price || 0
    }
  } catch (error) {
    ElMessage.error('获取课程详情失败')
    router.back()
  }
}

// 封面上传前的验证
const beforeCoverUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('上传封面只能是图片格式!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('上传封面图片大小不能超过 2MB!')
    return false
  }
  return true
}

// 封面上传成功的回调
const handleCoverSuccess = (res) => {
  form.value.coverImage = res.url
  ElMessage.success('封面上传成功')
}

// 提交表单
const submitForm = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitting.value = true
    
    // 处理富文本内容
    const formData = {
      ...form.value,
      content: form.value.type === 'TEXT' ? form.value.content : undefined
    }
    
    if (isEdit.value) {
      await courseApi.updateCourse(route.params.id, formData)
      ElMessage.success('课程更新成功')
    } else {
      await courseApi.createCourse(formData)
      ElMessage.success('课程创建成功')
    }
    
    router.push('/admin/courses')
  } catch (error) {
    if (error.message) {
      ElMessage.error(error.message)
    } else {
      ElMessage.error(isEdit.value ? '更新失败' : '创建失败')
    }
  } finally {
    submitting.value = false
  }
}

// 重置表单
const resetForm = () => {
  if (!formRef.value) return
  formRef.value.resetFields()
  if (isEdit.value) {
    fetchCourseDetail(route.params.id)
  }
}

// 在编辑模式下，获取课程详情
onMounted(() => {
  if (isEdit.value && route.params.id) {
    fetchCourseDetail(route.params.id)
  }
})

// 编辑器实例，必须用 shallowRef
const editorRef = shallowRef()
const mode = 'default'

// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})

const handleCreated = (editor) => {
  editorRef.value = editor
}

const handleChange = (editor) => {
  form.value.content = editor.getHtml()
}

const userStore = useUserStore()

// 上传请求头
const uploadHeaders = computed(() => ({
  Authorization: `Bearer ${userStore.token}`
}))

// 添加上传错误处理
const handleUploadError = (err) => {
  console.error('Upload error:', err)
  ElMessage.error('上传失败：' + (err.message || '未知错误'))
}
</script>

<style lang="scss" scoped>
.course-form {
  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    h2 {
      margin: 0;
    }
  }
  
  .editor-container {
    border: 1px solid #ccc;
    z-index: 100;
  }
}

// 编辑器样式
:deep(.w-e-text-container) {
  min-height: 500px;
}
</style> 