# How do I discover memory usage of application in Android?
Note that memory usage on modern operating systems like Linux is an extremely complicated and difficult to understand area. In fact the chances of you actually correctly interpreting whatever numbers you get is extremely low. (Pretty much every time I look at memory usage numbers with other engineers, there is always a long discussion about what they actually mean that only results in a vague conclusion.)

Note: we now have much more extensive documentation on [Managing Your App's Memory](http://developer.android.com/training/articles/memory.html) that covers much of the material here and is more up-to-date with the state of Android.

First thing is to probably read the last part of this article which has some discussion of how memory is managed on Android:

[Service API changes starting with Android 2.0](http://android-developers.blogspot.com/2010/02/service-api-changes-starting-with.html)

Now ActivityManager.getMemoryInfo() is our highest-level API for looking at overall memory usage. This is mostly there to help an application gauge how close the system is coming to having no more memory for background processes, thus needing to start killing needed processes like services. For pure Java applications, this should be of little use, since the Java heap limit is there in part to avoid one app from being able to stress the system to this point.

Going lower-level, you can use the Debug API to get raw kernel-level information about memory usage: [android.os.Debug.MemoryInfo](http://developer.android.com/intl/de/reference/android/os/Debug.html#getMemoryInfo)

Note starting with 2.0 there is also an API, ActivityManager.getProcessMemoryInfo, to get this information about another process: [ActivityManager.getProcessMemoryInfo(int[])](http://developer.android.com/reference/android/app/ActivityManager.html#getProcessMemoryInfo(int[]))

This returns a low-level MemoryInfo structure with all of this data:
		    /** The proportional set size for dalvik. */
		    public int dalvikPss;
		    /** The private dirty pages used by dalvik. */
		    public int dalvikPrivateDirty;
		    /** The shared dirty pages used by dalvik. */
		    public int dalvikSharedDirty;
		
		    /** The proportional set size for the native heap. */
		    public int nativePss;
		    /** The private dirty pages used by the native heap. */
		    public int nativePrivateDirty;
		    /** The shared dirty pages used by the native heap. */
		    public int nativeSharedDirty;
		
		    /** The proportional set size for everything else. */
		    public int otherPss;
		    /** The private dirty pages used by everything else. */
		    public int otherPrivateDirty;
		    /** The shared dirty pages used by everything else. */
		    public int otherSharedDirty;

But as to what the difference is between Pss, PrivateDirty, and SharedDirty... well now the fun begins.

A lot of memory in Android (and Linux systems in general) is actually shared across multiple processes. So how much memory a processes uses is really not clear. Add on top of that paging out to disk (let alone swap which we don't use on Android) and it is even less clear.

Thus if you were to take all of the physical RAM actually mapped in to each process, and add up all of the processes, you would probably end up with a number much greater than the actual total RAM.

The Pss number is a metric the kernel computes that takes into account memory sharing -- basically each page of RAM in a process is scaled by a ratio of the number of other processes also using that page. This way you can (in theory) add up the pss across all processes to see the total RAM they are using, and compare pss between processes to get a rough idea of their relative weight.

The other interesting metric here is PrivateDirty, which is basically the amount of RAM inside the process that can not be paged to disk (it is not backed by the same data on disk), and is not shared with any other processes. Another way to look at this is the RAM that will become available to the system when that process goes away (and probably quickly subsumed into caches and other uses of it).

That is pretty much the SDK APIs for this. However there is more you can do as a developer with your device.

Using adb, there is a lot of information you can get about the memory use of a running system. A common one is the command adb shell dumpsys meminfo which will spit out a bunch of information about the memory use of each Java process, containing the above info as well as a variety of other things. You can also tack on the name or pid of a single process to see, for example adb shell dumpsys meminfo system give me the system process:

		** MEMINFO in pid 890 [system] **
		                    native   dalvik    other    total
		            size:    10940     7047      N/A    17987
		       allocated:     8943     5516      N/A    14459
		            free:      336     1531      N/A     1867
		           (Pss):     4585     9282    11916    25783
		  (shared dirty):     2184     3596      916     6696
		    (priv dirty):     4504     5956     7456    17916
		
		 Objects
		           Views:      149        ViewRoots:        4
		     AppContexts:       13       Activities:        0
		          Assets:        4    AssetManagers:        4
		   Local Binders:      141    Proxy Binders:      158
		Death Recipients:       49
		 OpenSSL Sockets:        0
		
		 SQL
		            heap:      205          dbFiles:        0
		       numPagers:        0   inactivePageKB:        0
		    activePageKB:        0
The top section is the main one, where size is the total size in address space of a particular heap, allocated is the kb of actual allocations that heap thinks it has, free is the remaining kb free the heap has for additional allocations, and pss and priv dirty are the same as discussed before specific to pages associated with each of the heaps.

If you just want to look at memory usage across all processes, you can use the command adb shell procrank. Output of this on the same system looks like:

		  PID      Vss      Rss      Pss      Uss  cmdline
		  890   84456K   48668K   25850K   21284K  system_server
		 1231   50748K   39088K   17587K   13792K  com.android.launcher2
		  947   34488K   28528K   10834K    9308K  com.android.wallpaper
		  987   26964K   26956K    8751K    7308K  com.google.process.gapps
		  954   24300K   24296K    6249K    4824K  com.android.phone
		  948   23020K   23016K    5864K    4748K  com.android.inputmethod.latin
		  888   25728K   25724K    5774K    3668K  zygote
		  977   24100K   24096K    5667K    4340K  android.process.acore
		...
		   59     336K     332K      99K      92K  /system/bin/installd
		   60     396K     392K      93K      84K  /system/bin/keystore
		   51     280K     276K      74K      68K  /system/bin/servicemanager
		   54     256K     252K      69K      64K  /system/bin/debuggerd

Here the Vss and Rss columns are basically noise (these are the straight-forward address space and RAM usage of a process, where if you add up the RAM usage across processes you get an ridiculously large number).

Pss is as we've seen before, and Uss is Priv Dirty.

Interesting thing to note here: Pss and Uss are slightly (or more than slightly) different than what we saw in meminfo. Why is that? Well procrank uses a different kernel mechanism to collect its data than meminfo does, and they give slightly different results. Why is that? Honestly I haven't a clue. I believe procrank may be the more accurate one... but really, this just leave the point: "take any memory info you get with a grain of salt; often a very large grain."

Finally there is the command adb shell cat /proc/meminfo that gives a summary of the overall memory usage of the system. There is a lot of data here, only the first few numbers worth discussing (and the remaining ones understood by few people, and my questions of those few people about them often resulting in conflicting explanations):

		MemTotal:         395144 kB
		MemFree:          184936 kB
		Buffers:             880 kB
		Cached:            84104 kB
		SwapCached:            0 kB
MemTotal is the total amount of memory available to the kernel and user space (often less than the actual physical RAM of the device, since some of that RAM is needed for the radio, DMA buffers, etc).

MemFree is the amount of RAM that is not being used at all. The number you see here is very high; typically on an Android system this would be only a few MB, since we try to use available memory to keep processes running

Cached is the RAM being used for filesystem caches and other such things. Typical systems will need to have 20MB or so for this to avoid getting into bad paging states; the Android out of memory killer is tuned for a particular system to make sure that background processes are killed before the cached RAM is consumed too much by them to result in such paging.