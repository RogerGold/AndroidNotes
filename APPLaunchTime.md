# APP launch time


### 启动方式

![starts](https://github.com/RogerGold/media/blob/master/starts.PNG)

在安卓中应用的启动方式分为两种：冷启动和热启动。

- 冷启动：当启动应用时，后台没有该应用的进程，这时系统会重新创建。
- 热启动：当启动应用时，后台已有该应用的进程（例：按back键、home键，应用虽然会退出，但是该应用的进程是依然会保留在后台，可进入任务列表查看），所以在已有进程的情况下，这种启动会从已有的进程中来启动应用，这个方式叫热启动。

特点

- 冷启动：冷启动因为系统会重新创建一个新的进程分配给它，所以会先创建和初始化Application类，再创建和初始化MainActivity类（包括一系列的测量、布局、绘制），最后显示在界面上。

- 热启动：热启动因为会从已有的进程中来启动，所以热启动就不会走Application这步了，而是直接走MainActivity（包括一系列的测量、布局、绘制），所以热启动的过程只需要创建和初始化一个MainActivity就行了，而不必创建和初始化Application，因为一个应用从新进程的创建到进程的销毁，Application只会初始化一次。

### 启动过程

![launches](https://github.com/RogerGold/media/blob/master/launches.PNG)

当点击app图标后，会有一个start window，之后才会显示activity。
如果app初始化太久，那么白色start window 就会显示很久，影响用户体验。

 ![process](https://github.com/RogerGold/media/blob/master/process.PNG)

  上图展示了app的启动过程。

### 出现白屏以及处理

![async](https://github.com/RogerGold/media/blob/master/async.PNG)

![takeTiem](https://github.com/RogerGold/media/blob/master/takeTiem.PNG)

I/O读写，加载图片， 等待网络， 填充视图等行为会延迟启动时间。

也不要在Application的onCreate里处理耗时的任务或做太多的事情。

![Application](https://github.com/RogerGold/media/blob/master/Application.PNG)

使用lazyLoading，异步加载，或者postLoad来解决。

![postLoad](https://github.com/RogerGold/media/blob/master/postLoad.PNG)

![](https://github.com/RogerGold/media/blob/master/lazyLoading.PNG)

![loading](https://github.com/RogerGold/media/blob/master/delay.PNG)

![loading](https://github.com/RogerGold/media/blob/master/loading.PNG)


或者添加一个启动界面，主题，不显示白屏状态。

![lunachScreen](https://github.com/RogerGold/media/blob/master/lunachScreen.PNG)

![setTheme](https://github.com/RogerGold/media/blob/master/setTheme.PNG)
XML布局优化
此部分适用于解析、处理、绘制静态xml时的优化

xml布局优化。解决方法如下：

使用Include，Merge，viewStub简化布局
使用相对布局，layer-list降低树的层级
使用gone标签可以跳过绘制
被遮挡的view避免重复绘制

文／BlackSwift（简书作者）
原文链接：http://www.jianshu.com/p/d97b390da87b
著作权归作者所有，转载请联系作者获得授权，并标注“简书作者”。
### 工具
可以使用[Systrace](https://developer.android.com/studio/profile/systrace.html)工具来检查。
