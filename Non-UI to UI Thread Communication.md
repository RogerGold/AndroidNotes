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

### Use post( ) method call

### Use the Handler framework

### Use a Broadcasts and BroadcastReceiver (optionally with LocalBroadcastManager)

### Use an AsyncTask’s onProgressUpdate( ) method
