<template>
  <div class="live-learning">
    <el-row :gutter="20">
      <el-col :span="18">
        <el-card class="video-card">
          <template #header>
            <div class="course-header">
              <h2>{{ courseDetail?.title }}</h2>
              <el-tag :type="isLive ? 'success' : 'info'">
                {{ isLive ? '直播中' : '未开始' }}
              </el-tag>
            </div>
          </template>
          
          <div class="video-area">
            <div v-if="isLive" class="live-player">
              <!-- 直播播放器组件 -->
              <el-empty description="直播播放器待接入" />
            </div>
            <div v-else class="waiting">
              <el-empty>
                <template #description>
                  <div>
                    <p>直播未开始</p>
                    <p v-if="startTime">开始时间：{{ startTime }}</p>
                  </div>
                </template>
              </el-empty>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="chat-card">
          <template #header>
            <div class="chat-header">
              <span>直播互动</span>
              <el-badge :value="onlineCount" class="online-count">
                在线人数
              </el-badge>
            </div>
          </template>
          
          <div class="chat-messages">
            <el-empty description="暂无消息" />
          </div>
          
          <div class="chat-input">
            <el-input
              v-model="message"
              placeholder="发送消息"
              :disabled="!isLive">
              <template #append>
                <el-button type="primary" :disabled="!isLive">
                  发送
                </el-button>
              </template>
            </el-input>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { courseApi } from '@/api/course'

export default {
  name: 'LiveLearning',
  
  setup() {
    const route = useRoute()
    const courseDetail = ref(null)
    const isLive = ref(false)
    const startTime = ref(null)
    const onlineCount = ref(0)
    const message = ref('')

    const fetchCourseDetail = async () => {
      try {
        const data = await courseApi.getCourseDetail(route.params.id)
        courseDetail.value = data
        // 这里可以添加获取直播状态的逻辑
        isLive.value = false // 临时写死
        startTime.value = '2024-01-08 20:00'
        onlineCount.value = 0
      } catch (error) {
        console.error('Failed to fetch course:', error)
        ElMessage.error('获取课程详情失败')
      }
    }

    onMounted(fetchCourseDetail)

    return {
      courseDetail,
      isLive,
      startTime,
      onlineCount,
      message
    }
  }
}
</script>

<style lang="scss" scoped>
.live-learning {
  padding: 20px;
  
  .video-card {
    .course-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      
      h2 {
        margin: 0;
      }
    }
    
    .video-area {
      min-height: 500px;
      background: #000;
      display: flex;
      justify-content: center;
      align-items: center;
      
      .waiting {
        width: 100%;
        height: 100%;
        display: flex;
        justify-content: center;
        align-items: center;
        color: #fff;
      }
    }
  }
  
  .chat-card {
    height: 100%;
    
    .chat-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    
    .chat-messages {
      height: 400px;
      overflow-y: auto;
      padding: 10px;
      background: #f8f9fa;
      border-radius: 4px;
      margin-bottom: 10px;
    }
    
    .chat-input {
      padding: 10px 0;
    }
  }
}
</style> 