<template>
  <div class="login-container">
    <!-- Animated Background -->
    <div class="animated-bg">
      <div class="blob blob-1"></div>
      <div class="blob blob-2"></div>
      <div class="blob blob-3"></div>
    </div>

    <!-- Login Card -->
    <div class="login-card" :class="{ 'shake': hasError }">
      <!-- Logo & Title -->
      <div class="login-header">
        <div class="logo-container">
          <div class="logo-circle">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"></path>
              <path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"></path>
            </svg>
          </div>
        </div>
        <h1 class="logo-title">Letras<span>Vivas</span></h1>
        <p class="subtitle">Tu biblioteca digital favorita</p>
      </div>

      <!-- Alert Message -->
      <transition name="slide-fade">
        <div v-if="error" class="alert-error">
          <svg viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clip-rule="evenodd" />
          </svg>
          <span>{{ error }}</span>
        </div>
      </transition>

      <!-- Login Form -->
      <form @submit.prevent="handleLogin" class="login-form">
        <div class="form-group">
          <label for="email">Correo Electrónico</label>
          <div class="input-wrapper">
            <svg class="input-icon" viewBox="0 0 20 20" fill="currentColor">
              <path d="M2.003 5.884L10 9.882l7.997-3.998A2 2 0 0016 4H4a2 2 0 00-1.997 1.884z"></path>
              <path d="M18 8.118l-8 4-8-4V14a2 2 0 002 2h12a2 2 0 002-2V8.118z"></path>
            </svg>
            <input
              id="email"
              v-model="email"
              type="email"
              placeholder="tu@email.com"
              required
              autocomplete="email"
              :disabled="loading"
            />
          </div>
        </div>

        <div class="form-group">
          <label for="password">Contraseña</label>
          <div class="input-wrapper">
            <svg class="input-icon" viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M5 9V7a5 5 0 0110 0v2a2 2 0 012 2v5a2 2 0 01-2 2H5a2 2 0 01-2-2v-5a2 2 0 012-2zm8-2v2H7V7a3 3 0 016 0z" clip-rule="evenodd" />
            </svg>
            <input
              id="password"
              v-model="password"
              :type="showPassword ? 'text' : 'password'"
              placeholder="••••••••"
              required
              autocomplete="current-password"
              :disabled="loading"
            />
            <button
              type="button"
              class="toggle-password"
              @click="showPassword = !showPassword"
              tabindex="-1"
            >
              <svg v-if="!showPassword" viewBox="0 0 20 20" fill="currentColor">
                <path d="M10 12a2 2 0 100-4 2 2 0 000 4z" />
                <path fill-rule="evenodd" d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z" clip-rule="evenodd" />
              </svg>
              <svg v-else viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M3.707 2.293a1 1 0 00-1.414 1.414l14 14a1 1 0 001.414-1.414l-1.473-1.473A10.014 10.014 0 0019.542 10C18.268 5.943 14.478 3 10 3a9.958 9.958 0 00-4.512 1.074l-1.78-1.781zm4.261 4.26l1.514 1.515a2.003 2.003 0 012.45 2.45l1.514 1.514a4 4 0 00-5.478-5.478z" clip-rule="evenodd" />
                <path d="M12.454 16.697L9.75 13.992a4 4 0 01-3.742-3.741L2.335 6.578A9.98 9.98 0 00.458 10c1.274 4.057 5.065 7 9.542 7 .847 0 1.669-.105 2.454-.303z" />
              </svg>
            </button>
          </div>
        </div>

        <button type="submit" class="btn-login" :disabled="loading">
          <span v-if="!loading">Iniciar Sesión</span>
          <span v-else class="loading-spinner">
            <svg viewBox="0 0 24 24">
              ircle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4" fill="none" opacityty="0.25"/>
              <path d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z" fill="currentColor"/>
            </svg>
            Iniciando...
          </span>
        </button>
      </form>

      <!-- Register Link -->
      <div class="register-section">
        <p class="register-text">¿No tienes una cuenta?</p>
        <router-link to="/register" class="btn-register">
          <svg viewBox="0 0 20 20" fill="currentColor">
            <path d="M8 9a3 3 0 100-6 3 3 0 000 6zM8 11a6 6 0 016 6H2a6 6 0 016-6zM16 7a1 1 0 10-2 0v1h-1a1 1 0 100 2h1v1a1 1 0 102 0v-1h1a1 1 0 100-2h-1V7z" />
          </svg>
          Crear cuenta nueva
        </router-link>
      </div>
    </div>

    <!-- Footer -->
    <div class="login-footer">
      <p>© 2025 LetrasVivas. Hecho con ❤️</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { authService } from '../services/authService';

const router = useRouter();
const email = ref('');
const password = ref('');
const error = ref('');
const loading = ref(false);
const showPassword = ref(false);
const hasError = ref(false);

const handleLogin = async () => {
  error.value = '';
  hasError.value = false;
  loading.value = true;

  try {
    await authService.login({
      email: email.value,
      password: password.value,
    });

    const user = authService.getCurrentUser();
    console.log('👤 Usuario logueado:', user);
    
    if (user?.role === 'ROLE_ADMIN' || user?.role === 'ADMIN') {
      console.log('👑 Es admin, redirigiendo a /admin/books...');
      router.push('/admin/books');
    } else {
      console.log('👤 Es usuario normal, redirigiendo a /dashboard...');
      router.push('/dashboard');
    }
  } catch (err: any) {
    console.error('❌ Error en login:', err);
    error.value = err.response?.data?.message || 'Credenciales inválidas. Por favor, intenta de nuevo.';
    hasError.value = true;
    setTimeout(() => hasError.value = false, 500);
  } finally {
    loading.value = false;
  }
};
</script>


<style scoped>
.login-container {
  position: relative;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 2rem;
  overflow: hidden;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

/* Animated Background */
.animated-bg {
  position: absolute;
  inset: 0;
  overflow: hidden;
  z-index: 0;
}

.blob {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.3;
  animation: float 20s infinite ease-in-out;
}

.blob-1 {
  width: 500px;
  height: 500px;
  background: rgba(255, 255, 255, 0.3);
  top: -10%;
  left: -10%;
  animation-delay: 0s;
}

.blob-2 {
  width: 400px;
  height: 400px;
  background: rgba(102, 126, 234, 0.4);
  bottom: -10%;
  right: -10%;
  animation-delay: -7s;
}

.blob-3 {
  width: 350px;
  height: 350px;
  background: rgba(118, 75, 162, 0.3);
  top: 40%;
  left: 50%;
  animation-delay: -14s;
}

@keyframes float {
  0%, 100% { transform: translate(0, 0) scale(1); }
  33% { transform: translate(30px, -50px) scale(1.1); }
  66% { transform: translate(-20px, 20px) scale(0.9); }
}

/* Login Card */
.login-card {
  position: relative;
  z-index: 1;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 24px;
  padding: 3rem;
  width: 100%;
  max-width: 440px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  animation: slideUp 0.6s ease-out;
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

.shake {
  animation: shake 0.5s;
}

@keyframes shake {
  0%, 100% { transform: translateX(0); }
  25% { transform: translateX(-10px); }
  75% { transform: translateX(10px); }
}

/* Header */
.login-header {
  text-align: center;
  margin-bottom: 2rem;
}

.logo-container {
  display: flex;
  justify-content: center;
  margin-bottom: 1rem;
}

.logo-circle {
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
  animation: pulse 2s infinite;
}

.logo-circle svg {
  width: 40px;
  height: 40px;
}

@keyframes pulse {
  0%, 100% { transform: scale(1); box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4); }
  50% { transform: scale(1.05); box-shadow: 0 12px 30px rgba(102, 126, 234, 0.6); }
}

.logo-title {
  font-size: 2.5rem;
  font-weight: 800;
  color: #1a202c;
  margin: 0;
  letter-spacing: -1px;
}

.logo-title span {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.subtitle {
  color: #718096;
  margin: 0.5rem 0 0 0;
  font-size: 1rem;
}

/* Alert */
.alert-error {
  background: linear-gradient(135deg, #fc8181 0%, #f56565 100%);
  color: white;
  padding: 1rem;
  border-radius: 12px;
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-bottom: 1.5rem;
  box-shadow: 0 4px 12px rgba(245, 101, 101, 0.3);
}

.alert-error svg {
  width: 24px;
  height: 24px;
  flex-shrink: 0;
}

.slide-fade-enter-active, .slide-fade-leave-active {
  transition: all 0.3s ease;
}

.slide-fade-enter-from {
  opacity: 0;
  transform: translateY(-10px);
}

.slide-fade-leave-to {
  opacity: 0;
  transform: translateY(10px);
}

/* Form */
.login-form {
  margin-bottom: 2rem;
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
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.input-icon {
  position: absolute;
  left: 1rem;
  width: 20px;
  height: 20px;
  color: #a0aec0;
  pointer-events: none;
  z-index: 1;
}

.input-wrapper input {
  width: 100%;
  padding: 1rem 1rem 1rem 3rem;
  border: 2px solid #e2e8f0;
  border-radius: 12px;
  font-size: 1rem;
  transition: all 0.3s;
  background: white;
}

.input-wrapper input:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.1);
}

.input-wrapper input:disabled {
  background: #f7fafc;
  cursor: not-allowed;
}

.toggle-password {
  position: absolute;
  right: 1rem;
  background: none;
  border: none;
  cursor: pointer;
  padding: 0.5rem;
  color: #a0aec0;
  transition: color 0.2s;
}

.toggle-password:hover {
  color: #667eea;
}

.toggle-password svg {
  width: 20px;
  height: 20px;
}

/* Login Button */
.btn-login {
  width: 100%;
  padding: 1rem;
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

.btn-login:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.5);
}

.btn-login:active:not(:disabled) {
  transform: translateY(0);
}

.btn-login:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.loading-spinner {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
}

.loading-spinner svg {
  width: 20px;
  height: 20px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* Register Section */
.register-section {
  border-top: 2px solid #e2e8f0;
  padding-top: 2rem;
  text-align: center;
}

.register-text {
  color: #718096;
  font-size: 0.875rem;
  margin: 0 0 1rem 0;
}

.btn-register {
  display: inline-flex;
  align-items: center;
  gap: 0.75rem;
  padding: 1rem 2rem;
  background: white;
  color: #667eea;
  border: 2px solid #667eea;
  border-radius: 12px;
  text-decoration: none;
  font-weight: 700;
  font-size: 1rem;
  transition: all 0.3s;
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.1);
}

.btn-register svg {
  width: 20px;
  height: 20px;
}

.btn-register:hover {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
}

/* Footer */
.login-footer {
  position: relative;
  z-index: 1;
  margin-top: 2rem;
  text-align: center;
  color: rgba(255, 255, 255, 0.8);
  font-size: 0.875rem;
}

.login-footer p {
  margin: 0;
}

/* Responsive */
@media (max-width: 480px) {
  .login-card {
    padding: 2rem;
  }

  .logo-title {
    font-size: 2rem;
  }
}
</style>
