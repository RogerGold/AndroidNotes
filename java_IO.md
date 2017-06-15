# Java I/O Tutorial
Java I/O (Input and Output) is used to process the input and produce the output.

Java uses the concept of stream to make I/O operation fast. The java.io package contains all the classes required for input and output operations.

### Stream

A stream is a sequence of data.In Java a stream is composed of bytes. It's called a stream because it is like a stream of water that continues to flow.

In java, 3 streams are created for us automatically. All these streams are attached with console.

1) System.out: standard output stream

2) System.in: standard input stream

3) System.err: standard error stream

Example:

    System.out.println("simple message");  
    System.err.println("error message");  

    int i=System.in.read();//returns ASCII code of 1st character  
    System.out.println((char)i);//will print the character  
    
### OutputStream vs InputStream

#### OutputStream

Java application uses an output stream to write data to a destination, it may be a file, an array, peripheral device or socket.

#### InputStream

Java application uses an input stream to read data from a source, it may be a file, an array, peripheral device or socket.

![stream](https://www.javatpoint.com/java/javaio/images/java-io-flow.png)

### OutputStream class

OutputStream class is an abstract class. It is the super class of all classes representing an output stream of bytes. 
An output stream accepts output bytes and sends them to some sink.

### Useful methods of OutputStream

- public void write(int)throws IOException	is used to write a byte to the current output stream.
- public void write(byte[])throws IOException	is used to write an array of byte to the current output stream.
- public void flush()throws IOException	flushes the current output stream.
- public void close()throws IOException	is used to close the current output stream.

### OutputStream Hierarchy

![OutputStream Hierarchy](https://www.javatpoint.com/java/javaio/images/java-outputstream.png)

### InputStream class

InputStream class is an abstract class. It is the super class of all classes representing an input stream of bytes.

### Useful methods of InputStream

- public abstract int read()throws IOException	reads the next byte of data from the input stream. It returns -1 at the end of file.
- public int available()throws IOException	returns an estimate of the number of bytes that can be read from the current input stream.
- public void close()throws IOException	is used to close the current input stream.

### InputStream Hierarchy

![InputStream Hierarchy](https://www.javatpoint.com/java/javaio/images/java-inputstream.png)

### Java FileOutputStream Class

Java FileOutputStream is an output stream used for writing data to a file.
### FileOutputStream class declaration

Let's see the declaration for Java.io.FileOutputStream class:

    public class FileOutputStream extends OutputStream  
    
### FileOutputStream class methods

- protected void finalize()	It is sued to clean up the connection with the file output stream.
- void write(byte[] ary)	It is used to write ary.length bytes from the byte array to the file output stream.
- void write(byte[] ary, int off, int len)	It is used to write len bytes from the byte array starting at offset off to the file output stream.
- void write(int b)	It is used to write the specified byte to the file output stream.
- FileChannel getChannel()	It is used to return the file channel object associated with the file output stream.
- FileDescriptor getFD()	It is used to return the file descriptor associated with the stream.
- void close()	It is used to closes the file output stream.

### Java FileOutputStream Example

        import java.io.FileOutputStream;  
        public class FileOutputStreamExample {  
            public static void main(String args[]){    
                   try{    
                     FileOutputStream fout=new FileOutputStream("D:\\testout.txt");
                     //write byte
                     fout.write(65); //The content of a text file testout.txt is set with the data A.   
                     //write string
                     String s="hello world.";    
                     byte b[]=s.getBytes();//converting string into byte array    
                     fout.write(b);//The content of a text file testout.txt is set with the data hello world..    

                     fout.close();    
                     System.out.println("success...");    
                    }catch(Exception e){System.out.println(e);}    
              }    
              
 ### Java FileInputStream Class
 Java FileInputStream class obtains input bytes from a file. It is used for reading byte-oriented data (streams of raw bytes) such as image data, 
 audio, video etc. You can also read character-stream data. But, for reading streams of characters, it is recommended to use FileReader class.

### Java FileInputStream class declaration

    public class FileInputStream extends InputStream  

### Java FileInputStream class methods

- int available()	It is used to return the estimated number of bytes that can be read from the input stream.
- int read()	It is used to read the byte of data from the input stream.
- int read(byte[] b)	It is used to read up to b.length bytes of data from the input stream.
- int read(byte[] b, int off, int len)	It is used to read up to len bytes of data from the input stream.
- long skip(long x)	It is used to skip over and discards x bytes of data from the input stream.
- FileChannel getChannel()	It is used to return the unique FileChannel object associated with the file input stream.
- FileDescriptor getFD()	It is used to return the FileDescriptor object.
- protected void finalize()	It is used to ensure that the close method is call when there is no more reference to the file input stream.
- void close()	It is used to closes the stream.

 
### Java FileInputStream example

    import java.io.FileInputStream;  
    public class DataStreamExample {  
         public static void main(String args[]){    
              try{    
                FileInputStream fin=new FileInputStream("D:\\testout.txt");  
                //read single character
                int j=fin.read();  
                System.out.print((char)j);    
               //read all characters
               int i=0;    
                while((i=fin.read())!=-1){    
                 System.out.print((char)i);    
                }    
                fin.close();    
              }catch(Exception e){System.out.println(e);}    
             }    
            }  

### Java BufferedOutputStream Class
ava BufferedOutputStream class is used for buffering an output stream. It internally uses buffer to store data. It adds more efficiency than to write data directly into a stream. So, it makes the performance fast.

For adding the buffer in an OutputStream, use the BufferedOutputStream class. Let's see the syntax for adding the buffer in an OutputStream:

    OutputStream os= new BufferedOutputStream(new FileOutputStream("D:\\IO Package\\testout.txt"));  
    
### Java BufferedOutputStream class declaration

Let's see the declaration for Java.io.BufferedOutputStream class:

    public class BufferedOutputStream extends FilterOutputStream  
    
### Java BufferedOutputStream class methods
- void write(int b)	It writes the specified byte to the buffered output stream.
- void write(byte[] b, int off, int len)	It write the bytes from the specified byte-input stream into a specified byte array, starting with the given offset
- void flush()	It flushes the buffered output stream.

### Example of BufferedOutputStream class

    public class BufferedOutputStreamExample{    
    public static void main(String args[])throws Exception{    
         FileOutputStream fout=new FileOutputStream("D:\\testout.txt");    
         BufferedOutputStream bout=new BufferedOutputStream(fout);    
         String s="hello world.";    
         byte b[]=s.getBytes();    
         bout.write(b);    
         bout.flush();    
         bout.close();    
         fout.close();    
         System.out.println("success");    
    }    
    }  
    
    
### Java BufferedInputStream Class
Java BufferedInputStream class is used to read information from stream. It internally uses buffer mechanism to make the performance fast.

The important points about BufferedInputStream are:

- When the bytes from the stream are skipped or read, the internal buffer automatically refilled from the contained input stream, many     bytes at a time.
- When a BufferedInputStream is created, an internal buffer array is created.

### Java BufferedInputStream class declaration

        public class BufferedInputStream extends FilterInputStream  
        
### Example of Java BufferedInputStream

        public class BufferedInputStreamExample{    
         public static void main(String args[]){    
          try{    
            FileInputStream fin=new FileInputStream("D:\\testout.txt");    
            BufferedInputStream bin=new BufferedInputStream(fin);    
            int i;    
            while((i=bin.read())!=-1){    
             System.out.print((char)i);    
            }    
            bin.close();    
            fin.close();    
          }catch(Exception e){System.out.println(e);}    
         }    
        } 
        
### Serialization in Java
Serialization in java is a mechanism of writing the state of an object into a byte stream.

It is mainly used in Hibernate, RMI, JPA, EJB and JMS technologies.

The reverse operation of serialization is called deserialization.

### Advantage of Java Serialization

It is mainly used to travel object's state on the network (known as marshaling).

![serialization](https://www.javatpoint.com/images/core/java-serialization.png)

### java.io.Serializable interface
Serializable is a marker interface (has no data member and method). It is used to "mark" java classes so that objects of these classes may get certain capability. The Cloneable and Remote are also marker interfaces.

It must be implemented by the class whose object you want to persist.

The String class and all the wrapper classes implements java.io.Serializable interface by default.

####  example 

    import java.io.Serializable;  
    public class Student implements Serializable{  
     int id;  
     String name;  
     public Student(int id, String name) {  
      this.id = id;  
      this.name = name;  
     }  
    }  
    
In the above example, Student class implements Serializable interface. Now its objects can be converted into stream.

###  ObjectOutputStream class

The ObjectOutputStream class is used to write primitive data types and Java objects to an OutputStream. Only objects that support the java.io.Serializable interface can be written to streams.

### Important Methods


- public final void writeObject(Object obj) throws IOException {}	writes the specified object to the ObjectOutputStream.
- public void flush() throws IOException {}	flushes the current output stream.
- public void close() throws IOException {}	closes the current output stream.

### Example of Java Serialization

    class Persist{  
     public static void main(String args[])throws Exception{  
      Student s1 =new Student(211,"ravi");  

      FileOutputStream fout=new FileOutputStream("f.txt");  
      ObjectOutputStream out=new ObjectOutputStream(fout);  

      out.writeObject(s1);  
      out.flush();  
      System.out.println("success");  
     }  
    }  
    
### Deserialization in java

Deserialization is the process of reconstructing the object from the serialized state.It is the reverse operation of serialization.

### ObjectInputStream class

An ObjectInputStream deserializes objects and primitive data written using an ObjectOutputStream.

### Important Methods

- public final Object readObject() throws IOException, ClassNotFoundException{}	reads an object from the input stream.
- public void close() throws IOException {}	closes ObjectInputStream.

### Example of Java Deserialization

    import java.io.*;  
    class Depersist{  
     public static void main(String args[])throws Exception{  

      ObjectInputStream in=new ObjectInputStream(new FileInputStream("f.txt"));  
      Student s=(Student)in.readObject();  
      System.out.println(s.id+" "+s.name);  //output: 211 ravi

      in.close();  
     }  
    }  
    
### Java Serialization with Inheritance (IS-A Relationship)

If a class implements serializable then all its sub classes will also be serializable.

### Java Serialization with Aggregation (HAS-A Relationship)

If a class has a reference of another class, all the references must be Serializable otherwise serialization process will not be performed. In such case, NotSerializableException is thrown at runtime.

### Java Serialization with static data member

If there is any static data member in a class, it will not be serialized because static is the part of class not object.
#### example:
    class Employee implements Serializable{  
     int id;  
     String name;  
     static String company="SSS IT Pvt Ltd";//it won't be serialized  
     public Student(int id, String name) {  
      this.id = id;  
      this.name = name;  
     }  
    }  
    
### Java Serialization with array or collection

Rule: In case of array or collection, all the objects of array or collection must be serializable. If any object is not serialiizable, serialization will be failed.

### Java Transient Keyword

Java transient keyword is used in serialization. If you define any data member as transient, it will not be serialized.
If you deserialize the object, you will get the default value for transient variable.

#### Example of Java Transient Keyword

    public class Student implements Serializable{  
     int id;  
     String name;  
     transient int age;//Now it will not be serialized  
     public Student(int id, String name,int age) {  
      this.id = id;  
      this.name = name;  
      this.age=age;  
     }  
     
