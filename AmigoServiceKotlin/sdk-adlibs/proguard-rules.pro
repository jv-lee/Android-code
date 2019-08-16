###
# Mobgi
###
-dontwarn
-dontoptimize
-keepattributes InnerClasses
-keepattributes *Annotation*
-keepattributes Signature 
-keepattributes *JavascriptInterface*

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable { 
  public static final android.os.Parcelable$Creator *;
}
-keepnames class * implements java.io.Serializable

-dontnote android.net.http.*
-dontnote org.apache.http.**

-keep public class com.mobgi.* {*;}
-keep public class com.mobgi.adutil.parser.NativeAdBeanPro {*;}
-keep public interface com.mobgi.** {*;}
-keep public class com.mobgi.platform.** {*;}
-keep class net.skey.mob.std.dsr.ut.** { *; }

###
# AdView
###
-dontwarn
-keep public class com.kyview.** {*;}
-keepclassmembers class * {public *;}
-keep public class com.kuaiyou.**.** {*;}
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose

###
# Baidu
###
-keepclassmembers class * extends android.app.Activity {
	public void *(android.view.View);
}
-keepclassmembers enum * {
	public static **[] values();
	public static ** valueOf(java.lang.String);
}
-keep class com.baidu.mobads.*.** { *; }

###
# GDT
###
 -keep class com.qq.e.** {
    public protected *;
}
-keep class android.support.v4.**{
    public *;
}
-keep class android.support.v7.**{
    public *;
}

###
# Mintegral
###
-keepattributes Signature   
-keepattributes *Annotation*   
-keep class com.mintegral.** {*; }  
-keep interface com.mintegral.** {*; }  
-keep class android.support.v4.** { *; }  
-dontwarn com.mintegral.**   
-keep class **.R$* { public static final int mintegral*; }
-keep class com.alphab.** {*; }
-keep interface com.alphab.** {*; }

###
# Toutiao
###
-keep class com.bytedance.sdk.openadsdk.** { *; }
-keep class com.androidquery.callback.** {*;}
-keep public interface com.bytedance.sdk.openadsdk.downloadnew.** {*;}

###
# Uniplay
###
-dontwarn com.uniplay.**
-keep class com.uniplay.** { *; }

###
# Unity
###
# Keep filenames and line numbers for stack traces
-keepattributes SourceFile,LineNumberTable
# Keep JavascriptInterface for WebView bridge
-keepattributes JavascriptInterface
# Sometimes keepattributes is not enough to keep annotations
-keep class android.webkit.JavascriptInterface {
   *;
}
# Keep all classes in Unity Ads package
-keep class com.unity3d.ads.** {
   *;
}