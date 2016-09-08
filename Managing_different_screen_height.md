# An important note when managing different screen height
### Managing Different Height
Obviously Samsung Galaxy SII has is shorter (533.33dp) compare to Samsung Galaxy S7 (640dp).
But there are some devices does varies e.g. Nexus 5, although physically has the height of 640dp, but has 48dp of it has been used by the System Bar (at the bottom for Soft Button). Hence it essentially has 592dp.
Nicely we could easily programmatically calculate them

		Float deviceDensity  = getResources().getDisplayMetrics().density;
		
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);
		
		float deviceHeight = outMetrics.heightPixels / deviceDensity;
		float deviceWidth = outMetrics.widthPixels / deviceDensity;

This will return 640dp for Samsung Galaxy S7 and 592dp for Nexus 5. Great, we could now define the respective value folders for what we need.

### The important note
After some investigation, now all is clear and logical. Apparently it was stated in developer.android.com.

" The Android system might use some of the screen for system UI (such as the system bar at the bottom of the screen or the status bar at the top), so some of the screen might not be available for your layout "

The status bar! We should exclude the status bar height for the height number we use to name the folder. In the case of Nexus 5, the 592dp height includes the Status Bar height (which is 25dp). Hence the number we should define in your folder is 592–25 = 567dp.

So if we define the value folder as below, the Nexus 5 would be able to get the respective value defined in this folder.

- value-h567dp — Nexus 5

Do note, not all status bar is 25dp. For Samsung Galaxy S7 and Samsung Galaxy Note II, it is 24dp. For Samsung Note II, it is 25dp tough.

- value-h509dp — Samsung Galaxy SII

- value-h615dp — Samsung Galaxy Note II

- value-h616dp — Samsung Galaxy S7 (it will use h615dp if there’s no h616dp)


![Illustration_of_Nexus_5](https://github.com/RogerGold/media/blob/master/Illustration_of_Nexus_5.png)

One thing that bothers my mind is, the above snippet of code calculating the height, does remove the System Bar from the calculation, but not the Status Bar. Maybe because one could use the Status Bar area for its view. This can be done with the below code in onCreate()

		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

With or without this code, the height calculation still return the same (include the status bar height), and it is still referring to the same resource folder (with the number that shouldn’t include the status bar height), even though the actual height of the App used differs.



