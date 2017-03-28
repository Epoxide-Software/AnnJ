package org.epoxide.annj.listener;

import javassist.bytecode.ClassFile;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.Annotation;

public interface IListener {

    default void handleClass (ClassFile clazz, Annotation annotation) {

    }

    default void handleField (ClassFile clazz, FieldInfo field, Annotation annotation) {

    }

    default void handleMethod (ClassFile clazz, MethodInfo method, Annotation annotation) {

    }
}