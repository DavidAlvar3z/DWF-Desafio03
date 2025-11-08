import api from './api';

// ========================================
// INTERFACES
// ========================================

export interface CreateSubscriptionRequest {
  userId: number;
  bookId: number;
  subscriptionType: 'DAILY' | 'WEEKLY' | 'MONTHLY' | 'YEARLY';
  paymentMethod: 'CREDIT_CARD' | 'DEBIT_CARD' | 'PAYPAL' | 'BANK_TRANSFER' | 'CASH';
}

export interface UpdateSubscriptionRequest {
  status?: string;
  startDate?: string;
  endDate?: string;
}

export interface Subscription {
  id: number;
  userId: number;
  userName: string;
  userEmail: string;
  bookId: number;
  bookTitle: string;
  bookAuthor: string;
  subscriptionType: string;
  startDate: string;
  endDate: string;
  status: string;
  price: number;
  paymentMethod: string;
  createdAt: string;
}

// ========================================
// HELPER FUNCTIONS
// ========================================

const getSubscriptionDetails = (type: string) => {
  const details = {
    DAILY: { duration: 1, price: 2.99, name: 'Plan Diario' },
    WEEKLY: { duration: 7, price: 9.99, name: 'Plan Semanal' },
    MONTHLY: { duration: 30, price: 29.99, name: 'Plan Mensual' },
    YEARLY: { duration: 365, price: 299.99, name: 'Plan Anual' }
  };
  return details[type as keyof typeof details];
};

// ========================================
// SUBSCRIPTION SERVICE
// ========================================

export const subscriptionService = {
  /**
   * Obtener todas las suscripciones (Admin)
   */
  async getAll(page = 0, size = 10) {
    const response = await api.get('/subscriptions', {
      params: { page, size, sort: 'id' }
    });
    return response.data;
  },

  /**
   * Obtener una suscripción por ID
   */
  async getById(id: number): Promise<Subscription> {
    const response = await api.get(`/subscriptions/${id}`);
    return response.data;
  },

  /**
   * Obtener suscripciones de un usuario específico
   */
  async getByUserId(userId: number) {
    const response = await api.get(`/subscriptions/user/${userId}`);
    return response.data;
  },

  /**
   * Obtener las suscripciones del usuario actual
   */
  async getMySubscriptions() {
    const response = await api.get('/subscriptions/my-subscriptions');
    return response.data;
  },

  /**
   * Crear una nueva suscripción
   */
  async create(request: CreateSubscriptionRequest): Promise<Subscription> {
    // Calcular detalles según el tipo de suscripción
    const details = getSubscriptionDetails(request.subscriptionType);
    
    // Crear el payload completo con TODOS los campos requeridos
    const payload = {
      userId: request.userId,
      bookId: request.bookId,
      planName: details.name,
      description: `Suscripción ${details.name.toLowerCase()} al libro`,
      price: details.price,
      durationMonths: Math.ceil(details.duration / 30), // Convertir días a meses
      startDate: new Date().toISOString().split('T')[0], // Fecha actual en formato YYYY-MM-DD
      autoRenewal: true,
      status: 'ACTIVE',
      paymentMethod: request.paymentMethod
    };

    const response = await api.post('/subscriptions', payload);
    return response.data;
  },

  /**
   *
   * Acepta campos status, startDate, endDate
   */
  async update(id: number, data: UpdateSubscriptionRequest): Promise<Subscription> {
    const response = await api.put(`/subscriptions/${id}`, data);
    return response.data;
  },

  /**
   * Cancelar una suscripción
   */
  async cancel(id: number): Promise<void> {
    await api.patch(`/subscriptions/${id}/cancel`);
  },

  /**
   * Eliminar una suscripción
   */
  async delete(id: number): Promise<void> {
    await api.delete(`/subscriptions/${id}`);
  },

  /**
   * Actualizar solo el estado de una suscripción
   */
  async updateStatus(id: number, status: string): Promise<any> {
    const response = await api.patch(`/subscriptions/${id}/status`, { status });
    return response.data;
  }
};
