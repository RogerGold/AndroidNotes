# How to Share Resources Between Threads?

Multi threading can improve application performance when IO read/write is involved. 
Unfortunately, shared resources (shared variables) can have different versions at each CPU cache. 
The consequence is that the application's behavior cannot be predictable. 
Java provides synchronized keyword to keep shared resources consistent across CPU's caches. 
Unfortunately again, synchronized keyword slows down the application.

## Why Synchronized Slowdown Application?
When a thread gets locked and starts to execute instructions in a synchronized block, 
all other threads will be blocked and become idle. Execution context (CPU cache, instruction set, stack pointer ...)
of those threads will be stored and execution context of other active threads will be restored to resume computing.
It's called context switch and requires significant effort of the system. Task scheduler also has to run to pick which thread
will be loaded.

## volatile keyword
volatile keyword just does a few things: tells CPU read value of resources from main memory, 
not from CPU's cache; A write to a volatile field happens-before every subsequent read of that field.
Volatile can never have a higher overhead than synchronized, 
volatile will have the same overhead with synchronized if synchronized block has only one operation.

volatile keyword works well if there is only one writing thread. If there are 2 or more writing threads,
race condition will happen: all writing threads get the latest version of variable, modify value at its own CPU, 
then write to main memory. The consequence is that data in memory is just the output of one thread, 
other threads' modification were overridden.

## Package java.util.concurrent
This package has a lot of tools for managing threads, and also contains some thread-safe data structures.
Those data structures also use synchronized and volatile under the hood but in a sophisticated way, 
you can benefit from much better performance than writing your own code.

ConcurrentHashMap "obeys the same functional specification as Hashtable" and gives you the advantage of thread-safe.

AtomicInteger and other similar classes use volatile and Unsafe.compareAndSwapInt.
AtomicInteger can call as busy-wait, it mean a thread always checks condition to execution.
This thread does nothing but task scheduler cannot detect this check and considers this thread is busy,
so that task scheduler cannot take CPU to another thread that is ready for execution. 
Busy-wait works well if the condition can archive after a few clocks of CPU.

## lock
Lock has more flexible features than synchronized, you can use tryLock() for a specific time to wait or can make sure the
longest waiting thread gets the lock with fairness option. But synchronized keyword can guarantee both execution sequence 
and data freshness, the source code with synchronized is also simple. Lock will be a nightmare if a junior developer 
forgets to call unlock() or doesn't put unlock() at finally block.

## Immutable Object
The idea is simple, if one object never changes values, it's thread-safe. But there is a problem, 
you have to create a new object each time you want to change some values, consequently there is overheat of GC.
Some libraries can make immutable objects more easy to deal with, like https://immutables.github.io.

## Conclusion
Sharing resources between threads is easy with synchronized keyword, but it can cause world wide waiting and slowdown your applications.
Other simple techniques also can archive thread-safe, but are faster than synchronized.


