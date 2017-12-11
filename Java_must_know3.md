# Java Multi Threading

### What is synchronization in respect to multi-threading in Java?
With respect to multi-threading, synchronization is the capability to control the access of multiple threads to shared resources. Without synchronization, it is possible for one Java thread to modify a shared variable while another thread is in the process of using or updating same shared variable. This usually leads to erroneous behavior or program.

### Explain different way of using thread?
A Java thread could be implemented by using Runnable interface or by extending the Thread class. The Runnable is more advantageous when you are going for multiple inheritance.

### What is the difference between Thread.start() & Thread.run() method?
Thread.start() method (native method) of Thread class actually does the job of running the Thread.run() method in a thread. If we directly call Thread.run() method it will execute in same thread, so does not solve the purpose of creating a new thread.

### Why do we need run() & start() method both. Can we achieve it with only run method?
We need run() & start() method both because JVM needs to create a separate thread which can not be differentiated from a normal method call. So this job is done by start method native implementation which has to be explicitly called. Another advantage of having these two methods is we can have any object run as a thread if it implements Runnable interface. This is to avoid Java’s multiple inheritance problems which will make it difficult to inherit another class with Thread.

### What is ThreadLocal class? How can it be used?
Below are some key points about ThreadLocal variables
- A thread-local variable effectively provides a separate copy of its value for each thread that uses it.
- ThreadLocal instances are typically private static fields in classes that wish to associate state with a thread
- In the case when multiple threads access a ThreadLocal instance, a separate copy of the Threadlocal variable is maintained for each thread.
- Common use is seen in DAO pattern where the DAO class can be a singleton but the Database connection can be maintained separately for each thread. (Per Thread Singleton)

### When InvalidMonitorStateException is thrown? Why?
This exception is thrown when you try to call wait()/notify()/notifyAll() any of these methods for an Object from a point in your program where u are NOT having a lock on that object.(i.e. u r not executing any synchronized block/method of that object and still trying to call wait()/notify()/notifyAll()) wait(), notify() and notifyAll() all throw IllegalMonitorStateException. since This exception is a subclass of RuntimeException so we r not bound to catch it (although u may if u want to). and being a RuntimeException this exception is not mentioned in the signature of wait(), notify(), notifyAll() methods.

### What is the difference between sleep(), suspend() and wait() ?
Thread.sleep() takes the current thread to a "Not Runnable" state for specified amount of time. The thread holds the monitors it has acquired. For example, if a thread is running a synchronized block or method and sleep method is called then no other thread will be able to enter this block or method. The sleeping thread can wake up when some other thread calls t.interrupt on it. Note that sleep is a static method, that means it always affects the current thread (the one executing sleep method).

A common mistake is trying to call t2.sleep() where t2 is a different thread; even then, it is the current thread that will sleep, not the t2 thread. thread.suspend() is deprecated method. Its possible to send other threads into suspended state by making a suspend method call. In suspended state, a thread keeps all its monitors and can not be interrupted. This may cause deadlocks, therefore, it has been deprecated. object.wait() call also takes the current thread into a "Not Runnable" state, just like sleep(), but with a slight change. Wait method is invoked on a lock object, not thread.

Here is the sequence of operations you can think

- A thread T1 is already running a synchronized block with a lock on object - let's say "lockObject"
- Another thread T2 comes to execute the synchronized block and find that it's already acquired.
- Now T2 calls lockObject.wait() method for waiting on the lock to be release by the T1 thread.
- T1 thread finishes all its synchronized block work.
- The T1 thread calls lockObject.notifyAll() to notify all waiting threads that it done using the lock.
- Since the T2 thread is first in the queue of waiting it acquires the lock and starts processing.

### What happens when I make a static method as synchronized?
Synchronized static methods have a lock on the class "Class", so when a thread enters a synchronized static method, the class itself gets locked by the thread monitor and no other thread can enter any static synchronized methods on that class. This is unlike instance methods, as multiple threads can access "same synchronized instance methods" at the same time for different instances.

### Can a thread call a non-synchronized instance method of an Object when a synchronized method is being executed?
Yes, a Non-synchronized method can always be called without any problem. In fact, Java does not do any check for a non-synchronized method. The Lock object check is performed only for synchronized methods/blocks. In case the method is not declared synchronized Jave will call even if you are playing with shared data. So you have to be careful while doing such thing. The decision of declaring a method as synchronized has to be based on critical section access. If your method does not access a critical section (shared resource or data structure) it need not be declared synchronized. 

### Can two threads call two different synchronized instance methods of an Object?
No. If an object has synchronized instance methods then the Object itself is used a lock object for controlling the synchronization. Therefore all other instance methods need to wait until previous method call is completed. 

### What is a deadlock?
Deadlock is a situation where two or more threads are blocked forever, waiting for each other. This may occur when two threads, each having a lock on one resource, attempt to acquire a lock on the other's resource. Each thread would wait indefinitely for the other to release the lock unless one of the user processes is terminated. In terms of Java API, thread deadlock can occur in following conditions:
- When two threads call Thread.join() on each other.
- When two threads use nested synchronized blocks to lock two objects and the blocks lock the same objects in different order.

### What is Starvation? and What is a Livelock?
Starvation and livelock are much less common a problem than deadlock but are still problems that every designer of concurrent software is likely to encounter.
#### LiveLock
Livelock occurs when all threads are blocked or are otherwise unable to proceed due to unavailability of required resources, and the non-existence of any unblocked thread to make those resources available. In terms of Java API, thread livelock can occur in following conditions:
- When all the threads in a program execute Object.wait(0) on an object with zero parameters. The program is live-locked and cannot proceed until one or more threads call Object.notify() or Object.notifyAll() on the relevant objects. Because all the threads are blocked, neither call can be made.
- When all the threads in a program are stuck in infinite loops.

### Starvation
Starvation describes a situation where a thread is unable to gain regular access to shared resources and is unable to make progress. This happens when shared resources are made unavailable for long periods of "greedy" threads. For example, suppose an object provides a synchronized method that often takes a long time to return. If one thread invokes this method frequently, other threads that also need frequently synchronized access to the same object will often be blocked. Starvation occurs when one thread cannot access the CPU because one or more other threads are monopolizing the CPU. In Java, thread starvation can be caused by setting thread priorities inappropriately. A lower-priority thread can be starved by higher-priority threads if the higher-priority threads do not yield control of the CPU from time to time.

### How to find a deadlock has occurred in Java? How to detect a Deadlock in Java?
Earlier versions of Java had no mechanism to handle/detect deadlock. Since JDK 1.5 there are some powerful methods added in the java.lang.management package to diagnose and detect deadlocks. The java.lang.management.ThreadMXBean interface is management interface for the thread system of the Java virtual machine. It has two methods which can leverage to detect deadlock in a Java application.
- findMonitorDeadlockedThreads() - This method can be used to detect cycles of threads that are in deadlock waiting to acquire object monitors. It returns an array of thread IDs that are deadlocked waiting on the monitor.
- findDeadlockedThreads() - It returns an array of thread IDs that are deadlocked waiting on the monitor or ownable synchronizers.

### What is an immutable object? How does it help in writing a concurrent application?
An object is considered immutable if its state cannot change after it is constructed. Maximum reliance on immutable objects is widely accepted as a sound strategy for creating simple, reliable code. Immutable objects are particularly useful in concurrent applications. Since they cannot change state, they cannot be corrupted by thread interference or observed in an inconsistent state. Examples of immutable objects from the JDK include String and Integer. Immutable objects greatly simplify your multi-threaded program, since they are
- Simple to construct, test, and use.
- Automatically thread-safe and have no synchronization issues.
To create an object immutable You need to make the class final and all its member final so that once objects get created no one can modify its state. You can achieve the same functionality by making member as non-final but private and not modifying them except in constructor.

### How will you take thread dump in Java? How will you analyze Thread dump?
A Thread Dump is a complete list of active threads. A java thread dump is a way of finding out what each thread in the JVM is doing at a particular point in time. This is especially useful when your java application seems to have some performance issues. A thread dump will help you to find out which thread is causing this. There are several ways to take thread dumps from a JVM. It is highly recommended to take more than 1 thread dump and analyze the results based on it. Follow below steps to take thread dump of a java process

Step 1 

On UNIX, Linux and Mac OSX Environment run below command: 

ps -el | grep java 

On Windows: 

Press Ctrl+Shift+Esc to open the task manager and find the PID of the java process 

Step 2: 

Use jstack command to print the Java stack traces for a given Java process PID 

jstack [PID] 

### What is a thread leak? What does it mean in Java?
Thread leak is when an application does not release references to a thread object properly. Due to this, some Threads do not get garbage collected and the number of unused threads grows with time. Thread leak can often cause serious issues on a Java application since over a period of time too many threads will be created but not released and may cause applications to respond slow or hang.

### How can I trace whether the application has a thread leak?
If an application has thread leak then with time it will have too many unused threads. Try to find out what type of threads is leaking out. This can be done using following ways
- Give unique and descriptive names to the threads created in the application. - Add log entry in all thread at various entry and exit points in threads.
- Change debugging config levels (debug, info, error etc) and analyze log messages.
- When you find the class that is leaking out threads check how new threads are instantiated and how they're closed.
- Make sure the thread is Guaranteed to close properly by doing the following - Handling all Exceptions properly.
- Make sure the thread is Guaranteed to close properly by doing following
  - Handling all Exceptions properly.
  - releasing all resources (e.g. connections, files etc) before it closes.
  
### What is thread pool? Why should we use thread pools?
A thread pool is a collection of threads on which task can be scheduled. Instead of creating a new thread for each task, you can have one of the threads from the thread pool pulled out of the pool and assigned to the task. When the thread is finished with the task, it adds itself back to the pool and waits for another assignment. One common type of thread pool is the fixed thread pool. This type of pool always has a specified number of threads running; if a thread is somehow terminated while it is still in use, it is automatically replaced with a new thread. Below are key reasons to use a Thread Pool
- Using thread pools minimizes the JVM overhead due to thread creation. Thread objects use a significant amount of memory, and in a large-scale application, allocating and de-allocating many thread objects creates a significant memory management overhead.
- You have control over the maximum number of tasks that are being processed in parallel (= number of threads in the pool).
Most of the executor implementations in java.util.concurrent use thread pools, which consist of worker threads. This kind of thread exists separately from the Runnable and Callable tasks it executes and is often used to execute multiple tasks.

### Can we synchronize the run method? If yes then what will be the behavior?
Yes, the run method of a runnable class can be synchronized. If you make the run method synchronized then the lock on the runnable object will be occupied before executing the run method. In case we start multiple threads using the same runnable object in the constructor of the Thread then it would work. But until the 1st thread ends the 2nd thread cannot start and until the 2nd thread ends the next cannot start as all the threads depend on the lock on the same object.

### Can we synchronize the constructor of a Java Class?
As per Java Language Specification, constructors cannot be synchronized because other threads cannot see the object being created before the thread creating it has finished it. There is no practical need of a Java Objects constructor to be synchronized since it would lock the object being constructed, which is normally not available to other threads until all constructors of the object finish.
