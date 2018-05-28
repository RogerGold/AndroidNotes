# Non-UI to UI Thread Communication

- Use runOnUiThread( ) method call
- Use post( ) method call
- Use the Handler framework
- Use a Broadcasts and BroadcastReceiver (optionally with LocalBroadcastManager)
- Use an AsyncTask’s onProgressUpdate( ) method

### Use runOnUiThread( ) method call
Using, a non-UI thread communicates the desire to request work be run on the UI thread.  This is accomplished under the covers by publishing the requested action to the event queue of the UI thread.  When it can, the UI thread picks up the action message in the event queue and performs the UI change.

In the DoSomethingThread’s publishProgress() method, the activity’s runOnUiThread method is invoked. It is passed a Runnable object containing the UI-updating code. In this case, it contains the code to update the TextView widget through a call to updateResults(text).

      private void publishProgress(int randNum) {
        Log.v(TAG, "reporting back from the Random Number Thread");
        final String text = String.format(getString(R.string.service_msg), randNum);
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            updateResults(text);
          }
        }

So the non-UI thread doesn’t actually update the UI (the TextView widget).  It sends a message via the runOnUiThread() call to the UI event queue.  The runOnUiThread() method is a convenience for completing this messaging operation.  The UI thread watches the event queue and eventually reacts to the request.

What are the pros/cons when considering using the runOnUiThread.  First off, note that this method is defined on the Activity.  That means that the non-UI thread must have some knowledge or means of getting the Activity in order to take advantage of this method. 

### Use post( ) method call
When a non-UI thread has access to a View component from the user interface, the non-UI thread can make a call to that View component’s post() method to request a UI change.

Just like when calling on runOnUiThread() of an Activity, calling on the View’s post() communicates the desire to request work be run on the UI thread by publishing a message in the event queue of the UI thread.  When it can, the UI thread picks up the action message in the event queue and performs the UI change.  So the  post() method is a convenience (just as runOnUiThread is) for completing this messaging operation.

        private static final String TAG = "DoSomethingThread";
        private static final int DELAY = 5000; // 5 seconds
        private static final int RANDOM_MULTIPLIER = 10;

        @Override
        public void run() {
          Log.v(TAG, "doing work in Random Number Thread");
          while (true) {
            int randNum = (int) (Math.random() * RANDOM_MULTIPLIER);
            publishProgress(randNum);
            try {
              Thread.sleep(DELAY);
            } catch (InterruptedException e) {
              Log.v(TAG, "Interrupting and stopping the Random Number Thread");
              return;
            }
          }
        }

        private void publishProgress(int randNum) {
          Log.v(TAG, "reporting back from the Random Number Thread");
          final String text = String.format(getString(R.string.service_msg), randNum);
          mainFrag.resultsTextView.post(new Runnable() {
            @Override
            public void run() {
              mainFrag.getResultsTextView().setText(text);
            }
          });
        }
      }

In order to use post(), your non-UI thread must have awareness of a View component (from the UI).  This allows the non-UI thread to avoid direct connection to the Activity (which was required with runOnUiThread).  There are times when your non-UI thread may know a View and not an Activity or vice versa.  However, both options might more tightly couple the non-UI thread to the UI side given View components and Activities are UI “stuff.”
 
Unlike the runOnUiThread() method, the post() method does not check whether the current thread is the UI thread.  Therefore the post() method does not cause the Runnable to execute immediately if it is on the UI thread.  Instead, post() always has an event message pushed to the message queue for reaction by the UI thread.

ref [What's the difference between Activity.runOnUiThread and View.post?
](https://stackoverflow.com/questions/10558208/android-whats-the-difference-between-activity-runonuithread-and-view-post)
### Use the Handler framework
First, create a Handler in the UI thread to receive and react to new messages sent by the non-UI thread.  Here are the steps to create the Handler:

1 Create class that extends android.os.Handler. For simplicity, I created the Handler subclass (called HandlerExtension here) as an inner class to the application’s activity.

             private final WeakReference<ShowSomethingActivity> currentActivity;

              public HandlerExtension(ShowSomethingActivity activity){
                currentActivity = new WeakReference<ShowSomethingActivity>(activity);
              }

              @Override
              public void handleMessage(Message message){
                ShowSomethingActivity activity = currentActivity.get();
                if (activity!= null){
                   activity.updateResults(message.getData().getString("result"));
                }
              }
            }
            
            //add a property to hold the Handler subclass instance in the Activity.
            Handler resultHandler;
            // from the activity’s onCreate() method, create an instance of the Handler so that it can start processing any incoming messages from the non-UI thread.
            resultHandler = new HandlerExtension(this);
            
  The code here may look a little complex due to the static nature and WeakReference in the subclass.  If you try to create a simple class that extends Handler, you will find Eclipse and the SDK issues a compiler warning that the Handler class should be static or leaks might occur.
  
2 with the Handler subclass code in place, you can create a message from the non-UI thread and publish it into the UI thread’s message queue using the Handler subclass. Simply create an instance of Message and add data to the message to indicate to the Handler what UI changes should occur (in this case, providing the random number that needs to be displayed in the TextView widget). 

              private void publishProgress(int randNum) {
                Log.v(TAG, "reporting back from the Random Number Thread");
                String text = String.format(getString(R.string.service_msg),randNum);
                Bundle msgBundle = new Bundle();
                msgBundle.putString("result", text);
                Message msg = new Message();
                msg.setData(msgBundle);
                resultHandler.sendMessage(msg);
              }
  
  The runOnUiThread() and post() methods examined in previous posts are really special Hander Framework conveniences.  They use the event queue on the UI thread to perform their task.  So why use the Handler Framework directly as shown here?  Using the Handler Framework directly is a bit more complex, but it allows you more control.  This is a generic framework for thread communication – any thread.  It also allows the non-UI thread to communicate without direct knowledge/ties to the activity or UI side components.  The non-UI merely has to post a message to a handler.
  
ref [This Handler class should be static or leaks might occur: IncomingHandler
](https://stackoverflow.com/questions/11407943/this-handler-class-should-be-static-or-leaks-might-occur-incominghandler)  
### Use a Broadcasts and BroadcastReceiver (optionally with LocalBroadcastManager)
Broadcasts are Android Intents that indicate some action has occurred.  Some broadcasts are system broadcasts.  For example, one of the built in Android broadcast is that the battery is low.  You can create your own custom broadcasts as well.

Broadcast receivers are components in the application that listen for broadcasts and take some action.  You could, for example, build a broadcast receiver to listen for the battery getting low broadcast event in order to inform the user that unsaved data should be saved quickly.  Of course, you can also build a broadcast receiver to listen for your own custom application broadcasts.

So, a broadcast and broadcast receiver can be used to accomplish the non-UI to UI thread communications.  The non-UI thread can publish a broadcast intent that a broadcast receiver associated to the UI thread uses to perform the UI update.

Now, per the Android documentation on BroadcastReceivers, if your custom application broadcasts are not going to be used across applications, you should consider using a LocalBroadcastManager to send a local broadcast versus a system broadcast.  A LocalBroadcastManager’s intent broadcasts are not broadcast to other applications.  They are therefore a bit more efficient and secure than using a general broadcast message.

Therefore, given the fact that the non-UI to UI thread communication is local to your application, I would recommend (and will show below) using the LocalBroadcastManager to perform the thread communications.

First, from the non-UI thread, create an Intent that provides the necessary information to the UI thread about the user interface updates that are required.  In this example, the non-UI thread simply provides the new random number that was generated as extra data (under the key of “result”) in the Intent.  Then use an instance of Android’s LocalBroadcastManager to send the local broadcast.

      Intent intent = new Intent("com.intertech.random.generation");
      intent.putExtra("result", text);
      LocalBroadcastManager.getInstance(ShowSomethingActivity.this).sendBroadcast(intent);

Set a Broadcast Receiver Listening

On the UI thread, you need to create an instance of BroadcastReceiver to listen for updates coming from the non-UI thread.  In this simple application, I created an anonymous BroadcastReceiver instance from within the createBroadcastRecevier() method which is called from the onCreate( ) method of the application’s main activity.

      private BroadcastReceiver createBroadcastReceiver() {
        return new BroadcastReceiver() {
          @Override
          public void onReceive(Context context, Intent intent) {
            updateResults(intent.getStringExtra("result"));
          }
        };
      }
      
The updateResults() method of the broadcast receiver gets the TextView and updates the string with what is in the broadcast’s extra data at “result”.

Once the broadcast receiver is created in onCreate(), use a LocalBroadcastManager to register the UI thread for the broadcasts sent by the non-UI thread, specifically those with the string action of “com.intertech.random.generation”);

      @Override
      protected void onCreate(Bundle savedInstanceState) {
        ...

        resultReceiver = createBroadcastReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(resultReceiver, new IntentFilter("com.intertech.random.generation"));

        ...
      }
      
Now, once the non-UI thread publishes its local broadcast, the broadcast receiver listening for the message gets the Intent and takes on the responsibility of updating the UI on the UI thread.

As a last bit of housekeeping, make sure to unregister the broadcast receiver when the communication between non-UI and UI threads is no longer needed.  In this example, I unregister the BroadcastReceiver in the onDestroy() method of the main activity.

      protected void onDestroy() {
        if (resultReceiver != null) {
          LocalBroadcastManager.getInstance(this).unregisterReceiver(resultReceiver);
        }
        super.onDestroy();
      }
      
The broadcast option is not reliant on the message event queue.  Instead it relies on a different Android set of components; namely the Intent and Intent listener called a broadcast receiver.  This sub-framework has pluses and minuses.  There are no convenience methods as provided through methods like post() and runOnUiThread() using the thread’s event queue.  Some consider working with Intents, BroadcastReceivers (and LocalBroadcastManager) a bit more complex.  However, the broadcast intent can conveniently carry quite a bit of data to the UI thread from the non-UI thread.  Also importantly, the non-UI thread and UI thread do not have to share any component knowledge.  So the non-UI thread is quite decoupled from the UI thread.  The only information shared by the two threads is the name of the intent action.

### Use an AsyncTask’s onProgressUpdate( ) method
The AsyncTask is a special Android convenience class for creating a new Thread that can “publish results on the UI thread without having to manipulate threads and/or handlers.”  That last quote is directly from the AsyncTask class documentation in the Android reference guide.  In other words, the AsyncTask was explicitly built to provide Android developers with an easy way to create threads that have a direct communication channel back to the UI thread.

In order to create an AsynTask, developers must extend the abstract AsyncTask super class and then implement the methods below.

- doInBackground() – code here is executed on a new, non-UI thread that Android creates when the AsynTask is executed.  This is the only - required method of an AsyncTask subclass.
- onPreExecute() – code executed on the UI thread by Android before non-UI thread work is executed in doInBackground() code.
- onPostExecute() – code executed on the UI thread by Android after non-UI thread work is executed in doInBackground.
- onProgressUpdate – called on the UI thread by Android whenever publishProgress(Progress…) is called (typically in the doInBackground method) to provide the user interface (and user) with updates while the separate thread is still running.

Notice that three of four methods of the AsyncTask run on the UI thread.  The only method that runs in a non-UI thread is doInBackground().  So you can see the AsyncTask was really built to provide that non-UI to UI communications.

      public class DoSomethingTask extends AsyncTask<Void, String, Void> {

        private static final String TAG = "DoSomethingTask";
        private static final int DELAY = 5000; // 5 seconds
        private static final int RANDOM_MULTIPLIER = 10;

        @Override
        protected void onPreExecute() {
         Log.v(TAG, "starting the Random Number Task");
         super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
         Log.v(TAG, "reporting back from the Random Number Task");
         updateResults(values[0].toString());
         super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(Void result) {
         Log.v(TAG, "cancelled the Random Number Task");
         super.onCancelled(result);
        }

        @Override
        protected Void doInBackground(Void... params) {
         Log.v(TAG, "doing work in Random Number Task");
         String text="";
         while (true) {
          if (isCancelled()) {
           break;
          }
          int randNum = (int) (Math.random() * RANDOM_MULTIPLIER);
          text = String.format(getString(R.string.service_msg), randNum);
          publishProgress(text);
          try {
           Thread.sleep(DELAY);
          } catch (InterruptedException e) {
           Log.v(TAG, "Interrupting the Random Number Task");
          }
         }
         return null;
        }
      }

Note the random number generation (as well as the thread sleeping) occurs in the doInBackground() method – again to simulate long running, continuous work that is often accomplished in a background thread.  Also note that the doInBackground() method calls postProgress() with new String data after it has generated a random number and wants to send this data back to the UI thread.

DoSomethingTask extends AsyncTask and has three generic types.  The first, which in this example is Void, defines the input parameter type.  This simple task takes no input from the UI to generate a random number.  Your background work may need some information from the UI side to get it working and this parameter specifies the type of information being supplied.  This type defines the input parameter type for the doInBackground() method.

The second AsyncTask generic type provided, which is a String here, is the type of data passed to the onProgressUpdate() method in publishProgess() calls made from doInBackground().  Here, the new random number generated by doInBackground() is passed back to the UI thread in order to update the display by passing the random number as String text to the onProgressUpdate() call.

The last generic type to AsyncTask subclasses is the return type of the doInBackground() work.  In this case, the doInBackground() method does not return anything back to the UI thread (except through onProgressUpdate) so the third type is also Void.  Again, your non-UI threads may wish to return some information back to the thread’s creator and this is the type of information passed back.

With this AsyncTask in place, the UI – namely the owning Activity that drives the application – can create an instance of the AsyncTask (DoSomethingTask) and have its background work begin by calling execute.  This work is accomplished in the startGenerating() method (below) which I have setup to be called by an onClickListener waiting for the Start button to be pushed.

      private void startGenerating() {
        randomWork = new DoSomethingTask();
        randomWork.execute();
      }

Finally, the AsyncTask calls the Activity’s updateResult() method (below) from the onProgressUpdate() method when a new random number is generated and the UI TextView displaying the number needs to be updated.  Again, this is possible because the onProgressUpdate() method runs on the UI thread.

      public void updateResults (String results){
        mainFrag.getResultsTextView().setText(results);
      }
      
Note additional cleanup methods are provided in the code to stop the AsyncTask when the Stop button is pushed. It should also be noted that this example was kept simple for demonstration sake.  Therefore, it has some faults you would want to correct in a real app situation.  For example, you might want to make sure only one AsyncTask is ever created (right now you could create many by clicking on Start many times).

The AsyncTask is a real convenience for Android developers in that it allows them to accomplish multithreading without having to think about all the communications across threads and without having to think about Runnables, Thread instances, special methods to call, queues, etc. to get the work done.  The convenience of the three UI-thread methods makes getting information across the thread boundry real easy.  However, the AsyncTask is fairly basic and when more complex multithreading needs arise, it may be too simple to use.  In fact, the documentation is pretty clear about when to use AsyncTask and when to move to something like classes available in Java’s concurrent package.

AsyncTasks should ideally be used for short operations (a few seconds at the most.) If you need to keep threads running for long periods of time, it is highly recommended you use the various APIs provided by the java.util.concurrent pacakge such as Executor, ThreadPoolExecutor and FutureTask.

Good Android developers know it is important to use separate threads to avoid ANR.  The trick is sometimes figuring out how to get those threads communicating without creating other problems like CalledFromWrongThreadException.  Now you are armed with an understanding of thread communication options and considerations for making the best decision for your application needs.
