# 用 LeakCanary 检测内存泄漏

### 内存泄露
但是到底什么是内存泄露呢？我们从一个非技术角度来开始，先来举个例子。

假设我的手代表着我们 App 能用的所有内存。我的手里能放很多东西。比如：钥匙，Android 玩偶等等。设想我的 Android 玩偶需要扬声器才能工作，而我的扬声器也需要依赖 Android 玩偶才能工作，因此他们持有彼此的引用。

我的手里可以持有如此多的东西。扬声器依附到 Android 玩偶上会增加总重量，就像引用会占用内存一样。一旦我放弃了我的玩偶，把他扔到地上，会有垃圾回收器来回收掉它。一旦所有的东西都进了垃圾桶，我的手又轻便了。

不幸的是，有的时候，一些不好的情况会发生。比如：我的钥匙没准和我的 Android 玩偶黏在了一起，阻止我把 Android 玩偶扔到地上。最终的结果就是 Android 玩偶无论如何都不会被回收掉。这就是内存泄露。

有外部的引用（钥匙，扬声器）指向了 本不应该再指向的对象（Andorid 玩偶）。类似这样的小规模的内存泄露堆积以后就会造成大麻烦。
###  LeakCanary
[LeakCanary](https://github.com/square/leakcanary)是一个可以帮助我们发现和解决内存泄露的开源工具。

我现在可能已经清楚了 可被回收的 Android 对象应该及时被销毁。

但是我还是没法清楚饿看到这些对象是否已经被回收掉。有了 LeakCanary 以后，我们给可被回收的 Android 对象上打了智能标记。智能标记能知道他们所指向的对象是否被成功释放掉。如果过一小段时间对象依然没有被释放，他就会给内存做个快照。

LeakCanary 随后会把结果发布出来，帮助我们看到内存到底怎么泄露了，清晰的展示无法被释放的对象的引用链。

### 内存泄漏的原因
1.Static Activities
   
	private static MainActivity activity;

    void setStaticActivity() {
        activity = this;
    }

静态对象的生命周期是从应用运行到结束，所以对象不能马上被释放。当然，我们可以使用Weak references来解决：

 	 private static WeakReference<MainActivity> activityReference;

    void setStaticActivity() {
        activityReference = new WeakReference<MainActivity>(this);
    }



2.Static Views
	 
	private static View view;

    void setStaticView() {
        view = findViewById(R.id.sv_button);
    }

View持有Activity的引用，这样Activity会不能被回收，你也许会说：“只是一个view而已，没啥大不了”。问题是这个view还有一个成员变量：叫 “mContext”，这个东西指向了一个 Acitvity，Acitivty 又指向了一个 Window，Window 有拥有整个 View 继承树。算下来，那可是一大段的内存空间。我们可以在Activity生命周期结束的时候清掉引用，让GC回收。

	private static View view;
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (view != null) {
            unsetStaticView();
        }
    }

    void unsetStaticView() {
        view = null;
    }

3.Inner Classes

泄漏代码：

    private static Object inner;

    void createInnerClass() {
        class InnerClass {
        }
        inner = new InnerClass();
    }

内部类的对象持有外部类的引用。所以不经意也会造成内存泄漏。所以避免使用静态引用。

 	private Object inner;

    void createInnerClass() {
        class InnerClass {
        }
        inner = new InnerClass();
    }

4.Anonymous Classes

大多数的内存泄漏是对象直接或间接的被全局的静态引用持有。导致不能被CG回收。而下面的是因为匿名类（anonymous class）引起的。

AsyncTask:

	    void startAsyncTask() {
	        new AsyncTask<Void, Void, Void>() {
	            @Override protected Void doInBackground(Void... params) {
	                while(true);
	            }
	        }.execute();
	    }
Handler:

    void createHandler() {
        new Handler() {
            @Override public void handleMessage(Message message) {
                super.handleMessage(message);
            }
        }.postDelayed(new Runnable() {
            @Override public void run() {
                while(true);
            }
        }, Long.MAX_VALUE >> 1);
    }
Thread:

    void spawnThread() {
        new Thread() {
            @Override public void run() {
                while(true);
            }
        }.start();
    }
 TimerTask:

    void scheduleTimer() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                while(true);
            }
        }, Long.MAX_VALUE >> 1);
    }

匿名类实际上只是特别的内部类，这些匿名类都是用来执行应用的后台线程的，但是这些后台线程是全局的（app-global）并且持有创建他们的对象的引用。这些匿名类的实例持有外部类的引用，因为他们不是静态内部类（non-static inner class）。

所以，要把这些类声明为静态的嵌套类。静态的嵌套类就不会持有外部类实例的引用。

	private static class NimbleTask extends AsyncTask<Void, Void, Void> {
        @Override protected Void doInBackground(Void... params) {
            while(true);
        }
    }

    void startAsyncTask() {
        new NimbleTask().execute();
    }

Handler:

    private static class NimbleHandler extends Handler {
        @Override public void handleMessage(Message message) {
            super.handleMessage(message);
        }
    }

    private static class NimbleRunnable implements Runnable {
        @Override public void run() {
            while(true);
        }
    }

    void createHandler() {
        new NimbleHandler().postDelayed(new NimbleRunnable(), Long.MAX_VALUE >> 1);
    }

TimerTask:

    private static class NimbleTimerTask extends TimerTask {
        @Override public void run() {
            while(true);
        }
    }

    void scheduleTimer() {
        new Timer().schedule(new NimbleTimerTask(), Long.MAX_VALUE >> 1);
    }

如果你坚持要使用一个匿名类，并且他的存活时间比Activity长，那么记得在Activity的onDestroy()中设置中断，并且在循环中判断中断标志。这样当Activity结束的时候，线程也结束，就不会继续持有外部类的引用。

	private Thread thread;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (thread != null) {
            thread.interrupt();
        }
    }

    void spawnThread() {
        thread = new Thread() {
            @Override public void run() {
                while (!isInterrupted()) {
                }
            }
        }
        thread.start();
    }

5.Sensor Manager

例如：
	
	void registerListener() {
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ALL);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

使用Android服务也可能导致内存泄漏，因为我们常常需要通过注册来使用这些服务，例如上面的代码中，我们把Activity注册为监听器。只要监听器没有被注销，那么Acticity的引用就不会被释放，从而引起内存泄漏。我们要做的就是在活动结束时注销监听器。

 	private SensorManager sensorManager;
    private Sensor sensor;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sensor != null) {
            unregisterListener();
        }
    }

    void unregisterListener() {
        sensorManager.unregisterListener(this, sensor);
    }
### 什么是 弱引用（Weak References）

在垃圾回收过程中，你可以对一个对象有很多的强引用。当这些强引用的个数总和为零的时候，垃圾回收器就会释放掉它。

弱引用，就是一种不增加引用总数的持有引用方式。垃圾回收期是否决定要回收一个对象，只取决于它是否还存在强引用。

所以说，如果我们将我们的 Activity 持有为弱引用，一旦我们发现弱引用持有的对象已经被销毁了，那么这个 Activity 就已经被垃圾回收器回收了。否则，那可以大概确定这个 Activity 已经被泄露了。

弱引用的主要目的是为了做 Cache，而且非常有用。主要就是告诉 GC，尽管我持有了这个对象，但是如果一旦没有对象在用这个对象的时候，GC 就可以在需要的时候销毁掉。

### LeakCanary 的实现
我们有自己的库去解析 heap dump 文件，而且实现的很容易。我们打开 heap dump，加载进来，然后解析。然后我们根据 key 找到我们的引用。然后我们根据已有的 Key 去查看拥有的引用。我们拿到实例，然后得到对象图，再反向推导发现泄漏的引用。

所有的工作实际上都发生在 Android 设备上。当 LeakCanary 探测到一个 Activity 已经被销毁掉，而没有被垃圾回收器回收掉的时候，它就会强制导出一份 heap dump 文件存在磁盘上。然后开启另外一个进程去分析这个文件得到内存泄漏的结果。如果在同一进程做这件事的话，可能会在尝试分析堆内存结构的时候而发生内存不足的问题。

最后，你会得到一个通知，点击一下就会展示出详细的内存泄漏链。而且还会展示出内存泄漏的大小，你也会很明确自己解决掉这个内存泄漏后到底能够解救多少内存出来。

LeakCanary 也是支持 API 的，这样你就可以挂载内存泄漏的回调，比方说可以把内存泄漏问题传到服务器上。

		@Override protected void onLeakDetected(HeapDump heapDump, AnalysisResult result) {
		  
		}
LeakCanary 只是一个开发工具。不要将它用到生产环境中。一旦有内存泄漏，就会展示一个通知给用户，这一定不是用户想看到的。

### LeakCanary API Walkthrough 

我们希望知道的是当生命后期结束后，发生了什么。LearkCanary有一个很简单的 API。

第一步：创建 RefWatcher。给 RefWatcher 传入一个对象的实例，它会检测这个对象是否被成功释放掉。

	public class ExampleApplication extends Application {
	
	  public static RefWatcher getRefWatcher(Context context) {
	    ExampleApplication application = (exampleApplication) context.getApplicationContest();
	    return application.refWatcher;
	  }
	
	  private RefWatcher refWatcher;
	
	  @Override public void onCreate () {
	    super.onCreate();
	    // Using LeakCanary
	    refWatcher = LeakCanary.install(this);
	  }
	}
第二步：监听 Activity 生命周期。然后，当 onDestroy 被调用的时候，我们传入 Activity。

	public ActivityRefWatcher(Application application, final RefWatcher refWatcher) {
	  this.application = checkNotNull(application, "application");
	  checkNotNull(refWatcher, "androidLeakWatcher");
	  lifecycleCallbacks = new ActivityLifecycleCallbacksAdapter() {
	    @Override public void onActivityDestroyed(Activity activity) {
	      refWatcher.watch(activity);
	    }
	  };
	}
	
	public void watchActivities() {
	  // Make sure you don’t get installed twice.
	  stopWatchingActivities();
	  application.registerActivityLifecycleCallbacks(lifecycleCallbacks);
	}

### 在项目中使用LeakCanary
In your build.gradle:

		 dependencies {
		   debugCompile 'com.squareup.leakcanary:leakcanary-android:1.4'
		   releaseCompile 'com.squareup.leakcanary:leakcanary-android-no-op:1.4'
		   testCompile 'com.squareup.leakcanary:leakcanary-android-no-op:1.4'
		 }
In your Application class:

		public class ExampleApplication extends Application {
		
		  @Override public void onCreate() {
		    super.onCreate();
		    if (LeakCanary.isInAnalyzerProcess(this)) {
		      // This process is dedicated to LeakCanary for heap analysis.
		      // You should not init your app in this process.
		      return;
		    }
		    LeakCanary.install(this);
		    // Normal app init code...
		  }
		}


![LeakCanary](https://github.com/RogerGold/media/blob/master/LeakCanary.png)