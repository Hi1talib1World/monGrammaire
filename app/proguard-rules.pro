# project specific ProGuard rules
# -----------------------------------------------------------------------------------------

# --- ButterKnife ---
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **_ViewBinding { *; }
-keepclassesmembers class * {
    @butterknife.* <fields>;
    @butterknife.* <methods>;
}

# --- Firebase ---
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.firebase.**
-dontwarn com.google.android.gms.**

# --- Glide ---
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.VideoDecoder$* { *; }
-dontwarn com.bumptech.glide.load.resource.bitmap.VideoDecoder

# --- EventBus ---
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# --- Hawk ---
-keep class com.orhanobut.hawk.** { *; }

# --- Gson ---
-keep class com.google.gson.** { *; }
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }

# --- iText ---
-dontwarn com.itextpdf.**
-keep class com.itextpdf.** { *; }

# --- Volley ---
-keep class com.android.volley.** { *; }

# --- Kotlin Coroutines ---
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepnames class kotlinx.coroutines.android.AndroidExceptionPreHandler {}
-keepnames class kotlinx.coroutines.android.AndroidDispatcherFactory {}
-dontwarn kotlinx.coroutines.**

# --- General ---
-keepattributes SourceFile,LineNumberTable
-keepattributes Signature
-keepattributes EnclosingMethod,InnerClasses
-dontwarn javax.annotation.**
-dontwarn org.checkerframework.**
-dontwarn com.google.errorprone.annotations.**
