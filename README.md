# FastNetLoader
android library， preload Resoureces , 网络资源 预加载,打包前自动下载指定资源

-----

##实现功能 

 - 预加载
 	1.  (完成)通过app的build.gradle中配置 fastnetloader.preLoadResUrlFile = file("preLoad.txt") 来指定一个txt文件。txt文件中标出需要预加载的资源url,一行一个。  
 	2.  (完成)通过新建一个task 下载指定资源放入asset或者指定目录下，默认src/main/assets/preLoaded
 	3.  (完成)task嵌入task:preBuild之前，自动完成,可增量更新，已下载资源不会重复下载，如有问题，请手动删除asset下缓存文件
	4.  (完成)程序首次运行时，优先使用-预加载的资源

 
##other
 - 资源的安全问题:防止篡改,防止窃听  
 - (完成)资源的压缩  
 - (给使用者自己实现)如何与缓存框架结合
 - preLoad.txt 更多的配置选项，逻辑判断控制功能


##使用方式

在app的build.gradle中加入(还没传到maven)
    
    apply plugin: 'com.tianque.preloader'
    
    fastnetloader {
    	preLoadResUrlFile = file("preLoad.txt")
    }

	dependencies {
	    implementation project(':lib.preloader')
	}


在app文件夹中放入preLoad.txt文件，文件内容为每行一个url

    https://www.baidu.com/img/superlogo_c4d7df0a003d3db9b65e9ef0fe6da1ec.png?where=super
    https://github.com/WayJ/FastNetLoader/blob/master/README.md

![](https://i.imgur.com/rPkqqVD.png)

如此，app build时，会自动从preLoad.txt中读取要准备的资源（文件、接口返回数据），下载至tmp文件夹中，然后压缩放入assets（该处assets为默认路径，还未实现可配置）。资源会自动判断不会重复下载。  
注意点： 
 
 - 如果txt文件修改，删除了资源，请手动清空tmp缓存，不然删去的资源还是会放入压缩包中，增量资源无需清空缓存。
 - 目前对缓存文件大小 限制在100MB

app使用缓存的资源（kotlin）

	var loader = FastNetLoader(this)
	//get url from other api,load url
   	var url = intent.getStringExtra("url")
    if(url.contains("img"))
        loader.getBitmap(url){ url, found, data ->
           if(found)
              findViewById<ImageView>(R.id.imageView).setImageBitmap(data)
        }
    else
        loader.getString(url){ url, found, data ->
            if(found)
               findViewById<TextView>(R.id.textView).text = data
        }

loader.getXXXX方法有些是传入一个action类，因为使用了异步线程读取资源。
现有的getXXXX方法类型有
	
 - String
 - Bitmap
 - inputStream
 - File