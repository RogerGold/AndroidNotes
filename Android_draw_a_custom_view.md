# Android: draw a custom view
many customers requesting some custom views that no other application has, that makes application unique and different.
### View lifecycle

![customsViews](https://github.com/RogerGold/media/blob/master/customsViews.png)

### Constructor
![view_constructors](https://github.com/RogerGold/media/blob/master/view_constructors.PNG)

Every view stars it’s life from a Constructor. And what it gives us, is a great opportunity to prepare it for initial drawing, 
making various calculation, setting default values or whatever we need.But to make our view easy to use and setup, 
there are useful AttributeSet interface. It’s easy to implement and definitely worth to spend quite some time on it, 
because it will help you (and your team) to setup your view with some static parameters on future new and new screens.
First, create a new file and call it attrs.xml. In that file could be all the attributes for different custom views.
As you can see in this example we have a view called PageIndicatorView and single attribute piv_count.

![Attributes](https://github.com/RogerGold/media/blob/master/Attributes.PNG)

Secondary in your View constructor you need to obtain attributes and use it as shown below.

    public PageIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.PageIndicatorView);
        int count = typedArray.getInt(R.styleable.PageIndicatorView_piv_count,0);
        typedArray.recycle();
    }

Note:
- While creating custom attributes make a simple prefix to avoid name conflicts between other views with similar attribute names. 
  Mostly it is abbreviation of view name, just like we have piv_.
- if you are using Android Studio, Lint will advise you to call recycle() method as long as you are done with your attributes.
  The reason is just to get rid of inefficiently bound data that’s not gonna be used again.
  
### onAttachedToWindow
After parent view calls addView(View) that view will be attached to a window. 
At this stage our view will know the other views it is surrounded by. 
If your view is working with user’s other views located in same layout.xml it is good place to find them by id (which you can set by attributes) 
and save as global (if needed) reference.

### onMeasure
Means that our custom view is on stage to find out it’s own size.
It’s very important method, as for most cases you will need your view to have specific size to fit in your layout.
While overriding this method, what you need to do this is to set 

    setMeasuredDimension(int width, int height).
    
While setting size of a custom view you should handle case, that view could have specific size that user will set in
layout.xml or programmatically. To calculate it properly it is needed to do several things.
- Calculate your view content desired size (width and height).
- Get your view MeasureSpec (width and height) for size and mode.

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        }

- Check MeasureSpec mode that user set and adjust size of your view (for width and height).

    int width;
    if (widthMode == MeasureSpec.EXACTLY) {
      width = widthSize;
    } else if (widthMode == MeasureSpec.AT_MOST) {
      width = Math.min(desiredWidth, widthSize);
    } else {
      width = desiredWidth;
    }
    
Note: 
take a look at MeasureSpec values:
- MeasureSpec.EXACTLY means that user hardcoded size value, so
 regardless your view size, you should set specified width or height.
- MeasureSpec.AT_MOST used for making your view match parent size,
 so it should be as big as it could.
- MeasureSpec.UNSPECIFIED is actually a wrap size of view. So with 
 this parameter you can use desired size that you calculated above.
 
Before setting final values to setMeasuredDimension just in case check if those values is not negative. 
That will avoid issues in layout preview.

### onLayout
This method is assigning a size and position to each of its children. 
Because of that we are looking into a flat custom view (that extends a simple View) that
does not have any children there is no reason to override this method.

### onDraw
That’s where the magic happens. Having both Canvas and Paint objects will allow you draw anything you need.
A Canvas instance comes as onDraw parameter, it basicly respond for drawing a different shapes,
while Paint object defines that color that shape will get. Simply, Canvas respond for drawing an object, 
while Paint for styling it. And it used mostly everywhere whether it is going to be a line, circle or rectangle.

While making a custom view, always keep in mind that onDraw calls lots of time, like really alot.
While having some changes, scrolling, swiping your will be redrawn. So that’s why even Android Studio 
recommend to avoid object allocation during onDraw operation, instead to create it once and reuse further on.

![noteWhenDraw](https://github.com/RogerGold/media/blob/master/noteWhenDraw.PNG)

Note:
- While performing drawing always keep in mind to reuse objects instead of creating new ones. 
  Don’t rely on your IDE that will highlight a potential issue, but do it yourself because IDE could not
  see it if you create objects inside methods called from onDraw.
- Don’t hard code your view size while drawing. Handle case that other developers could have same 
  view but in different size, so draw your view depending on what size it has.
  
### View Update
From a view lifecycle diagram you may notices that there are two methods that leads view to redraw itself. 
invalidate() and requestLayout() methods will help you to make an interactive custom view, 
that may change its look on runtime. But why there are two of them?

- invalidate() method used to simple redrawing view. While your view for example updates its text, 
  color or touch interactivity. It means that view will only call onDraw() method once more to update its state.
- requestLayout() method, as you can see will produce view update through its lifecycle just from onMeasure() method. 
  And what it means that you will need it while after your view updates, it changed it’s size and you need to measure 
  it once again to draw it depending on new size.
  
![invalidate](https://github.com/RogerGold/media/blob/master/invalidate.PNG)  

### Animation
Animations in custom view is frame by frame process. It means that if you for example want to make a circle 
radius animate from smaller to bigger you will need to increase it one by one and after each step call invalidate() to draw it.

Your best friend in custom view animations is ValueAnimator. This class will help you to animate any value from start to 
the end with even Interpolator support (if you need).

    ValueAnimator animator = ValueAnimator.ofInt(0, 100);
    animator.setDuration(1000);
    animator.setInterpolator(new DecelerateInterpolator());
    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      public void onAnimationUpdate(ValueAnimator animation) {
        int newRadius = (int) animation.getAnimatedValue();
      }
    });

Note: 
Don’t forget to call invalidate() every time new animated values comes out.
