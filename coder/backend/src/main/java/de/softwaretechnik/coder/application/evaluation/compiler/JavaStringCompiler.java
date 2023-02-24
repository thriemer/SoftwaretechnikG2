package de.softwaretechnik.coder.application.evaluation.compiler;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

enum JavaStringCompiler {
    INSTANCE;

    private JavaCompiler compiler;
    private StandardJavaFileManager manager;

    private static final Logger logger = Logger.getLogger(JavaStringCompiler.class.getName());

    JavaStringCompiler() {
        this.compiler = ToolProvider.getSystemJavaCompiler();
        this.manager = compiler.getStandardFileManager(null, null, null);
    }

    // class to represent a string object as a source file
    class StringCodeObject extends SimpleJavaFileObject {
        private String code;

        StringCodeObject(final String name, final String code) {
            super(URI.create("string:///" + name.replace('.', File.separatorChar) + Kind.SOURCE.extension), Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return this.code;
        }
    }

    // Compile the Java code stored inside the string
    public boolean compileStringCode(final String name, final String code) throws ClassNotFoundException {
        logger.info("Compiling: " + name);

        boolean result = false;
        StringCodeObject source = new StringCodeObject(name, code);

        DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<>();
        result = compiler.getTask(null, manager, collector, null, null, List.of(source)).call();

        if (!result) {
            StringBuilder compilationErrorsBuilder = new StringBuilder();
            for (Diagnostic<? extends JavaFileObject> d : collector.getDiagnostics()) {
                logger.info(d.getMessage(Locale.ENGLISH));
                compilationErrorsBuilder.append("Error at line ").append(d.getLineNumber()).append(": ").append(d.getMessage(Locale.ENGLISH)).append("\n");
            }
            throw new ClassNotFoundException("Unable to load: " + name + ". Reason = compilation failed.\n" + compilationErrorsBuilder);
        }

        try {
            manager.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

        logger.info("Finished compiling: " + name);

        return result;
    }
}
