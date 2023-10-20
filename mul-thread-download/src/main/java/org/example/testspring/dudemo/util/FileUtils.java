package org.example.testspring.dudemo.util;

import java.io.File;

public class FileUtils {
    public static long getFileSize(String path) {
        File file = new File(path);
        return file.exists() && file.isFile() ? file.length() : 0;
    }
}