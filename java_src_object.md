# Java 源码阅读之Object

## 万事万物皆是对象
在java中所有类都继承Object，所以了解Object类是很有必要的。

## Object类中的方法

###  public final native Class<?> getClass();
getClass返回这个Object的运行类的类对象，你可以使用类对象来获取该类的metadata。

### public native int hashCode();
返回Object的哈希码。

这个方法用来优化hashTables，如HashMap, HashSet。

Java中的集合（Collection）有两类，一类是List，再有一类是Set。前者集合内的元素是有序的，元素可以重复；后者元素无序，但元素不可重复。
那么这里就有一个比较严重的问题了：要想保证元素不重复，可两个元素是否重复应该依据什么来判断呢？这就是 Object.equals方法了。
但是，如果每增加一个元素就检查一次，那么当元素很多时，后添加到集合中的元素比较的次数就非常多了。也就是说，如果集合中现在已经有1000个元素，
那么第1001个元素加入集合时，它就要调用1000次equals方法。这显然会大大降低效率。于是，Java采用了哈希表的原理。
哈希算法也称为散列算法，当集合要添加新的元素时，将对象通过哈希算法计算得到哈希值（正整数），然后将哈希值和集合（数组）长度进行&运算，得到该对象在该数组存放的位置索引。如果这个位置上没有元素，它就可以直接存储在这个位置上，不用再进行任何比较了；如果这个位置上已经有元素了，就调用它的equals方法与新元素进行比较，相同的话就不存了，不相同就表示发生冲突了，散列表对于冲突有具体的解决办法，但最终还会将新元素保存在适当的位置。
这样一来，实际调用equals方法比较的次数就大大降低了

ref: [Hashing](https://www.cs.cmu.edu/~adamchik/15-121/lectures/Hashing/hashing.html)

### public boolean equals(Object obj);
比较连个对象是否相等。重写该方法时一定要重写HashCode方法，要确保像个对象相等是，他们的hashCode也相等。

### 重写HashCode和equals方法

- 尽量保证使用对象的同一个属性来生成hashCode()和equals()两个方法。例如使用员工id。
- eqauls方法必须保证一致（如果对象没有被修改，equals应该返回相同的值）
- 任何时候只要a.equals(b),那么a.hashCode()必须和b.hashCode()相等，如果两个对象相等而hashCode不等，那么就可以在HashSet中存入相等的两个对象。
- 两者必须同时重写。

当使用ORM的时候特别要注意的

如果你使用ORM处理一些对象的话，你要确保在hashCode()和equals()对象中使用getter和setter而不是直接引用成员变量。因为在ORM中有的时候成员变量会被延时加载，这些变量只有当getter方法被调用的时候才真正可用。如果使用A.id == B.id则可能会出现这个问题，但是我们使用e1.getId() == e2.getId()就不会出现这个问题。

- equals() must define an equivalence relation (it must be reflexive, symmetric, and transitive). In addition, it must be consistent (if the objects are not modified, then it must keep returning the same value). Furthermore, o.equals(null) must always return false.

- hashCode() must also be consistent (if the object is not modified in terms of equals(), it must keep returning the same value).

The relation between the two methods is:

      Whenever a.equals(b), then a.hashCode() must be same as b.hashCode().
      
ref: [重写hashCode和equals](https://stackoverflow.com/questions/27581/what-issues-should-be-considered-when-overriding-equals-and-hashcode-in-java/27609#27609)

### Why does Java's hashCode() in String use 31 as a multiplier?
In Java, the hash code for a String object is computed as

           s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]
         
The value 31 was chosen because it is an odd prime. If it were even and the multiplication overflowed, information would be lost, as multiplication by 2 is equivalent to shifting. The advantage of using a prime is less clear, but it is traditional. A nice property of 31 is that the multiplication can be replaced by a shift and a subtraction for better performance: 31 * i == (i << 5) - i. Modern VMs do this sort of optimization automatically.


## 总结

  对于equals，我们必须遵循如下规则：

      对称性：如果x.equals(y)返回是“true”，那么y.equals(x)也应该返回是“true”。

      反射性：x.equals(x)必须返回是“true”。

      类推性：如果x.equals(y)返回是“true”，而且y.equals(z)返回是“true”，那么z.equals(x)也应该返回是“true”。

      一致性：如果x.equals(y)返回是“true”，只要x和y内容一直不变，不管你重复x.equals(y)多少次，返回都是“true”。

      任何情况下，x.equals(null)，永远返回是“false”；x.equals(和x不同类型的对象)永远返回是“false”。

      对于hashCode，我们应该遵循如下规则：

      1. 在一个应用程序执行期间，如果一个对象的equals方法做比较所用到的信息没有被修改的话，则对该对象调用hashCode方法多次，它必须始终如一地返回同一个整数。

      2. 如果两个对象根据equals(Object o)方法是相等的，则调用这两个对象中任一对象的hashCode方法必须产生相同的整数结果。

      3. 如果两个对象根据equals(Object o)方法是不相等的，则调用这两个对象中任一个对象的hashCode方法，不要求产生不同的整数结果。但如果能不同，则可能提高散列表的性能。

      至于两者之间的关联关系，我们只需要记住如下即可：

      如果x.equals(y)返回“true”，那么x和y的hashCode()必须相等。

      如果x.equals(y)返回“false”，那么x和y的hashCode()有可能相等，也有可能不等。
