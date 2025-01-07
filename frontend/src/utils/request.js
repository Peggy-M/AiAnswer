import axios from 'axios'
import { ElMessage } from 'element-plus'

console.log('API Base URL:', import.meta.env.VITE_API_BASE_URL || '/api')

const service = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 15000
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 开发环境下打印请求信息
    if (import.meta.env.DEV) {
      console.log('Request:', {
        url: config.url,
        method: config.method,
        params: config.params,
        data: config.data,
        baseURL: config.baseURL
      })
    }
    return config
  },
  error => {
    console.error('Request Error:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    // 开发环境下打印响应信息
    if (import.meta.env.DEV) {
      console.log('Response:', {
        url: response.config.url,
        status: response.status,
        data: response.data
      })
    }
    return response.data
  },
  error => {
    console.error('Response Error:', error)
    
    // 处理错误响应
    const message = error.response?.data?.message || error.message || '请求失败'
    ElMessage.error(message)
    
    return Promise.reject(error)
  }
)

export default service 