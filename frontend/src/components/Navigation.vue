<template>
  <div class="nav-container">
    <el-menu 
      :router="true"
      mode="horizontal"
      :ellipsis="false"
      :default-active="activeIndex">
      <div class="logo">
        <img src="@/assets/logo.png" alt="Logo">
        <span>在线教育平台</span>
      </div>
      
      <div class="flex-grow" />
      
      <el-menu-item index="/">
        <el-icon><HomeFilled /></el-icon>
        首页
      </el-menu-item>
      
      <el-menu-item index="/courses">
        <el-icon><Reading /></el-icon>
        课程学习
      </el-menu-item>
      
      <el-menu-item index="/exams">
        <el-icon><Edit /></el-icon>
        在线考试
      </el-menu-item>
      
      <el-menu-item index="/ai-analysis">
        <el-icon><DataLine /></el-icon>
        AI分析
      </el-menu-item>
      
      <div class="flex-grow" />
      
      <el-menu-item index="/user">
        <el-avatar :size="32" :src="userAvatar">
          {{ userInitials }}
        </el-avatar>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script>
import { defineComponent, computed, ref } from 'vue'
import { useRoute } from 'vue-router'

export default defineComponent({
  name: 'Navigation',
  setup() {
    const route = useRoute()
    
    const activeIndex = computed(() => route.path)
    const userAvatar = ref('')
    const userInitials = computed(() => 'U')

    return {
      activeIndex,
      userAvatar,
      userInitials
    }
  }
})
</script>

<style scoped>
.nav-container {
  height: 60px;
}

.el-menu {
  height: 100%;
  border-bottom: none;
  padding: 0 20px;
}

.logo {
  display: flex;
  align-items: center;
  margin-right: 20px;
}

.logo img {
  height: 32px;
  margin-right: 10px;
}

.logo span {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
}

.flex-grow {
  flex-grow: 1;
}

.el-menu-item {
  display: flex;
  align-items: center;
}

.el-icon {
  margin-right: 5px;
}
</style> 