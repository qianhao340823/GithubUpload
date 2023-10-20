package org.example.testspring.dudemo.core;

import org.example.testspring.dudemo.constant.Constant;
import org.example.testspring.dudemo.util.HttpUtils;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * 分块下载任务
 *
 * @author zjl
 * @date 2022/04/20
 */
public class DownloadTask implements Callable<Boolean> {

    private String url;
    // 起始位置
    private long startPos;
    // 结束位置
    private long endPos;
    // 标识当前是哪一部分
    private int part;

    private CountDownLatch countDownLatch;

    public DownloadTask(String url, long startPos, long endPos, int part,CountDownLatch countDownLatch) {
        this.url = url;
        this.startPos = startPos;
        this.endPos = endPos;
        this.part = part;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public Boolean call() throws Exception {
        // 获取文件名
        String httpFileName = HttpUtils.getHttpFileName(url);
        // 分块的文件名
        httpFileName = httpFileName + ".temp" + part;
        // 下载路径
        httpFileName = Constant.PATH + httpFileName;
        // 获取分块下载的链接
        HttpURLConnection connection = HttpUtils.getConnection(url, startPos, endPos);
        try (
                InputStream input = connection.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(input);
                RandomAccessFile accessFile = new RandomAccessFile(httpFileName,"rw");
        ){
            byte[] bytes = new byte[Constant.BYTE_SIZE];
            int len = -1;
            while((len = bis.read(bytes)) != -1) {
                accessFile.write(bytes,0,len);
                // 1s内下载数据之和
                DownloadInfoThread.downSize.add(len);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            countDownLatch.countDown();
            connection.disconnect();
        }
        return true;
    }
}
