# ArcFaceDemo
Free SDK demo

>如何使用？
 1. 前往[官网](http://www.arcsoft.com.cn/ai/arcface.html)申请appid和sdkkey
修改 app\src\main\java\com\arcsoft\sdk_demo\FaceDB.java 下面的对应的值:

    	public static String appid = "xxxx"; 		
    	public static String fd_key = "xxxx";    
   		public static String fr_key = "xxxx";
    
2. 下载sdk包，解压三个sdk包里libs中的文件到 本模块的[libs](https://github.com/asdfqwrasdf/ArcFaceDemo/tree/master/libs)下。

3. Android Studio2.3.3 中作为模块导入,编译运行即可。


> 最低支持的API-LEVEL？

   　　14-26

> demo使用哪些第三方库?

　　[android_extend](https://github.com/gqjjqg/android-extend)

> FAQ

1. Gradle 错误提示 Error:Failed to find target with hash string 'android-24'.......
	
    一般Android Studio 窗口会有个链接(Install missing platform(s) and sync project)
    点击下载更新 android-24 即可解决（其他版本没测试过，建议不要随意更改）。
	
2.  还有其他问题

    直接提交[issue](https://github.com/asdfqwrasdf/ArcFaceDemo/issues) 
    我们会尽快解决