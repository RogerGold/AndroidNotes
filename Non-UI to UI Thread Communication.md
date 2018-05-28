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
### Use the Handler framework

### Use a Broadcasts and BroadcastReceiver (optionally with LocalBroadcastManager)

### Use an AsyncTask’s onProgressUpdate( ) method
