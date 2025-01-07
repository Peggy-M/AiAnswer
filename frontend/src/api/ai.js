import request from './request'

export const aiApi = {
    analyzePerformance(userId) {
        return request.get(`/ai/analyze/${userId}`)
    },
    
    getSuggestions(userId) {
        return request.get(`/ai/suggestions/${userId}`)
    },
    
    generateQuestions(topic, count) {
        return request.get('/ai/questions/generate', {
            params: { topic, count }
        })
    }
} 