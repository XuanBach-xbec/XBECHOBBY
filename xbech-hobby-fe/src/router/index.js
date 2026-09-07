import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../Views/HomeView.vue';
import SeriesView from '../Views/SeriesView.vue';
import GundamDetailView from '../Views/GundamDetailView.vue';
import ReviewDetailView from '../Views/ReviewDetailView.vue';

const routes = [
  {
    path: '/',
    redirect: '/trang-chu'
  },
  {
    path: '/trang-chu',
    name: 'HomeVi',
    component: HomeView
  },
  {
    path: '/home',
    name: 'HomeEn',
    component: HomeView
  },
  {
    path: '/series',
    name: 'Series',
    component: SeriesView
  },
  {
    path: '/gundam/:id',
    name: 'GundamDetail',
    component: GundamDetailView
  },
  {
    path: '/review/:id',
    name: 'ReviewDetail',
    component: ReviewDetailView
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;