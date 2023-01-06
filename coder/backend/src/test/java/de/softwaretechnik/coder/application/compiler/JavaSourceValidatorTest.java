package de.softwaretechnik.coder.application.compiler;

import de.softwaretechnik.coder.domain.CodeEvaluation;
import de.softwaretechnik.coder.domain.TestResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class JavaSourceValidatorTest {

    private JavaSourceValidator cut = new JavaSourceValidator();

    @Test
    void testAPlusB_correctResult() {
        //arrange
        CodeEvaluation codeEvaluation = new CodeEvaluation("add", "add",
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
        var actual = cut.testProgram(sourceCode, codeEvaluation);
        //assert
        assertArrayEquals(new TestResult[]{new TestResult(true, "Input: \"[5, 4]\"\nCorrect: \"9\""), new TestResult(false, "Input: \"[11, 11]\"\n" +
                "Expected: \"24\" but was: \"22\"")}, actual);
    }

}