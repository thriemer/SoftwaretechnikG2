package de.softwaretechnik.coder.application;

import de.softwaretechnik.coder.adapter.secondary.UserRepository;
import de.softwaretechnik.coder.adapter.secondary.TaskRepository;
import de.softwaretechnik.coder.application.login.UserService;
import de.softwaretechnik.coder.application.tasks.TaskService;
import de.softwaretechnik.coder.domain.CodeSampleSolution;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@Profile("!prod")
@AllArgsConstructor
public class DBFiller {

    private final UserRepository userRepository;
    private final UserService userService;

    private final TaskRepository taskRepository;

    private final TaskService taskService;
    private final DBAbstraction dbAbstraction;

    @PostConstruct
    void initUsers() {
        if (userRepository.findByUserName("user").isEmpty()) {
            userService.createUser("user", "password");
        }
        if (userRepository.findByUserName("admin").isEmpty()) {
            userService.createUser("admin", "password");
        }
    }

    @PostConstruct
    void initTasks() {
        if (taskRepository.findByName("addTwoNumbers").isEmpty()) {
            taskService.createTask("addTwoNumbers", "Intro to functions", "Complete the function so that it adds two numbers", "coding", "Complete the function so that it adds two numbers.", """
                    public class Calculator {
                        public static int add(int a, int b){
                            return /*TODO*/;
                        }
                    }""");
        }
        if (taskRepository.findByName("reverseString").isEmpty()) {
            taskService.createTask("reverseString", "Reverse string", "Complete the function so that it reverses the String", "coding", "Complete the function so that it reverses the String", """
                    public class StringUtil {
                        public static String reverseString(String s){
                            return "TODO";
                        }
                    }""");
        }
    }

    @PostConstruct
    void initSampleSolution() {
        List<CodeSampleSolution> allEvaluations = List.of(
                new CodeSampleSolution("addTwoNumbers", "add",
                        new Object[][]{{5, 4}, {11, 12}},
                        new Object[]{9, 23}
                ),
                new CodeSampleSolution("reverseString", "reverseString",
                        new Object[][]{{"HelloWorld!"}, {"I've seen enough of Ba Sing Sei. And I can't even see! ~ Toph"}},
                        new Object[]{"!dlroWolleH", "hpoT ~ !ees neve t'nac I dnA .ieS gniS aB fo hguone nees ev'I"}
                )
        );
        allEvaluations.forEach(dbAbstraction::saveSampleSolution);
    }

}
