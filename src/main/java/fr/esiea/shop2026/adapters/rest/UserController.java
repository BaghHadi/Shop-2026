package fr.esiea.shop2026.adapters.rest;

import fr.esiea.shop2026.domain.entities.User;
import fr.esiea.shop2026.usecase.dto.UserLoginDTO;
import fr.esiea.shop2026.usecase.dto.UserRegisterDTO;
import fr.esiea.shop2026.usecase.dto.UserResponseDTO;
import fr.esiea.shop2026.usecase.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Tag(name = "Gestion Utilisateurs", description = "Inscription, Connexion et Gestion des profils.")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // POST /users (Au lieu de /users/register) -> C'est du pur CRUD (Create)
    @Operation(summary = "Créer un utilisateur", description = "Inscrit un nouvel utilisateur avec rôle (CLIENT ou SELLER)")
    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@RequestBody UserRegisterDTO dto) {
        User created = userService.createClient(
                dto.firstName, dto.lastName, dto.email, dto.password,
                dto.address, dto.phone, dto.role
        );
        return ResponseEntity.ok(mapToDTO(created));
    }

    @Operation(summary = "Connexion", description = "Authentification par email et mot de passe.")
    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody UserLoginDTO dto) {
        // Le GlobalExceptionHandler gérera les erreurs si login échoue
        User user = userService.loginUser(dto.email, dto.password);
        return ResponseEntity.ok(mapToDTO(user));
    }

    @Operation(summary = "Liste des utilisateurs", description = "Récupérer tous les utilisateurs (Admin).")
    @GetMapping
    public List<UserResponseDTO> getAll() {
        return userService.getAllUsers().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Récupérer un profil", description = "Obtenir les détails d'un utilisateur par son ID.")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(mapToDTO(userService.getUserById(id)));
    }

    @Operation(summary = "Supprimer un utilisateur", description = "Supprime un compte utilisateur.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        boolean deleted = userService.removeUser(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // --- Helper de mapping ---
    private UserResponseDTO mapToDTO(User u) {
        return new UserResponseDTO(
                u.getId(),
                u.getFirstName(),
                u.getLastName(),
                u.getEmail(),
                u.getDeliveryAddress(),
                u.getPhone(),
                u.getRole()
        );
    }
}