package de.softwaretechnik.coder.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity(name = "tasks")
public class CodeTask {

    public static String CODE_TASK_TYPE = "coding";
    public static String OUTPUT_TASK_TYPE = "write_output";

    @Id
    String name; //name in database
    String displayName; //name in frontend
    String shortDescription; //description in frontend
    String taskType;
    String taskDescription;
    String codeTemplate;
}
