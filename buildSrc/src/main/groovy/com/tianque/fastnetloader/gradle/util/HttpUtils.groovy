package com.tianque.fastnetloader.gradle.util

public class HttpUtils{


    /**
     * 建立HTTP请求，并获取图片流对象。
     * @param urlString 图片下载路径
     * @param outputStream  Editor的输出流
     * @return
     */
//    public static boolean downloadUrlToStream(String urlString,
//                                              OutputStream outputStream) {
//        HttpURLConnection urlConnection = null;
//        BufferedOutputStream out = null;
//        BufferedInputStream in = null;
//        try {
//            final URL url = new URL(urlString);
//            urlConnection = (HttpURLConnection) url.openConnection();
//            in = new BufferedInputStream(urlConnection.getInputStream(),
//                    8 * 1024);
//            out = new BufferedOutputStream(outputStream, 8 * 1024);
//            int b;
//            while ((b = in.read()) != -1) {
//                out.write(b);
//            }
//            return true;
//        } catch (final IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (urlConnection != null) {
//                urlConnection.disconnect();
//            }
//            try {
//                if (out != null) {
//                    out.close();
//                }
//                if (in != null) {
//                    in.close();
//                }
//            } catch (final IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return false;
//    }

    static boolean downloadUrlToStream(String url,OutputStream outputStream){
        HttpURLConnection urlConnection = null
        BufferedOutputStream outS = null
        BufferedInputStream inS = null

        try {
            urlConnection = (HttpURLConnection) (new URL(url)).openConnection();
            inS = new BufferedInputStream(urlConnection.getInputStream(),
                    8 * 1024)

//            def stream = new URL(url).openStream()

            def total = inS.available()
            char end = 'B'
            if(total>1024){
                total = total/1024
                end = 'K'
            }
            if(total>1024){
                total = total/1024
                end = 'M'
            }
            println("get: "+url+"  (size:"+ total + end+ ")")


            outS = new BufferedOutputStream(outputStream, 8 * 1024)
            int b
            while ((b = inS.read()) != -1) {
                outS.write(b)
            }
            return true
        } catch (final IOException e) {
            e.printStackTrace()
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect()
            }
            try {
                if (outS != null) {
                    outS.close()
                }
                if (inS != null) {
                    inS.close()
                }
            } catch (final IOException e) {
                println(e)
            }
        }
        return false

    }
}