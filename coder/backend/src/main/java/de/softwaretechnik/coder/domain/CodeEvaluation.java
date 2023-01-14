package de.softwaretechnik.coder.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class CodeEvaluation {

    public static final String ERROR_MESSAGE_TEMPLATE = "Input: \"%s\"\nExpected: \"%s\" but was: \"%s\"";
    public static final String CORRECT_MESSAGE_TEMPLATE = "Input: \"%s\"\nCorrect: \"%s\"";
    boolean correct;
    String message;

}
