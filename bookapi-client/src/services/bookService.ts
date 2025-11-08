import api from './api';

export interface Book {
  id: number;
  title: string;
  author: string;
  isbn: string;
  genre: string;
  publicationYear: number;
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
  genre: string;
  publicationYear: number;
  pageCount: number;
  description?: string;
  isAvailable: boolean;
}

export interface UpdateBookRequest {
  title?: string;
  author?: string;
  isbn?: string;
  genre?: string;
  publicationYear?: number;
  pageCount?: number;
  description?: string;
  isAvailable?: boolean;
}

export const bookService = {
  async getAllBooks(): Promise<Book[]> {
  const response = await api.get('/books');
    const books = response.data.content || response.data;
    return Array.isArray(books) ? books : [];
  },
  async getBookById(id: number): Promise<Book> {
    const response = await api.get(`/books/${id}`);
    return response.data;
  },

  async createBook(book: CreateBookRequest): Promise<Book> {
    const response = await api.post('/books', book);
    return response.data;
  },

  async updateBook(id: number, book: UpdateBookRequest): Promise<Book> {
    const response = await api.put(`/books/${id}`, book);
    return response.data;
  },

  async deleteBook(id: number): Promise<void> {
    await api.delete(`/books/${id}`);
  },

  async getAvailableBooks(): Promise<Book[]> {
    const response = await api.get('/books?available=true');
    return response.data;
  }
};

// Export individual functions for convenience
export const {
  getAllBooks,
  getBookById,
  createBook,
  updateBook,
  deleteBook,
  getAvailableBooks
} = bookService;
