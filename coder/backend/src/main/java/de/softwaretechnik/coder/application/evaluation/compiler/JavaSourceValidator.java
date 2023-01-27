package de.softwaretechnik.coder.application.evaluation.compiler;

import de.softwaretechnik.coder.domain.CodeSampleSolution;
import de.softwaretechnik.coder.domain.CodeEvaluation;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

@Service
public class JavaSourceValidator {

    static final String CODE_MODIFIED_MESSAGE = "The source code was modified in a way so that it can't be evaluated automatically. \n Easy way out is to reset the Task";

    public CodeEvaluation[] testProgram(String source, CodeSampleSolution codeSampleSolution) {
        try {
            Class<?> compiledClazz = new CompilingClassLoader().loadClassFromString(source);
            Class<?>[] parameterTypes = Arrays.stream(codeSampleSolution.input()[0]).map(this::getClassType).toArray(Class[]::new);

            Method toValidate = compiledClazz.getDeclaredMethod(codeSampleSolution.method(), parameterTypes);

            CodeEvaluation[] result = new CodeEvaluation[codeSampleSolution.input().length];
            for (int i = 0; i < codeSampleSolution.input().length; i++) {
                var in = codeSampleSolution.input()[i];
                var out = codeSampleSolution.expectedOutput()[i];
                var actual = toValidate.invoke(null, in);
                boolean correct = out.equals(actual);
                String wrongMessage = CodeEvaluation.ERROR_MESSAGE_TEMPLATE.formatted(Arrays.toString(in), out, actual);
                String correctMessage = CodeEvaluation.CORRECT_MESSAGE_TEMPLATE.formatted(Arrays.toString(in), out);
                result[i] = new CodeEvaluation(correct, correct ? correctMessage : wrongMessage);
            }
            return result;
        } catch (ClassNotFoundException ex) {
            return new CodeEvaluation[]{new CodeEvaluation(false, ex.getMessage())};
        } catch (NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException | NullPointerException ex) {
            return new CodeEvaluation[]{new CodeEvaluation(false, CODE_MODIFIED_MESSAGE)};
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
