<script setup>
import { useRoute } from 'vue-router';

const route = useRoute();
</script>

<template>
  <div class="gundam-detail-view">
    <h1>Chi Tiết Mô Hình Gundam</h1>
    <p>ID Mô hình: {{ route.params.id }}</p>
  </div>
</template>

<style scoped>
.gundam-detail-view {
  min-height: 100vh;
  padding: 120px 5% 40px;
  color: #ffffff;
}
</style>