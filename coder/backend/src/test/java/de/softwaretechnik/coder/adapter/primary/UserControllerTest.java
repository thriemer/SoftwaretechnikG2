package de.softwaretechnik.coder.adapter.primary;

import de.softwaretechnik.coder.adapter.secondary.UserRepository;
import de.softwaretechnik.coder.application.login.UserService;
import static org.mockito.Mockito.*;
import de.softwaretechnik.coder.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Optional;


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @MockBean
    private UserService userService;

    @Test
    void testShowLoginPage() {
        // create the UserController
        UserController controller = new UserController(mock(UserService.class));

        // act
        String result = controller.showLoginPage();

        // assert
        assertEquals("login", result);
    }

    @Test
    void testShowRegistrationPage() {
        // create the UserController
        UserController controller = new UserController(mock(UserService.class));

        // act
        String result = controller.showRegistrationPage();

        // assert
        assertEquals("registration", result);
    }

}