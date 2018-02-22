# How to avoid “ConcurrentModificationException” while removing elements from `ArrayList` while iterating it?

I'm trying to remove some elements from an ArrayList while iterating it like this:

    for (String str : myArrayList) {
        if (someCondition) {
            myArrayList.remove(str);
        }
    }
    
Of course, I get a ConcurrentModificationException when trying to remove items from the list at the same time when iterating myArrayList.
For-each loop uses some internal iterators, which check collection modification and throw ConcurrentModificationException exception.

## Use an Iterator and call remove():

    Iterator<String> iter = myArrayList.iterator();

    while (iter.hasNext()) {
        String str = iter.next();

        if (someCondition)
            iter.remove();
    }
    
## avoid you having to deal with the iterator directly, but requires another list 

    List<String> toRemove = new ArrayList<>();
    for (String str : myArrayList) {
        if (someCondition) {
            toRemove.add(str);
        }
    }
    myArrayList.removeAll(toRemove);
    
 ref:[stackoverflow](https://stackoverflow.com/questions/18448671/how-to-avoid-concurrentmodificationexception-while-removing-elements-from-arr)
