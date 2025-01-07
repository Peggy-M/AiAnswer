<template>
  <div class="ai-analysis">
    <h2>AI 学习助手</h2>
    
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card class="analysis-card">
          <template #header>
            <div class="card-header">
              <span>学习分析</span>
              <el-button type="primary" @click="analyzePerformance">刷新分析</el-button>
            </div>
          </template>
          <div v-if="analysis">
            <p>{{ analysis.suggestions }}</p>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card class="question-card">
          <template #header>
            <div class="card-header">
              <span>AI 出题</span>
            </div>
          </template>
          <el-form :model="questionForm" label-width="80px">
            <el-form-item label="主题">
              <el-input v-model="questionForm.topic"></el-input>
            </el-form-item>
            <el-form-item label="数量">
              <el-input-number v-model="questionForm.count" :min="1" :max="10"></el-input-number>
            </el-form-item>
            <el-button type="primary" @click="generateQuestions">生成题目</el-button>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { aiApi } from '../api/ai'
import { ElMessage } from 'element-plus'

const analysis = ref(null)
const questionForm = ref({
  topic: '',
  count: 1
})
const generatedQuestions = ref([])

// 分析学习表现
const analyzePerformance = async () => {
  try {
    // 这里暂时使用固定的用户ID，实际应该从登录用户中获取
    const userId = 1
    const response = await aiApi.analyzePerformance(userId)
    analysis.value = response.data
    ElMessage.success('分析完成')
  } catch (error) {
    ElMessage.error('分析失败')
    console.error('Analysis error:', error)
  }
}

// 生成题目
const generateQuestions = async () => {
  try {
    const response = await aiApi.generateQuestions(
      questionForm.value.topic,
      questionForm.value.count
    )
    generatedQuestions.value = response.data
    ElMessage.success('题目生成成功')
  } catch (error) {
    ElMessage.error('生成题目失败')
    console.error('Generate questions error:', error)
  }
}

onMounted(() => {
  analyzePerformance()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.analysis-card, .question-card {
  margin-bottom: 20px;
}
</style> 