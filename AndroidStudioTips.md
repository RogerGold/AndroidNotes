# Android Studio Tips
ref [50-android-studio-tips](https://medium.com/@mmbialas/50-android-studio-tips-tricks-resources-you-should-be-familiar-with-as-an-android-developer-af86e7cf56d2)
### Material Colors theme for Android Logcat
To change Android Studio Logcat you need to go to: Preferences (Settings on Windows / Linux machines) → Editor → Colors & Fonts → Android Logcat and change the foreground color for every type of log.
My material colors:
- Assert #BA68C8
- Debug #2196F3
- Error #F44336
- Info #4CAF50
- Verbose #BBBBBB
- Warning #FF9800

### Prevent Android Studio Logcat from clearing the log for the current application when it crashes. 
To do that you need to go to the Android Monitor panel and choose Edit filter configuration on the right side dropdown.

### Apply a proper code style to your IDE (IntelliJ / Android Studio).
Go to Preferences → Code Style → Java and in a Scheme dropdown you can choose your code style (or set up a new one).

### Use split screen for increasing efficiency.
To turn this feature on, you need to right mouse click on the tab of your main screen and choose Split Vertically / Horizontally feature.
But to be as efficient as possible we need to set up a custom keyboard’s shortcut. To do that go to Preferences → Keymap and search for Split Vertically. Then open a context menu and click Add Keyboard Shortcut. 
In my case, for vertical split view I added control + alt + v. 

### Distraction Free Mode. 
You can enable it by going to: View → Enter Distraction Free Mode

### Use Live Templates
you can use a shortcut: cmd + j (Windows / Linux: ctrl + j).

You can use the File > Settings > Editor > Live Templates menu option to see the full list.
Of course, if your favorite boiler plate isn’t already there, remember that you can Create Your Own Live Templates.

Go to File > Settings > Editor > Live Templates. Click the Android group, and press the plus button to add a new Live Template.

ref: [android-studio-live-templates](https://medium.com/google-developers/writing-more-code-by-writing-less-code-with-android-studio-live-templates-244f648d17c7)


### Shortcuts and helpful commands
- Open a class: cmd + o (Windows / Linux: ctrl + n).
- Open any file: cmd + shift + o (Windows / Linux: ctrl + shift + n).
