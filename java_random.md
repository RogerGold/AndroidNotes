#java随机数

### 1. 获取[min, max]之间的随机数
int max = 50;
int min = 1;
1. Using Math.random()

double random = Math.random() * 50 + 1;
or
int random = (int )(Math.random() * 50 + 1);
This will give you value from 1 to 50 using Math.random()

Why?

random() method returns a random number between 0.0 and 0.999. So, you multiply it by 50, 
so upper limit becomes 0.0 to 49.95, when you add 1, it becomes 1.0 to 50.95, 
now when you you truncate to int, you get 1 to 50. 

or Using Random class in Java.

Random rand = new Random(); 
int value = rand.nextInt(50); 
This will give value from 0 to 49.

For 1 to 50: rand.nextInt((max - min) + 1) + min;

### 2. Random methods
The most common methods are those which return a random number. These methods return a uniform distribution of values, except nextGaussian(). In these examples, x is a Random object.

    Return type   	Call	             Description
      int i =	r.nextInt(int n)	Returns random int >= 0 and < n
      int i =	r.nextInt()	Returns random int (full range)
      long l =	r.nextLong()	Returns random long (full range)
      float f =	r.nextFloat()	Returns random float >= 0.0 and < 1.0
      double d =	r.nextDouble()	Returns random double >=0.0 and < 1.0
      boolean b =	r.nextBoolean()	Returns random double (true or false)
      double d =	xrnextGaussian()	Returns random number with mean 0.0 and standard deviation 1.0

Example: Generating a random Color
To create any color with values for red, green, and blue (the RGB system) between 0-255:

    Random r = new Random();
    Color  c = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));
 
