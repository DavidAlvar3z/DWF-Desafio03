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
