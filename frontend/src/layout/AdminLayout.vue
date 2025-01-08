<template>
  <el-container class="admin-layout">
    <el-aside width="200px">
      <el-menu
        :router="true"
        :default-active="activeMenu"
        class="admin-menu">
        <el-menu-item index="/admin/courses">
          <el-icon><Document /></el-icon>
          <span>课程管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/courses/create">
          <el-icon><Plus /></el-icon>
          <span>创建课程</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    
    <el-container>
      <el-header>
        <div class="header-left">
          <h2>{{ currentTitle }}</h2>
        </div>
        <div class="header-right">
          <el-button @click="router.push('/')">返回前台</el-button>
        </div>
      </el-header>
      
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Document, Plus } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()

const activeMenu = computed(() => route.path)
const currentTitle = computed(() => route.meta.title || '管理后台')
</script>

<style lang="scss" scoped>
.admin-layout {
  height: 100vh;
  
  .el-aside {
    background-color: #304156;
    
    .admin-menu {
      height: 100%;
      border-right: none;
      background-color: transparent;
      
      :deep(.el-menu-item) {
        color: #bfcbd9;
        
        &:hover, &.is-active {
          color: #fff;
          background-color: #263445;
        }
        
        .el-icon {
          color: inherit;
        }
      }
    }
  }
  
  .el-header {
    background-color: #fff;
    border-bottom: 1px solid #dcdfe6;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 20px;
    
    .header-left {
      h2 {
        margin: 0;
        font-size: 18px;
      }
    }
  }
  
  .el-main {
    background-color: #f0f2f5;
    padding: 20px;
  }
}
</style> 