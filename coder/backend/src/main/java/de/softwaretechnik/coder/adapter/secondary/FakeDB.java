package de.softwaretechnik.coder.adapter.secondary;

import de.softwaretechnik.coder.application.DBAbstraction;
import de.softwaretechnik.coder.domain.CodeEvaluation;
import de.softwaretechnik.coder.domain.Task;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FakeDB implements DBAbstraction {

    private final List<Task> allTasks = List.of(
            new Task("add", "Complete the function so that it adds two numbers.", """
                    public class Calculator {
                        public static int add(int a, int b){
                            return /*TODO*/;
                        }
                    }
                        """),
            new Task("reverse", "Reverse the String", """
                    public class StringUtil {
                        public static String reverseString(String s){
                            return "TODO";
                        }
                    }
                    """)
    );
    private final List<CodeEvaluation> allEvaluations = List.of(
            new CodeEvaluation("add", "add",
                    new Object[][]{{5, 4}, {11, 12}},
                    new Object[]{9, 23}
            ),
            new CodeEvaluation("reverse", "reverseString",
                    new Object[][]{{"HelloWorld!"}, {"I've seen enough of Ba Sing Sei. And I can't even see! ~ Toph"}},
                    new Object[]{"!dlroWolleH", "hpoT ~ !ees neve t'nac I dnA .ieS gniS aB fo hguone nees ev'I"}
            )
    );

    public List<String> getTaskNames() {
        return allTasks.stream().map(Task::name).toList();
    }

    public Task getTaskByName(String name) {
        return allTasks.stream().filter(t -> t.name().equals(name)).findFirst().orElse(null);
    }

    public CodeEvaluation getCodeEvaluationByName(String taskName) {
        return allEvaluations.stream().filter(e -> e.taskName().equals(taskName)).findFirst().orElse(null);
    }

    public Task[] getAllTasks() {
        return allTasks.toArray(Task[]::new);
    }

}
