# Immutable objects

## What is an immutable object?

An immutable object is one that will not change state after it is instantiated. Examples of immutable objects from the JDK include String and Integer.

## How to make an object immutable?

In general, an immutable object can be made by defining a class which does not have any of its members exposed, and does not have any setters.

- Set the values of properties using constructor only.
- Make the properties of the class final and private
- Do not provide any setters for these properties.
- If the instance fields include references to mutable objects, don't allow those objects to be changed:
- Don't provide methods that modify the mutable objects.
- Don't share references to the mutable objects. Never store references to external, mutable objects passed to the constructor; if necessary, create copies, and store references to the copies. Similarly, create copies of your internal mutable objects when necessary to avoid returning the originals in your methods.

The following class will create an immutable object:

   public final class FinalPersonClass { 
      private final String name; 
      private final int age; 

      public FinalPersonClass(final String name, final int age) {       
       this.name = name; 
       this.age = age; 
      } 
      public int getAge() { 
        return age; 
      } 
      public String getName() { 
       return name; 
      } 
    }
    
 As can be seen in the above example, the value of the ImmutableInt can only be set when the object is instantiated, and by having only a getter (getValue) the object's state cannot be changed after instantiation.
 
 ## Why do we need immutable class?
 - Thread-safe, Since the state of the immutable objects can not be changed once they are created they are automatically synchronized/thread-safe.
 - The references to the immutable objects can be easily shared or cached without having to copy or clone them as there state can not be changed ever after construction.
 - Immutable objects make the best HashMap or HashSet keys. Some mutable objects will change their hashCode() value depending on their state.
 
 
