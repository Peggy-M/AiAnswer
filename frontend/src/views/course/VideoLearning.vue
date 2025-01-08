<template>
  <div class="video-learning">
    <el-card class="video-container">
      <template #header>
        <div class="course-header">
          <h2>{{ courseDetail?.title }}</h2>
          <el-tag>{{ courseDetail?.type }}</el-tag>
        </div>
      </template>
      
      <video-player 
        v-if="courseDetail?.videoUrl"
        :source="courseDetail.videoUrl"
        @play="handlePlay"
        @pause="handlePause"
        @ended="handleEnded"
      />
      <div v-else class="no-video">
        <el-empty description="暂无视频内容" />
      </div>
    </el-card>

    <el-card class="course-info">
      <template #header>
        <div class="info-header">
          <span>学习进度</span>
          <el-progress :percentage="progress" />
        </div>
      </template>
      
      <div class="course-description">
        {{ courseDetail?.description }}
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { courseApi } from '@/api/course'
import VideoPlayer from '@/components/VideoPlayer.vue'

export default {
  name: 'VideoLearning',
  components: { VideoPlayer },
  
  setup() {
    const route = useRoute()
    const courseDetail = ref(null)
    const progress = ref(0)

    const fetchCourseDetail = async () => {
      try {
        const data = await courseApi.getCourseDetail(route.params.id)
        courseDetail.value = data
        // 获取学习进度
        const status = await courseApi.getCourseStatus(route.params.id)
        progress.value = status.progress || 0
      } catch (error) {
        console.error('Failed to fetch course:', error)
        ElMessage.error('获取课程详情失败')
      }
    }

    const handlePlay = () => {
      console.log('Video started playing')
    }

    const handlePause = () => {
      console.log('Video paused')
    }

    const handleEnded = () => {
      console.log('Video ended')
      progress.value = 100
      ElMessage.success('课程学习完成！')
    }

    onMounted(fetchCourseDetail)

    return {
      courseDetail,
      progress,
      handlePlay,
      handlePause,
      handleEnded
    }
  }
}
</script>

<style lang="scss" scoped>
.video-learning {
  padding: 20px;
  
  .video-container {
    margin-bottom: 20px;
    
    .course-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      
      h2 {
        margin: 0;
      }
    }
  }
  
  .no-video {
    height: 400px;
    display: flex;
    justify-content: center;
    align-items: center;
  }
  
  .course-info {
    .info-header {
      display: flex;
      align-items: center;
      gap: 20px;
    }
    
    .course-description {
      padding: 20px;
      background: #f8f9fa;
      border-radius: 4px;
      margin-top: 20px;
    }
  }
}
</style> 