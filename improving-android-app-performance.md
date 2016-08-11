# 10 条提升 Android 性能的建议
### Activity 泄漏
我们第一个需要修复的问题就是 Activity 泄漏，我们先来看看内存泄漏是怎么发生的。 Activity 泄漏通常是内存泄漏的一种。为什么会泄漏呢？如果你持有一个未使用的 Activity 的引用，其实也就持有了 Activity 的布局，自然也就包含了所有的 View。最棘手的是持有静态引用。别忘了，Activity 和 Fragment 都有自己的生命周期。一旦我们持有了静态引用，Activity 和 Fragment 就不会被垃圾回收器清理掉了。这就是为什么静态引用很危险。
		
		m_staticActivity = staticFragment.getActivity()
我看过太多次这样的代码了。

另外，泄漏 Listener 也是经常会发生的事情。比如说，我有下面的代码。LeakActivity继承自 Activity，我们有一个单例：NastyManager，当我们通过 addListener(this) 将 Activity 作为 Listener 和 NastyManager 绑定起来的时候，不好的事情就发生了。
		
		public class LeakActivity extends Activity {
		  @Override
		  protected void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
		    NastyManager.getInstance().addListener(this);
		  }
		}
想要修复这样的 Bug，其实相当简单，就是在你的 Acitivity 被销毁的时候，将他和 NastyManager 取消掉绑定就好了。

		@Override
		public void onDestroy() {
		  super.onDestroy();
		
		  NastyManager.getInstance().removeListener(this);
		}
相对上面的解决方案，我们自然还有更好的。比如我们真的需要用到单例吗？通常，并不需要。不过某些时候可能真的很需要。我们得权衡和设计。不过无论如何，记住，当 Activity 销毁的时候，在单例中移除掉对 Activity 的引用。下面我们讨论下： 如果是内部类，会发生什么？比如说，我们有一个在 Activity 里有一个很简短的非静态 Handler。

尽管它看起来很短，但是只要它还存活着，那么包含它的 Activity 就会存活着。如果你不信我，在 VM 里试试看。这就是另一个内存泄漏的案例：Activity 内部的 Handler。

		public class MainActivity extends Activity {
		  //...
		  Handler handler;
		  @Override
		  protected void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
		    //...
		    handler = new Handler() {
		      @Override
		      public void handleMessage(Message msg) {
		              }
		  }
}
Handler 是个很常用也很有用的类，异步，线程安全等等。如果有下面这样的代码，会发生什么呢？handler.postDeslayed ，假设 delay 时间是几个小时… 这意味着什么？意味着只要 handler 的消息还没有被处理结束，它就一直存活着，包含它的 Activity 就跟着活着。我们来想办法修复它，修复的方案是WeakReference，也就是所谓的弱引用。垃圾回收器在回收的时候，是会忽视掉弱引用的，所以包含它的 Activity 会被正常清理掉。大概代码如下：
		
		private static class MyHandler extends Handler { 
		  private final WeakReference<MainActivity> mActivity; 
		  // ...
		  public MyHandler(MainActivity activity) {
		    mActivity = new WeakReference<MainActivity>(activity);
		    //... 
		  }
		
		  @Override
		  public void handleMessage(Message msg) { 
		  }
		  //...
		}
概括来说：我们有个内部类，就像 Handler，内部非静态类是不能脱离所属类而单独存活的，Android 里通常是 Activity。所以，看看你的代码里的内部类，确保他们没有出现内存泄漏。

相比非静态内部类，最好使用静态内部类。区别就是静态内部类不依赖所属类，他们拥有不同的生命周期。我经常见到类似的原因引起的内存泄露。

### 如何避免 Activity 泄漏?

- 移除掉所有的静态引用。

- 考虑用 EventBus 来解耦 Listener。

- 记着在不需要的时候，解除 Listener 的绑定。

- 尽量用静态内部类。

- 做 Code Review。个人经验：Code Review 能很早的发现内存泄漏。

- 了解你程序的结构。

- 用类似 MAT，Eclipse Analyzer，LeakCanary 这样的工具分析内存。

- 在 Callback 里打印 Log。

### 滑动 

实现流畅滑动的技巧：UI 线程只用作 UI 渲染。这一条真谛能够解决 99% 的滑动卡顿问题。不要在 UI 线程做下面的事情：

- 载入图片
- 网络请求
- 解析 JSON
- 读取数据库
做这些操作是很慢的，像图片，网络，JSON考虑用现成的库，有很多社区提供的解决方案，数据库考虑下用 Loader，支持批量更新和载入。

### 图片

图片相关的库有很多，比如 Glide, Picasso, Fresco。你可以自己去了解下他们之间的区别，以帮助自己在特定场景下做出取舍。

### 内存

Bitmap 操作是很需要技巧的，图片一般比较大，而且系统对最大内存又有限制和要求。在我面对 4.0 之前的系统的时候，我简直要崩溃了。内存管理也很需要技巧。有的时候需要放到文件里，有的时候需要放到内存里，别忘了，我们还有一个很有用的工具：LRUCache。

### 网络

首先，Java 的网络请求确实是 Android 的一个阻碍。很多 Java.net 的 API 都是阻断执行的，切记不可在 UI 线程执行网络请求。在线程里执行或者直接使用第三方库吧。

异步 HTTP 其实也挺麻烦的，4.4 起 OkHttp 就成了 Android 代码的一部分了，然而… 如果你需要最新版本的 OkHttp ，可以考虑自己引入。另外有个不错的库叫： Volley，也可以试试 Square 的 Retrofit。这些都能让你的网络请求变得更友好。

### 大 JSON 

在 UI 线程，也不做解析 Json 的事情，因为这是一个很耗时的事情。试着用 Google 的 GSON 来做反序列化的操作。

对于巨大的 JSON 解析，建议用更快的 Jackson 以及 ig-json-parser，这两个工具在 JSON 的解析上做的非常漂亮。从公司的反馈结果来看 ig-json-parser 的效率是最高的。

**Looper.myLooper() == Looper.getMainLooper()** 是可以帮助你确定你是否在主线程的代码。

### 如何优化滑动速度? 

- UI 线程只做 UI 更新。
- 理解并发 API。
- 开始使用优秀的第三方库。
- 使用 Loader 加载数据库数据
- 之所以要用第三方库，是因为你自己去完善一个复杂功能是需要花时间的。如果你打算专注在自己的功能性的 App 上，那么用库吧。

### 并发 APIs 

如何让 App 快速响应请求是个很重要。开发者们，甚至包括我，经常忘记 Service 的方法是在 UI 线程执行的。请考虑使用 IntentService，AsyncTask，Executors，Handler 和 Loopers。

我们来盘点下这些的区别：

### IntentService 

IntentService 是一个单线程，一次一个任务的工作流。我们没有很复杂的任务系统。如果你有大型复杂的任务，而且这个任务不需要跟 UI 打交道，那么考虑用 IntentService 吧。

### AsyncTask 

如果你的任务需要更新 UI，那么考虑用 AsyncTask 吧，AsyncTask 虽然相对容易，但是有些坑得留意。当你旋转手机的时候，Activity 会被关闭，然后重启。不然可能造成内存泄露。

### Executor Framework 

这是 Java 6 自带的并发方案。默认是存在一个由系统管理的线程池，你可以通过 callback，future 来控制和管理。这根 MapRedues 发难有点像，面对复杂的任务，你希望能够把他们拆分交给多个线程来处理。Executor 的框架就很能胜任这种场景。

### 如何适应并发APIs? 

- 学会和理解 API，懂得权衡
- 确保找到了问题的正确解决方案
- 了解问题真实所在
- 重构代码


### Deprecation 

我们肯定都知道，最好能够避免使用废弃的 API。比如以下的例子：

- 不要通过反射来调用私有 API。
- 不要再 NDK 和 C 语言层调用私有 Native 方法。
- 不要轻易调用 Runtime.exec 指令完成进程通讯功能。
- adb shell am 做进程通讯并不好。
- 废弃的意思是这些 API 将会被移除，通常在正式版发布 1，2天左右，- - 你的 App 就不会工作了。更糟糕的情况是，如果你的 App 依赖了一些库，而这些库哟改了废弃的 Api 或者工具。那可就惨了，如果一旦作者没有更新…你懂得。

- 不要用废弃 Api 的另一个原因是性能问题和安全问题。

### 如何避免废弃 Api：

- 使用正确的 API。
- 重构依赖。
- 不要滥用系统。
- 更新依赖和工具。
- 越新的通常越好。
- 用 Toolbar 而非 ActionBar，在需要动画的时候用 RecyclerView，因为它专门为动画做过优化。同时 Android M 里移除了 Apache Http Connection。请使用 HttpURLConnection，它拥有更简单的 API，更小的体积，默认的压缩功能，更好的 Response 缓存，等等其他很赞的功能。

### 架构 

架构中的 Bug 总是最为烦人。想要避免这种问题，学习下 App 组件的生命周期。比如什么是 Activity 的 Flag？什么是 Fragment？什么事 stated fragment？什么是 task？读读文档，尝试下用回调的 log 搞清楚这些概念。

当我在选择一个库的时候，我会用下面的 Checklist 来决策：

- 确保它能够解决你的问题。
- 确保它和当前所有的依赖能正常工作。
- 检查依赖
- 留意一下依赖的版本冲突
- 了解维护情况和成本


总的来说，提及架构和设计，最好的方法就是让你的程序最快响应。确保用户能够快速理解你的 App，并且拥有良好体验。

​