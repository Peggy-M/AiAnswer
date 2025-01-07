<template>
  <div class="exam-detail">
    <div class="header">
      <h2>{{ exam.title }}</h2>
      <div class="exam-info">
        <el-tag>{{ exam.subject }}</el-tag>
        <el-tag type="warning">难度: {{ exam.difficultyLevel }}/5</el-tag>
        <el-tag type="info">时长: {{ exam.duration }}分钟</el-tag>
        <el-tag type="success">总分: {{ exam.totalScore }}分</el-tag>
      </div>
    </div>

    <div v-if="!isStarted" class="exam-start">
      <el-alert
        title="考试须知"
        type="warning"
        :closable="false"
      >
        <p>1. 考试时长为{{ exam.duration }}分钟，请合理安排答题时间</p>
        <p>2. 考试期间请勿刷新页面或关闭浏览器</p>
        <p>3. 点击开始考试后计时开始</p>
      </el-alert>
      <el-button type="primary" size="large" @click="startExam">开始考试</el-button>
    </div>

    <div v-else class="exam-content">
      <div class="timer">
        剩余时间：{{ formatTime(remainingTime) }}
      </div>

      <div class="questions">
        <div v-for="(question, index) in exam.questions" :key="question.id" class="question-item">
          <h3>第 {{ index + 1 }} 题 ({{ question.score }}分)</h3>
          <div class="question-content">{{ question.content }}</div>
          
          <!-- 选择题 -->
          <template v-if="question.type === 'SINGLE_CHOICE' || question.type === 'MULTIPLE_CHOICE'">
            <el-radio-group v-if="question.type === 'SINGLE_CHOICE'" v-model="answers[question.id]">
              <el-radio v-for="option in parseOptions(question.options)" :key="option.key" :label="option.key">
                {{ option.value }}
              </el-radio>
            </el-radio-group>
            <el-checkbox-group v-else v-model="answers[question.id]">
              <el-checkbox v-for="option in parseOptions(question.options)" :key="option.key" :label="option.key">
                {{ option.value }}
              </el-checkbox>
            </el-checkbox-group>
          </template>

          <!-- 判断题 -->
          <template v-else-if="question.type === 'TRUE_FALSE'">
            <el-radio-group v-model="answers[question.id]">
              <el-radio label="true">正确</el-radio>
              <el-radio label="false">错误</el-radio>
            </el-radio-group>
          </template>

          <!-- 简答题 -->
          <template v-else-if="question.type === 'ESSAY'">
            <el-input
              v-model="answers[question.id]"
              type="textarea"
              :rows="4"
              placeholder="请输入答案"
            />
          </template>
        </div>
      </div>

      <div class="actions">
        <el-button type="primary" size="large" @click="submitExam">提交答案</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { examApi } from '@/api/exam'

const route = useRoute()
const router = useRouter()
const exam = ref({})
const isStarted = ref(false)
const answers = ref({})
const remainingTime = ref(0)
let timer = null

// 获取试卷详情
const fetchExam = async () => {
  try {
    const response = await examApi.getExam(route.params.id)
    exam.value = response.data
    remainingTime.value = exam.value.duration * 60
  } catch (error) {
    ElMessage.error('获取试卷失败')
    router.push('/exams')
  }
}

// 开始考试
const startExam = async () => {
  try {
    // 这里应该从登录用户信息中获取userId
    const userId = 1
    await examApi.startExam(exam.value.id, userId)
    isStarted.value = true
    startTimer()
  } catch (error) {
    ElMessage.error('开始考试失败')
  }
}

// 提交答案
const submitExam = async () => {
  try {
    await ElMessageBox.confirm('确定要提交答案吗？提交后不能修改', '提示', {
      type: 'warning'
    })
    
    // 这里应该从登录用户信息中获取userId
    const userId = 1
    await examApi.submitExam(exam.value.id, userId, answers.value)
    ElMessage.success('提交成功')
    router.push('/exams')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('提交失败')
    }
  }
}

// 解析选项
const parseOptions = (optionsStr) => {
  try {
    return JSON.parse(optionsStr)
  } catch {
    return []
  }
}

// 格式化时间
const formatTime = (seconds) => {
  const minutes = Math.floor(seconds / 60)
  const remainingSeconds = seconds % 60
  return `${minutes}:${remainingSeconds.toString().padStart(2, '0')}`
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

onMounted(() => {
  fetchExam()
})

onUnmounted(() => {
  if (timer) {
    clearInterval(timer)
  }
})
</script>

<style scoped>
.exam-detail {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.header {
  margin-bottom: 20px;
}

.exam-info {
  margin-top: 10px;
  display: flex;
  gap: 10px;
}

.exam-start {
  text-align: center;
  padding: 40px;
}

.timer {
  position: fixed;
  top: 20px;
  right: 20px;
  background: #fff;
  padding: 10px 20px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.questions {
  margin: 20px 0;
}

.question-item {
  margin-bottom: 30px;
  padding: 20px;
  border: 1px solid #eee;
  border-radius: 4px;
}

.question-content {
  margin: 10px 0 20px;
  color: #666;
}

.actions {
  text-align: center;
  margin-top: 40px;
  padding-bottom: 40px;
}

.el-radio-group,
.el-checkbox-group {
  display: flex;
  flex-direction: column;
  gap: 10px;
}
</style> 