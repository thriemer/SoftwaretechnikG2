package de.softwaretechnik.coder.application.evaluation.compiler;

public class TemplateModifiedException extends RuntimeException{
    public TemplateModifiedException(String message, Exception ex) {
        super(message, ex);
    }
}
