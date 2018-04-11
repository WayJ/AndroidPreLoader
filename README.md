# FastNetLoader
android网络优化，网络资源预加载、懒加载、modified

what to do
1.编写一段代码，指定需要预加载的资源（Image，File，String data,response body ......）
2.通过新建一个task 嵌入gradle buildApk Task之前，下载指定资源放入asset下
3.程序首次运行时，优先使用-预加载的资源

 - 资源的安全问题，防止篡改
 - 资源是否要压缩
 

