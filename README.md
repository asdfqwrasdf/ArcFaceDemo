# ArcFaceDemo
Free SDK demo

>如何使用？
 1. 下载代码: git clone --recursive https://github.com/asdfqwrasdf/ArcFaceDemo.git
 
　　(如采用zip包下载，则解压后还需前往下载 [android_extend](https://github.com/gqjjqg/android-extend)，并解压到本工程的android_extend目录下)    
   
 2. 前往[官网](http://www.arcsoft.com.cn/ai/arcface.html)申请appid和sdkkey.    
修改 ArcFaceDemo-master\src\main\java\com\arcsoft\sdk_demo\FaceDB.java 下面的对应的值:

    	public static String appid = "xxxx"; 		
    	public static String fd_key = "xxxx";    
    	public static String ft_key = "xxxx";
   		public static String fr_key = "xxxx";
    
3. 下载sdk包之后，解压各个包里libs中的文件到 ArcFaceDemo-master\libs 下，同名so直接覆盖。

4. Android Studio2.3.3 中直接打开或者导入Project,编译运行即可。

> [android_extend](https://github.com/gqjjqg/android-extend)？

　　此模块仅用来简化demo开发，例如简化camera调用，提供简单的工具方便demo开发。

> 最低支持的API-LEVEL？

   　　14-26

> demo如何使用?

 1. 点击第一个按钮 打开图片或者拍一张带人脸的照片，确认后自动执行人脸，弹出注册框，注册第一个人脸。    
注册界面底部会展示已注册的信息列表，点击列表项，则可以执行删除操作    
    
 2. 点击第二个按钮 选择打开前置或者后置的镜头进行检测。

---------------
> FAQ
1. android_extend 空目录    
   如果下载的是ZIP包，子模块[android_extend](https://github.com/gqjjqg/android-extend)代码不会自动拉取，所以需要手动拿一下代码。    	
	```bash 
   	git submodule init
   	git submodule update
   	```
   
2. Gradle 错误提示 Error:Failed to find target with hash string 'android-24'.......
	
    一般Android Studio 窗口会有个链接(Install missing platform(s) and sync project)    
    点击下载更新 android-24 即可解决（其他版本没测试过，建议不要随意更改）。    
	
3.  还有其他问题

    直接提交[issue](https://github.com/asdfqwrasdf/ArcFaceDemo/issues)     
    我们会尽快解决    
	
