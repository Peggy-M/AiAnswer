<template>
  <div class="exams">
    <div class="header">
      <h2>考试中心</h2>
      <el-button type="primary" @click="dialogVisible = true">创建考试</el-button>
    </div>

    <el-row :gutter="20">
      <el-col :span="6" v-for="exam in exams" :key="exam.id">
        <el-card class="exam-card">
          <h3>{{ exam.title }}</h3>
          <p>{{ exam.description }}</p>
          <p>时长：{{ exam.duration }}分钟</p>
          <el-button type="primary" @click="startExam(exam.id)">开始考试</el-button>
        </el-card>
      </el-col>
    </el-row>

    <!-- 创建考试对话框 -->
    <el-dialog v-model="dialogVisible" title="创建考试">
      <el-form :model="newExam" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="newExam.title"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input type="textarea" v-model="newExam.description"></el-input>
        </el-form-item>
        <el-form-item label="时长">
          <el-input-number v-model="newExam.duration" :min="1"></el-input-number>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="createExam">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { examApi } from '../api/exam'
import { ElMessage } from 'element-plus'

// ... 实现相关逻辑
</script>

<style scoped>
.exam-card {
  margin-bottom: 20px;
}
</style> 