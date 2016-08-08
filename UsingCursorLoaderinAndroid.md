# Using Cursor Loader in Android

### The characteristics of Loaders can be enumerated as follows:   

- Loaders are basically used to provide asynchronous loading of data for an Activity of Fragment on Non-UI thread. While the application should perform any call to a Loader from the main thread, the Loader (or subclasses of Loader) performs their work in a separate thread and delivers its results to the main thread.  
- The code implementation should not derive directly from android.content.Loader class but specifically from android.content.CursorLoader class. 
- The callbacks of the Loader are invoked at different stages during loading of data in an Activity or Fragment. In short, an Activity or a Fragment are required to implement Listeners to use Loaders.   
- Loaders internally use AsyncTask to perform the data load. There is no performance gain when - Loaders are compared to AsyncTask, provided that the AsyncTask are designed and developed properly.   
- Loader, more specifically, CursorLoader queries the Content Resolver in the background thread so that the application's User Interface is not blocked and returns the loaded Cursor to the Activity or Fragment. 
- CursorLoader implements Loader protocol for querying cursors.
- CursorLoader handles the life cycle of the cursor. When using CursorLoader, the developer should never call close() on the cursor.     
- Loader persist the data fetched to avoid repetitive fetch operations for simple Activity refresh event like orientation change, keyboard open etc.  
- Loader monitor the source of its data and deliver new results when the content changes. It automatically  reconnects to the last loader’s cursor when being recreated after a configuration change avoiding the  need to re-query their data. In other words, CursorLoader auto updates and hence there is no need to  requery the cursor.   
- Loaders, in particular CursorLoader, are expected to retain their data after being stopped. This allows applications to keep their data across the Activity or fragment's onStop() and onStart() methods, so that when users return to an application, they don't have to wait for the data to reload.   
- Loaders are available as a part of the compatibility library. So developers can use it in applications that run on android build previous to HoneyComb. 
- Developers should use CursorLoader instead of Activity.managedQuery or Activity.startManagingCursor starting in android 3.0.  
- There is only one LoaderManager per Activity or Fragment. The LoaderManager manages the life of one or more Loader instances automatically within an Activity or Fragment. 
An application that uses Loaders typically includes the following:
An Activity or Fragment.
An instance of the LoaderManager. 
A CursorLoader: to load the data backed by a ContentProvider. Alternatively, developers are free to implement their own subclass of Loader or AsyncTaskLoader to load other types of data. 
An implementation for LoaderManager.LoaderCallbacks. It is a callback interface that lets a client interact with the LoaderManager.  
A way of displaying the loader's data, such as a SimpleCursorAdapter. 
A data source, such as a ContentProvider, when using a CursorLoader.

### An application that uses Loaders typically includes the following:
- An Activity or Fragment.
- An instance of the LoaderManager. 
- A CursorLoader: to load the data backed by a ContentProvider. Alternatively, developers are free to implement their own subclass of Loader or AsyncTaskLoader to load other types of data. 
- An implementation for LoaderManager.LoaderCallbacks. It is a callback interface that lets a client interact with the LoaderManager.  
- A way of displaying the loader's data, such as a SimpleCursorAdapter. 
- A data source, such as a ContentProvider, when using a CursorLoader.  

### Steps to write code    

public class MyActivity implements LoaderManager.LoaderCallbacks<Cursor>

loadermanager.initLoader(1, null, this);

1. The activity needs to implement LoaderManager.LoaderCallbacks:
2. Initialize the loader:
3. Implement the loader callback methods.
###
- onCreateLoader: Instantiate and return a new Loader for the given ID. This is where the cursor is created.
- onLoadFinished: Called when a previously created loader has finished its load. Here, you can start using the cursor.
- onLoaderReset: Called when a previously created loader is being reset, thus making its data unavailable. It is being reset in order to create a new cursor to query different data. This is called when the last Cursor provided to onLoadFinished() above is about to be closed. We need to make sure we are no longer using it.

### The complete code on how to use Loader is as follows:

		public class MainActivity extends ListActivity implements LoaderCallbacks<Cursor>  {
		 
			
		    SimpleCursorAdapter mAdapter; 		
		    LoaderManager loadermanager;		
		    CursorLoader cursorLoader;
		    private static String TAG="CursorLoader";
		    
		    protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
		        loadermanager=getLoaderManager();
			
			String[] uiBindFrom = {  DatabaseHandler.UserTable.name};		
			int[] uiBindTo = {android.R.id.text1};
			
		        /*Empty adapter that is used to display the loaded data*/
			mAdapter = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_1, null, uiBindFrom, uiBindTo,0);  
		        setListAdapter(mAdapter);
		       
		/**
		* This initializes the loader and makes it active. If the loader 
		* specified by the ID already exists, the last created loader is reused.
		* If the loader specified by the ID does not exist, initLoader() triggers 
		* the LoaderManager.LoaderCallbacks method onCreateLoader(). 
		* This is where you implement the code to instantiate and return a new loader. 
		* Use restartLoader() instead of this, to discard the old data and restart the Loader.
		* Hence, here the given LoaderManager.LoaderCallbacks implementation are associated with the loader. 
		*/
		       loadermanager.initLoader(1, null, this);
		   }
			
		/**
		* This creates and return a new Loader (CursorLoader or custom Loader) for the given ID. 
		* This method returns the Loader that is created, but you don't need to capture a reference to it. 
		*/
		   public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
				
			String[] projection = { DatabaseHandler.UserTable.id, DatabaseHandler.UserTable.name };
				
		/**
		* This requires the URI of the Content Provider
		* projection is the list of columns of the database to return. Null will return all the columns
		* selection is the filter which declares which rows to return. Null will return all the rows for the given URI.
		* selectionArgs:  You may include ?s in the selection, which will be replaced
		* by the values from selectionArgs, in the order that they appear in the selection. 
		* The values will be bound as Strings.
		* sortOrder determines the order of rows. Passing null will use the default sort order, which may be unordered.
		* To back a ListView with a Cursor, the cursor must contain a column named _ID.
		*/
		 
			cursorLoader = new CursorLoader(this, DatabaseAccessUtility.CONTENT_URI, projection, null, null, null);
			return cursorLoader;
				
		   }
		 
		/**
		* Called when a previously created loader has finished its load. This assigns the new Cursor but does not close the previous one. 
		* This allows the system to keep track of the Cursor and manage it for us, optimizing where appropriate. This method is guaranteed
		* to be called prior to the release of the last data that was supplied for this loader. At this point you should remove all use of 
		* the old data (since it will be released soon), but should not
		* do your own release of the data since its loader owns it and will take care of that.
		* The framework would take of closing of old cursor once we return.
		*/
		 
		   public void onLoadFinished(Loader<Cursor> loader,Cursor cursor) {
			if(mAdapter!=null && cursor!=null)
				mAdapter.swapCursor(cursor); //swap the new cursor in.
			else
				Log.v(TAG,"OnLoadFinished: mAdapter is null");
		   }
		 
		/**
		* This method is triggered when the loader is being reset and the loader  data is no longer available. 
		* This is called when the last Cursor provided to onLoadFinished() above
		* is about to be closed. We need to make sure we are no longer using it.
		*/
		 
		   public void onLoaderReset(Loader<Cursor> arg0) {
			if(mAdapter!=null)
				mAdapter.swapCursor(null);
			else
				Log.v(TAG,"OnLoadFinished: mAdapter is null");
		   }
			
		}


The above code would just display the entries of DatabaseHandler.UserTable.name database table in the Listview.
