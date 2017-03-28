package org.epoxide.annj.filter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public interface IFilter {

    default boolean filter (String className) {

        return false;
    }

    default boolean filter (Method method) {

        return false;
    }

    default boolean filter (Field field) {

        return false;
    }

    default boolean filter (Parameter parameter) {

        return false;
    }
}
