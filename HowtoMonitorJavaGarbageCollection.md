# how JVM is actually running Garbage Collection in the real time.

## What is GC Monitoring? 
Garbage Collection Monitoring refers to the process of figuring out how JVM is running GC. For example, we can find out:
- when an object in young has moved to old and by how much,
- or when stop-the-world has occurred and for how long.

GC monitoring is carried out to see if JVM is running GC efficiently, and to check if additional GC tuning is necessary. 
Based on this information, the application can be edited or GC method can be changed (GC tuning).

## How to Monitor GC?
There are different ways to monitor GC, but the only difference is how the GC operation information is shown. 
GC is done by JVM, and since the GC monitoring tools disclose the GC information provided by JVM,
you will get the same results no matter how you monitor GC. Therefore, you do not need to learn all methods to monitor GC, 
but since it only requires a little amount of time to learn each GC monitoring method,
knowing a few of them can help you use the right one for different situations and environments.

The tools or JVM options listed below cannot be used universally regardless of the HVM vendor.
This is because there is no need for a "standard" for disclosing GC information. 
In this example we will use HotSpot JVM (Oracle JVM). 
Since NHN is using Oracle (Sun) JVM, there should be no difficulties in applying the tools or JVM options that we are explaining here.

First, the GC monitoring methods can be separated into CUI and GUI depending on the access interface. 
The typical CUI GC monitoring method involves using a separate CUI application called "jstat", 
or selecting a JVM option called "verbosegc" when running JVM.

GUI GC monitoring is done by using a separate GUI application, and three most commonly used applications would be "jconsole", 
"jvisualvm" and "Visual GC".

### jstat
jstat is a monitoring tool in HotSpot JVM. 
Other monitoring tools for HotSpot JVM are jps and jstatd. 
Sometimes, you need all three tools to monitor a Java application.

jstat does not provide only the GC operation information display. 
It also provides class loader operation information or Just-in-Time compiler operation information. 
Among all the information jstat can provide, 
in this article we will only cover its functionality to monitor GC operating information.

jstat is located in $JDK_HOME/bin, so if java or javac can run without setting a separate directory from the command line, so can jstat.

For example, running "jstat –gc <vmid> 1000" (or 1s) will display the GC monitoring data on the console every 1 second. 
"jstat –gc <vmid> 1000 10" will display the GC monitoring information once every 1 second for 10 times in total.

There are many options other than -gc, among which GC related ones are listed below.
ption Name	Description
- gc	It shows the current size for each heap area and its current usage (Ede, survivor, old, etc.), total number of GC performed, and the accumulated time for GC operations.
- gccapactiy	It shows the minimum size (ms) and maximum size (mx) of each heap area, current size, and the number of GC performed for each area. (Does not show current usage and accumulated time for GC operations.)
- gccause	It shows the "information provided by -gcutil" + reason for the last GC and the reason for the current GC.
- gcnew	Shows the GC performance data for the new area.
- gcnewcapacity	Shows statistics for the size of new area.
- gcold	Shows the GC performance data for the old area.
- gcoldcapacity	Shows statistics for the size of old area.
- gcpermcapacity	Shows statistics for the permanent area.
- gcutil	Shows the usage for each heap area in percentage. Also shows the total number of GC performed and the accumulated time for GC operations.

Only looking at frequency, you will probably use -gcutil (or -gccause), -gc and -gccapacity the most in that order.
- gcutil is used to check the usage of heap areas, the number of GC performed, and the total accumulated time for GC operations,
-gccapacity option and others can be used to check the actual size allocated.

Different jstat options show different types of columns, which are listed below. 
Each column information will be displayed when you use the "jstat option" listed on the right.
ref: [how-to-monitor-java-garbage-collection](http://www.cubrid.org/blog/dev-platform/how-to-monitor-java-garbage-collection/)
