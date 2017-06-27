# The builder pattern in practice

###  why and when you should consider using it
the pattern explained in this post deals with removing the unnecessary complexity that stems from multiple constructors,
multiple optional parameters and overuse of setters.

### example

Imagine you have a class with a substantial amount of attributes like the User class below. 

    public class User {
        private final String firstName;    //required
        private final String lastName;    //required
        private final int age;    //optional
        private final String phone;    //optional
        private final String address;    //optional
        ...
    }
    
Now, imagine that some of the attributes in your class are required while others are optional.
How would you go about building an object of this class?
All attributes are declared final so you have to set them all in the constructor, 
but you also want to give the clients of this class the chance of ignoring the optional attributes.

A first and valid option would be to have a constructor that only takes the required attributes as parameters,
one that takes all the required attributes plus the first optional one, 
another one that takes two optional attributes and so on. What does that look like?

Something like this:

    public User(String firstName, String lastName) {
      this(firstName, lastName, 0);
    }

    public User(String firstName, String lastName, int age) {
      this(firstName, lastName, age, "");
    }

    public User(String firstName, String lastName, int age, String phone) {
      this(firstName, lastName, age, phone, "");
    }

    public User(String firstName, String lastName, int age, String phone, String address) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.age = age;
      this.phone = phone;
      this.address = address;
    }
    
    
The good thing about this way of building objects of the class is that it works.However, the problem with this approach should be pretty obvious. 
When you only have a couple of attributes is not such a big deal, but as that number increases the code becomes harder to read and maintain. More importantly, 
the code becomes increasingly harder for clients. 

Which constructor should I invoke as a client? The one with 2 parameters? The one with 3? What is the default value for those parameters where I don’t pass an explicit value?
What if I want to set a value for address but not for age and phone? 

In that case I would have to call the constructor that takes all the parameters and pass default values for those that I don’t care about. 
Additionally, several parameters with the same type can be confusing. Was the first String the phone number or the address?

So what other choice do we have for these cases? We can always follow the JavaBeans convention,
where we have a default no-arg constructor and have setters and getters for every attribute.

Something like:

    public class User {
      private String firstName; // required
      private String lastName; // required
      private int age; // optional
      private String phone; // optional
      private String address;  //optional

      public String getFirstName() {
        return firstName;
      }
      public void setFirstName(String firstName) {
        this.firstName = firstName;
      }
      public String getLastName() {
        return lastName;
      }
      public void setLastName(String lastName) {
        this.lastName = lastName;
      }
      public int getAge() {
        return age;
      }
      public void setAge(int age) {
        this.age = age;
      }
      public String getPhone() {
        return phone;
      }
      public void setPhone(String phone) {
        this.phone = phone;
      }
      public String getAddress() {
        return address;
      }
      public void setAddress(String address) {
        this.address = address;
      }
    }
    
This approach seems easier to read and maintain. As a client I can just create an empty object and then set only the attributes that I’m interested in. 
So what’s wrong with it? There are two main problems with this solution.

The first issue has to do with having an instance of this class in an inconsistent state.
If you want to create an User object with values for all its 5 attributes then the object will not have a complete state until all the setX methods have been invoked. 
This means that some part of the client application might see this object and assume that is already constructed while that’s actually not the case. 

The second disadvantage of this approach is that now the User class is mutable. You’re loosing all the benefits of immutable objects.
An object is considered immutable if its state cannot change after it is constructed. Means Mutable objects can have their fields changed after construction. Immutable objects cannot.

Fortunately there is a third choice for these cases, the builder pattern. The solution will look something like the following.

      public class User {
        private final String firstName; // required
        private final String lastName; // required
        private final int age; // optional
        private final String phone; // optional
        private final String address; // optional

        private User(UserBuilder builder) {
          this.firstName = builder.firstName;
          this.lastName = builder.lastName;
          this.age = builder.age;
          this.phone = builder.phone;
          this.address = builder.address;
        }

        public String getFirstName() {
          return firstName;
        }

        public String getLastName() {
          return lastName;
        }

        public int getAge() {
          return age;
        }

        public String getPhone() {
          return phone;
        }

        public String getAddress() {
          return address;
        }

        public static class UserBuilder {
          private final String firstName;
          private final String lastName;
          private int age;
          private String phone;
          private String address;

          public UserBuilder(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
          }

          public UserBuilder age(int age) {
            this.age = age;
            return this;
          }

          public UserBuilder phone(String phone) {
            this.phone = phone;
            return this;
          }

          public UserBuilder address(String address) {
            this.address = address;
            return this;
          }

          public User build() {
            return new User(this);
          }

        }
      }

A couple of important points worth noting:

- The User constructor is private, which means that this class can not be directly instantiated from the client code.

- The class is once again immutable. All attributes are final and they’re set on the constructor. Additionally, we only provide getters for them.

- The builder uses the Fluent Interface idiom to make the client code more readable.

- The builder constructor only receives the required attributes and this attributes are the only ones that are defined “final” on the builder to ensure that their values are set on the constructor.

The use of the builder pattern has all the advantages of the first two approaches I mentioned at the beginning and none of their shortcomings. 
The client code is easier to write and, more importantly, to read. 

The only critique that I’ve heard about the pattern is the fact that you have to duplicate the class’ attributes on the builder.
However, given the fact that the builder class is usually a static member class of the class it builds, they can evolve together fairly easy.


Now, how does the client code trying to create a new User object looks like? Let’s see:

    public User getUser() {
      return new
        User.UserBuilder("Jhon", "Doe")
        .age(30)
        .phone("1234567")
        .address("Fake address 1234")
        .build();
    }
    
    
You can build a User object in 1 line of code and, most importantly, is very easy to read. Moreover, you’re making sure that whenever you get an object of this class is not going to be on an incomplete state.

An important point is that, like a constructor, a builder can impose invariants on its parameters. The build method can check these invariants and throw an IllegalStateException if they are not valid.

It is critical that they be checked after copying the parameters from the builder to the object,and that they be checked on the object fields rather than the builder fields. The reason for this is that, 

since the builder is not thread-safe, if we check the parameters before actually creating the object their values can be changed by another thread between the time the parameters are checked and the time they are copied. 
This period of time is known as the “window of vulnerability”. 

In our User example this could look like the following:

    public User build() {
      User user = new user(this);
      if (user.getAge() > 120) {
        throw new IllegalStateException(“Age out of range”); // thread-safe
      }
      return user;
    }


The previous version is thread-safe because we first create the user and then we check the invariants on the immutable object. 
The following code looks functionally identical but it’s not thread-safe and you should avoid doing things like this:

    public User build() {
      if (age > 120) {
        throw new IllegalStateException(“Age out of range”); // bad, not thread-safe
      }
      // This is the window of opportunity for a second thread to modify the value of age
      return new User(this);
    }
    
To sum it up, the Builder pattern is an excellent choice for classes with more than a few parameters (is not an exact science but I usually take 4 attributes to be a good indicator for using the pattern), 
especially if most of those parameters are optional. You get client code that is easier to read, write and maintain.

Additionally, your classes can remain immutable which makes your code safer.

