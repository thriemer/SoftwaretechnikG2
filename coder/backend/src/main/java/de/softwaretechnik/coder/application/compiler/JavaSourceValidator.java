package de.softwaretechnik.coder.application.compiler;

import de.softwaretechnik.coder.domain.CodeEvaluation;
import de.softwaretechnik.coder.domain.TestResult;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

@Service
public class JavaSourceValidator {

    public TestResult[] testProgram(String source, CodeEvaluation codeEvaluation) {
        try {
            Class<?> compiledClazz = new CompilingClassLoader().loadClassFromString(source);
            Class<?>[] parameterTypes = Arrays.stream(codeEvaluation.input()[0]).map(this::getClassType).toArray(Class[]::new);

            Method toValidate = compiledClazz.getDeclaredMethod(codeEvaluation.method(), parameterTypes);

            TestResult[] result = new TestResult[codeEvaluation.input().length];
            for (int i = 0; i < codeEvaluation.input().length; i++) {
                var in = codeEvaluation.input()[i];
                var out = codeEvaluation.expectedOutput()[i];
                var actual = toValidate.invoke(null, in);
                boolean correct = out.equals(actual);
                String wrongMessage = TestResult.ERROR_MESSAGE_TEMPLATE.formatted(Arrays.toString(in), out, actual);
                String correctMessage = TestResult.CORRECT_MESSAGE_TEMPLATE.formatted(Arrays.toString(in), out);
                result[i] = new TestResult(correct, correct ? correctMessage : wrongMessage);
            }
            return result;
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException ex) {
            throw new TemplateModifiedException("The source code was modified", ex);
        }
    }

    private Class<?> getClassType(Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.equals(Integer.class)) {
            clazz = int.class;
        } else if (clazz.equals(Float.class)) {
            clazz = float.class;
        } else if (clazz.equals(Double.class)) {
            clazz = double.class;
        } else if (clazz.equals(Boolean.class)) {
            clazz = boolean.class;
        }
        return clazz;
    }

}
