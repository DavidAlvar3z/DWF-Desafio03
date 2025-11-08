import api from './api';
import { jwtDecode } from 'jwt-decode';

interface LoginCredentials {
  email: string;
  password: string;
}

interface DecodedToken {
  sub: string;
  role: string;
  exp: number;
  iss: string;
  iat: number;
}

export const authService = {
  async login(credentials: LoginCredentials) {
    const response = await api.post('/auth/login', credentials);
    const { token } = response.data;
    
    if (token) {
      localStorage.setItem('token', token);
      console.log('✅ Token guardado');
      
      // Decodificar el token para verificar el contenido
      try {
        const decoded = jwtDecode<DecodedToken>(token);
        console.log('✅ Token decodificado:', decoded);
        console.log('🔍 Email:', decoded.sub);
        console.log('👑 Rol:', decoded.role);
      } catch (error) {
        console.error('❌ Error decodificando token:', error);
      }
    }
    
    return response.data;
  },

  logout() {
    localStorage.removeItem('token');
    window.location.href = '/login';
  },

  isAuthenticated(): boolean {
    const token = localStorage.getItem('token');
    if (!token) {
      console.log('❌ No hay token');
      return false;
    }

    try {
      const decoded = jwtDecode<DecodedToken>(token);
      const currentTime = Date.now() / 1000;
      
      if (decoded.exp < currentTime) {
        console.log('❌ Token expirado');
        this.logout();
        return false;
      }
      
      console.log('✅ Usuario autenticado:', decoded.sub);
      return true;
    } catch (error) {
      console.error('❌ Error decodificando token:', error);
      return false;
    }
  },

  isAdmin(): boolean {
    const token = localStorage.getItem('token');
    if (!token) {
      console.log('❌ No hay token para verificar admin');
      return false;
    }

    try {
      const decoded = jwtDecode<DecodedToken>(token);
      console.log('🔍 Verificando rol:', decoded.role);
      
      // El backend devuelve "ROLE_ADMIN"
      const isAdminUser = decoded.role === 'ROLE_ADMIN' || decoded.role === 'ADMIN';
      console.log(`👑 ¿Es admin? ${isAdminUser ? 'SÍ ✅' : 'NO ❌'}`);
      
      return isAdminUser;
    } catch (error) {
      console.error('❌ Error verificando admin:', error);
      return false;
    }
  },

  getCurrentUser() {
    const token = localStorage.getItem('token');
    if (!token) return null;

    try {
      const decoded = jwtDecode<DecodedToken>(token);
      return {
        email: decoded.sub,
        role: decoded.role
      };
    } catch (error) {
      console.error('❌ Error obteniendo usuario:', error);
      return null;
    }
  }
};
