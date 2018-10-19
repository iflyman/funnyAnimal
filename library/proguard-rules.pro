# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\bhyan\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keepattributes *Annotation*
-keepattributes JavascriptInterface
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes SourceFile
-keepattributes EnclosingMethod
-dontwarn com.taobao.**
-dontwarn anet.channel.**
-dontwarn anetwork.channel.**
-dontwarn org.android.**
-dontwarn org.apache.thrift.**
-dontwarn com.xiaomi.**
-dontwarn com.huawei.**
-dontoptimize

-keepattributes *Annotation*

-keep class com.taobao.** {*;}
-keep class org.android.** {*;}
-keep class anet.channel.** {*;}
-keep class com.umeng.** {*;}
-keep class com.xiaomi.** {*;}
-keep class com.huawei.** {*;}
-keep class org.apache.thrift.** {*;}

-keep class com.alibaba.sdk.android.**{*;}
-keep class com.ut.**{*;}
-keep class com.ta.**{*;}

-keep public class **.R$*{
   public static final int *;
}

#（可选）避免Log打印输出
-assumenosideeffects class android.util.Log {
   public static *** v(...);
   public static *** d(...);
   public static *** i(...);
   public static *** w(...);
 }

 -keepclassmembers class * {
    public <init> (org.json.JSONObject);
 }

 -keep public class funnyAnimal.R$*{
 public static final int *;
 }

 -keepclassmembers enum * {
     public static **[] values();
     public static ** valueOf(java.lang.String);
 }

 -keep class com.qiniu.**{*;}
 -keep class com.qiniu.**{public <init>();}
 -ignorewarnings

 # rxJava
 -dontwarn sun.misc.**
 -keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
 }
 -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
     rx.internal.util.atomic.LinkedQueueNode producerNode;
 }
 -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
     rx.internal.util.atomic.LinkedQueueNode consumerNode;
 }

 -dontwarn com.squareup.okhttp.**

 # retrofit2
 -dontwarn retrofit2.**
 -keep class retrofit2.** {*;}
 # okhttp3
 -dontwarn com.squareup.okhttp3.**
 -dontwarn okio.**
 -keep class com.squareup.okhttp3.** {*;}
 -keep interface com.squareup.okhttp3.** {*;}

 # gson
 -dontwarn com.google.**
 -keep class com.google.gson.** {*;}
 -keep class sun.misc.Unsafe { *; }

 # javax
 -dontwarn javax.**
 -keep class javax.**

  -keep class com.squareup.** {*;}

  -dontwarn com.squareup.okhttp.**

  #EventBus
  -keepclassmembers class ** {
      public void onEvent*(**);
  }

  -keepclassmembers class ** {
     public void onEventMainThread(**);
  }

  -keep class funnyAnimal.api.** {*;}

  #umeng share
  -dontwarn com.umeng.**
  -keep public interface com.tencent.**
  -keep public interface com.umeng.socialize.**
  -keep public interface com.umeng.socialize.sensor.**
  -keep public interface com.umeng.scrshot.**
  -keep public class com.umeng.socialize.* {*;}
  -keep class com.umeng.scrshot.**
  -keep class com.umeng.socialize.sensor.**
  -keep class com.umeng.socialize.handler.**
  -keep class com.umeng.socialize.handler.*
  -keep class com.umeng.weixin.handler.**
  -keep class com.umeng.weixin.handler.*
  -keep class com.umeng.qq.handler.**
  -keep class com.umeng.qq.handler.*
  -keep class UMMoreHandler{*;}

  #阿里用户反馈
  -keep class com.taobao.** {*;}
  -keep class com.alibaba.** {*;}
  -dontwarn com.taobao.**
  -dontwarn com.alibaba.**

  -keep class com.ut.** {*;}
  -dontwarn com.ut.**
  -keep class com.ta.** {*;}
  -dontwarn com.ta.**
