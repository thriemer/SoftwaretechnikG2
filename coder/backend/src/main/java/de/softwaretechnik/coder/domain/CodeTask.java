package de.softwaretechnik.coder.domain;

public record CodeTask(
        String name, //name in database
        String displayName, //name in frontend
        String shortDescription, //description in frontend
        String taskType,
        String taskDescription,
        String codeTemplate
) {
}
