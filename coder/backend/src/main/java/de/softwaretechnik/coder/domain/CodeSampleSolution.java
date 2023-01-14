package de.softwaretechnik.coder.domain;

public record CodeSampleSolution(
        String taskName,
        String method,
        Object[][] input,
        Object[] expectedOutput
) {
}
