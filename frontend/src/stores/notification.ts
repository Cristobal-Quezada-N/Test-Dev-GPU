import { defineStore } from "pinia";

export const useNotificationStore = defineStore("notification", {
  state: () => ({
    show: false,
    message: "",
    color: "success" as "success" | "error" | "info" | "warning",
    timeout: 3000,
  }),
  actions: {
    notify(message: string, color: "success" | "error" | "info" | "warning" = "info") {
      this.message = message;
      this.color = color;
      this.show = true;
    },
    close() {
      this.show = false;
    }
  }
});
