import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'

export const useUserStore = defineStore('user', () => {
  const userInfo = ref({
    id: null,
    name: '',
    avatar: '',
    roles: []
  })
  
  const token = ref('')
  
  const isAdmin = computed(() => userInfo.value.roles.includes('ROLE_ADMIN'))
  
  // 简化登录逻辑，先不调用后端
  const login = async (username, password) => {
    try {
      // 模拟网络延迟
      await new Promise(resolve => setTimeout(resolve, 500))
      
      if (username === 'admin' && password === 'admin123') {
        const adminUser = {
          id: 1,
          name: 'Admin',
          avatar: '',
          roles: ['ROLE_ADMIN']
        }
        userInfo.value = adminUser
        token.value = 'admin-token'
        
        // 保存到 localStorage
        localStorage.setItem('userInfo', JSON.stringify(adminUser))
        localStorage.setItem('token', token.value)
        
        ElMessage.success('登录成功')
        return true
      } else if (username === 'student' && password === 'student123') {
        const studentUser = {
          id: 2,
          name: 'Student',
          avatar: '',
          roles: ['ROLE_STUDENT']
        }
        userInfo.value = studentUser
        token.value = 'student-token'
        
        // 保存到 localStorage
        localStorage.setItem('userInfo', JSON.stringify(studentUser))
        localStorage.setItem('token', token.value)
        
        ElMessage.success('登录成功')
        return true
      }
      
      ElMessage.error('用户名或密码错误')
      return false
    } catch (error) {
      console.error('Login failed:', error)
      ElMessage.error('登录失败，请重试')
      return false
    }
  }
  
  // 退出登录
  const logout = () => {
    userInfo.value = {
      id: null,
      name: '',
      avatar: '',
      roles: []
    }
    token.value = ''
    
    localStorage.removeItem('userInfo')
    localStorage.removeItem('token')
    
    ElMessage.success('已退出登录')
  }
  
  return {
    userInfo,
    token,
    isAdmin,
    login,
    logout
  }
}) 