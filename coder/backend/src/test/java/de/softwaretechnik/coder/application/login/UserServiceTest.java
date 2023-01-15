package de.softwaretechnik.coder.application.login;

import de.softwaretechnik.coder.adapter.secondary.UserRepository;
import de.softwaretechnik.coder.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testLoadUserByUsername_found() {
        // arrange
        String username = "testuser";
        User user = new User();
        user.setUserName(username);
        when(userRepository.findByUserName(username)).thenReturn(Optional.of(user));

        // act
        UserDetails result = userService.loadUserByUsername(username);

        // assert
        assertEquals(username, result.getUsername());
        verify(userRepository).findByUserName(username);
    }

    @Test
    void testLoadUserByUsername_notFound() {
        // arrange
        String username = "testuser";
        when(userRepository.findByUserName(username)).thenReturn(Optional.empty());

        // assert
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername(username);
        });
        verify(userRepository).findByUserName(username);
    }
}
