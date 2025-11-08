<template>
  <div class="register-container">
    <!-- Animated Background -->
    <div class="animated-bg">
      <div class="blob blob-1"></div>
      <div class="blob blob-2"></div>
      <div class="blob blob-3"></div>
    </div>

    <!-- Register Card -->
    <div class="register-card" :class="{ 'shake': hasError }">
      <!-- Logo & Title -->
      <div class="register-header">
        <div class="logo-container">
          <div class="logo-circle">
            <svg viewBox="0 0 20 20" fill="currentColor">
              <path d="M8 9a3 3 0 100-6 3 3 0 000 6zM8 11a6 6 0 016 6H2a6 6 0 016-6zM16 7a1 1 0 10-2 0v1h-1a1 1 0 100 2h1v1a1 1 0 102 0v-1h1a1 1 0 100-2h-1V7z" />
            </svg>
          </div>
        </div>
        <h1 class="logo-title">Crear<span>Cuenta</span></h1>
        <p class="subtitle">Únete a LetrasVivas</p>
      </div>

      <!-- Alert Messages -->
      <transition name="slide-fade">
        <div v-if="error" class="alert-error">
          <svg viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clip-rule="evenodd" />
          </svg>
          <span>{{ error }}</span>
        </div>
      </transition>

      <transition name="slide-fade">
        <div v-if="success" class="alert-success">
          <svg viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />
          </svg>
          <span>{{ success }}</span>
        </div>
      </transition>

      <!-- Register Form -->
      <form @submit.prevent="handleRegister" class="register-form">
        <div class="form-row">
          <div class="form-group">
            <label for="firstName">Nombre *</label>
            <div class="input-wrapper">
              <svg class="input-icon" viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z" clip-rule="evenodd" />
              </svg>
              <input
                id="firstName"
                v-model="form.firstName"
                type="text"
                placeholder="Juan"
                required
                :disabled="loading"
              />
            </div>
          </div>

          <div class="form-group">
            <label for="lastName">Apellido *</label>
            <div class="input-wrapper">
              <svg class="input-icon" viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z" clip-rule="evenodd" />
              </svg>
              <input
                id="lastName"
                v-model="form.lastName"
                type="text"
                placeholder="Pérez"
                required
                :disabled="loading"
              />
            </div>
          </div>
        </div>

        <div class="form-group">
          <label for="email">Correo Electrónico *</label>
          <div class="input-wrapper">
            <svg class="input-icon" viewBox="0 0 20 20" fill="currentColor">
              <path d="M2.003 5.884L10 9.882l7.997-3.998A2 2 0 0016 4H4a2 2 0 00-1.997 1.884z"></path>
              <path d="M18 8.118l-8 4-8-4V14a2 2 0 002 2h12a2 2 0 002-2V8.118z"></path>
            </svg>
            <input
              id="email"
              v-model="form.email"
              type="email"
              placeholder="juan.perez@ejemplo.com"
              required
              :disabled="loading"
            />
          </div>
        </div>

        <div class="form-group">
          <label for="password">Contraseña *</label>
          <div class="input-wrapper">
            <svg class="input-icon" viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M5 9V7a5 5 0 0110 0v2a2 2 0 012 2v5a2 2 0 01-2 2H5a2 2 0 01-2-2v-5a2 2 0 012-2zm8-2v2H7V7a3 3 0 016 0z" clip-rule="evenodd" />
            </svg>
            <input
              id="password"
              v-model="form.password"
              :type="showPassword ? 'text' : 'password'"
              placeholder="Mín. 8 caracteres"
              required
              minlength="8"
              :disabled="loading"
              @input="validatePassword"
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
          <!-- Password Requirements -->
          <div class="password-requirements">
            <div class="requirement" :class="{ valid: hasMinLength }">
              <svg viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />
              </svg>
              Mínimo 8 caracteres
            </div>
            <div class="requirement" :class="{ valid: hasUppercase }">
              <svg viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />
              </svg>
              Una mayúscula
            </div>
            <div class="requirement" :class="{ valid: hasLowercase }">
              <svg viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />
              </svg>
              Una minúscula
            </div>
            <div class="requirement" :class="{ valid: hasNumber }">
              <svg viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />
              </svg>
              Un número
            </div>
            <div class="requirement" :class="{ valid: hasSpecialChar }">
              <svg viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />
              </svg>
              Un carácter especial
            </div>
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label for="age">Edad *</label>
            <div class="input-wrapper">
              <svg class="input-icon" viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M6 2a1 1 0 00-1 1v1H4a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V6a2 2 0 00-2-2h-1V3a1 1 0 10-2 0v1H7V3a1 1 0 00-1-1zm0 5a1 1 0 000 2h8a1 1 0 100-2H6z" clip-rule="evenodd" />
              </svg>
              <input
                id="age"
                v-model.number="form.age"
                type="number"
                placeholder="25"
                required
                min="18"
                max="120"
                :disabled="loading"
              />
            </div>
          </div>

          <div class="form-group">
            <label for="phoneNumber">Teléfono</label>
            <div class="input-wrapper">
              <svg class="input-icon" viewBox="0 0 20 20" fill="currentColor">
                <path d="M2 3a1 1 0 011-1h2.153a1 1 0 01.986.836l.74 4.435a1 1 0 01-.54 1.06l-1.548.773a11.037 11.037 0 006.105 6.105l.774-1.548a1 1 0 011.059-.54l4.435.74a1 1 0 01.836.986V17a1 1 0 01-1 1h-2C7.82 18 2 12.18 2 5V3z" />
              </svg>
              <input
                id="phoneNumber"
                v-model="form.phoneNumber"
                type="tel"
                placeholder="+503 1234-5678"
                :disabled="loading"
              />
            </div>
          </div>
        </div>

        <button type="submit" class="btn-register" :disabled="loading || !isPasswordValid">
          <span v-if="!loading">Crear Cuenta</span>
          <span v-else class="loading-spinner">
            <svg viewBox="0 0 24 24">
              ircle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4" fill="none" opacityty="0.25"/>
              <path d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z" fill="currentColor"/>
            </svg>
            Registrando...
          </span>
        </button>
      </form>

      <!-- Login Link -->
      <div class="login-link">
        <p>¿Ya tienes una cuenta?</p>
        <router-link to="/login" class="btn-login-link">
          <svg viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M3 3a1 1 0 011 1v12a1 1 0 11-2 0V4a1 1 0 011-1zm7.707 3.293a1 1 0 010 1.414L9.414 9H17a1 1 0 110 2H9.414l1.293 1.293a1 1 0 01-1.414 1.414l-3-3a1 1 0 010-1.414l3-3a1 1 0 011.414 0z" clip-rule="evenodd" />
          </svg>
          Iniciar Sesión
        </router-link>
      </div>
    </div>

    <!-- Footer -->
    <div class="register-footer">
      <p>© 2025 LetrasVivas. Hecho con ❤️</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { authService } from '../services/authService';

const router = useRouter();

const form = ref({
  firstName: '',
  lastName: '',
  email: '',
  password: '',
  age: null as number | null,
  phoneNumber: ''
});

const loading = ref(false);
const error = ref('');
const success = ref('');
const showPassword = ref(false);
const hasError = ref(false);

// Password validation states
const hasMinLength = ref(false);
const hasUppercase = ref(false);
const hasLowercase = ref(false);
const hasNumber = ref(false);
const hasSpecialChar = ref(false);

const isPasswordValid = computed(() => {
  return hasMinLength.value && hasUppercase.value && hasLowercase.value && 
         hasNumber.value && hasSpecialChar.value;
});

const validatePassword = () => {
  const pwd = form.value.password;
  hasMinLength.value = pwd.length >= 8;
  hasUppercase.value = /[A-Z]/.test(pwd);
  hasLowercase.value = /[a-z]/.test(pwd);
  hasNumber.value = /\d/.test(pwd);
  hasSpecialChar.value = /[!@#$%^&*(),.?":{}|<>]/.test(pwd);
};

const handleRegister = async () => {
  if (!isPasswordValid.value) {
    error.value = 'La contraseña no cumple con todos los requisitos';
    hasError.value = true;
    setTimeout(() => hasError.value = false, 500);
    return;
  }

  if (!form.value.age || form.value.age < 18) {
    error.value = 'Debes ser mayor de 18 años para registrarte';
    hasError.value = true;
    setTimeout(() => hasError.value = false, 500);
    return;
  }

  loading.value = true;
  error.value = '';
  success.value = '';

  try {
    const registerData = {
      ...form.value,
      name: `${form.value.firstName} ${form.value.lastName}` // <-- Aquí creas name
    };
    await authService.register(registerData);
    success.value = '¡Cuenta creada exitosamente! Redirigiendo al login...';
    setTimeout(() => {
      router.push('/login');
    }, 2000);
  } catch (err: any) {
    console.error('Registration error:', err);
    error.value = err.response?.data?.message || 'Error al crear la cuenta. Por favor, intenta nuevamente.';
    hasError.value = true;
    setTimeout(() => hasError.value = false, 500);
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.register-container {
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

/* Register Card */
.register-card {
  position: relative;
  z-index: 1;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 24px;
  padding: 3rem;
  width: 100%;
  max-width: 600px;
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
.register-header {
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

/* Alerts */
.alert-error, .alert-success {
  padding: 1rem;
  border-radius: 12px;
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-bottom: 1.5rem;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.alert-error {
  background: linear-gradient(135deg, #fc8181 0%, #f56565 100%);
  color: white;
}

.alert-success {
  background: linear-gradient(135deg, #48bb78 0%, #38a169 100%);
  color: white;
}

.alert-error svg, .alert-success svg {
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
.register-form {
  margin-bottom: 2rem;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

.form-group {
  margin-bottom: 1.25rem;
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

/* Password Requirements */
.password-requirements {
  margin-top: 0.75rem;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 0.5rem;
}

.requirement {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #a0aec0;
  font-size: 0.75rem;
  transition: all 0.3s;
}

.requirement svg {
  width: 16px;
  height: 16px;
  opacity: 0.3;
}

.requirement.valid {
  color: #48bb78;
}

.requirement.valid svg {
  opacity: 1;
}

/* Buttons */
.btn-register {
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

.btn-register:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.5);
}

.btn-register:disabled {
  opacity: 0.6;
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

/* Login Link */
.login-link {
  text-align: center;
  padding-top: 2rem;
  border-top: 2px solid #e2e8f0;
}

.login-link p {
  color: #718096;
  font-size: 0.875rem;
  margin: 0 0 1rem 0;
}

.btn-login-link {
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
  transition: all 0.3s;
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.1);
}

.btn-login-link svg {
  width: 20px;
  height: 20px;
}

.btn-login-link:hover {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
}

/* Footer */
.register-footer {
  position: relative;
  z-index: 1;
  margin-top: 2rem;
  text-align: center;
  color: rgba(255, 255, 255, 0.8);
  font-size: 0.875rem;
}

.register-footer p {
  margin: 0;
}

/* Responsive */
@media (max-width: 640px) {
  .register-card {
    padding: 2rem;
  }

  .logo-title {
    font-size: 2rem;
  }

  .form-row {
    grid-template-columns: 1fr;
  }

  .password-requirements {
    grid-template-columns: 1fr;
  }
}
</style>
