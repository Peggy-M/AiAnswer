<template>
  <el-menu
    :router="true"
    mode="horizontal"
    :ellipsis="false"
    class="nav-menu"
  >
    <el-menu-item index="/">首页</el-menu-item>
    <el-menu-item index="/courses">课程学习</el-menu-item>
    <el-menu-item index="/exams">在线考试</el-menu-item>
    <el-menu-item index="/ai-analysis">AI 辅助</el-menu-item>
  </el-menu>
</template>

<script setup>
</script>

<style scoped>
.nav-menu {
  padding: 0 20px;
}
</style> 