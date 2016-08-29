# VectorDrawable pathData commands in Android

### What is a VectorDrawable
A VectorDrawable is an XML representation of a Vector. Unlike popular image formats like Bitmap, JPEG, GIF and PNG, Vectors do not lose quality as they are scaled up or down. This makes bundling of images with different densities unnecessary, hence saving you a lot of APK bloat. In effect, VectorDrawables contains path commands on (how to draw lines and arcs) and just like Path commands when working with Canvas, drawing and rendering VectorDrawables is time and memory consuming process which is why VectorDrawable’s are best used for simple flat graphics.

### Understanding pathData commands
#### The basics
Basic path commands are comprised of alphabet followed by one or more numbers. The numbers are often comma separated but don’t have to be. E.g.

		M100,100 L300,100 L200,300 z
		//or
		M 100 100 L 300 100 L 200 300 z
		//or 
		M1100,100L300,100L200,300z

***The alphabet can be upper or lowercase. Uppercase means absolute position, lowercase means relative position.***

### Commands
#### M or m (X,Y)+
moveto: Move cursor to position, uppercase is absolute, lowercase is relative
moveto commands are followed by X,Y coordinates. There can be more than one set of coordinates following an M command, these are treated as implicit lineto commands.
#### Z or z
closepath: Draws a line from the current position of the cursor to the start position of the path. Does not have any parameters.
#### L or l (X,Y)+
lineto: Draws a line from the current position to the position specified by X,Y. Uppercase means absolute coordinates, lowercase means relative coordinates. You can have more than one set of coordinates following a lineto command. If you want specify more than one set of coordinates, it means that you’re creating a polyline (shape consisting of multiple string lines).
#### H or h (X)+
Horizontal lineto draws a horizontal line from the current cursor position to the position specified by X. If there are multiple X coordinates following the command, this is treated as a polyline. The Y coordinate remains unchanged. Uppercase H is absolute coordinates, lowercase h is relative coordinates.
#### V or v (Y)+
Vertical lineto draws a vertical line from the current cursor position to the position specified by Y. If there are multiple Y coordinates following the command, this is treated as a polyline. The X coordinate remains unchanged. Uppercase V is absolute coordinates, lowercase v is relative coordinates.
#### Example
With this much in mind, lets interpret the command we had above:

		M100,100 L300,100 L200,300 z
M100,100: Move cursor to absolute coordinates X=100 Y=100px.

L300,100: Draw a line to X=300 Y=100 (starting position was 100,100).

L200,300: Draw a line to X=200 Y=300 (starting position was 300,100).

z: Close path, straight line from current position to 100,100. When you close the path is when the shape is filled with the fill colour specified. You can leave this out if you shape doesn’t need to close, like in a check mark or a cross.

If we sketch it out, you’ll notice that the shape is an upside down triangle!

If we put this inside a simple VectorDrawable XML we can see the result:

		<vector xmlns:android="http://schemas.android.com/apk/res/android"
		    <!-- intrinsic size of the drawable -->
		        android:width="400px"
		        android:height="400px"
		    <!-- size of the virtual canvas -->
		        android:viewportWidth="400.0"
		        android:viewportHeight="400.0">
		    <path
		        android:fillColor="#0000FF"
		        android:strokeColor="#FFFFFF"
		        android:strokeWidth="4"
		        android:pathData="M100,100 L300,100 L200,300 z"/>
		</vector>


![upside_down_triangle](https://github.com/RogerGold/media/blob/master/upside_down_triangle.png)

### What can you do with this basic info?
Starting with just this much, you can do a lot. such as created an animated tick drawable using just this much information.

![animated_tick_drawable](https://github.com/RogerGold/media/blob/master/animated_tick_drawable.gif)

The final checkmark command is:

		M6,11 l3.5,4 l8,-7
Here my canvas size is 24dp x 24dp so I initially position my cursor at 6, 11 which is the starting point of the check, I move relative to 6, 11 down by 3.5, 4 and then up relative to the new position 8, -7 to complete our checkmark. Since I initially do not want my checkmark to appear, I set all the lineto commands to 0,0 then in my first animation, I animate the first line from a relative position 0,0 to a relative position of 3.5, 4. Note, these coordinates would not work as well if I used absolute positions.

		Starting point:   M6,11 l0,0   l0,0
		Animation Step 1: M6,11 l3.5,4 l0,0
		Animation Step 2: M6,11 l3.5,4 l8,-7 //complete!

drawable/check_mark.xml
		
		<vector xmlns:android="http://schemas.android.com/apk/res/android"
		    android:width="24dp"
		    android:height="24dp"
		    android:viewportHeight="24.0"
		    android:viewportWidth="24.0">
		    <group android:name="background">
		        <path
		            android:name="circle"
		            android:fillColor="@color/colorPrimary"
		            android:pathData="M12,12m-10,0a10,10 0,1 1,20 0a10,10 0,1 1,-20 0" />
		    </group>
		    <group android:name="check">
		        <path
		            android:name="tick"
		            android:pathData="M6,11 l0,0 l0,0"
		            android:strokeColor="@color/colorAccent"
		            android:strokeWidth="1" />
		    </group>
		
		</vector>

drawable-v21/animated_check.xml


		<?xml version="1.0" encoding="utf-8"?>
		<animated-vector xmlns:android="http://schemas.android.com/apk/res/android"
		    android:drawable="@drawable/check_mark">
		    <target
		        android:name="tick"
		        android:animation="@anim/check_animation" />
		</animated-vector>


anim/check_animation.xml

		<?xml version="1.0" encoding="utf-8"?>
		<set xmlns:android="http://schemas.android.com/apk/res/android"
		    android:interpolator="@android:anim/accelerate_interpolator"
		    android:ordering="sequentially"
		    android:shareInterpolator="false">
		    <!-- Step 1 -->
		    <objectAnimator
		        android:duration="@android:integer/config_shortAnimTime"
		        android:propertyName="pathData"
		        android:valueFrom="M6,11 l0,0 l0,0"
		        android:valueTo="M6,11 l3.5,4 l0,0"
		        android:valueType="pathType" />
		    <!-- Step 2 -->
		    <objectAnimator
		        android:duration="@android:integer/config_shortAnimTime"
		        android:propertyName="pathData"
		        android:valueFrom="M6,11 l3.5,4 l0,0"
		        android:valueTo="M6,11 l3.5,4 l8,-7"
		        android:valueType="pathType" />
		</set>

### Usage

XML

		<ImageView
		    android:id="@+id/imageView"
		    android:layout_width="100dp"
		    android:layout_height="100dp"
		    android:visibility="visible"
		    app:srcCompat="@drawable/animated_tick" />

Java

		mImgCheck = (ImageView) findViewById(R.id.imageView);
		((Animatable) mImgCheck.getDrawable()).start();

### More commands
[W3.orgs documentation on Paths](https://www.w3.org/TR/SVG/paths.html)