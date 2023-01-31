package de.softwaretechnik.coder.application;

import de.softwaretechnik.coder.adapter.secondary.UserRepository;
import de.softwaretechnik.coder.adapter.secondary.TaskRepository;
import de.softwaretechnik.coder.application.login.UserService;
import de.softwaretechnik.coder.application.tasks.TaskService;
import de.softwaretechnik.coder.domain.CodeSampleSolution;
import de.softwaretechnik.coder.domain.CodeTask;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

import static de.softwaretechnik.coder.domain.CodeTask.CODE_TASK_TYPE;
import static de.softwaretechnik.coder.domain.CodeTask.OUTPUT_TASK_TYPE;

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
            taskService.createTask("addTwoNumbers", "Intro to functions", "Complete the function so that it adds two numbers", CODE_TASK_TYPE, "Complete the function so that it adds two numbers.", """
                    public class Calculator {
                        public static int add(int a, int b){
                            return /*TODO*/;
                        }
                    }""");
        }
        if (taskRepository.findByName("reverseString").isEmpty()) {
            taskService.createTask("reverseString", "Reverse string", "Complete the function so that it reverses the String", CODE_TASK_TYPE, "Complete the function so that it reverses the String", """
                    public class StringUtil {
                        public static String reverseString(String s){
                            return "TODO";
                        }
                    }""");
        }
        if (taskRepository.findByName("helloWorld").isEmpty()) {
            taskService.createTask("helloWorld", "Hello World!", "What is the console output of this code", OUTPUT_TASK_TYPE, "What is the console output of this code. You can ignore newlines.", """
                    public class HelloJava {
                        public static void main(String args[]){
                            System.out.println("Hello fellow Java learner");
                        }
                    }""");
        }

    }

    @PostConstruct
    void initSampleSolution() {

        var sol1 = new CodeSampleSolution("addTwoNumbers", "add",
                new Object[][]{{5, 4}, {11, 12}},
                new Object[]{9, 23}
        );
        var sol2 = new CodeSampleSolution("reverseString", "reverseString",
                new Object[][]{{"HelloWorld!"}, {"I've seen enough of Ba Sing Sei. And I can't even see! ~ Toph"}},
                new Object[]{"!dlroWolleH", "hpoT ~ !ees neve t'nac I dnA .ieS gniS aB fo hguone nees ev'I"}
        );

        var sol3 = new CodeSampleSolution("helloWorld", "main",
                new Object[][]{{}},
                new Object[]{"Hello fellow Java learner"}
        );

        if (dbAbstraction.getCodeSampleSolutionByName(sol1.taskName()) == null) {
            dbAbstraction.saveSampleSolution(sol1);
        }

        if (dbAbstraction.getCodeSampleSolutionByName(sol2.taskName()) == null) {
            dbAbstraction.saveSampleSolution(sol2);
        }

        if (dbAbstraction.getCodeSampleSolutionByName(sol3.taskName()) == null) {
            dbAbstraction.saveSampleSolution(sol3);
        }

    }

}
