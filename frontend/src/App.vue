<template>
  <el-config-provider>
    <div id="app">
      <el-container>
        <el-header>
          <Navigation />
        </el-header>
        <el-main>
          <router-view v-slot="{ Component }">
            <transition name="fade" mode="out-in">
              <component :is="Component" />
            </transition>
          </router-view>
        </el-main>
      </el-container>
    </div>
  </el-config-provider>
</template>

<script>
import { defineComponent } from 'vue'
import Navigation from '@/components/Navigation.vue'

export default defineComponent({
  name: 'App',
  components: {
    Navigation
  }
})
</script>

<style>
#app {
  height: 100vh;
}

.el-container {
  height: 100%;
}

.el-header {
  padding: 0;
  background-color: #fff;
  border-bottom: 1px solid #dcdfe6;
}

.el-main {
  background-color: #f5f7fa;
  padding: 20px;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style> 