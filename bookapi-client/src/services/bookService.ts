import api from './api';

export interface Book {
  id: number;
  title: string;
  author: string;
  isbn: string;
  publicationYear: number;
  genre: string;
  pageCount: number;          
  description: string;
  isAvailable: boolean;        
  createdAt: string;
  updatedAt: string;
}

export interface CreateBookRequest {
  title: string;
  author: string;
  isbn: string;
  publicationYear: number;
  genre: string;
  pageCount: number;            
  description: string;
  isAvailable: boolean;
}

export const bookService = {
  async getAll(page = 0, size = 10) {
    const response = await api.get('/books', {
      params: { page, size, sort: 'id,desc' }
    });
    return response.data;
  },

  async getById(id: number): Promise<Book> {
    const response = await api.get(`/books/${id}`);
    return response.data;
  },

  async getAvailable(page = 0, size = 10) {
    const response = await api.get('/books/available', {
      params: { page, size }
    });
    return response.data;
  },

  async searchByTitle(title: string, page = 0, size = 10) {
    const response = await api.get('/books/search/title', {
      params: { title, page, size }
    });
    return response.data;
  },

  async searchByAuthor(author: string, page = 0, size = 10) {
    const response = await api.get('/books/search/author', {
      params: { author, page, size }
    });
    return response.data;
  },

  async create(data: CreateBookRequest): Promise<Book> {
    const response = await api.post('/books', data);
    return response.data;
  },

  async update(id: number, data: CreateBookRequest): Promise<Book> {
    const response = await api.put(`/books/${id}`, data);
    return response.data;
  },

  async delete(id: number): Promise<void> {
    await api.delete(`/books/${id}`);
  }
};
