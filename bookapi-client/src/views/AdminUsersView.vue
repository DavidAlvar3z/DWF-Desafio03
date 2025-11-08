<template>
  <div class="admin-users-view">
    <NavBar />
    
    <div class="container">
      <!-- Header -->
      <div class="header">
        <div class="title-section">
          <h1>👥 Gestión de Usuarios</h1>
          <p class="subtitle">Administra todos los usuarios del sistema</p>
        </div>
      </div>

      <!-- Stats Cards -->
      <div v-if="!loading" class="stats-grid">
        <div class="stat-card total">
          <div class="stat-icon">
            <svg viewBox="0 0 20 20" fill="currentColor">
              <path d="M9 6a3 3 0 11-6 0 3 3 0 016 0zM17 6a3 3 0 11-6 0 3 3 0 016 0zM12.93 17c.046-.327.07-.66.07-1a6.97 6.97 0 00-1.5-4.33A5 5 0 0119 16v1h-6.07zM6 11a5 5 0 015 5v1H1v-1a5 5 0 015-5z" />
            </svg>
          </div>
          <div class="stat-content">
            <h3>{{ users.length }}</h3>
            <p>Total Usuarios</p>
          </div>
        </div>

        <div class="stat-card active">
          <div class="stat-icon">
            <svg viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />
            </svg>
          </div>
          <div class="stat-content">
            <h3>{{ activeUsers }}</h3>
            <p>Usuarios Activos</p>
          </div>
        </div>

        <div class="stat-card admins">
          <div class="stat-icon">
            <svg viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M11.49 3.17c-.38-1.56-2.6-1.56-2.98 0a1.532 1.532 0 01-2.286.948c-1.372-.836-2.942.734-2.106 2.106.54.886.061 2.042-.947 2.287-1.561.379-1.561 2.6 0 2.978a1.532 1.532 0 01.947 2.287c-.836 1.372.734 2.942 2.106 2.106a1.532 1.532 0 012.287.947c.379 1.561 2.6 1.561 2.978 0a1.533 1.533 0 012.287-.947c1.372.836 2.942-.734 2.106-2.106a1.533 1.533 0 01.947-2.287c1.561-.379 1.561-2.6 0-2.978a1.532 1.532 0 01-.947-2.287c.836-1.372-.734-2.942-2.106-2.106a1.532 1.532 0 01-2.287-.947zM10 13a3 3 0 100-6 3 3 0 000 6z" clip-rule="evenodd" />
            </svg>
          </div>
          <div class="stat-content">
            <h3>{{ adminUsers }}</h3>
            <p>Administradores</p>
          </div>
        </div>
      </div>

      <!-- Loading State -->
      <div v-if="loading" class="loading-state">
        <div class="spinner"></div>
        <p>Cargando usuarios...</p>
      </div>

      <!-- Error State -->
      <div v-else-if="error" class="error-state">
        <p>❌ {{ error }}</p>
        <button @click="loadUsers" class="btn-retry">Reintentar</button>
      </div>

      <!-- Empty State -->
      <div v-else-if="!loading && users.length === 0" class="empty-state">
        <div class="empty-icon">👥</div>
        <h3>No hay usuarios registrados</h3>
        <p>No se encontraron usuarios en el sistema</p>
      </div>

      <!-- Users Table -->
      <div v-else class="users-table-container">
        <table class="users-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Nombre Completo</th>
              <th>Email</th>
              <th>Edad</th>
              <th>Teléfono</th>
              <th>Rol</th>
              <th>Estado</th>
              <th>Fecha Registro</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="user in users" :key="user.id">
              <td><span class="id-badge">{{ user.id }}</span></td>
              <td class="name-cell">
                <div class="user-name">
                  <svg viewBox="0 0 20 20" fill="currentColor">
                    <path fill-rule="evenodd" d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z" clip-rule="evenodd" />
                  </svg>
                  {{ user.firstName }} {{ user.lastName }}
                </div>
              </td>
              <td><code class="email-code">{{ user.email }}</code></td>
              <td>{{ user.age }} años</td>
              <td>{{ user.phoneNumber || '—' }}</td>
              <td>
                <span :class="['role-badge', user.role === 'ADMIN' ? 'admin' : 'user']">
                  {{ user.role === 'ADMIN' ? '👑 Admin' : '👤 Usuario' }}
                </span>
              </td>
              <td>
                <span :class="['status-badge', user.isActive ? 'active' : 'inactive']">
                  <span class="status-dot"></span>
                  {{ user.isActive ? 'Activo' : 'Inactivo' }}
                </span>
              </td>
              <td>{{ formatDate(user.createdAt) }}</td>
              <td>
                <div class="actions">
                  <button @click="openViewModal(user)" class="btn-action btn-view" title="Ver Detalles">
                    <svg viewBox="0 0 20 20" fill="currentColor">
                      <path d="M10 12a2 2 0 100-4 2 2 0 000 4z" />
                      <path fill-rule="evenodd" d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z" clip-rule="evenodd" />
                    </svg>
                  </button>
                  <button @click="openEditModal(user)" class="btn-action btn-edit" title="Editar">
                    <svg viewBox="0 0 20 20" fill="currentColor">
                      <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z" />
                    </svg>
                  </button>
                  <button 
                    @click="toggleUserStatus(user)" 
                    :class="['btn-action', user.isActive ? 'btn-deactivate' : 'btn-activate']" 
                    :title="user.isActive ? 'Desactivar' : 'Activar'"
                  >
                    <svg v-if="user.isActive" viewBox="0 0 20 20" fill="currentColor">
                      <path fill-rule="evenodd" d="M13.477 14.89A6 6 0 015.11 6.524l8.367 8.368zm1.414-1.414L6.524 5.11a6 6 0 018.367 8.367zM18 10a8 8 0 11-16 0 8 8 0 0116 0z" clip-rule="evenodd" />
                    </svg>
                    <svg v-else viewBox="0 0 20 20" fill="currentColor">
                      <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />
                    </svg>
                  </button>
                  <button @click="handleDelete(user)" class="btn-action btn-delete" title="Eliminar">
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
    </div>

    <!-- Modal View Details -->
    <div v-if="showViewModal" class="modal-overlay" @click="closeViewModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h2>
            <svg viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-6-3a2 2 0 11-4 0 2 2 0 014 0zm-2 4a5 5 0 00-4.546 2.916A5.986 5.986 0 0010 16a5.986 5.986 0 004.546-2.084A5 5 0 0010 11z" clip-rule="evenodd" />
            </svg>
            Detalles del Usuario
          </h2>
          <button @click="closeViewModal" class="btn-close">✕</button>
        </div>
        
        <div v-if="selectedUser" class="user-details">
          <div class="detail-grid">
            <div class="detail-item">
              <label>ID</label>
              <p><span class="id-badge">{{ selectedUser.id }}</span></p>
            </div>

            <div class="detail-item">
              <label>Nombre Completo</label>
              <p>{{ selectedUser.firstName }} {{ selectedUser.lastName }}</p>
            </div>

            <div class="detail-item">
              <label>Email</label>
              <p><code class="email-code">{{ selectedUser.email }}</code></p>
            </div>

            <div class="detail-item">
              <label>Edad</label>
              <p>{{ selectedUser.age }} años</p>
            </div>

            <div class="detail-item">
              <label>Teléfono</label>
              <p>{{ selectedUser.phoneNumber || 'No especificado' }}</p>
            </div>

            <div class="detail-item">
              <label>Rol</label>
              <p>
                <span :class="['role-badge', selectedUser.role === 'ADMIN' ? 'admin' : 'user']">
                  {{ selectedUser.role === 'ADMIN' ? '👑 Admin' : '👤 Usuario' }}
                </span>
              </p>
            </div>

            <div class="detail-item">
              <label>Estado</label>
              <p>
                <span :class="['status-badge', selectedUser.isActive ? 'active' : 'inactive']">
                  <span class="status-dot"></span>
                  {{ selectedUser.isActive ? 'Activo' : 'Inactivo' }}
                </span>
              </p>
            </div>

            <div class="detail-item">
              <label>Fecha de Registro</label>
              <p>{{ formatDate(selectedUser.createdAt) }}</p>
            </div>

            <div class="detail-item full-width">
              <label>Última Actualización</label>
              <p>{{ formatDate(selectedUser.updatedAt) }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal Edit User -->
    <div v-if="showEditModal" class="modal-overlay" @click="closeEditModal">
      <div class="modal-content modal-edit" @click.stop>
        <div class="modal-header">
          <h2>
            <svg viewBox="0 0 20 20" fill="currentColor">
              <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z" />
            </svg>
            Editar Usuario
          </h2>
          <button @click="closeEditModal" class="btn-close">✕</button>
        </div>
        
        <form v-if="editForm" @submit.prevent="handleUpdate" class="edit-form">
          <div class="form-row">
            <div class="form-group">
              <label>Nombre *</label>
              <input v-model="editForm.firstName" type="text" required />
            </div>

            <div class="form-group">
              <label>Apellido *</label>
              <input v-model="editForm.lastName" type="text" required />
            </div>
          </div>

          <div class="form-group">
            <label>Email *</label>
            <input v-model="editForm.email" type="email" required />
          </div>

          <div class="form-row">
            <div class="form-group">
              <label>Edad *</label>
              <input v-model.number="editForm.age" type="number" required min="18" max="120" />
            </div>

            <div class="form-group">
              <label>Teléfono</label>
              <input v-model="editForm.phoneNumber" type="tel" />
            </div>
          </div>

          <div class="form-actions">
            <button type="button" @click="closeEditModal" class="btn-cancel">
              Cancelar
            </button>
            <button type="submit" class="btn-save" :disabled="updating">
              <svg v-if="!updating" viewBox="0 0 20 20" fill="currentColor">
                <path d="M7.707 10.293a1 1 0 10-1.414 1.414l3 3a1 1 0 001.414 0l3-3a1 1 0 00-1.414-1.414L11 11.586V6h5a2 2 0 012 2v7a2 2 0 01-2 2H4a2 2 0 01-2-2V8a2 2 0 012-2h5v5.586l-1.293-1.293zM9 4a1 1 0 012 0v2H9V4z" />
              </svg>
              <div v-else class="spinner-small"></div>
              {{ updating ? 'Guardando...' : 'Guardar Cambios' }}
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
import { userService } from '../services/userService';

interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  age: number;
  phoneNumber?: string;
  role: string;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
}

const users = ref<User[]>([]);
const loading = ref(false);
const error = ref('');
const showViewModal = ref(false);
const showEditModal = ref(false);
const selectedUser = ref<User | null>(null);
const editForm = ref<Partial<User> | null>(null);
const updating = ref(false);

const activeUsers = computed(() => users.value.filter(u => u.isActive).length);
const adminUsers = computed(() => users.value.filter(u => u.role === 'ADMIN').length);

const loadUsers = async () => {
  loading.value = true;
  error.value = '';
  
  try {
    const response = await userService.getAll(0, 1000);
    if (response.content && Array.isArray(response.content)) {
      users.value = response.content;
    } else if (Array.isArray(response)) {
      users.value = response;
    }
    console.log('✅ Usuarios cargados:', users.value);
  } catch (err: any) {
    console.error('❌ Error al cargar usuarios:', err);
    error.value = 'Error al cargar los usuarios';
  } finally {
    loading.value = false;
  }
};

const openViewModal = (user: User) => {
  selectedUser.value = user;
  showViewModal.value = true;
};

const closeViewModal = () => {
  showViewModal.value = false;
  selectedUser.value = null;
};

const openEditModal = (user: User) => {
  editForm.value = {
    id: user.id,
    firstName: user.firstName,
    lastName: user.lastName,
    email: user.email,
    age: user.age,
    phoneNumber: user.phoneNumber || ''
  };
  showEditModal.value = true;
};

const closeEditModal = () => {
  showEditModal.value = false;
  editForm.value = null;
};

const handleUpdate = async () => {
  if (!editForm.value || !editForm.value.id) return;

  updating.value = true;

  try {
    await userService.update(editForm.value.id, editForm.value);
    alert('✅ Usuario actualizado exitosamente');
    closeEditModal();
    await loadUsers();
  } catch (err: any) {
    console.error('❌ Error al actualizar usuario:', err);
    alert(err.response?.data?.message || 'Error al actualizar el usuario');
  } finally {
    updating.value = false;
  }
};

const toggleUserStatus = async (user: User) => {
  const action = user.isActive ? 'desactivar' : 'activar';
  if (!confirm(`¿Estás seguro de ${action} este usuario?`)) return;

  try {
    if (user.isActive) {
      await userService.deactivate(user.id);
    } else {
      await userService.activate(user.id);
    }
    alert(`✅ Usuario ${action}do exitosamente`);
    await loadUsers();
  } catch (err: any) {
    console.error(`❌ Error al ${action} usuario:`, err);
    alert(err.response?.data?.message || `Error al ${action} el usuario`);
  }
};

const handleDelete = async (user: User) => {
  if (!confirm(`¿Estás seguro de eliminar a ${user.firstName} ${user.lastName}? Esta acción no se puede deshacer.`)) return;

  try {
    await userService.delete(user.id);
    alert('✅ Usuario eliminado exitosamente');
    await loadUsers();
  } catch (err: any) {
    console.error('❌ Error al eliminar usuario:', err);
    alert(err.response?.data?.message || 'Error al eliminar el usuario');
  }
};

const formatDate = (dateString: string) => {
  if (!dateString) return '—';
  const date = new Date(dateString);
  return date.toLocaleDateString('es-ES', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
};

onMounted(() => {
  loadUsers();
});
</script>

<style scoped>
.admin-users-view {
  min-height: 100vh;
  background: #f5f7fa;
}

.container {
  max-width: 1600px;
  margin: 0 auto;
  padding: 2rem;
}

/* Header */
.header {
  margin-bottom: 2rem;
}

.title-section h1 {
  margin: 0 0 0.5rem 0;
  color: #333;
  font-size: 2rem;
  font-weight: 800;
}

.subtitle {
  margin: 0;
  color: #666;
  font-size: 1rem;
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
  box-shadow: 0 2px 10px rgba(0,0,0,0.08);
  transition: transform 0.2s;
}

.stat-card:hover {
  transform: translateY(-3px);
}

.stat-icon {
  width: 50px;
  height: 50px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.stat-icon svg {
  width: 26px;
  height: 26px;
}

.stat-card.total .stat-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-card.active .stat-icon {
  background: linear-gradient(135deg, #48bb78 0%, #38a169 100%);
}

.stat-card.admins .stat-icon {
  background: linear-gradient(135deg, #f6ad55 0%, #ed8936 100%);
}

.stat-content h3 {
  margin: 0;
  font-size: 1.75rem;
  font-weight: 800;
  color: #2d3748;
}

.stat-content p {
  margin: 0.25rem 0 0 0;
  color: #718096;
  font-size: 0.875rem;
}

/* Loading State */
.loading-state {
  text-align: center;
  padding: 4rem 2rem;
}

.spinner {
  width: 50px;
  height: 50px;
  border: 5px solid #f3f3f3;
  border-top: 5px solid #667eea;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 1rem;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* Error State */
.error-state {
  text-align: center;
  padding: 4rem 2rem;
  background: white;
  border-radius: 12px;
}

.error-state p {
  color: #dc3545;
  font-size: 1.2rem;
  margin-bottom: 1rem;
}

.btn-retry {
  padding: 0.75rem 1.5rem;
  background: #667eea;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}

/* Empty State */
.empty-state {
  text-align: center;
  padding: 4rem 2rem;
  background: white;
  border-radius: 12px;
}

.empty-icon {
  font-size: 5rem;
  margin-bottom: 1rem;
}

.empty-state h3 {
  margin: 0 0 0.5rem 0;
  color: #333;
}

.empty-state p {
  color: #666;
}

/* Table */
.users-table-container {
  background: white;
  border-radius: 12px;
  overflow-x: auto;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

.users-table {
  width: 100%;
  border-collapse: collapse;
  min-width: 1200px;
}

.users-table th {
  background: #667eea;
  color: white;
  padding: 1rem;
  text-align: left;
  font-weight: 600;
  white-space: nowrap;
}

.users-table td {
  padding: 1rem;
  border-bottom: 1px solid #eee;
  color: #333;
}

.users-table tbody tr:hover {
  background: #f8f9fa;
}

.id-badge {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.875rem;
  font-weight: 600;
}

.name-cell {
  font-weight: 600;
}

.user-name {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #667eea;
}

.user-name svg {
  width: 18px;
  height: 18px;
}

.email-code {
  background: #f7fafc;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  font-size: 0.875rem;
  color: #4a5568;
  font-family: 'Courier New', monospace;
}

.role-badge {
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.875rem;
  font-weight: 600;
}

.role-badge.admin {
  background: #fef3c7;
  color: #92400e;
}

.role-badge.user {
  background: #dbeafe;
  color: #1e40af;
}

.status-badge {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.875rem;
  font-weight: 600;
}

.status-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
}

.status-badge.active {
  background: #d4edda;
  color: #155724;
}

.status-badge.active .status-dot {
  background: #28a745;
}

.status-badge.inactive {
  background: #f8d7da;
  color: #721c24;
}

.status-badge.inactive .status-dot {
  background: #dc3545;
}

.actions {
  display: flex;
  gap: 0.5rem;
}

.btn-action {
  border: none;
  background: none;
  cursor: pointer;
  padding: 0.5rem;
  border-radius: 6px;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.btn-action svg {
  width: 18px;
  height: 18px;
}

.btn-view {
  color: #3b82f6;
}

.btn-view:hover {
  background: #dbeafe;
}

.btn-edit {
  color: #8b5cf6;
}

.btn-edit:hover {
  background: #ede9fe;
}

.btn-activate {
  color: #10b981;
}

.btn-activate:hover {
  background: #d1fae5;
}

.btn-deactivate {
  color: #f59e0b;
}

.btn-deactivate:hover {
  background: #fef3c7;
}

.btn-delete {
  color: #ef4444;
}

.btn-delete:hover {
  background: #fee2e2;
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
  border-radius: 16px;
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

.modal-edit {
  max-width: 600px;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
  padding-bottom: 1rem;
  border-bottom: 2px solid #e2e8f0;
}

.modal-header h2 {
  margin: 0;
  color: #333;
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.modal-header svg {
  width: 28px;
  height: 28px;
  color: #667eea;
}

.btn-close {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: #999;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 6px;
  transition: all 0.2s;
}

.btn-close:hover {
  background: #f7fafc;
  color: #333;
}

.user-details {
  padding: 1rem 0;
}

.detail-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.5rem;
}

.detail-item {
  display: flex;
  flex-direction: column;
}

.detail-item.full-width {
  grid-column: 1 / -1;
}

.detail-item label {
  font-size: 0.875rem;
  font-weight: 600;
  color: #718096;
  margin-bottom: 0.5rem;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.detail-item p {
  margin: 0;
  color: #2d3748;
  font-size: 1rem;
}

/* Edit Form */
.edit-form {
  padding: 1rem 0;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 600;
  color: #2d3748;
  font-size: 0.875rem;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 0.75rem 1rem;
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

.form-actions {
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
  margin-top: 2rem;
}

.btn-cancel {
  padding: 0.75rem 1.5rem;
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
  padding: 0.75rem 1.5rem;
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

@media (max-width: 768px) {
  .detail-grid {
    grid-template-columns: 1fr;
  }

  .form-row {
    grid-template-columns: 1fr;
  }
}
</style>
