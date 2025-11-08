import axios from 'axios';
import router from '../router';

const api = axios.create({
  baseURL: 'http://localhost:8080/api/v1',
});

// Request interceptor - Agregar token a cada request
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor - Manejar errores de autenticación
api.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response?.status;
    const currentPath = window.location.pathname;
    
    console.log('🚨 Error en API:', status, 'Ruta:', currentPath);
    
    // Solo redirigir a login si es error 401 (no autenticado) Y NO estamos en login/register
    if (status === 401 && currentPath !== '/login' && currentPath !== '/register') {
      console.log('🧹 Limpiando token y redirigiendo a login...');
      localStorage.removeItem('token');
      router.push('/login');
    }
    
    // Si es 403 (forbidden), NO redirigir, solo loguear el error
    if (status === 403) {
      console.warn('⛔ Acceso denegado (403). El usuario no tiene permisos para esta acción.');
    }
    
    return Promise.reject(error);
  }
);

export default api;
