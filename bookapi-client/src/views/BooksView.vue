<template>
  <div class="books-view">
    <NavBar />
    
    <div class="container">
      <!-- Hero Header -->
      <div class="hero-header">
        <div class="hero-content">
          <h1>
            <span class="icon-animate">📚</span>
            Catálogo de Libros
          </h1>
          <p>Descubre tu próxima gran lectura entre nuestra colección</p>
        </div>
        
        <!-- Search Bar -->
        <div class="search-container">
          <div class="search-box">
            <svg viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z" clip-rule="evenodd" />
            </svg>
            <input
              v-model="searchQuery"
              type="text"
              placeholder="Buscar por título, autor, género..."
              @input="handleSearch"
            />
            <button v-if="searchQuery" @click="clearSearch" class="clear-btn">
              <svg viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clip-rule="evenodd" />
              </svg>
            </button>
          </div>
        </div>

        <!-- Stats -->
        <div class="catalog-stats">
          <div class="stat-item">
            <svg viewBox="0 0 20 20" fill="currentColor">
              <path d="M9 4.804A7.968 7.968 0 005.5 4c-1.255 0-2.443.29-3.5.804v10A7.969 7.969 0 015.5 14c1.669 0 3.218.51 4.5 1.385A7.962 7.962 0 0114.5 14c1.255 0 2.443.29 3.5.804v-10A7.968 7.968 0 0014.5 4c-1.255 0-2.443.29-3.5.804V12a1 1 0 11-2 0V4.804z" />
            </svg>
            <span>{{ books.length }} libros</span>
          </div>
          <div class="stat-item">
            <svg viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />
            </svg>
            <span>Siempre disponible</span>
          </div>
        </div>
      </div>

      <!-- Loading State -->
      <div v-if="loading" class="loading-container">
        <div class="loader-wrapper">
          <div class="loader"></div>
          <p>Cargando catálogo...</p>
        </div>
      </div>

      <!-- Error State -->
      <div v-else-if="error" class="error-container">
        <svg viewBox="0 0 20 20" fill="currentColor">
          <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clip-rule="evenodd" />
        </svg>
        <h3>¡Oops! Algo salió mal</h3>
        <p>{{ error }}</p>
        <button @click="loadBooks" class="retry-btn">
          <svg viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M4 2a1 1 0 011 1v2.101a7.002 7.002 0 0111.601 2.566 1 1 0 11-1.885.666A5.002 5.002 0 005.999 7H9a1 1 0 010 2H4a1 1 0 01-1-1V3a1 1 0 011-1zm.008 9.057a1 1 0 011.276.61A5.002 5.002 0 0014.001 13H11a1 1 0 110-2h5a1 1 0 011 1v5a1 1 0 11-2 0v-2.101a7.002 7.002 0 01-11.601-2.566 1 1 0 01.61-1.276z" clip-rule="evenodd" />
          </svg>
          Reintentar
        </button>
      </div>
      
      <!-- Books Grid -->
      <transition-group v-else-if="books.length > 0" name="book-list" tag="div" class="books-grid">
        <BookCard v-for="book in books" :key="book.id" :book="book" />
      </transition-group>

      <!-- Empty State -->
      <div v-else-if="!loading && books.length === 0" class="empty-state">
        <div class="empty-icon">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M4 19.5A2.5 2.5 0 016.5 17H20"></path>
            <path d="M6.5 2H20v20H6.5A2.5 2.5 0 014 19.5v-15A2.5 2.5 0 016.5 2z"></path>
          </svg>
        </div>
        <h3>No se encontraron libros</h3>
        <p>{{ searchQuery ? 'Intenta con otra búsqueda' : 'Aún no hay libros disponibles' }}</p>
        <button v-if="searchQuery" @click="clearSearch" class="btn-secondary">
          Limpiar búsqueda
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import NavBar from '../components/NavBar.vue';
import BookCard from '../components/BookCard.vue';
import { bookService } from '../services/bookService';
import type { Book } from '../types/book';

const books = ref<Book[]>([]);
const loading = ref(false);
const error = ref('');
const searchQuery = ref('');
const allBooks = ref<Book[]>([]); // Para mantener la lista original

const loadBooks = async () => {
  loading.value = true;
  error.value = '';
  try {
    const result = await bookService.getAllBooks();
    books.value = result;
    allBooks.value = result;
  } catch (err: any) {
    error.value = 'No pudimos cargar el catálogo. Por favor, intenta de nuevo.';
  } finally {
    loading.value = false;
  }
};

const handleSearch = () => {
  if (!searchQuery.value.trim()) {
    books.value = [...allBooks.value];
    return;
  }
  const query = searchQuery.value.toLowerCase();
  books.value = allBooks.value.filter(book =>
    book.title.toLowerCase().includes(query) ||
    book.author.toLowerCase().includes(query) ||
    book.genre.toLowerCase().includes(query)
  );
};

const clearSearch = () => {
  searchQuery.value = '';
  books.value = [...allBooks.value];
};

onMounted(() => {
  loadBooks();
});
</script>


<style scoped>
.books-view {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #e8ecf1 100%);
}

.container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 2rem;
}

/* Hero Header */
.hero-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 24px;
  padding: 3rem;
  margin-bottom: 3rem;
  box-shadow: 0 10px 40px rgba(102, 126, 234, 0.3);
  position: relative;
  overflow: hidden;
}

.hero-header::before {
  content: '';
  position: absolute;
  top: -50%;
  right: -10%;
  width: 400px;
  height: 400px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 50%;
  filter: blur(60px);
}

.hero-content {
  position: relative;
  z-index: 1;
  margin-bottom: 2rem;
}

.hero-content h1 {
  font-size: 3rem;
  font-weight: 800;
  color: white;
  margin: 0 0 0.75rem 0;
  display: flex;
  align-items: center;
  gap: 1rem;
}

.icon-animate {
  display: inline-block;
  animation: bounce 2s infinite;
}

@keyframes bounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}

.hero-content p {
  font-size: 1.25rem;
  color: rgba(255, 255, 255, 0.9);
  margin: 0;
}

/* Search Container */
.search-container {
  position: relative;
  z-index: 1;
  margin-bottom: 1.5rem;
}

.search-box {
  display: flex;
  align-items: center;
  background: white;
  border-radius: 16px;
  padding: 0 1.5rem;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
  transition: all 0.3s;
}

.search-box:focus-within {
  transform: translateY(-2px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.2);
}

.search-box svg {
  width: 24px;
  height: 24px;
  color: #667eea;
  margin-right: 1rem;
}

.search-box input {
  flex: 1;
  padding: 1.25rem 0;
  border: none;
  font-size: 1.125rem;
  outline: none;
}

.search-box input::placeholder {
  color: #a0aec0;
}

.clear-btn {
  background: none;
  border: none;
  padding: 0.5rem;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
  transition: all 0.2s;
}

.clear-btn:hover {
  background: #f7fafc;
}

.clear-btn svg {
  width: 20px;
  height: 20px;
  color: #718096;
  margin: 0;
}

/* Catalog Stats */
.catalog-stats {
  display: flex;
  gap: 2rem;
  position: relative;
  z-index: 1;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 0.75rem 1.5rem;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 12px;
  color: white;
  font-weight: 600;
  backdrop-filter: blur(10px);
}

.stat-item svg {
  width: 20px;
  height: 20px;
}

/* Loading State */
.loading-container {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 6rem 2rem;
}

.loader-wrapper {
  text-align: center;
}

.loader {
  width: 60px;
  height: 60px;
  border: 4px solid #e2e8f0;
  border-top-color: #667eea;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 1.5rem;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.loader-wrapper p {
  font-size: 1.125rem;
  color: #718096;
  font-weight: 600;
}

/* Error State */
.error-container {
  background: white;
  border-radius: 20px;
  padding: 3rem;
  text-align: center;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.error-container svg {
  width: 60px;
  height: 60px;
  color: #fc8181;
  margin-bottom: 1.5rem;
}

.error-container h3 {
  font-size: 1.75rem;
  color: #2d3748;
  margin: 0 0 0.75rem 0;
}

.error-container p {
  color: #718096;
  font-size: 1.125rem;
  margin-bottom: 2rem;
}

.retry-btn {
  display: inline-flex;
  align-items: center;
  gap: 0.75rem;
  padding: 1rem 2rem;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 12px;
  font-weight: 600;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
}

.retry-btn svg {
  width: 20px;
  height: 20px;
}

.retry-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 24px rgba(102, 126, 234, 0.4);
}

/* Books Grid */
.books-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 2rem;
}

/* Book List Animations */
.book-list-enter-active {
  transition: all 0.5s ease;
}

.book-list-leave-active {
  transition: all 0.3s ease;
}

.book-list-enter-from {
  opacity: 0;
  transform: translateY(30px);
}

.book-list-leave-to {
  opacity: 0;
  transform: scale(0.9);
}

.book-list-move {
  transition: transform 0.5s ease;
}

/* Empty State */
.empty-state {
  background: white;
  border-radius: 20px;
  padding: 4rem 2rem;
  text-align: center;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.empty-icon {
  width: 120px;
  height: 120px;
  margin: 0 auto 2rem;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8px 24px rgba(102, 126, 234, 0.3);
}

.empty-icon svg {
  width: 60px;
  height: 60px;
  color: white;
}

.empty-state h3 {
  font-size: 2rem;
  color: #2d3748;
  margin: 0 0 1rem 0;
  font-weight: 800;
}

.empty-state p {
  font-size: 1.125rem;
  color: #718096;
  margin-bottom: 2rem;
}

.btn-secondary {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  padding: 1rem 2rem;
  background: #edf2f7;
  color: #4a5568;
  border: 2px solid #e2e8f0;
  border-radius: 12px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-secondary:hover {
  background: #e2e8f0;
  border-color: #cbd5e0;
  transform: translateY(-2px);
}

/* Responsive */
@media (max-width: 768px) {
  .hero-content h1 {
    font-size: 2rem;
  }

  .hero-content p {
    font-size: 1rem;
  }

  .catalog-stats {
    flex-direction: column;
    gap: 0.75rem;
  }

  .books-grid {
    grid-template-columns: 1fr;
  }
}
</style>
