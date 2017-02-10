# android 数据库操作

1. Q：对table  中的name 排序：
    select * from table order by name;
   出来的结果显示，排序时区分了大小写，所有name  是小写的都在后面。怎样才能做到不区分大小写？
   
   A：使用select * from table order by upper(name);即可。
