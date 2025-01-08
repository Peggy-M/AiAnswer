<template>
  <div class="courses-container">
    <!-- 搜索和筛选部分 -->
    <div class="filter-section">
      <el-form :inline="true" :model="searchForm">
        <el-form-item>
          <el-input
            v-model="searchForm.keyword"
            placeholder="搜索课程"
            clearable
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item>
          <el-select v-model="searchForm.type" placeholder="课程类型" clearable>
            <el-option label="视频课程" value="VIDEO" />
            <el-option label="图文课程" value="TEXT" />
            <el-option label="直播课程" value="LIVE" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 课程列表 -->
    <el-table :data="courseList" v-loading="loading">
      <el-table-column prop="title" label="课程名称" min-width="200" />
      <el-table-column prop="type" label="类型" width="120">
        <template #default="{ row }">
          <el-tag :type="getTypeTagType(row.type)">
            {{ getTypeText(row.type) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="subject" label="科目" width="120" />
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button-group>
            <el-button type="primary" link @click="viewCourse(row)">
              查看
            </el-button>
            <el-button 
              v-if="!row.joined" 
              type="primary" 
              link 
              @click="joinCourse(row)">
              加入学习
            </el-button>
            <el-button 
              v-else 
              type="success" 
              link 
              @click="continueLearning(row)">
              继续学习
            </el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination">
      <el-pagination
        background
        v-model:current-page="page"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="sizes, prev, pager, next, total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { courseApi } from '@/api/course'

const router = useRouter()
const loading = ref(false)
const courseList = ref([])
const total = ref(0)
const page = ref(1)
const pageSize = ref(10)

const searchForm = ref({
  keyword: '',
  type: ''
})

// 获取课程列表
const fetchCourses = async () => {
  loading.value = true
  try {
    const params = {
      page: page.value - 1,
      size: pageSize.value,
      type: searchForm.value.type || undefined,
      keyword: searchForm.value.keyword || undefined
    }
    const { content, totalElements } = await courseApi.getCourseList(params)
    courseList.value = content
    total.value = totalElements
  } catch (error) {
    console.error('Failed to fetch courses:', error)
    ElMessage.error('获取课程列表失败')
  } finally {
    loading.value = false
  }
}

// 查看课程
const viewCourse = (course) => {
  router.push(`/course/${course.id}`)
}

// 加入课程
const joinCourse = async (course) => {
  try {
    await courseApi.joinCourse(course.id)
    ElMessage.success('加入课程成功')
    course.joined = true
    router.push(`/course/learning/${course.id}`)
  } catch (error) {
    ElMessage.error('加入课程失败')
  }
}

// 继续学习
const continueLearning = (course) => {
  router.push(`/course/learning/${course.id}`)
}

// 搜索
const handleSearch = () => {
  page.value = 1
  fetchCourses()
}

// 分页处理
const handleSizeChange = (val) => {
  pageSize.value = val
  page.value = 1
  fetchCourses()
}

const handleCurrentChange = (val) => {
  page.value = val
  fetchCourses()
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

onMounted(fetchCourses)
</script>

<style lang="scss" scoped>
.courses-container {
  padding: 20px;
  
  .filter-section {
    margin-bottom: 20px;
  }
  
  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}
</style> 