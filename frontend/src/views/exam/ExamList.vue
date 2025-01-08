<template>
  <div class="exam-list">
    <div class="header">
      <h2>考试中心</h2>
      <el-button type="primary" @click="showCreateDialog = true">
        <el-icon><Plus /></el-icon>
        创建考试
      </el-button>
    </div>

    <el-row :gutter="20">
      <el-col :span="6" v-for="exam in exams" :key="exam.id">
        <el-card class="exam-card">
          <div class="exam-header">
            <h3>{{ exam.title }}</h3>
            <el-tag :type="exam.isPublished ? 'success' : 'info'">
              {{ exam.isPublished ? '已发布' : '未发布' }}
            </el-tag>
          </div>
          <div class="exam-info">
            <p>科目：{{ exam.subject }}</p>
            <p>难度：{{ exam.difficultyLevel }}/5</p>
            <p>时长：{{ exam.duration }}分钟</p>
            <p>总分：{{ exam.totalScore }}分</p>
          </div>
          <div class="exam-footer">
            <el-button type="primary" @click="startExam(exam.id)">开始考试</el-button>
            <el-button 
              v-if="!exam.isPublished"
              type="success" 
              @click="publishExam(exam.id)"
            >
              发布
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 创建试卷对话框 -->
    <el-dialog
      v-model="showCreateDialog"
      title="创建试卷"
      width="500px"
    >
      <el-form :model="newExam" label-width="100px">
        <el-form-item label="标题">
          <el-input v-model="newExam.title"></el-input>
        </el-form-item>
        <el-form-item label="科目">
          <el-input v-model="newExam.subject"></el-input>
        </el-form-item>
        <el-form-item label="难度">
          <el-rate v-model="newExam.difficultyLevel" :max="5"></el-rate>
        </el-form-item>
        <el-form-item label="考试时长">
          <el-input-number v-model="newExam.duration" :min="30" :step="30"></el-input-number>
          <span class="ml-2">分钟</span>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreateDialog = false">取消</el-button>
        <el-button type="primary" @click="createExam">确定</el-button>
      </template>
    </el-dialog>

    <!-- 随机生成试卷对话框 -->
    <el-dialog v-model="generateDialogVisible" title="随机生成试卷">
      <el-form :model="generateForm" label-width="100px">
        <el-form-item label="科目">
          <el-input v-model="generateForm.subject"></el-input>
        </el-form-item>
        <el-form-item label="题目数量">
          <el-input-number v-model="generateForm.questionCount" :min="5" :max="50"></el-input-number>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="generateDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="generatePaper">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { examApi } from '@/api/exam'
import { Plus } from '@element-plus/icons-vue'

const router = useRouter()
const exams = ref([])
const showCreateDialog = ref(false)
const generateDialogVisible = ref(false)

const newExam = ref({
  title: '',
  subject: '',
  difficultyLevel: 3,
  duration: 60
})

const generateForm = ref({
  subject: '',
  questionCount: 10
})

const fetchExams = async () => {
  try {
    const response = await examApi.getAllExams()
    exams.value = response.data
  } catch (error) {
    ElMessage.error('获取试卷列表失败')
  }
}

const createExam = async () => {
  try {
    await examApi.createExam(newExam.value)
    ElMessage.success('创建成功')
    showCreateDialog.value = false
    fetchExams()
  } catch (error) {
    ElMessage.error('创建失败')
  }
}

const generatePaper = async () => {
  try {
    await examApi.generateRandomPaper(
      generateForm.value.subject,
      generateForm.value.questionCount
    )
    ElMessage.success('生成成功')
    generateDialogVisible.value = false
    fetchExams()
  } catch (error) {
    ElMessage.error('生成失败')
  }
}

const publishExam = async (id) => {
  try {
    await examApi.publishExam(id)
    ElMessage.success('发布成功')
    fetchExams()
  } catch (error) {
    ElMessage.error('发布失败')
  }
}

const startExam = (id) => {
  router.push(`/exam/${id}`)
}

onMounted(() => {
  fetchExams()
})
</script>

<style scoped>
.exam-list {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.exam-card {
  margin-bottom: 20px;
}

.exam-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.exam-info {
  margin: 10px 0;
  color: #666;
}

.exam-footer {
  margin-top: 15px;
  display: flex;
  gap: 10px;
}

.ml-2 {
  margin-left: 8px;
}
</style> 