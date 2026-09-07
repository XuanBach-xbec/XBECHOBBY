<script setup>
import { computed } from 'vue';

const props = defineProps({
  lang: {
    type: String,
    default: 'vi'
  }
});

const isEn = computed(() => props.lang === 'en');

const sectionData = computed(() => ({
  moduleTag: 'MODULE.002 //',
  title: 'NEWS',
  description: isEn.value
    ? 'Check out the latest news, official announcements, event updates, and release info across all Gundam titles.'
    : 'Cập nhật tin tức mới nhất, thông báo chính thức, sự kiện nổi bật và thông tin phát hành về thế giới Gundam.',
  viewAll: isEn.value ? 'VIEW ALL NEWS ▶' : 'XEM TẤT CẢ TIN TỨC ▶'
}));

// Dịch danh mục bài viết theo ngôn ngữ
const formatCategory = (category) => {
  if (isEn.value) return category;
  const categories = {
    'EVENT': 'SỰ KIỆN',
    'INFO': 'THÔNG TIN',
    'PRODUCT': 'SẢN PHẨM',
    'ANIME': 'ANIME'
  };
  return categories[category] || category;
};

// Danh sách tin tức chuẩn hóa hỗ trợ song ngữ VI / EN
const newsList = computed(() => [
  {
    id: 1,
    category: 'ANIME',
    date: '2026.09.01',
    title: isEn.value
      ? 'Special Anime Project "GUNDAM MOVIE MEMORY" Announced to Celebrate 2.5 Million Youtube Subscribers!'
      : 'Công bố dự án Anime đặc biệt "GUNDAM MOVIE MEMORY" mừng kênh YouTube chính thức đạt 2.5 triệu người đăng ký!',
    image: 'https://images.unsplash.com/photo-1578632767115-351597cf2477?w=800&auto=format&fit=crop&q=80',
    isFeatured: true
  },
  {
    id: 2,
    category: 'EVENT',
    date: '2026.08.28',
    title: isEn.value
      ? 'Admission Ticket Lottery Sales Started for Gundam 50th Anniversary Celebration Event "GUNDAM-Con 2027".'
      : 'Chính thức mở đợt đăng ký vé bốc thăm tham dự siêu sự kiện Kỷ niệm 50 năm "GUNDAM-Con 2027 SIDE MAKUHARI".',
    image: 'https://images.unsplash.com/photo-1534447677768-be436bb09401?w=600&auto=format&fit=crop&q=80',
    isFeatured: false
  },
  {
    id: 3,
    category: 'PRODUCT',
    date: '2026.08.20',
    title: isEn.value
      ? 'First Official Comprehensive Gundam Merchandise Facility Opens in Osaka - "THE GUNDAM BASE OSAKA".'
      : 'Tổ hợp không gian mua sắm và trải nghiệm Gunpla chính thức "THE GUNDAM BASE OSAKA" chính thức mở cửa.',
    image: 'https://images.unsplash.com/photo-1563089145-599997674d42?w=600&auto=format&fit=crop&q=80',
    isFeatured: false
  },
  {
    id: 4,
    category: 'ANIME',
    date: '2026.08.12',
    title: isEn.value
      ? 'Gundam Enters a New Era Under Kenji Kamiyama with "Mobile Suit Gundam RG XARX-ZERO" Vision Project.'
      : 'Gundam bước vào kỷ nguyên mới dưới sự chỉ đạo của đạo diễn Kenji Kamiyama với dự án "RG XARX-ZERO".',
    image: 'https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?w=600&auto=format&fit=crop&q=80',
    isFeatured: false
  }
]);
</script>

<template>
  <section class="news-section">
    <!-- HEADER GRID -->
    <div class="news-header-grid">
      <div class="header-left">
        <span class="module-tag">{{ sectionData.moduleTag }}</span>
        <h2 class="section-title">{{ sectionData.title }}</h2>
      </div>
      <div class="header-right">
        <p class="section-desc">{{ sectionData.description }}</p>
      </div>
    </div>

    <!-- KHU VỰC DANH SÁCH TIN TỨC -->
    <div class="news-container">
      <div 
        v-for="item in newsList" 
        :key="item.id" 
        class="news-card"
        :class="{ 'featured-card': item.isFeatured }"
      >
        <router-link :to="`/news/${item.id}`" class="card-link">
          <div class="image-wrapper">
            <img :src="item.image" :alt="item.title" class="news-img" loading="lazy" />
            <span class="category-badge">{{ formatCategory(item.category) }}</span>
          </div>

          <div class="news-info">
            <span class="news-date">{{ item.date }}</span>
            <h3 class="news-title">{{ item.title }}</h3>
          </div>
        </router-link>
      </div>
    </div>

    <!-- DÒNG NÚT DƯỚI CÙNG -->
    <div class="controls-bottom">
      <router-link to="/news" class="view-all-btn">
        {{ sectionData.viewAll }}
      </router-link>
    </div>
  </section>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Oswald:wght@500;700&family=Roboto+Mono:wght@400;500;700&display=swap');

.news-section {
  background-color: #ffffff;
  color: #000000;
  padding: 60px 0 40px 0;
  font-family: 'Oswald', sans-serif;
  border-top: 1px solid #e0e0e0;
}

.news-header-grid {
  display: grid;
  grid-template-columns: 1fr 380px;
  gap: 40px;
  align-items: flex-end;
  padding: 0 40px 30px 40px;
  margin-bottom: 20px;
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

/* LAYOUT LƯỚI TIN TỨC */
.news-container {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 30px;
  padding: 0 40px 40px 40px;
}

.news-card {
  border-bottom: 1px solid #e0e0e0;
  padding-bottom: 20px;
}

.news-card.featured-card {
  grid-column: span 3;
  border-bottom: 2px solid #000000;
  padding-bottom: 30px;
  margin-bottom: 10px;
}

.featured-card .card-link {
  display: grid;
  grid-template-columns: 1.2fr 1fr;
  gap: 30px;
  align-items: center;
}

.card-link {
  text-decoration: none;
  color: inherit;
  display: flex;
  flex-direction: column;
}

.image-wrapper {
  position: relative;
  width: 100%;
  aspect-ratio: 16 / 9;
  background-color: #f0f0f0;
  overflow: hidden;
  border-radius: 4px;
}

.news-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.4s ease;
}

.card-link:hover .news-img {
  transform: scale(1.05);
}

.category-badge {
  position: absolute;
  top: 12px;
  left: 12px;
  background-color: #000000;
  color: #ffffff;
  font-family: 'Roboto Mono', monospace;
  font-size: 0.7rem;
  font-weight: 700;
  padding: 4px 10px;
  letter-spacing: 1px;
}

.news-info {
  margin-top: 16px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.news-date {
  font-family: 'Roboto Mono', monospace;
  font-size: 0.78rem;
  font-weight: 700;
  color: #333333;
}

.news-title {
  font-size: 1.15rem;
  font-weight: 700;
  line-height: 1.4;
  color: #111111;
  margin: 0;
  transition: color 0.2s ease;
}

.featured-card .news-title {
  font-size: 1.8rem;
}

.card-link:hover .news-title {
  color: #e2b755;
}

/* NÚT XEM TẤT CẢ BÊN DƯỚI */
.controls-bottom {
  display: flex;
  justify-content: flex-end;
  padding: 10px 40px 0 40px;
  border-top: 1px solid #eeeeee;
}

.view-all-btn {
  font-family: 'Roboto Mono', monospace;
  font-size: 0.8rem;
  font-weight: 700;
  color: #ffffff;
  background-color: #000000;
  padding: 10px 22px;
  text-decoration: none;
  letter-spacing: 1px;
  transition: background-color 0.2s ease;
}

.view-all-btn:hover {
  background-color: #e2b755;
  color: #000000;
}

/* RESPONSIVE TABLET & MOBILE */
@media (max-width: 990px) {
  .news-header-grid {
    grid-template-columns: 1fr;
    gap: 15px;
    padding: 0 20px 20px 20px;
  }
  .header-right {
    border-left: none;
    padding-left: 0;
  }
  .news-container {
    grid-template-columns: repeat(2, 1fr);
    padding: 0 20px 20px 20px;
    gap: 24px;
  }
  .news-card.featured-card {
    grid-column: span 2;
  }
  .featured-card .card-link {
    grid-template-columns: 1fr;
    gap: 16px;
  }
  .featured-card .news-title {
    font-size: 1.4rem;
  }
}

@media (max-width: 600px) {
  .news-section {
    padding: 40px 0 20px 0;
  }
  .news-header-grid {
    padding: 0 16px 15px 16px;
  }
  .news-container {
    grid-template-columns: 1fr;
    padding: 0 16px 20px 16px;
    gap: 20px;
  }
  .news-card.featured-card {
    grid-column: span 1;
    padding-bottom: 20px;
  }
  .featured-card .news-title {
    font-size: 1.25rem;
  }
  .controls-bottom {
    padding: 15px 16px 0 16px;
  }
  .view-all-btn {
    width: 100%;
    text-align: center;
    box-sizing: border-box;
    padding: 12px 18px; /* Tối ưu vùng chạm cảm ứng cho mobile */
  }
}
</style>