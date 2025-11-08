<template>
  <nav class="navbar">
    <div class="nav-wrapper">
      <div class="nav-container">
        <!-- Logo -->
        <router-link to="/dashboard" class="nav-brand">
          <div class="brand-icon">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"></path>
              <path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"></path>
            </svg>
          </div>
          <span class="brand-text">
            Letras<span class="brand-highlight">Vivas</span>
          </span>
        </router-link>

        <!-- Desktop Menu -->
        <div class="nav-menu desktop-menu">
          <router-link to="/dashboard" class="nav-link">
            <svg viewBox="0 0 20 20" fill="currentColor">
              <path d="M10.707 2.293a1 1 0 00-1.414 0l-7 7a1 1 0 001.414 1.414L4 10.414V17a1 1 0 001 1h2a1 1 0 001-1v-2a1 1 0 011-1h2a1 1 0 011 1v2a1 1 0 001 1h2a1 1 0 001-1v-6.586l.293.293a1 1 0 001.414-1.414l-7-7z" />
            </svg>
            <span>Dashboard</span>
          </router-link>

          <router-link to="/books" class="nav-link">
            <svg viewBox="0 0 20 20" fill="currentColor">
              <path d="M9 4.804A7.968 7.968 0 005.5 4c-1.255 0-2.443.29-3.5.804v10A7.969 7.969 0 015.5 14c1.669 0 3.218.51 4.5 1.385A7.962 7.962 0 0114.5 14c1.255 0 2.443.29 3.5.804v-10A7.968 7.968 0 0014.5 4c-1.255 0-2.443.29-3.5.804V12a1 1 0 11-2 0V4.804z" />
            </svg>
            <span>Catálogo</span>
          </router-link>

          <!-- 🔴 ADMIN: Gestionar Suscripciones -->
          <router-link v-if="isAdmin" to="/admin/subscriptions" class="nav-link">
            <svg viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M6 2a1 1 0 00-1 1v1H4a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V6a2 2 0 00-2-2h-1V3a1 1 0 10-2 0v1H7V3a1 1 0 00-1-1zm0 5a1 1 0 000 2h8a1 1 0 100-2H6z" clip-rule="evenodd" />
            </svg>
            <span>Gestionar Suscripciones</span>
          </router-link>

          <!-- 🟢 USER: Mis Suscripciones -->
          <router-link v-else to="/subscriptions" class="nav-link">
            <svg viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M6 2a1 1 0 00-1 1v1H4a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V6a2 2 0 00-2-2h-1V3a1 1 0 10-2 0v1H7V3a1 1 0 00-1-1zm0 5a1 1 0 000 2h8a1 1 0 100-2H6z" clip-rule="evenodd" />
            </svg>
            <span>Mis Suscripciones</span>
          </router-link>

          <!-- Admin Panel (solo admins) -->
          <router-link v-if="isAdmin" to="/admin/books" class="nav-link admin-link">
            <svg viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M11.49 3.17c-.38-1.56-2.6-1.56-2.98 0a1.532 1.532 0 01-2.286.948c-1.372-.836-2.942.734-2.106 2.106.54.886.061 2.042-.947 2.287-1.561.379-1.561 2.6 0 2.978a1.532 1.532 0 01.947 2.287c-.836 1.372.734 2.942 2.106 2.106a1.532 1.532 0 012.287.947c.379 1.561 2.6 1.561 2.978 0a1.533 1.533 0 012.287-.947c1.372.836 2.942-.734 2.106-2.106a1.533 1.533 0 01.947-2.287c1.561-.379 1.561-2.6 0-2.978a1.532 1.532 0 01-.947-2.287c.836-1.372-.734-2.942-2.106-2.106a1.532 1.532 0 01-2.287-.947zM10 13a3 3 0 100-6 3 3 0 000 6z" clip-rule="evenodd" />
            </svg>
            <span>Gestionar Libros</span>
          </router-link>
        </div>

        <!-- User Menu -->
        <div class="nav-user">
          <div class="user-info" @click="toggleDropdown">
            <div class="user-avatar">
              {{ userInitials }}
            </div>
            <div class="user-details">
              <span class="user-name">{{ user?.firstName }} {{ user?.lastName }}</span>
              <span class="user-role" :class="{ 'admin-badge': isAdmin }">
                {{ isAdmin ? 'Administrador' : 'Usuario' }}
              </span>
            </div>
            <svg class="dropdown-icon" :class="{ 'rotate': dropdownOpen }" viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clip-rule="evenodd" />
            </svg>
          </div>

          <!-- Dropdown Menu -->
          <transition name="dropdown">
            <div v-if="dropdownOpen" class="dropdown-menu">
              <router-link to="/profile" class="dropdown-item" @click="dropdownOpen = false">
                <svg viewBox="0 0 20 20" fill="currentColor">
                  <path fill-rule="evenodd" d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z" clip-rule="evenodd" />
                </svg>
                <span>Mi Perfil</span>
              </router-link>

              <div class="dropdown-divider"></div>

              <button @click="handleLogout" class="dropdown-item logout-item">
                <svg viewBox="0 0 20 20" fill="currentColor">
                  <path fill-rule="evenodd" d="M3 3a1 1 0 00-1 1v12a1 1 0 102 0V4a1 1 0 00-1-1zm10.293 9.293a1 1 0 001.414 1.414l3-3a1 1 0 000-1.414l-3-3a1 1 0 10-1.414 1.414L14.586 9H7a1 1 0 100 2h7.586l-1.293 1.293z" clip-rule="evenodd" />
                </svg>
                <span>Cerrar Sesión</span>
              </button>
            </div>
          </transition>
        </div>

        <!-- Mobile Menu Button -->
        <button class="mobile-menu-btn" @click="mobileMenuOpen = !mobileMenuOpen">
          <svg v-if="!mobileMenuOpen" viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M3 5a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zM3 10a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zM3 15a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1z" clip-rule="evenodd" />
          </svg>
          <svg v-else viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd" />
          </svg>
        </button>
      </div>
    </div>

    <!-- Mobile Menu -->
    <transition name="slide-down">
      <div v-if="mobileMenuOpen" class="mobile-menu">
        <router-link to="/dashboard" class="mobile-nav-link" @click="mobileMenuOpen = false">
          <svg viewBox="0 0 20 20" fill="currentColor">
            <path d="M10.707 2.293a1 1 0 00-1.414 0l-7 7a1 1 0 001.414 1.414L4 10.414V17a1 1 0 001 1h2a1 1 0 001-1v-2a1 1 0 011-1h2a1 1 0 011 1v2a1 1 0 001 1h2a1 1 0 001-1v-6.586l.293.293a1 1 0 001.414-1.414l-7-7z" />
          </svg>
          <span>Dashboard</span>
        </router-link>

        <router-link to="/books" class="mobile-nav-link" @click="mobileMenuOpen = false">
          <svg viewBox="0 0 20 20" fill="currentColor">
            <path d="M9 4.804A7.968 7.968 0 005.5 4c-1.255 0-2.443.29-3.5.804v10A7.969 7.969 0 015.5 14c1.669 0 3.218.51 4.5 1.385A7.962 7.962 0 0114.5 14c1.255 0 2.443.29 3.5.804v-10A7.968 7.968 0 0014.5 4c-1.255 0-2.443.29-3.5.804V12a1 1 0 11-2 0V4.804z" />
          </svg>
          <span>Catálogo</span>
        </router-link>

        <!-- ADMIN: Gestionar Suscripciones -->
        <router-link v-if="isAdmin" to="/admin/subscriptions" class="mobile-nav-link" @click="mobileMenuOpen = false">
          <svg viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M6 2a1 1 0 00-1 1v1H4a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V6a2 2 0 00-2-2h-1V3a1 1 0 10-2 0v1H7V3a1 1 0 00-1-1zm0 5a1 1 0 000 2h8a1 1 0 100-2H6z" clip-rule="evenodd" />
          </svg>
          <span>Gestionar Suscripciones</span>
        </router-link>

        <!-- USER: Mis Suscripciones -->
        <router-link v-else to="/subscriptions" class="mobile-nav-link" @click="mobileMenuOpen = false">
          <svg viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M6 2a1 1 0 00-1 1v1H4a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V6a2 2 0 00-2-2h-1V3a1 1 0 10-2 0v1H7V3a1 1 0 00-1-1zm0 5a1 1 0 000 2h8a1 1 0 100-2H6z" clip-rule="evenodd" />
          </svg>
          <span>Mis Suscripciones</span>
        </router-link>

        <!-- Admin Panel -->
        <router-link v-if="isAdmin" to="/admin/books" class="mobile-nav-link admin-link" @click="mobileMenuOpen = false">
          <svg viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M11.49 3.17c-.38-1.56-2.6-1.56-2.98 0a1.532 1.532 0 01-2.286.948c-1.372-.836-2.942.734-2.106 2.106.54.886.061 2.042-.947 2.287-1.561.379-1.561 2.6 0 2.978a1.532 1.532 0 01.947 2.287c-.836 1.372.734 2.942 2.106 2.106a1.532 1.532 0 012.287.947c.379 1.561 2.6 1.561 2.978 0a1.533 1.533 0 012.287-.947c1.372.836 2.942-.734 2.106-2.106a1.533 1.533 0 01.947-2.287c1.561-.379 1.561-2.6 0-2.978a1.532 1.532 0 01-.947-2.287c.836-1.372-.734-2.942-2.106-2.106a1.532 1.532 0 01-2.287-.947zM10 13a3 3 0 100-6 3 3 0 000 6z" clip-rule="evenodd" />
          </svg>
          <span>Gestionar Libros</span>
        </router-link>

        <div class="mobile-divider"></div>

        <router-link to="/profile" class="mobile-nav-link" @click="mobileMenuOpen = false">
          <svg viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z" clip-rule="evenodd" />
          </svg>
          <span>Mi Perfil</span>
        </router-link>

        <button @click="handleLogout" class="mobile-nav-link logout-link">
          <svg viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M3 3a1 1 0 00-1 1v12a1 1 0 102 0V4a1 1 0 00-1-1zm10.293 9.293a1 1 0 001.414 1.414l3-3a1 1 0 000-1.414l-3-3a1 1 0 10-1.414 1.414L14.586 9H7a1 1 0 100 2h7.586l-1.293 1.293z" clip-rule="evenodd" />
          </svg>
          <span>Cerrar Sesión</span>
        </button>
      </div>
    </transition>
  </nav>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import { authService } from '../services/authService';

const router = useRouter();
const user = computed(() => authService.getCurrentUser());
const isAdmin = computed(() => authService.isAdmin());
const dropdownOpen = ref(false);
const mobileMenuOpen = ref(false);

const userInitials = computed(() => {
  if (!user.value) return '?';
  const first = user.value.firstName?.[0] || '';
  const last = user.value.lastName?.[0] || '';
  return (first + last).toUpperCase();
});

const toggleDropdown = () => {
  dropdownOpen.value = !dropdownOpen.value;
};

const handleLogout = () => {
  authService.logout();
  router.push('/login');
};

const handleClickOutside = (event: MouseEvent) => {
  const target = event.target as HTMLElement;
  if (!target.closest('.nav-user')) {
    dropdownOpen.value = false;
  }
};

onMounted(() => {
  document.addEventListener('click', handleClickOutside);
});

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside);
});
</script>

<style scoped>
.navbar {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  position: sticky;
  top: 0;
  z-index: 1000;
  backdrop-filter: blur(10px);
}

.nav-wrapper {
  background: rgba(255, 255, 255, 0.05);
}

.nav-container {
  max-width: 1400px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 2rem;
  gap: 2rem;
}

/* Logo */
.nav-brand {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  text-decoration: none;
  transition: transform 0.3s;
}

.nav-brand:hover {
  transform: scale(1.05);
}

.brand-icon {
  width: 40px;
  height: 40px;
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.brand-icon svg {
  width: 24px;
  height: 24px;
}

.brand-text {
  font-size: 1.5rem;
  font-weight: 800;
  color: white;
  letter-spacing: -0.5px;
}

.brand-highlight {
  color: rgba(255, 255, 255, 0.8);
  font-weight: 600;
}

/* Desktop Menu */
.desktop-menu {
  display: flex;
  gap: 0.5rem;
  flex: 1;
  justify-content: center;
}

.nav-link {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1.25rem;
  color: rgba(255, 255, 255, 0.9);
  text-decoration: none;
  border-radius: 10px;
  font-weight: 500;
  transition: all 0.3s;
  position: relative;
}

.nav-link svg {
  width: 20px;
  height: 20px;
}

.nav-link:hover {
  background: rgba(255, 255, 255, 0.15);
  color: white;
  transform: translateY(-2px);
}

.nav-link.router-link-active {
  background: rgba(255, 255, 255, 0.25);
  color: white;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.admin-link {
  background: rgba(255, 215, 0, 0.2);
  border: 1px solid rgba(255, 215, 0, 0.3);
}

.admin-link:hover {
  background: rgba(255, 215, 0, 0.3);
}

/* User Menu */
.nav-user {
  position: relative;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 0.5rem 1rem;
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(10px);
  border-radius: 50px;
  cursor: pointer;
  transition: all 0.3s;
}

.user-info:hover {
  background: rgba(255, 255, 255, 0.25);
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.15);
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #764ba2;
  font-weight: 700;
  font-size: 1rem;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}

.user-details {
  display: flex;
  flex-direction: column;
  gap: 0.125rem;
}

.user-name {
  color: white;
  font-weight: 600;
  font-size: 0.875rem;
  white-space: nowrap;
}

.user-role {
  color: rgba(255, 255, 255, 0.7);
  font-size: 0.75rem;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.admin-badge {
  color: #ffd700;
}

.dropdown-icon {
  width: 16px;
  height: 16px;
  color: rgba(255, 255, 255, 0.8);
  transition: transform 0.3s;
}

.dropdown-icon.rotate {
  transform: rotate(180deg);
}

/* Dropdown Menu */
.dropdown-menu {
  position: absolute;
  top: calc(100% + 0.5rem);
  right: 0;
  background: white;
  border-radius: 12px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
  min-width: 200px;
  overflow: hidden;
  z-index: 1001;
}

.dropdown-item {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 0.875rem 1.25rem;
  color: #2d3748;
  text-decoration: none;
  transition: all 0.2s;
  cursor: pointer;
  border: none;
  background: none;
  width: 100%;
  text-align: left;
  font-size: 0.875rem;
}

.dropdown-item svg {
  width: 18px;
  height: 18px;
  color: #718096;
}

.dropdown-item:hover {
  background: #f7fafc;
  color: #667eea;
}

.dropdown-item:hover svg {
  color: #667eea;
}

.logout-item {
  color: #dc3545;
}

.logout-item:hover {
  background: #fee;
  color: #dc3545;
}

.logout-item svg {
  color: #dc3545;
}

.dropdown-divider {
  height: 1px;
  background: #e2e8f0;
  margin: 0.5rem 0;
}

.dropdown-enter-active, .dropdown-leave-active {
  transition: all 0.3s ease;
}

.dropdown-enter-from {
  opacity: 0;
  transform: translateY(-10px) scale(0.95);
}

.dropdown-leave-to {
  opacity: 0;
  transform: translateY(10px) scale(0.95);
}

/* Mobile Menu Button */
.mobile-menu-btn {
  display: none;
  background: rgba(255, 255, 255, 0.15);
  border: none;
  padding: 0.75rem;
  border-radius: 10px;
  color: white;
  cursor: pointer;
  transition: all 0.3s;
}

.mobile-menu-btn svg {
  width: 24px;
  height: 24px;
}

.mobile-menu-btn:hover {
  background: rgba(255, 255, 255, 0.25);
}

/* Mobile Menu */
.mobile-menu {
  background: white;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.mobile-nav-link {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 1rem 2rem;
  color: #2d3748;
  text-decoration: none;
  transition: all 0.2s;
  border: none;
  background: white;
  width: 100%;
  text-align: left;
}

.mobile-nav-link svg {
  width: 20px;
  height: 20px;
  color: #718096;
}

.mobile-nav-link:hover {
  background: #f7fafc;
  color: #667eea;
}

.mobile-nav-link:hover svg {
  color: #667eea;
}

.mobile-nav-link.router-link-active {
  background: #eef2ff;
  color: #667eea;
  border-left: 4px solid #667eea;
}

.mobile-nav-link.admin-link {
  background: #fffbf0;
}

.logout-link {
  color: #dc3545 !important;
}

.logout-link svg {
  color: #dc3545 !important;
}

.mobile-divider {
  height: 1px;
  background: #e2e8f0;
  margin: 0.5rem 0;
}

.slide-down-enter-active, .slide-down-leave-active {
  transition: all 0.3s ease;
  transform-origin: top;
}

.slide-down-enter-from, .slide-down-leave-to {
  opacity: 0;
  transform: scaleY(0);
}

/* Responsive */
@media (max-width: 1024px) {
  .desktop-menu {
    display: none;
  }

  .mobile-menu-btn {
    display: block;
  }

  .user-details {
    display: none;
  }

  .dropdown-icon {
    display: none;
  }
}

@media (max-width: 640px) {
  .nav-container {
    padding: 1rem;
  }

  .brand-text {
    font-size: 1.25rem;
  }
}
</style>
