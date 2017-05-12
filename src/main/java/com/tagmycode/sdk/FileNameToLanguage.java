package com.tagmycode.sdk;

import java.util.HashMap;

public class FileNameToLanguage {
    public static final HashMap<String, String> map;
    public static final String TEXT = "text";
    public static final String JAVA = "java";
    public static final String PHP = "php";
    public static final String JAVASCRIPT = "javascript";
    public static final String HTML = "html";
    public static final String XML = "xml";
    public static final String BASH = "bash";
    public static final String RUBY = "ruby";
    public static final String SQL = "sql";
    public static final String C = "c";
    public static final String CPP = "cpp";
    public static final String CSS = "css";
    public static final String PYTHON = "python";

    static {
        map = new HashMap<String, String>();
        map.put("txt", TEXT);
        map.put("java", JAVA);
        map.put("php", PHP);
        map.put("phtml", PHP);
        map.put("js", JAVASCRIPT);
        map.put("html", HTML);
        map.put("xhtml", XML);
        map.put("xml", XML);
        map.put("sh", BASH);
        map.put("bash", BASH);
        map.put("rb", RUBY);
        map.put("erb", RUBY);
        map.put("sql", SQL);
        map.put("c", C);
        map.put("cpp", CPP);
        map.put("css", CSS);
        map.put("scss", CSS);
        map.put("py", PYTHON);
    }

    public static String getCode(String fileName) {
        return getCodeByExtension(getExtension(fileName));
    }

    protected static String getCodeByExtension(String extension) {
        return map.get(extension);
    }

    private static String getExtension(String fileName) {
        if (fileName == null) {
            return null;
        }

        String[] split = fileName.toLowerCase().split("\\.");

        if (split.length < 2) {
            return null;
        }
        return split[split.length - 1];
    }
}
