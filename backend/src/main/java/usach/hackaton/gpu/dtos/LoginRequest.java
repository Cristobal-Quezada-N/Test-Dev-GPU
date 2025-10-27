package usach.hackaton.gpu.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
    @NotBlank(message = "El correo electrónico es requerido") @Email(message = "El correo electrónico debe ser una dirección válida") String email,
    @NotBlank(message = "La contraseña es requerida") @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres") String password) {
}
