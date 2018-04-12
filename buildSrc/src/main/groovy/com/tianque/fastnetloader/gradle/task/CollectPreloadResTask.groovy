package com.tianque.fastnetloader.gradle.task

import com.tianque.fastnetloader.gradle.AppExtension
import com.tianque.fastnetloader.gradle.util.DiskLruCache
import com.tianque.fastnetloader.gradle.util.StrictLineReader
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.internal.impldep.org.apache.http.client.methods.HttpGet

import java.nio.charset.Charset

class CollectPreloadResTask extends DefaultTask {
    protected final String TAG = "FastNetLoader:---------- "
    protected AppExtension appExtension
//    private File preLoadResListFile
    private File outputDic

    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 50
    private int APP_VERSION = 1
    private static final int VALUES_COUNT = 1

    void setAppExtension(AppExtension extension) {
        this.appExtension = extension
//        APP_VERSION = appExtension.project.version
    }

//    @InputFile
    File getPreLoadResListFile() {
//        preLoadResListFile=appExtension.preLoadResUrlFile
        return appExtension.preLoadResUrlFile
    }


    void setOutputDic(File file){
        this.outputDic = file
    }

    File getOutputDic() {
        if(outputDic==null)
            outputDic = project.file(appExtension.outputDir)
        return outputDic
    }

    @TaskAction
    void collectPreLoadRes() {
        println()
        println(TAG+" start read "+getPreLoadResListFile().name)
        def reader = new StrictLineReader(new FileInputStream(getPreLoadResListFile()), com.tianque.fastnetloader.gradle.util.Util.US_ASCII)
        def urlList = []
        try {
            reader.allowEndWithOutLine = true
            def lineCount = 0
            while (true) {
                try {
                    String url  = reader.readLine()
                    lineCount++
                    urlList.add(url)
                    println("find url "+lineCount +" : " +url)
                } catch (Exception e) {
                    break
                }
            }
        } finally {
            println(TAG+"finish read "+getPreLoadResListFile().name)
            println()
            com.tianque.fastnetloader.gradle.util.Util.closeQuietly(reader)
        }
        if(urlList.size()==0){
            println(TAG+"zero resources need preload")
        }else{
            def diskLruCache = DiskLruCache.open(getOutputDic(),APP_VERSION,VALUES_COUNT,DISK_CACHE_SIZE)
            urlList.each {
                def httpTask = new HttpGetTask(it,diskLruCache)
                httpTask.doIt()
            }
            diskLruCache.close()
        }
    }

}
