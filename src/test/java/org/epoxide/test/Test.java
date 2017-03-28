package org.epoxide.test;

import java.util.ArrayList;

import org.epoxide.annj.AnnotationDiscoverer;
import org.epoxide.annj.filter.IFilter;
import org.epoxide.annj.listener.IListener;

@TestAnnotation(message = "Main Class")
public class Test {

    @TestAnnotation(message = "Random Integer")
    private static int randoInt = 5;

    private static int test = 0;

    @TestAnnotation(message = "Main Method")
    public static void main (String... args) {

        final AnnotationDiscoverer disco = new AnnotationDiscoverer(new ArrayList<IFilter>(), new ArrayList<IListener>());
        disco.addListener(new TestListener());
        disco.discover(true, false);
    }
}