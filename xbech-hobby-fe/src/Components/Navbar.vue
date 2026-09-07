<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue';
import logoImg from '../Assets/logo.png';

const props = defineProps({
  lang: {
    type: String,
    default: 'vi'
  }
});

const emit = defineEmits(['change-lang']);

const isLangOpen = ref(false);
const isSnsOpen = ref(false);
const isMobileMenuOpen = ref(false); // Trạng thái mở/đóng Mobile Menu

const translations = {
  vi: { 
    latestNews: 'TIN MỚI NHẤT', 
    language: 'NGÔN NGỮ',
    series: 'SERIES', 
    news: 'TIN TỨC', 
    products: 'SẢN PHẨM', 
    login: 'ĐĂNG NHẬP' 
  },
  en: { 
    latestNews: 'LATEST NEWS', 
    language: 'LANGUAGE',
    series: 'SERIES', 
    news: 'NEWS', 
    products: 'PRODUCTS', 
    login: 'LOG IN' 
  }
};

const t = (key) => translations[props.lang]?.[key] || key;
const homePath = computed(() => (props.lang === 'vi' ? '/trang-chu' : '/home'));

const changeLanguage = (lang) => {
  if (lang !== props.lang) {
    emit('change-lang', lang);
  }
  isLangOpen.value = false;
  isMobileMenuOpen.value = false;
};

const closeMobileMenu = () => {
  isMobileMenuOpen.value = false;
};

const newsList = ref([
  'BS11 『機動戦士ガンダムユニコーン RE:0096』 放送決定！9月12日より毎週土曜19時から1話ずつ！',
  '"METAL ROBOT SPIRITS Destiny Gundam Spec II Final Battle Ver." Pre-orders Now Open!',
  'Gundam 50th Anniversary Project: "A Boy with GUNDAM" officially announced!',
  'PG 1/60 UNICORN GUNDAM PERFECTIBILITY - Limited Restock Arrival Notification'
]);

const currentNewsIndex = ref(0);
let timer = null;

onMounted(() => {
  timer = setInterval(() => {
    currentNewsIndex.value = (currentNewsIndex.value + 1) % newsList.value.length;
  }, 4000);
});

onUnmounted(() => {
  if (timer) clearInterval(timer);
});
</script>

<template>
  <header class="mecha-header">
    <!-- BANNER TIN TỨC CHẠY -->
    <div class="top-ticker">
      <div class="ticker-container">
        <span class="ticker-badge">{{ t('latestNews') }}</span>
        <span class="ticker-sep">|</span>
        <div class="ticker-viewport">
          <transition name="slide-fade" mode="out-in">
            <a :key="currentNewsIndex" href="#" class="news-link">
              {{ newsList[currentNewsIndex] }}
            </a>
          </transition>
        </div>
        <span class="ticker-arrow">&gt;</span>
      </div>
    </div>

    <!-- THANH MENU CHÍNH -->
    <nav class="main-nav">
      <!-- CỘT TRÁI: MENU HAMBURGER (MOBILE) & DIEU HUONG (DESKTOP) -->
      <div class="nav-col nav-left">
        <div class="nav-cell cell-menu">
          <button 
            class="menu-btn" 
            aria-label="Menu" 
            @click="isMobileMenuOpen = !isMobileMenuOpen"
            :class="{ active: isMobileMenuOpen }"
          >
            <span></span><span></span><span></span>
          </button>
        </div>

        <div class="nav-cell desktop-only">
          <router-link to="/series" class="nav-item">{{ t('series') }}</router-link>
        </div>
        <div class="nav-cell desktop-only">
          <router-link to="/news" class="nav-item">{{ t('news') }}</router-link>
        </div>
        <div class="nav-cell desktop-only">
          <router-link to="/products" class="nav-item">{{ t('products') }}</router-link>
        </div>
      </div>

      <!-- CỘT GIỮA: LOGO -->
      <div class="nav-col nav-center">
        <router-link :to="homePath" class="brand-link" @click="closeMobileMenu">
          <img :src="logoImg" alt="XBECHOBBY" class="logo-img" />
        </router-link>
      </div>

      <!-- CỘT PHẢI: SNS / LOGIN / NGÔN NGỮ / SEARCH -->
      <div class="nav-col nav-right">
        <!-- MENU DROPDOWN SNS -->
        <div class="nav-cell sns-cell desktop-only" @mouseleave="isSnsOpen = false">
          <button class="nav-btn dropdown-trigger" @click="isSnsOpen = !isSnsOpen">
            SNS <small class="arrow">▾</small>
          </button>
          <transition name="fade">
            <div v-if="isSnsOpen" class="sns-dropdown">
              <a 
                href="https://www.facebook.com/share/1LzK48Z8NA/?mibextid=wwXIfr" 
                target="_blank" 
                rel="noopener noreferrer" 
                class="sns-option"
              >
                <svg class="sns-svg" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.792-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z"/>
                </svg>
                FACEBOOK
              </a>
              <a 
                href="https://www.tiktok.com/@springback_938" 
                target="_blank" 
                rel="noopener noreferrer" 
                class="sns-option"
              >
                <svg class="sns-svg" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M12.525.02c1.31-.02 2.61-.01 3.91-.02.08 1.53.63 3.09 1.75 4.17 1.12 1.11 2.7 1.62 4.24 1.79v4.03c-1.44-.05-2.89-.35-4.2-.97-.57-.26-1.1-.59-1.62-.93-.01 2.92.01 5.84-.02 8.75-.08 1.4-.54 2.79-1.35 3.94-1.31 1.92-3.58 3.17-5.91 3.21-1.43.08-2.86-.31-4.08-1.03-2.02-1.19-3.44-3.37-3.65-5.71-.02-.5-.03-1-.01-1.49.18-1.9 1.12-3.72 2.58-4.96 1.66-1.44 3.98-2.13 6.15-1.72.02 1.48-.04 2.96-.04 4.44-.99-.32-2.15-.23-3.02.37-.63.41-1.11 1.07-1.3 1.8-.24.84-.06 1.78.43 2.47.53.77 1.44 1.25 2.38 1.27.91.02 1.83-.34 2.43-1.03.62-.69.96-1.62.96-2.55.02-4.8.01-9.6.01-14.4z"/>
                </svg>
                TIKTOK
              </a>
            </div>
          </transition>
        </div>

        <div class="nav-cell desktop-only">
          <router-link to="/login" class="nav-item">{{ t('login') }}</router-link>
        </div>

        <!-- DROPDOWN NGÔN NGỮ DESKTOP -->
        <div class="nav-cell lang-cell desktop-only" @mouseleave="isLangOpen = false">
          <button class="nav-btn lang-btn" @click="isLangOpen = !isLangOpen">
            {{ t('language') }} <small class="arrow">▾</small>
          </button>
          <transition name="fade">
            <div v-if="isLangOpen" class="lang-dropdown">
              <button 
                class="lang-option" 
                :class="{ active: props.lang === 'vi' }" 
                @click="changeLanguage('vi')"
              >
                <span class="flag">🇻🇳</span> TIẾNG VIỆT
              </button>
              <button 
                class="lang-option" 
                :class="{ active: props.lang === 'en' }" 
                @click="changeLanguage('en')"
              >
                <span class="flag">🇺🇸</span> ENGLISH
              </button>
            </div>
          </transition>
        </div>

        <div class="nav-cell cell-search">
          <button class="search-btn" aria-label="Search">
            <svg viewBox="0 0 24 24" class="search-svg">
              <circle cx="10.5" cy="10.5" r="6.5" stroke="currentColor" stroke-width="2.2" fill="none" />
              <line x1="15.5" y1="15.5" x2="21" y2="21" stroke="currentColor" stroke-width="2.2" stroke-linecap="round" />
            </svg>
          </button>
        </div>
      </div>
    </nav>

    <!-- MOBILE MENU DRAWER (TRƯỢT TỪ BÊN TRÁI) -->
    <transition name="mobile-slide">
      <div v-if="isMobileMenuOpen" class="mobile-drawer">
        <div class="drawer-content">
          <router-link to="/series" class="drawer-item" @click="closeMobileMenu">{{ t('series') }}</router-link>
          <router-link to="/news" class="drawer-item" @click="closeMobileMenu">{{ t('news') }}</router-link>
          <router-link to="/products" class="drawer-item" @click="closeMobileMenu">{{ t('products') }}</router-link>
          <router-link to="/login" class="drawer-item highlight" @click="closeMobileMenu">{{ t('login') }}</router-link>

          <div class="drawer-divider"></div>

          <!-- Mạng xã hội Mobile -->
          <div class="drawer-section-title">SOCIAL MEDIA</div>
          <a href="https://www.facebook.com/share/1LzK48Z8NA/?mibextid=wwXIfr" target="_blank" rel="noopener" class="drawer-sns-item">
            <svg class="sns-svg" viewBox="0 0 24 24" fill="currentColor">
              <path d="M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.792-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z"/>
            </svg>
            FACEBOOK
          </a>
          <a href="https://www.tiktok.com/@springback_938" target="_blank" rel="noopener" class="drawer-sns-item">
            <svg class="sns-svg" viewBox="0 0 24 24" fill="currentColor">
              <path d="M12.525.02c1.31-.02 2.61-.01 3.91-.02.08 1.53.63 3.09 1.75 4.17 1.12 1.11 2.7 1.62 4.24 1.79v4.03c-1.44-.05-2.89-.35-4.2-.97-.57-.26-1.1-.59-1.62-.93-.01 2.92.01 5.84-.02 8.75-.08 1.4-.54 2.79-1.35 3.94-1.31 1.92-3.58 3.17-5.91 3.21-1.43.08-2.86-.31-4.08-1.03-2.02-1.19-3.44-3.37-3.65-5.71-.02-.5-.03-1-.01-1.49.18-1.9 1.12-3.72 2.58-4.96 1.66-1.44 3.98-2.13 6.15-1.72.02 1.48-.04 2.96-.04 4.44-.99-.32-2.15-.23-3.02.37-.63.41-1.11 1.07-1.3 1.8-.24.84-.06 1.78.43 2.47.53.77 1.44 1.25 2.38 1.27.91.02 1.83-.34 2.43-1.03.62-.69.96-1.62.96-2.55.02-4.8.01-9.6.01-14.4z"/>
            </svg>
            TIKTOK
          </a>

          <div class="drawer-divider"></div>

          <!-- Ngôn ngữ Mobile -->
          <div class="drawer-section-title">{{ t('language') }}</div>
          <div class="drawer-lang-group">
            <button class="drawer-lang-btn" :class="{ active: props.lang === 'vi' }" @click="changeLanguage('vi')">
              🇻🇳 TIẾNG VIỆT
            </button>
            <button class="drawer-lang-btn" :class="{ active: props.lang === 'en' }" @click="changeLanguage('en')">
              🇺🇸 ENGLISH
            </button>
          </div>
        </div>
      </div>
    </transition>

    <!-- Nền đen mờ bấm ra ngoài để đóng menu mobile -->
    <div v-if="isMobileMenuOpen" class="drawer-overlay" @click="closeMobileMenu"></div>
  </header>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Oswald:wght@600;700&display=swap');

.mecha-header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
  font-family: 'Oswald', 'Arial Narrow', sans-serif;
  color: #ffffff;
  user-select: none;
}

.top-ticker {
  background: rgba(10, 10, 15, 0.85);
  backdrop-filter: blur(6px);
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid rgba(255, 255, 255, 0.15);
  font-size: 0.85rem;
}

.ticker-container {
  width: 100%;
  max-width: 900px;
  padding: 0 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
}

.ticker-badge {
  font-weight: 700;
  color: #e2b755;
  white-space: nowrap;
  letter-spacing: 1px;
}

.ticker-sep { color: rgba(255, 255, 255, 0.4); }

.ticker-viewport {
  flex: 1;
  overflow: hidden;
  height: 22px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.news-link {
  color: #ffffff;
  text-decoration: none;
  font-weight: 600;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  text-shadow: 0 1px 4px rgba(0, 0, 0, 0.8);
}

.news-link:hover { color: #e2b755; }
.ticker-arrow { color: #aaa; font-weight: bold; }

.main-nav {
  display: grid;
  grid-template-columns: 1fr auto 1fr;
  height: 70px;
  background: linear-gradient(180deg, rgba(0, 0, 0, 0.75) 0%, rgba(0, 0, 0, 0.5) 100%);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.25);
}

.nav-col { display: flex; align-items: center; }
.nav-left { justify-content: flex-start; }

.nav-center { 
  justify-content: center; 
  padding: 0 25px; 
  height: 100%;
}

.nav-right { justify-content: flex-end; }

.nav-cell {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  border-left: 1px solid rgba(255, 255, 255, 0.22);
  border-right: 1px solid rgba(255, 255, 255, 0.22);
  margin-left: -1px;
  padding: 0 22px;
  position: relative;
  background: rgba(255, 255, 255, 0.03);
  transition: all 0.2s ease;
  white-space: nowrap;
}

.nav-cell:hover { 
  background: rgba(255, 255, 255, 0.15); 
  border-color: rgba(226, 183, 85, 0.6);
  z-index: 2;
}

/* BUTTON HEADER */
.nav-btn, .lang-btn {
  background: transparent !important;
  border: none !important;
  outline: none !important;
  color: #ffffff !important;
  font-family: 'Oswald', sans-serif;
  font-size: 0.95rem;
  font-weight: 700;
  letter-spacing: 1.5px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 4px;
  white-space: nowrap;
  padding: 0;
  margin: 0;
  text-shadow: 0 2px 6px rgba(0, 0, 0, 0.9);
  transition: color 0.2s ease;
  -webkit-appearance: none;
  appearance: none;
}

.nav-btn:hover, .lang-btn:hover { 
  color: #e2b755 !important; 
}

.nav-item {
  color: #ffffff;
  text-decoration: none;
  font-size: 0.95rem;
  font-weight: 700;
  letter-spacing: 1.5px;
  text-transform: uppercase;
  white-space: nowrap;
  text-shadow: 0 2px 6px rgba(0, 0, 0, 0.9);
}

.nav-item:hover, .router-link-active { color: #e2b755; }

.brand-link {
  display: flex;
  align-items: center;
  justify-content: center;
  text-decoration: none;
  height: 100%;
}

.logo-img {
  height: 55px;
  width: auto;
  object-fit: contain;
  transform: scale(1.1);
  filter: drop-shadow(0 2px 8px rgba(0, 0, 0, 0.7));
  transition: transform 0.2s ease;
}

.logo-img:hover {
  transform: scale(1.18);
}

/* NUT HAMBURGER */
.menu-btn {
  background: none;
  border: none;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  gap: 4px;
  padding: 5px;
}

.menu-btn span {
  display: block;
  width: 22px;
  height: 2px;
  background-color: #ffffff;
  transition: all 0.3s ease;
}

.menu-btn.active span:nth-child(1) {
  transform: translateY(6px) rotate(45deg);
}
.menu-btn.active span:nth-child(2) {
  opacity: 0;
}
.menu-btn.active span:nth-child(3) {
  transform: translateY(-6px) rotate(-45deg);
}

/* DROPDOWN MENU */
.sns-cell, .lang-cell { position: relative; }

.sns-dropdown, .lang-dropdown {
  position: absolute;
  top: 100%;
  right: 0;
  width: 155px;
  background: rgba(15, 16, 21, 0.95);
  backdrop-filter: blur(10px);
  border: 1px solid #e2b755;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.8);
  display: flex;
  flex-direction: column;
  z-index: 1001;
}

.sns-option, .lang-option {
  background: transparent !important;
  border: none !important;
  color: #cccccc !important;
  padding: 12px 16px;
  font-family: 'Oswald', sans-serif;
  font-size: 0.88rem;
  font-weight: 700;
  text-decoration: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 12px;
  transition: background 0.2s, color 0.2s;
  -webkit-appearance: none;
  appearance: none;
}

.sns-svg {
  width: 18px;
  height: 18px;
  fill: currentColor;
  transition: transform 0.2s ease;
}

.sns-option:hover .sns-svg {
  transform: scale(1.15);
}

.sns-option:hover, .lang-option:hover, .lang-option.active {
  background: rgba(226, 183, 85, 0.25) !important;
  color: #e2b755 !important;
}

.search-btn {
  background: none;
  border: none;
  color: #fff;
  cursor: pointer;
  display: flex;
  align-items: center;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.8));
}
.search-svg { width: 20px; height: 20px; }

/* DRAWER MOBILE */
.mobile-drawer {
  position: fixed;
  top: 106px; /* 36px ticker + 70px nav */
  left: 0;
  width: 260px;
  height: calc(100vh - 106px);
  background: rgba(10, 11, 16, 0.98);
  backdrop-filter: blur(15px);
  border-right: 1px solid #e2b755;
  z-index: 999;
  overflow-y: auto;
  box-shadow: 5px 0 25px rgba(0,0,0,0.8);
}

.drawer-content {
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.drawer-item {
  color: #ffffff;
  text-decoration: none;
  font-size: 1.1rem;
  font-weight: 700;
  letter-spacing: 1.5px;
  padding: 8px 0;
  border-bottom: 1px solid rgba(255,255,255,0.05);
}

.drawer-item.highlight {
  color: #e2b755;
}

.drawer-divider {
  height: 1px;
  background: rgba(255,255,255,0.15);
  margin: 10px 0;
}

.drawer-section-title {
  font-size: 0.75rem;
  letter-spacing: 1.5px;
  color: #888;
  margin-top: 5px;
}

.drawer-sns-item {
  display: flex;
  align-items: center;
  gap: 10px;
  color: #ccc;
  text-decoration: none;
  font-weight: 700;
  font-size: 0.95rem;
  padding: 6px 0;
}

.drawer-lang-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-top: 5px;
}

.drawer-lang-btn {
  background: rgba(255,255,255,0.05);
  border: 1px solid rgba(255,255,255,0.1);
  color: #fff;
  padding: 8px 12px;
  font-family: 'Oswald', sans-serif;
  font-weight: 700;
  text-align: left;
  border-radius: 4px;
}

.drawer-lang-btn.active {
  border-color: #e2b755;
  color: #e2b755;
  background: rgba(226, 183, 85, 0.15);
}

.drawer-overlay {
  position: fixed;
  top: 106px;
  left: 0;
  width: 100vw;
  height: calc(100vh - 106px);
  background: rgba(0, 0, 0, 0.6);
  z-index: 998;
}

/* ANIMATIONS */
.slide-fade-enter-active, .slide-fade-leave-active { transition: all 0.3s ease-out; }
.slide-fade-enter-from { opacity: 0; transform: translateY(8px); }
.slide-fade-leave-to { opacity: 0; transform: translateY(-8px); }

.fade-enter-active, .fade-leave-active { transition: opacity 0.2s ease; }
.fade-enter-from, .fade-leave-to { opacity: 0; }

.mobile-slide-enter-active, .mobile-slide-leave-active { transition: transform 0.3s ease; }
.mobile-slide-enter-from, .mobile-slide-leave-to { transform: translateX(-100%); }

/* MEDIA QUERIES FOR MOBILE RESPONSIVE */
@media (max-width: 900px) {
  .desktop-only {
    display: none !important;
  }

  .nav-cell {
    padding: 0 15px;
  }

  .nav-center {
    padding: 0 10px;
  }

  .logo-img {
    height: 45px;
  }

  .ticker-badge {
    font-size: 0.75rem;
  }

  .news-link {
    font-size: 0.75rem;
  }

  .ticker-arrow {
    display: none;
  }
}

@media (max-width: 480px) {
  .top-ticker {
    height: 30px;
  }

  .mobile-drawer, .drawer-overlay {
    top: 100px; /* 30px ticker + 70px nav */
    height: calc(100vh - 100px);
  }

  .ticker-container {
    padding: 0 10px;
    gap: 6px;
  }
}
</style>