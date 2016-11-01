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
