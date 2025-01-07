import request from './request'

export const examApi = {
    getAllExams() {
        return request.get('/exams')
    },
    
    getExam(id) {
        return request.get(`/exams/${id}`)
    },
    
    createExam(exam) {
        return request.post('/exams', exam)
    },
    
    updateExam(id, exam) {
        return request.put(`/exams/${id}`, exam)
    },
    
    deleteExam(id) {
        return request.delete(`/exams/${id}`)
    },
    
    publishExam(id) {
        return request.post(`/exams/${id}/publish`)
    },
    
    startExam(examId, userId) {
        return request.post(`/exams/${examId}/start?userId=${userId}`)
    },
    
    submitExam(examId, userId, answers) {
        return request.post(`/exams/${examId}/submit?userId=${userId}`, answers)
    },
    
    getUserExamRecords(userId) {
        return request.get(`/exams/user/${userId}`)
    },
    
    generateRandomPaper(subject, questionCount) {
        return request.post('/exams/generate', null, {
            params: { subject, questionCount }
        })
    }
} 