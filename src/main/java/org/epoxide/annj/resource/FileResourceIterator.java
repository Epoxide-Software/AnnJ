package org.epoxide.annj.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.epoxide.annj.filter.IFilter;

public class FileResourceIterator extends ResourceIterator {

    private final List<File> files;
    private int index;

    public FileResourceIterator (File file, List<IFilter> filters) {

        super(file, filters);
        this.files = new ArrayList<>();
        this.index = 0;
        this.searchDirectory(file);
    }

    private void searchDirectory (File directory) {

        for (final File file : directory.listFiles()) {

            if (file.isDirectory()) {

                this.searchDirectory(file);
                continue;
            }

            if (this.filter(file.getAbsolutePath()))
                continue;

            this.files.add(file);
        }
    }

    @Override
    public boolean hasNext () {

        return this.index < this.files.size();
    }

    @Override
    public InputStream next () {

        final File file = this.files.get(this.index++);

        try {

            return new FileInputStream(file);
        }

        catch (final FileNotFoundException e) {

            throw new MissingResourceException(file);
        }
    }
}
