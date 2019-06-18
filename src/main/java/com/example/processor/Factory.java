package com.example.processor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Factory {
    /**
     * The name of the Factory to be generated.
     * Should be the superclass of the class to which @Factory is applied.
     */
    Class type();

    /**
     * The ID of the annotated class in the generated Factory.
     * It must be unique for all the annotated classes.
     */
    String id();
}
