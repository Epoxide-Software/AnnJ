package org.epoxide.annj.resource;

import java.io.File;

public class MissingResourceException extends RuntimeException {

    public MissingResourceException (File file) {

        super("Resource not found! " + file.getAbsolutePath());
    }
}
