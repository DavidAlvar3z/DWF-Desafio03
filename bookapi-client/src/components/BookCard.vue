<template>
  <div class="book-card">
    <div class="book-cover">
      <div class="book-placeholder">
        <span class="book-icon">📖</span>
      </div>
    </div>
    <div class="book-info">
      <h3>{{ book.title }}</h3>
      <p class="author">{{ book.author }}</p>
      <p class="genre">{{ book.genre }}</p>
      <p class="pages">{{ book.pageCount }} páginas</p>
      <div class="availability">
        <span :class="book.isAvailable ? 'available' : 'unavailable'">
          {{ book.isAvailable ? '✅ Disponible' : '❌ No disponible' }}
        </span>
      </div>
      <router-link :to="`/books/${book.id}`" class="btn-details">
        Ver Detalles
      </router-link>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { Book } from '../types/book';

defineProps<{
  book: Book
}>();
</script>

<style scoped>
.book-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  transition: transform 0.3s, box-shadow 0.3s;
}

.book-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0,0,0,0.15);
}

.book-cover {
  height: 250px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
}

.book-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.book-icon {
  font-size: 80px;
  opacity: 0.8;
}

.book-info {
  padding: 1.5rem;
}

.book-info h3 {
  margin: 0 0 0.5rem 0;
  color: #333;
  font-size: 1.2rem;
  min-height: 2.4rem;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  line-clamp: 2; 
  overflow: hidden;
}

.author {
  color: #666;
  margin: 0 0 0.5rem 0;
  font-weight: 500;
}

.genre {
  color: #999;
  font-size: 0.9rem;
  margin: 0 0 0.5rem 0;
}

.pages {
  color: #667eea;
  font-weight: 600;
  margin: 0 0 1rem 0;
}

.availability {
  margin-bottom: 1rem;
}

.available {
  color: #4caf50;
  font-weight: 600;
  font-size: 0.95rem;
}

.unavailable {
  color: #f44336;
  font-weight: 600;
  font-size: 0.95rem;
}

.btn-details {
  display: block;
  text-align: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 0.75rem;
  border-radius: 8px;
  text-decoration: none;
  font-weight: 600;
  transition: transform 0.2s;
}

.btn-details:hover {
  transform: scale(1.05);
}
</style>
