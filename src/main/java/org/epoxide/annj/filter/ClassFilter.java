package org.epoxide.annj.filter;

public class ClassFilter implements IFilter {

    @Override
    public boolean filter (String className) {

        return !className.endsWith(".class");
    }
}