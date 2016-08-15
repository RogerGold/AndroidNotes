# How we reduced our Android app size by 65%
Most of the times it’s the small things in life that give you happiness. Android users love applications when they are of small size and easy to download on the go. Here is a story of how we reduced our Android application size by 65% within few days of launch.

### 1. Use .svg format icon set
Its time to say bye to PNGs and welcome vector drawables. There are a couple of benefits of using them. We don’t have to worry about different device DPI’s and it also helps in reducing apk size. With Support Library 23.2, we can now use the app:srcCompat property of ImageView instead of android:src to make it backward compatible.

When you are downloading system app icon set from Google’s [Material Design Icon Library](https://design.google.com/icons/), [download](https://github.com/google/material-design-icons) .svg format instead of .pngs. This helped us reduce our app size by 1 Mb.

### 2. Compress PNGs
We are using PNGs for our walkthrough screens for all screen densities. The PNGs were of very high quality and size which just bloated up our app size! 

when we started optimizing our app size, we compressed our walkthrough screens and boom! The walkthrough screens were ~1/10th size it was before! (Yes! 1/10th). That’s crazy!

 We quickly created a build and checked the image quality in various screen densities, looked just the same. A quick Google search will give you a lot of tools that will help you compress your PNGs. We also got rid of the ldpi resources after seeing the [Device Metrics](https://design.google.com/devices/) shared by the Google Design team.

### 3. Use only specific libraries of Google Play Services.
Prior to Google Play Services 6.5 we had to compile the entire package. But now we can selectively compile it into our app. We are now just using Google Cloud Messaging, Google Maps and Google Location APIs.

### 4. Use Proguard
Proguard is used for code obfuscation and it also removes unused Java code from the dependencies. The result after using Proguard is a smaller apk file size which is difficult to reverse engineer. To enable proguard:

build.gradle

		android {
		    ...
		    buildTypes {
		        release {
		            minifyEnabled true
		            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
		        }
		    }
		}

### 5. Shrink Resources
The Android Gradle plugin supports the ability to automatically exclude unused resources during the build process using shrinkResources gradle property. This alone reduced the apk size by ~0.5 Mb. To take advantage of this in your release builds, just add shrinkResources true to your release configuration.

build.gradle

		android {
		    ...
		    buildTypes {
		        release {
		            shrinkResources true
		            minifyEnabled true
		            ...
		        }
		    }
		}

These are small things and I am sure it won’t take much of your time to implement. But, in the end, you would end with a leaner app size and definitely a smile on your face. Please feel free to comment if you have newer techniques as well as ask questions. And, yeah! If this post helps you please do recommend and spread the word!