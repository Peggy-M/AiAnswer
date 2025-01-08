<template>
  <div class="app-wrapper">
    <nav-menu />
    <div class="main-container">
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </div>
  </div>
</template>

<script setup>
import NavMenu from '@/components/NavMenu.vue'
</script>

<style lang="scss" scoped>
.app-wrapper {
  min-height: 100vh;
  
  .main-container {
    padding: 20px;
    margin-top: 60px;
  }
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