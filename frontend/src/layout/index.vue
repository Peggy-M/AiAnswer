<template>
  <div class="app-wrapper">
    <div class="sidebar-container">
      <el-menu
        :default-active="activeMenu"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        router>
        <el-menu-item index="/course/list">
          <i class="el-icon-menu"></i>
          <span>全部课程</span>
        </el-menu-item>
        <!-- 其他菜单项 -->
      </el-menu>
    </div>
    <div class="main-container">
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Layout',
  computed: {
    activeMenu() {
      return this.$route.path
    }
  }
}
</script>

<style scoped>
.app-wrapper {
  display: flex;
  height: 100vh;
  width: 100%;
}

.sidebar-container {
  width: 210px;
  height: 100%;
  background: #304156;
  position: fixed;
  left: 0;
  top: 0;
  bottom: 0;
  overflow-y: auto;
}

.main-container {
  margin-left: 210px;
  flex: 1;
  overflow: auto;
}
</style> 