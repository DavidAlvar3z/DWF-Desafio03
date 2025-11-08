import api from './api';
import type { User } from '../types/user';

export const userService = {
  // Obtener todos los usuarios (paginado)
  async getAll(page = 0, size = 1000) {
    const response = await api.get('/users', {
      params: {
        page,
        size,
        sort: 'id'
      }
    });
    return response.data;
  },

  // Obtener usuarios (alias para compatibilidad)
  async getUsers(page = 0, size = 10) {
    const response = await api.get('/users', {
      params: {
        page,
        size,
        sort: 'id'
      }
    });
    return response.data;
  },

  // Obtener usuario por ID
  async getUserById(id: number): Promise<User> {
    const response = await api.get(`/users/${id}`);
    return response.data;
  },

  // Buscar usuarios por nombre
  async searchUsers(name: string): Promise<User[]> {
    const response = await api.get(`/users/search?name=${name}`);
    return response.data;
  },

  // Activar usuario
  async activate(id: number): Promise<void> {
    await api.put(`/users/${id}/activate`);
  },

  // Desactivar usuario
  async deactivate(id: number): Promise<void> {
    await api.put(`/users/${id}/deactivate`);
  },

  // Eliminar usuario
  async delete(id: number): Promise<void> {
    await api.delete(`/users/${id}`);
  },

  // Eliminar usuario (alias)
  async deleteUser(id: number): Promise<void> {
    await api.delete(`/users/${id}`);
  },

  // Actualizar usuario
  async update(id: number, userData: Partial<User>): Promise<User> {
    const response = await api.put(`/users/${id}`, userData);
    return response.data;
  },

  // Crear usuario
  async create(userData: Partial<User>): Promise<User> {
    const response = await api.post('/users', userData);
    return response.data;
  }
};
