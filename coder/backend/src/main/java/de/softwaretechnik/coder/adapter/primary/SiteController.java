package de.softwaretechnik.coder.adapter.primary;

import de.softwaretechnik.coder.application.tasks.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class SiteController {

    @Autowired
    TaskService taskService;

    @GetMapping("/succ")
    String showLogoutSuccessfulPage() {
        return "succ";
    }
    @GetMapping("/")
    String showHomePage(Model model, Principal principal) {
        model.addAttribute("userName", principal.getName());
        model.addAttribute("tasks", taskService.getAllTasks());
        return "index";
    }

}
