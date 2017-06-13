# Exception Handling in Java

The exception handling in java is one of the powerful mechanism to handle the runtime errors 
so that normal flow of the application can be maintained.

### What is exception

Dictionary Meaning: Exception is an abnormal condition.

In java, exception is an event that disrupts the normal flow of the program. It is an object which is thrown at runtime.

### What is exception handling

Exception Handling is a mechanism to handle runtime errors such as ClassNotFound, IO, SQL, Remote etc.

### Advantage of Exception Handling

The core advantage of exception handling is to maintain the normal flow of the application. 
Exception normally disrupts the normal flow of the application that is why we use exception handling.
Let's take a scenario:

    statement 1;  
    statement 2;  
    statement 3;  
    statement 4;  
    statement 5;//exception occurs  
    statement 6;  
    statement 7;  
    statement 8;  
    statement 9;  
    statement 10;  

Suppose there is 10 statements in your program and there occurs an exception at statement 5, rest of the code will not be executed i.e.
statement 6 to 10 will not run. If we perform exception handling, rest of the statement will be executed. That is why we use exception handling in java.

### Hierarchy of Java Exception classes

![Exception](https://www.javatpoint.com/images/throwable.png)

### Types of Exception

There are mainly two types of exceptions: checked and unchecked where error is considered as unchecked exception. 
The sun microsystem says there are three types of exceptions:

- Checked Exception
- Unchecked Exception
- Error

### Difference between checked and unchecked exceptions

1) Checked Exception

The classes that extend Throwable class except RuntimeException and Error are known as checked exceptions e.g.IOException, SQLException etc. Checked exceptions are checked at compile-time.

2) Unchecked Exception

The classes that extend RuntimeException are known as unchecked exceptions e.g. ArithmeticException, NullPointerException, ArrayIndexOutOfBoundsException etc. Unchecked exceptions are not checked at compile-time rather they are checked at runtime.

3) Error

Error is irrecoverable e.g. OutOfMemoryError, VirtualMachineError, AssertionError etc.

### Common scenarios where exceptions may occur

#### Scenario where ArithmeticException occurs

If we divide any number by zero, there occurs an ArithmeticException.

    int a=50/0;//ArithmeticException  
    
#### Scenario where NullPointerException occurs
If we have null value in any variable, performing any operation by the variable occurs an NullPointerException.

    String s=null;  
    System.out.println(s.length());//NullPointerException  
    
#### Scenario where NumberFormatException occurs

The wrong formatting of any value, may occur NumberFormatException. Suppose I have a string variable that have characters, converting this variable into digit will occur NumberFormatException.

    String s="abc";  
    int i=Integer.parseInt(s);//NumberFormatException      
    
#### Scenario where ArrayIndexOutOfBoundsException occurs

If you are inserting any value in the wrong index, it would result ArrayIndexOutOfBoundsException as shown below:

    int a[]=new int[5];  
    a[10]=50; //ArrayIndexOutOfBoundsException     
    
### Java Exception Handling Keywords

There are 5 keywords used in java exception handling.

- try
- catch
- finally
- throw
- throws    

### Java try-catch

Java try block is used to enclose the code that might throw an exception. It must be used within the method.

Java try block must be followed by either catch or finally block.

Syntax of java try-catch

    try{  
    //code that may throw exception  
    }catch(Exception_class_Name ref){}  

Syntax of try-finally block

    try{  
    //code that may throw exception  
    }finally{}  

### Internal working of java try-catch block

 ![try-catch](https://www.javatpoint.com/images/exceptionobject.JPG)
 
 The JVM firstly checks whether the exception is handled or not. If exception is not handled, 
 JVM provides a default exception handler that performs the following tasks:

- Prints out exception description.
- Prints the stack trace (Hierarchy of methods where the exception occurred).
- Causes the program to terminate.

But if exception is handled by the application programmer, normal flow of the application is maintained i.e. 
rest of the code is executed.

### Java finally block
Java finally block is a block that is used to execute important code such as closing connection, stream etc.

Java finally block is always executed whether exception is handled or not.

Java finally block follows try or catch block.

![finally](https://www.javatpoint.com/images/finally.JPG)

### Why use java finally

Finally block in java can be used to put "cleanup" code such as closing a file, closing connection etc.

### Java throw exception
The Java throw keyword is used to explicitly throw an exception.

We can throw either checked or uncheked exception in java by throw keyword. The throw keyword is mainly used to throw custom exception. We will see custom exceptions later.

The syntax of java throw keyword is given below.

    throw exception;  

Example of throw IOException.

    throw new IOException("sorry device error);  

### java throw keyword example

    public class TestThrow1{  
       static void validate(int age){  
         if(age<18)  
          throw new ArithmeticException("not valid");  
         else  
          System.out.println("welcome to vote");  
       }  
       public static void main(String args[]){  
          validate(13);  
          System.out.println("rest of the code...");  
      }  
    }  
 
 
 Output:

    Exception in thread main java.lang.ArithmeticException:not valid
    
### Java throws keyword

The Java throws keyword is used to declare an exception. It gives an information to the programmer that there may occur an exception so it is better for the programmer to provide the exception handling code so that normal flow can be maintained.

Exception Handling is mainly used to handle the checked exceptions. If there occurs any unchecked exception such as NullPointerException, it is programmers fault that he is not performing check up before the code being used.

### Syntax of java throws

        return_type method_name() throws exception_class_name{  
        //method code  
        }  
        
### Which exception should be declared

Ans) checked exception only, because:

unchecked Exception: under your control so correct your code.
error: beyond your control e.g. you are unable to do anything if there occurs VirtualMachineError or StackOverflowError.  

### Advantage of Java throws keyword

- Now Checked Exception can be propagated (forwarded in call stack).

- It provides information to the caller of the method about the exception.

### Difference between throw and throws in Java
    throw	                                                             throws
    1)	Java throw keyword is used to explicitly throw an exception.	Java throws keyword is used to declare an exception.
    2)	Checked exception cannot be propagated using throw only.	    Checked exception can be propagated with throws.
    3)	Throw is followed by an instance.	                            Throws is followed by class.
    4)	Throw is used within the method.	                            Throws is used with the method signature.
    5)	You cannot throw multiple exceptions.	                         You can declare multiple exceptions e.g.
                                                                         public void method()throws IOException,SQLException.
    
### Java throw example

void m(){  
throw new ArithmeticException("sorry");  
}  

### Java throws example

void m()throws ArithmeticException{  
//method code  
}  

### Java throw and throws example

void m()throws ArithmeticException{  
throw new ArithmeticException("sorry");  
}  
     
### Difference between final, finally and finalize

1)	Final is used to apply restrictions on class, method and variable. Final class can't be inherited, final method can't be overridden and final variable value can't be changed.	
Finally is used to place important code, it will be executed whether exception is handled or not.	
Finalize is used to perform clean up processing just before object is garbage collected.

2)	Final is a keyword.	Finally is a block.	Finalize is a method.

### Java final example

    class FinalExample{  
    public static void main(String[] args){  
    final int x=100;  
    x=200;//Compile Time Error  
    }}  
    
### Java finally example

    class FinallyExample{  
    public static void main(String[] args){  
    try{  
    int x=300;  
    }catch(Exception e){System.out.println(e);}  
    finally{System.out.println("finally block is executed");}  
    }} 
    
### Java finalize example

    class FinalizeExample{  
    public void finalize(){System.out.println("finalize called");}  
    public static void main(String[] args){  
    FinalizeExample f1=new FinalizeExample();  
    FinalizeExample f2=new FinalizeExample();  
    f1=null;  
    f2=null;  
    System.gc();  
    }}  
    
### Java Custom Exception
If you are creating your own Exception that is known as custom exception or user-defined exception. Java custom exceptions are used to customize the exception according to user need.

By the help of custom exception, you can have your own exception and message.

Let's see a simple example of java custom exception.

    class InvalidAgeException extends Exception{  
     InvalidAgeException(String s){  
      super(s);  
     }  
    }  
    
    class TestCustomException1{  

       static void validate(int age)throws InvalidAgeException{  
         if(age<18)  
          throw new InvalidAgeException("not valid");  
         else  
          System.out.println("welcome to vote");  
       }  

       public static void main(String args[]){  
          try{  
          validate(13);  
          }catch(Exception m){System.out.println("Exception occured: "+m);}  

          System.out.println("rest of the code...");  
      }  
    }      
    
 Output:
 
        Exception occured: InvalidAgeException:not valid
        rest of the code...
        
