import { createRouter, createWebHistory } from 'vue-router';
import { authService } from '../services/authService';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/login',
      name: 'Login',
      component: () => import('../views/LoginView.vue')
    },
    {
      path: '/register',
      name: 'Register',
      component: () => import('../views/RegisterView.vue')
    },
    {
      path: '/dashboard',
      name: 'Dashboard',
      component: () => import('../views/DashboardView.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/books',
      name: 'Books',
      component: () => import('../views/BooksView.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/books/:id',
      name: 'BookDetail',
      component: () => import('../views/BookDetailView.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/subscriptions',
      name: 'Subscriptions',
      component: () => import('../views/SubscriptionsView.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/profile',
      name: 'Profile',
      component: () => import('../views/ProfileView.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/admin/books',
      name: 'AdminBooks',
      component: () => import('../views/AdminBooksView.vue'),
      meta: { requiresAuth: true, requiresAdmin: true }
    },
    {
      path: '/admin/subscriptions',
      name: 'AdminSubscriptions',
      component: () => import('../views/AdminSubscriptionsView.vue'),
      meta: { requiresAuth: true, requiresAdmin: true }
    },
    {
    path: '/admin/users',
    name: 'AdminUsers',
    component: () => import('../views/AdminUsersView.vue'),
    meta: { requiresAuth: true, requiresAdmin: true }
    }
  ]
});

router.beforeEach((to, _from, next) => {
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth);
  const requiresAdmin = to.matched.some(record => record.meta.requiresAdmin);
  const isAuthenticated = authService.isAuthenticated();
  const isAdmin = authService.isAdmin();

  if (requiresAuth && !isAuthenticated) {
    next('/login');
  } else if (requiresAdmin && !isAdmin) {
    alert('Acceso denegado. Solo administradores.');
    next('/dashboard');
  } else if ((to.path === '/login' || to.path === '/register') && isAuthenticated) {
    next('/dashboard');
  } else {
    next();
  }
});

export default router;
