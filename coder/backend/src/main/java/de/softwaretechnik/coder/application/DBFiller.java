package de.softwaretechnik.coder.application;

import de.softwaretechnik.coder.adapter.secondary.UserRepository;
import de.softwaretechnik.coder.adapter.secondary.TaskRepository;
import de.softwaretechnik.coder.application.login.UserService;
import de.softwaretechnik.coder.application.tasks.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Profile("!prod")
@AllArgsConstructor
public class DBFiller {

    private final UserRepository userRepository;
    private final UserService userService;

    private final TaskRepository taskRepository;

    private final TaskService taskService;

    @PostConstruct
    void initUsers(){
        if(userRepository.findByUserName("user").isEmpty()){
            userService.createUser("user","password");
        }
        if(userRepository.findByUserName("admin").isEmpty()){
            userService.createUser("admin","password");
        }
    }

    @PostConstruct
    void initTasks(){
        if(taskRepository.findByName("addTwoNumbers").isEmpty()){
            taskService.createTask("addTwoNumbers","addTwoNumbers","Complete the function so that it adds two numbers","coding","Complete the function so that it adds two numbers","public class Calculator {\n" +
                    "                        public static int add(int a, int b){\n" +
                    "                            return /*TODO*/;\n" +
                    "                        }\n" +
                    "                    }");
        }
        if(taskRepository.findByName("reverseString").isEmpty()){
            taskService.createTask("reverseString","reverseString","Complete the function so that it reverses the String","coding","Complete the function so that it reverses the String","public class StringUtil {\n" +
                    "                        public static String reverseString(String s){\n" +
                    "                            return \"TODO\";\n" +
                    "                        }\n" +
                    "                    }");
        }
    }

}
