# What is an android window?
In one sentence, A Window is a rectangular area which has one view hierarchy. Colored rectangles in below image are windows.

![phoneWindow](https://github.com/RogerGold/media/blob/master/phoneWindow.png)

As you can see, there can be multiple windows in one screen, and [WindowManager manages](https://developer.android.com/reference/android/view/WindowManager.html) them. Window list in current screen can be obtained via Hierarchy Viewer, or adb shell dumpsys window. 

Here is a very basic and simple conceptual overview of how interaction happens among the Window, Surface, Canvas, and Bitmap.

![windowBasic](https://github.com/RogerGold/media/blob/master/windowBasic.jpg)

Window list in Hierarchy Viewer example : 

![HierarchyViewer](https://github.com/RogerGold/media/blob/master/HierarchyViewer.png)

According to [Android Developer Documentation](https://developer.android.com/guide/components/activities.html#qv-wrapper),

"Each activity is given a window in which to draw its user interface."

 Dianne Hackborn, who is a Android framework engineer, gave some definitions here. She said,

- A Surface is an object holding pixels that are being composited to the screen. Every window you see on the screen (a dialog, your full-screen activity, the status bar) has its own surface that it draws in to, and Surface Flinger renders these to the final display in their correct Z-order. A surface typically has more than one buffer (usually two) to do double-buffered rendering: the application can be drawing its next UI state while the surface flinger is compositing the screen using the last buffer, without needing to wait for the application to finish drawing.

- A window is basically like you think of a window on the desktop. It has a single Surface in which the contents of the window is rendered. An application interacts with the Window Manager to create windows; the Window Manager creates a Surface for each window and gives it to the application for drawing. The application can draw whatever it wants in the Surface; to the Window Manager it is just an opaque rectangle.

- A View is an interactive UI element inside of a window. A window has a single view hierarchy attached to it, which provides all of the behavior of the window. Whenever the window needs to be redrawn (such as because a view has invalidated itself), this is done into the window's Surface. The Surface is locked, which returns a Canvas that can be used to draw into it. A draw traversal is done down the hierarchy, handing the Canvas down for each view to draw its part of the UI. Once done, the Surface is unlocked and posted so that the just drawn buffer is swapped to the foreground to then be composited to the screen by Surface Flinger.

- A SurfaceView is a special implementation of View that also creates its own dedicated Surface for the application to directly draw into (outside of the normal view hierarchy, which otherwise must share the single Surface for the window). The way this works is simpler than you may expect -- all SurfaceView does is ask the window manager to create a new window, telling it to Z-order that window either immediately behind or in front of the SurfaceView's window, and positioning it to match where the SurfaceView appears in the containing window. If the surface is being placed behind the main window (in Z order), SurfaceView also fills its part of the main window with transparency so that the surface can be seen.

- A Bitmap is just an interface to some pixel data. The pixels may be allocated by Bitmap itself when you are directly creating one, or it may be pointing to pixels it doesn't own such as what internally happens to hook a Canvas up to a Surface for drawing. (A Bitmap is created and pointed to the current drawing buffer of the Surface.)

Also please keep in mind that, as this implies, a SurfaceView is a pretty heavy-weight object. If you have multiple SurfaceViews in a particular UI, stop and think about whether this is really needed. If you have more than two, you almost certainly have too many.

So, in a nutshell,A Activity has a window(in which to draw its user interface), and a Window has a single Surface & a single view hierarchy attached to it, and a Surface include ViewGroup which holds Views.

![nutshell](https://github.com/RogerGold/media/blob/master/nutshell.png)