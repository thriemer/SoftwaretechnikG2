package de.softwaretechnik.coder.adapter.secondary.sample_solution;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import de.softwaretechnik.coder.domain.CodeSampleSolution;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class SampleSolutionConverter {

    private final ObjectMapper objectMapper;

    public SampleSolutionConverter() {
        objectMapper = new ObjectMapper();
    }

    public CodeSampleSolution convertToDomainObject(SampleSolutionSDTO sampleSolutionSDTO) {
        if(sampleSolutionSDTO==null)return null;
        try {
            Class<?> expectedOutputTypeClass = objectMapper.readValue(sampleSolutionSDTO.getOutputType(), Class.class);
            Class<?>[] inputType = objectMapper.readValue(sampleSolutionSDTO.getInputType(), Class[].class);

            ArrayNode inputArrayNode = (ArrayNode) objectMapper.readTree(sampleSolutionSDTO.getInput());

            Object[][] input = new Object[inputArrayNode.size()][inputType.length];

            for (int outer = 0; outer < input.length; outer++) {
                ArrayNode innerNode = (ArrayNode) inputArrayNode.get(outer);
                for (int inner = 0; inner < input[0].length; inner++) {
                    input[outer][inner] = objectMapper.readValue(innerNode.get(inner).toPrettyString(), inputType[inner]);
                }
            }

            Object[] expectedOutput = (Object[]) objectMapper.readValue(sampleSolutionSDTO.getExpectedOutput(), expectedOutputTypeClass.arrayType());

            return new CodeSampleSolution(
                    sampleSolutionSDTO.getTaskName(),
                    sampleSolutionSDTO.getMethod(),
                    input,
                    expectedOutput
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public SampleSolutionSDTO convertToAdapterObject(CodeSampleSolution codeSampleSolution) {
        try {
            Class<?> expectedOutputTypeClass = codeSampleSolution.expectedOutput()[0].getClass();
            Class<?>[] inputTypeClasses = Arrays.stream(codeSampleSolution.input()[0])
                    .map(Object::getClass)
                    .toArray(Class[]::new);
            String input = objectMapper.writeValueAsString(codeSampleSolution.input());
            String inputType = objectMapper.writeValueAsString(inputTypeClasses);
            String expectedOutput = objectMapper.writeValueAsString(codeSampleSolution.expectedOutput());
            String expectedOutputType = objectMapper.writeValueAsString(expectedOutputTypeClass);
            return new SampleSolutionSDTO(
                    -1L,
                    codeSampleSolution.taskName(),
                    codeSampleSolution.method(),
                    input,
                    inputType,
                    expectedOutput,
                    expectedOutputType
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
