package de.softwaretechnik.coder.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CodeSampleSolutionTest {

    @Test
    void serialize_deserialize_sameResult() {
        var sol1 = new CodeSampleSolution("addTwoNumbers", "add",
                new Object[][]{{5, 4}, {11, 12}},
                new Object[]{9, 23}
        );
        String csv = sol1.toCSVString();
        var actual = CodeSampleSolution.fromCSVString("addTwoNumbers", csv);

        assertEquals(sol1.taskName(), actual.taskName());
        assertEquals(sol1.method(), actual.method());

        for (int i = 0; i < sol1.input().length; i++) {
            assertArrayEquals(sol1.input()[i], actual.input()[i]);
        }

        assertArrayEquals(sol1.expectedOutput(), actual.expectedOutput());
    }

}