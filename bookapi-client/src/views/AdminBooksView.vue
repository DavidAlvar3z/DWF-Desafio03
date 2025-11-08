<template>
  <div class="admin-books-view">
    <NavBar />
    
    <div class="container">
      <!-- Header Section -->
      <div class="header-section">
        <div class="title-area">
          <h1>
            <svg viewBox="0 0 20 20" fill="currentColor">
              <path d="M9 4.804A7.968 7.968 0 005.5 4c-1.255 0-2.443.29-3.5.804v10A7.969 7.969 0 015.5 14c1.669 0 3.218.51 4.5 1.385A7.962 7.962 0 0114.5 14c1.255 0 2.443.29 3.5.804v-10A7.968 7.968 0 0014.5 4c-1.255 0-2.443.29-3.5.804V12a1 1 0 11-2 0V4.804z" />
            </svg>
            Gestión de Libros
          </h1>
          <p class="subtitle">Administra el catálogo completo de tu biblioteca</p>
        </div>

        <button @click="openCreateModal" class="btn-create">
          <svg viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm1-11a1 1 0 10-2 0v2H7a1 1 0 100 2h2v2a1 1 0 102 0v-2h2a1 1 0 100-2h-2V7z" clip-rule="evenodd" />
          </svg>
          Nuevo Libro
        </button>
      </div>

      <!-- Stats Cards -->
      <div v-if="!loading" class="stats-grid">
        <div class="stat-card total">
          <div class="stat-icon">
            <svg viewBox="0 0 20 20" fill="currentColor">
              <path d="M9 4.804A7.968 7.968 0 005.5 4c-1.255 0-2.443.29-3.5.804v10A7.969 7.969 0 015.5 14c1.669 0 3.218.51 4.5 1.385A7.962 7.962 0 0114.5 14c1.255 0 2.443.29 3.5.804v-10A7.968 7.968 0 0014.5 4c-1.255 0-2.443.29-3.5.804V12a1 1 0 11-2 0V4.804z" />
            </svg>
          </div>
          <div class="stat-content">
            <h3>{{ books.length }}</h3>
            <p>Total Libros</p>
          </div>
        </div>

        <div class="stat-card available">
          <div class="stat-icon">
            <svg viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />
            </svg>
          </div>
          <div class="stat-content">
            <h3>{{ availableBooks }}</h3>
            <p>Disponibles</p>
          </div>
        </div>

        <div class="stat-card genres">
          <div class="stat-icon">
            <svg viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M17.707 9.293a1 1 0 010 1.414l-7 7a1 1 0 01-1.414 0l-7-7A.997.997 0 012 10V5a3 3 0 013-3h5c.256 0 .512.098.707.293l7 7zM5 6a1 1 0 100-2 1 1 0 000 2z" clip-rule="evenodd" />
            </svg>
          </div>
          <div class="stat-content">
            <h3>{{ uniqueGenres }}</h3>
            <p>Géneros</p>
          </div>
        </div>
      </div>

      <!-- Search & Filters -->
      <div class="filters-section">
        <div class="search-box">
          <svg viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z" clip-rule="evenodd" />
          </svg>
          <input 
            v-model="searchQuery" 
            type="text" 
            placeholder="Buscar por título, autor o ISBN..." 
            @input="handleSearch"
          />
        </div>

        <select v-model="filterGenre" @change="handleFilter" class="filter-select">
          <option value="">Todos los géneros</option>
          <option v-for="genre in genres" :key="genre" :value="genre">{{ genre }}</option>
        </select>

        <select v-model="filterAvailability" @change="handleFilter" class="filter-select">
          <option value="">Todos</option>
          <option value="true">Disponibles</option>
          <option value="false">No disponibles</option>
        </select>
      </div>

      <!-- Loading State -->
      <div v-if="loading" class="loading-state">
        <div class="loader-books">
          <div class="book-loading"></div>
          <div class="book-loading"></div>
          <div class="book-loading"></div>
        </div>
        <p>Cargando libros...</p>
      </div>

      <!-- Empty State -->
      <div v-else-if="!loading && filteredBooks.length === 0" class="empty-state">
        <div class="empty-icon">📚</div>
        <h3>No se encontraron libros</h3>
        <p>{{ searchQuery ? 'Intenta con otros términos de búsqueda' : 'Comienza agregando tu primer libro' }}</p>
        <button @click="openCreateModal" class="btn-add-first">
          <svg viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm1-11a1 1 0 10-2 0v2H7a1 1 0 100 2h2v2a1 1 0 102 0v-2h2a1 1 0 100-2h-2V7z" clip-rule="evenodd" />
          </svg>
          Agregar Primer Libro
        </button>
      </div>

      <!-- Books Grid -->
      <div v-else class="books-grid">
        <div v-for="book in filteredBooks" :key="book.id" class="book-card">
          <div class="book-card-header">
            <div class="book-cover-mini">
              <div class="book-icon">📖</div>
              <div class="book-title-mini">{{ book.title }}</div>
            </div>
            <span :class="['availability-badge', book.isAvailable ? 'available' : 'unavailable']">
              <span class="pulse-dot"></span>
              {{ book.isAvailable ? 'Disponible' : 'No disponible' }}
            </span>
          </div>

          <div class="book-card-body">
            <h3 class="book-title">{{ book.title }}</h3>
            <p class="book-author">
              <svg viewBox="0 0 20 20" fill="currentColor">
                <path d="M9 6a3 3 0 11-6 0 3 3 0 016 0zM17 6a3 3 0 11-6 0 3 3 0 016 0zM12.93 17c.046-.327.07-.66.07-1a6.97 6.97 0 00-1.5-4.33A5 5 0 0119 16v1h-6.07zM6 11a5 5 0 015 5v1H1v-1a5 5 0 015-5z" />
              </svg>
              {{ book.author }}
            </p>

            <div class="book-meta">
              <div class="meta-item">
                <svg viewBox="0 0 20 20" fill="currentColor">
                  <path fill-rule="evenodd" d="M17.707 9.293a1 1 0 010 1.414l-7 7a1 1 0 01-1.414 0l-7-7A.997.997 0 012 10V5a3 3 0 013-3h5c.256 0 .512.098.707.293l7 7zM5 6a1 1 0 100-2 1 1 0 000 2z" clip-rule="evenodd" />
                </svg>
                <span>{{ book.genre }}</span>
              </div>
              <div class="meta-item">
                <svg viewBox="0 0 20 20" fill="currentColor">
                  <path fill-rule="evenodd" d="M6 2a1 1 0 00-1 1v1H4a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V6a2 2 0 00-2-2h-1V3a1 1 0 10-2 0v1H7V3a1 1 0 00-1-1zm0 5a1 1 0 000 2h8a1 1 0 100-2H6z" clip-rule="evenodd" />
                </svg>
                <span>{{ book.publicationYear }}</span>
              </div>
              <div class="meta-item">
                <svg viewBox="0 0 20 20" fill="currentColor">
                  <path d="M9 4.804A7.968 7.968 0 005.5 4c-1.255 0-2.443.29-3.5.804v10A7.969 7.969 0 015.5 14c1.669 0 3.218.51 4.5 1.385A7.962 7.962 0 0114.5 14c1.255 0 2.443.29 3.5.804v-10A7.968 7.968 0 0014.5 4c-1.255 0-2.443.29-3.5.804V12a1 1 0 11-2 0V4.804z" />
                </svg>
                <span>{{ book.pageCount }} páginas</span>
              </div>
            </div>

            <p class="book-description">{{ truncateText(book.description, 100) }}</p>
            
            <div class="book-isbn">
              <code>ISBN: {{ book.isbn }}</code>
            </div>
          </div>

          <div class="book-card-actions">
            <button @click="openEditModal(book)" class="btn-action btn-edit">
              <svg viewBox="0 0 20 20" fill="currentColor">
                <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z" />
              </svg>
              Editar
            </button>
            <button @click="toggleAvailability(book)" class="btn-action btn-toggle">
              <svg viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />
              </svg>
              {{ book.isAvailable ? 'Deshabilitar' : 'Habilitar' }}
            </button>
            <button @click="handleDelete(book)" class="btn-action btn-delete">
              <svg viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z" clip-rule="evenodd" />
              </svg>
              Eliminar
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Create/Edit Modal -->
    <div v-if="showModal" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h2>
            <svg viewBox="0 0 20 20" fill="currentColor">
              <path d="M9 4.804A7.968 7.968 0 005.5 4c-1.255 0-2.443.29-3.5.804v10A7.969 7.969 0 015.5 14c1.669 0 3.218.51 4.5 1.385A7.962 7.962 0 0114.5 14c1.255 0 2.443.29 3.5.804v-10A7.968 7.968 0 0014.5 4c-1.255 0-2.443.29-3.5.804V12a1 1 0 11-2 0V4.804z" />
            </svg>
            {{ isEditing ? 'Editar Libro' : 'Nuevo Libro' }}
          </h2>
          <button @click="closeModal" class="btn-close">✕</button>
        </div>

        <form @submit.prevent="handleSubmit" class="book-form">
          <div class="form-row">
            <div class="form-group">
              <label>Título *</label>
              <input v-model="bookForm.title" type="text" required />
            </div>

            <div class="form-group">
              <label>Autor *</label>
              <input v-model="bookForm.author" type="text" required />
            </div>
          </div>

          <div class="form-row">
            <div class="form-group">
              <label>ISBN *</label>
              <input v-model="bookForm.isbn" type="text" required />
            </div>

            <div class="form-group">
              <label>Género *</label>
              <input v-model="bookForm.genre" type="text" required />
            </div>
          </div>

          <div class="form-row">
            <div class="form-group">
              <label>Año de Publicación *</label>
              <input v-model.number="bookForm.publicationYear" type="number" required min="1000" max="2025" />
            </div>

            <div class="form-group">
              <label>Páginas *</label>
              <input v-model.number="bookForm.pageCount" type="number" required min="1" />
            </div>
          </div>

          <div class="form-group">
            <label>Descripción *</label>
            <textarea v-model="bookForm.description" rows="4" required></textarea>
          </div>

          <div class="form-group">
            <label class="checkbox-label">
              <input type="checkbox" v-model="bookForm.isAvailable" />
              <span>Libro disponible para suscripción</span>
            </label>
          </div>

          <div class="form-actions">
            <button type="button" @click="closeModal" class="btn-cancel">
              Cancelar
            </button>
            <button type="submit" class="btn-save" :disabled="saving">
              <svg v-if="!saving" viewBox="0 0 20 20" fill="currentColor">
                <path d="M7.707 10.293a1 1 0 10-1.414 1.414l3 3a1 1 0 001.414 0l3-3a1 1 0 00-1.414-1.414L11 11.586V6h5a2 2 0 012 2v7a2 2 0 01-2 2H4a2 2 0 01-2-2V8a2 2 0 012-2h5v5.586l-1.293-1.293zM9 4a1 1 0 012 0v2H9V4z" />
              </svg>
              <div v-else class="spinner-small"></div>
              {{ saving ? 'Guardando...' : (isEditing ? 'Actualizar' : 'Crear Libro') }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import NavBar from '../components/NavBar.vue';
import { bookService } from '../services/bookService';

interface Book {
  id?: number;
  title: string;
  author: string;
  isbn: string;
  genre: string;
  publicationYear: number;
  pageCount: number;
  description: string;
  isAvailable: boolean;
}

const books = ref<Book[]>([]);
const loading = ref(false);
const saving = ref(false);
const showModal = ref(false);
const isEditing = ref(false);
const searchQuery = ref('');
const filterGenre = ref('');
const filterAvailability = ref('');

const bookForm = ref<Book>({
  title: '',
  author: '',
  isbn: '',
  genre: '',
  publicationYear: new Date().getFullYear(),
  pageCount: 0,
  description: '',
  isAvailable: true
});

const availableBooks = computed(() => books.value.filter(b => b.isAvailable).length);
const uniqueGenres = computed(() => new Set(books.value.map(b => b.genre)).size);
const genres = computed(() => Array.from(new Set(books.value.map(b => b.genre))).sort());

const filteredBooks = computed(() => {
  let result = books.value;

  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase();
    result = result.filter(book =>
      book.title.toLowerCase().includes(query) ||
      book.author.toLowerCase().includes(query) ||
      book.isbn.toLowerCase().includes(query)
    );
  }

  if (filterGenre.value) {
    result = result.filter(book => book.genre === filterGenre.value);
  }

  if (filterAvailability.value) {
    const isAvailable = filterAvailability.value === 'true';
    result = result.filter(book => book.isAvailable === isAvailable);
  }

  return result;
});

const loadBooks = async () => {
  loading.value = true;
  try {
    const response = await bookService.getAll();
    if (response.content && Array.isArray(response.content)) {
      books.value = response.content;
    } else if (Array.isArray(response)) {
      books.value = response;
    }
  } catch (error) {
    console.error('Error loading books:', error);
    alert('Error al cargar los libros');
  } finally {
    loading.value = false;
  }
};

const openCreateModal = () => {
  isEditing.value = false;
  bookForm.value = {
    title: '',
    author: '',
    isbn: '',
    genre: '',
    publicationYear: new Date().getFullYear(),
    pageCount: 0,
    description: '',
    isAvailable: true
  };
  showModal.value = true;
};

const openEditModal = (book: Book) => {
  isEditing.value = true;
  bookForm.value = { ...book };
  showModal.value = true;
};

const closeModal = () => {
  showModal.value = false;
  isEditing.value = false;
};

const handleSubmit = async () => {
  saving.value = true;
  try {
    if (isEditing.value && bookForm.value.id) {
      await bookService.update(bookForm.value.id, bookForm.value);
      alert('✅ Libro actualizado exitosamente');
    } else {
      await bookService.create(bookForm.value);
      alert('✅ Libro creado exitosamente');
    }
    closeModal();
    await loadBooks();
  } catch (error: any) {
    console.error('Error saving book:', error);
    alert(error.response?.data?.message || 'Error al guardar el libro');
  } finally {
    saving.value = false;
  }
};

const toggleAvailability = async (book: Book) => {
  if (!book.id) return;
  try {
    await bookService.update(book.id, { ...book, isAvailable: !book.isAvailable });
    alert(`✅ Libro ${!book.isAvailable ? 'habilitado' : 'deshabilitado'} exitosamente`);
    await loadBooks();
  } catch (error) {
    console.error('Error toggling availability:', error);
    alert('Error al cambiar la disponibilidad');
  }
};

const handleDelete = async (book: Book) => {
  if (!book.id || !confirm(`¿Estás seguro de eliminar "${book.title}"? Esta acción no se puede deshacer.`)) return;
  try {
    await bookService.delete(book.id);
    alert('✅ Libro eliminado exitosamente');
    await loadBooks();
  } catch (error) {
    console.error('Error deleting book:', error);
    alert('Error al eliminar el libro');
  }
};

const handleSearch = () => {
  // Reactive filtering
};

const handleFilter = () => {
  // Reactive filtering
};

const truncateText = (text: string, maxLength: number) => {
  if (!text) return '';
  return text.length > maxLength ? text.substring(0, maxLength) + '...' : text;
};

onMounted(() => {
  loadBooks();
});
</script>

<style scoped>
.admin-books-view {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #e8ecf1 100%);
}

.container {
  max-width: 1600px;
  margin: 0 auto;
  padding: 2rem;
}

/* Header Section */
.header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  flex-wrap: wrap;
  gap: 1rem;
}

.title-area h1 {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin: 0 0 0.5rem 0;
  color: #2d3748;
  font-size: 2.5rem;
  font-weight: 900;
}

.title-area h1 svg {
  width: 40px;
  height: 40px;
  color: #667eea;
}

.subtitle {
  margin: 0;
  color: #718096;
  font-size: 1rem;
}

.btn-create {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 1rem 2rem;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 1rem;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
}

.btn-create svg {
  width: 20px;
  height: 20px;
}

.btn-create:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.5);
}

/* Stats Grid */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.stat-card {
  background: white;
  border-radius: 16px;
  padding: 1.5rem;
  display: flex;
  align-items: center;
  gap: 1.5rem;
  box-shadow: 0 2px 10px rgba(0,0,0,0.08);
  transition: transform 0.2s;
}

.stat-card:hover {
  transform: translateY(-5px);
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.stat-icon svg {
  width: 30px;
  height: 30px;
}

.stat-card.total .stat-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-card.available .stat-icon {
  background: linear-gradient(135deg, #48bb78 0%, #38a169 100%);
}

.stat-card.genres .stat-icon {
  background: linear-gradient(135deg, #f6ad55 0%, #ed8936 100%);
}

.stat-content h3 {
  margin: 0;
  font-size: 2rem;
  font-weight: 900;
  color: #2d3748;
}

.stat-content p {
  margin: 0.25rem 0 0 0;
  color: #718096;
  font-size: 0.875rem;
}

/* Filters Section */
.filters-section {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr;
  gap: 1rem;
  margin-bottom: 2rem;
}

.search-box {
  display: flex;
  align-items: center;
  gap: 1rem;
  background: white;
  padding: 1rem 1.5rem;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.08);
}

.search-box svg {
  width: 20px;
  height: 20px;
  color: #718096;
}

.search-box input {
  flex: 1;
  border: none;
  outline: none;
  font-size: 1rem;
  color: #2d3748;
}

.filter-select {
  padding: 1rem 1.5rem;
  border: none;
  border-radius: 12px;
  background: white;
  font-size: 1rem;
  font-weight: 500;
  color: #2d3748;
  cursor: pointer;
  box-shadow: 0 2px 10px rgba(0,0,0,0.08);
}

/* Loading State */
.loading-state {
  text-align: center;
  padding: 4rem 2rem;
}

.loader-books {
  display: flex;
  justify-content: center;
  gap: 1rem;
  margin-bottom: 2rem;
}

.book-loading {
  width: 50px;
  height: 70px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 4px;
  animation: bookBounce 1s ease-in-out infinite;
}

.book-loading:nth-child(2) {
  animation-delay: 0.2s;
}

.book-loading:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes bookBounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-20px); }
}

/* Empty State */
.empty-state {
  text-align: center;
  padding: 4rem 2rem;
  background: white;
  border-radius: 20px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.1);
}

.empty-icon {
  font-size: 5rem;
  margin-bottom: 1rem;
}

.empty-state h3 {
  margin: 0 0 0.5rem 0;
  color: #2d3748;
  font-size: 1.5rem;
}

.empty-state p {
  color: #718096;
  margin-bottom: 2rem;
}

.btn-add-first {
  display: inline-flex;
  align-items: center;
  gap: 0.75rem;
  padding: 1rem 2rem;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 12px;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-add-first svg {
  width: 20px;
  height: 20px;
}

/* Books Grid */
.books-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 2rem;
}

.book-card {
  background: white;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0,0,0,0.1);
  transition: all 0.3s;
  display: flex;
  flex-direction: column;
}

.book-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 8px 30px rgba(0,0,0,0.15);
}

.book-card-header {
  position: relative;
  height: 180px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.book-cover-mini {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
  color: white;
}

.book-icon {
  font-size: 3rem;
}

.book-title-mini {
  font-size: 1.125rem;
  font-weight: 700;
  text-align: center;
  line-height: 1.3;
}

.availability-badge {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.75rem;
  font-weight: 600;
  align-self: flex-start;
}

.availability-badge.available {
  background: rgba(72, 187, 120, 0.2);
  border: 2px solid #48bb78;
  color: #48bb78;
}

.availability-badge.unavailable {
  background: rgba(245, 101, 101, 0.2);
  border: 2px solid #f56565;
  color: #f56565;
}

.pulse-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  animation: pulse 2s infinite;
}

.availability-badge.available .pulse-dot {
  background: #48bb78;
}

.availability-badge.unavailable .pulse-dot {
  background: #f56565;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

.book-card-body {
  padding: 1.5rem;
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.book-title {
  margin: 0;
  font-size: 1.25rem;
  font-weight: 800;
  color: #2d3748;
  line-height: 1.3;
}

.book-author {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin: 0;
  color: #718096;
  font-size: 0.875rem;
}

.book-author svg {
  width: 16px;
  height: 16px;
}

.book-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.875rem;
  color: #718096;
}

.meta-item svg {
  width: 16px;
  height: 16px;
  color: #667eea;
}

.book-description {
  color: #4a5568;
  font-size: 0.875rem;
  line-height: 1.6;
  margin: 0;
}

.book-isbn {
  margin-top: auto;
}

.book-isbn code {
  background: #f7fafc;
  padding: 0.5rem 1rem;
  border-radius: 8px;
  font-size: 0.75rem;
  color: #4a5568;
  font-family: 'Courier New', monospace;
}

.book-card-actions {
  display: flex;
  gap: 0.5rem;
  padding: 1rem 1.5rem;
  border-top: 1px solid #e2e8f0;
}

.btn-action {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  padding: 0.75rem;
  border: none;
  border-radius: 8px;
  font-size: 0.875rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-action svg {
  width: 16px;
  height: 16px;
}

.btn-edit {
  background: rgba(139, 92, 246, 0.1);
  color: #8b5cf6;
}

.btn-edit:hover {
  background: #8b5cf6;
  color: white;
}

.btn-toggle {
  background: rgba(16, 185, 129, 0.1);
  color: #10b981;
}

.btn-toggle:hover {
  background: #10b981;
  color: white;
}

.btn-delete {
  background: rgba(239, 68, 68, 0.1);
  color: #ef4444;
}

.btn-delete:hover {
  background: #ef4444;
  color: white;
}

/* Modal */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  animation: fadeIn 0.3s;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

.modal-content {
  background: white;
  border-radius: 20px;
  padding: 2rem;
  max-width: 700px;
  width: 90%;
  max-height: 90vh;
  overflow-y: auto;
  animation: slideUp 0.3s;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  padding-bottom: 1rem;
  border-bottom: 2px solid #e2e8f0;
}

.modal-header h2 {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin: 0;
  color: #2d3748;
  font-size: 1.5rem;
}

.modal-header h2 svg {
  width: 28px;
  height: 28px;
  color: #667eea;
}

.btn-close {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: #718096;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
  transition: all 0.2s;
}

.btn-close:hover {
  background: #f7fafc;
  color: #2d3748;
}

/* Form */
.book-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-group label {
  font-weight: 600;
  color: #2d3748;
  font-size: 0.875rem;
}

.form-group input,
.form-group textarea {
  padding: 0.875rem 1rem;
  border: 2px solid #e2e8f0;
  border-radius: 8px;
  font-size: 1rem;
  transition: all 0.3s;
}

.form-group input:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.1);
}

.checkbox-label {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  cursor: pointer;
  font-size: 0.875rem;
  color: #4a5568;
}

.checkbox-label input[type="checkbox"] {
  width: 20px;
  height: 20px;
  cursor: pointer;
}

.form-actions {
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
  margin-top: 1rem;
}

.btn-cancel {
  padding: 0.875rem 1.5rem;
  background: #e2e8f0;
  color: #4a5568;
  border: none;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-cancel:hover {
  background: #cbd5e0;
}

.btn-save {
  padding: 0.875rem 1.5rem;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.btn-save svg {
  width: 18px;
  height: 18px;
}

.btn-save:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.btn-save:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.spinner-small {
  width: 16px;
  height: 16px;
  border: 2px solid rgba(255,255,255,0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 0.6s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* Responsive */
@media (max-width: 1024px) {
  .books-grid {
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  }
}

@media (max-width: 768px) {
  .filters-section {
    grid-template-columns: 1fr;
  }

  .form-row {
    grid-template-columns: 1fr;
  }

  .books-grid {
    grid-template-columns: 1fr;
  }
}
</style>
