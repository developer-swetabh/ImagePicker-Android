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
