package de.softwaretechnik.coder.application.evaluation.compiler;

import de.softwaretechnik.coder.application.evaluation.compiler.JavaSourceValidator;
import de.softwaretechnik.coder.domain.CodeSampleSolution;
import de.softwaretechnik.coder.domain.CodeEvaluation;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Test;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static de.softwaretechnik.coder.application.evaluation.compiler.JavaSourceValidator.CODE_MODIFIED_MESSAGE;

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
    void testMethodNotFound() {
        //arrange
        CodeSampleSolution codeSampleSolution = new CodeSampleSolution("add", "notExist",
                new Object[][]{{5, 4}, {11, 11}},
                new Object[]{9, 22}
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
        assertArrayEquals(new CodeEvaluation[]{new CodeEvaluation(false,CODE_MODIFIED_MESSAGE)}, actual);
    }

    @Test
    void testGetClassType_Float() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // arrange
        Float input = 5f;
        Method method = cut.getClass().getDeclaredMethod("getClassType", Object.class);
        method.setAccessible(true);

        // act
        Class<?> result = (Class<?>) method.invoke(cut, input);

        // assert
        assertEquals(float.class, result);
    }

    @Test
    void testGetClassType_Double() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // arrange
        Double input = 5d;
        Method method = cut.getClass().getDeclaredMethod("getClassType", Object.class);
        method.setAccessible(true);

        // act
        Class<?> result = (Class<?>) method.invoke(cut, input);

        // assert
        assertEquals(double.class, result);
    }

    @Test
    void testGetClassType_Boolean() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // arrange
        Boolean input = true;
        Method method = cut.getClass().getDeclaredMethod("getClassType", Object.class);
        method.setAccessible(true);

        // act
        Class<?> result = (Class<?>) method.invoke(cut, input);

        // assert
        assertEquals(boolean.class, result);
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