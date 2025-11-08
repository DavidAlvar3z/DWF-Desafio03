<template>
  <div class="subscriptions-view">
    <NavBar />
    
    <div class="container">
      <h1>📋 Mis Suscripciones</h1>

      <div v-if="loading" class="loading">Cargando suscripciones...</div>
      <div v-else-if="error" class="error">{{ error }}</div>
      
      <div v-else-if="subscriptions.length === 0" class="no-subscriptions">
        <p>No tienes suscripciones activas</p>
        <router-link to="/books" class="btn-browse">Explorar Libros</router-link>
      </div>

      <div v-else class="subscriptions-grid">
        <div v-for="sub in subscriptions" :key="sub.id" class="subscription-card">
          <div class="sub-header">
            <h3>{{ sub.bookTitle }}</h3>
            <span :class="['status-badge', sub.status.toLowerCase()]">
              {{ sub.status }}
            </span>
          </div>

          <p class="author">{{ sub.bookAuthor }}</p>

          <div class="sub-details">
            <div class="detail-item">
              <strong>Tipo:</strong> {{ sub.subscriptionType }}
            </div>
            <div class="detail-item">
              <strong>Precio:</strong> ${{ sub.price.toFixed(2) }}
            </div>
            <div class="detail-item">
              <strong>Inicio:</strong> {{ formatDate(sub.startDate) }}
            </div>
            <div class="detail-item">
              <strong>Vence:</strong> {{ formatDate(sub.endDate) }}
            </div>
            <div class="detail-item">
              <strong>Método de pago:</strong> {{ sub.paymentMethod }}
            </div>
          </div>

          <div class="sub-actions">
            <button
              v-if="sub.status === 'ACTIVE'"
              @click="handleCancel(sub.id)"
              class="btn-cancel"
            >
              Cancelar
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import NavBar from '../components/NavBar.vue';
import { subscriptionService } from '../services/subscriptionService';
import type { Subscription } from '../types/subscription';

console.log('🔥 SUBSCRIPTIONS VIEW LOADED');

const subscriptions = ref<Subscription[]>([]);
const loading = ref(false);
const error = ref('');

const loadSubscriptions = async () => {
  loading.value = true;
  error.value = '';
  
  try {
    subscriptions.value = await subscriptionService.getMySubscriptions();
  } catch (err: any) {
    console.error('Error loading subscriptions:', err);
    
    if (err.response?.status === 404 || err.response?.status === 400) {
      subscriptions.value = [];
    } else {
      error.value = 'Error al cargar las suscripciones';
    }
  } finally {
    loading.value = false;
  }
};

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleDateString('es-ES');
};

const handleCancel = async (id: number) => {
  if (!confirm('¿Estás seguro de cancelar esta suscripción?')) return;

  try {
    await subscriptionService.cancel(id);
    await loadSubscriptions();
    alert('Suscripción cancelada');
  } catch (err: any) {
    alert('Error al cancelar la suscripción');
  }
};

onMounted(() => {
  loadSubscriptions();
});
</script>

<style scoped>
/* ... (mismo CSS que antes) ... */
.subscriptions-view {
  min-height: 100vh;
  background: #f5f7fa;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
}

h1 {
  margin-bottom: 2rem;
  color: #333;
}

.subscriptions-grid {
  display: grid;
  gap: 2rem;
}

.subscription-card {
  background: white;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.sub-header {
  display: flex;
  justify-content: space-between;
  align-items: start;
  margin-bottom: 0.5rem;
}

.sub-header h3 {
  margin: 0;
  color: #333;
}

.status-badge {
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: 500;
}

.status-badge.active {
  background: #e8f5e9;
  color: #4caf50;
}

.status-badge.expired {
  background: #ffebee;
  color: #f44336;
}

.status-badge.cancelled {
  background: #f5f5f5;
  color: #999;
}

.author {
  color: #666;
  margin: 0 0 1.5rem 0;
}

.sub-details {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1rem;
  padding: 1.5rem;
  background: #f5f7fa;
  border-radius: 8px;
  margin-bottom: 1.5rem;
}

.detail-item {
  color: #666;
}

.detail-item strong {
  color: #333;
}

.sub-actions {
  display: flex;
  gap: 1rem;
}

.btn-cancel {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.3s;
  background: #ffebee;
  color: #f44336;
}

.btn-cancel:hover {
  background: #f44336;
  color: white;
}

.no-subscriptions {
  text-align: center;
  padding: 4rem;
}

.no-subscriptions p {
  font-size: 1.2rem;
  color: #666;
  margin-bottom: 2rem;
}

.btn-browse {
  display: inline-block;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 1rem 2rem;
  border-radius: 8px;
  text-decoration: none;
  font-weight: 600;
}

.loading, .error {
  text-align: center;
  padding: 3rem;
  font-size: 1.2rem;
}

.error {
  color: #f44336;
}
</style>
