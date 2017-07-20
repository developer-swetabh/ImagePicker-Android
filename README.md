# ImagePicker-Android

This ImagePicker Library for android can be used to pick images either from **Camera** or **Gallery**. This library enforces you to implement file provider so that your app will work in **Nougat (Android 7)** as well as previous versions of android without any issues. **No need to handle required permessions explicitly (like storage or camera permission)**. This Library currently works for single image picker, multiple image selection is under progress..

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
# Using ImagePicker Library

### Initializing the library

For start using the [ImagePicker-Library](https://github.com/developer-swetabh/ImagePicker-Android), First you need to add the dependency as shown above. Right after that, In your activity first you need to create the Object of `ImagePicker` class.

```java
public class MainActivity extends AppCompatActivity {

    public ImagePicker mImagePicker;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
	//Initialize here with context and string for authorities
	mImagePicker = new ImagePicker(MainActivity.this, "Your Authorities String");
    }
}
```

### Using the Library

Now you can use the `mImagePicker` object to use the library as shown below.

For Camera..

```java
private void pickFromCamera() {
	mImagePicker.pickImageFromCamera();
}
```

For Gallery..

```java
private void pickFromGallery() {
	mImagePicker.pickImageFromGallery();
}
```

### Getting the Result

For getting the results or the path of the image you have selected, you should override the Activity's `onActivityResult()` as shown below..

```java
@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String imagePath = mImagePicker.onActivityResult(requestCode, resultCode, data);
        
        if(imagePath != null){
		Log.d("PickedImagePath", imagePath);
	}
	
	//use imagePath variable to show image
	
    }
```

# Creating File Provider

For more recent apps targeting **Android 7.0 (API level 24) and higher**, passing a `file://` URI across a package boundary causes a [FileUriExposedException](https://developer.android.com/reference/android/os/FileUriExposedException.html). Therefore, we are now using more generic way of storing images using a [FileProvider](https://developer.android.com/reference/android/support/v4/content/FileProvider.html).

This is how you can configure your [FileProvider](https://developer.android.com/reference/android/support/v4/content/FileProvider.html). In your app's `Manifest`, add a provider to your application:

```xml
<application>
   ...
   <provider
        android:name="android.support.v4.content.FileProvider"
        android:authorities="@string/authorities"
        android:exported="false"
        android:grantUriPermissions="true">
        <meta-data
            android:name="android.support.FILE_PROVIDER_PATHS"
            android:resource="@xml/file_paths"></meta-data>
    </provider>
    ...
</application>
```

Make sure that the authorities string matches with the string you are passing to constructor of library as follow. It would be better to store authorities string in `strings.xml` file to reduce the chances of conflicts. 

```xml
<resources>
    <string name="authorities">com.example.android.fileprovider</string>
</resources
```

```java
ImagePicker picker = new ImagePicker(MainActivity.this, R.string.authorities);
```

In the meta-data section of the provider definition, you can see that the provider expects eligible paths to be configured in a dedicated resource file, `res/xml/file_paths.xml`. Here is the content required for this particular example:

```xml
<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <external-path name="my_images" path="Android/data/com.example.package.name/files/Pictures" />
</paths>
```

For more detail on how to create a file provider, you can refer [Google's Android Documentation](https://developer.android.com/reference/android/support/v4/content/FileProvider.html)
