# why application has to do all its work in 16 milliseconds or less for every single screen refresh.

### Human eyes and 16 ms per refresh
The human brain receives and processes visual information continuously. This allows us some cool tricks. For example, when still images are displayed to follow each other fast enough, we perceive them as motion.

- At  about 10–12 pages per second, there is clearly motion, but you also retain awareness of individual pages.
- 24 pages per second allows the human eye to see fluid motion, thanks to technologies such as motion blurring, which help you perceive motion when you should just see each individual frame of the animation. 24 images per second is a sweet spot for the film industry, since it’s fast enough to show motion, but cheap enough to produce films on a budget.
- 30 pages per second is sufficient, but not life-like. It is enough for movies, but without fancy cinematic effects, it’s not convincing.
- 60 pages per second is ideal, as most people see this as high-quality, smooth motion.

This measurement of “pages per second” is called “frame rate” or “frames per second” in the computing world. And while it’s important to understand how frame rate leads to smooth motion, it’s not the whole story. The human eye is very discerning when it comes to motion inconsistencies. For example, if your app is running on average at 60 frames per second, and just one frame takes much longer than the preceding ones to display, users will notice a break in smoothness that is generally called “hitching”, “lag” ,“stutter”, or “jank.”

### Computer hardware and 16 ms per frame
Most modern mobile devices refresh their display at 60 frames per second. This number is dictated by the device’s hardware, which defines how many times per second the screen can update. (And of course, the hardware is built to successfully play make-believe with the human eye.)

To match the hardware screen’s 60 updates per second, the system software attempts to redraw your current app activity every 16 milliseconds 
(1000 ms / 60 frames = 16.666 ms/frame). And therefore, your app has 16 milliseconds, to update each frame.

And that’s 16 milliseconds TOTAL. The system also has to take time to draw, respond to intents, and handle input events, just to name a few. The big picture is that your app is sharing these 16 milliseconds with a lot of other subsystems on Android, so don’t plan on owning all of it.
If your app takes too long and does not finish its calculations in 16 milliseconds, you get what is called a “dropped frame”, and, yes, you already know it. JANK!!

### You, your app, and 16 ms per frame
As an app developer, you must keep your app consistently at 60 frames per second, and 16 milliseconds per frame, throughout your user’s experience, to avoid jank.

### From View To Pixel
Basic thing to understand about our UI is — how is it created?
How does the high level object we write, such as new Button(), on java code, or xml become pixels on screen?
It basically goes like this:

![view to pixel](https://github.com/RogerGold/AndroidNotes/blob/master/view_to_pixel.png)

- the CPU takes the high level object and turns it into a display list — a set of commands for drawing it.
- GPU (Graphics Processing Unit) is a hardware piece which masters at Rasterization.
Rasterization- taking high level object (button, string, etc…) and turning it into pixels in a texture or on the screen.
- The commands created on the CPU are uploaded to the GPU with OpenGL-ES api, in the format the latter can use for rasterizing.
- Once uploaded, the display list object is cached. That way, should we want to draw the display list again, there’s no need in creating it, but just redrawing the existing one- which is much cheaper.
### This process, unsurprisingly, might be time consuming.
In particular:

- Creating the display list for an object
- Uploading to the GPU is expensive.

Therefore, we should strive to reduce the number of time these actions are performed.

An example for optimization the system does for us, is in handling resources provided by our theme, like bitmaps and drawables. The system groups them into one texture and uploads it to the GPU which, as noted above, caches them.

That way, it is really cheap for us to use those, as they are being drawn from cache without the need to be converted or upload to GPU.
## Rendering — Phase By Phase
Creating a great UI experience is a main goal for us as developers. In order to do so, first step is to understand how does the system work, so we can better coordinate with it, benefit from its advantages, and avoid its flaws.

![phase_by](https://github.com/RogerGold/AndroidNotes/blob/master/phase_by_phase.png)

Before explaining the phases, in order to see it in action (and profile our app :) ), we can use the GPU Profiling Tool:
### Launching GPU Profiling Tool
Settings → Developer Options → Profile GPU Rendering → On Screen as bars.

![Profile_GPU_Rendering](https://github.com/RogerGold/AndroidNotes/blob/master/Profile_GPU_Rendering%20.png)

Outputs this to the screen :

![Profile_GPU_Rendering_output.](https://github.com/RogerGold/AndroidNotes/blob/master/Profile_GPU_Rendering_output.png)

- Each bar represents a frame, and its height indicate render time.
- The horizontal green line represents the 16 ms benchmark.
- Crossing it means skipping frames! Which means hurting our users’ experience. (which is the last thing we want!)
- A bar which crossed it will appear bolder to better detect its details.
- Each color on the bar represents a different phase on the rendering process, as shown above, and will be explained below.
- The higher the color — the more time spent on the phase.

### Overviewing The Rendering Phases
1.Vsync / Misc Time

- Misc = (vsync intended timestamp) — (vsync actual received timestamp)
- It represents work on UI thread between 2 frames.
- If it is not equal 0, it means that there was work on the UI thread that caused delay in vsync, and therefore skipped frames.
- Then, we’ll see the famous Choreographer log : “Missed vsync by XX ms skipping XX frames”

High? Check for work that can be moved off UI Thread

2.Input Handling

- Execution of our application code inside input event callbacks. (onClick()s, onTouch()s, etc…)

High? Check the work done on those callbacks, to optimize / offloaded to another thread.

3.Animation

- Evaluate all running animators, so they know what is the view’s state on current frame.
Often we use: object animator, view property animator and transitions

High? (2 ms and up)

- Check if unintended work happens as a result of updating view properties.
- Check your custom animators

4.Measure / Layout

- Executing measure (understand size of views) and layout (understand position of views) callbacks for all needed views.
- More on that later :)

High? In few words:

- Try to flatten view hierarchy or reduce its complexity
- Beware of double layout taxation and use view object to reduce it.

5.Draw

- Create or update all display lists, for all views that needs an update.
- Remember — we don’t actually see anything on screen yet. OnDraw() prepares the canvas, meaning collects the commands which will compose the display list that will be displayed on screen only later.

High?

- complex onDraw() logic
- many views invalidated
- Check out our last slides on improving this phase

6.Sync & upload

- Time to upload bitmap information to the GPU.
- The UI thread passes all the resources to the RenderThread.

RenderThread (added in L) is a thread helping the busy UI thread with the conversion of display lists to OpenGL commands, and sends them to the GPU. During which, the UI thread can start processing next frame.

High?

There are many resources to pass on. Usually it is due to too many images used or using too big of an image

Try to:

- reduce visible images
- reducing quality
- resizing large images before uploading
- Check out our last slides on bitmap abuse

7.Command Issue

The execution phase: Android’s 2D renderer issues commands to OpenGL to actually draw all display lists on screen.

High?

- Check some complex view (most likely a custom view)
- Maybe many views redrawing, due to:
- Invalidation
- Animation (for example, if a view is rotated or translated, we might need to redraw other views underneath it)

8.Swap Buffers

- The CPU tells the GPU that it’s done rendering a frame.
- Then, the CPU blocks the UI thread until the GPU finishes its work and acknowledges that the command was received.

High?:

- GPU is working hard! Could be due to many complex views that require a lot of OpenGL rendering commands processing.

## Measure… Layout… Draw!
This time we would focus on the Measure/Layout phase, which determines each view’s size and position, so that we can draw it.

![measure_layout_draw](https://github.com/RogerGold/AndroidNotes/blob/master/measure_layout_draw.png)

### Step 1: Measure

Goal: Determine the view size.

The size includes the view’s descendants size, and has to be agreed by the view parent.
View size is defined in 2 ways:

- measured width & measured height — how big a view wants to be within its parent. This is the size we’re looking for on this step.
- width & height (aka: drawing width & drawing height) — Actual size on screen, at drawing time and after layout. This will be figured out later on in step 2

How does it work?

- Top-down recursive traversal of the view tree.
- Each view hands dimension requirements to its descendants.
- How? the parent defines for each child’s height and width one of the 3 MeasureSpec class options:
###
1. UNSPECIFIED: the child can be as big as it wants.
2. EXACTLY: the child should be on an exact size.
3. AT MOST: the child can be as big as it wants up to some maximum.
###
- Each view’s height and width preferences are defined by one of the 3 ViewGroup.LayoutParams class options:
###
1. An exact number
2. MATCH_PARENT: the child wants to be as big as its parent
3. WRAP_CONTENT: the view wants to be as big as its content
###
- The measurement is done on the method: onMeasure(int widthMeasureSpec, int heightMeasureSpec)
- When returns, every view has to have its measuredWidth & measuredHeight (can be done by calling super()) , or else an IllegalStateException is thrown.
- Notice that this process sometimes a negotiation between a view and its children, and so measure() may be called more than once. More on that on a later post.

Since the traversal is top down, and each parent tells its children the requirements, we end up with our goal achieved:

Each view’s measured size includes its children size, and fit its parent requirements.

### Step 2: Layout
Goal: Set position and size (drawing width & drawing height) for view and all its descendants.

- Similar to step 1: top-down recursive traversal of the view tree.
- Each parent positions all of its children according to sizes measured on previous step.
- The positioning is done on method onLayout(boolean changed, int left, int top, int right, int bottom) whereas left, top, right and bottom are relative to parent.
- When overriding onLayout(), we have to call layout() on each child.

### Step 3: Draw
- After size and position is figured out, the view can draw itself accordingly.
- In onDraw(Canvas) Canvas object generates (or updates) a list of OpenGL-ES commands (displayList) to send to the GPU.

### When things change…
When view properties change, the view notifies the system. Depending on the changed properties, the view calls either:

- Invalidate — which calls onDraw() for only the view
- requestLayout — which bubbles up to the root view, then calls the entire process (measure → layout → draw)

A classic simple example for a situation that requests a layout: Let us we have 2 views located relatively to one another within a RelativeLayout. Than, if one view changes its size — it must result the other view to reposition, and maybe the parent to change size. So we changed one view’s properties, but it caused the whole layout to be outdated.

## Layout Once, Layout Twice — Sold!
### When might a second layout pass occur?
Few examples:
### RelativeLayout
Always runs at least 2 layout passes: First according to each view requests. Then the layout evaluates the relations between views, calculates weights etc.. and finally performs another pass, to determine the final positions for rendering.
### LinearLayout
Generally issues a single layout pass, unless using weights. That case is somewhat similar in process to the above mentioned: LinearLayout performs a first pass according to each view requests. Only then it can calculate the view sizes according to weights distribution, and finally issues a second layout pass.
### Gridlayout
Also generally issues one layout pass. However, if we use weights wrong, or use “fill*” in layout_gravity attribute — a second layout pass might occur.
In the simple case, the two layout passes might not affect performance that much. However, without paying attention it can aggregate quite quickly and the process becomes a lot more expensive.

### Situations to pay attention to
- Deep view hierarchy — the layout process might be expensive on anyhow, so the deep and complex our view hierarchy is, the more time consuming the layout traversal is.
- Double taxation causing object is close to the root view — remember that when requesting a layout, the request bubbles up the view tree. For a view that issues double layout, each child will automatically double the times participating in traversal. This can cascade quite quickly.
- If there are many of them like in list — which copies the layout.
- Nested double layout causing objects. To emphasize that this can be dangerous:

Remember that the layout pass begins on top of the hierarchy, all the way down. So if grandma asks father to layout twice, and father asks daughter to layout twice, and daughter asks teddy to layout twice — then overall teddy participated in 2³ = 8 layout passes.Expensive it might be.
### So what can we do?
Be aware

- Use the objects in situations described above with attention. 

### Flatten hierarchy
- Remove useless views
- Use <merge> when including a layout
- Flatter layout hierarchy will usually be beneficial.
- Create custom view that will be more efficient, as can be more accurate for your application needs.
### Minimize requests
- Just don’t request layout if you don’t really need to.

### Hierarchy Viewer
A cool tool that helps us visualize and better understand the complexity of our layouts.
What does it do for us?

- Displays complete view hierarchy
- Measure time for view rendering phases
- Show the view objects fields and properties

Found on Android Studio :
Tools→ Android → Android Device Monitor → Hierarchy Viewer
### What do we have here?
- Choose your activity on the Windows panel (on left hand side)
- Tree Overview shows the entire view hierarchy (on right hand side)
- Tree View window (on center) displays the part of the view hierarchy selected inside the square within the Tree Overview.
- Clicking a view on Tree View previews it on top of clicked box, as well as on Layout View panel (bottom right). It also show more View object properties on View Properties panel.
- Select a view box, then click Layout Times icon.

then each child view box gets 3 colored dots on it:
### What’s with the dots?
- Each view box has 3 dots, which represents: measure, layout and draw respectively.
- The dots color represents the time the system spent on the relevant phase, relative to other views in the tree.
- Green — the view is in the faster 50% views on the tree
- Yellow — in the slower 50%
- Red — the slowest view in the tree.


Remember that these are relative to other views on the tree. So red or yellow dots don’t necessarily mean there’s something wrong with the view. View groups and complex views, for example, will naturally render slower than simple views.


We may use the Invalidate and Request Layout icons on the top bar, for debugging, and examine how they affect the layout times.

![HierarchyViewer](https://github.com/RogerGold/AndroidNotes/blob/master/HierarchyViewer.png)

We understood better the reasons and implications of double layout pass, and have some ideas on how to improve and profile our application layout.