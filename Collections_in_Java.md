# Collections in Java

Collections in java is a framework that provides an architecture to store and manipulate the group of objects.

All the operations that you perform on a data such as searching, sorting, insertion, manipulation, deletion etc. can be performed by Java Collections.

Java Collection simply means a single unit of objects. Java Collection framework provides many interfaces (Set, List, Queue, Deque etc.) 
and classes (ArrayList, Vector, LinkedList, PriorityQueue, HashSet, LinkedHashSet, TreeSet etc).

### Hierarchy of Collection Framework

![Hierarchy of Collection Framework](https://www.javatpoint.com/images/collection-hierarchy.png)

### Iterator interface

Iterator interface provides the facility of iterating the elements in forward direction only.
Methods of Iterator interface

There are only three methods in the Iterator interface. They are:

- public boolean hasNext() it returns true if iterator has more elements.
- public object next() it returns the element and moves the cursor pointer to the next element.
- public void remove() it removes the last elements returned by the iterator. It is rarely used.

## Java ArrayList class
Java ArrayList class uses a dynamic array for storing the elements. It inherits AbstractList class and implements List interface.

The important points about Java ArrayList class are:

- Java ArrayList class can contain duplicate elements.
- Java ArrayList class maintains insertion order.
- Java ArrayList class is non synchronized.
- Java ArrayList allows random access because array works at the index basis.
- In Java ArrayList class, manipulation is slow because a lot of shifting needs to be occurred if any element is removed from the array list.

### Hierarchy of ArrayList class

![Hierarchy of ArrayList class](https://www.javatpoint.com/images/arraylist.png)

## LinkedList 
Java LinkedList class uses doubly linked list to store the elements. It provides a linked-list data structure. It inherits the AbstractList class and implements List and Deque interfaces.

The important points about Java LinkedList are:

- Java LinkedList class can contain duplicate elements.
- Java LinkedList class maintains insertion order.
- Java LinkedList class is non synchronized.
- In Java LinkedList class, manipulation is fast because no shifting needs to be occurred.
- Java LinkedList class can be used as list, stack or queue.

### Hierarchy of LinkedList class
 ![Hierarchy of LinkedList class](https://www.javatpoint.com/images/linkedlist.png)
 
 ### Doubly Linked List
 In case of doubly linked list, we can add or remove elements from both side.
 ![Doubly Linked List](https://www.javatpoint.com/images/doubly-linked-list.png)
 
 ### Difference between ArrayList and LinkedList
 
     ArrayList	                                                                          LinkedList
    1) ArrayList internally uses dynamic array to store the elements.                     	LinkedList internally uses doubly linked list to store the elements.
    2) Manipulation with ArrayList is slow because it internally uses array.              	Manipulation with LinkedList is faster than ArrayList because it uses doubly linked list so no bit shifting is required in memory.
    If any element is removed from the array, all the bits are shifted in memory.
    3) ArrayList class can act as a list only because it implements List only.           	LinkedList class can act as a list and queue both because it implements List and Deque interfaces.
    4) ArrayList is better for storing and accessing data.                              	LinkedList is better for manipulating data.
   
## Java List Interface
  List Interface is the subinterface of Collection.It contains methods to insert and delete elements in index basis.It is a factory of ListIterator interface.
  
### List Interface declaration

    public interface List<E> extends Collection<E>  

### Methods of Java List Interface

    Method	                            Description
    void add(int index,Object element)	It is used to insert element into the invoking list at the index passed in the index.
    boolean addAll(int index,Collection c)	It is used to insert all elements of c into the invoking list at the index passed in the index.
    object get(int index)	It is used to return the object stored at the specified index within the invoking collection.
    object set(int index,Object element)	It is used to assign element to the location specified by index within the invoking list.
    object remove(int index)	It is used to remove the element at position index from the invoking list and return the deleted element.
    ListIterator listIterator()	It is used to return an iterator to the start of the invoking list.
    ListIterator listIterator(int index)	It is used to return an iterator to the invoking list that begins at the specified index.

## Java ListIterator Interface
ListIterator Interface is used to traverse the element in backward and forward direction.

###ListIterator Interface declaration

    public interface ListIterator<E> extends Iterator<E>  
   
### Methods of Java ListIterator Interface:

    Method            	Description
    boolean hasNext()	This method return true if the list iterator has more elements when traversing the list in the forward direction.
    Object next()	This method return the next element in the list and advances the cursor position.
    boolean hasPrevious()	This method return true if this list iterator has more elements when traversing the list in the reverse direction.
    Object previous()	This method return the previous element in the list and moves the cursor position backwards.
    
 
## Java HashSet class
Java HashSet class is used to create a collection that uses a hash table for storage. It inherits the AbstractSet class and implements Set interface.

The important points about Java HashSet class are:

- HashSet stores the elements by using a mechanism called hashing.
- HashSet contains unique elements only.

### Difference between List and Set

List can contain duplicate elements whereas Set contains unique elements only.

### Hierarchy of HashSet class
![Hierarchy of HashSet class](https://www.javatpoint.com/images/hashset.png)

### HashSet class declaration

     public class HashSet<E> extends AbstractSet<E> implements Set<E>, Cloneable, Serializable  
 
## Java LinkedHashSet class
Java LinkedHashSet class is a Hash table and Linked list implementation of the set interface. It inherits HashSet class and implements Set interface.

The important points about Java LinkedHashSet class are:

- Contains unique elements only like HashSet.
- Provides all optional set operations, and permits null elements.
- Maintains insertion order.

### Hierarchy of LinkedHashSet class
![Hierarchy of LinkedHashSet class](https://www.javatpoint.com/images/linkedhashset.png)

### LinkedHashSet class declaration

    public class LinkedHashSet<E> extends HashSet<E> implements Set<E>, Cloneable, Serializable  
  
## Java TreeSet class
Java TreeSet class implements the Set interface that uses a tree for storage. It inherits AbstractSet class and implements NavigableSet interface. The objects of TreeSet class are stored in ascending order.

The important points about Java TreeSet class are:

- Contains unique elements only like HashSet.
- Access and retrieval times are quiet fast.
- Maintains ascending order.

### Hierarchy of TreeSet class
![Hierarchy of TreeSet class](https://www.javatpoint.com/images/treeset.png)

### TreeSet class declaration

  public class TreeSet<E> extends AbstractSet<E> implements NavigableSet<E>, Cloneable, Serializable  
  
## Java Queue Interface
Java Queue interface orders the element in FIFO(First In First Out) manner. In FIFO, first element is removed first and last element is removed at last.

### Queue Interface declaration
    public interface Queue<E> extends Collection<E> 
  
## PriorityQueue class

The PriorityQueue class provides the facility of using queue. But it does not orders the elements in FIFO manner. It inherits AbstractQueue class.

### PriorityQueue class declaration
    public class PriorityQueue<E> extends AbstractQueue<E> implements Serializable  
  
## Java Deque Interface
Java Deque Interface is a linear collection that supports element insertion and removal at both ends. Deque is an acronym for "double ended queue".

### Deque Interface declaration

    public interface Deque<E> extends Queue<E>  
 
 ## ArrayDeque class
 The ArrayDeque class provides the facility of using deque and resizable-array. It inherits AbstractCollection class and implements the Deque interface.

The important points about ArrayDeque class are:

- Unlike Queue, we can add or remove elements from both sides.
- Null elements are not allowed in the ArrayDeque.
- ArrayDeque is not thread safe, in the absence of external synchronization.
- ArrayDeque has no capacity restrictions.
- ArrayDeque is faster than LinkedList and Stack.

### ArrayDeque Hierarchy
![ArrayDeque Hierarchy](https://www.javatpoint.com/java/collection/images/arraydeque.png)

### ArrayDeque class declaration

    public class ArrayDeque<E> extends AbstractCollection<E> implements Deque<E>, Cloneable, Serializable  
  
## Java Map Interface
A map contains values on the basis of key i.e. key and value pair. Each key and value pair is known as an entry. Map contains only unique keys.

Map is useful if you have to search, update or delete elements on the basis of key.

### Map.Entry Interface

Entry is the sub interface of Map. So we will be accessed it by Map.Entry name. It provides methods to get key and value.

### Methods of Map.Entry interface

    Method	Description
    Object getKey()	It is used to obtain key.
    Object getValue()	It is used to obtain value.
  
Example:

    Map<Integer,String> map=new HashMap<Integer,String>();  
    map.put(100,"Amit");  
    map.put(101,"Vijay");  
    map.put(102,"Rahul");  
    for(Map.Entry m:map.entrySet()){  
     System.out.println(m.getKey()+" "+m.getValue());  
    }  
    
 ## Java HashMap class
 Java HashMap class implements the map interface by using a hashtable. It inherits AbstractMap class and implements Map interface.

The important points about Java HashMap class are:

- A HashMap contains values based on the key.
- It contains only unique elements.
- It may have one null key and multiple null values.
- It maintains no order.

### Hierarchy of HashMap class
![Hierarchy of HashMap class](https://www.javatpoint.com/images/hashmap.png)

### HashMap class declaration

    public class HashMap<K,V> extends AbstractMap<K,V> implements Map<K,V>, Cloneable, Serializable  
  
### HashMap class Parameters

Let's see the Parameters for java.util.HashMap class.

- K: It is the type of keys maintained by this map.
- V: It is the type of mapped values.
  
Example:

    HashMap<Integer,String> hm=new HashMap<Integer,String>();  
    hm.put(100,"Amit");  
    hm.put(101,"Vijay");  
    hm.put(102,"Rahul");  
    for(Map.Entry m:hm.entrySet()){  
     System.out.println(m.getKey()+" "+m.getValue());  
    }  

### Difference between HashSet and HashMap

HashSet contains only values whereas HashMap contains entry(key and value).

## Java LinkedHashMap class
Java LinkedHashMap class is Hash table and Linked list implementation of the Map interface, with predictable iteration order. It inherits HashMap class and implements the Map interface.

The important points about Java LinkedHashMap class are:

- A LinkedHashMap contains values based on the key.
- It contains only unique elements.
- It may have one null key and multiple null values.
- It is same as HashMap instead maintains insertion order.

### Hierarchy of LinkedHashMap class
![LinkedHashMap class declaration](https://www.javatpoint.com/images/linkedhashmap.png)

### LinkedHashMap class declaration
    public class LinkedHashMap<K,V> extends HashMap<K,V> implements Map<K,V>

### LinkedHashMap class Parameters

Let's see the Parameters for java.util.LinkedHashMap class.

- K: It is the type of keys maintained by this map.
- V: It is the type of mapped values.

Example:

    LinkedHashMap<Integer,String> hm=new LinkedHashMap<Integer,String>();  
    hm.put(100,"Amit");  
    hm.put(101,"Vijay");  
    hm.put(102,"Rahul");  
    for(Map.Entry m:hm.entrySet()){  
     System.out.println(m.getKey()+" "+m.getValue());  
    }  
    
## Java TreeMap class
Java TreeMap class implements the Map interface by using a tree. It provides an efficient means of storing key/value pairs in sorted order.

The important points about Java TreeMap class are:

- A TreeMap contains values based on the key. It implements the NavigableMap interface and extends AbstractMap class.
- It contains only unique elements.
- It cannot have null key but can have multiple null values.
- It is same as HashMap instead maintains ascending order.

### Hierarchy of TreeMap class
![Hierarchy of TreeMap class](https://www.javatpoint.com/images/treemap.png)

### TreeMap class declaration

    public class TreeMap<K,V> extends AbstractMap<K,V> implements NavigableMap<K,V>, Cloneable, Serializable  
  
### TreeMap class Parameters

Let's see the Parameters for java.util.TreeMap class.

- K: It is the type of keys maintained by this map.
- V: It is the type of mapped values.

Example:

    TreeMap<Integer,String> hm=new TreeMap<Integer,String>();  
    hm.put(100,"Amit");  
    hm.put(102,"Ravi");  
    hm.put(101,"Vijay");  
    hm.put(103,"Rahul");  
    for(Map.Entry m:hm.entrySet()){  
     System.out.println(m.getKey()+" "+m.getValue());  
    }  
    
    
### What is difference between HashMap and TreeMap?
  
       HashMap	                              TreeMap
    1) HashMap can contain one null key.  	TreeMap can not contain any null key.
    2) HashMap maintains no order.	        TreeMap maintains ascending order.

## Java Hashtable class
Java Hashtable class implements a hashtable, which maps keys to values. It inherits Dictionary class and implements the Map interface.

The important points about Java Hashtable class are:

- A Hashtable is an array of list. Each list is known as a bucket. The position of bucket is identified by calling the hashcode() method. A Hashtable contains values based on the key.
- It contains only unique elements.
- It may have not have any null key or value.
- It is synchronized.

### Hashtable class declaration

    public class Hashtable<K,V> extends Dictionary<K,V> implements Map<K,V>, Cloneable, Serializable  
  
### Hashtable class Parameters

Let's see the Parameters for java.util.Hashtable class.

- K: It is the type of keys maintained by this map.
- V: It is the type of mapped values.

Example:

    Hashtable<Integer,String> hm=new Hashtable<Integer,String>();  
    hm.put(100,"Amit");  
    hm.put(102,"Ravi");  
    hm.put(101,"Vijay");  
    hm.put(103,"Rahul");   
    for(Map.Entry m:hm.entrySet()){  
     System.out.println(m.getKey()+" "+m.getValue());  
    }  
    
### Difference between HashMap and Hashtable


1) HashMap is non synchronized. It is not-thread safe and can't be shared between many threads without proper synchronization code.	Hashtable is synchronized. It is thread-safe and can be shared with many threads.
2) HashMap allows one null key and multiple null values.	Hashtable doesn't allow any null key or value.
3) HashMap is a new class introduced in JDK 1.2.	Hashtable is a legacy class.
4) HashMap is fast.	Hashtable is slow.
5) We can make the HashMap as synchronized by calling this code
 Map m = Collections.synchronizedMap(hashMap);	Hashtable is internally synchronized and can't be unsynchronized.
6) HashMap is traversed by Iterator.	Hashtable is traversed by Enumerator and Iterator.
7) Iterator in HashMap is fail-fast.	Enumerator in Hashtable is not fail-fast.
8) HashMap inherits AbstractMap class.	Hashtable inherits Dictionary class.

## Java EnumSet class
Java EnumSet class is the specialized Set implementation for use with enum types. It inherits AbstractSet class and implements the Set interface.

### EnumSet class hierarchy
![EnumSet class hierarchy](https://www.javatpoint.com/java/collection/images/enumset.png)

### EnumSet class declaration

    public abstract class EnumSet<E extends Enum<E>> extends AbstractSet<E> implements Cloneable, Serializable  
  
Example:

    enum days {  
      SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY  
    }  
    public class EnumSetExample {  
      public static void main(String[] args) {  
        Set<days> set = EnumSet.of(days.TUESDAY, days.WEDNESDAY);  
        // Traversing elements  
        Iterator<days> iter = set.iterator();  
        while (iter.hasNext())  
          System.out.println(iter.next());  
      }  
    } 


## Java EnumMap class
Java EnumMap class is the specialized Map implementation for enum keys. It inherits Enum and AbstractMap classes.

### EnumMap class hierarchy
![EnumMap class hierarchy](https://www.javatpoint.com/java/collection/images/enummap.png)

### EnumMap class declaration

    public class EnumMap<K extends Enum<K>,V> extends AbstractMap<K,V> implements Serializable, Cloneable 
   
### EnumMap class Parameters

Let's see the Parameters for java.util.EnumMap class.

- K: It is the type of keys maintained by this map.
- V: It is the type of mapped values.

Example:

       // create an enum  
       public enum Days {  
       Monday, Tuesday, Wednesday, Thursday  
       };  
       public static void main(String[] args) {  
       //create and populate enum map  
       EnumMap<Days, String> map = new EnumMap<Days, String>(Days.class);  
       map.put(Days.Monday, "1");  
       map.put(Days.Tuesday, "2");  
       map.put(Days.Wednesday, "3");  
       map.put(Days.Thursday, "4");  
       // print the map  
       for(Map.Entry m:map.entrySet()){    
           System.out.println(m.getKey()+" "+m.getValue());    
          }   
          
## Java Collections class
Java collection class is used exclusively with static methods that operate on or return collections. It inherits Object class.

The important points about Java Collections class are:

- Java Collection class supports the polymorphic algorithms that operate on collections.
- Java Collection class throws a NullPointerException if the collections or class objects provided to them are null.

### Collections class declaration
    public class Collections extends Object  
    
 Example:
 
        List<Integer> list = new ArrayList<Integer>();  
        list.add(46);  
        list.add(67);  
        list.add(24);  
        list.add(16);  
        list.add(8);  
        list.add(12);  
        System.out.println("Value of minimum element from the collection: "+Collections.min(list)); //Value of minimum element from the collection: 8    
 
## Sorting in Collection
We can sort the elements of:

- String objects
- Wrapper class objects
- User-defined class objects

Collections class provides static methods for sorting the elements of collection.If collection elements are of Set type, we can use TreeSet.But We cannot sort the elements of List.Collections class provides methods for sorting the elements of List type elements.

### Method of Collections class for sorting List elements

public void sort(List list): is used to sort the elements of List.List elements must be of Comparable type.

Note: String class and Wrapper classes implements the Comparable interface.So if you store the objects of string or wrapper classes, it will be Comparable.

Example:

    ArrayList<String> al=new ArrayList<String>();  
    al.add("Viru");  
    al.add("Saurav");  
    al.add("Mukesh");  
    al.add("Tahir");  

    Collections.sort(al);  
    Iterator itr=al.iterator();  
    while(itr.hasNext()){  
    System.out.println(itr.next());  
     }  
     
     
## Java Comparable interface
Java Comparable interface is used to order the objects of user-defined class.This interface is found in java.lang package and contains only one method named compareTo(Object). It provide single sorting sequence only i.e. you can sort the elements on based on single data member only. For example it may be rollno, name, age or anything else.

### compareTo(Object obj) method

public int compareTo(Object obj): is used to compare the current object with the specified object.

We can sort the elements of:

- String objects
- Wrapper class objects
- User-defined class objects

### Collections class

Collections class provides static methods for sorting the elements of collections. If collection elements are of Set or Map, we can use TreeSet or TreeMap. But We cannot sort the elements of List. Collections class provides methods for sorting the elements of List type elements.

### Method of Collections class for sorting List elements

public void sort(List list): is used to sort the elements of List. List elements must be of Comparable type.

Note: String class and Wrapper classes implements Comparable interface by default. So if you store the objects of string or wrapper classes in list, set or map, it will be Comparable by default.

Example:

    class Student implements Comparable<Student>{  
     int rollno;  
     String name;  
     int age;  
  

      public int compareTo(Student st){  
      if(age==st.age)  
      return 0;  
      else if(age>st.age)  
      return 1;  
      else  
      return -1;  
      }  
    }  
    
## Java Comparator interface
Java Comparator interface is used to order the objects of user-defined class.

This interface is found in java.util package and contains 2 methods compare(Object obj1,Object obj2) and equals(Object element).

It provides multiple sorting sequence i.e. you can sort the elements on the basis of any data member, for example rollno, name, age or anything else.

### compare() method

public int compare(Object obj1,Object obj2): compares the first object with second object.

### Collections class

Collections class provides static methods for sorting the elements of collection. If collection elements are of Set or Map, we can use TreeSet or TreeMap. But we cannot sort the elements of List. Collections class provides methods for sorting the elements of List type elements also.

### Method of Collections class for sorting List elements

public void sort(List list, Comparator c): is used to sort the elements of List by the given Comparator.


## Properties class in Java

The properties object contains key and value pair both as a string. The java.util.Properties class is the subclass of Hashtable.

It can be used to get property value based on the property key. The Properties class provides methods to get data from properties file and store data into properties file. Moreover, it can be used to get properties of system.

### Advantage of properties file

Recompilation is not required, if information is changed from properties file: If any information is changed from the properties file, you don't need to recompile the java class. It is used to store information which is to be changed frequently.

### Example of Properties class to get information from properties file

db.properties

    user=system  
    password=oracle 
    
Test.java

    public class Test {  
    public static void main(String[] args)throws Exception{  
        FileReader reader=new FileReader("db.properties");  

        Properties p=new Properties();  
        p.load(reader);  

        System.out.println(p.getProperty("user"));  //output: system
        System.out.println(p.getProperty("password")); //output:  oracle 
    }  
    
Now if you change the value of the properties file, you don't need to compile the java class again. That means no maintenance problem.
### Example of Properties class to create properties file

     public class Test {  
       public static void main(String[] args)throws Exception{  

       Properties p=new Properties();  
       p.setProperty("name","Sonoo Jaiswal");  
       p.setProperty("email","sonoojaiswal@javatpoint.com");  

       p.store(new FileWriter("info.properties"),"Javatpoint Properties Example");  

    }  
    }  
  
### Example of Properties class to get all the system properties
    public class Test {  
     public static void main(String[] args)throws Exception{  

     Properties p=System.getProperties();  
     Set set=p.entrySet();  

     Iterator itr=set.iterator();  
     while(itr.hasNext()){  
     Map.Entry entry=(Map.Entry)itr.next();  
     System.out.println(entry.getKey()+" = "+entry.getValue());  
     }  

     }  
     }  
     

### Difference between ArrayList and Vector

ArrayList and Vector both implements List interface and maintains insertion order.

But there are many differences between ArrayList and Vector classes that are given below.

1) ArrayList is not synchronized.	Vector is synchronized.
2) ArrayList increments 50% of current array size if number of element exceeds from its capacity.	Vector increments 100% means doubles the array size if total number of element exceeds than its capacity.
3) ArrayList is not a legacy class, it is introduced in JDK 1.2.	Vector is a legacy class.
4) ArrayList is fast because it is non-synchronized.	Vector is slow because it is synchronized i.e. in multithreading environment, it will hold the other threads in runnable or non-runnable state until current thread releases the lock of object.
5) ArrayList uses Iterator interface to traverse the elements.	Vector uses Enumeration interface to traverse the elements. But it can use Iterator also.

