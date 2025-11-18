# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Keep line number information for debugging stack traces
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Gson: Keep all data model classes used for JSON serialization
-keep class com.geniusjun.lotto.data.model.** { *; }

# Retrofit: Keep interfaces and their methods
-keep interface com.geniusjun.lotto.data.api.** { *; }

# Kotlin: Keep data classes
-keepclassmembers class * {
    @kotlinx.serialization.SerialName <fields>;
}

# OkHttp: Keep platform classes
-dontwarn okhttp3.**
-dontwarn okio.**

# Google Play Services Auth
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

# Compose: Keep Compose runtime
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**