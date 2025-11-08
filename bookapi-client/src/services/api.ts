import axios from 'axios';
import router from '../router';

const api = axios.create({
  baseURL: 'http://localhost:8080/api/v1',
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  }
});

// Request interceptor - Agregar token a cada request
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
      console.log('🔑 Token agregado al request:', config.url);
    } else {
      console.warn('⚠️ No hay token en localStorage para:', config.url);
    }
    
    // Debug: Muestra la petición completa
    console.log('📤 Request:', {
      method: config.method?.toUpperCase(),
      params: config.params,
      data: config.data,
      headers: {
        Authorization: config.headers.Authorization ? '✅ Presente' : '❌ Faltante',
        ContentType: config.headers['Content-Type']
      }
    });
    
    return config;
  },
  (error) => {
    console.error('❌ Error en request interceptor:', error);
    return Promise.reject(error);
  }
);

// Response interceptor - Manejar errores de autenticación
api.interceptors.response.use(
  (response) => {
    console.log('✅ Response exitoso:', response.config.url, response.status);
    return response;
  },
  (error) => {
    const status = error.response?.status;
    const currentPath = window.location.pathname;
    const errorData = error.response?.data;
    
    console.log('🚨 Error en API:', {
      status,
      ruta: error.config?.url,
      mensaje: errorData?.message || error.message,
      detalles: errorData
    });
    
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
    
    // Si es 400 (bad request), mostrar detalles del error
    if (status === 400) {
      console.error('🔴 Bad Request (400):', {
        url: error.config?.url,
        params: error.config?.params,
        data: errorData
      });
    }
    
    return Promise.reject(error);
  }
);

export default api;