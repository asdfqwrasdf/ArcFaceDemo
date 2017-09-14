# ArcFaceDemo
Free SDK demo

>如何使用？
 1. 前往[官网](http://www.arcsoft.com.cn/ai/arcface.html)申请appid和sdkkey    
    修改 app\src\main\java\com\arcsoft\sdk_demo\FaceDB.java 下面的对应的值:

    	public static String appid = "xxxx"; 		
    	public static String fd_key = "xxxx";    
    	public static String ft_key = "xxxx";
   		public static String fr_key = "xxxx";
    
2. 下载sdk包，解压三个sdk包里libs中的文件到 本模块的[libs](https://github.com/asdfqwrasdf/ArcFaceDemo/tree/master/libs)下。

3. Android Studio2.3.3 中作为模块导入,编译运行即可。


> 最低支持的API-LEVEL？

   　　14-26

> demo使用哪些第三方库?

 - [android_extend](https://github.com/gqjjqg/android-extend)

---------------
> FAQ

1. Gradle 错误提示 Error:Failed to find target with hash string 'android-24'.......
	
    一般Android Studio 窗口会有个链接(Install missing platform(s) and sync project)    
    点击下载更新 android-24 即可解决（其他版本没测试过，建议不要随意更改）。    
	
2.	Gradle 错误提示 Error:(1, 0) Plugin with id 'com.android.application' not found.	
	
	问题根源：首先要搞明白Android Studio的Project，一个AS只能开一个Project，一个Project下有多个Moudle。    
	本demo是一个Module工程，不是一个Project工程，所以强行用Project的方式导入，会导致缺失Project的build.gradle，找不到gradle的classpath，也不会去jcenter下载依赖包，就会出现这个问题。    
	
	解决方法有两个：   
	
	a. 新建立一个Project，再import module，这是推荐的正确打开姿势。    
	b.  已经导入了不想重新删掉来过，那可以将错就错, 在Module的build.gradle中添加Project的build.gradle内容即可解决:
		
		// Top-level build file where you can add configuration options common to all sub-projects/modules.

		buildscript {
			repositories {
				jcenter()
			}
			dependencies {
				classpath 'com.android.tools.build:gradle:2.3.3'

				// NOTE: Do not place your application dependencies here; they belong
				// in the individual module build.gradle files
			}
		}

		allprojects {
			repositories {
				jcenter()
			}
		}

	
3.  还有其他问题

    直接提交[issue](https://github.com/asdfqwrasdf/ArcFaceDemo/issues)     
    我们会尽快解决    
	
