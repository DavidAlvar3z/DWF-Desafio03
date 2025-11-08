<template>
  <router-view />
</template>

<script setup lang="ts">
// App component - just renders router-view
</script>
  await authService.login(email.value, password.value);
    router.push('/dashboard');
  } catch (err: any) {
    error.value = err.response?.data?.message || 'Login failed. Please try again.';
  } finally {
    loading.value = false;
  }
};