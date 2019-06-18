package com.example.processor;

import com.squareup.javawriter.JavaWriter;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;

public class FactoryGroup {
    private String groupName;
    private Map<String, FactoryAnnotatedClass> annotatedClassMap;
    private static final String SUFFIX = "Factory";

    public static class IdExistent extends Exception {
        public IdExistent(String message) {
            super(message);
        }
    }

    public FactoryGroup(String groupName) {
        this.groupName = groupName;
        annotatedClassMap = new LinkedHashMap<>();
    }

    public void add(FactoryAnnotatedClass annotatedClass) throws IdExistent {
        FactoryAnnotatedClass existingClass = annotatedClassMap.get(annotatedClass.getId());
        if (existingClass != null) {
            throw new IdExistent(String.format("Duplicate id: %s", annotatedClass.getId()));
        }
        annotatedClassMap.put(annotatedClass.getId(), annotatedClass);
    }

    public void generateCode(Elements elements, Filer filer) throws IOException {
        TypeElement typeElement = elements.getTypeElement(groupName);
        String sourceFileName = typeElement.getSimpleName() + SUFFIX;

        JavaFileObject javaFileObject = filer.createSourceFile(sourceFileName);
        JavaWriter writer = new JavaWriter(javaFileObject.openWriter());

        PackageElement packageElement = elements.getPackageOf(typeElement);
        if (packageElement.isUnnamed()) {
            writer.emitPackage("");
        } else {
            writer.emitPackage(packageElement.getQualifiedName().toString());
            writer.emitEmptyLine();
        }

        writer.beginType(sourceFileName, "class", EnumSet.of(Modifier.PUBLIC));
        writer.emitEmptyLine();

        writer.beginMethod(groupName, "create", EnumSet.of(Modifier.PUBLIC), "String", "id");

        writer.beginControlFlow("if (id == null) ");
        writer.emitStatement("throw new IllegalArgumentException(\"id is null\")");
        writer.endControlFlow();

        for (FactoryAnnotatedClass annotatedClass : annotatedClassMap.values()) {
            writer.beginControlFlow("if (\"%s\".equals(id))", annotatedClass.getId());
            writer.emitStatement("return new %s()", annotatedClass.getElement().getSimpleName().toString());
            writer.endControlFlow();
            writer.emitEmptyLine();
        }

        writer.emitStatement("throw new IllegalArgumentException(\"Unknown id = \" + id)");
        writer.endMethod();
        writer.close();
    }
}
