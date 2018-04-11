package com.tianque.fastnetloader.gradle.task

import com.tianque.fastnetloader.gradle.util.DiskLruCache
import com.tianque.fastnetloader.gradle.util.HttpUtils
import org.gradle.api.DefaultTask


class HttpGetTask{

    protected String url

    protected DiskLruCache diskLruCache

    public HttpGetTask(String url, DiskLruCache diskLruCache){
        this.url=url
        this.diskLruCache = diskLruCache
    }

    public String getUrl(){
        return url
    }


    public boolean doIt(){
        def result = false
        String key = com.tianque.fastnetloader.gradle.util.Util.hashKeyForDisk(url) // 通过md5加密了这个URL，生成一个key
        try {
            DiskLruCache.Editor editor = diskLruCache.edit(key)// 产生一个editor对象
            if (editor != null) {
                // 创建一个新的输出流 ，创建DiskLruCache时设置一个节点只有一个数据，所以这里的index直接设为0即可
                OutputStream outputStream = editor.newOutputStream(0)
                // 通过地址获取图片数据写入到输出流
                if (HttpUtils.downloadUrlToStream(url,
                        outputStream)) {
                    // 写入成功，提交
                    editor.commit()
                    result = true
                } else {
                    // 写入失败，中止
                    editor.abort()
                    result = false
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }



}