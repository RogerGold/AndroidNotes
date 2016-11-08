First of all: what is a reference in Java?
  A reference is the direction of an object that is annotated, so you can access it.
 
Java has by default 4 types of references: strong, soft, weak and phantom.
What does each type of reference mean?

### Strong reference
Strong reference: strong references are the ordinary references in Java. 
Anytime we create a new object, a strong reference is by default created. For example, when we do:
  
    MyObject object = new MyObject();
    
A new object MyObject is created, and a strong reference to it is stored in object.

This object is strongly reachable — that means, it can be reached through a chain of strong references.
That will prevent the Garbage Collector of picking it up and destroy it, which is what we mostly want.
But now, let´s see an example where this can play against us.
    
        public class MainActivity extends Activity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {   
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main);
            new MyAsyncTask().execute();
        }

        private class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] params) {
                return doSomeStuff();
            }
            private Object doSomeStuff() {
                //do something to get result
                return new MyObject();
            } 
        }
    }
    
The AsyncTask will be created and executed together with the Activity onCreate() method. 
But here we have a problem: the inner class needs to be accessing the outside class during its entire lifetime.

What happens when the Activity is destroyed? The AsyncTask is holding a reference to the Activity,
and the Activity cannot be collected by the GC. This is what we called a memory leak.

The memory leak actually happens not only when the Activity is destroyed per-se, 
but as well when it is forcibly destroyed by the system due to a change in the configuration or more memory is needed, etc. 
If the AsyncTask is complex (i.e., keeps references to Views in the Activity, etc) it could even lead to crashes, 
since the view references are null.

So how can prevent this problem from ever happening again? Let´s explain the other type of references:

### WeakReference
WeakReference: a weak reference is a reference not strong enough to keep the object in memory. 
If we try to determine if the object is strongly referenced and it happened to be through WeakReferences,
the object will be garbage-collected. For terms of understanding, 
is better to kill the theory and show as a practical example how could we adapt the previous code to use a WeakReference 
and avoid a memory leak:

    public class MainActivity extends Activity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            new MyAsyncTask(this).execute();
        }
        private static class MyAsyncTask extends AsyncTask {
            private WeakReference<MainActivity> mainActivity;    

            public MyAsyncTask(MainActivity mainActivity) {   
                this.mainActivity = new WeakReference<>(mainActivity);            
            }
            @Override
            protected Object doInBackground(Object[] params) {
                return doSomeStuff();
            }
            private Object doSomeStuff() {
                //do something to get result
                return new Object();
            }
            @Override
            protected void onPostExecute(Object object) {
                super.onPostExecute(object);
                if (mainActivity.get() != null){
                    //adapt contents
                }
            }
        }
    }

Note a main difference now: the Activity within the inner class is now referenced as follows:

    private WeakReference<MainActivity> mainActivity;
    
What will happen here? When the Activity stops existing, since it is hold through the means of a WeakReference, 
it can be collected. Therefore no memory leaks will happen.

Side note: now that you hopefully understand what WeakReferences are a little bit better,
you will find useful the class WeakHashMap. It works exactly as a HashMap, 
except that the keys (key, not values) are referred to using WeakReferences.
This makes them very useful to implement entities such as caches.

### SoftReference
SoftReference: think of a SoftReference as a stronger WeakReference. Whereas a WeakReference will be collected immediately, 
a SoftReference will beg to the GC to stay in memory unless there is no other option.

“I will always reclaim the WeakReference. If the object is a SoftReference, I will decide what to do based on the ecosystem conditions”. 
This makes a SoftReference very useful for the implementation of a cache: as long as the memory is plenty,
we do not have to worry of manually removing objects.

### PhantomReference
PhantomReference: An Object that has only being referenced through a PhantomReference them can be collected whenever 
the Garbage Collector wants. No further explanations, no “call me back”.
PhantomReference can be used exactly to detect when an object has been removed from memory.
