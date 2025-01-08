<template>
  <div class="exam-detail">
    <el-card v-loading="loading">
      <template #header>
        <div class="header">
          <h2>{{ exam.title }}</h2>
          <div class="info">
            <el-tag>{{ exam.subject }}</el-tag>
            <span>难度：{{ exam.difficultyLevel }}/5</span>
            <span>时长：{{ exam.duration }}分钟</span>
            <span>总分：{{ exam.totalScore }}分</span>
          </div>
        </div>
      </template>

      <div class="exam-content">
        <!-- 考试说明 -->
        <div class="exam-instructions" v-if="!started">
          <h3>考试说明</h3>
          <p>1. 本次考试时长为 {{ exam.duration }} 分钟</p>
          <p>2. 考试期间请勿刷新页面或离开</p>
          <p>3. 点击开始考试后计时开始</p>
          <el-button type="primary" @click="startExam">开始考试</el-button>
        </div>

        <!-- 考试题目 -->
        <div v-else class="questions">
          <div v-for="(question, index) in exam.questions" :key="question.id" class="question-item">
            <h4>{{ index + 1 }}. {{ question.content }}</h4>
            <el-radio-group v-model="answers[question.id]">
              <el-radio 
                v-for="option in question.options" 
                :key="option.key" 
                :label="option.key"
              >
                {{ option.key }}. {{ option.content }}
              </el-radio>
            </el-radio-group>
          </div>

          <!-- 提交按钮 -->
          <div class="submit-section">
            <el-button type="primary" @click="submitExam" :loading="submitting">
              提交答案
            </el-button>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 倒计时 -->
    <div v-if="started" class="timer-card">
      <el-card>
        <template #header>
          <div class="timer-header">
            剩余时间
          </div>
        </template>
        <div class="timer">
          {{ formatTime(remainingTime) }}
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { examApi } from '@/api/exam'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const submitting = ref(false)
const started = ref(false)
const exam = ref({})
const answers = ref({})
const remainingTime = ref(0)
let timer = null

// 获取考试信息
const fetchExam = async () => {
  loading.value = true
  try {
    const response = await examApi.getExamDetail(route.params.id)
    exam.value = response
    console.log('Exam data:', exam.value)  // 添加调试日志
  } catch (error) {
    console.error('Failed to fetch exam:', error)
    ElMessage.error('获取考试信息失败')
  } finally {
    loading.value = false
  }
}

// 开始考试
const startExam = async () => {
  try {
    await ElMessageBox.confirm('确定要开始考试吗？开始后计时不可暂停', '提示')
    started.value = true
    remainingTime.value = exam.value.duration * 60
    startTimer()
  } catch {
    // 用户取消开始考试
  }
}

// 提交考试
const submitExam = async () => {
  try {
    submitting.value = true
    await examApi.submitExam(exam.value.id, answers.value)
    ElMessage.success('提交成功')
    router.push('/exam/list')
  } catch (error) {
    console.error('Failed to submit exam:', error)
    ElMessage.error('提交失败')
  } finally {
    submitting.value = false
  }
}

// 开始计时
const startTimer = () => {
  timer = setInterval(() => {
    if (remainingTime.value > 0) {
      remainingTime.value--
    } else {
      clearInterval(timer)
      submitExam()
    }
  }, 1000)
}

// 格式化时间
const formatTime = (seconds) => {
  const minutes = Math.floor(seconds / 60)
  const remainingSeconds = seconds % 60
  return `${minutes}:${remainingSeconds.toString().padStart(2, '0')}`
}

onMounted(fetchExam)

onBeforeUnmount(() => {
  if (timer) {
    clearInterval(timer)
  }
})
</script>

<style lang="scss" scoped>
.exam-detail {
  position: relative;
  
  .header {
    h2 {
      margin: 0 0 10px;
    }
    
    .info {
      display: flex;
      gap: 20px;
      align-items: center;
      color: #666;
    }
  }
  
  .exam-content {
    margin-top: 20px;
  }
  
  .exam-instructions {
    text-align: center;
    padding: 40px;
    
    h3 {
      margin-bottom: 20px;
    }
    
    p {
      margin: 10px 0;
      color: #666;
    }
    
    .el-button {
      margin-top: 30px;
    }
  }
  
  .questions {
    .question-item {
      margin-bottom: 30px;
      
      h4 {
        margin: 0 0 15px;
      }
      
      .el-radio {
        display: block;
        margin: 10px 0;
      }
    }
  }
  
  .submit-section {
    margin-top: 40px;
    text-align: center;
  }
  
  .timer-card {
    position: fixed;
    top: 100px;
    right: 20px;
    width: 200px;
    
    .timer-header {
      text-align: center;
      font-weight: bold;
    }
    
    .timer {
      font-size: 24px;
      text-align: center;
      color: #409EFF;
    }
  }
}
</style> 