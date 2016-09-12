# Launch screen

### Understanding the launch screen
First of all, what is a launch screen? Well it is basically the same thing as a splash screen, but with a different name.

The most important thing however is to understand how the launch screen should work and how to implement it in the correct way.

According to the Material Design guidelines:

>“The launch screen is a user’s first experience of your application.”

The launch screen is the very first thing that the user associate your app with, therefor, it is very important that you don’t implement it in the wrong way.

The launch screen’s main function is to act as a placeholder until the rest of your app has loaded.

### What the launch screen is not
It is important to remember that the launch screen is firstly a placeholder, secondly a place to advertise your brand at.

*Do not show the launch screen if you don’t have to.*

You should not in any way display the launch screen if it not necessary, the user will see the launch screen as something that slows down the user experience rather than a placeholder. If the app is already loaded in the phone’s memory, skip the launch screen and head directly to the UI.

And lastly, implement the launch screen so that it is the first thing that the user sees. Do not show the launch screen after displaying a blank canvas as shown below. That is not the way to go!

![launch_screen](https://github.com/RogerGold/media/blob/master/launch_screen.png)

### The code
We are actually going to work with themes!
In your styles.xml:

Create a new theme style that looks like your existing but add this line to the style:

	<item name=”android:windowBackground”>@drawable/launch_screen</item>

It should look like this:

	<--! styles.xml -->
	<style name="PartyTheme" parent="Theme.AppCompat.NoActionBar">
	    <item name="windowNoTitle">true</item>
	    <item name="colorPrimary">@color/pink500</item>
	    <item name="colorPrimaryDark">@color/transparentToolbar</item>
	    <item name="colorAccent">@color/pink500</item>
	    <item name="android:windowBackground">@drawable/launch_screen</item>
	
	</style>

Next up is to create a drawable that acts like our background to the style that we just created. Right click on your drawable folder > New > Drawable resource file. Paste the following code to the new file:

	<--! launch_screen.xml -->
	<?xml version="1.0" encoding="utf-8"?>
	
	<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
	    <item>
	        <color android:color="@color/background_material_light"/>
	    </item>
	    <item>
	        <bitmap
	            android:src="@drawable/launch_logo"
	            android:tileMode="disabled"
	            android:gravity="center"/>
	    </item>
	</layer-list>

As you can see, this drawable has a background colour and a bitmap with our logo in the middle. Remember to set the bitmap’s gravity to center if you want it to located in the center of the screen.

Now, go to your manifest and change the theme of the launch activity to the style we created earlier.

	<--! AndroidManifest.xml -->
	<activity
	    android:name=".MainActivity"
	    android:label="@string/app_name"
	    android:theme="@style/PartyTheme" >
	    <intent-filter>
	        <action android:name="android.intent.action.MAIN" />
	        <category android:name="android.intent.category.LAUNCHER" />    </intent-filter>
	</activity>

You are now done.

Now your style’s background will display the launch screen. And because of the order that Android shows the different parts of the app, the launch screen will now be shown instead of a blank canvas when to app is being loaded, cool right?

### Final notes
Depending on how your MainActivity look like, you might need to change the background colour of your RelativeLayout/LinearLayout/etc to the original colour that you had previously.

Creating a launch screen is a very task to do and gives your app a more branded look (if that is what you want). Implementing a launch screen in the correct way does not only make your app look better, it also functions as it should: as a placeholder.