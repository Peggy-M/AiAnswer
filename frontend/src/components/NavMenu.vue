<template>
  <div class="nav-container">
    <el-menu
      :router="true"
      mode="horizontal"
      :ellipsis="false"
      :default-active="activeMenu"
      class="nav-menu"
    >
      <!-- 左侧菜单项 -->
      <div class="left-menu">
        <el-menu-item index="/">
          <el-icon><HomeFilled /></el-icon>
          首页
        </el-menu-item>
        <el-menu-item index="/course/list">
          <el-icon><Reading /></el-icon>
          全部课程
        </el-menu-item>
        <el-menu-item index="/exam/list">
          <el-icon><Edit /></el-icon>
          考试中心
        </el-menu-item>
        <el-menu-item index="/ai-analysis">
          <el-icon><DataAnalysis /></el-icon>
          AI分析
        </el-menu-item>
      </div>

      <!-- 右侧用户信息 -->
      <div class="right-menu">
        <template v-if="userInfo.id">
          <el-dropdown @command="handleCommand">
            <div class="avatar-container">
              <el-avatar :size="32" :src="userInfo.avatar">
                {{ userInfo.name?.[0]?.toUpperCase() }}
              </el-avatar>
              <span class="username">{{ userInfo.name }}</span>
              <el-icon><CaretBottom /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <template v-if="isAdmin">
                  <el-dropdown-item command="admin">
                    <el-icon><Setting /></el-icon>
                    管理后台
                  </el-dropdown-item>
                  <el-dropdown-item divided />
                </template>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon>
                  个人中心
                </el-dropdown-item>
                <el-dropdown-item command="logout" divided>
                  <el-icon><SwitchButton /></el-icon>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
        <el-button v-else type="primary" @click="router.push('/login')">
          登录
        </el-button>
      </div>
    </el-menu>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { 
  HomeFilled,
  Reading,
  Edit,
  DataAnalysis,
  Setting, 
  User, 
  SwitchButton, 
  CaretBottom 
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const userInfo = computed(() => userStore.userInfo)
const isAdmin = computed(() => userStore.isAdmin)

// 计算当前激活的菜单项
const activeMenu = computed(() => {
  const path = route.path
  if (path.startsWith('/course/')) {
    return '/course/list'
  }
  if (path.startsWith('/exam/')) {
    return '/exam/list'
  }
  return path
})

const handleCommand = (command) => {
  switch (command) {
    case 'admin':
      router.push('/admin/courses')
      break
    case 'profile':
      router.push('/user/profile')
      break
    case 'logout':
      userStore.logout()
      router.push('/login')
      break
  }
}
</script>

<style lang="scss" scoped>
.nav-container {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  background-color: #fff;
  border-bottom: solid 1px var(--el-border-color-light);
  
  .nav-menu {
    display: flex;
    justify-content: space-between;
    height: 60px;
    padding: 0 20px;
    
    .left-menu {
      display: flex;
      align-items: center;
      
      .el-menu-item {
        display: flex;
        align-items: center;
        gap: 4px;
      }
    }
    
    .right-menu {
      display: flex;
      align-items: center;
      
      .avatar-container {
        display: flex;
        align-items: center;
        gap: 8px;
        padding: 0 12px;
        height: 100%;
        cursor: pointer;
        
        &:hover {
          background-color: var(--el-menu-hover-bg-color);
        }
        
        .username {
          font-size: 14px;
          color: var(--el-text-color-regular);
        }
        
        .el-icon {
          font-size: 12px;
          color: var(--el-text-color-secondary);
        }
      }
    }
  }
}

:deep(.el-menu) {
  border-bottom: none;
}

:deep(.el-menu-item) {
  font-size: 14px;
}

:deep(.el-dropdown-menu__item) {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  
  .el-icon {
    margin: 0;
  }
}
</style> 