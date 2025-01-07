import axios from 'axios'
import { ElMessage } from 'element-plus'

// 创建 axios 实例
const request = axios.create({
    baseURL: 'http://localhost:8080/api',
    timeout: 5000
})

// 添加请求拦截器
request.interceptors.request.use(
    config => {
        console.log('Request Config:', config)
        // 这里可以添加token等认证信息
        // if (store.getters.token) {
        //     config.headers['Authorization'] = `Bearer ${store.getters.token}`
        // }
        return config
    },
    error => {
        console.error('Request Error:', error)
        return Promise.reject(error)
    }
)

// 添加响应拦截器
request.interceptors.response.use(
    response => {
        console.log('Response:', response)
        return response
    },
    error => {
        console.error('API Error:', error.response || error)
        const message = error.response?.data?.message || error.message || '请求失败'
        ElMessage.error(message)
        return Promise.reject(error)
    }
)

export default request 