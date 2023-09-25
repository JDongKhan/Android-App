# JDApp
 这是一款比较基础的基于androidx 的 Android快速开发框架

 技术栈：initiator + arouter + butterknife + retrofit2 + okhttp3 + rxjava2 + glide + logger 

 initiator： Application优雅的初始化方案

 arouter： 路由

 ~butterknife：用于处理Android view绑定、事件等。(kotlin版本无)~

 okhttp3：网络请求

 retrofit2：restful 风格的网络请求的适配器

 rxjava2：异步编程的 API

 glide：图片库

 logger：日志库 


有待持续完善



![1](/Users/jd/Documents/GitHub/Android/JDApp/1.png)


#### 环境变更

1、修改build.gradle里面的dependencies的版本

```groovy
    classpath 'com.android.tools.build:gradle:7.2.1'
```

2、修改gradle-wrapper.properties里面的gradle的版本
```properties
    distributionUrl=https\://services.gradle.org/distributions/gradle-7.3.3-all.zip
```
