# What's the difference between Gravity and layout_gravity, Padding and Margin, fill_parent and wrap_content?

### Gravity and layout_gravity on Android
android:gravity sets the gravity of the content of the View its used on.

android:layout_gravity sets the gravity of the View or Layout in its parent.


![gravity_layout_gravity](https://github.com/RogerGold/media/blob/master/gravity_layout_gravity.png)

Don't use gravity/layout_gravity with a RelativeLayout. Use them for Views in LinearLayouts and FrameLayouts.

If I hadn't made the width and height of the TextViews larger than the text, then setting the gravity would have had no effect. So if you're using wrap_content on the TextView then gravity won't do anything. In the same way, if the LinearLayout had been set to wrap_content, then the layout_gravity would have had no effect on the TextViews.

The layout_gravity=center looks the same as layout_gravity=center_horizontal here because they are in a vertical linear layout. You can't center vertically in this case, so layout_gravity=center only centers horizontally.

So remember, layout_gravity arranges a view in its layout. Gravity arranges the content inside the view. Its easy to forget which is which. 

### Padding and Margin

![Padding_and_Margin](https://github.com/RogerGold/media/blob/master/Padding_and_Margin.jpg)

Padding is the space inside the border, between the border and the actual view's content. Note that padding goes completely around the content: there is padding on the top, bottom, right and left sides (which can be independent).

Margins are the spaces outside the border, between the border and the other elements next to this view. In the image, the margin is the grey area outside the entire object. Note that, like the padding, the margin goes completely around the content: there are margins on the top, bottom, right, and left sides.

### fill_parent and wrap_content

fill_parent (deprecated and renamed MATCH_PARENT in API Level 8 and higher)

Setting the layout of a widget to fill_parent will force it to expand to take up as much space as is available within the layout element it's been placed in. It's roughly equivalent of setting the dockstyle of a Windows Form Control to Fill.

Setting a top level layout or control to fill_parent will force it to take up the whole screen.

wrap_content

Setting a View's size to wrap_content will force it to expand only far enough to contain the values (or child controls) it contains. For controls -- like text boxes (TextView) or images (ImageView) -- this will wrap the text or image being shown. For layout elements it will resize the layout to fit the controls / layouts added as its children.

