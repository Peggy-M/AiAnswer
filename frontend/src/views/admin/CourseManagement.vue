<template>
  <div class="course-management">
    <div class="header">
      <h2>课程管理</h2>
      <el-button type="primary" @click="router.push('/admin/courses/create')">
        <el-icon><Plus /></el-icon>
        创建课程
      </el-button>
    </div>
    
    <el-table
      v-loading="loading"
      :data="courseList"
      border
      style="width: 100%">
      <el-table-column prop="title" label="课程名称" min-width="200" />
      <el-table-column prop="type" label="类型" width="120">
        <template #default="{ row }">
          <el-tag :type="getTypeTagType(row.type)">
            {{ getTypeText(row.type) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="subject" label="科目" width="120" />
      <el-table-column prop="isPublished" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.isPublished ? 'success' : 'info'">
            {{ row.isPublished ? '已发布' : '草稿' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="250" fixed="right">
        <template #default="{ row }">
          <el-button-group>
            <el-button type="primary" link @click="editCourse(row)">
              编辑
            </el-button>
            <el-button type="primary" link @click="manageCourseContent(row)">
              管理内容
            </el-button>
            <el-button 
              type="primary" 
              link 
              @click="togglePublish(row)"
              :loading="publishing === row.id">
              {{ row.isPublished ? '取消发布' : '发布' }}
            </el-button>
            <el-button type="danger" link @click="deleteCourse(row)">
              删除
            </el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>
    
    <div class="pagination">
      <el-pagination
        v-model:current-page="page"
        v-model:page-size="size"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { courseApi } from '@/api/course'

const router = useRouter()
const loading = ref(false)
const publishing = ref(null)
const courseList = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)

// 获取课程列表
const fetchCourses = async () => {
  loading.value = true
  try {
    const res = await courseApi.getCourseList({
      page: page.value - 1,
      size: size.value
    })
    courseList.value = res.content
    total.value = res.totalElements
  } catch (error) {
    ElMessage.error('获取课程列表失败')
  } finally {
    loading.value = false
  }
}

// 课程类型标签
const getTypeTagType = (type) => {
  const types = {
    VIDEO: '',
    TEXT: 'success',
    LIVE: 'warning'
  }
  return types[type] || 'info'
}

// 课程类型文本
const getTypeText = (type) => {
  const types = {
    VIDEO: '视频课程',
    TEXT: '图文课程',
    LIVE: '直播课程'
  }
  return types[type] || '未知类型'
}

// 编辑课程
const editCourse = (course) => {
  router.push(`/admin/courses/edit/${course.id}`)
}

// 管理课程内容
const manageCourseContent = (course) => {
  router.push(`/admin/courses/${course.id}/content`)
}

// 发布/取消发布课程
const togglePublish = async (course) => {
  publishing.value = course.id
  try {
    await courseApi.updateCourse(course.id, {
      ...course,
      isPublished: !course.isPublished
    })
    course.isPublished = !course.isPublished
    ElMessage.success(course.isPublished ? '发布成功' : '已取消发布')
  } catch (error) {
    ElMessage.error('操作失败')
  } finally {
    publishing.value = null
  }
}

// 删除课程
const deleteCourse = async (course) => {
  try {
    await ElMessageBox.confirm(
      '确定要删除该课程吗？删除后无法恢复。',
      '警告',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await courseApi.deleteCourse(course.id)
    ElMessage.success('删除成功')
    fetchCourses()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 分页处理
const handleSizeChange = (val) => {
  size.value = val
  page.value = 1
  fetchCourses()
}

const handleCurrentChange = (val) => {
  page.value = val
  fetchCourses()
}

onMounted(fetchCourses)
</script>

<style lang="scss" scoped>
.course-management {
  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    
    h2 {
      margin: 0;
    }
  }
  
  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}
</style> 