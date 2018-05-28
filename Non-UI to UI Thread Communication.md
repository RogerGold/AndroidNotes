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
            // from the activity’s onCreate() method, create an instance of the Handler so that it can start processing any incoming                 //messages from the non-UI thread.
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

### Use an AsyncTask’s onProgressUpdate( ) method
