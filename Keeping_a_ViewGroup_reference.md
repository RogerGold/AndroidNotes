# Keeping a ViewGroup reference

If you plan on keeping a reference to any ViewGroup (LinearLayout, FrameLayout, RelativeLayout, etc.), and you don’t want to use any methods specific to this particular type of Layout, keep it as a ViewGroup object.

Along the way you will probably make many changes to your layout xml, including changing the actual type of a ViewGroup (e.g. you decide that the FrameLayout is not enough for you anymore and you actually need a RelativeLayout). Keeping a Layout object as a ViewGroup (where possible) will decrease issues with ViewGroup casting, and will make it easier to propagate changes in your layout xml to any classes that are using it.

Example:
If you need a reference to a FrameLayout but you don’t need to call any of those methods (FrameLayout-specific methods for API 16):

- generateLayoutParams(AttributeSet attrs)
- getConsiderGoneChildrenWhenMeasuring()
- getMeasureAllChildren()
- setForegroundGravity(int foregroundGravity)
- setMeasureAllChildren(boolean measureAll)
- shouldDelayChildPressedState()


Just keep it as a ViewGroup object.

If your code looks like this:

 ![FrameLayout](https://github.com/RogerGold/media/blob/master/FrameLayout.png)

and you decide to change your FrameLayout to RelativeLayout:

 ![layout](https://github.com/RogerGold/media/blob/master/layout.png)

and you didn’t remember to change the exampleLayout field type, that’s what you’ll see (more-less) when you run your app:

![error](https://github.com/RogerGold/media/blob/master/error.png)

A simple trick of using a ViewGroup instead of a FrameLayout for your exampleLayout field, would make you not even have to worry about this kind of stuff in your code.

![ViewGroup](https://github.com/RogerGold/media/blob/master/ViewGroup.png)
