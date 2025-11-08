import api from './api';
import { jwtDecode } from 'jwt-decode';

interface LoginCredentials {
  email: string;
  password: string;
}

interface RegisterData {
  name: string;
  email: string;
  password: string;
  birthDate?: string;
  phone?: string;
}

interface DecodedToken {
  sub: string;
  role: string;
  exp: number;
  iss: string;
  iat: number;
}

interface User {
  id?: number;
  name?: string;
  email: string;
  role: string;
  firstName?: string;
  lastName?: string;
}

export const authService = {
  async login(credentials: LoginCredentials) {
    const response = await api.post('/auth/login', credentials);
    const { token, user } = response.data;
    
    if (token) {
      localStorage.setItem('token', token);
      console.log('✅ Token guardado');
      
      if (user) {
        localStorage.setItem('user', JSON.stringify(user));
        console.log('✅ Usuario guardado:', user);
      }
      
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

  async register(userData: RegisterData) {
    try {
      const response = await api.post('/auth/register', userData);
      const { token, user } = response.data;
      
      if (token) {
        localStorage.setItem('token', token);
        console.log('✅ Token de registro guardado');
        
        if (user) {
          localStorage.setItem('user', JSON.stringify(user));
          console.log('✅ Usuario registrado:', user);
        }
      }
      
      return response.data;
    } catch (error: any) {
      console.error('❌ Error en registro:', error);
      throw error;
    }
  },

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    console.log('👋 Sesión cerrada');
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
      
      const isAdminUser = decoded.role === 'ROLE_ADMIN' || decoded.role === 'ADMIN';
      console.log(`👑 ¿Es admin? ${isAdminUser ? 'SÍ ✅' : 'NO ❌'}`);
      
      return isAdminUser;
    } catch (error) {
      console.error('❌ Error verificando admin:', error);
      return false;
    }
  },

  getCurrentUser(): User | null {
    const userStr = localStorage.getItem('user');
    const token = localStorage.getItem('token');
    
    if (userStr) {
      try {
        const user = JSON.parse(userStr);
        console.log('✅ Usuario obtenido de localStorage:', user);
        return user;
      } catch (error) {
        console.error('❌ Error parseando usuario:', error);
      }
    }
    
    if (token) {
      try {
        const decoded = jwtDecode<DecodedToken>(token);
        const user: User = {
          email: decoded.sub,
          role: decoded.role
        };
        console.log('✅ Usuario obtenido del token:', user);
        return user;
      } catch (error) {
        console.error('❌ Error obteniendo usuario del token:', error);
      }
    }
    
    return null;
  }
};