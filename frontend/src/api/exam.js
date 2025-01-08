import request from '@/utils/request'

export const examApi = {
    // 获取考试列表
    getExamList(params) {
        return request({
            url: '/exam/list',
            method: 'get',
            params
        })
    },
    
    // 获取考试详情
    getExamDetail(id) {
        return request({
            url: `/exam/${id}`,
            method: 'get'
        })
    },
    
    // 提交考试答案
    submitExam(id, answers) {
        return request({
            url: `/exam/${id}/submit`,
            method: 'post',
            data: answers
        })
    }
}

export default examApi 