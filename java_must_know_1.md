# Java must know 1

### What is immutable object in Java? Can you change values of a immutable object?
A Java object is considered immutable when its state cannot change after it is created. Use of immutable objects is widely accepted 
as a sound strategy for creating simple, reliable code. Immutable objects are particularly useful in concurrent applications. 
Since they cannot change state, they cannot be corrupted by thread interference or observed in an inconsistent state. java.lang.String 
and java.lang.Integer classes are the Examples of immutable objects from the Java Development Kit. Immutable objects simplify your 
program due to following characteristics :

- Immutable objects are simple to use test and construct.
- Immutable objects are automatically thread-safe.
- Immutable objects do not require a copy constructor.
- Immutable objects do not require an implementation of clone.
- Immutable objects allow hashCode to use lazy initialization, and to cache its return value.
- Immutable objects do not need to be copied defensively when used as a field.
- Immutable objects are good Map keys and Set elements (Since state of these objects must not change while stored in a collection).
- Immutable objects have their class invariant established once upon construction, and it never needs to be checked again.
- Immutable objects always have "failure atomicity" (a term used by Joshua Bloch) : if an immutable object throws an exception, it's never left in an undesirable or indeterminate state.

### How to create a immutable object in Java? Does all property of immutable object needs to be final?
To create a object immutable You need to make the class final and all its member final so that once objects gets crated no one
can modify its state. You can achieve same functionality by making member as non final but private and not modifying them except 
in constructor. Also its NOT necessary to have all the properties final since you can achieve same functionality by making member 
as non final but private and not modifying them except in constructor.

### What is difference between String, StringBuffer and StringBuilder? When to use them?
The main difference between the three most commonly used String classes as follows.

- StringBuffer and StringBuilder objects are mutable whereas String class objects are immutable.
- StringBuffer class implementation is synchronized while StringBuilder class is not synchronized.
- Concatenation operator "+" is internally implemented by Java using either StringBuffer or StringBuilder.

Criteria to choose among String, StringBuffer and StringBuilder

- If the Object value will not change in a scenario use String Class because a String object is immutable.
- If the Object value can change and will only be modified from a single thread, use a StringBuilder because StringBuilder is unsynchronized(means faster).
- If the Object value may change, and can be modified by multiple threads, use a StringBuffer because StringBuffer is thread safe(synchronized).

### Why String class is final or immutable?
It is very useful to have strings implemented as final or immutable objects. Below are some advantages of String Immutability in Java

- Immutable objects are thread-safe. Two threads can both work on an immutable object at the same time without any possibility of conflict.
- Security: the system can pass on sensitive bits of read-only information without worrying that it will be altered
- You can share duplicates by pointing them to a single instance.
- You can create substrings without copying. You just create a pointer into an existing base String guaranteed never to change. Immutability is the secret that makes Java substring implementation very fast.
- Immutable objects are good fit for becoming Hashtable keys. If you change the value of any object that is used as a hash table key without removing it and re-adding it you will lose the object mapping.
- Since String is immutable, inside each String is a char[] exactly the correct length. Unlike a StringBuilder there is no need for padding to allow for growth.
- If String were not final, you could create a subclass and have two strings that look alike when "seen as Strings", but that are actually different.

###Is Java Pass by Reference or Pass by Value?
The Java Spec says that everything in Java is pass-by-value. There is no such thing as "pass-by-reference" in Java. 
The difficult thing can be to understand that Java passes "objects as references" passed by value. This can certainly get 
confusing and I would recommend reading this article from an expert: http://javadude.com/articles/passbyvalue.htm Also read 
this interesting thread with example on StackOverflow : Java Pass By Ref or Value

### What is OutOfMemoryError in java? How to deal with java.lang.OutOfMemeryError error?
This Error is thrown when the Java Virtual Machine cannot allocate an object because it is out of memory, and no more memory 
could be made available by the garbage collector. Note: Its an Error (extends java.lang.Error) not Exception. Two important types
of OutOfMemoryError are often encountered

- java.lang.OutOfMemoryError: Java heap space
The quick solution is to add these flags to JVM command line when Java runtime is started:
-Xms1024m -Xmx1024m 
- java.lang.OutOfMemoryError: PermGen space
The solution is to add these flags to JVM command line when Java runtime is started:
-XX:+CMSClassUnloadingEnabled-XX:+CMSPermGenSweepingEnabled
Long Term Solution: Increasing the Start/Max Heap size or changing Garbage Collection options may not always be a long term solution for your Out Of Memory Error problem. Best approach is to understand the memory needs of your program and ensure it uses memory wisely and does not have leaks. You can use a Java memory profiler to determine what methods in your program are allocating large number of objects and then determine if there is a way to make sure they are no longer referenced, or to not allocate them in the first place.

### What is the use of the finally block? Is finally block in Java guaranteed to be called? When finally block is NOT called?
Finally is the block of code that executes always. The code in finally block will execute even if an exception is occurred. Finally block is NOT called in following conditions

- If the JVM exits while the try or catch code is being executed, then the finally block may not execute. This may happen due to System.exit() call.
- If the thread executing the try or catch code is interrupted or killed, the finally block may not execute even though the application as a whole continues.
- If a exception is thrown in finally block and not handled then remaining code in finally block may not be executed.

### Why there are two Date classes; one in java.util package and another in java.sql?
From the JavaDoc of java.sql.Date:

A thin wrapper around a millisecond value that allows JDBC to identify this as an SQL DATE value. 
A milliseconds value represents the number of milliseconds that have passed since January 1, 1970 00:00:00.000 GMT. 
To conform with the definition of SQL DATE, the millisecond values wrapped inside a java.sql.Date instance must be 'normalized' 
by setting the hours, minutes, seconds, and milliseconds to zero.

Explanation: A java.util.Date represents date and time of day, a java.sql.Date only represents a date
(the complement of java.sql.Date is java.sql.Time, which only represents a time of day, but also extends java.util.Date).

### What is Marker interface? How is it used in Java?
The marker interface is a design pattern, used with languages that provide run-time type information about objects.
It provides a way to associate metadata with a class where the language does not have explicit support for such metadata.
To use this pattern, a class implements a marker interface, and code that interact with instances of that class test for the
existence of the interface. Whereas a typical interface specifies methods that an implementing class must support, a marker
interface does not do so. The mere presence of such an interface indicates specific behavior on the part of the implementing class.
There can be some hybrid interfaces, which both act as markers and specify required methods, are possible but may prove confusing 
if improperly used. Java utilizes this pattern very well and the example interfaces are

- java.io.Serializable - Serializability of a class is enabled by the class implementing the java.io.Serializable interface. The Java Classes that do not implement Serializable interface will not be able to serialize or deserializ their state. All subtypes of a serializable class are themselves serializable. The serialization interface has no methods or fields and serves only to identify the semantics of being serializable.
- java.rmi.Remote - The Remote interface serves to identify interfaces whose methods may be invoked from a non-local virtual machine. Any object that is a remote object must directly or indirectly implement this interface. Only those methods specified in a "remote interface", an interface that extends java.rmi.Remote are available remotely.
- java.lang.Cloneable - A class implements the Cloneable interface to indicate to the Object.clone() method that it is legal for that method to make a field-for-field copy of instances of that class. Invoking Object's clone method on an instance that does not implement the Cloneable interface results in the exception CloneNotSupportedException being thrown.
- javax.servlet.SingleThreadModel - Ensures that servlets handle only one request at a time. This interface has no methods.
- java.util.EvenListener - A tagging interface that all event listener interfaces must extend.

The "instanceof" keyword in java can be used to test if an object is of a specified type. 
So this keyword in combination with Marker interface can be used to take different actions based on type of 
interface an object implements.

### Why main() in java is declared as public static void main? What if the main method is declared as private?
Public - main method is called by JVM to run the method which is outside the scope of project therefore the access specifier ha
s to be public to permit call from anywhere outside the application static - When the JVM makes are call to the main method there 
is not object existing for the class being called therefore it has to have static method to allow invocation from class. 
void - Java is platform independent language therefore if it will return some value then the value may mean different to different 
platforms so unlike C it can not assume a behavior of returning value to the operating system. If main method is declared as private 
then - Program will compile properly but at run-time it will give "Main method not public." error. 
