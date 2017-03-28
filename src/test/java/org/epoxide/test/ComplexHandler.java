package org.epoxide.test;

public class ComplexHandler {

    @TestAnnotation(message = "SubClassStatic")
    public static class SubClassStatic {

        @TestAnnotation(message = "protected integer")
        protected int integer = 10;

        @TestAnnotation(message = "private string")
        private final String string = "hello";

        @TestAnnotation(message = "private static final decimal")
        private static final float decimal = 5f;

        @TestAnnotation(message = "private method")
        private void test () {

        }
    }

    @TestAnnotation(message = "SubClassPublic")
    public class SubClassPublic {

        @TestAnnotation(message = "protected integer")
        protected int integer = 10;

        @TestAnnotation(message = "private string")
        private final String string = "hello";

        @TestAnnotation(message = "private static final decimal")
        private static final float decimal = 5f;

        @TestAnnotation(message = "private method")
        private void test () {

        }
    }

    @TestAnnotation(message = "SubClassPrivate")
    private class SubClassPrivate {

        @TestAnnotation(message = "protected integer")
        protected int integer = 10;

        @TestAnnotation(message = "private string")
        private final String string = "hello";

        @TestAnnotation(message = "private static final decimal")
        private static final float decimal = 5f;

        @TestAnnotation(message = "private method")
        private void test () {

        }
    }
}
