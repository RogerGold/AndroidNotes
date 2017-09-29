# 10 points about final modifier in Java


- The final is a modifier in Java, which can be applied to a variable, a method or a class.

- When a final modifier is used with a class then the class cannot be extended further. 
 This is one way to protect your class from being subclassed and often sensitive classes are made final due to security reason. 
 This is also one of the reasons why String and wrapper classes are final in Java.

- When the final keyword is used with a method that it cannot be overridden in Java,
  which means you cannot override the logic of the method in the subclass. 
  This is also done to protect the original logic of method.

Btw, It's a compromise between making the whole class final or just making one method final. Since making a class final takes out 
the power of inheritance, sometimes it's better just to make the sensitive method final instead of whole class.

- When the final keyword is used with a variable then its value cannot be changed once assigned. 
 Though this is a little bit tricky to understand especially when you make a reference variable final pointing to array or collection.
 
 
- What this exactly means is that a final reference variable cannot point to another object but internal state of the object can 
 be changed i.e. you can add or remove elements from array or collection referenced by a final variable.

 This is also one of the tricky questions from Java interview and sometimes asked as to whether you can make an array or collection final 
 in Java. Your answer should mention the fact that even though it syntactically allowed, you can still modify the collection. 
 There is a difference in making a reference variable pointing to collection final and creating a read-only or immutable collection. 
 
- When the static keyword is used with final modifier then the variable becomes a compile time constant.
  This means the value of the static final variable is copied to wherever it gets referred. 
  This can create an issue if you don't understand the concept completely because if that variable is shared across multiple JARs
  then just updating its value in one JAR will not be sufficient, other JARs will still have old value.

  #It's very important to compile the whole project whenever you change the value of a public static final constant in Java. 
  
- A non-static final variable can also be a blank final variable if it's not initialized in the same line it has declared. 
  Java allows a blank final variable but you must initialize that in all constructor. 
  If you forget to initialize a blank final variable in all the constructors, the compiler will throw an error.
  
- Along with lambda expression, streams and a couple of other changes, Java 8 has introduced a new concept called effectively final variable,
  which allows a non-final variable to be accessed inside an inner class or lambda expression.

  Earlier, you cannot access a non-final local variable inside an inner or anonymous class but from Java 8 onwards you can provide 
  it's effectively final i.e. it's value has not changed after assignment.


- When you make a class final than all its method effectively become final because a final class cannot be extended 
  and without inheritance, you cannot override them. Some programmers argue that how about overriding final methods in the inner class,
  well that's not true because even an inner class cannot extend a final class.

- Last but not the least, final modifier can be used with both member variable and local variables declared inside a method or local 
  code block. Earlier only final local variables can be accessed inside anonymous inner classes or local classes, 
  but from Java 8 onwards even effectively final variable can be accessed as discussed above.







