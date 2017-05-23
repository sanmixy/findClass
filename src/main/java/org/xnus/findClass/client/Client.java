package org.xnus.findClass.client;

import org.xnus.findClass.core.ParamParser;

public class Client {
    public static void main(String[] args) {
        new ParamParser(args).build().load();
    }
}
