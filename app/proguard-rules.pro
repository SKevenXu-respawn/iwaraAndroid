# ===== Media3 全模块不加密 =====
-keep class androidx.media3.** { *; }
-keep interface androidx.media3.** { *; }
-keep class com.google.android.exoplayer2.** { *; }   # 旧包名也覆盖
-keep interface com.google.android.exoplayer2.** { *; }

# 保持注解、泛型、内部类可见
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes EnclosingMethod

# Transformer 自定义实现类（若你写过）
-keep class * implements androidx.media3.transformer.ExportResult { *; }
-keep class * implements androidx.media3.common.Effect { *; }