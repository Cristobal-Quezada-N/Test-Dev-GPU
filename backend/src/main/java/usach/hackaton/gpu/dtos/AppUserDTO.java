package usach.hackaton.gpu.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDTO {
    private String id;
    private String email;
    private String password;
    private String role;
    private String status;
}
