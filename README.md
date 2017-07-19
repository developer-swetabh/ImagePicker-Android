# ImagePicker-Android

This ImagePicker Library for android can be used to pick images either from camera or gallery. This library enforces you to implement file provider so that your app will work in Nougat (Android 7) as well as previous versions of android without any issues. No need to handle required permessions explicitly (like storage or camera permission). This Library currently works for single image picker, multiple image selection is under progress..

# Gradle Dependency

### Repository

The Gradle dependency is available via [jCenter](https://bintray.com/swetabh-suman/ImagePicker/imagepicker).
jCenter is the default Maven repository used by Android Studio.

The minimum API level supported by this library is API 16 (JellyBean).

```gradle
dependencies {
	// ... other dependencies here
    compile 'com.swetabh.imagepicker:imagepicker:0.1.0'
}
```

# Creating File Provider

For more recent apps targeting Android 7.0 (API level 24) and higher, passing a file:// URI across a package boundary causes a [FileUriExposedException](https://developer.android.com/reference/android/os/FileUriExposedException.html). Therefore, we are now using more generic way of storing images using a FileProvider.

For more detail on how to create a file provider, you can refer [here..](https://developer.android.com/training/camera/photobasics.html)
