package org.epoxide.annj.resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.epoxide.annj.filter.IFilter;

public class JarResourceIterator extends ResourceIterator {

    private final JarFile jar;
    private final List<JarEntry> files;
    private int index = 0;

    public JarResourceIterator (File file, List<IFilter> filters) throws FileNotFoundException, IOException {

        super(file, filters);
        this.jar = new JarFile(file);
        this.files = new ArrayList<>();
        this.search();
    }

    private void search () {

        final Enumeration<JarEntry> entries = this.jar.entries();
        while (entries.hasMoreElements()) {

            final JarEntry entry = entries.nextElement();

            if (this.filter(entry.getName()))
                continue;

            this.files.add(entry);
        }
    }

    @Override
    public boolean hasNext () {

        return this.index < this.files.size();
    }

    @Override
    public InputStream next () {

        final JarEntry file = this.files.get(this.index++);
        try {
            return this.jar.getInputStream(file);
        }
        catch (final IOException e) {

            // TOOD fix
            return null;
        }
    }

}
