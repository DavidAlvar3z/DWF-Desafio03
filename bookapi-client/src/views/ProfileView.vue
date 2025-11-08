<template>
  <div class="profile-view">
    <NavBar />
    
    <div class="container">
      <h1>👤 Mi Perfil</h1>
      
      <div v-if="user" class="profile-card">
        <div class="profile-header">
          <div class="avatar">{{ user.firstName?.charAt(0) }}{{ user.lastName?.charAt(0) }}</div>
          <div>
            <h2>{{ user.firstName }} {{ user.lastName }}</h2>
            <p class="email">{{ user.email }}</p>
            <span class="role-badge">{{ user.role }}</span>
          </div>
        </div>

        <div class="profile-info">
          <div class="info-item">
            <strong>Email:</strong> {{ user.email }}
          </div>
          <div class="info-item">
            <strong>Nombre:</strong> {{ user.firstName }} {{ user.lastName }}
          </div>
          <div class="info-item">
            <strong>Rol:</strong> {{ user.role }}
          </div>
        </div>

        <button @click="handleLogout" class="btn-logout">
          Cerrar Sesión
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import NavBar from '../components/NavBar.vue';
import { authService } from '../services/authService';

const router = useRouter();
const user = ref<any>(null);

onMounted(() => {
  user.value = authService.getCurrentUser();
});

const handleLogout = () => {
  if (confirm('¿Estás seguro de cerrar sesión?')) {
    authService.logout();
    router.push('/login');
  }
};
</script>

<style scoped>
.profile-view {
  min-height: 100vh;
  background: #f5f7fa;
}

.container {
  max-width: 800px;
  margin: 0 auto;
  padding: 2rem;
}

h1 {
  margin-bottom: 2rem;
  color: #333;
}

.profile-card {
  background: white;
  border-radius: 20px;
  padding: 3rem;
  box-shadow: 0 4px 20px rgba(0,0,0,0.1);
}

.profile-header {
  display: flex;
  align-items: center;
  gap: 2rem;
  padding-bottom: 2rem;
  border-bottom: 2px solid #e1e1e1;
  margin-bottom: 2rem;
}

.avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 2rem;
  font-weight: bold;
}

.profile-header h2 {
  margin: 0 0 0.5rem 0;
  color: #333;
}

.email {
  color: #666;
  margin: 0 0 1rem 0;
}

.role-badge {
  display: inline-block;
  background: #667eea;
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: 600;
}

.profile-info {
  display: grid;
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.info-item {
  padding: 1rem;
  background: #f5f7fa;
  border-radius: 8px;
  color: #666;
}

.info-item strong {
  color: #333;
  margin-right: 0.5rem;
}

.btn-logout {
  width: 100%;
  padding: 1rem;
  background: #f44336;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-logout:hover {
  background: #d32f2f;
  transform: translateY(-2px);
}
</style>
