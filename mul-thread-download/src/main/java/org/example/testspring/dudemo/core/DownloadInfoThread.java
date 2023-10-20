package org.example.testspring.dudemo.core;


import org.example.testspring.dudemo.constant.Constant;

import java.util.concurrent.atomic.LongAdder;

public class DownloadInfoThread implements Runnable{

    /**
     * 文件总大小
     */
    private long fileSize;

    public DownloadInfoThread(long fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * 已下载的文件大小
     */
    private static LongAdder finishedSize = new LongAdder();

    /**
     * 本次下载大小
     */
    public static volatile LongAdder downSize = new LongAdder();

    /**
     * 上一次下载大小
     */
    private double preSize;


    @Override
    public void run() {
        // 计算文件总大小 单位mb
        String httpFileSize = String.format("%.2f",fileSize/ Constant.MB);

        // 计算每秒下载速度 单位kb/s
        int speed = (int) ((downSize.longValue() - preSize) / 1024d);
        preSize = downSize.longValue();

        // 剩余文件大小
        double remainSize = fileSize - finishedSize.longValue() - downSize.longValue();

        // 计算剩余时间
        String remainTime = String.format("%.1f",remainSize / 1024d / speed);
        if ("Infinity".equalsIgnoreCase(remainTime)) {
            remainTime = "-";
        }

        // 计算已经下载大小
        String currentFileSize = String.format("%.2f",(downSize.longValue() - finishedSize.longValue()) / Constant.MB);

        String downloadInfo = String.format("已下载 %smb/%smb,速度 %skb/s,剩余时间 %ss", currentFileSize, httpFileSize, speed, remainTime);

        System.out.print("\r");
        System.out.print(downloadInfo);

    }
}
