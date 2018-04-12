# FastNetLoader
android 网络优化,preload,lozyload, 网络资源预加载、懒加载、modified

-----

###what to do  

 - 预加载
 	1.  (完成)通过app的build.gradle中配置 fastnetloader.preLoadResUrlFile = file("preLoad.txt") 来指定一个txt文件。txt文件中标出需要预加载的资源url,一行一个。  
 	2.  (完成)通过新建一个task 下载指定资源放入asset或者指定目录下，默认src/main/assets/preLoaded
 	3.  (完成)task嵌入task:preBuild之前，自动完成,可增量更新，已下载资源不会重复下载，如有问题，请手动删除asset下缓存文件
	4.  程序首次运行时，优先使用-预加载的资源  
 - 懒加载  
	1. 加载逻辑：先加载离线资源、同时请求线上资源，如有更新，则刷新界面  
 - modified  
	 - 判断资源是否有更新，单独在做一套判断逻辑是否合适，沿用http协议中的缓存方式，或者直接更新资源就直接更新资源名，类似 "/js/jquery1.1.js"  and  "/js/jquery1.2.js"
 
###other
 - 资源的安全问题:防止篡改,防止窃听  
 - 资源的压缩问题:是否要压缩，是否大于多少才要压缩  
 - 如何与缓存框架结合

