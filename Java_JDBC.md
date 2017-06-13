# Java JDBC Tutorial

Java JDBC is a java API to connect and execute query with the database. JDBC API uses jdbc drivers to connect with the database.

![jabd](https://www.javatpoint.com/images/core/jdbc.png)

## JDBC Driver

JDBC Driver is a software component that enables java application to interact with the database.There are 4 types of JDBC drivers:

- JDBC-ODBC bridge driver
- Native-API driver (partially java driver)
- Network Protocol driver (fully java driver)
- Thin driver (fully java driver)

### JDBC-ODBC bridge driver

The JDBC-ODBC bridge driver uses ODBC driver to connect to the database. 
The JDBC-ODBC bridge driver converts JDBC method calls into the ODBC function calls. This is now discouraged because of thin driver.

![JDBC-ODBC bridge driver](https://www.javatpoint.com/images/type11.JPG)

#### Advantages:

- easy to use.
- can be easily connected to any database.

#### Disadvantages:

- Performance degraded because JDBC method call is converted into the ODBC function calls.
- The ODBC driver needs to be installed on the client machine.

### Native-API driver

The Native API driver uses the client-side libraries of the database. 
The driver converts JDBC method calls into native calls of the database API. It is not written entirely in java.

![Native-API driver](https://www.javatpoint.com/images/type12.JPG)

#### Advantage:

performance upgraded than JDBC-ODBC bridge driver.

#### Disadvantage:

- The Native driver needs to be installed on the each client machine.
- The Vendor client library needs to be installed on client machine.

### Network Protocol driver

The Network Protocol driver uses middleware (application server) that converts JDBC calls directly or indirectly into the vendor-specific database protocol.
It is fully written in java.

![Network Protocol driver](https://www.javatpoint.com/images/type13.JPG)

#### Advantage:

No client side library is required because of application server that can perform many tasks like auditing, load balancing, logging etc.

#### Disadvantages:

- Network support is required on client machine.
- Requires database-specific coding to be done in the middle tier.
- Maintenance of Network Protocol driver becomes costly because it requires database-specific coding to be done in the middle tier.

### Thin driver

The thin driver converts JDBC calls directly into the vendor-specific database protocol.
That is why it is known as thin driver. It is fully written in Java language.

![Thin driver](https://www.javatpoint.com/images/type14.JPG)

#### Advantage:

- Better performance than all other drivers.
- No software is required at client side or server side.

#### Disadvantage:

Drivers depends on the Database.

## Steps to connect to the database in java
There are 5 steps to connect any java application with the database in java using JDBC. They are as follows:
- Register the driver class
- Creating connection
- Creating statement
- Executing queries
- Closing connection

### Register the driver class

The forName() method of Class class is used to register the driver class. This method is used to dynamically load the driver class.

#### Syntax of forName() method

        public static void forName(String className)throws ClassNotFoundException  
        
#### Example to register the OracleDriver class

        Class.forName("oracle.jdbc.driver.OracleDriver");  
        
### Create the connection object

The getConnection() method of DriverManager class is used to establish connection with the database.

#### Syntax of getConnection() method

        1) public static Connection getConnection(String url)throws SQLException  
        2) public static Connection getConnection(String url,String name,String password)  throws SQLException  
        
#### Example to establish connection with the Oracle database

        Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","password");  
        
### Create the Statement object

The createStatement() method of Connection interface is used to create statement. 
The object of statement is responsible to execute queries with the database.

#### Syntax of createStatement() method

        public Statement createStatement()throws SQLException  
        
#### Example to create the statement object

        Statement stmt=con.createStatement(); 
        
### Execute the query

The executeQuery() method of Statement interface is used to execute queries to the database. This method returns the object of ResultSet that can be used to get all the records of a table.

#### Syntax of executeQuery() method

        public ResultSet executeQuery(String sql)throws SQLException  

#### Example to execute query

        ResultSet rs=stmt.executeQuery("select * from emp");  

        while(rs.next()){  
        System.out.println(rs.getInt(1)+" "+rs.getString(2));  
        }  
        
###  Close the connection object

By closing connection object statement and ResultSet will be closed automatically.
The close() method of Connection interface is used to close the connection.

#### Syntax of close() method

        public void close()throws SQLException  
        
#### Example to close connection

        con.close();  
        
## Example to connect to the Oracle database in java
For connecting java application with the oracle database, you need to follow 5 steps to perform database connectivity.
In this example we are using Oracle10g as the database. So we need to know following information for the oracle database:

- Driver class: The driver class for the oracle database is oracle.jdbc.driver.OracleDriver.
- Connection URL: The connection URL for the oracle10G database is jdbc:oracle:thin:@localhost:1521:xe where jdbc is the API, oracle is the database, thin is the driver, 
   localhost is the server name on which oracle is running, we may also use IP address, 1521 is the port number and XE is the Oracle service name.
   You may get all these information from the [tnsnames.ora](https://docs.oracle.com/cd/B28359_01/network.111/b28317/tnsnames.htm) file.
- Username: The default username for the oracle database is system.
- Password: Password is given by the user at the time of installing the oracle database.

### first create a table in oracle database.

        create table emp(id number(10),name varchar2(40),age number(3));  
        
### Example to Connect Java Application with Oracle database

        import java.sql.*;  
        class OracleCon{  
        public static void main(String args[]){  
        try{  
        //step1 load the driver class  
        Class.forName("oracle.jdbc.driver.OracleDriver");  

        //step2 create  the connection object  
        Connection con=DriverManager.getConnection(  
        "jdbc:oracle:thin:@localhost:1521:xe","system","oracle");  

        //step3 create the statement object  
        Statement stmt=con.createStatement();  

        //step4 execute query  
        ResultSet rs=stmt.executeQuery("select * from emp");  
        while(rs.next())  
        System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));  

        //step5 close the connection object  
        con.close();  

        }catch(Exception e){ System.out.println(e);}  

        }  
        }  
        
note:To connect java application with the Oracle database ojdbc14.jar file is required to be loaded.
search the ojdbc14.jar file then go to JRE/lib/ext folder and paste the jar file here.

## Example to connect to the mysql database in java

For connecting java application with the mysql database, you need to follow 5 steps to perform database connectivity.

In this example we are using MySql as the database. So we need to know following informations for the mysql database:

- Driver class: The driver class for the mysql database is com.mysql.jdbc.Driver.
- Connection URL: The connection URL for the mysql database is jdbc:mysql://localhost:3306/sonoo where jdbc is the API, mysql is the database, localhost is the server name on which mysql is running, we may also use IP address, 3306 is the port number and sonoo is the database name. We may use any database, in such case, you need to replace the sonoo with your database name.
- Username: The default username for the mysql database is root.
- Password: Password is given by the user at the time of installing the mysql database. In this example, we are going to use root as the password.

### first create a table in the mysql database, but before creating table, we need to create database first.

        create database sonoo;  
        use sonoo;  
        create table emp(id int(10),name varchar(40),age int(3));  
        
### Example to Connect Java Application with mysql database

        import java.sql.*;  
        class MysqlCon{  
        public static void main(String args[]){  
        try{  
        Class.forName("com.mysql.jdbc.Driver");  
        Connection con=DriverManager.getConnection(  
        "jdbc:mysql://localhost:3306/sonoo","root","root");  
        //here sonoo is database name, root is username and password  
        Statement stmt=con.createStatement();  
        ResultSet rs=stmt.executeQuery("select * from emp");  
        while(rs.next())  
        System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));  
        con.close();  
        }catch(Exception e){ System.out.println(e);}  
        }  
        } 
        
note: To connect java application with the mysql database mysqlconnector.jar file is required to be loaded.
Download the mysqlconnector.jar file. Go to jre/lib/ext folder and paste the jar file here.

## DriverManager class

The DriverManager class acts as an interface between user and drivers. It keeps track of the drivers that are available and handles establishing a connection between a database and the appropriate driver. The DriverManager class maintains a list of Driver classes that have registered themselves by calling the method DriverManager.registerDriver().

### Commonly used methods of DriverManager class:

        1) public static void registerDriver(Driver driver):	is used to register the given driver with DriverManager.
        2) public static void deregisterDriver(Driver driver):	is used to deregister the given driver (drop the driver from the list)              with DriverManager.
        3) public static Connection getConnection(String url):	is used to establish the connection with the specified url.
        4) public static Connection getConnection(String url,String userName,String password):	is used to establish the connection with            the specified url, username and password.

## Connection interface

A Connection is the session between java application and database. The Connection interface is a factory of Statement, PreparedStatement, and DatabaseMetaData i.e. object of Connection can be used to get the object of Statement and DatabaseMetaData. The Connection interface provide many methods for transaction management like commit(), rollback() etc.

### Commonly used methods of Connection interface

        1) public Statement createStatement(): creates a statement object that can be used to execute SQL queries.
        2) public Statement createStatement(int resultSetType,int resultSetConcurrency): Creates a Statement object that will generate              ResultSet objects with the given type and concurrency.
        3) public void setAutoCommit(boolean status): is used to set the commit status.By default it is true.
        4) public void commit(): saves the changes made since the previous commit/rollback permanent.
        5) public void rollback(): Drops all changes made since the previous commit/rollback.
        6) public void close(): closes the connection and Releases a JDBC resources immediately.
        
## Statement interface

The Statement interface provides methods to execute queries with the database. The statement interface is a factory of ResultSet i.e. it provides factory method to get the object of ResultSet.

### Commonly used methods of Statement interface

The important methods of Statement interface are as follows:

        1) public ResultSet executeQuery(String sql): is used to execute SELECT query. It returns the object of ResultSet.
        2) public int executeUpdate(String sql): is used to execute specified query, it may be create, drop, insert, update, delete etc.
        3) public boolean execute(String sql): is used to execute queries that may return multiple results.
        4) public int[] executeBatch(): is used to execute batch of commands.
        
### Example of Statement interface

        class FetchRecord{  
        public static void main(String args[])throws Exception{  
        Class.forName("oracle.jdbc.driver.OracleDriver");  
        Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","oracle");  
        Statement stmt=con.createStatement();  

        //stmt.executeUpdate("insert into emp765 values(33,'Irfan',50000)");  
        //int result=stmt.executeUpdate("update emp765 set name='Vimal',salary=10000 where id=33");  
        int result=stmt.executeUpdate("delete from emp765 where id=33");  
        System.out.println(result+" records affected");  
        con.close();  
        }}  

## ResultSet interface
The object of ResultSet maintains a cursor pointing to a row of a table. Initially, cursor points to before the first row.

But we can make this object to move forward and backward direction by passing either TYPE_SCROLL_INSENSITIVE or TYPE_SCROLL_SENSITIVE in createStatement(int,int) method as well as we can make this object as updatable by:

        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,  ResultSet.CONCUR_UPDATABLE); 
        
### Example of Scrollable ResultSet

        import java.sql.*;  
        class FetchRecord{  
        public static void main(String args[])throws Exception{  

        Class.forName("oracle.jdbc.driver.OracleDriver");  
        Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","oracle");  
        Statement stmt=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);  
        ResultSet rs=stmt.executeQuery("select * from emp765");  

        //getting the record of 3rd row  
        rs.absolute(3);  
        System.out.println(rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3));  

        con.close();  
        }}  
        
## PreparedStatement interface

The PreparedStatement interface is a subinterface of Statement. It is used to execute parameterized query.

### Example of parameterized query

        String sql="insert into emp values(?,?,?)";  
        
As you can see, we are passing parameter (?) for the values. Its value will be set by calling the setter methods of PreparedStatement.

### Why use PreparedStatement?

Improves performance: The performance of the application will be faster if you use PreparedStatement interface because query is compiled only once.

### How to get the instance of PreparedStatement?

The prepareStatement() method of Connection interface is used to return the object of PreparedStatement. Syntax:

        public PreparedStatement prepareStatement(String query)throws SQLException{}  

### Methods of PreparedStatement interface

The important methods of PreparedStatement interface are given below:

        public void setInt(int paramIndex, int value)	       sets the integer value to the given parameter index.
        public void setString(int paramIndex, String value)	sets the String value to the given parameter index.
        public void setFloat(int paramIndex, float value)	sets the float value to the given parameter index.
        public void setDouble(int paramIndex, double value)	sets the double value to the given parameter index.
        public int executeUpdate()	               executes the query. It is used for create, drop, insert, update, delete etc.
        public ResultSet executeQuery()	                executes the select query. It returns an instance of ResultSet.
        
### Example of PreparedStatement interface that inserts the record
        import java.sql.*;  
        class InsertPrepared{  
        public static void main(String args[]){  
        try{  
        Class.forName("oracle.jdbc.driver.OracleDriver");  

        Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","oracle");  

        PreparedStatement stmt=con.prepareStatement("insert into Emp values(?,?)");  
        stmt.setInt(1,101);//1 specifies the first parameter in the query  
        stmt.setString(2,"Ratan");  

        int i=stmt.executeUpdate();  
        System.out.println(i+" records inserted");  

        con.close();  

        }catch(Exception e){ System.out.println(e);}  

        }  
        }
        
### Example of PreparedStatement interface that updates the record

        PreparedStatement stmt=con.prepareStatement("update emp set name=? where id=?");  
        stmt.setString(1,"Sonoo");//1 specifies the first parameter in the query i.e. name  
        stmt.setInt(2,101);  

        int i=stmt.executeUpdate();  
        System.out.println(i+" records updated");  
        
### Example of PreparedStatement interface that deletes the record  

        PreparedStatement stmt=con.prepareStatement("delete from emp where id=?");  
        stmt.setInt(1,101);  

        int i=stmt.executeUpdate();  
        System.out.println(i+" records deleted");  
        
### Example of PreparedStatement interface that retrieve the records of a table

        PreparedStatement stmt=con.prepareStatement("select * from emp");  
        ResultSet rs=stmt.executeQuery();  
        while(rs.next()){  
        System.out.println(rs.getInt(1)+" "+rs.getString(2));  
        }  
        
## Java ResultSetMetaData Interface
The metadata means data about data i.e. we can get further information from the data.

If you have to get metadata of a table like total number of column, column name, column type etc. , ResultSetMetaData interface is useful because it provides methods to get metadata from the ResultSet object.

### Commonly used methods of ResultSetMetaData interface


        public int getColumnCount()throws SQLException	it returns the total number of columns in the ResultSet object.
        public String getColumnName(int index)throws SQLException	it returns the column name of the specified column index.
        public String getColumnTypeName(int index)throws SQLException	it returns the column type name for the specified index.
        public String getTableName(int index)throws SQLException	it returns the table name for the specified column index.
        
### How to get the object of ResultSetMetaData:
the getMetaData() method of ResultSet interface returns the object of ResultSetMetaData. Syntax:

        public ResultSetMetaData getMetaData()throws SQLException  
        
### Example of ResultSetMetaData interface 

        import java.sql.*;  
        class Rsmd{  
        public static void main(String args[]){  
        try{  
        Class.forName("oracle.jdbc.driver.OracleDriver");  
        Connection con=DriverManager.getConnection(  
        "jdbc:oracle:thin:@localhost:1521:xe","system","oracle");  

        PreparedStatement ps=con.prepareStatement("select * from emp");  
        ResultSet rs=ps.executeQuery();  
        ResultSetMetaData rsmd=rs.getMetaData();  

        System.out.println("Total columns: "+rsmd.getColumnCount());  
        System.out.println("Column Name of 1st column: "+rsmd.getColumnName(1));  
        System.out.println("Column Type Name of 1st column: "+rsmd.getColumnTypeName(1));  

        con.close();  
        }catch(Exception e){ System.out.println(e);}  
        }  
        }  
        
## Java DatabaseMetaData interface

DatabaseMetaData interface provides methods to get meta data of a database such as database product name, database product version, driver name, name of total number of tables, name of total number of views etc.

### Commonly used methods of DatabaseMetaData interface

        public String getDriverName()throws SQLException: it returns the name of the JDBC driver.
        public String getDriverVersion()throws SQLException: it returns the version number of the JDBC driver.
        public String getUserName()throws SQLException: it returns the username of the database.
        public String getDatabaseProductName()throws SQLException: it returns the product name of the database.
        public String getDatabaseProductVersion()throws SQLException: it returns the product version of the database.
        public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types)throws SQLException: 
        it  returns the description of the tables of the specified catalog. The table type can be TABLE, VIEW, ALIAS, SYSTEM TABLE,             SYNONYM etc.    

next →← prev
Java DatabaseMetaData interface

DatabaseMetaData interface provides methods to get meta data of a database such as database product name, database product version, driver name, name of total number of tables, name of total number of views etc.

Commonly used methods of DatabaseMetaData interface

public String getDriverName()throws SQLException: it returns the name of the JDBC driver.
public String getDriverVersion()throws SQLException: it returns the version number of the JDBC driver.
public String getUserName()throws SQLException: it returns the username of the database.
public String getDatabaseProductName()throws SQLException: it returns the product name of the database.
public String getDatabaseProductVersion()throws SQLException: it returns the product version of the database.
public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types)throws SQLException: it returns the description of the tables of the specified catalog. The table type can be TABLE, VIEW, ALIAS, SYSTEM TABLE, SYNONYM etc.

 
### How to get the object of DatabaseMetaData:

The getMetaData() method of Connection interface returns the object of DatabaseMetaData. Syntax:

        public DatabaseMetaData getMetaData()throws SQLException    
        
### Simple Example of DatabaseMetaData interface

        import java.sql.*;  
        class Dbmd{  
        public static void main(String args[]){  
        try{  
        Class.forName("oracle.jdbc.driver.OracleDriver");  

        Connection con=DriverManager.getConnection(  
        "jdbc:oracle:thin:@localhost:1521:xe","system","oracle");  
        DatabaseMetaData dbmd=con.getMetaData();  

        System.out.println("Driver Name: "+dbmd.getDriverName());  
        System.out.println("Driver Version: "+dbmd.getDriverVersion());  
        System.out.println("UserName: "+dbmd.getUserName());  
        System.out.println("Database Product Name: "+dbmd.getDatabaseProductName());  
        System.out.println("Database Product Version: "+dbmd.getDatabaseProductVersion());  

        con.close();  
        }catch(Exception e){ System.out.println(e);}  
        }  
        }  
        
### Example of DatabaseMetaData interface that prints total number of tables 

        import java.sql.*;  
        class Dbmd2{  
        public static void main(String args[]){  
        try{  
        Class.forName("oracle.jdbc.driver.OracleDriver");  

        Connection con=DriverManager.getConnection(  
        "jdbc:oracle:thin:@localhost:1521:xe","system","oracle");  

        DatabaseMetaData dbmd=con.getMetaData();  
        String table[]={"TABLE"};  
        ResultSet rs=dbmd.getTables(null,null,null,table);  

        while(rs.next()){  
        System.out.println(rs.getString(3));  
        }  

        con.close();  

        }catch(Exception e){ System.out.println(e);}  

        }  
        }  
        
## Example to store image in Oracle database
You can store images in the database in java by the help of PreparedStatement interface.

The setBinaryStream() method of PreparedStatement is used to set Binary information into the parameterIndex.


### Signature of setBinaryStream method

The syntax of setBinaryStream() method is given below:

        1) public void setBinaryStream(int paramIndex,InputStream stream)  
        throws SQLException  
        2) public void setBinaryStream(int paramIndex,InputStream stream,long length)  throws SQLException  

For storing image into the database, BLOB (Binary Large Object) datatype is used in the table. For example:

        CREATE TABLE  "IMGTABLE"   
           (    "NAME" VARCHAR2(4000),   
            "PHOTO" BLOB  
           )  
           
### Java Example to store image in the database

        import java.sql.*;  
        import java.io.*;  
        public class InsertImage {  
        public static void main(String[] args) {  
        try{  
        Class.forName("oracle.jdbc.driver.OracleDriver");  
        Connection con=DriverManager.getConnection(  
        "jdbc:oracle:thin:@localhost:1521:xe","system","oracle");  

        PreparedStatement ps=con.prepareStatement("insert into imgtable values(?,?)");  
        ps.setString(1,"sonoo");  

        FileInputStream fin=new FileInputStream("d:\\g.jpg");  
        ps.setBinaryStream(2,fin,fin.available());  
        int i=ps.executeUpdate();  
        System.out.println(i+" records affected");  

        con.close();  
        }catch (Exception e) {e.printStackTrace();}  
        }  
        }  
        
### Example to retrieve image from Oracle database
By the help of PreparedStatement we can retrieve and store the image in the database.

The getBlob() method of PreparedStatement is used to get Binary information, it returns the instance of Blob. After calling the getBytes() method on the blob object, we can get the array of binary information that can be written into the image file.

### Signature of getBlob() method of PreparedStatement

        public Blob getBlob()throws SQLException  
        
### Signature of getBytes() method of Blob interface

        public  byte[] getBytes(long pos, int length)throws SQLException  
        
We are assuming that image is stored in the imgtable.

        CREATE TABLE  "IMGTABLE"   
           (    "NAME" VARCHAR2(4000),   
            "PHOTO" BLOB  
           )       
           
Now let's write the code to retrieve the image from the database and write it into the directory so that it can be displayed.

In AWT, it can be displayed by the Toolkit class. In servlet, jsp, or html it can be displayed by the img tag.

        import java.sql.*;  
        import java.io.*;  
        public class RetrieveImage {  
        public static void main(String[] args) {  
        try{  
        Class.forName("oracle.jdbc.driver.OracleDriver");  
        Connection con=DriverManager.getConnection(  
        "jdbc:oracle:thin:@localhost:1521:xe","system","oracle");  

        PreparedStatement ps=con.prepareStatement("select * from imgtable");  
        ResultSet rs=ps.executeQuery();  
        if(rs.next()){//now on 1st row  

        Blob b=rs.getBlob(2);//2 means 2nd column data  
        byte barr[]=b.getBytes(1,(int)b.length());//1 means first image  

        FileOutputStream fout=new FileOutputStream("d:\\sonoo.jpg");  
        fout.write(barr);  

        fout.close();  
        }//end of if  
        System.out.println("ok");  

        con.close();  
        }catch (Exception e) {e.printStackTrace();  }  
        }  
        }  
        
Now if you see the d drive, sonoo.jpg image is created.

### Example to store file in Oracle database
The setCharacterStream() method of PreparedStatement is used to set character information into the parameterIndex.

### Syntax:

        1) public void setBinaryStream(int paramIndex,InputStream stream)throws SQLException
        2) public void setBinaryStream(int paramIndex,InputStream stream,long length)throws SQLException

For storing file into the database, CLOB (Character Large Object) datatype is used in the table. For example:

        CREATE TABLE  "FILETABLE"   
           (    "ID" NUMBER,   
            "NAME" CLOB  
           )  

### Java Example to store file in database

        import java.io.*;  
        import java.sql.*;  

        public class StoreFile {  
        public static void main(String[] args) {  
        try{  
        Class.forName("oracle.jdbc.driver.OracleDriver");  
        Connection con=DriverManager.getConnection(  
        "jdbc:oracle:thin:@localhost:1521:xe","system","oracle");  

        PreparedStatement ps=con.prepareStatement(  
        "insert into filetable values(?,?)");  

        File f=new File("d:\\myfile.txt");  
        FileReader fr=new FileReader(f);  

        ps.setInt(1,101);  
        ps.setCharacterStream(2,fr,(int)f.length());  
        int i=ps.executeUpdate();  
        System.out.println(i+" records affected");  

        con.close();  

        }catch (Exception e) {e.printStackTrace();}  
        }  
        }  
        
### Example to retrieve file from Oracle database:
The getClob() method of PreparedStatement is used to get file information from the database.

### Syntax of getClob method

        public Clob getClob(int columnIndex){}  
        
Let's see the table structure of this example to retrieve the file.

        CREATE TABLE  "FILETABLE"   
           (    "ID" NUMBER,   
            "NAME" CLOB  
           )  
           
The example to retrieve the file from the Oracle database is given below.

        import java.io.*;  
        import java.sql.*;  

        public class RetrieveFile {  
        public static void main(String[] args) {  
        try{  
        Class.forName("oracle.jdbc.driver.OracleDriver");  
        Connection con=DriverManager.getConnection(  
        "jdbc:oracle:thin:@localhost:1521:xe","system","oracle");  

        PreparedStatement ps=con.prepareStatement("select * from filetable");  
        ResultSet rs=ps.executeQuery();  
        rs.next();//now on 1st row  

        Clob c=rs.getClob(2);  
        Reader r=c.getCharacterStream();              

        FileWriter fw=new FileWriter("d:\\retrivefile.txt");  

        int i;  
        while((i=r.read())!=-1)  
        fw.write((char)i);  

        fw.close();  
        con.close();  

        System.out.println("success");  
        }catch (Exception e) {e.printStackTrace();  }  
        }  
        }   
        
### Transaction Management in JDBC
Transaction represents a single unit of work.

The ACID properties describes the transaction management well. ACID stands for Atomicity, Consistency, isolation and durability.

- Atomicity means either all successful or none.

- Consistency ensures bringing the database from one consistent state to another consistent state.

- Isolation ensures that transaction is isolated from other transaction.

- Durability means once a transaction has been committed, it will remain so, even in the event of errors, power loss etc.

### Advantage of Transaction Mangaement

fast performance It makes the performance fast because database is hit at the time of commit.

![Transaction](https://www.javatpoint.com/images/hibernate/tx.jpg)

In JDBC, Connection interface provides methods to manage transaction.

        void setAutoCommit(boolean status)	It is true bydefault means each transaction is committed bydefault.
        void commit()	commits the transaction.
        void rollback()	cancels the transaction.
        
### Simple example of transaction management in jdbc using Statement

        import java.sql.*;  
        class FetchRecords{  
        public static void main(String args[])throws Exception{  
        Class.forName("oracle.jdbc.driver.OracleDriver");  
        Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","oracle");  
        con.setAutoCommit(false);  

        Statement stmt=con.createStatement();  
        stmt.executeUpdate("insert into user420 values(190,'abhi',40000)");  
        stmt.executeUpdate("insert into user420 values(191,'umesh',50000)");  

        con.commit();  
        con.close();  
        }} 
        
 ### Example of transaction management in jdbc using PreparedStatement
 
         import java.sql.*;  
        import java.io.*;  
        class TM{  
        public static void main(String args[]){  
        try{  

        Class.forName("oracle.jdbc.driver.OracleDriver");  
        Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","oracle");  
        con.setAutoCommit(false);  

        PreparedStatement ps=con.prepareStatement("insert into user420 values(?,?,?)");  

        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));  
        while(true){  

        System.out.println("enter id");  
        String s1=br.readLine();  
        int id=Integer.parseInt(s1);  

        System.out.println("enter name");  
        String name=br.readLine();  

        System.out.println("enter salary");  
        String s3=br.readLine();  
        int salary=Integer.parseInt(s3);  

        ps.setInt(1,id);  
        ps.setString(2,name);  
        ps.setInt(3,salary);  
        ps.executeUpdate();  

        System.out.println("commit/rollback");  
        String answer=br.readLine();  
        if(answer.equals("commit")){  
        con.commit();  
        }  
        if(answer.equals("rollback")){  
        con.rollback();  
        }  


        System.out.println("Want to add more records y/n");  
        String ans=br.readLine();  
        if(ans.equals("n")){  
        break;  
        }  

        }  
        con.commit();  
        System.out.println("record successfully saved");  

        con.close();//before closing connection commit() is called  
        }catch(Exception e){System.out.println(e);}  

        }} 
        
It will ask to add more records until you press n. If you press n, transaction is committed.

## Batch Processing in JDBC

Instead of executing a single query, we can execute a batch (group) of queries. It makes the performance fast.

The java.sql.Statement and java.sql.PreparedStatement interfaces provide methods for batch processing.

### Methods of Statement interface

The required methods for batch processing are given below:

        void addBatch(String query)	It adds query into batch.
        int[] executeBatch()	It executes the batch of queries.
        
### Example of batch processing in jdbc

Let's see the simple example of batch processing in jdbc. It follows following steps:

- Load the driver class
- Create Connection
- Create Statement
- Add query in the batch
- Execute Batch
- Close Connection        

Example:

        class FetchRecords{  
        public static void main(String args[])throws Exception{  
        Class.forName("oracle.jdbc.driver.OracleDriver");  
        Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","oracle");  
        con.setAutoCommit(false);  

        Statement stmt=con.createStatement();  
        stmt.addBatch("insert into user420 values(190,'abhi',40000)");  
        stmt.addBatch("insert into user420 values(191,'umesh',50000)");  

        stmt.executeBatch();//executing the batch  

        con.commit();  
        con.close();  
        }}  
    
## JDBC RowSet

The instance of RowSet is the java bean component because it has properties and java bean notification mechanism. It is introduced since JDK 5.

It is the wrapper of ResultSet. It holds tabular data like ResultSet but it is easy and flexible to use.

The implementation classes of RowSet interface are as follows:

- JdbcRowSet
- CachedRowSet
- WebRowSet
- JoinRowSet
- FilteredRowSet    

Let's see how to create and execute RowSet.

        JdbcRowSet rowSet = RowSetProvider.newFactory().createJdbcRowSet();  
        rowSet.setUrl("jdbc:oracle:thin:@localhost:1521:xe");  
        rowSet.setUsername("system");  
        rowSet.setPassword("oracle");  

        rowSet.setCommand("select * from emp400");  
        rowSet.execute();  
        
#### Advantage of RowSet

The advantages of using RowSet are given below:

- It is easy and flexible to use
- It is Scrollable and Updatable bydefault  

####vFull example of Jdbc RowSet with event handling

To perform event handling with JdbcRowSet, you need to add the instance of RowSetListener in the addRowSetListener method of JdbcRowSet.

The RowSetListener interface provides 3 method that must be implemented. They are as follows:

        1) public void cursorMoved(RowSetEvent event);
        2) public void rowChanged(RowSetEvent event);
        3) public void rowSetChanged(RowSetEvent event);
        
Let's write the code to retrieve the data and perform some additional tasks while cursor is moved, cursor is changed or rowset is changed. The event handling operation can't be performed using ResultSet so it is preferred now.

        public class RowSetExample {  
                public static void main(String[] args) throws Exception {  
                         Class.forName("oracle.jdbc.driver.OracleDriver");  

            //Creating and Executing RowSet  
            JdbcRowSet rowSet = RowSetProvider.newFactory().createJdbcRowSet();  
            rowSet.setUrl("jdbc:oracle:thin:@localhost:1521:xe");  
            rowSet.setUsername("system");  
            rowSet.setPassword("oracle");  

                rowSet.setCommand("select * from emp400");  
                rowSet.execute();  

            //Adding Listener and moving RowSet  
            rowSet.addRowSetListener(new MyListener());  

                         while (rowSet.next()) {  
                                // Generating cursor Moved event  
                                System.out.println("Id: " + rowSet.getString(1));  
                                System.out.println("Name: " + rowSet.getString(2));  
                                System.out.println("Salary: " + rowSet.getString(3));  
                        }  

                }  
        }  

        class MyListener implements RowSetListener {  
              public void cursorMoved(RowSetEvent event) {  
                        System.out.println("Cursor Moved...");  
              }  
             public void rowChanged(RowSetEvent event) {  
                        System.out.println("Cursor Changed...");  
             }  
             public void rowSetChanged(RowSetEvent event) {  
                        System.out.println("RowSet changed...");  
             }  
        }  
        
The output is as follows:

        Cursor Moved...
        Id: 55
        Name: Om Bhim
        Salary: 70000
        Cursor Moved...
        Id: 190
        Name: abhi
        Salary: 40000
        Cursor Moved...
        Id: 191
        Name: umesh
        Salary: 50000
        Cursor Moved...   
        
  
