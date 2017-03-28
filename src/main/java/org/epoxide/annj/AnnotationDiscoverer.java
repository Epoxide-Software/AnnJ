package org.epoxide.annj;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.epoxide.annj.filter.BlacklistFilter;
import org.epoxide.annj.filter.ClassFilter;
import org.epoxide.annj.filter.IFilter;
import org.epoxide.annj.listener.IListener;
import org.epoxide.annj.resource.FileResourceIterator;
import org.epoxide.annj.resource.JarResourceIterator;

import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.Annotation;

public class AnnotationDiscoverer {

    private final List<IFilter> filters;
    private final List<IListener> listeners;

    public AnnotationDiscoverer (List<IFilter> filters, List<IListener> listeners) {

        this.filters = filters;
        this.filters.add(new ClassFilter());
        this.filters.add(BlacklistFilter.COMMON_INSTANCE);
        this.listeners = listeners;
    }

    public boolean addFilter (IFilter filter) {

        return this.filters.add(filter);
    }

    public boolean removeFilter (IFilter filter) {

        return this.filters.remove(filter);
    }

    public boolean addListener (IListener listener) {

        return this.listeners.add(listener);
    }

    public boolean removeListener (IListener listener) {

        return this.listeners.remove(listener);
    }

    public void discover (boolean includeInvisible, boolean excludeVisible) {

        try {

            for (final URL resource : this.getResources()) {

                final Iterator<InputStream> streamIterator = this.getResourceStreams(resource);

                while (streamIterator.hasNext()) {

                    final ClassFile clazz = new ClassFile(new DataInputStream(new BufferedInputStream(streamIterator.next())));

                    this.discoverAnnotations(clazz, includeInvisible, excludeVisible);

                    for (final FieldInfo field : (List<FieldInfo>) clazz.getFields())
                        this.discoverAnnotations(clazz, field, includeInvisible, excludeVisible);

                    for (final MethodInfo method : (List<MethodInfo>) clazz.getMethods())
                        this.discoverAnnotations(clazz, method, includeInvisible, excludeVisible);
                }
            }
        }

        catch (IOException | URISyntaxException e) {

            e.printStackTrace();
        }
    }

    private void discoverAnnotations (ClassFile clazz, boolean includeInvisible, boolean excludeVisible) {

        this.discoverAnnotations(clazz, clazz, includeInvisible, excludeVisible);
    }

    private void discoverAnnotations (ClassFile clazz, Object object, boolean includeInvisible, boolean excludeVisible) {

        final List<Annotation> annotations = new ArrayList<>();

        if (includeInvisible)
            this.getAnnotationsForTag(object, AnnotationsAttribute.invisibleTag, annotations);

        if (!excludeVisible)
            this.getAnnotationsForTag(object, AnnotationsAttribute.visibleTag, annotations);

        for (final IListener listener : this.listeners)
            for (final Annotation annotation : annotations)
                if (object instanceof ClassFile)
                    listener.handleClass(clazz, annotation);

                else if (object instanceof FieldInfo)
                    listener.handleField(clazz, (FieldInfo) object, annotation);

                else if (object instanceof MethodInfo)
                    listener.handleMethod(clazz, (MethodInfo) object, annotation);
    }

    private void getAnnotationsForTag (Object object, String name, List<Annotation> annotations) {

        AnnotationsAttribute attribute = null;

        // ClassFile, FieldInfo and MethodInfo do not share a common type, so this is needed.
        if (object instanceof ClassFile)
            attribute = (AnnotationsAttribute) ((ClassFile) object).getAttribute(name);

        else if (object instanceof FieldInfo)
            attribute = (AnnotationsAttribute) ((FieldInfo) object).getAttribute(name);

        else if (object instanceof MethodInfo)
            attribute = (AnnotationsAttribute) ((MethodInfo) object).getAttribute(name);

        if (attribute != null)
            Collections.addAll(annotations, attribute.getAnnotations());
    }

    public List<URL> getResources () {

        final List<URL> urls = new ArrayList<>();
        this.addClasspathResources(urls);

        if (urls.size() == 0)
            this.addSystemResouces(urls);
        return urls;
    }

    private void addClasspathResources (List<URL> resourceList) {

        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        while (loader != null) {

            if (loader instanceof URLClassLoader)
                Collections.addAll(resourceList, ((URLClassLoader) loader).getURLs());

            loader = loader.getParent();
        }
    }

    private void addSystemResouces (List<URL> resourceList) {

        final String classpath = System.getProperty("java.class.path");
        final StringTokenizer tokenizer = new StringTokenizer(classpath, File.pathSeparator);

        while (tokenizer.hasMoreTokens()) {

            final String path = tokenizer.nextToken();
            final File file = new File(path);

            if (!file.exists())
                // TODO skip
                continue;
        
            try {
                resourceList.add(file.toURI().toURL());
            }
            catch (final MalformedURLException e) {

                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private Iterator<InputStream> getResourceStreams (URL url) throws FileNotFoundException, IOException, URISyntaxException {

        String path = url.toString();

        if (path.endsWith("!/")) {

            path = path.substring(4);
            path = path.substring(0, path.length() - 2);
            url = new URL(path);
        }

        if (!path.endsWith("/"))
            return new JarResourceIterator(new File(url.toURI()), this.filters);

        else {

            if (!url.getProtocol().equals("file"))
                throw new IOException("Unsupported Protocol: " + url.getProtocol());

            final File file = new File(url.toURI());

            if (file.isDirectory())
                return new FileResourceIterator(file, this.filters);

            else
                return new JarResourceIterator(file, this.filters);
        }
    }
}
