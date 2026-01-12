package fr.esiea.shop2026;

import fr.esiea.shop2026.domain.entities.User;
import fr.esiea.shop2026.domain.entities.UserEnum;
import fr.esiea.shop2026.domain.repository.UserEventRepository;
import fr.esiea.shop2026.domain.repository.UserRepository;
import fr.esiea.shop2026.usecase.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserEventRepository userEventRepository; // ✅ AJOUT DU MOCK MANQUANT

    @InjectMocks
    private UserService userService;

    @Test
    void createClient_ShouldEncodePassword_AndPublishEvent() {
        // Arrange
        String rawPwd = "secret";
        String encodedPwd = "encoded_secret";
        User createdUser = new User(); // On crée un user vide pour le retour

        when(passwordEncoder.encode(rawPwd)).thenReturn(encodedPwd);

        // On simule que le repo DB renvoie un user
        when(userRepository.createClient(any(), any(), any(), eq(encodedPwd), any(), any(), any()))
                .thenReturn(createdUser);

        // Act
        userService.createClient("John", "Doe", "john@test.com", rawPwd, "Address", "0000", UserEnum.CLIENT);

        // Assert
        verify(passwordEncoder).encode(rawPwd);
        verify(userRepository).createClient(any(), any(), any(), eq(encodedPwd), any(), any(), any());

        // ✅ ON VÉRIFIE QUE L'ÉVÉNEMENT KAFKA EST BIEN APPELÉ
        verify(userEventRepository).publishUserCreated(createdUser);
    }

    @Test
    void loginUser_WrongPassword_ThrowsException() {
        // Arrange
        String email = "test@test.com";
        String inputPwd = "wrong";
        String storedHash = "hash";

        User foundUser = new User();
        foundUser.setPassword(storedHash);

        when(userRepository.loginUser(email, inputPwd)).thenReturn(foundUser);
        when(passwordEncoder.matches(inputPwd, storedHash)).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.loginUser(email, inputPwd));
    }

    @Test
    void loginUser_Success() {
        // Arrange
        String email = "test@test.com";
        String inputPwd = "good";
        String storedHash = "hash_of_good";

        User foundUser = new User();
        foundUser.setPassword(storedHash);

        when(userRepository.loginUser(email, inputPwd)).thenReturn(foundUser);
        when(passwordEncoder.matches(inputPwd, storedHash)).thenReturn(true);

        // Act
        User result = userService.loginUser(email, inputPwd);

        // Assert
        assertNotNull(result);
    }
}