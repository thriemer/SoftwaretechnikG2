package de.softwaretechnik.coder.application.evaluation.compiler;

import de.softwaretechnik.coder.application.evaluation.compiler.JavaSourceValidator;
import de.softwaretechnik.coder.domain.CodeSampleSolution;
import de.softwaretechnik.coder.domain.CodeEvaluation;
import org.junit.jupiter.api.Test;

import static de.softwaretechnik.coder.application.evaluation.compiler.JavaSourceValidator.CODE_MODIFIED_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class JavaSourceValidatorTest {

    private final JavaSourceValidator cut = new JavaSourceValidator();

    @Test
    void testAPlusB_correctResult() {
        //arrange
        CodeSampleSolution codeSampleSolution = new CodeSampleSolution("add", "add",
                new Object[][]{{5, 4}, {11, 11}},
                new Object[]{9, 24}
        );
        String sourceCode = """
                public class Calculator {
                    public static int add(int a, int b){
                        return a+b;
                    }
                }
                """;
        //act
        var actual = cut.testProgram(sourceCode, codeSampleSolution);
        //assert
        assertArrayEquals(new CodeEvaluation[]{new CodeEvaluation(true, "Input: \"[5, 4]\"\nCorrect: \"9\""), new CodeEvaluation(false, "Input: \"[11, 11]\"\n" +
                "Expected: \"24\" but was: \"22\"")}, actual);
    }


    @Test
    void testAPlusB_invalidSource_compilationFailed() {
        //arrange
        CodeSampleSolution codeSampleSolution = new CodeSampleSolution("add", "add",
                new Object[][]{{5, 4}, {11, 11}},
                new Object[]{9, 24}
        );
        String sourceCode = """
                public class Calculator {
                    public static int add(int a, int b){
                        return a+b
                    }
                }
                """;
        //act
        var actual = cut.testProgram(sourceCode, codeSampleSolution);
        //assert
        assertArrayEquals(new CodeEvaluation[]{new CodeEvaluation(false,"Unable to load: Calculator. Reason = compilation failed.")}, actual);
    }


    @Test
    void testAPlusB_invalidSource_reflectionFailed() {
        //arrange
        CodeSampleSolution codeSampleSolution = new CodeSampleSolution("add", "add",
                new Object[][]{{5, 4}, {11, 11}},
                new Object[]{9, 24}
        );
        String sourceCode = """
                public class Calculator {
                    private int add(int a, int b){
                        return a+b;
                    }
                }
                """;
        //act
        var actual = cut.testProgram(sourceCode, codeSampleSolution);
        //assert
        assertArrayEquals(new CodeEvaluation[]{new CodeEvaluation(false,CODE_MODIFIED_MESSAGE)}, actual);
    }

}