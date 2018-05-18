# Java中的注解

### 什么是注解？
注解就是元数据，即一种描述数据的数据。

### 注解的作用例子
重写toString()方法并使用了@Override注解。但是，即使我们不使用@Override注解标记代码，程序也能够正常执行。
那么，该注解表示什么？这么写有什么好处吗？事实上，@Override告诉编译器这个方法是一个重写方法(描述方法的元数据)，
如果父类中不存在该方法，编译器便会报错，提示该方法没有重写父类中的方法。如果我不小心拼写错误，
例如将toString()写成了toStrring(){double r}，而且我也没有使用@Override注解，那程序依然能编译运行。
但运行结果会和我期望的大不相同。现在我们了解了什么是注解，并且使用注解有助于阅读程序。

### 为什么要引入注解？
使用Annotation之前(甚至在使用之后)，XML被广泛的应用于描述元数据。不知何时开始一些应用开发人员和架构师发现XML的维护越来越糟糕了。
他们希望使用一些和代码紧耦合的东西，而不是像XML那样和代码是松耦合的(在某些情况下甚至是完全分离的)代码描述。

假如你想为应用设置很多的常量或参数，这种情况下，XML是一个很好的选择，因为它不会同特定的代码相连。
如果你想把某个方法声明为服务，那么使用Annotation会更好一些，因为这种情况下需要注解和方法紧密耦合起来，开发人员也必须认识到这点。

### 自定义注解

java.lang.annotation提供了四种元注解，专门注解其他的注解：

    @Documented –注解是否将包含在JavaDoc中
    @Retention –什么时候使用该注解
    @Target? –注解用于什么地方
    @Inherited – 是否允许子类继承该注解

 @Documented–一个简单的Annotations标记注解，表示是否将注解信息添加在java文档中。

 @Retention– 定义该注解的生命周期。

- RetentionPolicy.SOURCE – 在编译阶段丢弃。这些注解在编译结束之后就不再有任何意义，所以它们不会写入字节码。@Override, @SuppressWarnings都属于这类注解。

- RetentionPolicy.CLASS – 在类加载的时候丢弃。在字节码文件的处理中有用。注解默认使用这种方式。

- RetentionPolicy.RUNTIME– 始终不会丢弃，运行期也保留该注解，因此可以使用反射机制读取该注解的信息。我们自定义的注解通常使用这种方式。

@Target – 表示该注解用于什么地方。如果不明确指出，该注解可以放在任何地方。以下是一些可用的参数。需要说明的是：属性的注解是兼容的，如果你想给7个属性都添加注解，仅仅排除一个属性，那么你需要在定义target包含所有的属性。

- ElementType.TYPE:用于描述类、接口或enum声明
- ElementType.FIELD:用于描述实例变量
- ElementType.METHOD
- ElementType.PARAMETER
- ElementType.CONSTRUCTOR
- ElementType.LOCAL_VARIABLE
- ElementType.ANNOTATION_TYPE 另一个注释
- ElementType.PACKAGE 用于记录java文件的package信息

@Inherited – 定义该注释和子类的关系

Annotations只支持基本类型、String及枚举类型。注释中所有的属性被定义成方法，并允许提供默认值。

AnnotatedElement 接口是所有程序元素（Class、Method和Constructor）的父接口，所以程序通过反射获取了某个类的AnnotatedElement对象之后，程序就可以调用该对象的如下四个个方法来访问Annotation信息：

　- <T extends Annotation> T getAnnotation(Class<T> annotationClass): 返回改程序元素上存在的、指定类型的注解，如果该类型注解不存在，则返回null。
　- Annotation[] getAnnotations():返回该程序元素上存在的所有注解。
　- boolean is AnnotationPresent(Class<?extends Annotation> annotationClass):判断该程序元素上是否包含指定类型的注解，存在则返回true，否则返回false.
　- Annotation[] getDeclaredAnnotations()：返回直接存在于此元素上的所有注释。与此接口中的其他方法不同，该方法将忽略继承的注释。（如果没有注释直接存在于此元素上，则返回长度为零的一个数组。）该方法的调用者可以随意修改返回的数组；这不会对其他调用者返回的数组产生任何影响。
  
### 实例

 自定义注解类FruitName：
         /**
         * 水果名称注解
         *
         */
        @Target(ElementType.FIELD)
        @Retention(RetentionPolicy.RUNTIME)
        @Documented
        public @interface FruitName {
            String value() default "";
        }
        
        
 使用注解：


        /***********注解使用***************/

        public class Apple {

            @FruitName("Apple")
            private String appleName;

        }

Annotations仅仅是元数据，和业务逻辑无关。Annotations不包含业务逻辑，所以需要元数据的用户来做实现业务逻辑。Annotations仅仅提供它定义的属性(类/方法/包/域)的信息。Annotations的用户(同样是一些代码)来读取这些信息并实现必要的逻辑。

        /***********注解处理器***************/

        public class FruitInfoUtil {
            public static void getFruitInfo(Class<?> clazz){

                String strFruitName=" 水果名称：";

                Field[] fields = clazz.getDeclaredFields();

                for(Field field :fields){
                    if(field.isAnnotationPresent(FruitName.class)){
                        FruitName fruitName = (FruitName) field.getAnnotation(FruitName.class);
                        strFruitName=strFruitName+fruitName.value();
                        System.out.println(strFruitName);
                    }
               }
            }
        }

输出结果：


    /***********输出结果***************/
    public class FruitRun {

        public static void main(String[] args) {

            FruitInfoUtil.getFruitInfo(Apple.class);

        }

    }

====================================
 水果名称：Apple
