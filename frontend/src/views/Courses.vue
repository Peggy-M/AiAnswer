<template>
  <div class="courses-container">
    <!-- 课程分类筛选 -->
    <el-card class="filter-card">
      <el-form :inline="true" :model="filterForm">
        <el-form-item label="课程类型">
          <el-select v-model="filterForm.type" placeholder="全部类型">
            <el-option label="图文课程" value="text"></el-option>
            <el-option label="视频课程" value="video"></el-option>
            <el-option label="直播课程" value="live"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="科目">
          <el-select v-model="filterForm.subject" placeholder="全部科目">
            <el-option label="数学" value="math"></el-option>
            <el-option label="英语" value="english"></el-option>
            <el-option label="物理" value="physics"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleFilter">筛选</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 课程列表 -->
    <el-row :gutter="20" class="course-list" v-loading="loading">
      <el-col :span="6" v-for="course in courseList" :key="course.id">
        <el-card class="course-card" :body-style="{ padding: '0px' }">
          <!-- 课程封面 -->
          <div class="course-cover" @click="viewCourse(course)">
            <el-image 
              :src="course.coverImage || defaultImage" 
              fit="cover">
              <template #error>
                <div class="image-placeholder">
                  <el-icon><Picture /></el-icon>
                </div>
              </template>
            </el-image>
            <div class="course-type-tag" :class="course.type">
              <el-icon>
                <VideoCamera v-if="course.type === 'video'" />
                <Reading v-else-if="course.type === 'text'" />
                <VideoCameraFilled v-else />
              </el-icon>
              {{ getTypeText(course.type) }}
            </div>
          </div>
          
          <!-- 课程信息 -->
          <div class="course-info">
            <h3 class="course-title" @click="viewCourse(course)">
              {{ course.title }}
            </h3>
            <p class="course-desc">{{ course.description }}</p>
            <div class="course-meta">
              <span>
                <el-icon><User /></el-icon>
                {{ course.studentCount }}人学习
              </span>
              <span>
                <el-icon><Star /></el-icon>
                {{ course.rating }}分
              </span>
            </div>
            <div class="course-actions">
              <el-button 
                type="primary" 
                @click="startLearning(course)"
                :loading="startingCourse === course.id">
                开始学习
              </el-button>
              <el-button 
                type="text" 
                @click="showPreview(course)"
                :disabled="!course.previewUrl && !course.previewContent">
                <el-icon><VideoPlay /></el-icon>
                课程预览
              </el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="page"
        :page-sizes="[8, 16, 24, 32]"
        :page-size="size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>

    <!-- 课程预览弹窗 -->
    <el-dialog
      v-model="previewVisible"
      :title="previewCourse.title"
      width="70%"
      destroy-on-close>
      <div class="preview-content">
        <!-- 视频预览 -->
        <template v-if="previewCourse.type === 'video' && previewCourse.previewUrl">
          <video-player 
            :source="previewCourse.previewUrl"
            @play="handlePreviewPlay"
            @ended="handlePreviewEnded" />
        </template>
        <!-- 图文预览 -->
        <template v-else>
          <div class="text-preview" v-html="previewCourse.previewContent"></div>
        </template>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="previewVisible = false">关闭</el-button>
          <el-button type="primary" @click="startLearning(previewCourse)">
            开始学习完整课程
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { courseApi } from '@/api/course'
import VideoPlayer from '@/components/VideoPlayer.vue'

export default {
  name: 'Courses',
  components: {
    VideoPlayer
  },
  setup() {
    const router = useRouter()
    const courseList = ref([])
    const loading = ref(false)
    const page = ref(1)
    const size = ref(8)
    const total = ref(0)
    const startingCourse = ref(null)
    const defaultImage = new URL('@/assets/default-course.jpg', import.meta.url).href
    
    const filterForm = ref({
      type: '',
      subject: ''
    })

    const previewVisible = ref(false)
    const previewCourseData = ref({})

    // 获取课程列表
    const fetchCourses = async () => {
      loading.value = true
      try {
        const response = await courseApi.getCourseList({
          page: page.value - 1,
          size: size.value,
          ...filterForm.value
        })
        courseList.value = response.content
        total.value = response.totalElements
      } catch (error) {
        console.error('Failed to fetch courses:', error)
        ElMessage.error('获取课程列表失败')
      } finally {
        loading.value = false
      }
    }

    // 开始学习
    const startLearning = async (course) => {
      try {
        startingCourse.value = course.id
        // 这里可以添加加入课程或创建学习记录的逻辑
        await courseApi.joinCourse(course.id)
        router.push(`/course/${course.id}/learn`)
      } catch (error) {
        ElMessage.error('加入课程失败')
      } finally {
        startingCourse.value = null
      }
    }

    // 预览课程
    const showPreview = (course) => {
      if (!course.previewUrl && !course.previewContent) {
        ElMessage.warning('该课程暂无预览内容')
        return
      }
      previewCourseData.value = course
      previewVisible.value = true
    }

    // 查看课程详情
    const viewCourse = (course) => {
      router.push(`/course/${course.id}`)
    }

    // 处理预览相关事件
    const handlePreviewPlay = () => {
      console.log('Preview started playing')
    }

    const handlePreviewEnded = () => {
      ElMessage.info('预览结束，欢迎加入完整课程')
    }

    // 获取课程类型文本
    const getTypeText = (type) => {
      const types = {
        video: '视频课程',
        text: '图文课程',
        live: '直播课程'
      }
      return types[type] || '图文课程'
    }

    onMounted(fetchCourses)

    return {
      courseList,
      loading,
      page,
      size,
      total,
      filterForm,
      defaultImage,
      startingCourse,
      previewVisible,
      previewCourse: previewCourseData,
      startLearning,
      previewCourse: showPreview,
      viewCourse,
      getTypeText,
      handlePreviewPlay,
      handlePreviewEnded
    }
  }
}
</script>

<style lang="scss" scoped>
.courses-container {
  padding: 20px;
}

.course-card {
  margin-bottom: 20px;
  transition: all 0.3s;
  cursor: pointer;
  
  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
  }
}

.course-cover {
  position: relative;
  height: 160px;
  overflow: hidden;
  
  .el-image {
    width: 100%;
    height: 100%;
  }

  .image-placeholder {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    background-color: #f5f7fa;
    color: #909399;
    font-size: 24px;
  }
}

.course-type-tag {
  position: absolute;
  top: 10px;
  right: 10px;
  padding: 4px 8px;
  border-radius: 4px;
  color: #fff;
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 4px;
  
  &.video {
    background-color: $color-primary;
  }
  
  &.live {
    background-color: $color-success;
  }
  
  &.text {
    background-color: $color-info;
  }
}

.course-info {
  padding: 14px;
}

.course-title {
  margin: 0 0 10px;
  font-size: 16px;
  font-weight: bold;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  cursor: pointer;

  &:hover {
    color: $color-primary;
  }
}

.course-desc {
  margin: 0 0 10px;
  font-size: 14px;
  color: #666;
  height: 40px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.course-meta {
  margin-bottom: 10px;
  font-size: 12px;
  color: #999;
  display: flex;
  gap: 15px;
  
  span {
    display: flex;
    align-items: center;
    gap: 4px;
  }
}

.course-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.preview-content {
  min-height: 400px;
  
  .text-preview {
    padding: 20px;
    background: #f8f9fa;
    border-radius: 4px;
  }
}

.dialog-footer {
  margin-top: 20px;
  text-align: right;
}
</style> 