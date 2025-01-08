<template>
  <div class="profile-container">
    <el-card class="profile-card">
      <template #header>
        <div class="header">
          <h2>个人中心</h2>
        </div>
      </template>
      
      <el-form
        ref="formRef"
        :model="userForm"
        label-width="100px">
        <el-form-item label="头像">
          <el-avatar
            :size="64"
            :src="userForm.avatar">
            {{ userForm.name?.[0]?.toUpperCase() }}
          </el-avatar>
        </el-form-item>
        
        <el-form-item label="用户名">
          <el-input v-model="userForm.name" disabled />
        </el-form-item>
        
        <el-form-item label="角色">
          <el-tag v-if="isAdmin" type="success">管理员</el-tag>
          <el-tag v-else>学生</el-tag>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const userForm = ref({
  name: userStore.userInfo.name,
  avatar: userStore.userInfo.avatar
})

const isAdmin = computed(() => userStore.isAdmin)
</script>

<style lang="scss" scoped>
.profile-container {
  padding: 20px;
  
  .profile-card {
    max-width: 600px;
    margin: 0 auto;
    
    .header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      
      h2 {
        margin: 0;
      }
    }
  }
}
</style> 