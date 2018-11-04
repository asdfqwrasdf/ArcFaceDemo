# ArcFaceDemo
Free SDK demo

>工程如何使用？
 1. 下载代码:    
    git clone https://github.com/asdfqwrasdf/ArcFaceDemo.git 或者直接下载压缩包
 
 2. 前往[官网](http://www.arcsoft.com.cn/ai/arcface.html)申请appid和sdkkey。  
    **注意：当前版本请下载ArcFace1.2**    
    修改 ArcFaceDemo-master\src\main\java\com\arcsoft\sdk_demo\FaceDB.java 下面的对应的值:    
   
    ```java    
    public static String appid = "xxxx"; 		
    public static String fd_key = "xxxx";    
    public static String ft_key = "xxxx";    
    public static String fr_key = "xxxx";    
    public static String age_key = "xxxx";    
    public static String gender_key = "xxxx";    
    ```
3. 下载sdk包之后，解压各个包里libs中的文件到 ArcFaceDemo-master\libs 下，同名so直接覆盖。

4. Android Studio3.x 中直接打开或者导入Project,编译运行即可。    

> demo如何使用?    

 1. 点击第一个按钮 打开图片或者拍一张带人脸的照片，确认后自动执行人脸，弹出注册框，注册第一个人脸。    
 注册界面底部会展示已注册的信息列表，点击列表项，则可以执行删除操作。   
 2. 点击第二个按钮 选择打开前置或者后置的镜头进行检测。
 
> demo中人脸数据的保存方式?  

　以注册时人名为关键索引，保存在face.txt中。  
　创建的 name.data 则为实际的数据存储文件，保存了所有特征信息。  
　同一个名字可以注册多个不同状态角度的人脸，在name.data 中连续保存，占用的数据文件长度为:  
　N * {4字节(特征数据长度) + 22020字节(特征数据信息)}
  
> 最低支持的API-LEVEL?  

　14-27    　
 
---------------
> Issue Report
1. before report    
    please check the closed issues.    
  
2. issue format    
    a.错误信息:log，input image，core stack, etc...    
    b.设备信息:cpu, memory, device name, etc...    
    c.系统版本:OS version, API leve,etc...    
    d.具体操作流程:which step,how to recurrence,etc...    
  
---------------
> FAQ
1. Gradle 错误提示 Error:Failed to find target with hash string 'android-24'.......    
 一般Android Studio 窗口会有个链接(Install missing platform(s) and sync project)    
 点击下载更新 android-24 即可解决（其他版本没测试过，建议不要随意更改）。    
	
2. 加载图片注册时Crash.    
 NV21格式限制高度和宽度不能同时为奇数，demo已经对这个做了保护。    
 如有发生，请提供图像尺寸和发生时的全部log。    
    
3. 年龄和性别检测结果准确度不够.    
 Video的接口性能优先，Image的接口准确度优先。    
    
4. com.guo.android_extend:android-extend 找不到依赖.    
 此第三方库[android_extend](https://github.com/gqjjqg/android-extend) 用来简化camera调用，提供简单的工具方便demo开发。 
 一般android studio会自动从jcenter 下载对应的aar包，如果没有自动下载，请自行检查是否网络问题，或者删掉build等编译目录，重新运行gradle.
    
5. 还有其他问题.    
 直接提交[issue](https://github.com/asdfqwrasdf/ArcFaceDemo/issues)     
 我们会尽快解决    
	
