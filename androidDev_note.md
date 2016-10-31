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
