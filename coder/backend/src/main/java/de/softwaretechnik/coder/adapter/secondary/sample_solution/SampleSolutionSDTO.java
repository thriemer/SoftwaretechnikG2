package de.softwaretechnik.coder.adapter.secondary.sample_solution;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity(name="SampleSolution")
public class SampleSolutionSDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String taskName;
    String method;

    String input;
    String inputType;
    String expectedOutput;
    String outputType;

}
