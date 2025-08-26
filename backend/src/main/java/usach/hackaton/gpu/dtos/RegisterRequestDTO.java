package usach.hackaton.gpu.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequestDTO {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
