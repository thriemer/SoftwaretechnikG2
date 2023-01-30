package de.softwaretechnik.coder.adapter.secondary.sample_solution;

import de.softwaretechnik.coder.domain.CodeSampleSolution;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SampleSolutionConverterTest {

    @Test
    void testConversion_roundTrip_objectsAreEqual() {
        //arrange
        SampleSolutionConverter cut = new SampleSolutionConverter();
        var original = new CodeSampleSolution("addTwoNumbers", "add",
                new Object[][]{{5, 4}, {11, 12}},
                new Object[]{9, 23}
        );

        //act
        var adapterObject = cut.convertToAdapterObject(original);
        var actual = cut.convertToDomainObject(adapterObject);

        //assert
        assertEquals(original.taskName(), actual.taskName());
        assertEquals(original.method(), actual.method());
        assertArrayEquals(original.expectedOutput(), actual.expectedOutput());
        assertArrayEquals(original.input(), actual.input());
    }

}