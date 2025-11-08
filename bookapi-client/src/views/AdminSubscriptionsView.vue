<template>
  <div class="admin-subscriptions-view">
    <NavBar />

    <div class="container">
      <!-- Header -->
      <div class="header">
        <div class="header-content">
          <div class="header-icon">⚙️</div>
          <div>
            <h1>Gestión de Suscripciones</h1>
            <p>Administra todas las suscripciones del sistema</p>
          </div>
        </div>
      </div>

      <!-- Loading -->
      <div v-if="loading" class="loading">
        <div class="spinner"></div>
        <p>Cargando suscripciones...</p>
      </div>

      <!-- Error -->
      <div v-if="error" class="error-banner">
        <svg viewBox="0 0 20 20" fill="currentColor">
          <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clip-rule="evenodd" />
        </svg>
        <span>{{ error }}</span>
      </div>

      <!-- Stats Cards -->
      <div v-if="!loading" class="stats-grid">
        <div class="stat-card active">
          <div class="stat-icon">📅</div>
          <div class="stat-content">
            <h3>{{ stats.active }}</h3>
            <p>Activas</p>
          </div>
        </div>

        <div class="stat-card expired">
          <div class="stat-icon">⏰</div>
          <div class="stat-content">
            <h3>{{ stats.expired }}</h3>
            <p>Expiradas</p>
          </div>
        </div>

        <div class="stat-card cancelled">
          <div class="stat-icon">🚫</div>
          <div class="stat-content">
            <h3>{{ stats.cancelled }}</h3>
            <p>Canceladas</p>
          </div>
        </div>

        <div class="stat-card total">
          <div class="stat-icon">📊</div>
          <div class="stat-content">
            <h3>{{ subscriptions.length }}</h3>
            <p>Total</p>
          </div>
        </div>
      </div>

      <!-- Filters -->
      <div class="filters">
        <div class="search-box">
          <svg viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z" clip-rule="evenodd" />
          </svg>
          <input 
            v-model="searchQuery" 
            type="text" 
            placeholder="Buscar por usuario, libro o email..."
          />
        </div>
        
        <select v-model="filterStatus" class="filter-select">
          <option value="">Todos los estados</option>
          <option value="ACTIVE">✅ Activas</option>
          <option value="EXPIRED">⏰ Expiradas</option>
          <option value="CANCELLED">🚫 Canceladas</option>
        </select>
      </div>

      <!-- Table -->
      <div v-if="!loading && filteredSubscriptions.length > 0" class="table-container">
        <table class="subscriptions-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Usuario</th>
              <th>Libro</th>
              <th>Plan</th>
              <th>Inicio</th>
              <th>Fin</th>
              <th>Estado</th>
              <th>Precio</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="sub in filteredSubscriptions" :key="sub.id">
              <td><span class="id-badge">#{{ sub.id }}</span></td>
              <td>
                <div class="user-info">
                  <strong>{{ sub.userName }}</strong>
                  <small>{{ sub.userEmail }}</small>
                </div>
              </td>
              <td>
                <div class="book-info">
                  <strong>{{ sub.bookTitle }}</strong>
                  <small>{{ sub.bookAuthor }}</small>
                </div>
              </td>
              <td>
                <span class="badge plan">{{ sub.subscriptionType }}</span>
              </td>
              <td>{{ formatDate(sub.startDate) }}</td>
              <td>{{ formatDate(sub.endDate) }}</td>
              <td>
                <span class="badge" :class="getStatusClass(sub.status)">
                  {{ getStatusText(sub.status) }}
                </span>
              </td>
              <td class="price">${{ sub.price.toFixed(2) }}</td>
              <td>
                <div class="actions">
                  <button 
                    @click="editSubscription(sub)" 
                    class="btn-icon btn-edit"
                    title="Editar"
                  >
                    <svg viewBox="0 0 20 20" fill="currentColor">
                      <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z" />
                    </svg>
                  </button>
                  <button 
                    v-if="sub.status === 'ACTIVE'"
                    @click="cancelSubscription(sub.id)" 
                    class="btn-icon btn-cancel"
                    title="Cancelar"
                  >
                    <svg viewBox="0 0 20 20" fill="currentColor">
                      <path fill-rule="evenodd" d="M13.477 14.89A6 6 0 015.11 6.524l8.367 8.368zm1.414-1.414L6.524 5.11a6 6 0 018.367 8.367zM18 10a8 8 0 11-16 0 8 8 0 0116 0z" clip-rule="evenodd" />
                    </svg>
                  </button>
                  <button 
                    @click="deleteSubscription(sub.id)" 
                    class="btn-icon btn-delete"
                    title="Eliminar"
                  >
                    <svg viewBox="0 0 20 20" fill="currentColor">
                      <path fill-rule="evenodd" d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z" clip-rule="evenodd" />
                    </svg>
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Empty State -->
      <div v-if="!loading && filteredSubscriptions.length === 0" class="empty-state">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"></path>
        </svg>
        <h3>No hay suscripciones</h3>
        <p>No se encontraron suscripciones con los filtros aplicados</p>
      </div>
    </div>

    <!-- Edit Modal -->
    <transition name="modal">
      <div v-if="showEditModal" class="modal-overlay" @click="closeModal">
        <div class="modal" @click.stop>
          <div class="modal-header">
            <h2>✏️ Editar Suscripción #{{ editingSubscription?.id }}</h2>
            <button @click="closeModal" class="btn-close">
              <svg viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd" />
              </svg>
            </button>
          </div>
          <form @submit.prevent="saveSubscription" class="modal-body">
            <div class="form-group">
              <label>Estado</label>
              <select v-model="editForm.status" required>
                <option value="ACTIVE">✅ Activa</option>
                <option value="EXPIRED">⏰ Expirada</option>
                <option value="CANCELLED">🚫 Cancelada</option>
              </select>
            </div>

            <div class="form-row">
              <div class="form-group">
                <label>Fecha de Inicio</label>
                <input v-model="editForm.startDate" type="date" required />
              </div>

              <div class="form-group">
                <label>Fecha de Fin</label>
                <input v-model="editForm.endDate" type="date" required />
              </div>
            </div>

            <div class="modal-actions">
              <button type="button" @click="closeModal" class="btn-secondary">
                Cancelar
              </button>
              <button type="submit" class="btn-primary" :disabled="saving">
                <span v-if="!saving">Guardar Cambios</span>
                <span v-else class="loading-text">
                  <div class="mini-spinner"></div>
                  Guardando...
                </span>
              </button>
            </div>
          </form>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import NavBar from '../components/NavBar.vue';
import { subscriptionService, type Subscription, type UpdateSubscriptionRequest } from '../services/subscriptionService';

// ========================================
// INTERFACES & STATE
// ========================================

const subscriptions = ref<Subscription[]>([]);
const loading = ref(true);
const error = ref('');
const searchQuery = ref('');
const filterStatus = ref('');
const showEditModal = ref(false);
const editingSubscription = ref<Subscription | null>(null);
const saving = ref(false);

const editForm = ref({
  status: '',
  startDate: '',
  endDate: ''
});

// ========================================
// OMPUTED PROPERTIES
// ========================================

const stats = computed(() => {
  return {
    active: subscriptions.value.filter(s => s.status === 'ACTIVE').length,
    expired: subscriptions.value.filter(s => s.status === 'EXPIRED').length,
    cancelled: subscriptions.value.filter(s => s.status === 'CANCELLED').length
  };
});

const filteredSubscriptions = computed(() => {
  let result = subscriptions.value;

  if (filterStatus.value) {
    result = result.filter(s => s.status === filterStatus.value);
  }

  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase();
    result = result.filter(s => 
      s.userName.toLowerCase().includes(query) ||
      s.userEmail.toLowerCase().includes(query) ||
      s.bookTitle.toLowerCase().includes(query) ||
      s.bookAuthor.toLowerCase().includes(query)
    );
  }

  return result;
});

// ========================================
// METHODS
// ========================================

const loadSubscriptions = async () => {
  try {
    loading.value = true;
    error.value = '';
    const response = await subscriptionService.getAll(0, 1000);
    subscriptions.value = response.content || response;
  } catch (err: any) {
    error.value = err.response?.data?.message || 'Error al cargar suscripciones';
    console.error('Error:', err);
  } finally {
    loading.value = false;
  }
};

const editSubscription = (sub: Subscription) => {
  editingSubscription.value = sub;
  
  // ✅ Manejo explícito con condicionales
  let startDate = '';
  let endDate = '';
  
  if (sub.startDate) {
    const parts = sub.startDate.split('T');
    startDate = parts[0] || '';
  }
  
  if (sub.endDate) {
    const parts = sub.endDate.split('T');
    endDate = parts[0] || '';
  }
  
  editForm.value = {
    status: sub.status || 'ACTIVE',
    startDate: startDate,
    endDate: endDate
  };
  
  showEditModal.value = true;
};

const saveSubscription = async () => {
  if (!editingSubscription.value) return;
  
  try {
    saving.value = true;
    
    // Crear objeto con el tipo correcto
    const updateData: UpdateSubscriptionRequest = {
      status: editForm.value.status,
      startDate: editForm.value.startDate,
      endDate: editForm.value.endDate
    };
    
    await subscriptionService.update(editingSubscription.value.id, updateData);
    await loadSubscriptions();
    closeModal();
  } catch (err: any) {
    alert('Error: ' + (err.response?.data?.message || 'No se pudo guardar'));
  } finally {
    saving.value = false;
  }
};

const cancelSubscription = async (id: number) => {
  if (!confirm('¿Cancelar esta suscripción?')) return;
  
  try {
    await subscriptionService.cancel(id);
    await loadSubscriptions();
  } catch (err: any) {
    alert('Error: ' + (err.response?.data?.message || 'No se pudo cancelar'));
  }
};

const deleteSubscription = async (id: number) => {
  if (!confirm('¿Eliminar permanentemente esta suscripción? Esta acción no se puede deshacer.')) return;
  
  try {
    await subscriptionService.delete(id);
    await loadSubscriptions();
  } catch (err: any) {
    alert('Error: ' + (err.response?.data?.message || 'No se pudo eliminar'));
  }
};

const closeModal = () => {
  showEditModal.value = false;
  editingSubscription.value = null;
};

const formatDate = (date: string) => {
  if (!date) return 'N/A';
  return new Date(date).toLocaleDateString('es-ES', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  });
};

const getStatusClass = (status: string) => {
  return {
    'status-active': status === 'ACTIVE',
    'status-expired': status === 'EXPIRED',
    'status-cancelled': status === 'CANCELLED'
  };
};

const getStatusText = (status: string) => {
  const texts: Record<string, string> = {
    'ACTIVE': '✅ Activa',
    'EXPIRED': '⏰ Expirada',
    'CANCELLED': '🚫 Cancelada'
  };
  return texts[status] || status;
};

// ========================================
// LIFECYCLE
// ========================================

onMounted(() => {
  loadSubscriptions();
});
</script>

<style scoped>
.admin-subscriptions-view {
  min-height: 100vh;
  background: #f5f7fa;
}

.container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 2rem;
}

/* Header */
.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 20px;
  padding: 2rem;
  margin-bottom: 2rem;
  box-shadow: 0 10px 40px rgba(102, 126, 234, 0.3);
}

.header-content {
  display: flex;
  align-items: center;
  gap: 1.5rem;
  color: white;
}

.header-icon {
  font-size: 3rem;
  width: 70px;
  height: 70px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 15px;
}

.header h1 {
  margin: 0;
  font-size: 2rem;
  font-weight: 800;
}

.header p {
  margin: 0.5rem 0 0 0;
  opacity: 0.9;
}

/* Loading */
.loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 4rem 2rem;
  gap: 1rem;
}

.spinner {
  width: 50px;
  height: 50px;
  border: 4px solid #e2e8f0;
  border-top-color: #667eea;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* Error Banner */
.error-banner {
  background: linear-gradient(135deg, #fc8181 0%, #f56565 100%);
  color: white;
  padding: 1rem 1.5rem;
  border-radius: 12px;
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-bottom: 2rem;
  box-shadow: 0 4px 15px rgba(245, 101, 101, 0.3);
}

.error-banner svg {
  width: 24px;
  height: 24px;
  flex-shrink: 0;
}

/* Stats Grid */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.stat-card {
  background: white;
  border-radius: 16px;
  padding: 1.5rem;
  display: flex;
  align-items: center;
  gap: 1rem;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
  transition: all 0.3s;
  border-left: 4px solid transparent;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
}

.stat-card.active {
  border-left-color: #48bb78;
}

.stat-card.expired {
  border-left-color: #ed8936;
}

.stat-card.cancelled {
  border-left-color: #f56565;
}

.stat-card.total {
  border-left-color: #667eea;
}

.stat-card .stat-icon {
  font-size: 2.5rem;
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f7fafc;
  border-radius: 12px;
}

.stat-content h3 {
  font-size: 2rem;
  font-weight: 800;
  color: #2d3748;
  margin: 0;
}

.stat-content p {
  font-size: 0.875rem;
  color: #718096;
  margin: 0.25rem 0 0 0;
}

/* Filters */
.filters {
  display: flex;
  gap: 1rem;
  margin-bottom: 2rem;
}

.search-box {
  flex: 1;
  position: relative;
  display: flex;
  align-items: center;
}

.search-box svg {
  position: absolute;
  left: 1rem;
  width: 20px;
  height: 20px;
  color: #a0aec0;
  pointer-events: none;
}

.search-box input {
  width: 100%;
  padding: 1rem 1rem 1rem 3rem;
  border: 2px solid #e2e8f0;
  border-radius: 12px;
  font-size: 1rem;
  transition: all 0.3s;
  background: white;
}

.search-box input:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.1);
}

.filter-select {
  padding: 1rem 1.5rem;
  border: 2px solid #e2e8f0;
  border-radius: 12px;
  font-size: 1rem;
  background: white;
  cursor: pointer;
  transition: all 0.3s;
}

.filter-select:focus {
  outline: none;
  border-color: #667eea;
}

/* Table */
.table-container {
  background: white;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
}

.subscriptions-table {
  width: 100%;
  border-collapse: collapse;
}

.subscriptions-table thead {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.subscriptions-table th {
  padding: 1rem;
  text-align: left;
  font-weight: 600;
  font-size: 0.875rem;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.subscriptions-table tbody tr {
  border-bottom: 1px solid #e2e8f0;
  transition: background 0.2s;
}

.subscriptions-table tbody tr:hover {
  background: #f7fafc;
}

.subscriptions-table td {
  padding: 1rem;
  font-size: 0.875rem;
}

.id-badge {
  background: #edf2f7;
  color: #4a5568;
  padding: 0.25rem 0.75rem;
  border-radius: 6px;
  font-weight: 600;
  font-size: 0.75rem;
}

.user-info, .book-info {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.user-info strong, .book-info strong {
  color: #2d3748;
  font-weight: 600;
}

.user-info small, .book-info small {
  color: #718096;
  font-size: 0.75rem;
}

.badge {
  display: inline-block;
  padding: 0.375rem 0.75rem;
  border-radius: 8px;
  font-size: 0.75rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.badge.plan {
  background: #edf2f7;
  color: #4a5568;
}

.badge.status-active {
  background: #c6f6d5;
  color: #22543d;
}

.badge.status-expired {
  background: #feebc8;
  color: #7c2d12;
}

.badge.status-cancelled {
  background: #fed7d7;
  color: #742a2a;
}

.price {
  font-weight: 700;
  color: #48bb78;
  font-size: 1rem;
}

/* Actions */
.actions {
  display: flex;
  gap: 0.5rem;
}

.btn-icon {
  width: 36px;
  height: 36px;
  border: none;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-icon svg {
  width: 18px;
  height: 18px;
}

.btn-edit {
  background: #bee3f8;
  color: #2c5282;
}

.btn-edit:hover {
  background: #90cdf4;
}

.btn-cancel {
  background: #feebc8;
  color: #7c2d12;
}

.btn-cancel:hover {
  background: #fbd38d;
}

.btn-delete {
  background: #fed7d7;
  color: #742a2a;
}

.btn-delete:hover {
  background: #fc8181;
}

/* Empty State */
.empty-state {
  text-align: center;
  padding: 4rem 2rem;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
}

.empty-state svg {
  width: 80px;
  height: 80px;
  color: #cbd5e0;
  margin-bottom: 1rem;
}

.empty-state h3 {
  color: #2d3748;
  margin: 0 0 0.5rem 0;
  font-size: 1.5rem;
}

.empty-state p {
  color: #718096;
  margin: 0;
}

/* Modal */
.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(4px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 2rem;
}

.modal {
  background: white;
  border-radius: 20px;
  width: 100%;
  max-width: 500px;
  max-height: 90vh;
  overflow: auto;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.modal-header {
  padding: 2rem;
  border-bottom: 2px solid #e2e8f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-header h2 {
  margin: 0;
  font-size: 1.5rem;
  color: #2d3748;
}

.btn-close {
  width: 40px;
  height: 40px;
  border: none;
  background: #f7fafc;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-close:hover {
  background: #edf2f7;
}

.btn-close svg {
  width: 20px;
  height: 20px;
}

.modal-body {
  padding: 2rem;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  color: #2d3748;
  font-weight: 600;
  margin-bottom: 0.5rem;
  font-size: 0.875rem;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 0.75rem 1rem;
  border: 2px solid #e2e8f0;
  border-radius: 10px;
  font-size: 1rem;
  transition: all 0.3s;
}

.form-group input:focus,
.form-group select:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.1);
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

.modal-actions {
  display: flex;
  gap: 1rem;
  margin-top: 2rem;
}

.btn-secondary,
.btn-primary {
  flex: 1;
  padding: 0.875rem 1.5rem;
  border: none;
  border-radius: 10px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-secondary {
  background: #edf2f7;
  color: #4a5568;
}

.btn-secondary:hover {
  background: #e2e8f0;
}

.btn-primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
}

.btn-primary:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.5);
}

.btn-primary:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.loading-text {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
}

.mini-spinner {
  width: 16px;
  height: 16px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 0.6s linear infinite;
}

/* Modal Animation */
.modal-enter-active, .modal-leave-active {
  transition: all 0.3s ease;
}

.modal-enter-from, .modal-leave-to {
  opacity: 0;
}

.modal-enter-from .modal,
.modal-leave-to .modal {
  transform: scale(0.9) translateY(20px);
}

@media (max-width: 768px) {
  .container {
    padding: 1rem;
  }

  .filters {
    flex-direction: column;
  }

  .table-container {
    overflow-x: auto;
  }

  .subscriptions-table {
    min-width: 800px;
  }

  .form-row {
    grid-template-columns: 1fr;
  }
}
</style>
