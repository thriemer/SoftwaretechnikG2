package de.softwaretechnik.coder.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class TestResult {

    public static final String ERROR_MESSAGE_TEMPLATE = "Expected: \"%s\" but was: \"%s\"";

    boolean correct;
    String errorMessage;

}
