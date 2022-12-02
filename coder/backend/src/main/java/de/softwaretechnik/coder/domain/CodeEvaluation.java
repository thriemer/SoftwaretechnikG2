package de.softwaretechnik.coder.domain;

public record CodeEvaluation(
        String taskName,
        String method,
        Object[][] input,
        Object[] expectedOutput
) {
}
