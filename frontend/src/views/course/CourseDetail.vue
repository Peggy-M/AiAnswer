<template>
  <div class="course-detail">
    <el-card v-loading="loading">
      <template #header>
        <div class="header">
          <h2>{{ course.title }}</h2>
          <el-button link @click="router.back()">返回</el-button>
        </div>
      </template>

      <!-- 课程基本信息 -->
      <div class="course-info">
        <div class="cover">
          <img :src="course.coverImage" :alt="course.title">
        </div>
        <div class="info">
          <el-descriptions :column="1">
            <el-descriptions-item label="课程类型">
              <el-tag :type="getTypeTagType(course.type)">
                {{ getTypeText(course.type) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="科目">
              {{ course.subject }}
            </el-descriptions-item>
            <el-descriptions-item label="价格">
              {{ course.price ? `¥${course.price}` : '免费' }}
            </el-descriptions-item>
            <el-descriptions-item label="学习人数">
              {{ course.studentCount || 0 }}
            </el-descriptions-item>
          </el-descriptions>
          
          <div class="actions">
            <el-button 
              v-if="!course.joined" 
              type="primary" 
              @click="joinCourse"
              :loading="joining"
            >
              立即学习
            </el-button>
            <el-button 
              v-else 
              type="success" 
              @click="continueLearning"
            >
              继续学习
            </el-button>
          </div>
        </div>
      </div>

      <!-- 课程简介 -->
      <div class="description">
        <h3>课程简介</h3>
        <p>{{ course.description }}</p>
      </div>

      <!-- 课程大纲 -->
      <div class="outline" v-if="course.outline">
        <h3>课程大纲</h3>
        <el-tree :data="course.outline" :props="defaultProps" />
      </div>
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
const joining = ref(false)
const course = ref({})

// 获取课程详情
const fetchCourse = async () => {
  loading.value = true
  try {
    const data = await courseApi.getCourseDetail(route.params.id)
    course.value = data
  } catch (error) {
    console.error('Failed to fetch course:', error)
    ElMessage.error('获取课程信息失败')
  } finally {
    loading.value = false
  }
}

// 加入课程
const joinCourse = async () => {
  joining.value = true
  try {
    await courseApi.joinCourse(course.value.id)
    ElMessage.success('加入课程成功')
    course.value.joined = true
    router.push(`/course/learning/${course.value.id}`)
  } catch (error) {
    ElMessage.error('加入课程失败')
  } finally {
    joining.value = false
  }
}

// 继续学习
const continueLearning = () => {
  router.push(`/course/learning/${course.value.id}`)
}

// 获取课程类型标签样式
const getTypeTagType = (type) => {
  switch (type) {
    case 'VIDEO':
      return 'success'
    case 'TEXT':
      return 'info'
    case 'LIVE':
      return 'warning'
    default:
      return 'default'
  }
}

// 获取课程类型文本
const getTypeText = (type) => {
  switch (type) {
    case 'VIDEO':
      return '视频课程'
    case 'TEXT':
      return '图文课程'
    case 'LIVE':
      return '直播课程'
    default:
      return '未知类型'
  }
}

// 树形控件配置
const defaultProps = {
  children: 'children',
  label: 'title'
}

onMounted(fetchCourse)
</script>

<style lang="scss" scoped>
.course-detail {
  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    h2 {
      margin: 0;
    }
  }
  
  .course-info {
    display: flex;
    gap: 20px;
    margin: 20px 0;
    
    .cover {
      width: 400px;
      height: 225px;
      overflow: hidden;
      border-radius: 4px;
      
      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
    }
    
    .info {
      flex: 1;
      
      .actions {
        margin-top: 20px;
      }
    }
  }
  
  .description {
    margin: 20px 0;
    
    h3 {
      margin: 0 0 10px;
      font-size: 18px;
    }
    
    p {
      color: #666;
      line-height: 1.6;
    }
  }
  
  .outline {
    margin: 20px 0;
    
    h3 {
      margin: 0 0 10px;
      font-size: 18px;
    }
  }
}
</style> 