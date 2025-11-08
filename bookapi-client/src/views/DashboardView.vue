<template>
  <div class="dashboard-view">
    <NavBar />

    <div class="dashboard-container">
      <!-- Hero Section -->
      <div class="hero-section">
        <div class="hero-content">
          <h1 class="hero-title">
            ¡Bienvenido, <span class="highlight">{{ user?.firstName }}</span>! 👋
          </h1>
          <p class="hero-subtitle">
            {{ isAdmin ? 'Panel de administración completo' : 'Explora nuestra increíble colección de libros y descubre tu próxima lectura favorita' }}
          </p>
        </div>
        <div class="hero-illustration">
          <svg viewBox="0 0 200 200" fill="none">
            ircle cx="100" cy="100" r="80" fill="url(#grad1)" opacity="0.2.2"/>
            <path d="M70 80h60v70H70z" fill="url(#grad2)"/>
            <path d="M75 85h50v60H75z" fill="white"/>
            <line x1="85" y1="95" x2="115" y2="95" stroke="#667eea" stroke-width="2"/>
            <line x1="85" y1="105" x2="115" y2="105" stroke="#667eea" stroke-width="2"/>
            <line x1="85" y1="115" x2="115" y2="115" stroke="#667eea" stroke-width="2"/>
            <defs>
              <linearGradient id="grad1" x1="0%" y1="0%" x2="100%" y2="100%">
                <stop offset="0%" style="stop-color:#667eea;stop-opacity:1" />
                <stop offset="100%" style="stop-color:#764ba2;stop-opacity:1" />
              </linearGradient>
              <linearGradient id="grad2" x1="0%" y1="0%" x2="100%" y2="100%">
                <stop offset="0%" style="stop-color:#667eea;stop-opacity:1" />
                <stop offset="100%" style="stop-color:#764ba2;stop-opacity:1" />
              </linearGradient>
            </defs>
          </svg>
        </div>
      </div>

      <!-- Stats Cards -->
      <div v-if="!loadingStats" class="stats-grid">
        <div class="stat-card">
          <div class="stat-icon books">
            <svg viewBox="0 0 20 20" fill="currentColor">
              <path d="M9 4.804A7.968 7.968 0 005.5 4c-1.255 0-2.443.29-3.5.804v10A7.969 7.969 0 015.5 14c1.669 0 3.218.51 4.5 1.385A7.962 7.962 0 0114.5 14c1.255 0 2.443.29 3.5.804v-10A7.968 7.968 0 0014.5 4c-1.255 0-2.443.29-3.5.804V12a1 1 0 11-2 0V4.804z" />
            </svg>
          </div>
          <div class="stat-content">
            <h3 class="stat-value">{{ stats.totalBooks }}</h3>
            <p class="stat-label">Libros Disponibles</p>
          </div>
        </div>

        <div class="stat-card">
          <div class="stat-icon subscriptions">
            <svg viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M6 2a1 1 0 00-1 1v1H4a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V6a2 2 0 00-2-2h-1V3a1 1 0 10-2 0v1H7V3a1 1 0 00-1-1zm0 5a1 1 0 000 2h8a1 1 0 100-2H6z" clip-rule="evenodd" />
            </svg>
          </div>
          <div class="stat-content">
            <h3 class="stat-value">{{ stats.activeSubscriptions }}</h3>
            <p class="stat-label">{{ isAdmin ? 'Total Suscripciones Activas' : 'Mis Suscripciones Activas' }}</p>
          </div>
        </div>

        <div class="stat-card">
          <div class="stat-icon user">
            <svg viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z" clip-rule="evenodd" />
            </svg>
          </div>
          <div class="stat-content">
            <h3 class="stat-value">{{ user?.role === 'ADMIN' ? 'Admin' : 'Usuario' }}</h3>
            <p class="stat-label">Rol de Cuenta</p>
          </div>
        </div>
      </div>

      <!-- Quick Actions -->
      <div class="quick-actions">
        <h2 class="section-title">{{ isAdmin ? 'Panel de Administración' : 'Accesos Rápidos' }}</h2>
        <div class="actions-grid">
          <router-link to="/books" class="action-card">
            <div class="action-icon">📚</div>
            <h3>Explorar Catálogo</h3>
            <p>Descubre miles de libros</p>
            <svg class="action-arrow" viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M10.293 3.293a1 1 0 011.414 0l6 6a1 1 0 010 1.414l-6 6a1 1 0 01-1.414-1.414L14.586 11H3a1 1 0 110-2h11.586l-4.293-4.293a1 1 0 010-1.414z" clip-rule="evenodd" />
            </svg>
          </router-link>

          <!-- USER: Mis Suscripciones -->
          <router-link v-if="!isAdmin" to="/subscriptions" class="action-card">
            <div class="action-icon">📅</div>
            <h3>Mis Suscripciones</h3>
            <p>Gestiona tus suscripciones</p>
            <svg class="action-arrow" viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M10.293 3.293a1 1 0 011.414 0l6 6a1 1 0 010 1.414l-6 6a1 1 0 01-1.414-1.414L14.586 11H3a1 1 0 110-2h11.586l-4.293-4.293a1 1 0 010-1.414z" clip-rule="evenodd" />
            </svg>
          </router-link>

          <!-- ADMIN: Gestionar Suscripciones -->
          <router-link v-if="isAdmin" to="/admin/subscriptions" class="action-card admin-card">
            <div class="action-icon">📅</div>
            <h3>Gestionar Suscripciones</h3>
            <p>Administra todas las suscripciones</p>
            <svg class="action-arrow" viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M10.293 3.293a1 1 0 011.414 0l6 6a1 1 0 010 1.414l-6 6a1 1 0 01-1.414-1.414L14.586 11H3a1 1 0 110-2h11.586l-4.293-4.293a1 1 0 010-1.414z" clip-rule="evenodd" />
            </svg>
          </router-link>

          <router-link to="/profile" class="action-card">
            <div class="action-icon">👤</div>
            <h3>Mi Perfil</h3>
            <p>Configura tu cuenta</p>
            <svg class="action-arrow" viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M10.293 3.293a1 1 0 011.414 0l6 6a1 1 0 010 1.414l-6 6a1 1 0 01-1.414-1.414L14.586 11H3a1 1 0 110-2h11.586l-4.293-4.293a1 1 0 010-1.414z" clip-rule="evenodd" />
            </svg>
          </router-link>

          <!-- ADMIN: Gestionar Libros -->
          <router-link v-if="isAdmin" to="/admin/books" class="action-card admin-card">
            <div class="action-icon">📚</div>
            <h3>Gestionar Libros</h3>
            <p>Administra la biblioteca</p>
            <svg class="action-arrow" viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M10.293 3.293a1 1 0 011.414 0l6 6a1 1 0 010 1.414l-6 6a1 1 0 01-1.414-1.414L14.586 11H3a1 1 0 110-2h11.586l-4.293-4.293a1 1 0 010-1.414z" clip-rule="evenodd" />
            </svg>
          </router-link>

          <!-- ADMIN: Gestionar Usuarios -->
          <router-link v-if="isAdmin" to="/admin/users" class="action-card admin-card">
            <div class="action-icon">👥</div>
            <h3>Gestionar Usuarios</h3>
            <p>Ver, editar y eliminar usuarios</p>
            <svg class="action-arrow" viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M10.293 3.293a1 1 0 011.414 0l6 6a1 1 0 010 1.414l-6 6a1 1 0 01-1.414-1.414L14.586 11H3a1 1 0 110-2h11.586l-4.293-4.293a1 1 0 010-1.414z" clip-rule="evenodd" />
            </svg>
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import NavBar from '../components/NavBar.vue';
import { authService } from '../services/authService';
import { bookService } from '../services/bookService';
import { subscriptionService } from '../services/subscriptionService';

const user = computed(() => authService.getCurrentUser());
const isAdmin = computed(() => authService.isAdmin());
const loadingStats = ref(true);

const stats = ref({
  totalBooks: 0,
  activeSubscriptions: 0
});

onMounted(async () => {
  try {
    // argar total de libros
  const allBooks = await bookService.getAllBooks();
  stats.value.totalBooks = Array.isArray(allBooks) ? allBooks.length : 0;


    // Cargar suscripciones según el rol
    if (isAdmin.value) {
      // ADMIN: Ver TODAS las suscripciones activas del sistema
      try {
        const allSubsResponse = await subscriptionService.getAll(0, 1000);
        let allSubscriptions = [];
        
        if (Array.isArray(allSubsResponse)) {
          allSubscriptions = allSubsResponse;
        } else if (allSubsResponse.content && Array.isArray(allSubsResponse.content)) {
          allSubscriptions = allSubsResponse.content;
        }
        
        // Filtrar solo las activas
        const activeSubs = allSubscriptions.filter((sub: any) => 
          sub.status === 'ACTIVE' || sub.status === 'Active'
        );
        
        stats.value.activeSubscriptions = activeSubs.length;
      } catch (error: any) {
        console.error('Error cargando todas las suscripciones:', error);
        stats.value.activeSubscriptions = 0;
      }
    } else {
      // USER: Ver solo sus suscripciones
      if (user.value?.id) {
        try {
          const subsResponse = await subscriptionService.getMySubscriptions();
          let subscriptions = [];
          
          if (Array.isArray(subsResponse)) {
            subscriptions = subsResponse;
          } else if (subsResponse.content && Array.isArray(subsResponse.content)) {
            subscriptions = subsResponse.content;
          }
          
          // Filtrar solo las activas
          const activeSubs = subscriptions.filter((sub: any) => 
            sub.status === 'ACTIVE' || sub.status === 'Active'
          );
          
          stats.value.activeSubscriptions = activeSubs.length;
        } catch (error: any) {
          console.error('Error cargando mis suscripciones:', error);
          stats.value.activeSubscriptions = 0;
        }
      }
    }
  } catch (error: any) {
    console.error('Error general:', error);
  } finally {
    loadingStats.value = false;
  }
});
</script>

<style scoped>
.dashboard-view {
  min-height: 100vh;
  background: #f5f7fa;
}

.dashboard-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 2rem;
}

/* Hero Section */
.hero-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 24px;
  padding: 3rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  box-shadow: 0 10px 40px rgba(102, 126, 234, 0.3);
  overflow: hidden;
  position: relative;
}

.hero-section::before {
  content: '';
  position: absolute;
  top: -50%;
  right: -20%;
  width: 400px;
  height: 400px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 50%;
  filter: blur(60px);
}

.hero-content {
  flex: 1;
  z-index: 1;
}

.hero-title {
  font-size: 2.5rem;
  font-weight: 800;
  color: white;
  margin: 0 0 1rem 0;
}

.highlight {
  background: rgba(255, 255, 255, 0.2);
  padding: 0.25rem 0.75rem;
  border-radius: 8px;
}

.hero-subtitle {
  font-size: 1.125rem;
  color: rgba(255, 255, 255, 0.9);
  margin: 0;
  max-width: 600px;
}

.hero-illustration {
  width: 200px;
  height: 200px;
  z-index: 1;
}

/* Stats Grid */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 1.5rem;
  margin-bottom: 3rem;
}

.stat-card {
  background: white;
  border-radius: 16px;
  padding: 2rem;
  display: flex;
  align-items: center;
  gap: 1.5rem;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
  transition: all 0.3s;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
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

.stat-icon.books {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon.subscriptions {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-icon.user {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-value {
  font-size: 2rem;
  font-weight: 800;
  color: #2d3748;
  margin: 0;
}

.stat-label {
  font-size: 0.875rem;
  color: #718096;
  margin: 0.25rem 0 0 0;
}

/* Quick Actions */
.section-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: #2d3748;
  margin: 0 0 1.5rem 0;
}

.actions-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
}

.action-card {
  background: white;
  border-radius: 16px;
  padding: 2rem;
  text-decoration: none;
  position: relative;
  overflow: hidden;
  transition: all 0.3s;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
}

.action-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
}

.action-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
}

.action-card h3 {
  font-size: 1.25rem;
  font-weight: 700;
  color: #2d3748;
  margin: 0 0 0.5rem 0;
}

.action-card p {
  color: #718096;
  margin: 0;
  font-size: 0.875rem;
}

.action-arrow {
  position: absolute;
  bottom: 1rem;
  right: 1rem;
  width: 24px;
  height: 24px;
  color: #667eea;
  opacity: 0;
  transform: translateX(-10px);
  transition: all 0.3s;
}

.action-card:hover .action-arrow {
  opacity: 1;
  transform: translateX(0);
}

.admin-card {
  background: linear-gradient(135deg, rgba(255, 215, 0, 0.1) 0%, rgba(255, 215, 0, 0.05) 100%);
  border: 2px solid rgba(255, 215, 0, 0.3);
}

@media (max-width: 768px) {
  .hero-section {
    flex-direction: column;
    text-align: center;
    padding: 2rem;
  }

  .hero-illustration {
    margin-top: 2rem;
  }

  .hero-title {
    font-size: 2rem;
  }

  .stats-grid {
    grid-template-columns: 1fr;
  }
}
</style>
