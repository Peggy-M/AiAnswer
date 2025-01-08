<template>
  <div class="course-learning">
    <el-card v-loading="loading">
      <template #header>
        <div class="header">
          <h2>{{ course.title }}</h2>
          <el-button link @click="router.back()">返回</el-button>
        </div>
      </template>

      <!-- 视频课程 -->
      <template v-if="course.type === 'VIDEO'">
        <div class="video-container">
          <video v-if="course.videoUrl" :src="course.videoUrl" controls></video>
          <div v-else class="empty-tip">暂无视频内容</div>
        </div>
      </template>

      <!-- 图文课程 -->
      <template v-else-if="course.type === 'TEXT'">
        <div class="text-content" v-html="course.content"></div>
      </template>

      <!-- 直播课程 -->
      <template v-else-if="course.type === 'LIVE'">
        <div class="live-container">
          <template v-if="course.liveStatus === 'NOT_STARTED'">
            <el-empty description="直播未开始">
              <template #description>
                <p>直播开始时间：{{ formatDateTime(course.startTime) }}</p>
              </template>
            </el-empty>
          </template>
          <template v-else-if="course.liveStatus === 'LIVING'">
            <div class="live-player">
              <!-- 这里放直播播放器组件 -->
              <div>直播进行中...</div>
            </div>
          </template>
          <template v-else>
            <el-empty description="直播已结束">
              <template #description>
                <p>直播结束时间：{{ formatDateTime(course.endTime) }}</p>
              </template>
            </el-empty>
          </template>
        </div>
      </template>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { courseApi } from '@/api/course'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const course = ref({})

// 获取课程信息
const fetchCourse = async () => {
  loading.value = true
  try {
    const [courseData, contentData] = await Promise.all([
      courseApi.getCourseDetail(route.params.id),
      courseApi.getCourseContent(route.params.id)
    ])
    course.value = { ...courseData, ...contentData }
    console.log('Course data:', course.value)  // 添加调试日志
  } catch (error) {
    console.error('Failed to fetch course:', error)
    ElMessage.error('获取课程信息失败')
  } finally {
    loading.value = false
  }
}

// 格式化日期时间
const formatDateTime = (datetime) => {
  if (!datetime) return ''
  const date = new Date(datetime)
  return date.toLocaleString()
}

onMounted(fetchCourse)
</script>

<style lang="scss" scoped>
.course-learning {
  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    h2 {
      margin: 0;
    }
  }
  
  .video-container {
    width: 100%;
    max-width: 800px;
    margin: 0 auto;
    
    video {
      width: 100%;
    }
  }
  
  .text-content {
    padding: 20px;
    line-height: 1.6;
    
    :deep(img) {
      max-width: 100%;
    }
  }
  
  .live-container {
    min-height: 400px;
    display: flex;
    justify-content: center;
    align-items: center;
    
    .live-player {
      width: 100%;
      max-width: 800px;
    }
  }
  
  .empty-tip {
    text-align: center;
    color: #909399;
    padding: 40px;
  }
}
</style> 