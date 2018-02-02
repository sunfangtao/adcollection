# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Application\Android\sdk/tools/proguard/proguard-android.txt
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

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-ignorewarnings
-dontwarn

-dontskipnonpubliclibraryclassmembers
-dontskipnonpubliclibraryclasses

-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keepattributes SourceFile,LineNumberTable

-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

-keep class org.jivesoftware.** {*;}
-keep class org.apache.** {*;}

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-dontwarn vi.com.gdi.bgl.android.**
-keep class vi.com.gdi.bgl.android.**{ *; }
-keep class com.google.** { *; }
-keep class android.support.v4.**{ *; }
-keep class android.support.v7.**{ *; }
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
    public <fields>;
}

-keepattributes Signature
-keep public class * implements java.io.Serializable { *; }
-keep class com.google.gson.examples.android.model.** { *; }

-keep class cz.msebera.android.httpclient.**{ *; }
-keep class com.squareup.picasso.**{ *; }
-keep class com.edmodo.cropper.**{ *; }
-keep class antistatic.spinnerwheel.**{ *; }
-keep class com.iflytek.**{ *; }
-keep class com.jph.takephoto.**{ *; }
-keep class com.nineoldandroids.**{ *; }
-keep class soundcloud.android.crop.**{ *; }
-keep class cn.sft.**{ *; }

# 实体类
-keep class com.sft.adcollection.bean**{ *; }

#  3D 地图
-keep   class com.amap.api.mapcore.**{*;}
-keep   class com.amap.api.maps.**{*;}
-keep   class com.autonavi.amap.mapcore.*{*;}

#  定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

# 搜索
-keep   class com.amap.api.services.**{*;}

#  2D地图
-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}

#  导航
-keep class com.amap.api.navi.**{*;}
-keep class com.autonavi.**{*;}
