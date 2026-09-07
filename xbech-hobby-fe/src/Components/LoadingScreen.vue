<script setup>
import { ref, watch, computed, onUnmounted } from 'vue';
import logoImg from '../Assets/logo.png';

const props = defineProps({
  show: {
    type: Boolean,
    default: false
  },
  lang: {
    type: String,
    default: 'vi'
  }
});

const emit = defineEmits(['finish']);

const progress = ref(0);
const isComplete = ref(false);
let timer = null;

// Tự động chuyển toàn bộ văn bản theo ngôn ngữ
const labels = computed(() => {
  const isEn = props.lang === 'en';
  return {
    loadingText: isEn ? 'LOADING' : 'ĐANG TẢI',
    statusText: isComplete.value 
      ? (isEn ? 'LAUNCH' : 'CẤT CÁNH') 
      : (isEn ? 'STANDBY' : 'SẴN SÀNG')
  };
});

const startLoading = () => {
  progress.value = 0;
  isComplete.value = false;
  clearInterval(timer);

  timer = setInterval(() => {
    progress.value += Math.floor(Math.random() * 4) + 1;

    if (progress.value >= 100) {
      progress.value = 100;
      clearInterval(timer);
      isComplete.value = true; // Chuyển sang LAUNCH (English) hoặc CẤT CÁNH (Tiếng Việt)

      // Giữ hiệu ứng nhấp nháy 800ms trước khi đóng loading
      setTimeout(() => {
        emit('finish');
      }, 800);
    }
  }, 30);
};

watch(() => props.show, (newVal) => {
  if (newVal) {
    startLoading();
  }
});

onUnmounted(() => {
  clearInterval(timer);
});
</script>

<template>
  <Teleport to="body">
    <Transition name="fade">
      <div v-if="show" class="gundam-loading-overlay">
        <div class="loading-container">
          <div class="logo-wrapper">
            <img :src="logoImg" alt="XBECHOBBY" class="loading-logo" />
          </div>

          <div class="status-row">
            <div class="loading-text">
              {{ labels.loadingText }} : <span class="percent">{{ progress }}%</span>
              <span class="arrows">►►</span>
            </div>

            <div class="standby-box" :class="{ 'is-launch': isComplete }">
              {{ labels.statusText }}
            </div>
          </div>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Roboto+Mono:wght@500;700&display=swap');

.gundam-loading-overlay {
  position: fixed;
  inset: 0;
  z-index: 999999;
  background-color: #ffffff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: 'Roboto Mono', monospace;
  color: #000000;
  padding: 20px;
  box-sizing: border-box;
  touch-action: none; /* Khóa thao tác vuốt cuộn màn hình khi đang loading */
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 30px;
  max-width: 100%;
}

.logo-wrapper {
  animation: logoPulse 1.8s ease-in-out infinite;
  display: flex;
  justify-content: center;
}

.loading-logo {
  height: 75px;
  width: auto;
  max-width: 80vw;
  object-fit: contain;
  filter: drop-shadow(0 4px 10px rgba(0, 0, 0, 0.15));
}

@keyframes logoPulse {
  0%, 100% { transform: scale(1); opacity: 1; }
  50% { transform: scale(1.06); opacity: 0.85; }
}

.status-row {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 25px;
  font-size: 0.85rem;
  font-weight: 700;
  letter-spacing: 1.5px;
  flex-wrap: wrap;
}

.loading-text {
  display: flex;
  align-items: center;
  gap: 8px;
  white-space: nowrap;
}

.percent {
  display: inline-block;
  min-width: 42px;
}

.arrows {
  font-size: 0.75rem;
  letter-spacing: -2px;
}

.standby-box {
  border: 1.5px solid #000000;
  padding: 3px 10px;
  font-size: 0.75rem;
  letter-spacing: 1.2px;
  background: #ffffff;
  color: #000000;
  transition: all 0.2s ease;
  white-space: nowrap;
}

/* Nhấp nháy khi đạt 100% */
.standby-box.is-launch {
  animation: launchBlink 0.2s infinite alternate;
}

@keyframes launchBlink {
  0% {
    background-color: #000000;
    color: #ffffff;
    border-color: #000000;
  }
  100% {
    background-color: #ffffff;
    color: #000000;
    border-color: #000000;
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

/* FIX GIAO DIỆN MOBLE & TABLET */
@media (max-width: 600px) {
  .loading-container {
    gap: 20px;
  }

  .loading-logo {
    height: 55px;
  }

  .status-row {
    gap: 15px;
    font-size: 0.8rem;
  }
}

@media (max-width: 400px) {
  .status-row {
    flex-direction: column;
    gap: 12px;
  }

  .loading-logo {
    height: 48px;
  }

  .standby-box {
    font-size: 0.7rem;
    padding: 2px 8px;
  }
}
</style>