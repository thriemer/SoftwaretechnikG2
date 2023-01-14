package de.softwaretechnik.coder.adapter.secondary;

import de.softwaretechnik.coder.application.DBAbstraction;
import de.softwaretechnik.coder.domain.CodeSampleSolution;
import de.softwaretechnik.coder.domain.CodeTask;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FakeDB implements DBAbstraction {

    private final List<CodeTask> allCodeTasks = List.of(
            new CodeTask("add", "Complete the function so that it adds two numbers.", """
                    public class Calculator {
                        public static int add(int a, int b){
                            return /*TODO*/;
                        }
                    }
                        """),
            new CodeTask("reverse", "Reverse the String", """
                    public class StringUtil {
                        public static String reverseString(String s){
                            return "TODO";
                        }
                    }
                    """)
    );
    private final List<CodeSampleSolution> allEvaluations = List.of(
            new CodeSampleSolution("add", "add",
                    new Object[][]{{5, 4}, {11, 12}},
                    new Object[]{9, 23}
            ),
            new CodeSampleSolution("reverse", "reverseString",
                    new Object[][]{{"HelloWorld!"}, {"I've seen enough of Ba Sing Sei. And I can't even see! ~ Toph"}},
                    new Object[]{"!dlroWolleH", "hpoT ~ !ees neve t'nac I dnA .ieS gniS aB fo hguone nees ev'I"}
            )
    );

    public List<String> getTaskNames() {
        return allCodeTasks.stream().map(CodeTask::name).toList();
    }

    public CodeTask getTaskByName(String name) {
        return allCodeTasks.stream().filter(t -> t.name().equals(name)).findFirst().orElse(null);
    }

    public CodeSampleSolution getCodeSampleSolutionByName(String taskName) {
        return allEvaluations.stream().filter(e -> e.taskName().equals(taskName)).findFirst().orElse(null);
    }

    public CodeTask[] getAllTasks() {
        return allCodeTasks.toArray(CodeTask[]::new);
    }

}
