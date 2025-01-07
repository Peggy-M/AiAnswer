<template>
  <div class="course-list">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>全部课程</span>
      </div>
      
      <el-row :gutter="20">
        <el-col :span="6" v-for="course in courseList" :key="course.id">
          <el-card :body-style="{ padding: '0px' }" class="course-card">
            <img :src="course.coverImage || defaultImage" class="image">
            <div style="padding: 14px;">
              <span>{{ course.title }}</span>
              <div class="bottom">
                <el-button type="text" class="button" @click="viewCourse(course.id)">
                  查看详情
                </el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      
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
    </el-card>
  </div>
</template>

<script>
import { courseApi } from '@/api/course'

export default {
  name: 'CourseList',
  data() {
    return {
      courseList: [],
      page: 1,
      size: 8,
      total: 0,
      loading: false,
      defaultImage: require('@/assets/default-course.jpg')
    }
  },
  created() {
    this.fetchCourses()
  },
  methods: {
    async fetchCourses() {
      this.loading = true
      try {
        const response = await courseApi.getCourseList({
          page: this.page - 1,
          size: this.size
        })
        this.courseList = response.content
        this.total = response.totalElements
      } catch (error) {
        console.error('Failed to fetch courses:', error)
        this.$message.error('获取课程列表失败')
      } finally {
        this.loading = false
      }
    },
    handleSizeChange(val) {
      this.size = val
      this.page = 1
      this.fetchCourses()
    },
    handleCurrentChange(val) {
      this.page = val
      this.fetchCourses()
    },
    viewCourse(id) {
      this.$router.push(`/course/${id}`)
    }
  }
}
</script>

<style scoped>
.course-list {
  padding: 20px;
}
.course-card {
  margin-bottom: 20px;
  transition: all 0.3s;
}
.course-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
}
.image {
  width: 100%;
  height: 160px;
  object-fit: cover;
}
.bottom {
  margin-top: 13px;
  line-height: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.pagination-container {
  text-align: center;
  margin-top: 20px;
}
</style> 