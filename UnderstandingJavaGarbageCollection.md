# Understanding Java Garbage Collection
### should know before learning about GC
 The term is "stop-the-world." Stop-the-world will occur no matter which GC algorithm you choose. 
 Stop-the-world means that the JVM is stopping the application from running to execute a GC. When stop-the-world occurs, 
 every thread except for the threads needed for the GC will stop their tasks. 
 The interrupted tasks will resume only after the GC task has completed. 
 GC tuning often means reducing this stop-the-world time.
 
 ### Generational Garbage Collection 
 Java does not explicitly specify a memory and remove it in the program code. 
 Some people sets the relevant object to null or use System.gc() method to remove the memory explicitly.
 Setting it to null is not a big deal,
 but calling System.gc() method will affect the system performance drastically, and must not be carried out. 
 
 In Java, as the developer does not explicitly remove the memory in the program code,
 the garbage collector finds the unnecessary (garbage) objects and removes them. 
 This garbage collector was created based on the following two hypotheses. 
 (It is more correct to call them suppositions or preconditions, rather than hypotheses.) 
- Most objects soon become unreachable.
- References from old objects to young objects only exist in small numbers.

These hypotheses are called the weak generational hypothesis.
So in order to preserve the strengths of this hypothesis,
it is physically divided into two - young generation and old generation - in HotSpot VM.
#### Young generation
Young generation: Most of the newly created objects are located here.
Since most objects soon become unreachable, many objects are created in the young generation, then disappear.
When objects disappear from this area, we say a "minor GC" has occurred. 
#### Old generation
Old generation: The objects that did not become unreachable and survived from the young generation are copied here. 
It is generally larger than the young generation. 
As it is bigger in size, the GC occurs less frequently than in the young generation. 
When objects disappear from the old generation, we say a "major GC" (or a "full GC") has occurred. 

![java GC](https://github.com/RogerGold/media/blob/master/java_GC.PNG)

The permanent generation from the chart above is also called the "method area," 
and it stores classes or interned character strings. 
So, this area is definitely not for objects that survived from the old generation to stay permanently.
A GC may occur in this area. The GC that took place here is still counted as a major GC. 

What if an object in the old generation need to reference an object in the young generation?
To handle these cases, there is something called the a "card table" in the old generation, which is a 512 byte chunk.
Whenever an object in the old generation references an object in the young generation, it is recorded in this table. 
When a GC is executed for the young generation, only this card table is searched to determine whether or not it is subject for GC,
instead of checking the reference of all the objects in the old generation.
This card table is managed with write barrier. This write barrier is a device that allows a faster performance for minor GC. 
Though a bit of overhead occurs because of this, the overall GC time is reduced. 

![CardTable_GC](https://github.com/RogerGold/media/blob/master/CardTable_GC.PNG)

### Composition of the Young Generation
In order to understand GC, let's learn about the young generation, where the objects are created for the first time. 
The young generation is divided into 3 spaces. 
- One Eden space
- Two Survivor spaces

There are 3 spaces in total, two of which are Survivor spaces. The order of execution process of each space is as below:
1. The majority of newly created objects are located in the Eden space.
2. After one GC in the Eden space, the surviving objects are moved to one of the Survivor spaces. 
3. After a GC in the Eden space, the objects are piled up into the Survivor space, where other surviving objects already exist.
4. Once a Survivor space is full, surviving objects are moved to the other Survivor space. Then, the Survivor space that is full will be changed to a state where there is no data at all.
5. The objects that survived these steps that have been repeated a number of times are moved to the old generation.

As you can see by checking these steps, one of the Survivor spaces must remain empty. 
If data exists in both Survivor spaces, or the usage is 0 for both spaces, 
then take that as a sign that something is wrong with your system.

The process of data piling up into the old generation through minor GCs can be shown as in the below chart:
![before-and-after-java-gc](https://github.com/RogerGold/media/blob/master/before-and-after-java-gc.png)

Note that in HotSpot VM, two techniques are used for faster memory allocations. One is called "bump-the-pointer," and the other is called "TLABs (Thread-Local Allocation Buffers)." 

Bump-the-pointer technique tracks the last object allocated to the Eden space.
That object will be located on top of the Eden space. And if there is an object created afterwards, 
it checks only if the size of the object is suitable for the Eden space. If the said object seems right, 
t will be placed in the Eden space, and the new object goes on top. So, when new objects are created, 
only the lastly added object needs to be checked, which allows much faster memory allocations. However, 
it is a different story if we consider a multithreaded environment. 
To save objects used by multiple threads in the Eden space for Thread-Safe, 
an inevitable lock will occur and the performance will drop due to the lock-contention.

TLABs is the solution to this problem in HotSpot VM.
This allows each thread to have a small portion of its Eden space that corresponds to its own share. 
As each thread can only access to their own TLAB, even the bump-the-pointer technique will allow memory allocations without a lock. 

This has been a quick overview of the GC in the young generation. 
You do not necessarily have to remember the two techniques that I have just mentioned. 
You will not go to jail for not knowing them. 
But please remember that after the objects are first created in the Eden space, 
and the long-surviving objects are moved to the old generation through the Survivor space.

### GC for the Old Generation
