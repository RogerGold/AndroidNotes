# android开发杂记

#### 1.get bitmap from resource
Bitmap barcodeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.name_image);
#### 2.PopupWindow
点击空白处的时候让PopupWindow消失：
  - 原本以为如果你setOutsideTouchable(true)则点击PopupWindow之外的地方PopupWindow会消失，其实这玩意儿好像一点用都没有。
  - 要让点击PopupWindow之外的地方PopupWindow消失你需要调用setBackgroundDrawable(new BitmapDrawable());
  - 设置背景，为了不影响样式，这个背景是空的。背景不为空但是完全透明。如此设置还能让PopupWindow在点击back的时候消失:
        setBackgroundDrawable(new ColorDrawable(0x00000000));
        
在android中一个模态对话框应该是这样的：阻止屏幕上的其他View事件，且点击PopupWindow外面不会消失，但是能响应back事件（点击back消失）：

        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentview = inflater.inflate(R.layout.popup_process, null);
        contentview.setFocusable(true); // 这个很重要
        contentview.setFocusableInTouchMode(true);
        final PopupWindow popupWindow = new PopupWindow(contentview,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        contentview.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    popupWindow.dismiss();

                    return true;
                }
                return false;
            }
        });
        popupWindow.showAtLocation(view,  Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
        
 必须设置宽和高，否则不显示任何东西.
 
 为了降低出现崩溃的概率，最好检查该 PopupWindow 所依赖的content是否还在，防止重复弹出多个window，可以设置一个flag，来标志是否已经弹出。
 
         if(!activity.isFinishing() || !activity.isDestroyed() && flag == false){
           //弹出PopupWindow
           ...
           flag = true;//标记window已经弹出
         }
         
### 2. android:layout_weight="1" android:layout_width="0dp"
- Setting the width to zero (0dp) improves layout performance because using "wrap_content" as
the width requires the system to calculate a width that is ultimately irrelevant because the weight 
value requires another width calculation to fill the remaining space.

- The weight value is a number that specifies the amount of remaining space each view should consume, 
relative to the amount consumed by sibling views. This works kind of like the amount of ingredients in a drink recipe: 
"2 parts soda, 1 part syrup" means two-thirds of the drink is soda. For example, if you give one view a weight of 2 
and another one a weight of 1, the sum is 3, so the first view fills 2/3 of the remaining space and the second view fills the rest. 
If you add a third view and give it a weight of 1, then the first view (with weight of 2) now gets 1/2 the remaining space, 
while the remaining two each get 1/4.
- The default weight for all views is 0, so if you specify any weight value greater than 0 to only one view,
then that view fills whatever space remains after all views are given the space they require

### 3.Supporting Different Languages
Once you’ve decided on the languages you will support, create the resource subdirectories and string resource files. For example:

    MyProject/
        res/
           values/
               strings.xml
           values-es/
               strings.xml
           values-fr/
               strings.xml
               
Add the string values for each locale into the appropriate file.
At runtime, the Android system uses the appropriate set of string resources based on the locale currently set for the user's device.

#### Use the String Resources
You can reference your string resources in your source code and other XML files using the resource name defined by the <string> element's name attribute.
In your source code, you can refer to a string resource with the syntax R.string.<string_name>. There are a variety of methods that accept a string resource this way.

In code:

             // Get a string resource from your app's Resources
            String hello = getResources().getString(R.string.hello_world);


            // Or supply a string resource to a method that requires a string
            TextView textView = new TextView(this);
            textView.setText(R.string.hello_world);

In XML: 

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/hello_world" />


### 4.Supporting Different Screens

        MyProject/
            res/
                layout/              # default (portrait)
                    main.xml
                layout-land/         # landscape
                    main.xml
                layout-large/        # large (portrait)
                    main.xml
                layout-large-land/   # large landscape
                    main.xml
                    
### 5. Create Different Bitmaps    
   You should always provide bitmap resources that are properly scaled to each of the generalized density buckets: low, medium, high and extra-high density. This helps you achieve good graphical quality and performance on all screen densities.

To generate these images, you should start with your raw resource in vector format and generate the images for each density using the following size scale:

        xhdpi: 2.0
        hdpi: 1.5
        mdpi: 1.0 (baseline)
        ldpi: 0.75
This means that if you generate a 200x200 image for xhdpi devices, you should generate the same resource in 150x150 for hdpi, 100x100 for mdpi, and 75x75 for ldpi devices.

Then, place the files in the appropriate drawable resource directory:

        MyProject/
            res/
                drawable-xhdpi/
                    awesomeimage.png
                drawable-hdpi/
                    awesomeimage.png
                drawable-mdpi/
                    awesomeimage.png
                drawable-ldpi/
                    awesomeimage.png
            
Any time you reference @drawable/awesomeimage, the system selects the appropriate bitmap based on the screen's density.

### 转dip为px

    private int dip2px(Context context, float dipValue){
      final float scale = context.getResource().getDisplayMetrics().density;
      return (int)(dipValue * scale = 0.5f);
    }
 
 
 ###  The Case of the Invisible Activity
 
You have two ways of setting up an invisible activity, both involving using a
particular android:theme value on the &lt; activity &gt; element.

The most efficient option is to use Theme.NoDisplay. With this value, Android does
nothing in terms of setting up a UI for you. However, the key limitation is that all
the work the activity is going to do needs to be completed in onCreate(), and in
there you need to call finish() to trigger the activity to be destroyed. Most of the
time, this will work just fine

Occasionally, you need an invisible activity that has to hang around for a few
seconds, perhaps waiting on some callback result, before it can be destroyed. Using
Theme.NoDisplay will still work

On Android 6.0 and higher, using Theme.NoDisplay without calling finish() in onCreate() (or,
technically, before onResume()) will crash your app.

The workaround is to use Theme.Translucent.NoTitleBar. This actually does
allocate a UI for you, but sets it up to have a transparent background and no action
bar. The user may still perceive that the activity is around — for example, it will
show up in the overview screen (a.k.a., recent-tasks list). Also, since the activity is
“really there”, the user may not be able to interact with whatever the user can see,
such as the underlying home screen. But, if the activity can finish() itself quickly,
and is interacting with the user in the meantime (e.g., displaying some system
dialog), you may be able to get away with this approach.
