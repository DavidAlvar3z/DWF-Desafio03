import api from './api';
import { jwtDecode } from 'jwt-decode';
import type { LoginRequest, LoginResponse, User } from '../types/user';

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

export const userService = {
  async getAllUsers(page = 0, size = 50): Promise<{ content: User[] }> {  // ✅ Cambiar a 50
    const response = await api.get(`/admin/users?page=${page}&size=${size}`);
    return response.data;
  },
  
  async update(id: number, data: Partial<User>): Promise<User> {
    const response = await api.put(`/admin/users/${id}`, data);  
    return response.data;
  },
  
  async delete(id: number): Promise<void> {
    await api.delete(`/admin/users/${id}`);  
  },
  
  async activate(id: number): Promise<User> {
    const response = await api.patch(`/admin/users/${id}/activate`);  
    return response.data;
  },
  
  async deactivate(id: number): Promise<User> {
    const response = await api.patch(`/admin/users/${id}/deactivate`);  
    return response.data;
  }
};


export const authService = {
  async login(credentials: LoginRequest): Promise<LoginResponse> {
    const response = await api.post('/auth/login', credentials);
    const loginData: LoginResponse = response.data;
    
    if (loginData.token) {
      localStorage.setItem('token', loginData.token);
      console.log('✅ Token guardado');
      
      const user: User = {
        id: loginData.userId,
        firstName: loginData.firstName,
        lastName: loginData.lastName,
        email: loginData.email,
        phoneNumber: '',
        age: 0,
        isActive: true,
        role: loginData.role,
        fullName: `${loginData.firstName} ${loginData.lastName}`,
        subscriptionCount: 0,
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString()
      };
      
      localStorage.setItem('user', JSON.stringify(user));
      console.log('✅ Usuario guardado:', user);
      
      try {
        const decoded = jwtDecode<DecodedToken>(loginData.token);
        console.log('✅ Token decodificado:', decoded);
        console.log('🔍 Email:', decoded.sub);
        console.log('👑 Rol:', decoded.role);
      } catch (error) {
        console.error('❌ Error decodificando token:', error);
      }
    }
    
    return loginData;
  },

  async register(userData: RegisterData): Promise<LoginResponse> {
    try {
      const response = await api.post('/auth/register', userData);
      const loginData: LoginResponse = response.data;
      
      if (loginData.token) {
        localStorage.setItem('token', loginData.token);
        console.log('✅ Token de registro guardado');
        
        const user: User = {
          id: loginData.userId,
          firstName: loginData.firstName,
          lastName: loginData.lastName,
          email: loginData.email,
          phoneNumber: '',
          age: 0,
          isActive: true,
          role: loginData.role,
          fullName: `${loginData.firstName} ${loginData.lastName}`,
          subscriptionCount: 0,
          createdAt: new Date().toISOString(),
          updatedAt: new Date().toISOString()
        };
        
        localStorage.setItem('user', JSON.stringify(user));
        console.log('✅ Usuario registrado:', user);
      }
      
      return loginData;
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
        const user: User = JSON.parse(userStr);
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
          id: 0,
          firstName: '',
          lastName: '',
          email: decoded.sub,
          phoneNumber: '',
          age: 0,
          isActive: true,
          role: decoded.role,
          fullName: decoded.sub,
          subscriptionCount: 0,
          createdAt: new Date().toISOString(),
          updatedAt: new Date().toISOString()
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