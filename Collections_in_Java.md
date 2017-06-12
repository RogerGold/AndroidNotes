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
    
 
