package de.softwaretechnik.coder.domain;

public record CodeSampleSolution(
        String taskName,
        String method,
        Object[][] input,
        Object[] expectedOutput
) {

    private static final String SEPARATOR = "|";
    private static final String SEPARATOR_REGEX = "\\|";

    public String toCSVString() {
        StringBuilder csv = new StringBuilder(method + "\n");
        //input types + output type, comma separated
        for (Object in : input[0]) {
            csv.append(serializeType(in.getClass())).append(SEPARATOR);
        }
        csv.append(serializeType(expectedOutput[0].getClass())).append("\n");

        for (int i = 0; i < input.length; i++) {
            for (Object in : input[i]) {
                csv.append(in.toString()).append(SEPARATOR);
            }
            csv.append(expectedOutput[i].toString()).append("\n");
        }
        return csv.toString();
    }


    public static CodeSampleSolution fromCSVString(String taskName, String csv) {
        String[] lines = csv.split("\n");
        int testLength = lines.length - 2;
        String method = lines[0];
        Object[][] input = new Object[testLength][];
        Object[] expectedOutput = new Object[testLength];
        String[] types = lines[1].split(SEPARATOR_REGEX);
        for (int i = 2; i < lines.length; i++) {
            String[] objects = lines[i].split(SEPARATOR_REGEX);
            var newInputLine = new Object[objects.length - 1];
            for (int j = 0; j < newInputLine.length; j++) {
                newInputLine[j] = deserialize(objects[j], types[j]);
            }
            input[i - 2] = newInputLine;
            expectedOutput[i - 2] = deserialize(objects[objects.length - 1], types[types.length - 1]);
        }
        return new CodeSampleSolution(taskName, method, input, expectedOutput);
    }

    private static String serializeType(Class<?> clazz) {
        if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
            return "int";
        }
        if (clazz.equals(String.class)) {
            return "string";
        }
        if (clazz.equals(double.class) || clazz.equals(Double.class)) {
            return "double";
        }
        if (clazz.equals(float.class) || clazz.equals(Float.class)) {
            return "float";
        }
        return "not-supported";
    }


    private static Object deserialize(String value, String classString) {
        if (classString.equals("int")) {
            return Integer.parseInt(value);
        }
        if (classString.equals("string")) {
            return value;
        }
        if (classString.equals("double")) {
            return Double.parseDouble(value);
        }
        if (classString.equals("float")) {
            return Float.parseFloat(value);
        }
        return "not-supported";
    }


}
