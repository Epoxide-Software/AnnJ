package org.epoxide.annj.resource;

import java.io.File;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.epoxide.annj.filter.IFilter;

public abstract class ResourceIterator implements Iterator<InputStream> {

    private final File file;
    private final List<IFilter> filters;

    public ResourceIterator (File file, List<IFilter> filters) {

        this.file = file;
        this.filters = filters;
    }

    public boolean filter (String name) {

        for (final IFilter filter : this.filters)
            if (filter.filter(name))
                return true;
            
        return false;
    }

    public File getFile () {

        return this.file;
    }
}