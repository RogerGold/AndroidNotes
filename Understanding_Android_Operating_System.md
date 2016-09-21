# 	Understanding Android Operating System


![android_OS](https://github.com/RogerGold/media/blob/master/android_os.jpg)

### Linux Kernel
Starting from the bottom we have Linux Kernel , Android is built up on the Linux Kernel. Linux is already being used extensively from so many years and it’s kernel had received so many security patches. Linux Kernel provides basic system functionality like process management, memory management, device management like camera, keypad, display etc. Also, the kernel handles all the things that Linux is really good at such as networking and a vast array of device drivers, which take the pain out of interfacing to peripheral hardware.

 What actually Linux Kernel Offers Android ?

As the base for a mobile computing environment, the Linux kernel provides Android with several key security features, including:

- A user-based permissions model
- Process isolation
- Extensible mechanism for secure IPC
- The ability to remove unnecessary and potentially insecure parts of the kernel

As a multiuser operating system, a fundamental security objective of the Linux kernel is to isolate user resources from one another. The Linux security philosophy is to protect user resources from one another. Thus, Linux:

- Prevents user A from reading user B’s files
- Ensures that user A does not exhaust user B’s memory
- Ensures that user A does not exhaust user B’s CPU resources
- Ensures that user A does not exhaust user B’s devices (e.g. telephony, GPS, bluetooth)

### Hardware Abstraction Layer
Hardware Abstraction Layer just gives Applications direct access to the Hardware resources.

### Android Runtime and Dalvik
Moving on to the third part comes the Libraries , Android Runtime and Dalvik. The libraries shown in the image are very necessary without which application will not run like Webkit library is used for browsing the web , SQLite library is used for maintaining SQL database and so on.
### Dalvik Virtual Machine
Dalvik Virtual Machine which is specifically designed by Android Open Source Project to execute application written for Android.Each app running in the Android Device has its own Dalvik Virtual Machine .

### Android Runtime (ART) 
Android Runtime (ART) is a alternative to Dalvik Virtual Machine which has been released with Android 4.4 as an experimental release,in Android Lollipop(5.0) it will completely replace Dalvik Virtual Machine.Major change in ART is because of Ahead-of-time(AOT) Compilation and Garbage Collection. In Ahead-of-time(AOT) Compilation ,android apps will be compiled when user installs them on their device whereas in the Dalvik used Just-in-time(JIT) compilation in which bytecode are compiled when user runs the app. Moving to the last one ,these are common.

### Application Framework

The Application Framework layer provides many higher-level services to applications in the form of Java classes. Application developers are allowed to make use of these services in their applications.