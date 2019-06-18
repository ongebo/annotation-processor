package com.example.processor;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;

public class FactoryAnnotatedClass {
    private TypeElement element;
    private String qualifiedSuperClassName;
    private String simpleSuperClassName;
    private String id;

    public FactoryAnnotatedClass(TypeElement element) throws IllegalArgumentException {
        this.element = element;
        Factory annotation = element.getAnnotation(Factory.class);
        id = annotation.id();
        if (id.isEmpty()) { // Issue: can throw NullPointerException.
            throw new IllegalArgumentException(String.format(
                    "id() in @%s for class %s is empty, not allowed!",
                    Factory.class.getSimpleName(),
                    element.getQualifiedName().toString()
            ));
        }

        try {
            Class<?> cls = annotation.type();
            qualifiedSuperClassName = cls.getCanonicalName();
            simpleSuperClassName = cls.getSimpleName();
        } catch (MirroredTypeException mte) {
            DeclaredType typeMirror = (DeclaredType) mte.getTypeMirror();
            TypeElement typeElement = (TypeElement) typeMirror.asElement();
            qualifiedSuperClassName = typeElement.getQualifiedName().toString();
            simpleSuperClassName = typeElement.getSimpleName().toString();
        }
    }

    public TypeElement getElement() {
        return element;
    }

    public String getQualifiedSuperClassName() {
        return qualifiedSuperClassName;
    }

    public String getSimpleSuperClassName() {
        return simpleSuperClassName;
    }

    public String getId() {
        return id;
    }
}
