import { useMutation } from "@tanstack/vue-query"
import { useAuthStore } from "@/stores/auth"
import { useAppStore } from "@/stores/app"
import { authService } from "./auth.services";
import queryClient from "../query.client";

export const useLogin = () => {
  const authStore = useAuthStore();
  const appStore = useAppStore();
  return useMutation({
    mutationFn: (data: any) => authService.login(data.email, data.password),
    onSuccess: (data) => {
      authStore.setStoredToken(data.token);
      authStore.setStoredUser(data.user);
      appStore.login(data.user);
    }
  }, queryClient)
}