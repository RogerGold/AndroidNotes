# Android Handler Internals

For an Android application to be responsive, you need to prevent the UI thread from blocking. 
Responsiveness also increases when blocking or computationally intensive tasks are offloaded to worker threads.
The results of these operations often need to update UI components, which must be performed on the UI thread.
Mechanisms like blocking queues, shared memory, and pipes impose blocking problems for the UI thread.
To prevent this issue, Android provides its own message passing mechanism — the Handler. 

The Handler is a fundamental component in the Android framework. It offers a non-blocking message passing mechanism. 
Neither the producer nor the consumer block during message hand-offs.

### A Deeper Look Inside the Handler
The components of a Handler are:
- Handler
- Message
- Message Queue
- Looper

### Handler
The [Handler](https://developer.android.com/reference/android/os/Handler.html) is the immediate interface for message passing between threads. 
Both the consumer and producer threads interact with the Handler by invoking the following operations:

- creating, inserting, or removing Messages from the Message Queue
- processing Messages on the consumer thread

![Handler](https://github.com/RogerGold/media/blob/master/handler.PNG)

Each Handler is associated with a Looper and a Message Queue. There’s two ways to create a Handler:
- through the default constructor, which uses the Looper associated with the current thread
- by explicitly specifying which Looper to use

A Handler can’t function without a Looper because it can’t put messages in the Message Queue. 
Thus, it won’t receive any messages to process.

    public Handler(Callback callback, boolean async) {
        // code removed for simplicity
        mLooper = Looper.myLooper();
        if (mLooper == null) {
            throw new RuntimeException( “Can’t create handler inside thread that has not called Looper.prepare()”);
        }
        mQueue = mLooper.mQueue;
        mCallback = callback;
        mAsynchronous = async;
    }
    
 The snippet from [Android Source](https://github.com/android/platform_frameworks_base/blob/master/core/java/android/os/Handler.java#L188) (above) demonstrates the logic of constructing a new Handler. 
 The handler checks if the current thread has a valid Looper.
 If not, it throws a Runtime exception. The Handler then receives a reference to the Looper’s Message Queue.
 
#### Note: Multiple Handlers associated with the same thread share the same Message Queue because they share the same Looper.
The Callback is an optional argument. If provided, it processes messages dispatched by the Looper.

### Message
The Message acts as a container for arbitrary data.
The producer thread sends Messages to the Handler, which enqueues to the Message Queue.
The Message provides three pieces of extra information, required by the Handler and Message Queue to process the message:
- what — an identifier the Handler can use to distinguish messages and process them differently
- time — informs the Message Queue when to process a Message
- target — indicates which Handler should process the Message

![/handler_message](https://github.com/RogerGold/media/blob/master/handler_message.PNG)

Message creation typically uses of one of the following Handler methods:
    public final Message obtainMessage()
    public final Message obtainMessage(int what)
    public final Message obtainMessage(int what, Object obj)
    public final Message obtainMessage(int what, int arg1, int arg2)
    public final Message obtainMessage(int what, int arg1, int arg2, Object obj)
    
 The Message is obtained from the message pool. The supplied arguments populate the fields of the Message.
 The Handler also sets the Message’s target to itself. This allows us to chain the call as such:
     mHandler.obtainMessage(MSG_SHOW_IMAGE, mBitmap).sendToTarget();
     
 The Message pool is a LinkedList of Message objects with a maximum pool size of 50. 
 After the Handler processes the Message, the Message Queue returns the object to the pool and resets all fields.   
 
 When posting a Runnable to the Handler via post(Runnable r), the Handler implicitly constructs a new Message. 
 It also sets the callback field to hold the Runnable.
     Message m = Message.obtain();
    m.callback = r;
    
 ![](https://github.com/RogerGold/media/blob/master/thread_send_message_handler.PNG)
 
 At this point we can see the interaction between a producer thread and a Handler. 
 The producer creates a Message and sends it to the Handler. The Handler then enqueues the Message into the Message Queue. 
 The Handler processes the Message on the consumer thread sometime in the future.
 
 ### Message Queue
 The Message Queue is an unbounded LinkedList of Message objects. 
 It inserts Messages in time order, where the lowest timestamp dispatches first.
 
 ![/message_queue](https://github.com/RogerGold/media/blob/master/message_queue.PNG)
 
 The MessageQueue also maintains a dispatch barrier that represents the current time according to SystemClock.uptimeMillis.
 When a Message timestamp is less than this value, the message is dispatched and processed by the Handler.
 
 The Handler offers three variations for sending a Message:
    public final boolean sendMessageDelayed(Message msg, long delayMillis)
    public final boolean sendMessageAtFrontOfQueue(Message msg)
    public boolean sendMessageAtTime(Message msg, long uptimeMillis)
    
  Sending a message with a delay sets the Message’s time field as SystemClock.uptimeMillis() + delayMillis.
  
  Messages sent with a delay have the time field set to SystemClock.uptimeMillis() + delayMillis.
  Whereas, Messages sent to the front of the queue have the time field set to 0, and process on the next Message loop iteration. 
  Use this method with care as it can starve the message queue, cause ordering problems, or have other unexpected side-effects.
  
  Handlers are often associated with UI components, which reference an Activity. 
  The reference from the Handler back to these components can potentially leak the Activity. 
  Consider the following scenario:
  
      public class MainActivity extends AppCompatActivity {
        private static final String IMAGE_URL = "https://www.android.com/static/img/android.png";

        private static final int MSG_SHOW_PROGRESS = 1;
        private static final int MSG_SHOW_IMAGE = 2;

        private ProgressBar progressIndicator;
        private ImageView imageView;
        private Handler handler;

        class ImageFetcher implements Runnable {
            final String imageUrl;

            ImageFetcher(String imageUrl) {
                this.imageUrl = imageUrl;
            }

            @Override
            public void run() {
                handler.obtainMessage(MSG_SHOW_PROGRESS).sendToTarget();
                InputStream is = null;
                try {
                    // Download image over the network
                    URL url = new URL(imageUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();
                    is = conn.getInputStream();

                    // Decode the byte payload into a bitmap
                    final Bitmap bitmap = BitmapFactory.decodeStream(is);
                    handler.obtainMessage(MSG_SHOW_IMAGE, bitmap).sendToTarget();
                } catch (IOException ignore) {
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException ignore) {
                        }
                    }
                }
            }
        }

        class UIHandler extends Handler {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_SHOW_PROGRESS: {
                        imageView.setVisibility(View.GONE);
                        progressIndicator.setVisibility(View.VISIBLE);
                        break;
                    }
                    case MSG_SHOW_IMAGE: {
                        progressIndicator.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                        imageView.setImageBitmap((Bitmap) msg.obj);
                        break;
                    }
                }
            }
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            progressIndicator = (ProgressBar) findViewById(R.id.progress);
            imageView = (ImageView) findViewById(R.id.image);

            handler = new UIHandler();

            final Thread workerThread = new Thread(new ImageFetcher(IMAGE_URL));
            workerThread.start();
        }
    }

In this example, the Activity starts a new worker thread to download and display an image in an ImageView. 
The worker thread communicates UI updates via the UIHandler, which retains references to Views and updates their 
state (toggle visibility, set the bitmap).

Let’s assume the worker thread is taking long to download the image due to slow network. 
Destroying the Activity before the worker thread completes results in an Activity leak. 
There are two strong references in this example. One between the worker thread and UIHandler,
and another between the UIHandler and the views. This prevents the Garbage Collector from reclaiming the Activity reference.

Now, let’s look at another example:

    public class MainActivity extends AppCompatActivity {
        private static final String TAG = "Ping";

        private Handler handler;

        class PingHandler extends Handler {
            @Override
            public void handleMessage(Message msg) {
                Log.d(TAG, "Ping message received");
            }
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            handler = new PingHandler();

            final Message msg = handler.obtainMessage();
            handler.sendEmptyMessageDelayed(0, TimeUnit.MINUTES.toMillis(1));
        }
    }
    
In this example, the following sequence of events occur:
- A PingHandler is created
- The Activity sends a delayed Message to the Handler, which enqueues into the MessageQueue
- The Activity is destroyed before the Message dispatches
- The Message is dispatched and processed by the UIHandler, and a log statement is output   

Though it may not be apparent at first, the Activity leaks in this example as well.
After destroying the Activity, the Handler reference should be available for Garbage Collection. 
However, when we create a Message object, it retains a reference to the Handler:

    private boolean enqueueMessage(MessageQueue queue, Message msg, long uptimeMillis) {
        msg.target = this;
        if (mAsynchronous) {
            msg.setAsynchronous(true);
        }
        return queue.enqueueMessage(msg, uptimeMillis);
    }
    
 The Android Source snippet (above) shows that all Messages sent to a Handler eventually invoke enqueueMessage. 
 Notice that the Handler reference is explicitly assigned to msg.target. 
 This tells the Looper which Handler should process the message when it’s retrieved from the MessageQueue.
 
 The Message is added to the MessageQueue, which now holds a reference to the Message. 
 The MessageQueue also has a Looper associated with it. 
 An explicit Looper lives until it’s terminated, whereas the Main Looper lives as long as the application does. 
 The Handler reference lives as long as the Message isn’t recycled by the MessageQueue. 
 Once it’s recycled, it’s fields, including the target reference, are cleared.
 
 Though there is a long living reference to the Handler, it isn’t clear if the Activity leaks. 
 To check for a leak, we must determine if the Handler also references the Activity within the class. 
 In this example, it does. There’s an implicit reference retained by a non-static class member to its enclosing class. 
 Specifically, the PingHandler wasn’t declared as a static class, so it has an implicit reference to the Activity.
 
 Using a combination of a WeakReference and a static class modifier prevents the Handler from leaking the Activity. 
 When the Activity is destroyed, the WeakReference allows the Garbage Collector to reclaim the object you 
 want to retain (typically an Activity). The static modifier on the inner Handler class prevents an implicit 
 reference to the parent class.
 
 Let’s modify our UIHandler from this example to address this concern:
 static class UIHandler extends Handler {
 
        private final WeakReference<ImageFetcherActivity> mActivityRef;

        UIHandler(ImageFetcherActivity activity) {
            mActivityRef = new WeakReference(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final ImageFetcherActivity activity = mActivityRef.get();
            if (activity == null) {
                return
            }

            switch (msg.what) {
                case MSG_SHOW_LOADER: {
                    activity.progressIndicator.setVisibility(View.VISIBLE);
                    break;
                }
                case MSG_HIDE_LOADER: {
                    activity.progressIndicator.setVisibility(View.GONE);
                    break;
                }
                case MSG_SHOW_IMAGE: {
                    activity.progressIndicator.setVisibility(View.GONE);
                    activity.imageView.setImageBitmap((Bitmap) msg.obj);
                    break;
                }
            }
        }
    }
    
Now, the UIHandler constructor takes in the Activity, which is wrapped in a WeakReference. 
This allows the Garbage Collector to reclaim the activity reference when the activity is destroyed.
To interact with UI components of the Activity, we need a strong reference to the Activity from mActivityRef.
Since we’re using a WeakReference, we must exercise caution when accessing the Activity. 
If the only path to an Activity reference is through a WeakReference, the Garbage Collector may have already reclaimed it.
We need to check if that’s happened. If it has, the Handler is effectively irrelevant and the messages should be ignored.   

Though this logic addresses leaking memory, there’s still a problem with it. 
The activity is already destroyed, yet the Garbage Collector hasn’t reclaimed the reference. 
Depending on the operation being performed, this can potentially crash your application. 
To work around this, we need to detect what state the activity is in.

Let’s update the UIHandler logic to account for these scenarios:

    static class UIHandler extends Handler {
        private final WeakReference<ImageFetcherActivity> mActivityRef;

        UIHandler(ImageFetcherActivity activity) {
            mActivityRef = new WeakReference(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final ImageFetcherActivity activity = mActivityRef.get();
            if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
                removeCallbacksAndMessages(null);
                return
            }

            switch (msg.what) {
                case MSG_SHOW_LOADER: {
                    activity.progressIndicator.setVisibility(View.VISIBLE);
                    break;
                }
                case MSG_HIDE_LOADER: {
                    activity.progressIndicator.setVisibility(View.GONE);
                    break;
                }
                case MSG_SHOW_IMAGE: {
                    activity.progressIndicator.setVisibility(View.GONE);
                    activity.imageView.setImageBitmap((Bitmap) msg.obj);
                    break;
                }
            }
        }
    }
    
  Now, we can generalize the interaction between a MessageQueue, the Handler, and Producer Threads:
  
  !(handler_messageQ_thread)[https://github.com/RogerGold/media/blob/master/handler_messageQ_thread.PNG]
  
In the figure (above), multiple producer threads submit Messages to different Handlers. 
However, each Handler is associated with the same Looper, so all Messages publish to the same MessageQueue. 
This is important because Android creates several Handlers associated with the Main Looper:
- The Choreographer: handles vsync and frame updates
- The ViewRoot: handles input and window events, configuration changes, etc.
- The InputMethodManager: handles keyboard touch events
- And several others

Tip: Ensure that producer threads aren’t spawning several Messages, as they may starve processing system generated Messages.

Debugging Tips:
You can debug/dump all Messages dispatched by a Looper by attaching a LogPrinter:
    final Looper looper = getMainLooper();
    looper.setMessageLogging(new LogPrinter(Log.DEBUG, "Looper"));
    
Similarly, you can debug/dump all pending Messages in a MessageQueue associated with your Handler by 
attaching a LogPrinter to your Handler:    
    handler.dump(new LogPrinter(Log.DEBUG, "Handler"), "");
    
### Looper
The [Looper](https://developer.android.com/reference/android/os/Looper.html) reads messages from the Message Queue and dispatches execution to the target Handler. 
Once a Message passes the dispatch barrier, it’s eligible for the Looper to read it in the next message loop. 
The Looper blocks when no messages are eligible for dispatch. It resumes when a Message is available.

Only one Looper can be associated with a Thread. 
Attaching another Looper to a Thread results in a RuntimeException. 
The use of a static ThreadLocal object in the Looper class ensures that only one Looper is attached to the Thread.

Calling Looper.quit terminates the Looper immediately. 
It also discards any Messages in the Message Queue that passed the dispatch barrier. 
Calling Looper.quitSafely ensures all Messages ready for dispatch are processed before pending messages are discarded.

!(Looper)[https://github.com/RogerGold/media/blob/master/looperr.PNG]
