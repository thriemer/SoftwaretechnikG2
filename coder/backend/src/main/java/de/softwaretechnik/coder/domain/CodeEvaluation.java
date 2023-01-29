package de.softwaretechnik.coder.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class CodeEvaluation {

    public static final String ERROR_MESSAGE_TEMPLATE = "Input: \"%s\"\nExpected: \"%s\" but was: \"%s\"";
    public static final String CORRECT_MESSAGE_TEMPLATE = "Input: \"%s\"\nCorrect: \"%s\"";

    public CodeEvaluation(boolean correct, String message) {
        this.correct = correct;
        this.message = message;
    }

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    long submittedSolutionId;
    boolean correct;
    String message;

}
