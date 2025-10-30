package usach.hackaton.gpu.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import usach.hackaton.gpu.dtos.LoginRequest;
import usach.hackaton.gpu.dtos.LoginResponse;
import usach.hackaton.gpu.dtos.RegisterRequestDTO;
import usach.hackaton.gpu.service.AuthService;

@RestController
@RequestMapping("api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequestDTO dto) {
        authService.register(dto);
        return ResponseEntity.ok(Map.of("message", "Usuario registrado correctamente"));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateAccount(@RequestParam("token") String token) {
        boolean activated = authService.activateUser(token);
        if (activated) {
            return ResponseEntity.ok("Cuenta activada correctamente. Ya puedes iniciar sesión.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Token inválido o expirado. No se pudo activar la cuenta.");
        }
    }
}
