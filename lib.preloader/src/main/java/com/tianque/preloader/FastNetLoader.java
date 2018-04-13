package com.tianque.preloader;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.tianque.preloader.disklrucache.OnlyReadDiskCache;
import com.tianque.preloader.util.MD5Utils;
import com.tianque.preloader.util.StreamUtils;
import com.tianque.preloader.util.ZipUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FastNetLoader {

    private OnlyReadDiskCache mDiskLruCache;
    private Context appContext;
    private File cacheDirPath;
    private String zipFileName = "preLoaded.zip";

    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 1024; // 磁盘缓存的大小为50M
    private static final int APP_VERSION = 1;
    private static final int VALUES_COUNT = 1;


    private static final int CPU_COUNT = Runtime.getRuntime()
            .availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;// 核心线程数
    private ExecutorService pool;// 线程池
    private Future future;
    private Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            ActionResult result = (ActionResult) msg.obj;
            result.action();
        }
    };

    public FastNetLoader(Context context) {
        this.appContext = context.getApplicationContext();
        init();
    }

    public void init() {
        cacheDirPath = getDiskCacheDir(appContext, "preLoaded");
        try {
            checkCacheDirExist();
            mDiskLruCache = OnlyReadDiskCache.open(cacheDirPath, APP_VERSION, VALUES_COUNT, DISK_CACHE_SIZE);
            pool = Executors.newFixedThreadPool(CORE_POOL_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void checkCacheDirExist() throws Exception {
        SharedPreferences sharedPreferences = appContext.getSharedPreferences("FastNetLoader", Context.MODE_PRIVATE);
        String key = getStoreKey("FastNetLoader", appContext);
        boolean hasDir = sharedPreferences.getBoolean(key, false);
        if (!hasDir) {
            unZipFromAssets(appContext);
            sharedPreferences.edit().putBoolean(key,true).commit();
        }

    }

    public void put(String key, File file) {
    }

    public void put(String key, String data) {
    }

    public void getString(final String url,final Action<String> action) {
        if(mDiskLruCache==null)
            throw new RuntimeException("Cache not found");
        Runnable loadBitmapTask = new Runnable() {

            @Override
            public void run() {
                if (!Thread.currentThread().isInterrupted()) {
                    String key = MD5Utils.hashKeyForDisk(url);// 把Url转换成KEY
                    String str=null;
                    try {
                        OnlyReadDiskCache.Snapshot snapShot = mDiskLruCache.get(key);// 通过key获取Snapshot对象
                        if (snapShot != null) {
                            InputStream is = snapShot.getInputStream(0);// 通过Snapshot对象获取缓存文件的输入流
                            str = StreamUtils.getStringFromInputStream(is);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mMainHandler.obtainMessage(0, new ActionResult(url,str,action))
                            .sendToTarget();
                }
            }
        };
        future = pool.submit(loadBitmapTask);
    }

    public void getBitmap(final String url,final Action<Bitmap> action) {
        if(mDiskLruCache==null)
            throw new RuntimeException("Cache not found");
        Runnable loadBitmapTask = new Runnable() {

            @Override
            public void run() {
                if (!Thread.currentThread().isInterrupted()) {
                    String key = MD5Utils.hashKeyForDisk(url);// 把Url转换成KEY
                    Bitmap bitmap=null;
                    try {
                        OnlyReadDiskCache.Snapshot snapShot = mDiskLruCache.get(key);// 通过key获取Snapshot对象
                        if (snapShot != null) {
                            InputStream is = snapShot.getInputStream(0);// 通过Snapshot对象获取缓存文件的输入流
                            bitmap = StreamUtils.getBitmapFromInputStream(is);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    mMainHandler.obtainMessage(0, new ActionResult(url,bitmap,action))
                            .sendToTarget();
                }
            }
        };
        future = pool.submit(loadBitmapTask);
    }


    public InputStream getInputStream(String url){
        if(mDiskLruCache==null)
            throw new RuntimeException("Cache not found");
        String key = MD5Utils.hashKeyForDisk(url);// 把Url转换成KEY
        try {
            OnlyReadDiskCache.Snapshot snapShot = mDiskLruCache.get(key);// 通过key获取Snapshot对象
            if (snapShot != null) {
                return snapShot.getInputStream(0);// 通过Snapshot对象获取缓存文件的输入流
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 这里返回的文件名是加密过的文件名 类似 xxxxx.0，如果需要改名使用建议先copy，不要改动原文件
     * @param url
     * @return
     */
    public File getFile(String url){
        if(mDiskLruCache==null)
            throw new RuntimeException("Cache not found");
        String key = MD5Utils.hashKeyForDisk(url);// 把Url转换成KEY
        try {
            return mDiskLruCache.getCacheFile(key,0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private String getStoreKey(String preStr, Context context) {
        PackageManager pm = context.getPackageManager();//context为当前Activity上下文
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(context.getPackageName(), 0);

            String versionName = pi.versionName;
            int versionCode = pi.versionCode;
            return preStr + ":" + versionName + ":" + versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return preStr + ":" + "default";
        }
    }


    private void unZipFromAssets(Context context) throws Exception {
        File newZipFile = new File(cacheDirPath, zipFileName);
        if(newZipFile.exists())
            newZipFile.delete();
        if(cacheDirPath.exists()){
            cacheDirPath.delete();
        }
        if(!cacheDirPath.exists()){
            cacheDirPath.mkdirs();
        }
        newZipFile.createNewFile();
        ZipUtils.copyFileFromStream(context.getAssets().open(zipFileName), newZipFile.getPath());
        ZipUtils.unzip(newZipFile.getPath(), newZipFile.getParent(), false);
    }

    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }


    public interface Action<T>{
        void action(String url,boolean found,T data);
    }


    private final class ActionResult<T>{
        private String url;
        private T obj;
        private Action<T> actionObj;
        public ActionResult(String url,T t,Action<T> a){
            this.url=url;
            obj=t;
            actionObj =a;
        }

        public void action(){
            actionObj.action(url,obj!=null,obj);
        }
    }

}
