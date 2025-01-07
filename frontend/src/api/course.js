import request from '@/utils/request'

// 将所有API方法封装成一个对象导出
export const courseApi = {
  // 获取课程列表
  getCourseList(params) {
    console.log('Calling getCourseList with params:', params)
    return request({
      url: '/courses',
      method: 'get',
      params
    })
  },

  getLearningCourses(params) {
    return request({
      url: '/courses/learning',
      method: 'get',
      params
    })
  },

  // 获取课程详情
  getCourseDetail(id) {
    return request({
      url: `/courses/${id}`,
      method: 'get'
    })
  },

  // 创建课程
  createCourse(data) {
    return request({
      url: '/courses',
      method: 'post',
      data
    })
  },

  // 更新课程
  updateCourse(id, data) {
    return request({
      url: `/courses/${id}`,
      method: 'put',
      data
    })
  },

  // 删除课程
  deleteCourse(id) {
    if (!id || id === 'undefined') {
      return Promise.reject(new Error('Invalid course ID'))
    }
    return request({
      url: `/courses/${id}`,
      method: 'delete'
    })
  },

  // 加入课程
  joinCourse(courseId) {
    return request({
      url: `/courses/${courseId}/join`,
      method: 'post'
    })
  },

  // 获取课程学习状态
  getCourseStatus(courseId) {
    return request({
      url: `/courses/${courseId}/status`,
      method: 'get'
    })
  }
}

// 默认导出
export default courseApi 