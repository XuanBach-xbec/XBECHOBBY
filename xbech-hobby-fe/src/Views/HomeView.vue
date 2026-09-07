<template>
  <div class="official-page">
    <!-- HERO SLIDER BANNER (Mẫu Ảnh 1) -->
    <section class="hero-banner">
      <!-- Hình Nền Gundam Siêu Sắc Nét -->
      <div class="banner-bg">
        <img src="https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?q=80&w=1600" alt="Gundam RX-93" />
        <div class="overlay-gradient"></div>
      </div>

      <!-- Thẻ Nội Dung Nổi Bên Trái -->
      <div class="hero-card">
        <div class="mini-poster">
          <img src="https://images.unsplash.com/photo-1563089145-599997674d42?q=80&w=300" alt="Poster Mini" />
        </div>
        <div class="card-details">
          <h1 class="hero-heading">A Boy with GUNDAM</h1>
          <p class="hero-desc">
            To commemorate the 50th anniversary of the "Gundam" series anime broadcast, the special project has officially begun...
          </p>
          <button class="watch-btn">WATCH NOW &gt;</button>
        </div>
      </div>

      <!-- Thanh Điều Hướng Slider Góc Dưới Phải -->
      <div class="slider-controls">
        <span class="slide-num"><b>03</b> | 05</span>
        <button class="ctrl-btn">&lt;</button>
        <button class="ctrl-btn">&gt;</button>
        <button class="ctrl-btn">||</button>
      </div>
    </section>

    <!-- KHU VỰC SERIES (Mẫu Ảnh 2) -->
    <section class="series-container">
      <div class="series-header">
        <h2 class="section-title">SERIES</h2>
        <p class="section-subtext">
          Information related to Gundam series titles released so far, including streaming availability, storylines, characters, and mechanics.
        </p>
      </div>

      <div v-if="loading" class="loading-state">Đang tải danh sách Series...</div>

      <div v-else class="series-grid">
        <div v-for="item in gundams" :key="item.id" class="poster-item">
          <div class="poster-frame">
            <img :src="item.coverImage" :alt="item.name" />
          </div>
          <div class="poster-meta">
            <span class="badge">{{ item.grade }} {{ item.scale }}</span>
            <h3 class="poster-title">{{ item.name }}</h3>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import gundamApi from '../API/gundamApi';

const gundams = ref([]);
const loading = ref(true);

onMounted(async () => {
  try {
    const res = await gundamApi.getAll();
    gundams.value = res.data;
  } catch (err) {
    console.error('Lỗi API:', err);
  } finally {
    loading.value = false;
  }
});
</script>

<style scoped>
.official-page {
  background-color: #000;
  color: #fff;
  font-family: Arial, Helvetica, sans-serif;
}

/* --- HERO SECTION --- */
/* Trong HomeView.vue */
.hero-banner {
  padding-top: 96px; /* 36px Ticker + 60px Navbar */
  min-height: 100vh;
  background: url('duong-dan-anh-gundam.jpg') center/cover no-repeat;
}

.banner-bg img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center 20%;
}

.overlay-gradient {
  position: absolute;
  top: 0; left: 0; right: 0; bottom: 0;
  background: linear-gradient(90deg, rgba(0,0,0,0.85) 0%, rgba(0,0,0,0.3) 50%, rgba(0,0,0,0.7) 100%);
}

/* Floating Card */
.hero-card {
  position: absolute;
  bottom: 60px;
  left: 60px;
  display: flex;
  gap: 20px;
  max-width: 550px;
  background: rgba(0, 0, 0, 0.65);
  backdrop-filter: blur(8px);
  padding: 20px;
  border-left: 4px solid #e53e3e;
  border-radius: 4px;
}

.mini-poster img {
  width: 100px;
  height: 140px;
  object-fit: cover;
  border-radius: 4px;
}

.hero-heading {
  font-family: 'Arial Black', sans-serif;
  font-size: 1.8rem;
  margin: 0 0 10px 0;
  letter-spacing: 1px;
}

.hero-desc {
  font-size: 0.85rem;
  color: #ccc;
  line-height: 1.4;
  margin-bottom: 16px;
}

.watch-btn {
  background: #333;
  color: #fff;
  border: 1px solid #555;
  padding: 10px 20px;
  font-weight: bold;
  font-size: 0.8rem;
  letter-spacing: 1px;
  cursor: pointer;
  transition: all 0.2s;
}
.watch-btn:hover {
  background: #e53e3e;
  border-color: #e53e3e;
}

/* Slider Controls */
.slider-controls {
  position: absolute;
  bottom: 40px;
  right: 60px;
  display: flex;
  align-items: center;
  gap: 12px;
  background: rgba(0,0,0,0.7);
  padding: 8px 16px;
  border-radius: 4px;
}

.slide-num {
  font-size: 0.9rem;
  margin-right: 12px;
  letter-spacing: 1px;
}

.ctrl-btn {
  background: none;
  border: 1px solid #444;
  color: #fff;
  padding: 6px 12px;
  cursor: pointer;
  font-weight: bold;
}
.ctrl-btn:hover { border-color: #fff; }

/* --- SERIES SECTION --- */
.series-container {
  background-color: #fff;
  color: #000;
  padding: 70px 60px;
}

.series-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 50px;
  border-bottom: 2px solid #eee;
  padding-bottom: 20px;
}

.section-title {
  font-family: 'Arial Black', sans-serif;
  font-size: 3.5rem;
  margin: 0;
  letter-spacing: 2px;
}

.section-subtext {
  max-width: 450px;
  font-size: 0.85rem;
  color: #555;
  line-height: 1.5;
}

.series-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(230px, 1fr));
  gap: 32px;
}

.poster-item {
  cursor: pointer;
  transition: transform 0.3s ease;
}
.poster-item:hover {
  transform: translateY(-8px);
}

.poster-frame img {
  width: 100%;
  height: 330px;
  object-fit: cover;
  border-radius: 4px;
  box-shadow: 0 6px 16px rgba(0,0,0,0.15);
}

.poster-meta {
  margin-top: 12px;
}

.badge {
  font-size: 0.75rem;
  font-weight: bold;
  color: #e53e3e;
  text-transform: uppercase;
}

.poster-title {
  font-size: 1.1rem;
  margin: 4px 0 0 0;
  font-weight: bold;
}

.loading-state {
  text-align: center;
  padding: 50px;
  color: #666;
}
</style>