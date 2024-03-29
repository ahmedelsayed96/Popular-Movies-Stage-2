# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/mido/Downloads/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html
#-keep class com.google.api.services.** { *; }
#-keep class com.google.android.youtube.player.** { *; }
## Needed by google-api-client to keep generic types and @Key annotations accessed via reflection
#-keepclassmembers class * {
#  @com.google.api.client.util.Key <AIzaSyCDKNxMItBqn3B7Ipvuy3Dx9SpC46TmaBg>;
## Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
