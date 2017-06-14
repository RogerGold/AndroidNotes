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
    
    
