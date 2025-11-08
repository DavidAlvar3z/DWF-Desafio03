<template>
  <div class="book-detail-view">
    <NavBar />
    
    <div class="animated-background">
      <div class="gradient-orb orb-1"></div>
      <div class="gradient-orb orb-2"></div>
      <div class="gradient-orb orb-3"></div>
      <div class="floating-shapes">
        <div class="shape shape-1">📚</div>
        <div class="shape shape-2">✨</div>
        <div class="shape shape-3">📖</div>
        <div class="shape shape-4">💫</div>
      </div>
    </div>

    <div class="container">
      <button @click="router.back()" class="btn-back">
        <div class="btn-back-icon">
          <svg viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M9.707 16.707a1 1 0 01-1.414 0l-6-6a1 1 0 010-1.414l6-6a1 1 0 011.414 1.414L5.414 9H17a1 1 0 110 2H5.414l4.293 4.293a1 1 0 010 1.414z" clip-rule="evenodd" />
          </svg>
        </div>
        <span>Volver al catálogo</span>
      </button>

      <div v-if="loading" class="loading-container">
        <div class="loading-spinner"></div>
        <p>Cargando detalles del libro...</p>
      </div>

      <div v-else-if="error" class="error-container">
        <div class="error-icon">
          <svg viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z" clip-rule="evenodd" />
          </svg>
        </div>
        <h3>Error al cargar el libro</h3>
        <p>{{ error }}</p>
        <button @click="fetchBook" class="btn-retry">Reintentar</button>
      </div>

      <div v-else-if="book" class="book-detail-content">
        <div class="book-hero">
          <div class="book-cover-section">
            <div class="book-cover-wrapper">
              <div class="book-cover-3d">
                <div class="book-cover-front">
                  <div class="book-cover-overlay"></div>
                  <div class="book-cover-title">{{ book.title }}</div>
                  <div class="book-cover-author">{{ book.author }}</div>
                </div>
                <div class="book-spine"></div>
              </div>
            </div>
            <div class="book-badges">
              <span class="badge badge-genre">{{ book.genre }}</span>
              <span v-if="book.isAvailable" class="badge badge-available">
                <svg viewBox="0 0 20 20" fill="currentColor">
                  <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />
                </svg>
                Disponible
              </span>
              <span v-else class="badge badge-unavailable">
                <svg viewBox="0 0 20 20" fill="currentColor">
                  <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clip-rule="evenodd" />
                </svg>
                No disponible
              </span>
            </div>
          </div>

          <div class="book-info-section">
            <div class="book-header">
              <h1 class="book-title">{{ book.title }}</h1>
              <p class="book-author">por {{ book.author }}</p>
            </div>

            <div class="book-meta">
              <div class="meta-item">
                <div class="meta-icon">
                  <svg viewBox="0 0 20 20" fill="currentColor">
                    <path fill-rule="evenodd" d="M6 2a1 1 0 00-1 1v1H4a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V6a2 2 0 00-2-2h-1V3a1 1 0 10-2 0v1H7V3a1 1 0 00-1-1zm0 5a1 1 0 000 2h8a1 1 0 100-2H6z" clip-rule="evenodd" />
                  </svg>
                </div>
                <div class="meta-content">
                  <div class="meta-label">Año de publicación</div>
                  <div class="meta-value">{{ book.publicationYear }}</div>
                </div>
              </div>

              <div class="meta-item">
                <div class="meta-icon">
                  <svg viewBox="0 0 20 20" fill="currentColor">
                    <path d="M9 4.804A7.968 7.968 0 005.5 4c-1.255 0-2.443.29-3.5.804v10A7.969 7.969 0 015.5 14c1.669 0 3.218.51 4.5 1.385A7.962 7.962 0 0114.5 14c1.255 0 2.443.29 3.5.804v-10A7.968 7.968 0 0014.5 4c-1.255 0-2.443.29-3.5.804V12a1 1 0 11-2 0V4.804z" />
                  </svg>
                </div>
                <div class="meta-content">
                  <div class="meta-label">Páginas</div>
                  <div class="meta-value">{{ book.pageCount }}</div>
                </div>
              </div>

              <div class="meta-item">
                <div class="meta-icon">
                  <svg viewBox="0 0 20 20" fill="currentColor">
                    <path fill-rule="evenodd" d="M10 2a1 1 0 011 1v1.323l3.954 1.582 1.599-.8a1 1 0 01.894 1.79l-1.233.616 1.738 5.42a1 1 0 01-.285 1.05A3.989 3.989 0 0115 15a3.989 3.989 0 01-2.667-1.019 1 1 0 01-.285-1.05l1.715-5.349L11 6.477V16h2a1 1 0 110 2H7a1 1 0 110-2h2V6.477L6.237 7.582l1.715 5.349a1 1 0 01-.285 1.05A3.989 3.989 0 015 15a3.989 3.989 0 01-2.667-1.019 1 1 0 01-.285-1.05l1.738-5.42-1.233-.617a1 1 0 01.894-1.788l1.599.799L9 4.323V3a1 1 0 011-1z" clip-rule="evenodd" />
                  </svg>
                </div>
                <div class="meta-content">
                  <div class="meta-label">Género</div>
                  <div class="meta-value">{{ book.genre }}</div>
                </div>
              </div>

              <div class="meta-item">
                <div class="meta-icon">
                  <svg viewBox="0 0 20 20" fill="currentColor">
                    <path fill-rule="evenodd" d="M3 3a1 1 0 000 2v8a2 2 0 002 2h2.586l-1.293 1.293a1 1 0 101.414 1.414L10 15.414l2.293 2.293a1 1 0 001.414-1.414L12.414 15H15a2 2 0 002-2V5a1 1 0 100-2H3zm11.707 4.707a1 1 0 00-1.414-1.414L10 9.586 8.707 8.293a1 1 0 00-1.414 0l-2 2a1 1 0 101.414 1.414L8 10.414l1.293 1.293a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />
                  </svg>
                </div>
                <div class="meta-content">
                  <div class="meta-label">ISBN</div>
                  <div class="meta-value">{{ book.isbn }}</div>
                </div>
              </div>
            </div>

            <div class="book-description">
              <h3 class="description-title">Descripción</h3>
              <p class="description-text">{{ book.description || 'Sin descripción disponible.' }}</p>
            </div>
          </div>
        </div>

        <div class="subscription-section">
          <div class="section-header">
            <div class="section-icon">
              <svg viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M5 2a1 1 0 011 1v1h1a1 1 0 010 2H6v1a1 1 0 01-2 0V6H3a1 1 0 010-2h1V3a1 1 0 011-1zm0 10a1 1 0 011 1v1h1a1 1 0 110 2H6v1a1 1 0 11-2 0v-1H3a1 1 0 110-2h1v-1a1 1 0 011-1zM12 2a1 1 0 01.967.744L14.146 7.2 17.5 9.134a1 1 0 010 1.732l-3.354 1.935-1.18 4.455a1 1 0 01-1.933 0L9.854 12.8 6.5 10.866a1 1 0 010-1.732l3.354-1.935 1.18-4.455A1 1 0 0112 2z" clip-rule="evenodd" />
              </svg>
            </div>
            <div>
              <h2 class="section-title">Planes de Suscripción</h2>
              <p class="section-subtitle">Elige el plan perfecto para tu lectura</p>
            </div>
          </div>

          <div v-if="isAdmin" class="admin-premium-section">
            <div class="admin-glow"></div>
            <div class="admin-content">
              <div class="admin-icon-large">
                <svg viewBox="0 0 20 20" fill="currentColor">
                  <path fill-rule="evenodd" d="M11.49 3.17c-.38-1.56-2.6-1.56-2.98 0a1.532 1.532 0 01-2.286.948c-1.372-.836-2.942.734-2.106 2.106.54.886.061 2.042-.947 2.287-1.561.379-1.561 2.6 0 2.978a1.532 1.532 0 01.947 2.287c-.836 1.372.734 2.942 2.106 2.106a1.532 1.532 0 012.287.947c.379 1.561 2.6 1.561 2.978 0a1.533 1.533 0 012.287-.947c1.372.836 2.942-.734 2.106-2.106a1.533 1.533 0 01.947-2.287c1.561-.379 1.561-2.6 0-2.978a1.532 1.532 0 01-.947-2.287c.836-1.372-.734-2.942-2.106-2.106a1.532 1.532 0 01-2.287-.947zM10 13a3 3 0 100-6 3 3 0 000 6z" clip-rule="evenodd" />
                </svg>
              </div>
              <h3 class="admin-title">Administrator Preview Mode</h3>
              
              <div class="admin-info-box">
                <div class="info-icon-admin">
                  <svg viewBox="0 0 20 20" fill="currentColor">
                    <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z" clip-rule="evenodd" />
                  </svg>
                </div>
                <div class="info-text-admin">
                  <p class="info-title-admin">Testing & Preview Access</p>
                  <p class="info-desc-admin">
                    As an administrator, you have full access to view book details and subscription plans for testing purposes. 
                    This preview allows you to verify content availability, pricing, and user experience without creating actual subscriptions.
                  </p>
                </div>
              </div>

              <div class="admin-features">
                <div class="feature-item">
                  <svg viewBox="0 0 20 20" fill="currentColor">
                    <path d="M10 12a2 2 0 100-4 2 2 0 000 4z" />
                    <path fill-rule="evenodd" d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z" clip-rule="evenodd" />
                  </svg>
                  <span>Preview book availability status</span>
                </div>
                <div class="feature-item">
                  <svg viewBox="0 0 20 20" fill="currentColor">
                    <path fill-rule="evenodd" d="M6 2a1 1 0 00-1 1v1H4a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V6a2 2 0 00-2-2h-1V3a1 1 0 10-2 0v1H7V3a1 1 0 00-1-1zm0 5a1 1 0 000 2h8a1 1 0 100-2H6z" clip-rule="evenodd" />
                  </svg>
                  <span>Test subscription plans and pricing</span>
                </div>
                <div class="feature-item">
                  <svg viewBox="0 0 20 20" fill="currentColor">
                    <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />
                  </svg>
                  <span>Verify user interface and flow</span>
                </div>
              </div>
              
              <router-link to="/admin/books" class="btn-admin-premium">
                <span class="btn-admin-glow"></span>
                <span class="btn-admin-content">
                  <svg viewBox="0 0 20 20" fill="currentColor">
                    <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z" />
                  </svg>
                  Go to Book Management
                </span>
              </router-link>
            </div>
          </div>

          <div v-else-if="!book.isAvailable" class="unavailable-message">
            <div class="unavailable-icon">
              <svg viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M13.477 14.89A6 6 0 015.11 6.524l8.367 8.368zm1.414-1.414L6.524 5.11a6 6 0 018.367 8.367zM18 10a8 8 0 11-16 0 8 8 0 0116 0z" clip-rule="evenodd" />
              </svg>
            </div>
            <h3>Libro no disponible</h3>
            <p>Este libro no está disponible para suscripción en este momento. Por favor, revisa más tarde o explora otros títulos de nuestro catálogo.</p>
            <router-link to="/books" class="btn-explore">
              <svg viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M12.293 5.293a1 1 0 011.414 0l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414-1.414L14.586 11H3a1 1 0 110-2h11.586l-2.293-2.293a1 1 0 010-1.414z" clip-rule="evenodd" />
              </svg>
              Explorar catálogo
            </router-link>
          </div>

          <div v-else class="subscription-plans">
            <div v-for="plan in subscriptionPlans" :key="plan.type" class="plan-card" :class="{ 'plan-popular': plan.popular }">
              <div v-if="plan.popular" class="plan-popular-badge">
                <svg viewBox="0 0 20 20" fill="currentColor">
                  <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                </svg>
                Más popular
              </div>
              
              <div class="plan-header">
                <div class="plan-icon" :class="`plan-icon-${plan.type.toLowerCase()}`">
                  <svg v-if="plan.type === 'DAILY'" viewBox="0 0 20 20" fill="currentColor">
                    <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm1-12a1 1 0 10-2 0v4a1 1 0 00.293.707l2.828 2.829a1 1 0 101.415-1.415L11 9.586V6z" clip-rule="evenodd" />
                  </svg>
                  <svg v-else-if="plan.type === 'WEEKLY'" viewBox="0 0 20 20" fill="currentColor">
                    <path fill-rule="evenodd" d="M6 2a1 1 0 00-1 1v1H4a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V6a2 2 0 00-2-2h-1V3a1 1 0 10-2 0v1H7V3a1 1 0 00-1-1zm0 5a1 1 0 000 2h8a1 1 0 100-2H6z" clip-rule="evenodd" />
                  </svg>
                  <svg v-else-if="plan.type === 'MONTHLY'" viewBox="0 0 20 20" fill="currentColor">
                    <path d="M9 2a1 1 0 000 2h2a1 1 0 100-2H9z" />
                    <path fill-rule="evenodd" d="M4 5a2 2 0 012-2 3 3 0 003 3h2a3 3 0 003-3 2 2 0 012 2v11a2 2 0 01-2 2H6a2 2 0 01-2-2V5zm3 4a1 1 0 000 2h.01a1 1 0 100-2H7zm3 0a1 1 0 000 2h3a1 1 0 100-2h-3zm-3 4a1 1 0 100 2h.01a1 1 0 100-2H7zm3 0a1 1 0 100 2h3a1 1 0 100-2h-3z" clip-rule="evenodd" />
                  </svg>
                  <svg v-else viewBox="0 0 20 20" fill="currentColor">
                    <path fill-rule="evenodd" d="M6 2a1 1 0 00-1 1v1H4a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V6a2 2 0 00-2-2h-1V3a1 1 0 10-2 0v1H7V3a1 1 0 00-1-1zm0 5a1 1 0 000 2h8a1 1 0 100-2H6z" clip-rule="evenodd" />
                  </svg>
                </div>
                <h3 class="plan-name">{{ plan.name }}</h3>
                <div class="plan-price">
                  <span class="price-currency">$</span>
                  <span class="price-amount">{{ plan.price }}</span>
                  <span class="price-period">/{{ plan.period }}</span>
                </div>
              </div>

              <div class="plan-features">
                <div v-for="(feature, index) in plan.features" :key="index" class="feature">
                  <svg viewBox="0 0 20 20" fill="currentColor">
                    <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
                  </svg>
                  <span>{{ feature }}</span>
                </div>
              </div>

              <button @click="subscribe(plan)" class="btn-subscribe" :class="{ 'btn-subscribe-popular': plan.popular }">
                <span class="btn-subscribe-glow"></span>
                <span class="btn-subscribe-content">
                  <svg viewBox="0 0 20 20" fill="currentColor">
                    <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-8.707l-3-3a1 1 0 00-1.414 1.414L10.586 9H7a1 1 0 100 2h3.586l-1.293 1.293a1 1 0 101.414 1.414l3-3a1 1 0 000-1.414z" clip-rule="evenodd" />
                  </svg>
                  Suscribirse ahora
                </span>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import NavBar from '../components/NavBar.vue'
import { getBookById } from '../services/bookService'
import { authService } from '../services/authService'

const route = useRoute()
const router = useRouter()

const book = ref<any>(null)
const loading = ref(true)
const error = ref<string | null>(null)

const currentUser = computed(() => {
  return authService.getCurrentUser()
})

const isAdmin = computed(() => {
  return authService.isAdmin()
})

const subscriptionPlans = ref([
  {
    type: 'DAILY',
    name: 'Diario',
    price: '2.99',
    period: 'día',
    features: [
      'Acceso completo por 24 horas',
      'Lectura ilimitada',
      'Sin compromiso',
      'Soporte básico'
    ],
    popular: false
  },
  {
    type: 'WEEKLY',
    name: 'Semanal',
    price: '14.99',
    period: 'semana',
    features: [
      'Acceso completo por 7 días',
      'Lectura ilimitada',
      'Ahorra 50% vs diario',
      'Soporte prioritario'
    ],
    popular: true
  },
  {
    type: 'MONTHLY',
    name: 'Mensual',
    price: '29.99',
    period: 'mes',
    features: [
      'Acceso completo por 30 días',
      'Lectura ilimitada',
      'Mejor valor',
      'Soporte premium'
    ],
    popular: false
  },
  {
    type: 'YEARLY',
    name: 'Anual',
    price: '299.99',
    period: 'año',
    features: [
      'Acceso completo por 365 días',
      'Lectura ilimitada',
      'Ahorra 17% vs mensual',
      'Soporte VIP'
    ],
    popular: false
  }
])

const fetchBook = async () => {
  loading.value = true
  error.value = null
  
  try {
    const bookId = Number(route.params.id)
    book.value = await getBookById(bookId)
  } catch (err: any) {
    console.error('Error fetching book:', err)
    error.value = err.message || 'Error al cargar el libro'
  } finally {
    loading.value = false
  }
}

const subscribe = (plan: any) => {
  // Validar que el usuario esté autenticado
  if (!currentUser.value) {
    console.error('❌ Usuario no autenticado')
    alert('Debes iniciar sesión para suscribirte')
    router.push('/login')
    return
  }

  // Validar que el libro exista
  if (!book.value?.id) {
    console.error('❌ Libro no disponible')
    alert('Error: Libro no disponible')
    return
  }

  // Validar que el usuario tenga ID
  if (!currentUser.value.id) {
    console.error('❌ ID de usuario no disponible')
    alert('Error: Sesión inválida. Por favor, inicia sesión nuevamente')
    router.push('/login')
    return
  }

  console.log('✅ Iniciando suscripción:', {
    userId: currentUser.value.id,
    bookId: book.value.id,
    plan: plan.type
  })

  alert(`Suscripción ${plan.name} iniciada para "${book.value.title}"`)
}

onMounted(() => {
  fetchBook()
})
</script>

<style scoped>
.book-detail-view {
  min-height: 100vh;
  position: relative;
  background: #0a0e27;
  color: white;
  overflow-x: hidden;
}

/* Animated Background */
.animated-background {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 0;
  overflow: hidden;
}

.gradient-orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(100px);
  opacity: 0.3;
  animation: float 20s infinite ease-in-out;
}

.orb-1 {
  width: 600px;
  height: 600px;
  background: radial-gradient(circle, #667eea 0%, transparent 70%);
  top: -200px;
  left: -200px;
  animation-delay: 0s;
}

.orb-2 {
  width: 500px;
  height: 500px;
  background: radial-gradient(circle, #764ba2 0%, transparent 70%);
  bottom: -150px;
  right: -150px;
  animation-delay: -7s;
}

.orb-3 {
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, #f093fb 0%, transparent 70%);
  top: 50%;
  left: 50%;
  animation-delay: -14s;
}

@keyframes float {
  0%, 100% { transform: translate(0, 0) scale(1); }
  33% { transform: translate(50px, -80px) scale(1.1); }
  66% { transform: translate(-30px, 40px) scale(0.9); }
}

.floating-shapes {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.shape {
  position: absolute;
  font-size: 3rem;
  opacity: 0.1;
  animation: floatShape 15s infinite ease-in-out;
}

.shape-1 {
  top: 10%;
  left: 10%;
  animation-delay: 0s;
}

.shape-2 {
  top: 70%;
  right: 15%;
  animation-delay: -5s;
}

.shape-3 {
  bottom: 20%;
  left: 20%;
  animation-delay: -10s;
}

.shape-4 {
  top: 40%;
  right: 30%;
  animation-delay: -7s;
}

@keyframes floatShape {
  0%, 100% { transform: translateY(0) rotate(0deg); }
  50% { transform: translateY(-50px) rotate(180deg); }
}

.container {
  position: relative;
  z-index: 1;
  max-width: 1600px;
  margin: 0 auto;
  padding: 2rem;
}

/* Premium Back Button */
.btn-back {
  display: inline-flex;
  align-items: center;
  gap: 1rem;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(20px);
  border: 2px solid rgba(255, 255, 255, 0.2);
  padding: 1rem 2rem;
  border-radius: 50px;
  cursor: pointer;
  font-size: 1rem;
  font-weight: 600;
  color: white;
  margin-bottom: 3rem;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.btn-back::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left 0.5s;
}

.btn-back:hover::before {
  left: 100%;
}

.btn-back-icon {
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: transform 0.3s;
}

.btn-back:hover .btn-back-icon {
  transform: translateX(-5px);
}

.btn-back:hover {
  background: rgba(102, 126, 234, 0.2);
  border-color: #667eea;
  transform: translateY(-2px);
  box-shadow: 0 10px 40px rgba(102, 126, 234, 0.3);
}

/* Premium Loading */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 8rem 2rem;
  gap: 2rem;
}

.loader-premium {
  perspective: 1000px;
}

.book-loader {
  width: 80px;
  height: 100px;
  position: relative;
  transform-style: preserve-3d;
  animation: flipBook 2s infinite ease-in-out;
}

.page {
  position: absolute;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 4px;
  transform-origin: left;
}

.page:nth-child(1) {
  animation: turnPage1 2s infinite ease-in-out;
}

.page:nth-child(2) {
  animation: turnPage2 2s infinite ease-in-out;
  animation-delay: 0.2s;
}

.page:nth-child(3) {
  animation: turnPage3 2s infinite ease-in-out;
  animation-delay: 0.4s;
}

@keyframes flipBook {
  0%, 100% { transform: rotateY(0deg); }
  50% { transform: rotateY(30deg); }
}

@keyframes turnPage1 {
  0%, 40%, 100% { transform: rotateY(0deg); }
  70% { transform: rotateY(-180deg); }
}

@keyframes turnPage2 {
  0%, 40%, 100% { transform: rotateY(0deg); }
  70% { transform: rotateY(-180deg); }
}

@keyframes turnPage3 {
  0%, 40%, 100% { transform: rotateY(0deg); }
  70% { transform: rotateY(-180deg); }
}

.loading-text {
  font-size: 1.25rem;
  color: rgba(255, 255, 255, 0.8);
  animation: pulse 2s infinite;
}

/* Premium Error State */
.error-container {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(20px);
  border: 2px solid rgba(255, 255, 255, 0.1);
  border-radius: 30px;
  padding: 4rem;
  text-align: center;
  max-width: 600px;
  margin: 4rem auto;
}

.error-icon {
  width: 100px;
  height: 100px;
  margin: 0 auto 2rem;
  background: linear-gradient(135deg, #fc8181 0%, #f56565 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 20px 60px rgba(252, 129, 129, 0.4);
  animation: errorPulse 2s infinite;
}

.error-icon svg {
  width: 50px;
  height: 50px;
  color: white;
}

@keyframes errorPulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.05); }
}

.error-container h3 {
  font-size: 2rem;
  margin-bottom: 1rem;
  color: white;
}

.error-container p {
  font-size: 1.125rem;
  color: rgba(255, 255, 255, 0.7);
  margin-bottom: 2rem;
}

.btn-retry {
  display: inline-flex;
  align-items: center;
  gap: 0.75rem;
  padding: 1rem 2rem;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 50px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 10px 30px rgba(102, 126, 234, 0.4);
}

.btn-retry svg {
  width: 20px;
  height: 20px;
}

.btn-retry:hover {
  transform: translateY(-3px);
  box-shadow: 0 15px 40px rgba(102, 126, 234, 0.6);
}

/* Premium Book Detail Layout */
.book-detail-premium {
  display: grid;
  grid-template-columns: 550px 1fr;
  gap: 4rem;
  margin-top: 2rem;
}

/* 3D Book Showcase */
.book-showcase {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.book-3d-wrapper {
  perspective: 2000px;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 3rem;
}

.book-3d {
  width: 350px;
  height: 500px;
  position: relative;
  transform-style: preserve-3d;
  transform: rotateY(-15deg) rotateX(5deg);
  transition: all 0.8s cubic-bezier(0.4, 0, 0.2, 1);
  animation: bookFloat 6s infinite ease-in-out;
}

.book-3d:hover {
  transform: rotateY(0deg) rotateX(0deg) scale(1.05);
}

@keyframes bookFloat {
  0%, 100% { transform: rotateY(-15deg) rotateX(5deg) translateY(0); }
  50% { transform: rotateY(-15deg) rotateX(5deg) translateY(-20px); }
}

.book-spine {
  position: absolute;
  left: 0;
  top: 0;
  width: 50px;
  height: 100%;
  background: linear-gradient(90deg, 
    rgba(0,0,0,0.4), 
    rgba(0,0,0,0.2),
    transparent
  );
  transform: translateX(-50px) rotateY(-90deg);
  transform-origin: right;
}

.book-top {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 50px;
  background: linear-gradient(180deg, 
    rgba(255,255,255,0.2), 
    rgba(255,255,255,0.05)
  );
  transform: translateY(-50px) rotateX(90deg);
  transform-origin: bottom;
}

.book-front {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 10px;
  position: relative;
  overflow: hidden;
  box-shadow: 
    0 30px 80px rgba(0, 0, 0, 0.5),
    0 0 0 1px rgba(255, 255, 255, 0.1),
    inset 0 0 80px rgba(255, 255, 255, 0.1);
}

.book-cover-glow {
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(255,255,255,0.3) 0%, transparent 70%);
  animation: glowMove 8s infinite ease-in-out;
}

@keyframes glowMove {
  0%, 100% { transform: translate(0, 0); }
  50% { transform: translate(30%, 30%); }
}

.book-content {
  position: relative;
  z-index: 1;
  padding: 3rem;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
}

.book-icon-large {
  font-size: 100px;
  margin-bottom: 2rem;
  filter: drop-shadow(0 10px 20px rgba(0,0,0,0.3));
  animation: iconBounce 3s infinite ease-in-out;
}

@keyframes iconBounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}

.book-cover-title {
  font-size: 1.75rem;
  font-weight: 800;
  color: white;
  margin: 0 0 1rem 0;
  line-height: 1.3;
  text-shadow: 0 4px 10px rgba(0,0,0,0.3);
}

.book-cover-author {
  font-size: 1.125rem;
  color: rgba(255, 255, 255, 0.9);
  margin: 0 0 2rem 0;
  font-weight: 500;
}

.book-cover-decoration {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-top: auto;
}

.deco-line {
  width: 60px;
  height: 2px;
  background: linear-gradient(90deg, transparent, white, transparent);
}

.deco-star {
  font-size: 1.5rem;
  color: white;
  animation: starTwinkle 2s infinite ease-in-out;
}

@keyframes starTwinkle {
  0%, 100% { opacity: 1; transform: scale(1); }
  50% { opacity: 0.5; transform: scale(1.2); }
}

/* Info Cards */
.info-cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1rem;
}

.info-card {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 20px;
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
  transition: all 0.3s;
}

.info-card:hover {
  background: rgba(255, 255, 255, 0.1);
  border-color: rgba(102, 126, 234, 0.5);
  transform: translateY(-5px);
}

.info-icon {
  width: 50px;
  height: 50px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 15px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  box-shadow: 0 10px 30px rgba(102, 126, 234, 0.3);
}

.info-icon svg {
  width: 26px;
  height: 26px;
}

.info-content {
  text-align: center;
}

.info-value {
  display: block;
  font-size: 1.5rem;
  font-weight: 800;
  color: white;
  margin-bottom: 0.25rem;
}

.info-label {
  display: block;
  font-size: 0.875rem;
  color: rgba(255, 255, 255, 0.6);
}

/* Book Info Premium */
.book-info-premium {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

/* Availability Badge */
.availability-section {
  margin-bottom: 1rem;
}

.availability-badge {
  display: inline-flex;
  align-items: center;
  gap: 0.75rem;
  padding: 1rem 2rem;
  border-radius: 50px;
  font-size: 1rem;
  font-weight: 700;
  position: relative;
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

.pulse-ring {
  position: absolute;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  animation: pulseRing 2s infinite;
}

.availability-badge.available .pulse-ring {
  background: #48bb78;
}

.availability-badge.unavailable .pulse-ring {
  background: #f56565;
}

@keyframes pulseRing {
  0% {
    transform: scale(0.8);
    opacity: 1;
  }
  100% {
    transform: scale(1.5);
    opacity: 0;
  }
}

.pulse-dot {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  position: relative;
  z-index: 1;
}

.availability-badge.available .pulse-dot {
  background: #48bb78;
  box-shadow: 0 0 20px #48bb78;
}

.availability-badge.unavailable .pulse-dot {
  background: #f56565;
  box-shadow: 0 0 20px #f56565;
}

/* Title Section */
.title-section {
  margin-bottom: 2rem;
}

.book-title-main {
  font-size: 3.5rem;
  font-weight: 900;
  color: white;
  margin: 0 0 1rem 0;
  line-height: 1.1;
  position: relative;
  display: inline-block;
}

.title-underline {
  height: 6px;
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
  border-radius: 3px;
  margin-top: 0.5rem;
  animation: expandWidth 1s ease-out;
}

@keyframes expandWidth {
  from { width: 0; }
  to { width: 100%; }
}

.book-author-main {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  font-size: 1.5rem;
  font-weight: 400;
  color: rgba(255, 255, 255, 0.7);
  margin: 0;
}

.book-author-main svg {
  width: 24px;
  height: 24px;
}

.author-name {
  font-weight: 600;
  color: #667eea;
}

/* Premium Tags */
.tags-section {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  margin-bottom: 2rem;
}

.tag {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1.5rem;
  border-radius: 50px;
  font-size: 0.875rem;
  font-weight: 600;
  backdrop-filter: blur(20px);
}

.genre-tag-premium {
  background: rgba(102, 126, 234, 0.2);
  border: 2px solid #667eea;
  color: #667eea;
}

.isbn-tag-premium {
  background: rgba(246, 173, 85, 0.2);
  border: 2px solid #f6ad55;
  color: #f6ad55;
}

.tag svg {
  width: 18px;
  height: 18px;
}

/* Description Premium */
.description-premium {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 24px;
  padding: 2rem;
}

.section-header {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 1.5rem;
}

.section-icon {
  font-size: 2rem;
}

.section-header h3 {
  font-size: 1.75rem;
  font-weight: 700;
  color: white;
  margin: 0;
}

.description-content p {
  font-size: 1.125rem;
  line-height: 1.8;
  color: rgba(255, 255, 255, 0.8);
  margin: 0;
}

/* Subscription Premium Section */
.subscription-premium-section {
  background: rgba(255, 255, 255, 0.03);
  backdrop-filter: blur(20px);
  border: 2px solid rgba(255, 255, 255, 0.1);
  border-radius: 30px;
  padding: 3rem;
}

.plans-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.plan-premium {
  position: relative;
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(20px);
  border: 2px solid rgba(255, 255, 255, 0.1);
  border-radius: 24px;
  padding: 2rem;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.plan-premium:hover {
  background: rgba(255, 255, 255, 0.08);
  border-color: rgba(102, 126, 234, 0.5);
  transform: translateY(-5px);
  box-shadow: 0 20px 60px rgba(102, 126, 234, 0.3);
}

.plan-premium.selected {
  background: rgba(102, 126, 234, 0.15);
  border-color: #667eea;
  box-shadow: 0 20px 60px rgba(102, 126, 234, 0.4);
}

.plan-premium.popular {
  border-color: #f6ad55;
}

.popular-badge {
  position: absolute;
  top: -12px;
  right: 20px;
  background: linear-gradient(135deg, #f6ad55 0%, #ed8936 100%);
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.75rem;
  font-weight: 700;
  box-shadow: 0 4px 15px rgba(246, 173, 85, 0.4);
}

.plan-icon-premium {
  font-size: 3rem;
  margin-bottom: 1rem;
}

.plan-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: white;
  margin: 0 0 0.5rem 0;
}

.plan-description {
  font-size: 0.875rem;
  color: rgba(255, 255, 255, 0.6);
  margin: 0 0 1.5rem 0;
}

.plan-price-premium {
  display: flex;
  align-items: baseline;
  gap: 0.25rem;
  margin-bottom: 1.5rem;
}

.price-currency {
  font-size: 1.5rem;
  font-weight: 700;
  color: #667eea;
}

.price-amount {
  font-size: 3rem;
  font-weight: 900;
  color: #667eea;
  line-height: 1;
}

.price-period {
  font-size: 1rem;
  color: rgba(255, 255, 255, 0.6);
}

.plan-features {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  margin-bottom: 1.5rem;
}

.feature {
  display: flex;
  align-items: center;
  font-size: 0.875rem;
  color: rgba(255, 255, 255, 0.8);
}

.plan-selector {
  display: flex;
  justify-content: center;
  margin-top: auto;
}

.selector-ring {
  width: 32px;
  height: 32px;
  border: 3px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s;
}

.plan-premium.selected .selector-ring {
  border-color: #667eea;
  background: #667eea;
}

.selector-check {
  width: 20px;
  height: 20px;
  color: white;
}

.selector-check svg {
  width: 100%;
  height: 100%;
}

/* Payment Premium */
.payment-premium {
  margin-bottom: 2rem;
}

.payment-selector {
  display: flex;
  align-items: center;
  gap: 1rem;
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(20px);
  border: 2px solid rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  padding: 1rem 1.5rem;
  transition: all 0.3s;
}

.payment-selector:hover {
  border-color: rgba(102, 126, 234, 0.5);
  background: rgba(255, 255, 255, 0.08);
}

.payment-icon {
  width: 24px;
  height: 24px;
  color: #667eea;
}

.payment-select-premium {
  flex: 1;
  background: transparent;
  border: none;
  color: white;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  outline: none;
}

.payment-select-premium option {
  background: #1a1f3a;
  color: white;
}

/* Subscribe Button Premium */
.btn-subscribe-premium {
  width: 100%;
  padding: 1.75rem;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 50px;
  font-size: 1.25rem;
  font-weight: 700;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 10px 40px rgba(102, 126, 234, 0.5);
  margin-bottom: 2rem;
}

.btn-glow {
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(255,255,255,0.3) 0%, transparent 70%);
  animation: glowRotate 3s linear infinite;
}

@keyframes glowRotate {
  to { transform: rotate(360deg); }
}

.btn-content {
  position: relative;
  z-index: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1rem;
}

.btn-content svg {
  width: 28px;
  height: 28px;
}

.btn-subscribe-premium:hover:not(:disabled) {
  transform: translateY(-5px);
  box-shadow: 0 20px 60px rgba(102, 126, 234, 0.7);
}

.btn-subscribe-premium:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  background: rgba(255, 255, 255, 0.1);
}

.spinner-premium {
  width: 24px;
  height: 24px;
  border: 3px solid rgba(255, 255, 255, 0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* Trust Badges */
.trust-badges {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1rem;
}

.trust-badge {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;
  padding: 1rem;
  background: rgba(255, 255, 255, 0.03);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  text-align: center;
}

.trust-badge svg {
  width: 28px;
  height: 28px;
  color: #48bb78;
}

.trust-badge span {
  font-size: 0.75rem;
  color: rgba(255, 255, 255, 0.8);
  font-weight: 500;
}

/* Admin Premium Section */
.admin-premium-section {
  position: relative;
  background: rgba(246, 173, 85, 0.1);
  backdrop-filter: blur(20px);
  border: 3px dashed #f6ad55;
  border-radius: 30px;
  padding: 4rem;
  text-align: center;
  overflow: hidden;
}

.admin-glow {
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(246, 173, 85, 0.2) 0%, transparent 70%);
  animation: adminGlow 5s linear infinite;
}

@keyframes adminGlow {
  to { transform: rotate(360deg); }
}

.admin-content {
  position: relative;
  z-index: 1;
}

.admin-icon-large {
  width: 100px;
  height: 100px;
  margin: 0 auto 2rem;
  background: linear-gradient(135deg, #f6ad55 0%, #ed8936 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 20px 60px rgba(246, 173, 85, 0.5);
  animation: adminFloat 3s infinite ease-in-out;
}

@keyframes adminFloat {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}

.admin-icon-large svg {
  width: 50px;
  height: 50px;
  color: white;
}

.admin-title {
  font-size: 2rem;
  font-weight: 800;
  color: white;
  margin: 0 0 1rem 0;
}

.admin-desc {
  font-size: 1.125rem;
  color: rgba(255, 255, 255, 0.8);
  margin: 0 0 2rem 0;
  line-height: 1.6;
}

.btn-admin-premium {
  display: inline-flex;
  align-items: center;
  gap: 1rem;
  padding: 1.5rem 3rem;
  background: linear-gradient(135deg, #f6ad55 0%, #ed8936 100%);
  color: white;
  text-decoration: none;
  border-radius: 50px;
  font-size: 1.125rem;
  font-weight: 700;
  position: relative;
  overflow: hidden;
  transition: all 0.4s;
  box-shadow: 0 10px 40px rgba(246, 173, 85, 0.5);
}

.btn-admin-glow {
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(255,255,255,0.3) 0%, transparent 70%);
  animation: glowRotate 3s linear infinite;
}

.btn-admin-content {
  position: relative;
  z-index: 1;
  display: flex;
  align-items: center;
  gap: 1rem;
}

.btn-admin-content svg {
  width: 24px;
  height: 24px;
}

.btn-admin-premium:hover {
  transform: translateY(-5px);
  box-shadow: 0 20px 60px rgba(246, 173, 85, 0.7);
}

/* Responsive */
@media (max-width: 1400px) {
  .book-detail-premium {
    grid-template-columns: 1fr;
  }

  .book-3d-wrapper {
    padding: 2rem;
  }
}

@media (max-width: 768px) {
  .plans-grid {
    grid-template-columns: 1fr;
  }

  .info-cards {
    grid-template-columns: 1fr;
  }

  .trust-badges {
    grid-template-columns: 1fr;
  }

  .book-title-main {
    font-size: 2.5rem;
  }

  .book-3d {
    width: 280px;
    height: 400px;
  }
}
</style>