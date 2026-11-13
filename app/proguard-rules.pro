# Add project specific ProGuard rules here.
# Preserve line numbers and annotations for clean stack traces and reflection-based libraries.
-keepattributes SourceFile,LineNumberTable,*Annotation*,Signature,InnerClasses,EnclosingMethod

# Room Database & Entities
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keepclassmembers class * {
    @androidx.room.* <fields>;
    @androidx.room.* <methods>;
}
-keep class com.example.data.model.** { *; }
-keep class com.example.data.db.** { *; }

# Firebase Auth & Cloud Firestore
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**
-keepclassmembers class * {
    @com.google.firebase.firestore.* <fields>;
    @com.google.firebase.firestore.* <methods>;
}

# Google Play Services & Credential Manager
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**
-keep class androidx.credentials.** { *; }
-keep class com.google.android.libraries.identity.googleid.** { *; }

# Moshi & Retrofit / OkHttp Networking
-keep class com.squareup.moshi.** { *; }
-keepclassmembers class * {
    @com.squareup.moshi.Json <fields>;
    @com.squareup.moshi.JsonClass *;
}
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn retrofit2.**

# Keep Receivers referenced in AndroidManifest.xml
-keep class com.example.receiver.** { *; }
