package org.xnus.findClass.core;


import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class ParamParser {
    private List<String> args;

    private boolean regex_enabled = false;
    private boolean include_subfolders = false;
    private String root = "";
    private String class_name = "";

    public ParamParser(String[] userArgs) {
        args = Arrays.asList(checkNotNull(userArgs));
    }

    public Loader build() {
        System.out.println("args: " + args);
        args.forEach(arg -> {
            if (arg.startsWith("-")) {
                if ("-r".equalsIgnoreCase(arg))
                    include_subfolders = true;
                if ("-g".equalsIgnoreCase(arg))
                    regex_enabled = true;
            } else {
                if ("".equals(root))
                    root = arg;
                else
                    class_name = arg;
            }
        });
        System.out.println(root + "  " + class_name + "  " + regex_enabled + "  " + include_subfolders);
        return new Loader(root, class_name, regex_enabled, include_subfolders);
    }
}
