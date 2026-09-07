<template>
  <div class="gundam-card">
    <img :src="gundam.coverImage" :alt="gundam.name" class="card-img" />
    <div class="card-content">
      <div class="tags">
        <span class="badge grade">{{ gundam.grade }}</span>
        <span class="badge scale">{{ gundam.scale }}</span>
      </div>
      <h3 class="card-title">{{ gundam.name }}</h3>
      <p class="series-name" v-if="gundam.series">
        Series: {{ gundam.series.title }}
      </p>
      <p class="description">{{ gundam.originDescription }}</p>
    </div>
  </div>
</template>

<script setup>
defineProps({
  gundam: {
    type: Object,
    required: true
  }
});
</script>

<style scoped>
.gundam-card {
  background-color: #1a1a1a;
  border-radius: 12px;
  overflow: hidden;
  border: 1px solid #333;
  transition: transform 0.2s, box-shadow 0.2s;
  color: #fff;
}

.gundam-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(255, 255, 255, 0.1);
}

.card-img {
  width: 100%;
  height: 220px;
  object-fit: cover;
}

.card-content {
  padding: 1rem;
}

.tags {
  display: flex;
  gap: 8px;
  margin-bottom: 8px;
}

.badge {
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 0.75rem;
  font-weight: bold;
}

.grade { background-color: #e53e3e; }
.scale { background-color: #3182ce; }

.card-title {
  font-size: 1.1rem;
  margin: 0 0 4px 0;
}

.series-name {
  font-size: 0.85rem;
  color: #a0aec0;
  margin-bottom: 8px;
}

.description {
  font-size: 0.85rem;
  color: #cbd5e0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>