<template>
  <div class="text-learning">
    <el-card class="content-card">
      <template #header>
        <div class="course-header">
          <h2>{{ courseDetail?.title }}</h2>
          <el-tag>{{ courseDetail?.type }}</el-tag>
        </div>
      </template>
      
      <div v-if="courseDetail?.content" 
           class="content" 
           v-html="courseDetail.content">
      </div>
      <el-empty v-else description="暂无内容" />
      
      <div class="progress-bar">
        <span>学习进度</span>
        <el-progress :percentage="progress" />
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { courseApi } from '@/api/course'

export default {
  name: 'TextLearning',
  
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

    onMounted(fetchCourseDetail)

    return {
      courseDetail,
      progress
    }
  }
}
</script>

<style lang="scss" scoped>
.text-learning {
  padding: 20px;
  
  .content-card {
    .course-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      
      h2 {
        margin: 0;
      }
    }
    
    .content {
      padding: 20px;
      min-height: 400px;
      line-height: 1.6;
    }
    
    .progress-bar {
      margin-top: 20px;
      padding: 20px;
      background: #f8f9fa;
      border-radius: 4px;
      
      span {
        display: block;
        margin-bottom: 10px;
      }
    }
  }
}
</style> 