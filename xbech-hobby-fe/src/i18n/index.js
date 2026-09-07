import { createI18n } from 'vue-i18n';

const messages = {
  vi: {
    nav: { series: 'SERIES', news: 'TIN TỨC', products: 'SẢN PHẨM', login: 'ĐĂNG NHẬP', language: 'NGÔN NGỮ' },
    hero: { latestNews: 'TIN MỚI NHẤT', viewDetail: 'Xem Chi Tiết' },
    home: { seriesTitle: 'DANH SÁCH SERIES', gundamTitle: 'MÔ HÌNH GUNDAM' }
  },
  en: {
    nav: { series: 'SERIES', news: 'NEWS', products: 'PRODUCTS', login: 'LOG IN', language: 'LANGUAGE' },
    hero: { latestNews: 'LATEST NEWS', viewDetail: 'View Detail' },
    home: { seriesTitle: 'SERIES', gundamTitle: 'GUNDAM KITS' }
  }
};

const i18n = createI18n({
  locale: 'vi',
  fallbackLocale: 'en',
  messages,
});

export default i18n;