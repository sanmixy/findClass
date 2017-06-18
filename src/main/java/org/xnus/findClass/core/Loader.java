package org.xnus.findClass.core;

import com.google.common.base.Strings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.io.Files.fileTreeTraverser;

public class Loader {
    private static final String JAR_SUFFIX = ".jar";
    private static final String CLASS_SUFFIX = ".class";
    private String root, class_name;
    private boolean regex_enabled, include_sub_folder;

    /**
     * 构造函数.
     *
     * @param root               要扫描的路径
     * @param class_name         要扫描的类名
     * @param regex_enabled      是否允许正则匹配
     * @param include_sub_folder 是否包含子目录
     */
    Loader(final String root, final String class_name, final boolean regex_enabled, final boolean include_sub_folder) {
        checkState(!Strings.isNullOrEmpty(root), "scan dir could not be null or empty");
        checkState(!Strings.isNullOrEmpty(class_name), "target class name could not be null or empty");
        this.root = root;
        this.include_sub_folder = include_sub_folder;
        this.class_name = class_name;
        this.regex_enabled = regex_enabled;
    }


    List<File> scan() {
        File root = new File(this.root);
        checkState(root.isDirectory(), "path " + root.getPath() + " is not a valid directory");
        return include_sub_folder ?
                fileTreeTraverser()
                        .preOrderTraversal(root)
                        .filter(file -> null != file && file.isFile() && file.getName().endsWith(JAR_SUFFIX)).toList()
                : Arrays.asList(Optional.ofNullable(
                root.listFiles(file -> file.isFile() && file.getName().endsWith(JAR_SUFFIX)))
                .orElse(new File[0]));
    }

    public List<JarEntry> load() {
        List<JarEntry> entries = new ArrayList<>();
        List<File> files = scan();
        files.parallelStream().forEach(file -> {
            try (JarFile jarFile = new JarFile(file)) {
                List<JarEntry> found = jarFile.stream().parallel().filter(entry -> entry.getName().endsWith(CLASS_SUFFIX) && (regex_enabled
                        ? entry.getName().substring(entry.getName().lastIndexOf('/') + 1, entry.getName().length() - 6).matches(class_name)
                        : entry.getName().substring(entry.getName().lastIndexOf('/') + 1, entry.getName().length() - 6).equals(class_name)))
                        .collect(Collectors.toList());
                entries.addAll(found);
                found.forEach(entry -> System.out.println("found in " + file.getPath() + " : " + entry.getName()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return entries;
    }
}
