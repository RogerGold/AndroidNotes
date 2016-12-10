# Android   Security

Security in Android is something you can’t be sure about. You as a developer don’t know if your app is secured enough or not. 
Each system can be cracked but you can make the life of the hacker much more effortful.

### Network
Almost all applications nowadays exchange user’s data, tokens with the server via the Internet.
You should think about the safety of users’ Internet connection and protect their info from being stolen.
The first step to make your connection more protected is to use HTTPS, but it’s not enough of course.

One of the most popular network attacks is Man-In-The-Middle (MITM). It can be passive or active.
To protect your application from a passive MITM you can just use Diffie-Hellman key exchange algorithm.

An active attack is a bit stronger. To prevent data from being stolen you can use SSL pinning.
There are some tools that support HTTPS and SSL pinning. 
Two of them are [Retrofit](https://square.github.io/retrofit/) and [OkHttp](http://square.github.io/okhttp/) and we in UPTech use them almost everywhere.

Retrofit is easy to use, it supports RxJava, and it doesn’t take much time to configure it.

With the help of OkHttp you can add your own trusted SSL certificate.
“By default, OkHttp trusts the certificate authorities of the host platform. 
Certificate pinning increases security, but limits your server team’s abilities to update their TLS certificates. 
Do not use certificate pinning without the blessing of your server’s TLS administrator! “— Jesse Wilson, Square Inc.

You can learn more about SSL pinning and how to install it here:
[SSL Pinning on Android](https://medium.com/@richa123/ssl-pinning-on-android-8baa822e3bd5#.xyiz8mf06)
[security-ssl](https://developer.android.com/training/articles/security-ssl.html)

### Intents
You know that an application can communicate with other apps using Intents. There are two types of intents: Explicit and Implicit.
#### Explicit Intents
Pros: They are impossible to be intercepted.
Cons: Work only inside the application.
#### Implicit Intents
Pros: Work everywhere in the Android OS.
Cons: Easy to intercept.
By defining the same intent-filters a hacker can easily intercept your intent and get all data.

To avoid this you can make your intent explicit as much as possible. For example, you can specify the package.

    Intent intent = new Intent(Intent.ACTION_SEND);
    intent.setPackage("com.test.package");
    sendBroadcast(intent);
Broadcast Receivers or Services that don’t communicate with external components shouldn’t be exported.
    <service
        android:name=".service.SomeService"
        android:enabled="true"
        android:exported="false">

        <intent-filter>
            <action android:name="android.intent.action.MAIN" />
            <category android:name="android.intent.category.LAUNCHER" />
        </intent-filter>
    </service>
### Summary
Try to use Explicit Intents in your application where possible. Explicitly specify the receiver of the intent you want to send.

## Data Storage
### Memory cache
Memory cache is useful when you retrieve data from a server and want to save this data for a limited time.
Or maybe you are processing bitmaps and want to use the ready-to-use pictures somewhere else.
Or you need to save some user data, ids, etc.
Here is an example of how to create a temp file in Memory Cache:
    File outputDir = this.getCacheDir();
    File outputFile = File.createTempFile("prefix", "extension", outputDir);
It’s much harder for a third person to dump memory than to just open a file in external storage and get the passwords. 
Dumping memory requires ROOT access.

Here is the official documentation of using Memory Cache (caching bitmaps example):
[Caching Bitmaps](https://developer.android.com/training/displaying-bitmaps/cache-bitmap.html)

### Cipher
Also, you can encode your data before storing it in the cache, DB or in SharedPreferences. For this purpose I recommend to use Cipher.
Cipher is a built-in tool for encoding/decoding your data.This class provides the functionality of a cryptographic cipher for encryption and decryption. 
It forms the core of the Java Cryptographic Extension (JCE) framework.

For more information about Cipher parameters check here:
[Cipher](https://docs.oracle.com/javase/7/docs/api/javax/crypto/Cipher.html)

Here is an example of using Cipher:
       private static byte[] encrypt(byte[] key, byte[] input) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(input);
        return encrypted;
    }

    private static byte[] decrypt(byte[] key, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }
You should know that some cryptography algorithms (e.g. AES) require fixed key length, such as 128, 192, 256 bits.

Detailed information about the algorithms Cipher supports here:
[Cipher Algorithm Names](https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#Cipher)

### Shared Preferences
Like in other examples, don’t put plain text in Shared Preferences, encrypt all data. 
Everything you want to put to the preferences should be written with “MODE_PRIVATE” property which is set by default.

With “MODE_PRIVATE” you can be sure that only your app can read and write into preferences.
 And of course without ROOT access nobody can access your data.
 
 There is another tool which is called SecureSharedPreferences, you can read more following the link:
 [secure-preferences](https://github.com/scottyab/secure-preferences)
 
 It is easy to use, it is based on default Shared Preferences but with a bunch of cryptographical algorithms on top. 
 You don’t need to get used to something new, just follow default way of using Shared Preferences.

     SharedPreferences prefs = new SecurePreferences(context, "userpassword", "my_user_prefs.xml");
     
This is what it looks like after the encryption:

      <map>
          <string name="TuwbBU0IrAyL9znGBJ87uEi7pW0FwYwX8SZiiKnD2VZ7">
         pD2UhS2K2MNjWm8KzpFrag==:MWm7NgaEhvaxAvA9wASUl0HUHCVBWkn3c2T1WoSAE/g=rroijgeWEGRDFSS/hg
          </string>
          <string name="8lqCQqn73Uo84Rj">k73tlfVNYsPshll19ztma7U>
      </map>    
      
### Keys
Next problem is where to store the key for your algorithm.
It is a common problem and there is no best answer. No matter where you hide your key, 
a hacker will find it anyway. The only thing that can stop a hacker from cracking your app is 
the complexity of the algorithm and time which is required to find the key.

You can use the KeyStore, but it is available only from Android API 18. And another problem is ROOT again,
using ROOT access hacker can retrieve everything.

Or you can use Java Native Interface (JNI). It is harder to decompile C/C++ compiled code. 
Decompilers like [JaDx](https://github.com/skylot/jadx), [dex2jar](https://github.com/pxb1988/dex2jar) won’t help because they are Java-oriented decompilers.

Another possible variant is to use steganography algorithms and hide your keys wherever you want.
E.g. you can replace some bits in the picture with your key’s bits.

All the methods above can add extra time to the “health” of your app. Use something is better than use nothing.

### Summary
There are a ton of algorithms how to hide, encrypt your keys or data. 
Even the best, perverted algorithm ever can’t protect your data.
The best variant is not storing anything, but sometimes it is impossible.

## Other recommendations
There are some more recommendations on what you can do to increase the security of your app:
### Emulator
Check if the emulator is running. For this purpose you can use the Build class from android.os package.
It contains a lot of information about the device the app is launched on. Check this link if you are interested:
[android-emulator-detector](https://github.com/framgia/android-emulator-detector)
 ### Debug
Check if the debugger is connected. The simplest solution is:
    Debug.isDebuggerConnected()
### Root
Check if the device is rooted. To check if your device is rooted you can use app that allows you to use terminal. Check this sample:
      private static boolean isRooted() {
          return findBinary("su");
      }

      public static boolean findBinary(String binaryName) {
          boolean found = false;
          if (!found) {
              String[] places = {"/sbin/", "/system/bin/",
                      "/system/xbin/", "/data/local/xbin/",
                      "/data/local/bin/", "/system/sd/xbin/",
                      "/system/bin/failsafe/", "/data/local/"};
              for (String where : places) {
                  if (new File(where + binaryName).exists()) {
                      found = true;
                      break;
                  }
              }
          }
          return found;
      }
This code is borrowed from the RootTools library: [RootTools](https://github.com/Stericson/RootTools)

su command is usually used to change the ownership from an original user to the root user. Check out [this link](http://www.linfo.org/su.html) to learn more about su.

Notify your server, interrupt connection, crash your app if one of the checks returns true.
But don’t go too far or your UX will die in the user’s eyes and nobody will use your app.

### Strings
Use char array instead of string literals or create a string with the new operator 
so the new string will be in the heap and not in the string pool.

    String a = new String("a");
    a = new String("a");
    
 In the example above, two objects will be created. After the second line is executed, the Garbage Collector (GC) can remove the first object.
 
    String a = "a";
    String b = "a";
 In this example, only one object will be created. Because here string literals are used, object will be in string pool and the GC won’t kill it shortly.
 
 ### Obfuscation
 Use obfuscators such as [ProGuard](https://www.guardsquare.com/en/proguard), [DexGuard](https://www.guardsquare.com/en/dexguard), [DexProtector](https://dexprotector.com/) etc.
 
 ProGuard is the default Android Studio minificator and it can obfuscate your code too. 
 A ProGuard config file is automatically created when you create a new Android Studio project.
 It is in your project with proguard-rules.pro name.
 
 There you can specify rules you want to be executed while building your APK.
Here you can find some rules for popular libraries:
[android-proguard-snippets](https://github.com/krschultz/android-proguard-snippets/tree/master/libraries)

To enable ProGuard you should do this:
     android {
       buildTypes {
          dev {
              minifyEnabled true  // enables ProGuard
              proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
          }
    }
    
 Using obfuscators you can tangle the hacker and it will take him much more time to crack your application.
There are a lot of companies which provide obfuscation tools. Here is a list of the most popular obfuscators:
[proguard](https://www.guardsquare.com/en/proguard)

The best of them are quite expensive, but they can transform your app into a one file code without any understandable logic. 
And that is not the end, checks for root/debug/emulator/MITM/Code modification are also available but it costs much more.

### Useful links
- Official Security Documentation:[Security](https://source.android.com/security/index.html)
- Top 10 Mobile Risks:[OWASP Mobile Security Project](https://www.owasp.org/index.php/OWASP_Mobile_Security_Project#tab=Top_10_Mobile_Risks)
- Security testing tools:[Top 12 Best Pentesting Tools & Apps For Android](https://www.qdtricks.net/android-penetration-testing-apps/)
