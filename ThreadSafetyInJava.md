# Thread Safety in Java
Thread Safety in Java is a very important topic. Java provide multi-threaded environment support using Java Threads, we know that multiple threads created from same Object share object variables and this can lead to data inconsistency when the threads are used to read and update the shared data.

Let’s check this with a simple program where multiple threads are updating the shared data.

public class ThreadSafety {

		    public static void main(String[] args) throws InterruptedException {
		    
		        ProcessingThread pt = new ProcessingThread();
		        Thread t1 = new Thread(pt, "t1");
		        t1.start();
		        Thread t2 = new Thread(pt, "t2");
		        t2.start();
		        //wait for threads to finish processing
		        t1.join();
		        t2.join();
		        System.out.println("Processing count="+pt.getCount());
		    }
		
		}
		
		class ProcessingThread implements Runnable{
		    private int count;
		    
		    @Override
		    public void run() {
		        for(int i=1; i < 5; i++){
		            processSomething(i);
		        	count++;
		        }
		    }
		
		    public int getCount() {
		        return this.count;
		    }
		
		    private void processSomething(int i) {
		        // processing some job
		        try {
		            Thread.sleep(i*1000);
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
		    }
		    
		}

In above program for loop, count is incremented by 1 four times and since we have two threads, it's value should be 8 after both the threads finished executing. But when you will run above program multiple times, you will notice that count value is varying between 6,7,8. This is happening because even if count++ seems to be an atomic operation, its NOT and causing data corruption.

###Thread Safety in Java

Thread safety in java is the process to make our program safe to use in multithreaded environment, there are different ways through which we can make our program thread safe.

Synchronization is the easiest and most widely used tool for thread safety in java.
Use of Atomic Wrapper classes from java.util.concurrent.atomic package. For example AtomicInteger
Use of locks from java.util.concurrent.locks package.
Using thread safe collection classes, check this post for usage of ConcurrentHashMap for thread safety.
Using volatile keyword with variables to make every thread read the data from memory, not read from thread cache.

###Java synchronized

Synchronization is the tool using which we can achieve thread safety, JVM guarantees that synchronized code will be executed by only one thread at a time. java keyword synchronized is used to create synchronized code and internally it uses locks on Object or Class to make sure only one thread is executing the synchronized code.

- Java synchronization works on locking and unlocking of resource, before any thread enters into synchronized code, it has to acquire lock on the Object and when code execution ends, it unlocks the resource that can be locked by other threads. In the mean time other threads are in wait state to lock the synchronized resource.
- We can use synchronized keyword in two ways, one is to make a complete method synchronized and other way is to create synchronized block.
- When a method is synchronized, it locks the Object, if method is static it locks the Class, so it's always best practice to use synchronized block to lock the only sections of method that needs synchronization.
- While creating synchronized block, we need to provide the resource on which lock will be acquired, it can be XYZ.class or any Object field of the class.
- synchronized(this) will lock the Object before entering into the synchronized block.
You should use the lowest level of locking, for example if there are multiple synchronized block in a class and one of them is locking the Object, then other synchronized blocks will also be not available for execution by other threads. When we lock an Object, it acquires lock on all the fields of the Object.
- Java Synchronization provides data integrity on the cost of performance, so it should be used only when it's absolutely necessary.
- Java Synchronization works only in the same JVM, so if you need to lock some resource in multiple JVM environment, it will not work and you might have to look after some global locking mechanism.
Java Synchronization could result in deadlocks, check this post about deadlock in java and how to avoid them.
Java synchronized keyword cannot be used for constructors and variables.
- It is preferable to create a dummy private Object to use for synchronized block, so that it's reference can't be changed by any other code. For example if you have a setter method for Object on which you are synchronizing, it's reference can be changed by some other code leads to parallel execution of the synchronized block.
- We should not use any object that is maintained in a constant pool, for example String should not be used for synchronization because if any other code is also locking on same String, it will try to acquire lock on the same reference object from String pool and even though both the codes are unrelated, they will lock each other.

Here is the code changes we need to do in above program to make it thread safe.
		
		    //dummy object variable for synchronization
		    private Object mutex=new Object();
		    ...
		    //using synchronized block to read, increment and update count value synchronously
		    synchronized (mutex) {
		            count++;
		    }