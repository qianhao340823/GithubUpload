package org.example.testspring.dudemo.core;

import org.example.testspring.dudemo.constant.Constant;
import org.example.testspring.dudemo.util.FileUtils;
import org.example.testspring.dudemo.util.HttpUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * 下载器
 * @author zjl
 * @date 2022/04/20
 */
public class Downloader {

    // 监听下载信息的线程池
    public ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    // 创建线程池对象
    public ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(Constant.THREAD_NUM,Constant.THREAD_NUM,0,TimeUnit.SECONDS,new ArrayBlockingQueue<>(Constant.THREAD_NUM));

    // CountDownLatch 保证合并之前分片都下载完毕
    CountDownLatch countDownLatch = new CountDownLatch(Constant.THREAD_NUM);

    /**
     * 下载
     * @param url url
     */
    public void download(String url) {
        // 获取文件名
        String fileName = HttpUtils.getHttpFileName(url);
        // 设置文件下载路径
        fileName = Constant.PATH + fileName;
        // 获取文件大小
        long localFileSize = FileUtils.getFileSize(fileName);
        // 下载运行信息线程类
        DownloadInfoThread downloadInfoThread = null;
        // HTTP连接对象
        HttpURLConnection connection = null;
        try{
            // 建立连接
            connection = HttpUtils.getConnection(url);
            // 获取下载文件的总大小
            int totalLength = connection.getContentLength();
            // 保证本文件未下载过
            if(localFileSize >= totalLength) {
                System.out.println("该文件已经下载过");
                return;
            }

            // 创建获取下载信息的任务对象
            downloadInfoThread = new DownloadInfoThread(totalLength);
            // 获取下载状态，创建一个每秒执行一次的线程，捕获当前下载状态（大小、速度）
            executorService.scheduleAtFixedRate(downloadInfoThread,1,1, TimeUnit.SECONDS);
        }catch (IOException e) {
            e.printStackTrace();
        }
        // 保证连接存在
        if(connection == null) {
            System.out.println("获取连接失败");
            return;
        }

        // 括号内代码会自动关闭
        try {
            // 切分任务
            ArrayList<Future> list = new ArrayList<>();
            split(url,list);

            // 保证多个线程的分片数据下载完毕
            countDownLatch.await();

            // 合并文件
            if(merge(fileName)) {
                removeTmpFile(fileName);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.print("\r");
            System.out.print("下载完成");
            // 关闭连接
            connection.disconnect();
            executorService.shutdownNow();
            // 关闭线程池
            poolExecutor.shutdown();
        }
    }

    /**
     * 文件切分
     * @param url        url
     * @param futureList 未来的列表
     */
    public void split(String url, ArrayList<Future> futureList) throws IOException {
        // 获取下载文件大小
        long fileSize = HttpUtils.getFileSize(url);
        // 计算切分后的文件大小
        long size = fileSize / Constant.THREAD_NUM;

        for (int i = 0; i < Constant.THREAD_NUM; i++) {
            // 计算下载起始位置
            long startPos = i * size;
            long endPos;

            if(i == Constant.THREAD_NUM - 1) {
                // 下载的最后一块
                endPos = 0;
            }else {
                endPos = startPos + size;
            }

            // 如果不是第一块，那么起始位置+1
            if(startPos != 0) {
                startPos++;
            }

            // 创建任务对象
            DownloadTask downloadTask = new DownloadTask(url, startPos, endPos,i,countDownLatch);
            // 提交任务到线程池
            Future<Boolean> future = poolExecutor.submit(downloadTask);
            // 添加到结果集合中
            futureList.add(future);
        }
    }

    /**
     * 文件合并
     * @param fileName 合并的文件名前缀
     */
    public boolean merge(String fileName) {
        System.out.print("\r");
        System.out.println("开始合并文件");
        byte[] buffer = new byte[Constant.BYTE_SIZE];
        int len = -1;
        try(RandomAccessFile accessFile = new RandomAccessFile(fileName,"rw")){
            for (int i = 0; i < Constant.THREAD_NUM; i++) {
                try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileName + ".temp" + i))){
                    while((len = bis.read(buffer)) != -1) {
                        accessFile.write(buffer,0,len);
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        System.out.println("文件合并完毕！");
        return true;
    }


    /**
     * 删除临时文件
     * @param fileName 文件名称
     * @return boolean
     */
    public boolean removeTmpFile(String fileName) {
        for (int i = 0; i < Constant.THREAD_NUM; i++) {
            File file = new File(fileName + ".temp" + i);
            file.delete();
        }
        return true;
    }

}