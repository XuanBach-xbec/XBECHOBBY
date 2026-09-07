<script setup>
import { useRoute } from 'vue-router';

const route = useRoute();
</script>

<template>
  <div class="review-detail-view">
    <h1>Chi Tiết Review</h1>
    <p>ID Bài viết: {{ route.params.id }}</p>
  </div>
</template>

<style scoped>
.review-detail-view {
  min-height: 100vh;
  padding: 120px 5% 40px;
  color: #ffffff;
}
</style>