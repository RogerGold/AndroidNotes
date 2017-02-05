# 构建字符串

有些时候， 需要由较短的字符串构建字符串，例如，按键或者来自文件中的单词。采用字符串连接的方式达到此目的的效率比较低。
每次连接字符串都会构建一个新的String对象， 既耗费时间，也浪费控件，使用StringBuilder可以避免这个问题。

如果需要用许多小段的字符串构建一个字符串，那么可以这样：
1. 构建一个空的自古次构造器。

       StringBuilder builder = new StringBuilder();
  
2. 当每次需要添加一部分内容时， 就使用append方法.

       builder.append("hello");
       builder.append("world");
   
3. 在需要构建字符串时就调用toString方法。

       String str = builder.toString();
