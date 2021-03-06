package com.tianque.preloader.gradle.task

import com.tianque.preloader.gradle.AppExtension
import com.tianque.preloader.gradle.util.DiskLruCache
import com.tianque.preloader.gradle.util.StrictLineReader
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction


class CollectPreloadResTask extends DefaultTask {
    protected final String TAG = " PreLoader:---------- "
    protected AppExtension appExtension
//    private File preLoadResListFile
    private File outputDic

    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 100
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
        def reader = new StrictLineReader(new FileInputStream(getPreLoadResListFile()), com.tianque.preloader.gradle.util.Util.US_ASCII)
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
            com.tianque.preloader.gradle.util.Util.closeQuietly(reader)
        }
        if(urlList.size()==0){
            println(TAG+"zero resources need preload")
        }else{
            def diskLruCache = null
            try{
                diskLruCache = DiskLruCache.open(getOutputDic(),APP_VERSION,VALUES_COUNT,DISK_CACHE_SIZE)
                urlList.each {
                    def httpTask = new HttpGetTask(it,diskLruCache)
                    httpTask.doIt()
                }
            }catch (Exception e){
                println(e)
            }finally{
                com.tianque.preloader.gradle.util.Util.closeQuietly(diskLruCache)
            }
        }
    }

}
