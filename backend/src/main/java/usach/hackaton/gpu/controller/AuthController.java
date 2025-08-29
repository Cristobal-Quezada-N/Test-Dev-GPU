package usach.hackaton.gpu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usach.hackaton.gpu.dtos.LoginDTO;
import usach.hackaton.gpu.dtos.RegisterRequestDTO;
import usach.hackaton.gpu.service.AuthService;

import java.util.Map;

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
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginDTO dto) {
        Map<String, Object> response = authService.login(dto.getEmail(), dto.getPassword());
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
