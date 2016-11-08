# SoftReferenceCache

### SoftReference

What is a SoftReference?
A SoftReference is a java class that represents a reference that me be deleted by the Garbage Collector (GC).

What does this mean? Every object in Java has references to other objects. As long as a object has references to it, 
the GC will not delete the item. When the application runs out of memory and the GC is unable to free more memory by deleting 
items that has no real reference to it any more, the items with only SoftRefences to it will be deleted. So, basically the items 
that only have a SoftReference to it will be kept in memory as long as possible until there is no other way to free memory.

Because objects with SoftRefences to it will be kept in memory as long as possible they are perfect for implementing caches.

### SoftReferenceCache

import java.lang.ref.SoftReference;
import java.util.HashMap;

    /**
     * SoftRefenceCache
     * @param <K> The type of the key's.
     * @param <V> The type of the value's.
     */
    public class SoftReferenceCache<K, V> {
      private final HashMap<K, SoftReference<V>> mCache;

      public SoftReferenceCache() {
        mCache = new HashMap<K, SoftReference<V>>();
      }

      /**
       * Put a new item in the cache. This item can be gone after a GC run.
       * 
       * @param key
       *            The key of the value.
       * @param value
       *            The value to store.
       */
      public void put(K key, V value) {
        mCache.put(key, new SoftReference<V>(value));
      }

      /**
       * Retrieve a value from the cache (if available).
       * 
       * @param key
       *            The key to look for.
       * @return The value if it's found. Return null if the key-value pair is not
       *         stored yet or the GC has removed the value from memory.
       */
      public V get(K key) {
        V value = null;

        SoftReference<V> reference = mCache.get(key);

        if (reference != null) {
          value = reference.get();
        }

        return value;
      }
    }

Here is a example of how to use this class：

    SoftReferenceCache<Integer, Person> mPersonCache = new SoftReferenceCache<Integer, Person>();

    mPersonCache.put(0, new Person("Peter");
    mPersonCache.put(1, new Person("Jan");
    mPersonCahce.put(2, new Person("Kees");

    Person p = (Person)mPersonCache.get(1); // This will retrieve the Person object of Jan.

