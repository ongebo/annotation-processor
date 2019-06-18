package com.example.processor;

import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@AutoService(Processor.class)
public class FactoryProcessor extends AbstractProcessor {
    private Types types;
    private Elements elements;
    private Filer filer;
    private Messager messager;
    private Map<String, FactoryGroup> factoryGroups;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        types = processingEnv.getTypeUtils();
        elements = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        factoryGroups = new LinkedHashMap<>();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        factoryGroups.clear();
        for (Element element : roundEnv.getElementsAnnotatedWith(Factory.class)) {
            if (element.getKind() != ElementKind.CLASS) {
                error(element, "Only classes can be annotated with @%s", Factory.class.getSimpleName());
                return true;
            }
            TypeElement classElement = (TypeElement) element;
            try {
                FactoryAnnotatedClass annotatedClass = new FactoryAnnotatedClass(classElement);
                if (!isValidElement(annotatedClass)) {
                    return true;
                }
                FactoryGroup factoryGroup = factoryGroups.get(annotatedClass.getQualifiedSuperClassName());
                if (factoryGroup == null) {
                    String groupKey = annotatedClass.getQualifiedSuperClassName();
                    FactoryGroup groupValue = new FactoryGroup(groupKey);
                    factoryGroups.put(groupKey, groupValue);
                    factoryGroup = factoryGroups.get(groupKey);
                }
                factoryGroup.add(annotatedClass);

            } catch (IllegalArgumentException | FactoryGroup.IdExistent e) {
                error(classElement, e.getMessage());
                return true;
            }
        }

        try {
            for (FactoryGroup factoryGroup : factoryGroups.values()) {
                factoryGroup.generateCode(elements, filer);
            }
        } catch (IOException e) {
            error(null, e.getMessage());
        }
        return true;
    }

    private void error(Element element, String message, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(message, args), element);
    }

    private boolean isValidElement(FactoryAnnotatedClass annotatedClass) {
        TypeElement classElement = annotatedClass.getElement();
        if (!classElement.getModifiers().contains(Modifier.PUBLIC)) {
            error(classElement, String.format(
                    "The class %s is not public.", classElement.getQualifiedName().toString()
            ));
            return false;
        }
        if (classElement.getModifiers().contains(Modifier.ABSTRACT)) {
            error(
                    classElement,
                    "The class %s is abstract. You can't annotate abstract classes with @%s",
                    classElement.getQualifiedName().toString(),
                    Factory.class.getSimpleName()
            );
            return false;
        }

        TypeElement superClassElement = elements.getTypeElement(annotatedClass.getQualifiedSuperClassName());

        // Issue: Refactor this if statement.
        if (
                superClassElement.getKind() == ElementKind.INTERFACE
                        && !classElement.getInterfaces().contains(superClassElement.asType())
        ) {
            error(
                    classElement,
                    "The class %s annotated with @%s must implement the interface %s",
                    classElement.getQualifiedName().toString(),
                    Factory.class.getSimpleName(),
                    superClassElement.getQualifiedName().toString()
            );
            return false;
        } else {
            TypeElement current = classElement;
            while (true) {
                TypeMirror typeMirror = current.getSuperclass();
                if (typeMirror.getKind() == TypeKind.NONE) {
                    error(
                            classElement,
                            "The class %s annotated with @%s must inherit from %s",
                            classElement.getQualifiedName().toString(),
                            Factory.class.getSimpleName(),
                            superClassElement.getQualifiedName().toString()
                    );
                    return false;
                } else if (typeMirror.toString().equals(annotatedClass.getQualifiedSuperClassName())) {
                    break;
                } else {
                    current = (TypeElement) types.asElement(typeMirror);
                }
            }
        }
        for (Element element : classElement.getEnclosedElements()) {
            if (element.getKind() == ElementKind.CONSTRUCTOR) {
                ExecutableElement constructor = (ExecutableElement) element;
                if (constructor.getParameters().size() == 0 && constructor.getModifiers().contains(Modifier.PUBLIC)) {
                    return true;
                }
                error(
                        classElement,
                        "The class %s must provide a public empty default constructor",
                        classElement.getQualifiedName().toString()
                );
                return false;
            }
        }
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(Factory.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
