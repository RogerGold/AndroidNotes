# What is the difference between “px”, “dp”, “dip” and “sp” on Android?
### px
Pixels - corresponds to actual pixels on the screen.
### in
Inches - based on the physical size of the screen.
1 Inch = 2.54 centimeters
### mm
Millimeters - based on the physical size of the screen.
### pt
Points - 1/72 of an inch based on the physical size of the screen.
### dp or dip
Density-independent Pixels - an abstract unit that is based on the physical density of the screen. These units are relative to a 160 dpi screen, so one dp is one pixel on a 160 dpi screen. The ratio of dp-to-pixel will change with the screen density, but not necessarily in direct proportion. Note: The compiler accepts both "dip" and "dp", though "dp" is more consistent with "sp".
### sp
Scale-independent Pixels - this is like the dp unit, but it is also scaled by the user's font size preference. It is recommend you use this unit when specifying font sizes, so they will be adjusted for both the screen density and user's preference.

---
dp is dip. Use it for everything (margin, padding, etc.)

Use sp for {text-size} only.

To get the same size on different screen densities, Android translates these units into pixels at runtime, 

## Moreover

Screen size: Actual physical size, measured as the screen's diagonal. For simplicity, Android groups all actual screen sizes into four generalized sizes: small, normal, large, and extra large.

Screen density: The quantity of pixels within a physical area of the screen; usually referred to as dpi (dots per inch). For example, a "low" density screen has fewer pixels within a given physical area, compared to a "normal" or "high" density screen. For simplicity, Android groups all actual screen densities into four generalized densities: low, medium, high, and extra high.

Orientation: The orientation of the screen from the user's point of view. This is either landscape or portrait, meaning that the screen's aspect ratio is either wide or tall, respectively. Be aware that not only do different devices operate in different orientations by default, but the orientation can change at runtime when the user rotates the device.

Resolution: The total number of physical pixels on a screen. When adding support for multiple screens, applications do not work directly with resolution; applications should be concerned only with screen size and density, as specified by the generalized size and density groups.

Density-independent pixel (dp): A virtual pixel unit that you should use when defining UI layout, to express layout dimensions or position in a density-independent way. The density-independent pixel is equivalent to one physical pixel on a 160 dpi screen, which is the baseline density assumed by the system for a "medium" density screen. At runtime, the system transparently handles any scaling of the dp units, as necessary, based on the actual density of the screen in use. The conversion of dp units to screen pixels is simple: px = dp * (dpi / 160). For example, on a 240 dpi screen, 1 dp equals 1.5 physical pixels. You should always use dp units when defining your application's UI, to ensure proper display of your UI on screens with different densities.

## Converting pixels to dp
	
		/**
		 * This method converts dp unit to equivalent pixels, depending on device density. 
		 * 
		 * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
		 * @param context Context to get resources and device specific display metrics
		 * @return A float value to represent px equivalent to dp depending on device density
		 */
		public static float convertDpToPixel(float dp, Context context){
		    Resources resources = context.getResources();
		    DisplayMetrics metrics = resources.getDisplayMetrics();
		    float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
		    return px;
		}
		
		/**
		 * This method converts device specific pixels to density independent pixels.
		 * 
		 * @param px A value in px (pixels) unit. Which we need to convert into db
		 * @param context Context to get resources and device specific display metrics
		 * @return A float value to represent dp equivalent to px value
		 */
		public static float convertPixelsToDp(float px, Context context){
		    Resources resources = context.getResources();
		    DisplayMetrics metrics = resources.getDisplayMetrics();
		    float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
		    return dp;
		}

***note:Use ´Resources.getSystem().getDisplayMetrics()´ if you don't have a Context handy'***

