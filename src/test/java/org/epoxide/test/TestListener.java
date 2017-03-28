package org.epoxide.test;

import org.epoxide.annj.listener.IListener;

import javassist.ClassPool;
import javassist.bytecode.ClassFile;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.NoSuchClassError;

public class TestListener implements IListener {

    public static final ClassPool POOL = ClassPool.getDefault();

    @Override
    public void handleClass (ClassFile clazz, Annotation annotation) {

        this.printMessage("Found class annotation! " + clazz.getName(), annotation);
    }

    @Override
    public void handleField (ClassFile clazz, FieldInfo field, Annotation annotation) {

        this.printMessage("Found field annotation! " + field.getName(), annotation);
    }

    @Override
    public void handleMethod (ClassFile clazz, MethodInfo method, Annotation annotation) {

        this.printMessage("Found method annotation! " + method.getName(), annotation);
    }

    private void printMessage (String nametype, Annotation annotation) {

        Object obj = null;
        try {
            obj = annotation.toAnnotationType(this.getClass().getClassLoader(), POOL);
        }
        catch (ClassNotFoundException | NoSuchClassError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (obj instanceof TestAnnotation)
            System.out.println(nametype + " - " + ((TestAnnotation) obj).message());
    }
}
