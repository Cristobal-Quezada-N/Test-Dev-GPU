package usach.hackaton.gpu.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import usach.hackaton.gpu.entities.AppUser;
import usach.hackaton.gpu.service.AppUserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
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
    @PostMapping("/save")
    public ResponseEntity<AppUser> save(@RequestBody AppUser user) {
        appUserService.save(user);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/me")
    public ResponseEntity<AppUser> getProfile(Authentication authentication) {
        String email = authentication.getName();
        AppUser user = appUserService.getByEmail(email); // usas tu método
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUser> getUserById(@PathVariable String id) {
        return appUserService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        if (appUserService.getUserById(id).isPresent()) {
            appUserService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}