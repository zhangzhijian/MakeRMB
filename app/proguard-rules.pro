# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/zengliangdong/Public/software/android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keep class com.dream.makermb.model.** { *; }
-keep class com.dream.makermb.model.JSInterface { *; }
-keepattributes *Annotation*
-keepattributes *JavascriptInterface*

# Keep - Library. Keep all public and protected classes, fields, and methods.


# Also keep - Enumerations. Keep the special static methods that are required in
# enumeration classes.
-keepclassmembers enum  * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Also keep - Serialization code. Keep all fields and methods that are used for
# serialization.
-keepclassmembers class * extends java.io.Serializable {
    static final long serialVersionUID;
    static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Keep names - Native method names. Keep all native class/method names.
-keepclasseswithmembers,allowshrinking class * {
    native <methods>;
}

# Keep names - _class method names. Keep all .class method names. This may be
# useful for libraries that will be obfuscated again with different obfuscators.
-keepclassmembers,allowshrinking class * {
    java.lang.Class class$(java.lang.String);
    java.lang.Class class$(java.lang.String,boolean);
}
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

# -dontnote android.support.**
# -dontwarn android.support.**
-dontnote com.android.internal.**
-dontwarn com.android.internal.**
-dontwarn com.google.**
-dontwarn com.google.**
-ignorewarnings

#-libraryjars   libs/android-support-v4.jar
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment

#-libraryjars   libs/android-support-v7.jar
-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.app.** { *; }
-keep public class * extends android.support.v7.**

#-libraryjars   libs/android-design.jar
-dontwarn android.design.widget.**
-keep class android.design.widget.** { *; }
-keep interface android.design.R.** { *; }
-keep public class * extends android.design.widget.**

#keep line number when umeng crash, upload new mapping.txt
-keepattributes SourceFile,LineNumberTable

-keep class com.iflytek.** { *; }

# -keep class android.support.** { *; }

-keep class com.android.internal.** { *; }

-keep class com.google.gson.** { *; }

-keep class com.umeng.** { *; }

-keep class com.dream.makermb.model.** { *; }

-keep class com.alipay.android.app.IAliPay{*;}
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}

-dontwarn com.igexin.**
-keep class com.igexin.**{*;}

-keepattributes Exceptions,InnerClasses
-keep class io.rong.** {*;}
-keepclassmembers class * extends com.sea_monster.dao.AbstractDao {
 public static java.lang.String TABLENAME;
}
-keep class **$Properties
-dontwarn org.eclipse.jdt.annotation.**
-keep class com.ultrapower.** {*;}
-keepclassmembers class **.R$* {
    public static <fields>;
}
-keep class com.dream.makermb.R$*{*;}
-keepclassmembers class com.dream.makermb.R$* {
    public static <fields>;
}

-keep class com.tencent.mm.sdk.openapi.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*;}
-dontwarn com.tencent.mm.**
-keep class com.tencent.mm.**{*;}

#-libraryjars libs/baidumapapi
-keep class com.baidu.** { *; }
-keep class vi.com.gdi.bgl.android.**{*;}
-dontwarn com.baidu.**