-optimizationpasses 5          # 指定代码的压缩级别

-dontusemixedcaseclassnames   # 是否使用大小写混合

-dontpreverify           # 混淆时是否做预校验

-verbose                # 混淆时是否记录日志

-dontskipnonpubliclibraryclasses

-keepattributes SourceFile,LineNumberTable

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法

-keep class org.apache.http.**
-keep class android.net.http.**
-dontwarn org.apache.http.**
-dontwarn android.net.http.AndroidHttpClient
-dontwarn com.google.android.gms.**
-dontwarn com.android.volley.toolbox.**
-dontoptimize
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

-keep public class * extends android.app.Activity      # 保持哪些类不被混淆

-keep public class * extends android.app.Application   # 保持哪些类不被混淆

-keep public class * extends android.app.Service       # 保持哪些类不被混淆

-keep public class * extends android.content.BroadcastReceiver  # 保持哪些类不被混淆

-keep public class * extends android.content.ContentProvider    # 保持哪些类不被混淆

-keep public class * extends android.app.backup.BackupAgentHelper # 保持哪些类不被混淆

-keep public class * extends android.preference.Preference        # 保持哪些类不被混淆

-keep public class com.android.vending.licensing.ILicensingService    # 保持哪些类不被混淆



-keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
    native <methods>;
}

-keepclasseswithmembers class * {   # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {# 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {     # 保持枚举 enum 类不被混淆

    public static **[] values();

    public static ** valueOf(java.lang.String);

}

-keep class * implements android.os.Parcelable { # 保持 Parcelable 不被混淆

    public static final android.os.Parcelable$Creator *;

}
-keep public class * implements java.io.Serializable {*;}
-keepclassmembers class * implements java.io.Serializable {
   static final long serialVersionUID;
   private static final java.io.ObjectStreamField[]   serialPersistentFields;
   private void writeObject(java.io.ObjectOutputStream);
   private void readObject(java.io.ObjectInputStream);
   java.lang.Object writeReplace();
   java.lang.Object readResolve();
}
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keep public class com.cgbsoft.privatefund.R$*{
   public static final int *;
}
-dontwarn freemarker.**
-keep class freemarker.** { *;}
-dontwarn android.webkit.**
#如果引用了v4或者v7包

-dontwarn android.support.**

-dontwarn android.support.v4.**

-keep class android.support.v4.** { *; }

-keep public class * extends android.support.v4.**

-keep public class * extends android.app.Fragment

#健康视频混淆--------------------------------------------------------------------------
-keep class tv.danmaku.ijk.** { *; }
-dontwarn tv.danmaku.ijk.**
-keep class com.shuyu.gsyvideoplayer.** { *; }
-dontwarn com.shuyu.gsyvideoplayer.**

# For retrolambda--------------------------------------------------------------------
-dontwarn java.lang.invoke.*
-dontwarn **$$Lambda$*


# RongCloud SDK----------------------------------------------------------------------
-keepattributes Exceptions,InnerClasses
-keepattributes Signature
-keep class io.rong.** {*;}
-keep class * implements io.rong.imlib.model.MessageContent {*;}
-dontwarn io.rong.push.**
-dontnote com.xiaomi.**
-dontnote com.google.android.gms.gcm.**
-dontnote io.rong.**
# VoIP
-keep class io.agora.rtc.** {*;}

# Location
-keep class com.amap.api.**{*;}
-keep class com.amap.api.services.**{*;}

# 红包
-keep class com.google.gson.** { *; }
-keep class com.uuhelper.Application.** {*;}
-keep class net.sourceforge.zbar.** { *; }
-keep class com.google.android.gms.** { *; }
-keep class com.alipay.** {*;}
-keep class com.jrmf360.rylib.** {*;}
-keep class app.privatefund.com.im.listener.MyPushMessageReceive {*;}
-ignorewarnings
#针对gson的配置---------------------------------------------------------------------------
-keep class com.google.gson.** {*;}
#-keep class com.google.**{*;}
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.google.gson.** { *;}
#这句非常重要，主要是滤掉 com.xxx.xxx.bean包下的所有.class文件不进行混淆编译
-keep class com.cgbsoft.privatefund.bean.** {*;}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-dontwarn com.google.gson.**
#针对greenDao数据库的配置-------------------------------------------------------------------
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties

# If you do not use SQLCipher:
-dontwarn org.greenrobot.greendao.database.**
# If you do not use Rx:
-dontwarn rx.**
-dontwarn org.greenrobot.greendao.**
-keep class org.greenrobot.greendao.** { *;}
 # OkHttp---------------------------------------------------------------------------------
 #-dontwarn com.squareup.okhttp.**
 #-keep class com.squareup.okhttp.** {*;}
 #-keep interface com.squareup.okhttp.** {*;}
 #-dontwarn okio.**
 -dontwarn okhttp3.**
 -dontwarn okio.**
 -dontwarn javax.annotation.**
 -dontwarn com.squareup.okhttp.**
 #butterknife---------------------------------------------------------------------------------
 -keep class butterknife.** { *; }
 -dontwarn butterknife.internal.**
 -keep class **$$ViewBinder { *; }
 -dontwarn butterknife.**
 -keepclasseswithmembernames class * {
     @butterknife.* <fields>;
 }
 -keepclasseswithmembernames class * {
     @butterknife.* <methods>;
 }
 # rxjava---------------------------------------------------------------------------------
 -dontwarn rx.**
 -keepclassmembers class rx.** { *; }
 -keep public class * implements com.bumptech.glide.module.GlideModule
 -keep public class * extends com.bumptech.glide.AppGlideModule
 -keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
   **[] $VALUES;
   public *;
 }
# glide--------------------------------------------------------------
 -keep public class * implements com.bumptech.glide.module.GlideModule
 -keep public class * extends com.bumptech.glide.AppGlideModule
 -keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
   **[] $VALUES;
   public *;
 }

 # for DexGuard only
 #-keepresourcexmlelements manifest/application/meta-data@value=GlideModule
 # Only required if you use AsyncExecutor
 -keepclassmembers class * extends de.greenrobot.event.util.ThrowableFailureEvent {
     public <init>(java.lang.Throwable);
 }
 # Don't warn for missing support classes
 -dontwarn de.greenrobot.event.util.*$Support
 -dontwarn de.greenrobot.event.util.*$SupportManagerFragment

 -dontwarn com.tencent.bugly.**
 -keep public class com.tencent.bugly.**{*;}

 #友盟-------------------------------------------------------------------------------------
 -keepclassmembers class * {
    public <init> (org.json.JSONObject);
 }
 -keep public class com.cgbsoft.privatefund.R$*{
 public static final int *;
 }
 -keepclassmembers enum * {
     public static **[] values();
     public static ** valueOf(java.lang.String);
 }
 #retrofit----------------------------------------------------------------------------------
 -dontwarn okio.**
 -dontwarn javax.annotation.**
 -keep class retrofit.** { *; }


 -keep class com.google.api.** { *; }

 -dontwarn sun.misc.Unsafe

 -keepattributes Signature
 -keepattributes InnerClasses
 -keepattributes InnerClasses,EnclosingMethod

 # Router
 -keep class com.chenenyu.router.** {*;}
 -keep class * implements com.chenenyu.router.ParamInjector {*;}