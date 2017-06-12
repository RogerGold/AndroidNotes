# Java Reflection
Java Reflection is a process of examining or modifying the run time behavior of a class at run time.

The java.lang.Class class provides many methods that can be used to get metadata, examine and change the run time behavior of a class.

The java.lang and java.lang.reflect packages provide classes for java reflection

### java.lang.Class class

The java.lang.Class class performs mainly two tasks:

- provides methods to get the metadata of a class at run time.
- provides methods to examine and change the run time behavior of a class.

### Commonly used methods of Class class:

 
    1) public String getName()	
                  returns the class name
    2) public static Class forName(String className) throws ClassNotFoundException	
                 loads the class and returns the reference of Class class.
    3) public Object newInstance()throws InstantiationException,IllegalAccessException	
                 creates new instance.
    4) public boolean isInterface()	
                checks if it is interface.
    5) public boolean isArray()	
                checks if it is array.
    6) public boolean isPrimitive()	
                 checks if it is primitive.
    7) public Class getSuperclass()	
                 returns the superclass class reference.
    8) public Field[] getDeclaredFields()throws SecurityException
                returns the total number of fields of this class.
    9) public Method[] getDeclaredMethods()throws SecurityException	
               returns the total number of methods of this class.
    10) public Constructor[] getDeclaredConstructors()throws SecurityException	
               returns the total number of constructors of this class.
    11) public Method getDeclaredMethod(String name,Class[] parameterTypes)throws NoSuchMethodException,SecurityException	
               returns the method class instance.
               
### How to get the object of Class class?

There are 3 ways to get the instance of Class class. They are as follows:

- forName() method of Class class
- getClass() method of Object class
- the .class syntax

### forName() method of Class class
Is used to load the class dynamically.returns the instance of Class class.
It should be used if you know the fully qualified name of class.This cannot be used for primitive types.

    class Simple{}  

    class Test{  
     public static void main(String args[]){  
      Class c=Class.forName("Simple");  
      System.out.println(c.getName());  
     }  
    }  

### getClass() method of Object class
It returns the instance of Class class. It should be used if you know the type. Moreover, it can be used with primitives.

    class Simple{}  

    class Test{  
      void printName(Object obj){  
      Class c=obj.getClass();    
      System.out.println(c.getName());  
      }  
      public static void main(String args[]){  
       Simple s=new Simple();  

       Test t=new Test();  
       t.printName(s);  
     }  
    }
    
### The .class syntax
If a type is available but there is no instance then it is possible to obtain a Class by appending ".class" to the name of the type.
It can be used for primitive data type also.

    class Test{  
      public static void main(String args[]){  
       Class c = boolean.class;   
       System.out.println(c.getName());  

       Class c2 = Test.class;   
       System.out.println(c2.getName());  
     }  
    }  

### Determining the class object

    class Simple{}  
    interface My{}  

    class Test{  
     public static void main(String args[]){  
      try{  
       Class c=Class.forName("Simple");  
       System.out.println(c.isInterface());  

       Class c2=Class.forName("My");  
       System.out.println(c2.isInterface());  

      }catch(Exception e){System.out.println(e);}  

     }  
    }  
    
### newInstance() method
The newInstance() method of Class class and Constructor class is used to create a new instance of the class.

The newInstance() method of Class class can invoke zero-argument constructor whereas newInstance() method of Constructor class can invoke any number of arguments.
So Constructor class is preferred over Class class.

    class Simple{  
     void message(){System.out.println("Hello Java");}  
    }  

    class Test{  
     public static void main(String args[]){  
      try{  
      Class c=Class.forName("Simple");  
      Simple s=(Simple)c.newInstance();  
      s.message();  

      }catch(Exception e){System.out.println(e);}  

     }  
    }  
    
 ### Understanding javap tool
 The javap command disassembles a class file. The javap command displays information about the fields,
 constructors and methods present in a class file.

     javap fully_class_name  
     
  Example to use javap tool:
  
      javap java.lang.Object  
      
  Output:
  
      Compiled from "Object.java"  
    public class java.lang.Object {  
      public java.lang.Object();  
      public final native java.lang.Class<?> getClass();  
      public native int hashCode();  
      public boolean equals(java.lang.Object);  
      protected native java.lang.Object clone() throws java.lang.CloneNotSupportedException;  
      public java.lang.String toString();  
      public final native void notify();  
      public final native void notifyAll();  
      public final native void wait(long) throws java.lang.InterruptedException;  
      public final void wait(long, int) throws java.lang.InterruptedException;  
      public final void wait() throws java.lang.InterruptedException;  
      protected void finalize() throws java.lang.Throwable;  
      static {};  
    }  

#### javap -c command

You can use the javap -c command to see disassembled code. The code that reflects the java bytecode.

    javap -c fully_class_name 
    
#### Options of javap tool

The important options of javap tool are as follows.

      Option         	Description
      -help	        prints the help message.
      -l	          prints line number and local variable
      -c	          disassembles the code
      -s	          prints internal type signature
      -sysinfo	    shows system info (path, size, date, MD5 hash)
      -constants  	shows static final constants
      -version	    shows version information
      
### Creating a program that works as javap tool

Following methods of java.lang.Class class can be used to display the metadata of a class.

    Method                                                                	Description
    public Field[] getDeclaredFields()throws SecurityException             	returns an array of Field objects reflecting all the fields declared by the class or interface represented by this Class object.
    public Constructor[] getDeclaredConstructors()throws SecurityException	returns an array of Constructor objects reflecting all the constructors declared by the class represented by this Class object.
    public Method[] getDeclaredMethods()throws SecurityException	          returns an array of Method objects reflecting all the methods declared by the class or interface represented by this Class object.

#### Example of creating javap tool

    import java.lang.reflect.*;  

    public class MyJavap{  
       public static void main(String[] args)throws Exception {  
        Class c=Class.forName(args[0]);  

        System.out.println("Fields........");  
        Field f[]=c.getDeclaredFields();  
        for(int i=0;i<f.length;i++)  
            System.out.println(f[i]);  

        System.out.println("Constructors........");  
        Constructor con[]=c.getDeclaredConstructors();  
        for(int i=0;i<con.length;i++)  
            System.out.println(con[i]);  

            System.out.println("Methods........");  
        Method m[]=c.getDeclaredMethods();  
        for(int i=0;i<m.length;i++)  
            System.out.println(m[i]);  
       }  
    } 
    
 ### How to call private method from another class in java
 You can call the private method from outside the class by changing the runtime behaviour of the class.

By the help of java.lang.Class class and java.lang.reflect.Method class, we can call private method from any other class.

### Required methods of Method class

1) public void setAccessible(boolean status) throws SecurityException sets the accessibility of the method.

2) public Object invoke(Object method, Object... args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException is used to invoke the method.

### Required method of Class class
1) public Method getDeclaredMethod(String name,Class[] parameterTypes)throws NoSuchMethodException,SecurityException: returns a Method object that reflects the specified declared method of the class or interface represented by this Class object.

File: A.java

     public class A {  
      private void message(){System.out.println("hello java"); }  
    }  
    
File: MethodCall.java

    import java.lang.reflect.Method;  
    public class MethodCall{  
    public static void main(String[] args)throws Exception{  

        Class c = Class.forName("A");  
        Object o= c.newInstance();  
        Method m =c.getDeclaredMethod("message", null);  
        m.setAccessible(true);  
        m.invoke(o, null);  //Output:hello java
    }  
    }  
    
### Another example to call parameterized private method from another class
File: A.java

    class A{  
    private void cube(int n){System.out.println(n*n*n);}  
    }  
    
File: M.java

    import java.lang.reflect.*;  
    class M{  
    public static void main(String args[])throws Exception{  
    Class c=A.class;  
    Object obj=c.newInstance();  
    Method m=c.getDeclaredMethod("cube",new Class[]{int.class});  
    m.setAccessible(true);  
    m.invoke(obj,4);//Output:64  
    }}  
 
