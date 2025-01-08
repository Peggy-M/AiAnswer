<template>
  <div class="home">
    <h1>欢迎来到在线教育系统</h1>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="feature-card" @click="router.push('/course/list')">
          <h3>课程学习</h3>
          <p>丰富的课程资源，随时随地学习</p>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="feature-card" @click="router.push('/exam/list')">
          <h3>在线考试</h3>
          <p>专业的考试系统，公平公正评估</p>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="feature-card" @click="router.push('/ai-analysis')">
          <h3>AI 辅助</h3>
          <p>智能分析学习数据，提供个性化建议</p>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()
</script>

<style lang="scss" scoped>
.home {
  padding: 20px;
  
  h1 {
    text-align: center;
    margin-bottom: 40px;
  }
  
  .feature-card {
    cursor: pointer;
    transition: all 0.3s;
    
    &:hover {
      transform: translateY(-5px);
      box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
    }
    
    h3 {
      margin: 0 0 10px;
      color: #303133;
    }
    
    p {
      margin: 0;
      color: #606266;
    }
  }
}
</style> 