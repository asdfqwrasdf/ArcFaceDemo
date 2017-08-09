# ArcFaceDemo
Free SDK demo

如何使用？
1. 前往官网(http://www.arcsoft.com.cn/ai/arcface.html)申请appid和sdkkey, 
	修改 app\src\main\java\com\arcsoft\sdk_demo\FaceDB.java 下面的对应的值
		
    public static String appid = "xxxx";
    public static String fd_key = "xxxx";
    public static String ft_key = "xxxx";
    public static String fr_key = "xxxx";

2. 下载sdk包，解压三个sdk包里libs中的文件到 app\libs下。

3. Android Studio2.3.3 中作为模块导入,编译运行即可。


最低支持的API-LEVEL？

    14-26

