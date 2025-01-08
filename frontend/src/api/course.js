import request from '@/utils/request'

// 将所有API方法封装成一个对象导出
export const courseApi = {
  // 获取课程列表
  getCourseList(params) {
    return request({
      url: '/courses',
      method: 'get',
      params: {
        page: params.page || 0,
        size: params.size || 10,
        ...params
      }
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
    return request({
      url: `/courses/${id}`,
      method: 'delete'
    })
  },

  // 获取课程内容
  getCourseContent(id) {
    return request({
      url: `/courses/${id}/content`,
      method: 'get'
    })
  },

  // 加入课程
  joinCourse(id) {
    return request({
      url: `/courses/${id}/join`,
      method: 'post'
    })
  }
}

// 默认导出
export default courseApi