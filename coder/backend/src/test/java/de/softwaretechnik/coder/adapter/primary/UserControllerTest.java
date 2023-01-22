package de.softwaretechnik.coder.adapter.primary;

import de.softwaretechnik.coder.application.login.UserService;
import de.softwaretechnik.coder.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Test
    void showLoginPage_mappedToThymeleafLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void showRegistrationPage_mappedToThymeleafRegistration() throws Exception {
        mockMvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"));
    }

    @Test
    void showLoginPage_registerUserAlreadyExist_noRedirect() throws Exception {
        //arrange
        when(userService.loadUserByUsername(anyString())).thenReturn(new User());
        //act, assert
        mockMvc.perform(post("/registration")
                        .param("username", "user")
                        .param("password", "strongPassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(model().attribute("userAlreadyExists", true));

    }


    @Test
    void showLoginPage_freshUser_redirected() throws Exception {
        //arrange
        when(userService.loadUserByUsername(anyString())).
                thenThrow(new UsernameNotFoundException("Ist halt nicht da"));
        //act, assert
        mockMvc.perform(post("/registration")
                        .param("username", "user")
                        .param("password", "strongPassword")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/"));

    }


}
