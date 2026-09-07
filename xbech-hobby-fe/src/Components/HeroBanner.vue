<script setup>
import { ref, watch, onMounted, onUnmounted, computed } from 'vue';
import { bannerSlides } from '../data/bannerData.js';

const props = defineProps({
  lang: {
    type: String,
    default: 'vi'
  }
});

const slides = ref([]);
const currentIndex = ref(0);
const isPlaying = ref(true);
const isMuted = ref(false); // Mặc định mở âm thanh
const videoRef = ref(null);
const videoBlurRef = ref(null);

// Trạng thái nhận diện thumbnail dọc hay ngang (mặc định là dọc)
const isThumbVertical = ref(true);

const handleThumbLoad = (e) => {
  const img = e.target;
  if (img.naturalWidth && img.naturalHeight) {
    isThumbVertical.value = img.naturalWidth < img.naturalHeight;
  }
};

// Hàm gọi API lấy danh sách Banner từ Backend Spring Boot
const fetchBanners = async () => {
  try {
    const res = await fetch('http://localhost:8080/api/banners/active');
    if (res.ok) {
      const data = await res.json();
      // Nếu Database có dữ liệu thì sử dụng API, ngược lại dùng file bannerData.js fallback
      if (Array.isArray(data) && data.length > 0) {
        slides.value = data;
      } else {
        slides.value = bannerSlides;
      }
    } else {
      slides.value = bannerSlides;
    }
  } catch (err) {
    console.warn('Chưa kết nối được Backend, chuyển sang dùng dữ liệu tĩnh:', err);
    slides.value = bannerSlides;
  }
};

// Đọc dữ liệu linh hoạt (Tương thích cả dữ liệu Database lẫn File JS tĩnh)
const currentSlideData = computed(() => {
  const slide = slides.value[currentIndex.value];
  if (!slide) return {};

  const currentLang = props.lang || 'vi';
  const isEn = currentLang === 'en';

  // Xử lý Tiêu đề
  let title = slide.titleVi || slide.title;
  if (typeof title === 'object' && title !== null) {
    title = title[currentLang] || title['vi'] || '';
  } else if (isEn && slide.titleEn) {
    title = slide.titleEn;
  }

  // Xử lý Mô tả
  let desc = slide.descVi || slide.desc;
  if (typeof desc === 'object' && desc !== null) {
    desc = desc[currentLang] || desc['vi'] || '';
  } else if (isEn && slide.descEn) {
    desc = slide.descEn;
  }

  // Xử lý Nút hành động
  let btnText = slide.btnTextVi || slide.btnText;
  if (typeof btnText === 'object' && btnText !== null) {
    btnText = btnText[currentLang] || btnText['vi'] || '';
  } else if (isEn && slide.btnTextEn) {
    btnText = slide.btnTextEn;
  }

  return {
    title: title || '',
    desc: desc || '',
    btnText: btnText || (isEn ? 'VIEW DETAILS' : 'XEM CHI TIẾT'),
    link: slide.link || '#',
    videoUrl: slide.videoUrl || '',
    thumbUrl: slide.thumbUrl || ''
  };
});

const controlTitles = computed(() => {
  const isEn = props.lang === 'en';
  return {
    prev: isEn ? 'Previous slide' : 'Slide trước',
    next: isEn ? 'Next slide' : 'Slide tiếp',
    pause: isEn ? 'Pause' : 'Tạm dừng',
    play: isEn ? 'Play' : 'Phát',
    mute: isEn ? 'Mute sound' : 'Tắt âm thanh',
    unmute: isEn ? 'Unmute sound' : 'Bật âm thanh'
  };
});

// Hiệu ứng Typewriter
const typedTitle = ref('');
const typedDesc = ref('');
let titleTimer = null;
let descTimer = null;

const startTypewriter = () => {
  clearInterval(titleTimer);
  clearInterval(descTimer);

  typedTitle.value = '';
  typedDesc.value = '';

  const fullTitle = currentSlideData.value.title || '';
  const fullDesc = currentSlideData.value.desc || '';

  let i = 0;
  titleTimer = setInterval(() => {
    if (i < fullTitle.length) {
      typedTitle.value += fullTitle.charAt(i);
      i++;
    } else {
      clearInterval(titleTimer);
      let j = 0;
      descTimer = setInterval(() => {
        if (j < fullDesc.length) {
          typedDesc.value += fullDesc.charAt(j);
          j++;
        } else {
          clearInterval(descTimer);
        }
      }, 12);
    }
  }, 25);
};

const nextSlide = () => {
  if (!slides.value.length) return;
  currentIndex.value = (currentIndex.value + 1) % slides.value.length;
};

const prevSlide = () => {
  if (!slides.value.length) return;
  currentIndex.value = (currentIndex.value - 1 + slides.value.length) % slides.value.length;
};

const onVideoEnded = () => {
  if (videoRef.value) videoRef.value.pause();
  if (videoBlurRef.value) videoBlurRef.value.pause();
  nextSlide();
};

// Xử lý khi video chính đã sẵn sàng phát
const onMainVideoCanPlay = (e) => {
  const video = e.target;
  if (!video) return;

  if (isPlaying.value) {
    video.muted = isMuted.value;
    video.play().catch(() => {
      isMuted.value = true;
      video.muted = true;
      video.play().catch(() => {});
    });
  } else {
    video.pause();
  }
};

// Xử lý khi video nền mờ đã sẵn sàng phát
const onBlurVideoCanPlay = (e) => {
  const video = e.target;
  if (!video) return;

  if (isPlaying.value) {
    video.muted = true;
    video.play().catch(() => {});
  } else {
    video.pause();
  }
};

const togglePlay = () => {
  isPlaying.value = !isPlaying.value;
  
  if (isPlaying.value) {
    if (videoRef.value) {
      videoRef.value.muted = isMuted.value;
      videoRef.value.play().catch(() => {
        isMuted.value = true;
        videoRef.value.muted = true;
        videoRef.value.play().catch(() => {});
      });
    }
    if (videoBlurRef.value) {
      videoBlurRef.value.muted = true;
      videoBlurRef.value.play().catch(() => {});
    }
  } else {
    if (videoRef.value) videoRef.value.pause();
    if (videoBlurRef.value) videoBlurRef.value.pause();
  }
};

const toggleMute = () => {
  isMuted.value = !isMuted.value;
  if (videoRef.value) {
    videoRef.value.muted = isMuted.value;
  }
};

const openExternalLink = (url) => {
  if (!url || url === '#') return;
  window.open(url, '_blank', 'noopener,noreferrer');
};

watch([currentIndex, () => props.lang], () => {
  clearInterval(titleTimer);
  clearInterval(descTimer);
  typedTitle.value = '';
  typedDesc.value = '';

  startTypewriter();
});

onMounted(async () => {
  await fetchBanners();
  startTypewriter();
});

onUnmounted(() => {
  clearInterval(titleTimer);
  clearInterval(descTimer);
});

const formatNum = (num) => String(num).padStart(2, '0');
</script>

<template>
  <section class="hero-banner">
    <!-- Nền Cyberpunk mờ phía sau toàn trang -->
    <div class="hero-bg-glow"></div>
    <div class="scanline-overlay"></div>

    <div class="hero-container" v-if="slides.length > 0">
      <!-- CỘT TRÁI: THÔNG TIN & NÚT BẤM -->
      <div class="hero-left">
        <transition name="content-slide" mode="out-in">
          <div :key="currentIndex + '-' + props.lang" class="content-box">
            
            <!-- KHUNG THUMBNAIL ÔM VỪA KHÍT 100% + ANIMATION -->
            <div 
              class="thumb-badge" 
              :class="isThumbVertical ? 'thumb-vertical' : 'thumb-horizontal'" 
              v-if="currentSlideData.thumbUrl"
            >
              <img 
                :src="currentSlideData.thumbUrl" 
                alt="thumb" 
                @load="handleThumbLoad"
                @error="(e) => e.target.style.display='none'"
              />
            </div>

            <h1 class="hero-title">
              {{ typedTitle }}<span class="cursor" v-if="typedTitle.length < (currentSlideData.title || '').length">|</span>
            </h1>

            <p class="hero-desc">
              {{ typedDesc }}
            </p>

            <button 
              type="button"
              class="hero-btn"
              @click="openExternalLink(currentSlideData.link)"
            >
              {{ currentSlideData.btnText }}
            </button>

          </div>
        </transition>

        <!-- Điều khiển Slide -->
        <div class="hero-controls">
          <div class="counter">
            <span class="current">{{ formatNum(currentIndex + 1) }}</span>
            <span class="sep">/</span>
            <span class="total">{{ formatNum(slides.length) }}</span>
          </div>

          <div class="nav-buttons">
            <button class="ctrl-btn" @click="prevSlide" :title="controlTitles.prev">&lt;</button>
            <button class="ctrl-btn" @click="nextSlide" :title="controlTitles.next">&gt;</button>
          </div>

          <button 
            class="ctrl-btn" 
            @click="togglePlay" 
            :title="isPlaying ? controlTitles.pause : controlTitles.play"
          >
            <span v-if="isPlaying">||</span>
            <span v-else>▶</span>
          </button>

          <button 
            class="ctrl-btn" 
            @click="toggleMute" 
            :title="isMuted ? controlTitles.unmute : controlTitles.mute"
          >
            <svg v-if="isMuted" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <polygon points="11 5 6 9 2 9 2 15 6 15 11 19 11 5"></polygon>
              <line x1="23" y1="9" x2="17" y2="15"></line>
              <line x1="17" y1="9" x2="23" y2="15"></line>
            </svg>
            <svg v-else xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <polygon points="11 5 6 9 2 9 2 15 6 15 11 19 11 5"></polygon>
              <path d="M19.07 4.93a10 10 0 0 1 0 14.14M15.54 8.46a5 5 0 0 1 0 7.07"></path>
            </svg>
          </button>
        </div>
      </div>

      <!-- CỘT PHẢI: KHUNG CARD MỜ RỘNG - VIDEO -->
      <div class="hero-right">
        <div class="video-frame">
          <transition name="fade-video" mode="out-in">
            <div :key="currentIndex" class="video-container">
              <!-- Nền mờ -->
              <video
                ref="videoBlurRef"
                class="video-card-blur"
                :muted="true"
                loop
                playsinline
                @canplay="onBlurVideoCanPlay"
              >
                <source :src="currentSlideData.videoUrl" type="video/mp4" />
              </video>

              <!-- Video chính -->
              <video
                ref="videoRef"
                class="video-card-main"
                :muted="isMuted"
                playsinline
                @canplay="onMainVideoCanPlay"
                @ended="onVideoEnded"
              >
                <source :src="currentSlideData.videoUrl" type="video/mp4" />
              </video>
            </div>
          </transition>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Oswald:wght@600;700&family=Roboto+Mono:wght@400;500&display=swap');

.hero-banner {
  position: relative;
  width: 100vw;
  left: 50%;
  right: 50%;
  margin-left: -50vw;
  margin-right: -50vw;
  height: calc(100vh - 65px);
  min-height: 600px;
  overflow: hidden;
  font-family: 'Oswald', sans-serif;
  color: #ffffff;
  background-color: #08090c;
  display: flex;
  align-items: center;
}

.hero-bg-glow {
  position: absolute;
  inset: 0;
  background: radial-gradient(circle at 75% 50%, rgba(226, 183, 85, 0.08) 0%, rgba(5, 7, 12, 0.95) 70%);
  z-index: 1;
}

.scanline-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(rgba(18, 16, 16, 0) 50%, rgba(0, 0, 0, 0.25) 50%);
  background-size: 100% 4px;
  z-index: 2;
  pointer-events: none;
}

.hero-container {
  position: relative;
  z-index: 5;
  width: 100%;
  max-width: 1650px;
  height: 100%;
  margin: 0 auto;
  padding: 75px 4% 15px 4%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 30px;
  box-sizing: border-box;
}

/* CỘT TRÁI */
.hero-left {
  flex: 0 0 38%;
  max-width: 550px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

/* KHUNG THUMBNAIL TỰ ĐỘNG ÔM VỪA KHÍT BẰNG LINE-HEIGHT: 0 & DISPLAY INLINE-BLOCK */
.thumb-badge {
  display: inline-block;
  line-height: 0;
  border: 2px solid #e2b755;
  border-radius: 4px;
  margin-bottom: 16px;
  overflow: hidden;
  box-shadow: 0 0 15px rgba(226, 183, 85, 0.35);
  background: transparent;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  animation: thumbPop 0.45s cubic-bezier(0.175, 0.885, 0.32, 1.275);
}

.thumb-badge:hover {
  transform: scale(1.05);
  box-shadow: 0 0 22px rgba(226, 183, 85, 0.6);
}

/* KÍCH THƯỚC DÀNH CHO ẢNH DỌC */
.thumb-badge.thumb-vertical img {
  display: block;
  height: 135px;
  width: auto;
}

/* KÍCH THƯỚC DÀNH CHO ẢNH NGANG */
.thumb-badge.thumb-horizontal img {
  display: block;
  width: 160px;
  height: auto;
}

/* HIỆU ỨNG ANIMATION HIỂN THỊ KHUNG THUMBNAIL */
@keyframes thumbPop {
  0% {
    opacity: 0;
    transform: scale(0.8) translateY(-10px);
    box-shadow: 0 0 0px rgba(226, 183, 85, 0);
  }
  50% {
    box-shadow: 0 0 25px rgba(226, 183, 85, 0.8);
  }
  100% {
    opacity: 1;
    transform: scale(1) translateY(0);
    box-shadow: 0 0 15px rgba(226, 183, 85, 0.35);
  }
}

.hero-title {
  font-size: 2.8rem;
  font-weight: 700;
  line-height: 1.15;
  text-transform: uppercase;
  letter-spacing: 1.5px;
  margin-bottom: 15px;
  min-height: 80px;
  color: #ffffff;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.8);
}

.cursor {
  color: #e2b755;
  animation: blink 0.7s infinite;
  margin-left: 2px;
}

@keyframes blink {
  0%, 100% { opacity: 1; }
  50% { opacity: 0; }
}

.hero-desc {
  font-family: 'Roboto Mono', monospace;
  font-size: 0.95rem;
  color: #cccccc;
  line-height: 1.6;
  margin-bottom: 25px;
  min-height: 50px;
}

.hero-btn {
  display: inline-block;
  align-self: flex-start;
  padding: 12px 28px;
  border: 1px solid #e2b755;
  background: rgba(226, 183, 85, 0.1);
  color: #e2b755;
  font-family: 'Oswald', sans-serif;
  font-size: 1rem;
  font-weight: 700;
  letter-spacing: 1.5px;
  cursor: pointer;
  transition: all 0.25s ease;
  margin-bottom: 30px;
}

.hero-btn:hover {
  background: #e2b755;
  color: #000000;
  box-shadow: 0 0 20px rgba(226, 183, 85, 0.6);
}

.hero-controls {
  display: flex;
  align-items: center;
  gap: 12px;
  background: rgba(15, 17, 23, 0.85);
  backdrop-filter: blur(10px);
  padding: 8px 16px;
  border: 1px solid rgba(255, 255, 255, 0.15);
  width: fit-content;
}

.counter {
  font-size: 1.1rem;
  font-weight: 700;
  letter-spacing: 2px;
  padding-right: 10px;
}

.counter .current { color: #ffffff; }
.counter .sep { color: #555; margin: 0 4px; }
.counter .total { color: #888888; }

.nav-buttons { display: flex; gap: 4px; }

.ctrl-btn {
  background: transparent;
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: #ffffff;
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 0.9rem;
  transition: all 0.2s;
}

.ctrl-btn:hover {
  background: rgba(226, 183, 85, 0.2);
  border-color: #e2b755;
  color: #e2b755;
}

/* CỘT PHẢI */
.hero-right {
  flex: 1;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: flex-end;
}

.video-frame {
  position: relative;
  width: 100%;
  max-width: 820px;
  height: 85%;
  margin-top: 30px;
  max-height: 640px;
  border-radius: 12px;
  overflow: hidden;
  background: #000;
  box-shadow: 0 0 35px rgba(0, 0, 0, 0.9), 0 0 20px rgba(226, 183, 85, 0.25);
  border: 1px solid rgba(226, 183, 85, 0.4);
}

.video-container {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
  padding: 5px 0;
  box-sizing: border-box;
}

.video-card-blur {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  filter: blur(25px) brightness(0.35);
  transform: scale(1.1);
  pointer-events: none;
}

.video-card-main {
  position: relative;
  z-index: 2;
  width: 100%;
  height: 100%;
  object-fit: contain;
  border-radius: 6px;
  filter: drop-shadow(0 0 18px rgba(0, 0, 0, 0.85));
}

.content-slide-enter-active { transition: all 0.4s ease-out; }
.content-slide-leave-active { transition: all 0.25s ease-in; }
.content-slide-enter-from { opacity: 0; transform: translateX(-20px); }
.content-slide-leave-to { opacity: 0; transform: translateX(20px); }

.fade-video-enter-active, .fade-video-leave-active { transition: opacity 0.5s ease; }
.fade-video-enter-from, .fade-video-leave-to { opacity: 0; }

/* OPTIMIZATION CHO RESPONSIVE MOBILE & TABLET */
@media (max-width: 1024px) {
  .hero-banner {
    height: auto;
    min-height: 100vh;
    padding: 20px 0;
  }

  .hero-container {
    flex-direction: column;
    padding: 40px 20px 20px 20px;
    gap: 20px;
  }

  .hero-left {
    flex: initial;
    width: 100%;
    max-width: 100%;
    order: 2;
  }

  .hero-right {
    width: 100%;
    order: 1;
  }

  .video-frame {
    height: 40vh;
    min-height: 250px;
    max-height: 400px;
    margin-top: 0;
  }

  .hero-title {
    font-size: 1.8rem;
    min-height: auto;
    margin-bottom: 10px;
  }

  .hero-desc {
    font-size: 0.85rem;
    min-height: auto;
    margin-bottom: 15px;
  }

  .thumb-badge.thumb-vertical img {
    height: 100px;
  }

  .thumb-badge.thumb-horizontal img {
    width: 120px;
  }

  .hero-btn {
    padding: 10px 20px;
    font-size: 0.9rem;
    margin-bottom: 20px;
    width: 100%;
    text-align: center;
  }

  .hero-controls {
    width: 100%;
    justify-content: space-between;
    box-sizing: border-box;
  }
}

@media (max-width: 480px) {
  .hero-banner {
    padding: 10px 0;
  }

  .video-frame {
    height: 30vh;
    min-height: 200px;
  }

  .hero-title {
    font-size: 1.5rem;
  }

  .ctrl-btn {
    width: 32px;
    height: 32px;
  }
}
</style>