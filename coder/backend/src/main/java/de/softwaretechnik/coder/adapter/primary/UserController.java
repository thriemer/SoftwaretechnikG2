package de.softwaretechnik.coder.adapter.primary;

import de.softwaretechnik.coder.application.login.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    String showLoginPage() {
        return "login";
    }

    @GetMapping("/registration")
    String showRegistrationPage() {
        return "registration";
    }

    @PostMapping("/registration")
    String registerUser(@RequestParam String username, @RequestParam String password, Model model, HttpServletRequest request) throws ServletException {
        try {
            var user = userService.loadUserByUsername(username);
            //user already exists
            model.addAttribute("userAlreadyExists", true);
            return "registration";
        } catch (UsernameNotFoundException ex) {
            userService.createUser(username, password, false);
            request.login(username, password);
            return "redirect:/";
        }
    }

}
