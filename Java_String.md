# Java String

In java, string is basically an object that represents sequence of char values. An array of characters works same as java string. For example:

    char[] ch={'j','a','v','a','t','p','o','i','n','t'};  
    String s=new String(ch);  
    
is same as:

    String s="javatpoint";  
    
The java.lang.String class implements Serializable, Comparable and CharSequence interfaces.

![String](https://www.javatpoint.com/images/core/string-implements.png)

### CharSequence Interface
The CharSequence interface is used to represent sequence of characters. 
It is implemented by String, StringBuffer and StringBuilder classes. It means, we can create string in java by using these 3 classes.

![CharSequence](https://www.javatpoint.com/images/core/charsequence.png)

The java String is immutable i.e. it cannot be changed. Whenever we change any string, a new instance is created. For mutable string, you can use StringBuffer and StringBuilder classes.

### What is String in java

Generally, string is a sequence of characters. But in java, string is an object that represents a sequence of characters. The java.lang.String class is used to create string object.

How to create String object?

There are two ways to create String object:
- By string literal
- By new keyword

### String Literal

Java String literal is created by using double quotes. For Example:

  String s="welcome";  
  
Each time you create a string literal, the JVM checks the string constant pool first. If the string already exists in the pool, a reference to the pooled instance is returned. 
If string doesn't exist in the pool, a new string instance is created and placed in the pool. For example:

  String s1="Welcome";  
  String s2="Welcome";//will not create new instance  
  
 ![string](https://www.javatpoint.com/images/string.JPG)
 
 In the above example only one object will be created. Firstly JVM will not find any string object with the value "Welcome" in string constant pool, so it will create a new object. 
 After that it will find the string with the value "Welcome" in the pool,  it will not create new object but will return the reference to the same instance.
 
 ### Why java uses concept of string literal?

To make Java more memory efficient (because no new objects are created if it exists already in string constant pool).

### By new keyword

  String s=new String("Welcome");//creates two objects and one reference variable  
  
In such case, JVM will create a new string object in normal(non pool) heap memory and the literal "Welcome" will be placed in the string constant pool.
The variable s will refer to the object in heap(non pool).

### Immutable String in Java

In java, string objects are immutable. Immutable simply means unmodifiable or unchangeable.

Once string object is created its data or state can't be changed but a new string object is created.

    class Testimmutablestring{  
     public static void main(String args[]){  
       String s="Sachin";  
       s.concat(" Tendulkar");//concat() method appends the string at the end  
       System.out.println(s);//will print Sachin because strings are immutable objects  
     }  
    }  
    
 Now it can be understood by the diagram given below. Here Sachin is not changed but a new object is created with sachintendulkar. 
 That is why string is known as immutable.
 
 ![string](https://www.javatpoint.com/images/string2.JPG)
 
 As you can see in the above figure that two objects are created but s reference variable still refers to "Sachin" not to "Sachin Tendulkar".

But if we explicitely assign it to the reference variable, it will refer to "Sachin Tendulkar" object.For example:

    class Testimmutablestring1{  
     public static void main(String args[]){  
       String s="Sachin";  
       s=s.concat(" Tendulkar");  
       System.out.println(s); //Output:Sachin Tendulkar 
     }  
    }  
 
 In such case, s points to the "Sachin Tendulkar". Please notice that still sachin object is not modified.
 
 ### Why string objects are immutable in java?

Because java uses the concept of string literal.Suppose there are 5 reference variables,all referes to one object "sachin".
If one reference variable changes the value of the object, it will be affected to all the reference variables. 
That is why string objects are immutable in java.

### Java String compare
There are three ways to compare string in java:

- By equals() method
- By = = operator
- By compareTo() method

### String compare by equals() method

The String equals() method compares the original content of the string. It compares values of string for equality. String class provides two methods:

- public boolean equals(Object another) compares this string to the specified object.
- public boolean equalsIgnoreCase(String another) compares this String to another string, ignoring case.

### String compare by == operator

The = = operator compares references not values.

#### Eexample:

    class Teststringcomparison3{  
     public static void main(String args[]){  
       String s1="Sachin";  
       String s2="Sachin";  
       String s3=new String("Sachin");  
       System.out.println(s1==s2);//true (because both refer to same instance)  
       System.out.println(s1==s3);//false(because s3 refers to instance created in nonpool)  
     }  
    }
    

### String compare by compareTo() method

The String compareTo() method compares values lexicographically and returns an integer value that describes if first string is less than, equal to or greater than second string.

Suppose s1 and s2 are two string variables. If:

    s1 == s2 :0
    s1 > s2   :positive value
    s1 < s2   :negative value
    
 #### Example:
 
     class Teststringcomparison4{  
     public static void main(String args[]){  
       String s1="Sachin";  
       String s2="Sachin";  
       String s3="Ratan";  
       System.out.println(s1.compareTo(s2));//0  
       System.out.println(s1.compareTo(s3));//1(because s1>s3)  
       System.out.println(s3.compareTo(s1));//-1(because s3 < s1 )  
     }  
    }  
    
### Java String intern() method

A pool of strings, initially empty, is maintained privately by the class String.

When the intern method is invoked, if the pool already contains a string equal to this String object as determined by the equals(Object) method, 
then the string from the pool is returned. Otherwise, this String object is added to the pool and a reference to this String object is returned.

    String s=new String("Sachin");  
    String s2=s.intern();  
    System.out.println(s2);//Sachin  
    
Basically doing String.intern() on a series of strings will ensure that all strings having same contents share same memory. 
So if you have list of names where 'john' appears 1000 times, by interning you ensure only one 'john' is actually allocated memory.

This can be useful to reduce memory requirements of your program. But be aware that the cache is maintained by JVM in
permanent memory pool which is usually limited in size compared to heap so you should not use intern if you don't have too many 
duplicate values.

### Difference between StringBuffer and StringBuilder

1)	StringBuffer is synchronized i.e. thread safe. It means two threads can't call the methods of StringBuffer simultaneously.	
    StringBuilder is non-synchronized i.e. not thread safe. It means two threads can call the methods of StringBuilder simultaneously.
2)	StringBuffer is less efficient than StringBuilder.	
    StringBuilder is more efficient than StringBuffer.


