package org.example;

import org.example.testspring.dudemo.core.Downloader;

public class Main {
    public static void main(String[] args) {
        String url = "https://dldir1.qq.com/qqfile/qq/PCQQ9.7.17/QQ9.7.17.29230.exe";
        new Downloader().download(url);
    }
}