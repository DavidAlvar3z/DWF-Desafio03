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
