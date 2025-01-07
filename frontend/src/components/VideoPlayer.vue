<template>
  <div class="video-player">
    <video
      :src="source"
      controls
      class="video-element"
      @play="handlePlay"
      @pause="handlePause"
      @ended="handleEnded">
    </video>
  </div>
</template>

<script>
export default {
  name: 'VideoPlayer',
  props: {
    source: {
      type: String,
      required: true
    }
  },
  methods: {
    handlePlay() {
      this.$emit('play')
    },
    handlePause() {
      this.$emit('pause')
    },
    handleEnded() {
      this.$emit('ended')
    }
  }
}
</script>

<style scoped>
.video-player {
  width: 100%;
  background: #000;
}

.video-element {
  width: 100%;
  max-height: 500px;
}
</style> 