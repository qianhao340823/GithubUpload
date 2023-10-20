package org.example.testspring.dudemo.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * http工具包
 * @author zjl
 * @date 2022/04/20
 */
public class HttpUtils {

    /**
     * 获得连接
     * @param url url
     * @return {@link HttpURLConnection}
     * @throws IOException ioexception
     */
    public static HttpURLConnection getConnection(String url) throws IOException {
        URL httpUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection)httpUrl.openConnection();
        // 向文件所在服务器发送标识信息
        connection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko)Chrome/14.0.835.163 Safari/535.1");
        return connection;
    }


    /**
     * 得到http文件名称
     * @param url url
     * @return {@link String}
     */
    public static String getHttpFileName(String url) {
        int index = url.lastIndexOf("/");
        return url.substring(index+1);
    }


    /**
     * 获得分片连接
     * @param url      url
     * @param startPos 开始pos
     * @param endPos   终端pos
     * @return {@link HttpURLConnection}
     * @throws IOException ioexception
     */
    public static HttpURLConnection getConnection(String url, long startPos,long endPos) throws IOException {
        HttpURLConnection connection = getConnection(url);
        System.out.println("下载的分片区间是" + startPos + "-" + endPos);
        if(endPos != 0) {
            // bytes = 100-200
            connection.setRequestProperty("RANGE","bytes="+startPos+"-"+endPos);
        }else {
            // 只有 - 会下载到结尾的所有数据
            connection.setRequestProperty("RANGE","bytes="+startPos+"-");
        }
        return connection;
    }

    /**
     * 获取下载文件大小
     * @param url
     * @return long
     */
    public static long getFileSize(String url) throws IOException {
        return getConnection(url).getContentLength();
    }

}