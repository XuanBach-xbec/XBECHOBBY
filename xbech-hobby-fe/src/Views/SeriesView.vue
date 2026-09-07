<script setup>
import { useRoute } from 'vue-router';

const route = useRoute();
</script>

<template>
  <div class="series-view">
    <h1>Danh Sách Series Gundam</h1>
  </div>
</template>

<style scoped>
.series-view {
  min-height: 100vh;
  padding: 120px 5% 40px;
  color: #ffffff;
}
</style>