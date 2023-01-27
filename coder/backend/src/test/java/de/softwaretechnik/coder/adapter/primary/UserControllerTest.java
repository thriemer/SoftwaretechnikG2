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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;


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

    @Test
    void testRegisterUser_userExists() throws Exception {
        //arrange
        String username = "testuser";
        String password = "password";

        User user = User.builder().userName("testuser").password("password").build();
        when(userService.loadUserByUsername(username)).thenReturn(user);
        //act
        mockMvc.perform(post("/registration")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isOk())
                .andExpect(model().attribute("userAlreadyExists", true))
                .andExpect(view().name("registration"));

        //assert
        verify(userService).loadUserByUsername(username);
    }

    @Test
    void testRegisterUser_success() throws Exception {
        //arrange
        String username = "testuser";
        String password = "password";
        when(userService.loadUserByUsername(username)).thenThrow(UsernameNotFoundException.class);
        //act
        mockMvc.perform(post("/registration")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
        //assert
        verify(userService).createUser(username,password);
        verify(userService,times(2)).loadUserByUsername(username);
    }

}