# Eight Ways Your Android App Can Leak Memory

### 1. Static Activities

The easiest way to leak an Activity is by defining a static variable inside the class definition of the Activity and then setting it to the running instance of that Activity. If this reference is not cleared before the Activity’s lifecycle completes, the Activity will be leaked. This is because the object representing the class of the Activity (i.e., MainActivity) is static and remains loaded in memory for the entire runtime of the app. If this class object holds a reference to your Activity instance, it therefore won’t be eligible for garbage collection.

### 2. Static Views

A similar situation would be implementing a singleton pattern where an activity might be visited often and it would be beneficial to keep the instance loaded in memory so that it can be restored quickly. However, for reasons stated before, defying the defined lifecycle of an Activity and persisting it in memory is an extremely dangerous and unnecessary practice - and should be avoided at all costs.

But what if we have a particular View that takes a great deal of effort to instantiate but will remain unchanged across different lifetimes of the same Activity? Well then let’s make just that View static after instantiating it and attaching it to the View hierarchy. Now if our Activity is destroyed, we should be able to release most of its memory.

Surely you knew that an attached View will maintain a reference to its Context, which, in this case, is our Activity. By making a static reference to the View, we’ve created a persistent reference chain to our Activity and leaked it. Don’t make attached Views static and if you must, at least detach them from the View hierarchy at some point before the Activity completes.

### 3. Inner Classes

Moving on, let’s say we define a class inside the definition of our Activity’s class, known as an Inner Class. The programmer may choose to do this for a number of reasons including increasing readability and encapsulation. What if we create an instance of this Inner Class and maintain a static reference to it? At this point you might as well just guess that a memory leak is imminent.

Unfortunately because one of the benefits of Inner Class instances is that they have access to their Outer Class’s variables, they must maintain a reference to the Outer Class’s instance which causes our Activity to be leaked.

### 4. Anonymous Classes

Similarly, Anonymous Classes will also maintain a reference to the class that they were declared inside. Therefore a leak can occur if you declare and instantiate an AsyncTask anonymously inside your Activity. If it continues to perform background work after the Activity has been destroyed, the reference to the Activity will persist and it won’t be garbage collected until after the background task completes.

### 5. Handlers

The very same principle applies to background tasks declared anonymously by a Runnable object and queued up for execution by a Handler object. The Runnable object will implicitly reference the Activity it was declared in and will then be posted as a Message on the Handler’s MessageQueue. As long as the message hasn’t been handled before the Activity is destroyed, the chain of references will keep the Activity live in memory and will cause a leak.

### 6. Threads

We can repeat this same mistake again with both the Thread and TimerTask classes.

### 7. Timer Tasks

As long as they are declared and instantiated anonymously, despite the work occurring in a separate thread, they will persist a reference chain to the Activity after it has been destroyed and will yet again cause a leak.

### 8. Sensor Manager

Finally, there are system services that can be retrieved by a Context with a call to getSystemService. These Services run in their own processes and assist applications by performing some sort of background work or interfacing to the device’s hardware capabilities. If the Context want to be notified every time an event occurs inside a Service, it needs to register itself as a listener. However, this will cause the Service to maintain a reference to the Activity, and if the programmer neglects to unregister the Activity as a listener before the Activity is destroyed it will be ineligible for garbage collection and leak will occur.