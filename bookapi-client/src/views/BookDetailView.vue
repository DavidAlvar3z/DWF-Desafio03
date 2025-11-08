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
        <div class="loader-premium">
          <div class="book-loader">
            <div class="page"></div>
            <div class="page"></div>
            <div class="page"></div>
          </div>
        </div>
        <p class="loading-text">Cargando tu próxima aventura...</p>
      </div>

      <div v-else-if="error" class="error-container">
        <div class="error-icon">
          <svg viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clip-rule="evenodd" />
          </svg>
        </div>
        <h3>¡Houston, tenemos un problema!</h3>
        <p>{{ error }}</p>
        <button @click="loadBook" class="btn-retry">
          <svg viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M4 2a1 1 0 011 1v2.101a7.002 7.002 0 0111.601 2.566 1 1 0 11-1.885.666A5.002 5.002 0 005.999 7H9a1 1 0 010 2H4a1 1 0 01-1-1V3a1 1 0 011-1zm.008 9.057a1 1 0 011.276.61A5.002 5.002 0 0014.001 13H11a1 1 0 110-2h5a1 1 0 011 1v5a1 1 0 11-2 0v-2.101a7.002 7.002 0 01-11.601-2.566 1 1 0 01.61-1.276z" clip-rule="evenodd" />
          </svg>
          Intentar nuevamente
        </button>
      </div>
      
      <div v-else-if="book" class="book-detail-premium">
        <div class="book-showcase">
          <div class="book-3d-wrapper">
            <div class="book-3d">
              <div class="book-spine"></div>
              <div class="book-top"></div>
              <div class="book-front">
                <div class="book-cover-glow"></div>
                <div class="book-content">
                  <div class="book-icon-large">📖</div>
                  <h3 class="book-cover-title">{{ book.title }}</h3>
                  <p class="book-cover-author">{{ book.author }}</p>
                  <div class="book-cover-decoration">
                    <div class="deco-line"></div>
                    <div class="deco-star">✦</div>
                    <div class="deco-line"></div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="info-cards">
            <div class="info-card">
              <div class="info-icon">
                <svg viewBox="0 0 20 20" fill="currentColor">
                  <path d="M9 4.804A7.968 7.968 0 005.5 4c-1.255 0-2.443.29-3.5.804v10A7.969 7.969 0 015.5 14c1.669 0 3.218.51 4.5 1.385A7.962 7.962 0 0114.5 14c1.255 0 2.443.29 3.5.804v-10A7.968 7.968 0 0014.5 4c-1.255 0-2.443.29-3.5.804V12a1 1 0 11-2 0V4.804z" />
                </svg>
              </div>
              <div class="info-content">
                <span class="info-value">{{ book.pageCount }}</span>
                <span class="info-label">Páginas</span>
              </div>
            </div>

            <div class="info-card">
              <div class="info-icon">
                <svg viewBox="0 0 20 20" fill="currentColor">
                  <path fill-rule="evenodd" d="M6 2a1 1 0 00-1 1v1H4a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V6a2 2 0 00-2-2h-1V3a1 1 0 10-2 0v1H7V3a1 1 0 00-1-1zm0 5a1 1 0 000 2h8a1 1 0 100-2H6z" clip-rule="evenodd" />
                </svg>
              </div>
              <div class="info-content">
                <span class="info-value">{{ book.publicationYear }}</span>
                <span class="info-label">Año</span>
              </div>
            </div>

            <div class="info-card">
              <div class="info-icon">
                <svg viewBox="0 0 20 20" fill="currentColor">
                  <path fill-rule="evenodd" d="M17.707 9.293a1 1 0 010 1.414l-7 7a1 1 0 01-1.414 0l-7-7A.997.997 0 012 10V5a3 3 0 013-3h5c.256 0 .512.098.707.293l7 7zM5 6a1 1 0 100-2 1 1 0 000 2z" clip-rule="evenodd" />
                </svg>
              </div>
              <div class="info-content">
                <span class="info-value">{{ book.genre }}</span>
                <span class="info-label">Género</span>
              </div>
            </div>
          </div>
        </div>

        <div class="book-info-premium">
          <div class="availability-section">
            <div class="availability-badge" :class="{ available: book.isAvailable, unavailable: !book.isAvailable }">
              <span class="pulse-ring"></span>
              <span class="pulse-dot"></span>
              <span class="badge-text">{{ book.isAvailable ? '✅ Disponible Ahora' : '❌ No Disponible' }}</span>
            </div>
          </div>

          <div class="title-section">
            <h1 class="book-title-main">
              {{ book.title }}
              <div class="title-underline"></div>
            </h1>
            <h2 class="book-author-main">
              <svg viewBox="0 0 20 20" fill="currentColor">
                <path d="M9 6a3 3 0 11-6 0 3 3 0 016 0zM17 6a3 3 0 11-6 0 3 3 0 016 0zM12.93 17c.046-.327.07-.66.07-1a6.97 6.97 0 00-1.5-4.33A5 5 0 0119 16v1h-6.07zM6 11a5 5 0 015 5v1H1v-1a5 5 0 015-5z" />
              </svg>
              Por <span class="author-name">{{ book.author }}</span>
            </h2>
          </div>

          <div class="tags-section">
            <div class="tag genre-tag-premium">
              <svg viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M17.707 9.293a1 1 0 010 1.414l-7 7a1 1 0 01-1.414 0l-7-7A.997.997 0 012 10V5a3 3 0 013-3h5c.256 0 .512.098.707.293l7 7zM5 6a1 1 0 100-2 1 1 0 000 2z" clip-rule="evenodd" />
              </svg>
              {{ book.genre }}
            </div>
            <div class="tag isbn-tag-premium">
              <svg viewBox="0 0 20 20" fill="currentColor">
                <path d="M3 4a1 1 0 011-1h12a1 1 0 011 1v2a1 1 0 01-1 1H4a1 1 0 01-1-1V4zM3 10a1 1 0 011-1h6a1 1 0 011 1v6a1 1 0 01-1 1H4a1 1 0 01-1-1v-6zM14 9a1 1 0 00-1 1v6a1 1 0 001 1h2a1 1 0 001-1v-6a1 1 0 00-1-1h-2z" />
              </svg>
              ISBN: {{ book.isbn }}
            </div>
          </div>

          <div class="description-premium">
            <div class="section-header">
              <span class="section-icon">📚</span>
              <h3>Sinopsis</h3>
            </div>
            <div class="description-content">
              <p>{{ book.description }}</p>
            </div>
          </div>

          <div v-if="user?.role !== 'ADMIN'" class="subscription-premium-section">
            <div class="section-header">
              <span class="section-icon">🎯</span>
              <h3>Elige tu Plan Premium</h3>
            </div>

            <div class="plans-grid">
              <div 
                v-for="type in subscriptionTypes" 
                :key="type.value" 
                class="plan-premium"
                :class="{ 
                  selected: selectedType === type.value,
                  popular: type.value === 'MONTHLY'
                }"
                @click="selectedType = type.value"
              >
                <div v-if="type.value === 'MONTHLY'" class="popular-badge">⭐ Más Popular</div>
                <div class="plan-icon-premium">{{ type.icon }}</div>
                <h4 class="plan-title">{{ type.label }}</h4>
                <p class="plan-description">{{ type.description }}</p>
                <div class="plan-price-premium">
                  <span class="price-currency">$</span>
                  <span class="price-amount">{{ type.price }}</span>
                  <span class="price-period">{{ type.period }}</span>
                </div>
                <div class="plan-features">
                  <div class="feature">✓ Acceso ilimitado</div>
                  <div class="feature">✓ Sin publicidad</div>
                  <div class="feature">✓ Descarga offline</div>
                </div>
                <div class="plan-selector">
                  <div class="selector-ring">
                    <div v-if="selectedType === type.value" class="selector-check">
                      <svg viewBox="0 0 20 20" fill="currentColor">
                        <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
                      </svg>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="payment-premium">
              <div class="section-header">
                <span class="section-icon">💳</span>
                <h3>Método de Pago</h3>
              </div>
              <div class="payment-selector">
                <svg class="payment-icon" viewBox="0 0 20 20" fill="currentColor">
                  <path d="M4 4a2 2 0 00-2 2v1h16V6a2 2 0 00-2-2H4z" />
                  <path fill-rule="evenodd" d="M18 9H2v5a2 2 0 002 2h12a2 2 0 002-2V9zM4 13a1 1 0 011-1h1a1 1 0 110 2H5a1 1 0 01-1-1zm5-1a1 1 0 100 2h1a1 1 0 100-2H9z" clip-rule="evenodd" />
                </svg>
                <select v-model="selectedPayment" class="payment-select-premium">
                  <option value="CREDIT_CARD">💳 Tarjeta de Crédito</option>
                  <option value="DEBIT_CARD">💳 Tarjeta de Débito</option>
                  <option value="PAYPAL">🅿️ PayPal</option>
                  <option value="BANK_TRANSFER">🏦 Transferencia Bancaria</option>
                  <option value="CASH">💵 Efectivo</option>
                </select>
              </div>
            </div>

            <button
              @click="handleSubscribe"
              class="btn-subscribe-premium"
              :disabled="!book.isAvailable || subscribing"
            >
              <span class="btn-glow"></span>
              <span class="btn-content">
                <svg v-if="!subscribing" viewBox="0 0 20 20" fill="currentColor">
                  <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm1-11a1 1 0 10-2 0v2H7a1 1 0 100 2h2v2a1 1 0 102 0v-2h2a1 1 0 100-2h-2V7z" clip-rule="evenodd" />
                </svg>
                <div v-else class="spinner-premium"></div>
                {{ subscribing ? 'Procesando tu suscripción...' : '🚀 Suscribirme Ahora' }}
              </span>
            </button>

            <div class="trust-badges">
              <div class="trust-badge">
                <svg viewBox="0 0 20 20" fill="currentColor">
                  <path fill-rule="evenodd" d="M2.166 4.999A11.954 11.954 0 0010 1.944 11.954 11.954 0 0017.834 5c.11.65.166 1.32.166 2.001 0 5.225-3.34 9.67-8 11.317C5.34 16.67 2 12.225 2 7c0-.682.057-1.35.166-2.001zm11.541 3.708a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />
                </svg>
                <span>Pago Seguro</span>
              </div>
              <div class="trust-badge">
                <svg viewBox="0 0 20 20" fill="currentColor">
                  <path fill-rule="evenodd" d="M6.267 3.455a3.066 3.066 0 001.745-.723 3.066 3.066 0 013.976 0 3.066 3.066 0 001.745.723 3.066 3.066 0 012.812 2.812c.051.643.304 1.254.723 1.745a3.066 3.066 0 010 3.976 3.066 3.066 0 00-.723 1.745 3.066 3.066 0 01-2.812 2.812 3.066 3.066 0 00-1.745.723 3.066 3.066 0 01-3.976 0 3.066 3.066 0 00-1.745-.723 3.066 3.066 0 01-2.812-2.812 3.066 3.066 0 00-.723-1.745 3.066 3.066 0 010-3.976 3.066 3.066 0 00.723-1.745 3.066 3.066 0 012.812-2.812zm7.44 5.252a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />
                </svg>
                <span>Cancela Cuando Quieras</span>
              </div>
              <div class="trust-badge">
                <svg viewBox="0 0 20 20" fill="currentColor">
                  <path d="M2 10.5a1.5 1.5 0 113 0v6a1.5 1.5 0 01-3 0v-6zM6 10.333v5.43a2 2 0 001.106 1.79l.05.025A4 4 0 008.943 18h5.416a2 2 0 001.962-1.608l1.2-6A2 2 0 0015.56 8H12V4a2 2 0 00-2-2 1 1 0 00-1 1v.667a4 4 0 01-.8 2.4L6.8 7.933a4 4 0 00-.8 2.4z" />
                </svg>
                <span>Garantía 30 Días</span>
              </div>
            </div>
          </div>

          <div v-else class="admin-premium-section">
            <div class="admin-glow"></div>
            <div class="admin-content">
              <div class="admin-icon-large">
                <svg viewBox="0 0 20 20" fill="currentColor">
                  <path fill-rule="evenodd" d="M11.49 3.17c-.38-1.56-2.6-1.56-2.98 0a1.532 1.532 0 01-2.286.948c-1.372-.836-2.942.734-2.106 2.106.54.886.061 2.042-.947 2.287-1.561.379-1.561 2.6 0 2.978a1.532 1.532 0 01.947 2.287c-.836 1.372.734 2.942 2.106 2.106a1.532 1.532 0 012.287.947c.379 1.561 2.6 1.561 2.978 0a1.533 1.533 0 012.287-.947c1.372.836 2.942-.734 2.106-2.106a1.533 1.533 0 01.947-2.287c1.561-.379 1.561-2.6 0-2.978a1.532 1.532 0 01-.947-2.287c.836-1.372-.734-2.942-2.106-2.106a1.532 1.532 0 01-2.287-.947zM10 13a3 3 0 100-6 3 3 0 000 6z" clip-rule="evenodd" />
                </svg>
              </div>
              <h3 class="admin-title">👑 Panel de Administrador</h3>
              <p class="admin-desc">Como administrador, tienes acceso completo para gestionar este libro y todo el contenido del sistema.</p>
              <router-link to="/admin/books" class="btn-admin-premium">
                <span class="btn-admin-glow"></span>
                <span class="btn-admin-content">
                  <svg viewBox="0 0 20 20" fill="currentColor">
                    <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z" />
                  </svg>
                  Ir a Gestión de Libros
                </span>
              </router-link>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import NavBar from '../components/NavBar.vue';
import { bookService } from '../services/bookService';
import { subscriptionService } from '../services/subscriptionService';
import { authService } from '../services/authService';
import type { Book } from '../types/book';

const route = useRoute();
const router = useRouter();

const book = ref<Book | null>(null);
const loading = ref(false);
const error = ref('');
const subscribing = ref(false);

const selectedType = ref('MONTHLY');
const selectedPayment = ref('CREDIT_CARD');

const user = computed(() => authService.getCurrentUser());

const subscriptionTypes = [
  { 
    value: 'DAILY', 
    label: 'Plan Diario', 
    price: '2.99', 
    period: '/día', 
    icon: '⚡', 
    description: 'Perfecto para una lectura rápida' 
  },
  { 
    value: 'WEEKLY', 
    label: 'Plan Semanal', 
    price: '9.99', 
    period: '/semana', 
    icon: '📅', 
    description: 'Ideal para lectores ocasionales' 
  },
  { 
    value: 'MONTHLY', 
    label: 'Plan Mensual', 
    price: '29.99', 
    period: '/mes', 
    icon: '⭐', 
    description: 'El favorito de nuestros lectores' 
  },
  { 
    value: 'YEARLY', 
    label: 'Plan Anual', 
    price: '299.99', 
    period: '/año', 
    icon: '🏆', 
    description: 'Ahorra 2 meses gratis' 
  }
];

const loadBook = async () => {
  loading.value = true;
  error.value = '';
  
  try {
    const bookId = Number(route.params.id);
    book.value = await bookService.getById(bookId);
  } catch (err: any) {
    error.value = 'No pudimos cargar este libro. Por favor, intenta nuevamente.';
  } finally {
    loading.value = false;
  }
};

const handleSubscribe = async () => {
  if (!book.value) return;

  subscribing.value = true;
  const currentUser = authService.getCurrentUser();

  try {
    await subscriptionService.create({
      userId: currentUser.id?,
      bookId: book.value.id,
      subscriptionType: selectedType.value as any,
      paymentMethod: selectedPayment.value as any
    });

    alert('🎉 ¡Felicitaciones! Tu suscripción ha sido creada exitosamente.');
    router.push('/subscriptions');
  } catch (err: any) {
    alert('❌ ' + (err.response?.data?.message || 'Error al crear la suscripción'));
  } finally {
    subscribing.value = false;
  }
};

onMounted(() => {
  loadBook();
});
</script>

<style scoped>
.book-detail-view {
  min-height: 100vh;
  position: relative;
  background: #0a0e27;
  color: white;
  overflow-x: hidden;
}

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
  max-width: none;
  width: 100%;
  margin: 0 auto;
  padding: 2rem 4rem;
}

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

.book-detail-premium {
  display: grid;
  grid-template-columns: 550px 1fr;
  gap: 4rem;
  margin-top: 2rem;
}

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

.book-info-premium {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

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
  padding: 1rem;
}

.plan-premium {
  position: relative;
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(20px);
  border: 2px solid rgba(255, 255, 255, 0.1);
  border-radius: 24px;
  padding: 2rem 2rem 3rem 2rem;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  display: flex;
  flex-direction: column;
  min-height: 420px;
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
  top: -14px;
  right: 20px;
  background: linear-gradient(135deg, #f6ad55 0%, #ed8936 100%);
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.75rem;
  font-weight: 700;
  box-shadow: 0 4px 15px rgba(246, 173, 85, 0.4);
  z-index: 10;
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
  padding-top: 1.5rem;
}

.selector-ring {
  width: 40px;
  height: 40px;
  border: 3px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s;
  flex-shrink: 0;
}

.plan-premium.selected .selector-ring {
  border-color: #667eea;
  background: #667eea;
  box-shadow: 0 0 20px rgba(102, 126, 234, 0.6);
}

.selector-check {
  width: 24px;
  height: 24px;
  color: white;
}

.selector-check svg {
  width: 100%;
  height: 100%;
}

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
