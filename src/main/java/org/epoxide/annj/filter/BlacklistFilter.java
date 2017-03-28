package org.epoxide.annj.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlacklistFilter implements IFilter {

    public static final String[] COMMON_BLACKLIST = { "javassist", "com/sun", "sun", "javafx", "jdk", "netscape" };
    public static final BlacklistFilter COMMON_INSTANCE = new BlacklistFilter(COMMON_BLACKLIST);

    private final List<String> blacklist;

    public BlacklistFilter (String[] entries) {

        this(new ArrayList<String>(), entries);
    }

    public BlacklistFilter (List<String> blacklist, String[] entries) {

        this(blacklist);
        Collections.addAll(this.blacklist, entries);
    }

    public BlacklistFilter (List<String> blacklist) {

        this.blacklist = blacklist;
    }

    public void addBlacklistEntry (String entry) {

        this.blacklist.add(entry);
    }

    public boolean isBlacklisted (String name) {

        for (final String entry : this.blacklist)
            if (name.startsWith(entry))
                return true;
            
        return false;
    }

    @Override
    public boolean filter (String className) {

        return this.isBlacklisted(className);
    }
}