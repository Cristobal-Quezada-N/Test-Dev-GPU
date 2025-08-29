package usach.hackaton.gpu.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import usach.hackaton.gpu.entities.AppUser;
import usach.hackaton.gpu.service.AppUserService;
import usach.hackaton.gpu.service.AuthService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class AppUserController {

    private final AppUserService appUserService;
    private final AuthService authService;

    public AppUserController(AppUserService appUserService, AuthService authService) {
        this.appUserService = appUserService;
        this.authService = authService;
    }

    // Obtener todos los usuarios
    @GetMapping("/getUsers")
    public ResponseEntity<List<AppUser>> getAllUsers() {
        return ResponseEntity.ok(appUserService.getAllUsers());
    }

    // Buscar usuario por email
    @GetMapping("/getByEmail/{email}")
    public ResponseEntity<AppUser> getByEmail(@PathVariable String email) {
        try {
            return ResponseEntity.ok(appUserService.getByEmail(email));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Guardar usuario
    @PostMapping("/createUser")
    public ResponseEntity<AppUser> save(@RequestBody AppUser user) {
        appUserService.save(user);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/me")
    public ResponseEntity<AppUser> getProfile(Authentication authentication) {
        String email = authentication.getName();
        AppUser user = appUserService.getByEmail(email);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/getUserById/{id}")
    public ResponseEntity<AppUser> getUserById(@PathVariable String id) {
        return appUserService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        appUserService.deleteUserAndAuthFactors(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/updateStatus/{id}")
    public ResponseEntity<AppUser> updateUserStatus(
            @PathVariable String id,
            @RequestBody Map<String, Long> body) {

        Long statusId = body.get("statusId");
        AppUser updatedUser = appUserService.updateStatus(id, statusId);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getUserStatus(Authentication authentication) {
        String email = authentication.getName();
        AppUser user = appUserService.getByEmail(email);

        Map<String, Object> response = new HashMap<>();
        response.put("email", user.getEmail());
        response.put("statusId", user.getStatusId());

        return ResponseEntity.ok(response);
    }
}