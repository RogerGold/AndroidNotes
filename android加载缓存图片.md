### Loading Large Bitmaps Efficiently
Images come in all shapes and sizes. In many cases they are larger than required for a typical application user interface (UI).

#### Read Bitmap Dimensions and Type
 Setting the inJustDecodeBounds property to true while decoding avoids memory allocation, returning null for the bitmap object but setting outWidth, outHeight and outMimeType. This technique allows you to read the dimensions and type of the image data prior to construction (and memory allocation) of the bitmap.
 
         BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.id.myimage, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        String imageType = options.outMimeType;
        
 To avoid java.lang.OutOfMemory exceptions, check the dimensions of a bitmap before decoding it, unless you absolutely trust the source to provide you with predictably sized image data that comfortably fits within the available memory.
 
#### Load a Scaled Down Version into Memory
Now that the image dimensions are known, they can be used to decide if the full image should be loaded into memory or if a subsampled version should be loaded instead. Here are some factors to consider:
- Estimated memory usage of loading the full image in memory.
- Amount of memory you are willing to commit to loading this image given any other memory requirements of your application.
- Dimensions of the target ImageView or UI component that the image is to be loaded into.
- Screen size and density of the current device.

Here’s a method to calculate a sample size value that is a power of two based on a target width and height:

        public static int calculateInSampleSize(
                    BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) >= reqHeight
                        && (halfWidth / inSampleSize) >= reqWidth) {
                    inSampleSize *= 2;
                }
            }

            return inSampleSize;
        }

To use this method, first decode with inJustDecodeBounds set to true, pass the options through and then decode again using the new inSampleSize value and inJustDecodeBounds set to false:

        public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                int reqWidth, int reqHeight) {

            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res, resId, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeResource(res, resId, options);
        }
        
This method makes it easy to load a bitmap of arbitrarily large size into an ImageView that displays a 100x100 pixel thumbnail, as shown in the following example code:

        mImageView.setImageBitmap(
            decodeSampledBitmapFromResource(getResources(), R.id.myimage, 100, 100));
         
#### Use an AsyncTask
Here’s an example of loading a large image into an ImageView using AsyncTask and decodeSampledBitmapFromResource():

        class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
            private final WeakReference<ImageView> imageViewReference;
            private int data = 0;

            public BitmapWorkerTask(ImageView imageView) {
                // Use a WeakReference to ensure the ImageView can be garbage collected
                imageViewReference = new WeakReference<ImageView>(imageView);
            }

            // Decode image in background.
            @Override
            protected Bitmap doInBackground(Integer... params) {
                data = params[0];
                return decodeSampledBitmapFromResource(getResources(), data, 100, 100));
            }

            // Once complete, see if ImageView is still around and set bitmap.
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (imageViewReference != null && bitmap != null) {
                    final ImageView imageView = imageViewReference.get();
                    if (imageView != null) {
                        imageView.setImageBitmap(bitmap);
                    }
                }
            }
        }

The WeakReference to the ImageView ensures that the AsyncTask does not prevent the ImageView and anything it references from being garbage collected. There’s no guarantee the ImageView is still around when the task finishes, so you must also check the reference in onPostExecute(). The ImageView may no longer exist, if for example, the user navigates away from the activity or if a configuration change happens before the task finishes.

To start loading the bitmap asynchronously, simply create a new task and execute it:

        public void loadBitmap(int resId, ImageView imageView) {
            BitmapWorkerTask task = new BitmapWorkerTask(imageView);
            task.execute(resId);
        }
        
#### Handle Concurrency
Common view components such as ListView and GridView introduce another issue when used in conjunction with the AsyncTask as demonstrated in the previous section. In order to be efficient with memory, these components recycle child views as the user scrolls. If each child view triggers an AsyncTask, there is no guarantee that when it completes, the associated view has not already been recycled for use in another child view. Furthermore, there is no guarantee that the order in which asynchronous tasks are started is the order that they complete.

Create a dedicated Drawable subclass to store a reference back to the worker task. In this case, a BitmapDrawable is used so that a placeholder image can be displayed in the ImageView while the task completes:

        static class AsyncDrawable extends BitmapDrawable {
            private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

            public AsyncDrawable(Resources res, Bitmap bitmap,
                    BitmapWorkerTask bitmapWorkerTask) {
                super(res, bitmap);
                bitmapWorkerTaskReference =
                    new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
            }

            public BitmapWorkerTask getBitmapWorkerTask() {
                return bitmapWorkerTaskReference.get();
            }
        }
        
Before executing the BitmapWorkerTask, you create an AsyncDrawable and bind it to the target ImageView:

        public void loadBitmap(int resId, ImageView imageView) {
            if (cancelPotentialWork(resId, imageView)) {
                final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
                final AsyncDrawable asyncDrawable =
                        new AsyncDrawable(getResources(), mPlaceHolderBitmap, task);
                imageView.setImageDrawable(asyncDrawable);
                task.execute(resId);
            }
        }
        
 The cancelPotentialWork method referenced in the code sample above checks if another running task is already associated with the ImageView. If so, it attempts to cancel the previous task by calling cancel(). In a small number of cases, the new task data matches the existing task and nothing further needs to happen. Here is the implementation of cancelPotentialWork:
 
         public static boolean cancelPotentialWork(int data, ImageView imageView) {
            final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

            if (bitmapWorkerTask != null) {
                final int bitmapData = bitmapWorkerTask.data;
                // If bitmapData is not yet set or it differs from the new data
                if (bitmapData == 0 || bitmapData != data) {
                    // Cancel previous task
                    bitmapWorkerTask.cancel(true);
                } else {
                    // The same work is already in progress
                    return false;
                }
            }
            // No task associated with the ImageView, or an existing task was cancelled
            return true;
        }

A helper method, getBitmapWorkerTask(), is used above to retrieve the task associated with a particular ImageView:

        private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
           if (imageView != null) {
               final Drawable drawable = imageView.getDrawable();
               if (drawable instanceof AsyncDrawable) {
                   final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                   return asyncDrawable.getBitmapWorkerTask();
               }
            }
            return null;
        }
        
The last step is updating onPostExecute() in BitmapWorkerTask so that it checks if the task is cancelled and if the current task matches the one associated with the ImageView:

        class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
            ...

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (isCancelled()) {
                    bitmap = null;
                }

                if (imageViewReference != null && bitmap != null) {
                    final ImageView imageView = imageViewReference.get();
                    final BitmapWorkerTask bitmapWorkerTask =
                            getBitmapWorkerTask(imageView);
                    if (this == bitmapWorkerTask && imageView != null) {
                        imageView.setImageBitmap(bitmap);
                    }
                }
            }
        }
        
This implementation is now suitable for use in ListView and GridView components as well as any other components that recycle their child views. 

#### Caching Bitmaps
Loading a single bitmap into your user interface (UI) is straightforward, however things get more complicated if you need to load a larger set of images at once. A memory and disk cache can often help here, allowing components to quickly reload processed images.

#### use LruCache
In order to choose a suitable size for a LruCache, a number of factors should be taken into consideration, for example:
- How memory intensive is the rest of your activity and/or application?
- How many images will be on-screen at once? How many need to be available ready to come on-screen?
- What is the screen size and density of the device? An extra high density screen (xhdpi) device like Galaxy Nexus will need a larger cache to hold the same number of images in memory compared to a device like Nexus S (hdpi).
- What dimensions and configuration are the bitmaps and therefore how much memory will each take up?
- How frequently will the images be accessed? Will some be accessed more frequently than others? If so, perhaps you may want to keep certain items always in memory or even have multiple LruCache objects for different groups of bitmaps.
- Can you balance quality against quantity? Sometimes it can be more useful to store a larger number of lower quality bitmaps, potentially loading a higher quality version in another background task.

There is no specific size or formula that suits all applications, it's up to you to analyze your usage and come up with a suitable solution. A cache that is too small causes additional overhead with no benefit, a cache that is too large can once again cause java.lang.OutOfMemory exceptions and leave the rest of your app little memory to work with.

Here’s an example of setting up a LruCache for bitmaps:

   private LruCache<String, Bitmap> mMemoryCache;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
       ...
       // Get max available VM memory, exceeding this amount will throw an
       // OutOfMemory exception. Stored in kilobytes as LruCache takes an
       // int in its constructor.
       final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

       // Use 1/8th of the available memory for this memory cache.
       final int cacheSize = maxMemory / 8;

       mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
           @Override
           protected int sizeOf(String key, Bitmap bitmap) {
               // The cache size will be measured in kilobytes rather than
               // number of items.
               return bitmap.getByteCount() / 1024;
           }
       };
       ...
   }

   public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
       if (getBitmapFromMemCache(key) == null) {
           mMemoryCache.put(key, bitmap);
       }
   }

   public Bitmap getBitmapFromMemCache(String key) {
       return mMemoryCache.get(key);
   }

When loading a bitmap into an ImageView, the LruCache is checked first. If an entry is found, it is used immediately to update the ImageView, otherwise a background thread is spawned to process the image:

   public void loadBitmap(int resId, ImageView imageView) {
       final String imageKey = String.valueOf(resId);

       final Bitmap bitmap = getBitmapFromMemCache(imageKey);
       if (bitmap != null) {
           mImageView.setImageBitmap(bitmap);
       } else {
           mImageView.setImageResource(R.drawable.image_placeholder);
           BitmapWorkerTask task = new BitmapWorkerTask(mImageView);
           task.execute(resId);
       }
   }
   
The BitmapWorkerTask also needs to be updated to add entries to the memory cache:

   class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
       ...
       // Decode image in background.
       @Override
       protected Bitmap doInBackground(Integer... params) {
           final Bitmap bitmap = decodeSampledBitmapFromResource(
                   getResources(), params[0], 100, 100));
           addBitmapToMemoryCache(String.valueOf(params[0]), bitmap);
           return bitmap;
       }
       ...
   }
   
#### Use a Disk Cache 
A memory cache is useful in speeding up access to recently viewed bitmaps, however you cannot rely on images being available in this cache. Components like GridView with larger datasets can easily fill up a memory cache. Your application could be interrupted by another task like a phone call, and while in the background it might be killed and the memory cache destroyed. Once the user resumes, your application has to process each image again.

A disk cache can be used in these cases to persist processed bitmaps and help decrease loading times where images are no longer available in a memory cache. Of course, fetching images from disk is slower than loading from memory and should be done in a background thread, as disk read times can be unpredictable.

The sample code of this class uses a DiskLruCache implementation that is pulled from the Android source. Here’s updated example code that adds a disk cache in addition to the existing memory cache:

    private DiskLruCache mDiskLruCache;
    private final Object mDiskCacheLock = new Object();
    private boolean mDiskCacheStarting = true;
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB
    private static final String DISK_CACHE_SUBDIR = "thumbnails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...
        // Initialize memory cache
        ...
        // Initialize disk cache on background thread
        File cacheDir = getDiskCacheDir(this, DISK_CACHE_SUBDIR);
        new InitDiskCacheTask().execute(cacheDir);
        ...
    }

    class InitDiskCacheTask extends AsyncTask<File, Void, Void> {
        @Override
        protected Void doInBackground(File... params) {
            synchronized (mDiskCacheLock) {
                File cacheDir = params[0];
                mDiskLruCache = DiskLruCache.open(cacheDir, DISK_CACHE_SIZE);
                mDiskCacheStarting = false; // Finished initialization
                mDiskCacheLock.notifyAll(); // Wake any waiting threads
            }
            return null;
        }
    }

    class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
        ...
        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Integer... params) {
            final String imageKey = String.valueOf(params[0]);

            // Check disk cache in background thread
            Bitmap bitmap = getBitmapFromDiskCache(imageKey);

            if (bitmap == null) { // Not found in disk cache
                // Process as normal
                final Bitmap bitmap = decodeSampledBitmapFromResource(
                        getResources(), params[0], 100, 100));
            }

            // Add final bitmap to caches
            addBitmapToCache(imageKey, bitmap);

            return bitmap;
        }
        ...
    }

    public void addBitmapToCache(String key, Bitmap bitmap) {
        // Add to memory cache as before
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }

        // Also add to disk cache
        synchronized (mDiskCacheLock) {
            if (mDiskLruCache != null && mDiskLruCache.get(key) == null) {
                mDiskLruCache.put(key, bitmap);
            }
        }
    }

    public Bitmap getBitmapFromDiskCache(String key) {
        synchronized (mDiskCacheLock) {
            // Wait while disk cache is started from background thread
            while (mDiskCacheStarting) {
                try {
                    mDiskCacheLock.wait();
                } catch (InterruptedException e) {}
            }
            if (mDiskLruCache != null) {
                return mDiskLruCache.get(key);
            }
        }
        return null;
    }

    // Creates a unique subdirectory of the designated app cache directory. Tries to use external
    // but if not mounted, falls back on internal storage.
    public static File getDiskCacheDir(Context context, String uniqueName) {
        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir
        final String cachePath =
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                        !isExternalStorageRemovable() ? getExternalCacheDir(context).getPath() :
                                context.getCacheDir().getPath();

        return new File(cachePath + File.separator + uniqueName);
    }

Note: Even initializing the disk cache requires disk operations and therefore should not take place on the main thread. However, this does mean there's a chance the cache is accessed before initialization. To address this, in the above implementation, a lock object ensures that the app does not read from the disk cache until the cache has been initialized.

While the memory cache is checked in the UI thread, the disk cache is checked in the background thread. Disk operations should never take place on the UI thread. When image processing is complete, the final bitmap is added to both the memory and disk cache for future use.

#### Handle Configuration Changes
Runtime configuration changes, such as a screen orientation change, cause Android to destroy and restart the running activity with the new.

Luckily, you have a nice memory cache of bitmaps that you built in the Use a Memory Cache section. This cache can be passed through to the new activity instance using a Fragment which is preserved by calling setRetainInstance(true)). After the activity has been recreated, this retained Fragment is reattached and you gain access to the existing cache object, allowing images to be quickly fetched and re-populated into the ImageView objects.

Here’s an example of retaining a LruCache object across configuration changes using a Fragment:

   private LruCache<String, Bitmap> mMemoryCache;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
       ...
       RetainFragment retainFragment =
               RetainFragment.findOrCreateRetainFragment(getFragmentManager());
       mMemoryCache = retainFragment.mRetainedCache;
       if (mMemoryCache == null) {
           mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
               ... // Initialize cache here as usual
           }
           retainFragment.mRetainedCache = mMemoryCache;
       }
       ...
   }

   class RetainFragment extends Fragment {
       private static final String TAG = "RetainFragment";
       public LruCache<String, Bitmap> mRetainedCache;

       public RetainFragment() {}

       public static RetainFragment findOrCreateRetainFragment(FragmentManager fm) {
           RetainFragment fragment = (RetainFragment) fm.findFragmentByTag(TAG);
           if (fragment == null) {
               fragment = new RetainFragment();
               fm.beginTransaction().add(fragment, TAG).commit();
           }
           return fragment;
       }

       @Override
       public void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           setRetainInstance(true);
       }
   }
   
### Managing Bitmap Memory
#### Save a bitmap for later use
The following snippet demonstrates how an existing bitmap is stored for possible later use in the sample app. When an app is running on Android 3.0 or higher and a bitmap is evicted from the LruCache, a soft reference to the bitmap is placed in a HashSet, for possible reuse later with inBitmap:

  Set<SoftReference<Bitmap>> mReusableBitmaps;
  private LruCache<String, BitmapDrawable> mMemoryCache;

  // If you're running on Honeycomb or newer, create a
  // synchronized HashSet of references to reusable bitmaps.
  if (Utils.hasHoneycomb()) {
      mReusableBitmaps =
              Collections.synchronizedSet(new HashSet<SoftReference<Bitmap>>());
  }

  mMemoryCache = new LruCache<String, BitmapDrawable>(mCacheParams.memCacheSize) {

      // Notify the removed entry that is no longer being cached.
      @Override
      protected void entryRemoved(boolean evicted, String key,
              BitmapDrawable oldValue, BitmapDrawable newValue) {
          if (RecyclingBitmapDrawable.class.isInstance(oldValue)) {
              // The removed entry is a recycling drawable, so notify it
              // that it has been removed from the memory cache.
              ((RecyclingBitmapDrawable) oldValue).setIsCached(false);
          } else {
              // The removed entry is a standard BitmapDrawable.
              if (Utils.hasHoneycomb()) {
                  // We're running on Honeycomb or later, so add the bitmap
                  // to a SoftReference set for possible use with inBitmap later.
                  mReusableBitmaps.add
                          (new SoftReference<Bitmap>(oldValue.getBitmap()));
              }
          }
      }
  ....
  }

#### Use an existing bitmap
In the running app, decoder methods check to see if there is an existing bitmap they can use. For example:

  public static Bitmap decodeSampledBitmapFromFile(String filename,
          int reqWidth, int reqHeight, ImageCache cache) {

      final BitmapFactory.Options options = new BitmapFactory.Options();
      ...
      BitmapFactory.decodeFile(filename, options);
      ...

      // If we're running on Honeycomb or newer, try to use inBitmap.
      if (Utils.hasHoneycomb()) {
          addInBitmapOptions(options, cache);
      }
      ...
      return BitmapFactory.decodeFile(filename, options);
  }
  
 The next snippet shows the addInBitmapOptions() method that is called in the above snippet. It looks for an existing bitmap to set as the value for inBitmap. Note that this method only sets a value for inBitmap if it finds a suitable match (your code should never assume that a match will be found):
 
   private static void addInBitmapOptions(BitmapFactory.Options options,
          ImageCache cache) {
      // inBitmap only works with mutable bitmaps, so force the decoder to
      // return mutable bitmaps.
      options.inMutable = true;

      if (cache != null) {
          // Try to find a bitmap to use for inBitmap.
          Bitmap inBitmap = cache.getBitmapFromReusableSet(options);

          if (inBitmap != null) {
              // If a suitable bitmap has been found, set it as the value of
              // inBitmap.
              options.inBitmap = inBitmap;
          }
      }
  }

  // This method iterates through the reusable bitmaps, looking for one
  // to use for inBitmap:
  protected Bitmap getBitmapFromReusableSet(BitmapFactory.Options options) {
          Bitmap bitmap = null;

      if (mReusableBitmaps != null && !mReusableBitmaps.isEmpty()) {
          synchronized (mReusableBitmaps) {
              final Iterator<SoftReference<Bitmap>> iterator
                      = mReusableBitmaps.iterator();
              Bitmap item;

              while (iterator.hasNext()) {
                  item = iterator.next().get();

                  if (null != item && item.isMutable()) {
                      // Check to see it the item can be used for inBitmap.
                      if (canUseForInBitmap(item, options)) {
                          bitmap = item;

                          // Remove from reusable set so it can't be used again.
                          iterator.remove();
                          break;
                      }
                  } else {
                      // Remove from the set if the reference has been cleared.
                      iterator.remove();
                  }
              }
          }
      }
      return bitmap;
  }
  
Finally, this method determines whether a candidate bitmap satisfies the size criteria to be used for inBitmap:

  static boolean canUseForInBitmap(
          Bitmap candidate, BitmapFactory.Options targetOptions) {

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
          // From Android 4.4 (KitKat) onward we can re-use if the byte size of
          // the new bitmap is smaller than the reusable bitmap candidate
          // allocation byte count.
          int width = targetOptions.outWidth / targetOptions.inSampleSize;
          int height = targetOptions.outHeight / targetOptions.inSampleSize;
          int byteCount = width * height * getBytesPerPixel(candidate.getConfig());
          return byteCount <= candidate.getAllocationByteCount();
      }

      // On earlier versions, the dimensions must match exactly and the inSampleSize must be 1
      return candidate.getWidth() == targetOptions.outWidth
              && candidate.getHeight() == targetOptions.outHeight
              && targetOptions.inSampleSize == 1;
  }

  /**
   * A helper function to return the byte usage per pixel of a bitmap based on its configuration.
   */
  static int getBytesPerPixel(Config config) {
      if (config == Config.ARGB_8888) {
          return 4;
      } else if (config == Config.RGB_565) {
          return 2;
      } else if (config == Config.ARGB_4444) {
          return 2;
      } else if (config == Config.ALPHA_8) {
          return 1;
      }
      return 1;
  }
  
### Displaying Bitmaps in Your UI
This lesson brings together everything from previous lessons, showing you how to load multiple bitmaps into ViewPager and GridView components using a background thread and bitmap cache, while dealing with concurrency and configuration changes.

#### Load Bitmaps into a ViewPager Implementation
