<template>
  <div class="course-content">
    <div class="header">
      <h2>课程内容管理</h2>
      <el-button link @click="router.back()">返回</el-button>
    </div>
    
    <el-card v-loading="loading">
      <template #header>
        <div class="card-header">
          <span>{{ course.title }}</span>
          <el-tag :type="getTypeTagType(course.type)">
            {{ getTypeText(course.type) }}
          </el-tag>
        </div>
      </template>
      
      <!-- 视频课程 -->
      <template v-if="course.type === 'VIDEO'">
        <el-upload
          class="upload-video"
          action="/api/upload/video"
          :auto-upload="false"
          :limit="1"
          accept="video/*"
          :before-upload="beforeVideoUpload"
          :on-success="handleUploadSuccess"
          :on-error="handleUploadError">
          <template #trigger>
            <el-button type="primary">选择视频</el-button>
          </template>
          <template #tip>
            <div class="el-upload__tip">
              支持 MP4、WebM 格式视频，大小不超过 500MB
            </div>
          </template>
        </el-upload>
        
        <div v-if="course.videoUrl" class="preview-video">
          <video :src="course.videoUrl" controls></video>
        </div>
      </template>
      
      <!-- 图文课程 -->
      <template v-else-if="course.type === 'TEXT'">
        <div class="rich-editor">
          <Toolbar
            style="border-bottom: 1px solid #ccc"
            :editor="editorRef"
            :defaultConfig="toolbarConfig"
            mode="default"
          />
          <Editor
            style="height: 500px"
            v-model="course.content"
            :defaultConfig="editorConfig"
            mode="default"
            @onCreated="handleEditorCreated"
          />
        </div>
        <div class="editor-actions">
          <el-button type="primary" @click="saveContent" :loading="saving">
            保存内容
          </el-button>
        </div>
      </template>
      
      <!-- 直播课程 -->
      <template v-else-if="course.type === 'LIVE'">
        <el-form :model="liveForm" label-width="100px">
          <el-form-item label="直播时间">
            <el-date-picker
              v-model="liveForm.startTime"
              type="datetime"
              placeholder="选择直播开始时间"
            />
          </el-form-item>
          <el-form-item label="直播地址">
            <el-input v-model="liveForm.streamUrl" placeholder="输入直播推流地址" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="saveLiveSettings" :loading="saving">
              保存设置
            </el-button>
          </el-form-item>
        </el-form>
      </template>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, shallowRef } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { courseApi } from '@/api/course'
import '@wangeditor/editor/dist/css/style.css'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const saving = ref(false)
const course = ref({})
const editorRef = shallowRef()

// 编辑器配置
const toolbarConfig = { /* ... */ }
const editorConfig = { /* ... */ }

// 获取课程信息
const fetchCourse = async () => {
  loading.value = true
  try {
    const [courseData, contentData] = await Promise.all([
      courseApi.getCourseDetail(route.params.id),
      courseApi.getCourseContent(route.params.id)
    ])
    course.value = { ...courseData, ...contentData }
  } catch (error) {
    ElMessage.error('获取课程信息失败')
  } finally {
    loading.value = false
  }
}

// 保存内容
const saveContent = async () => {
  saving.value = true
  try {
    await courseApi.uploadContent(course.value.id, {
      content: course.value.content
    })
    ElMessage.success('保存成功')
  } catch (error) {
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

onMounted(fetchCourse)
</script>

<style lang="scss" scoped>
.course-content {
  padding: 20px;
  
  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
  }
  
  .card-header {
    display: flex;
    align-items: center;
    gap: 12px;
  }
  
  .preview-video {
    margin-top: 20px;
    video {
      max-width: 100%;
    }
  }
  
  .editor-actions {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}
</style> 