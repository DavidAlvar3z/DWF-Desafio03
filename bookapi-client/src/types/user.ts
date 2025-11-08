export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  age: number;
  isActive: boolean;
  role: string;
  fullName: string;
  subscriptionCount: number;
  createdAt: string;
  updatedAt: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  type: string;
  userId: number;
  email: string;
  firstName: string;
  lastName: string;
  role: string;
  expiresIn: number;
}