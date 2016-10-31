### 1. get bitmap from resource
Bitmap barcodeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.name_image);

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
