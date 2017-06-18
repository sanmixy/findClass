package org.xnus.findClass.core;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URLDecoder;
import java.util.List;
import java.util.jar.JarEntry;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class LoaderTest {
    private String root;
    private Loader loader;

    @Before
    public void setUp() throws Exception {
        root = URLDecoder.decode(
                Preconditions.checkNotNull(
                        this.getClass().getClassLoader().getResource("path")).getPath(), Charsets.UTF_8.name());
    }


    @Test
    public void should_not_find_target_when_sub_folders_are_not_included() throws Exception {
        loader = new Loader(root, "Preconditions", false, false);

        List<File> potentials = loader.scan();

        assertThat(potentials.size(), equalTo(0));
    }

    @Test
    public void should_find_target_when_sub_folders_are_not_included() throws Exception {
        loader = new Loader(root, "Preconditions", false, true);

        List<File> potentials = loader.scan();

        assertThat(potentials.size(), equalTo(1));
    }

    @Test
    public void show_not_find_class_when_match_without_regex() throws Exception {
        loader = new Loader(root, "^\\w+s$", false, true);

        List<JarEntry> load = loader.load();

        assertThat(load.size(), equalTo(0));
    }

    @Test
    public void show_find_class_when_match_without_regex() throws Exception {
        loader = new Loader(root, "^\\w+s$", true, true);

        List<JarEntry> load = loader.load();

        assertThat(load.size(), equalTo(94));
    }
}