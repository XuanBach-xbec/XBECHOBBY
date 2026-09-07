<script setup>
import { ref, watch } from 'vue';
import Navbar from './Components/Navbar.vue';
import HeroBanner from './Components/HeroBanner.vue';
import SeriesSection from './Components/SeriesSection.vue';
import NewsSection from './Components/NewsSection.vue'; // 1. Import NewsSection
import LoadingScreen from './Components/LoadingScreen.vue';

const currentLang = ref('vi');
const pendingLang = ref('vi');
const isLoading = ref(false);

watch(currentLang, (newLang) => {
  const isVi = newLang === 'vi';
  document.title = isVi ? 'Trang Chủ - XBECHOBBY' : 'Home - XBECHOBBY';
  const newPath = isVi ? '/trang-chu' : '/home';
  window.history.pushState({}, '', newPath);
}, { immediate: true });

const handleLangChange = (newLang) => {
  if (newLang === currentLang.value || isLoading.value) return;
  pendingLang.value = newLang;
  isLoading.value = true;
};

const onLoadingFinish = () => {
  currentLang.value = pendingLang.value;
  isLoading.value = false;
};
</script>

<template>
  <div class="app-layout">
    <LoadingScreen 
      :show="isLoading" 
      :lang="pendingLang"
      @finish="onLoadingFinish" 
    />

    <Navbar :lang="currentLang" @change-lang="handleLangChange" />
    <HeroBanner :lang="currentLang" />
    
    <SeriesSection :lang="currentLang" />
    <NewsSection :lang="currentLang" /> </div>
</template>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  background-color: #08090b;
  overflow-x: hidden;
}

.app-layout {
  position: relative;
  width: 100%;
  min-height: 100vh;
}
</style>