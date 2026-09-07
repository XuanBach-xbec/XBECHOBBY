<script setup>
import { ref, computed, onMounted } from 'vue';

const props = defineProps({
  lang: {
    type: String,
    default: 'vi'
  }
});

const isEn = computed(() => props.lang === 'en');

const sectionData = computed(() => ({
  moduleTag: 'MODULE.001 //',
  title: 'SERIES',
  description: isEn.value
    ? 'We introduce a wide range of information related to Gundam series titles released so far, including streaming availability, storylines, characters, and mechanics.'
    : 'Tổng hợp các thông tin chi tiết về các dòng phim Gundam đã phát hành, bao gồm nền tảng trình chiếu, cốt truyện, nhân vật và thiết kế Mecha.',
  viewAll: isEn.value ? 'VIEW ALL SERIES ▶' : 'XEM TẤT CẢ SERIES ▶'
}));

const formatTimeline = (timeline) => {
  if (isEn.value) return timeline;

  if (timeline === 'Alternative Timeline Series') {
    return 'Dòng phim Dòng thời gian Khác';
  }
  if (timeline === 'Universal Century Timeline Series') {
    return 'Dòng phim Universal Century (UC)';
  }
  return timeline;
};

import series1 from '../Assets/Series/series1.png';
import series2 from '../Assets/Series/series2.png';
import series3 from '../Assets/Series/series3.png';
import series4 from '../Assets/Series/series4.png';
import series5 from '../Assets/Series/series5.png';
import series6 from '../Assets/Series/series6.png';
import series7 from '../Assets/Series/series7.png';
import series8 from '../Assets/Series/series8.png';
import series9 from '../Assets/Series/series9.png';
import series10 from '../Assets/Series/series10.png';

const seriesList = [
  { id: 1, title: 'Mobile Suit Gundam RG XARX-ZERO', year: '2027', timeline: 'Alternative Timeline Series', image: series1 },
  { id: 2, title: 'MOBILE SUIT GUNDAM HATHAWAY The Sorcery of Nymph Circe', year: '2026', timeline: 'Universal Century Timeline Series', image: series2 },
  { id: 3, title: 'Special Edition: Mobile Suit Gundam Iron-Blooded Orphans Urdr-Hunt "-Path of the Little Challenger-" and Mobile Suit Gundam Iron-Blooded Orphans 10th Anniversary Short Film “Wedge of Interposition”', year: '2025', timeline: 'Alternative Timeline Series', image: series3 },
  { id: 4, title: 'Mobile Suit Gundam GQuuuuuuX', year: '2025', timeline: 'Universal Century Timeline Series', image: series4 },
  { id: 5, title: 'Mobile Suit Gundam GQuuuuuuX - Beginning', year: '2025', timeline: 'Universal Century Timeline Series', image: series5 },
  { id: 6, title: 'Mobile Suit Gundam: Requiem for Vengeance', year: '2024', timeline: 'Universal Century Timeline Series', image: series6 },
  { id: 7, title: 'MOBILE SUIT GUNDAM THE ORIGIN Chapter 3: Chronicle of the Loum Battlefield', year: '2024', timeline: 'Universal Century Timeline Series', image: series7 },
  { id: 8, title: 'MOBILE SUIT GUNDAM THE ORIGIN Chapter 2: The Outbreak of War', year: '2024', timeline: 'Universal Century Timeline Series', image: series8 },
  { id: 9, title: 'MOBILE SUIT GUNDAM THE ORIGIN Chapter 1: Chronicle of Char and Sayla', year: '2024', timeline: 'Universal Century Timeline Series', image: series9 },
  { id: 10, title: 'Mobile Suit Gundam SEED FREEDOM', year: '2024', timeline: 'Alternative Timeline Series', image: series10 }
];

// Nhân bản 5 tập hợp thẻ để phục vụ xoay vòng vô tận
const infiniteSeriesList = computed(() => [
  ...seriesList.map((item, idx) => ({ ...item, uniqueKey: `set1-${item.id}-${idx}` })),
  ...seriesList.map((item, idx) => ({ ...item, uniqueKey: `set2-${item.id}-${idx}` })),
  ...seriesList.map((item, idx) => ({ ...item, uniqueKey: `set3-${item.id}-${idx}` })),
  ...seriesList.map((item, idx) => ({ ...item, uniqueKey: `set4-${item.id}-${idx}` })),
  ...seriesList.map((item, idx) => ({ ...item, uniqueKey: `set5-${item.id}-${idx}` }))
]);

const scrollContainer = ref(null);
const isMouseDown = ref(false);
const startX = ref(0);
const scrollLeftPos = ref(0);
const sliderValue = ref(0);
const isDragged = ref(false);

let velocity = 0;
let momentumID = null;
let lastPageX = 0;

onMounted(() => {
  if (scrollContainer.value) {
    const singleSetWidth = scrollContainer.value.scrollWidth / 5;
    scrollContainer.value.scrollLeft = singleSetWidth * 2; // Đặt vị trí xuất phát ở Set 3 trung tâm
    updateProgress();
  }
});

// Thuật toán dịch chuyển mốc tọa độ vô tận ngầm (Zero-stutter Infinite Loop)
const checkInfiniteLoop = () => {
  if (!scrollContainer.value) return;
  const container = scrollContainer.value;
  const singleSetWidth = container.scrollWidth / 5;
  if (!singleSetWidth) return;

  if (container.scrollLeft >= singleSetWidth * 3) {
    container.scrollLeft -= singleSetWidth;
    scrollLeftPos.value -= singleSetWidth;
  } else if (container.scrollLeft <= singleSetWidth * 1) {
    container.scrollLeft += singleSetWidth;
    scrollLeftPos.value += singleSetWidth;
  }
  updateProgress();
};

// Cập nhật vị trí con lăn slider
const updateProgress = () => {
  if (!scrollContainer.value) return;
  const container = scrollContainer.value;
  const singleSetWidth = container.scrollWidth / 5;
  if (!singleSetWidth) return;

  const normalized = container.scrollLeft % singleSetWidth;
  sliderValue.value = (normalized / singleSetWidth) * 100;
};

// Xử lý kéo thanh trượt con lăn
const onSliderInput = (e) => {
  if (!scrollContainer.value) return;
  const val = parseFloat(e.target.value);
  const singleSetWidth = scrollContainer.value.scrollWidth / 5;
  
  const currentSetIndex = Math.floor(scrollContainer.value.scrollLeft / singleSetWidth) || 2;
  scrollContainer.value.scrollLeft = (currentSetIndex * singleSetWidth) + (val / 100) * singleSetWidth;
  checkInfiniteLoop();
};

// BỘ MÁY ĐIỀU HƯỚNG BẰNG TAY / CHUỘT DÀNH CHO CẢ MOBILE VÀ PC
const handlePointerDown = (e) => {
  isMouseDown.value = true;
  isDragged.value = false;
  startX.value = e.pageX - scrollContainer.value.offsetLeft;
  scrollLeftPos.value = scrollContainer.value.scrollLeft;
  lastPageX = e.pageX;
  velocity = 0;
  cancelAnimationFrame(momentumID);

  if (e.target.setPointerCapture) {
    try { e.target.setPointerCapture(e.pointerId); } catch (_) {}
  }
};

const handlePointerMove = (e) => {
  if (!isMouseDown.value || !scrollContainer.value) return;

  const x = e.pageX - scrollContainer.value.offsetLeft;
  const walk = (x - startX.value) * 1.0;

  if (Math.abs(x - startX.value) > 4) {
    isDragged.value = true;
  }

  velocity = (e.pageX - lastPageX) * 0.8;
  lastPageX = e.pageX;

  scrollContainer.value.scrollLeft = scrollLeftPos.value - walk;
  checkInfiniteLoop();
};

const applyMomentum = () => {
  if (Math.abs(velocity) > 0.1) {
    if (scrollContainer.value) {
      scrollContainer.value.scrollLeft -= velocity;
      velocity *= 0.92; // Ma sát lướt mượt mà 120fps
      checkInfiniteLoop();
      momentumID = requestAnimationFrame(applyMomentum);
    }
  }
};

const handlePointerUpOrLeave = (e) => {
  if (!isMouseDown.value) return;
  isMouseDown.value = false;

  if (e?.target?.releasePointerCapture) {
    try { e.target.releasePointerCapture(e.pointerId); } catch (_) {}
  }

  applyMomentum();
};

// Nút bấm Prev / Next kết hợp đà quán tính mượt
const scrollNext = () => {
  cancelAnimationFrame(momentumID);
  velocity = -18;
  applyMomentum();
};

const scrollPrev = () => {
  cancelAnimationFrame(momentumID);
  velocity = 18;
  applyMomentum();
};
</script>

<template>
  <section class="series-section">
    <!-- HEADER GRID -->
    <div class="series-header-grid">
      <div class="header-left">
        <span class="module-tag">{{ sectionData.moduleTag }}</span>
        <h2 class="section-title">{{ sectionData.title }}</h2>
      </div>
      <div class="header-right">
        <p class="section-desc">{{ sectionData.description }}</p>
      </div>
    </div>

    <!-- KHU VỰC THẺ PHIM (SỬ DỤNG BỘ MÁY VẬT LÝ VÂN TAY / CHUỘT UNIFIED) -->
    <div 
      ref="scrollContainer" 
      class="series-scroll-container"
      :class="{ 'is-dragging': isMouseDown }"
      @pointerdown="handlePointerDown"
      @pointermove="handlePointerMove"
      @pointerup="handlePointerUpOrLeave"
      @pointercancel="handlePointerUpOrLeave"
    >
      <div 
        v-for="item in infiniteSeriesList" 
        :key="item.uniqueKey" 
        class="series-card"
        :class="{ 'prevent-click': isDragged }"
      >
        <div class="card-inner">
          <div class="bg-blur-layer" :style="{ backgroundImage: `url(${item.image})` }"></div>

          <div class="poster-wrapper">
            <img :src="item.image" :alt="item.title" class="poster-img" draggable="false" />
          </div>

          <div class="card-info">
            <h3 class="card-title">{{ item.title }}</h3>
            <div class="meta-row">
              <span class="card-year">{{ item.year }}</span>
              <span class="card-timeline">{{ formatTimeline(item.timeline) }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- DÒNG ĐIỀU HƯỚNG BÊN DƯỚI -->
    <div class="controls-bottom">
      <div class="nav-arrows">
        <button class="nav-btn" aria-label="Previous" @click="scrollPrev">◄</button>
        
        <!-- THANH TRƯỢT CON LĂN -->
        <div class="slider-wrapper">
          <input 
            type="range" 
            min="0" 
            max="100" 
            step="0.1" 
            :value="sliderValue" 
            @input="onSliderInput" 
            class="custom-slider"
            aria-label="Scroll series list"
          />
        </div>

        <button class="nav-btn" aria-label="Next" @click="scrollNext">►</button>
      </div>

      <router-link to="/series" class="view-all-btn">
        {{ sectionData.viewAll }}
      </router-link>
    </div>
  </section>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Oswald:wght@500;700&family=Roboto+Mono:wght@400;500;700&display=swap');

.series-section {
  background-color: #ffffff;
  color: #000000;
  padding: 50px 0 30px 0;
  font-family: 'Oswald', sans-serif;
  border-top: 1px solid #e0e0e0;
  overflow: hidden;
}

.series-header-grid {
  display: grid;
  grid-template-columns: 1fr 380px;
  gap: 40px;
  align-items: flex-end;
  padding: 0 40px 30px 40px;
  margin-bottom: 10px;
}

.module-tag {
  font-family: 'Roboto Mono', monospace;
  font-size: 0.85rem;
  font-weight: 700;
  color: #666666;
  display: block;
  margin-bottom: 8px;
}

.section-title {
  font-size: clamp(2.8rem, 6vw, 4.5rem);
  font-weight: 700;
  line-height: 0.9;
  letter-spacing: 2px;
  text-transform: uppercase;
  margin: 0;
}

.header-right {
  border-left: 1px solid #e5e5e5;
  padding-left: 25px;
}

.section-desc {
  font-family: 'Arial', sans-serif;
  font-size: 0.88rem;
  line-height: 1.6;
  color: #555555;
  margin: 0;
}

/* CONTAINER CUỘN DRAG KHÔNG KHỰNG TRÊN MOBILE & PC */
.series-scroll-container {
  display: flex;
  overflow-x: hidden; /* Khóa cuộn Native của OS để loại bỏ hoàn toàn hiện tượng khựng/sượng */
  padding: 20px 40px 40px 40px;
  cursor: grab;
  user-select: none;
  touch-action: pan-y; /* Vẫn cho phép trang web vuốt lên/xuống bình thường */
  will-change: scroll-position;
}

.series-scroll-container.is-dragging {
  cursor: grabbing;
}

/* THẺ SERIES */
.series-card {
  flex: 0 0 285px;
  border-right: 1px solid #eaeaea;
  padding: 0 16px;
  box-sizing: border-box;
}

.prevent-click {
  pointer-events: none;
}

.card-inner {
  position: relative;
  padding: 16px;
  border-radius: 6px;
  overflow: hidden;
  transition: transform 0.4s cubic-bezier(0.16, 1, 0.3, 1), box-shadow 0.4s ease;
  display: flex;
  flex-direction: column;
  height: 100%;
  background: transparent;
}

.bg-blur-layer {
  position: absolute;
  inset: 0;
  background-size: cover;
  background-position: center;
  filter: blur(22px) brightness(0.35);
  transform: scale(1.2);
  opacity: 0;
  transition: opacity 0.45s ease-in-out;
  z-index: 0;
  pointer-events: none;
}

.series-card:hover .card-inner {
  box-shadow: 0 16px 40px rgba(0, 0, 0, 0.3);
}

.series-card:hover .bg-blur-layer {
  opacity: 1;
}

.poster-wrapper {
  position: relative;
  z-index: 1;
  width: 100%;
  aspect-ratio: 3 / 4;
  background-color: #1a1a1a;
  border-radius: 4px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.4s cubic-bezier(0.16, 1, 0.3, 1), box-shadow 0.4s ease;
}

.poster-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.4s ease;
}

.series-card:hover .poster-wrapper {
  transform: translateY(-4px);
  box-shadow: 0 10px 24px rgba(0, 0, 0, 0.4);
}

.series-card:hover .poster-img {
  transform: scale(1.04);
}

.card-info {
  position: relative;
  z-index: 1;
  margin-top: 18px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.card-title {
  font-size: 1rem;
  font-weight: 700;
  color: #111111;
  line-height: 1.35;
  margin: 0;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  transition: color 0.3s ease;
}

.series-card:hover .card-title {
  color: #ffffff;
}

.meta-row {
  display: flex;
  flex-direction: column;
  gap: 4px;
  font-family: 'Roboto Mono', monospace;
  transition: color 0.3s ease;
}

.card-year {
  font-size: 0.78rem;
  font-weight: 700;
  color: #333333;
}

.card-timeline {
  font-size: 0.72rem;
  font-weight: 600;
  color: #555555;
  display: inline-block;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.series-card:hover .card-year {
  color: #ffffff;
}

.series-card:hover .card-timeline {
  color: #dddddd;
}

/* CONTROLS ĐÁY TRANG */
.controls-bottom {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 40px 0 40px;
  border-top: 1px solid #eeeeee;
}

.nav-arrows {
  display: flex;
  align-items: center;
  gap: 15px;
}

.nav-btn {
  background: transparent;
  border: 1px solid #cccccc;
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.8rem;
  cursor: pointer;
  transition: all 0.2s ease;
  flex-shrink: 0;
}

.nav-btn:hover {
  background-color: #000000;
  color: #ffffff;
  border-color: #000000;
}

/* SLIDER / CON LĂN */
.slider-wrapper {
  width: 180px;
  display: flex;
  align-items: center;
}

.custom-slider {
  -webkit-appearance: none;
  appearance: none;
  width: 100%;
  height: 4px;
  background: #e5e5e5;
  outline: none;
  border-radius: 2px;
  cursor: pointer;
}

.custom-slider::-webkit-slider-thumb {
  -webkit-appearance: none;
  appearance: none;
  width: 24px;
  height: 14px;
  border-radius: 2px;
  background: #000000;
  cursor: pointer;
  transition: transform 0.1s ease, background-color 0.2s ease;
}

.custom-slider::-webkit-slider-thumb:active {
  transform: scale(1.15);
  background: #e2b755;
}

.custom-slider::-moz-range-thumb {
  width: 24px;
  height: 14px;
  border-radius: 2px;
  background: #000000;
  cursor: pointer;
  border: none;
}

.view-all-btn {
  font-family: 'Roboto Mono', monospace;
  font-size: 0.8rem;
  font-weight: 700;
  color: #ffffff;
  background-color: #000000;
  padding: 8px 18px;
  text-decoration: none;
  letter-spacing: 1px;
  transition: background-color 0.2s ease;
}

.view-all-btn:hover {
  background-color: #e2b755;
  color: #000000;
}

/* RESPONSIVE TABLET & MOBILE */
@media (max-width: 900px) {
  .series-header-grid {
    grid-template-columns: 1fr;
    gap: 15px;
    padding: 0 20px 20px 20px;
  }
  .header-right {
    border-left: none;
    padding-left: 0;
  }
  .series-card {
    flex: 0 0 230px;
  }
  .series-scroll-container {
    padding: 10px 20px 20px 20px;
  }
  .controls-bottom {
    padding: 10px 20px 0 20px;
  }
}

@media (max-width: 600px) {
  .series-section {
    padding: 30px 0 20px 0;
  }
  .series-header-grid {
    padding: 0 16px 15px 16px;
  }
  .series-scroll-container {
    padding: 10px 16px 20px 16px;
  }
  .series-card {
    flex: 0 0 200px;
    padding: 0 10px;
  }
  .card-inner {
    padding: 12px;
  }
  .controls-bottom {
    flex-direction: column;
    align-items: stretch;
    gap: 15px;
    padding: 15px 16px 0 16px;
  }
  .nav-arrows {
    width: 100%;
    justify-content: space-between;
  }
  .slider-wrapper {
    flex: 1;
    width: auto;
    padding: 0 10px;
  }
  .custom-slider::-webkit-slider-thumb {
    width: 28px;
    height: 16px;
  }
  .nav-btn {
    width: 42px;
    height: 42px;
  }
  .view-all-btn {
    text-align: center;
    width: 100%;
    box-sizing: border-box;
    padding: 12px 18px;
  }
}
</style>