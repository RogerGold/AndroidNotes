### 1. get bitmap from resource
Bitmap barcodeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.name_image);

### 2. android:layout_weight="1" android:layout_width="0dp"
- Setting the width to zero (0dp) improves layout performance because using "wrap_content" as
the width requires the system to calculate a width that is ultimately irrelevant because the weight 
value requires another width calculation to fill the remaining space.

- The weight value is a number that specifies the amount of remaining space each view should consume, 
relative to the amount consumed by sibling views. This works kind of like the amount of ingredients in a drink recipe: 
"2 parts soda, 1 part syrup" means two-thirds of the drink is soda. For example, if you give one view a weight of 2 
and another one a weight of 1, the sum is 3, so the first view fills 2/3 of the remaining space and the second view fills the rest. 
If you add a third view and give it a weight of 1, then the first view (with weight of 2) now gets 1/2 the remaining space, 
while the remaining two each get 1/4.
- The default weight for all views is 0, so if you specify any weight value greater than 0 to only one view,
then that view fills whatever space remains after all views are given the space they require

### 3.Supporting Different Languages
Once you’ve decided on the languages you will support, create the resource subdirectories and string resource files. For example:

    MyProject/
        res/
           values/
               strings.xml
           values-es/
               strings.xml
           values-fr/
               strings.xml
               
Add the string values for each locale into the appropriate file.
At runtime, the Android system uses the appropriate set of string resources based on the locale currently set for the user's device.

#### Use the String Resources
You can reference your string resources in your source code and other XML files using the resource name defined by the <string> element's name attribute.
In your source code, you can refer to a string resource with the syntax R.string.<string_name>. There are a variety of methods that accept a string resource this way.

In code:

             // Get a string resource from your app's Resources
            String hello = getResources().getString(R.string.hello_world);


            // Or supply a string resource to a method that requires a string
            TextView textView = new TextView(this);
            textView.setText(R.string.hello_world);

In XML: 

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/hello_world" />


### 4.Supporting Different Screens

        MyProject/
            res/
                layout/              # default (portrait)
                    main.xml
                layout-land/         # landscape
                    main.xml
                layout-large/        # large (portrait)
                    main.xml
                layout-large-land/   # large landscape
                    main.xml
                    
### 5. Create Different Bitmaps    
   You should always provide bitmap resources that are properly scaled to each of the generalized density buckets: low, medium, high and extra-high density. This helps you achieve good graphical quality and performance on all screen densities.

To generate these images, you should start with your raw resource in vector format and generate the images for each density using the following size scale:

        xhdpi: 2.0
        hdpi: 1.5
        mdpi: 1.0 (baseline)
        ldpi: 0.75
This means that if you generate a 200x200 image for xhdpi devices, you should generate the same resource in 150x150 for hdpi, 100x100 for mdpi, and 75x75 for ldpi devices.

Then, place the files in the appropriate drawable resource directory:

        MyProject/
            res/
                drawable-xhdpi/
                    awesomeimage.png
                drawable-hdpi/
                    awesomeimage.png
                drawable-mdpi/
                    awesomeimage.png
                drawable-ldpi/
                    awesomeimage.png
            
Any time you reference @drawable/awesomeimage, the system selects the appropriate bitmap based on the screen's density.

### 6. Activity Lifecycle

![Lifecycle](https://github.com/RogerGold/media/blob/master/basic_lifecycle.png)

正确实现您的Activity生命周期方法可确保您的应用按照以下几种方式良好运行，包括：

- 如果用户在使用您的应用时接听来电或切换到另一个应用，它不会崩溃。
- 在用户未主动使用它时不会消耗宝贵的系统资源。
- 如果用户离开您的应用并稍后返回，不会丢失用户的进度。
- 当屏幕在横向和纵向之间旋转时，不会崩溃或丢失用户的进度。

当用户从主屏幕选择您的应用图标时，系统会为您已声明为“启动器”（ 或“主要”）Activity的应用中的 Activity 调用 onCreate() 方法。 这是作为您的应用的用户界面主入口的Activity。
您可以在AndroidManifest.xml 定义哪个Activity用作主Activity，该说明文件位于您项目目录的根目录中。
您的应用的主Activity必须使用 <intent-filter>（包括 MAIN 操作和 LAUNCHER 类别）声明。例如

        <activity android:name=".MainActivity" android:label="@string/app_name">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        
如果未对您的Activity之一声明 MAIN 操作或 LAUNCHER 类别，那么您的应用图标将不会出现在应用的主屏幕列表中。

无论Activity是用户单击您的应用图标时创建的主Activity还是您的应用在响应用户操作时开始的其他Activity，系统都会通过调用其 onCreate() 方法创建 Activity 的每个新实例。

一旦 onCreate() 完成执行操作，系统会相继调用 onStart() 和 onResume() 方法,一旦这一系列回调完成，Activity就进入“Resume”状态，此时用户可与Activity进行交互，直至用户切换到其他Activity。

Activity只能在三种状态之一下存在很长时间。
#### Resume
在这种状态下，Activity处于前台，且用户可以与其交互。（有时也称为“运行”状态。）
#### Pause
在这种状态下，Activity被在前台中处于半透明状态或者未覆盖整个屏幕的另一个Activity—部分阻挡。 暂停的Activity不会接收用户输入并且无法执行任何代码。
#### Stop
在这种状态下，Activity被完全隐藏并且对用户不可见；它被视为处于后台。 停止时，Activity实例及其诸如成员变量等所有状态信息将保留，但它无法执行任何代码。

#### 销毁Activity
当Activity的第一个生命周期回调是 onCreate() 时，它最近的回调是 onDestroy()。系统会对您的Activity调用此方法，作为您的Activity实例完全从系统内存删除的最终信号。

大多数应用不需要实现此方法，因为本地类引用与Activity一同销毁，并且您的Activity应在 onPause() 和 onStop() 期间执行大多数清理操作。 但是，如果您的Activity包含您在 onCreate() 期间创建的后台线程或其他如若未正确关闭可能导致内存泄露的长期运行资源，您应在 onDestroy() 期间终止它们。

注意：在所有情况下，系统在调用 onPause() 和 onStop() 之后都会调用 onDestroy() ，只有一个例外：当您从 onCreate() 方法内调用 finish() 时。在有些情况下，比如当您的Activity作为临时决策工具运行以启动另一个Activity时，您可从 onCreate() 内调用 finish() 来销毁Activity。 在这种情况下，系统会立刻调用 onDestroy()，而不调用任何其他 生命周期方法。

#### Pause和Resume Activity
只要Activity仍然部分可见但目前又未处于焦点之中，它会一直暂停。

当您的Activity进入暂停状态时，系统会对您的 Activity 调用 onPause() 方法，通过该方法，您可以停止不应在暂停时继续的进行之中的操作（比如视频）或保留任何应该永久保存的信息，以防用户坚持离开应用。如果用户从暂停状态返回到您的Activity，系统会重新开始该Activity并调用 onResume() 方法。

当系统为您的Activity调用 onPause() 时，它从技术角度看意味着您的Activity仍然处于部分可见状态，但往往说明用户即将离开Activity并且它很快就要进入“停止”状态。 您通常应使用 onPause() 回调：
- 停止动画或其他可能消耗 CPU 的进行之中的操作。
- 提交未保存的更改，但仅当用户离开时希望永久性保存此类更改（比如电子邮件草稿）。
- 释放系统资源，比如广播接收器、传感器手柄（比如 GPS） 或当您的Activity暂停且用户不需要它们时仍然可能影响电池寿命的任何其他资源。

一般情况下，您不得使用 onPause() 永久性存储用户更改（比如输入表格的个人信息）。 只有在您确定用户希望自动保存这些更改的情况（比如，电子邮件草稿）下，才能在 onPause()中永久性存储用户更改。但您应避免在 onPause() 期间执行 CPU 密集型工作，比如向数据库写入信息，因为这会拖慢向下一Activity过渡的过程（您应改为在 onStop()期间执行高负载关机操作。）

您应通过相对简单的方式在 onPause() 方法中完成大量操作，这样才能加快在您的Activity确实停止的情况下用户向下一个目标过渡的速度，即使Activity切换流畅。

注意：当您的Activity暂停时，Activity 实例将驻留在内存中并且在Activity继续时被再次调用。您无需重新初始化在执行任何导致进入“继续”状态的回调方法期间创建的组件。

#### Resume Activity
当用户从“Pause”状态继续您的Activity时，系统会调用 onResume() 方法。

请注意，每当您的Activity进入前台时系统便会调用此方法，包括它初次创建之时。 同样地，您应实现onResume() 初始化您在 onPause() 期间释放的组件并且执行每当Activity进入“Resume”状态时必须进行的任何其他初始化操作（比如开始动画和初始化只在Activity具有用户焦点时使用的组件）。

#### Stop和Restart Activity
有几种Activity停止和重新开始的关键场景：
- 用户打开“最近应用”窗口并从您的应用切换到另一个应用。当前位于前台的您的应用中的Activity将停止。 如果用户从主屏幕启动器图标或“最近应用”窗口返回到您的应用，Activity会重新开始。
- 用户在您的应用中执行开始新Activity的操作。当第二个Activity创建好后，当前Activity便停止。 如果用户之后按了返回按钮，第一个Activity会重新开始。
- 用户在其手机上使用您的应用的同时接听来电。

#### Stop Activity
当您的Activity收到 onStop() 方法的调用时，它不再可见，并且应释放几乎所有用户不使用时不需要的资源。 

一旦您的Activity停止，如果需要恢复系统内存，系统可能会销毁该实例。 在极端情况下，系统可能会仅终止应用进程，而不会调用Activity的最终 onDestroy() 回调，因此您使用 onStop() 释放可能泄露内存的资源非常重要。

尽管 onPause() 方法在 onStop()之前调用，您应使用 onStop() 执行更大、占用更多 CPU 的关闭操作，比如向数据库写入信息。

当您的Activity停止时， Activity 对象将驻留在内存中并在Activity继续时被再次调用。 您无需重新初始化在任何导致进入“继续”状态的回调方法过程中创建的组件。 系统还会在布局中跟踪每个 View 的当前状态，如果用户在 EditText 小工具中输入文本，该内容会保留，因此您无需保存即可恢复它。

注意：即使系统在Activity停止时销毁了Activity，它仍会保留 Bundle（键值对的二进制大对象）中的 View 对象（比如 EditText 中的文本），并在用户导航回Activity的相同实例时恢复它们 。

#### Restart Activity

当您的Activity从停止状态返回前台时，它会接收对 onRestart() 的调用。系统还会在每次您的Activity变为可见时调用 onStart() 方法（无论是正重新开始还是初次创建）。 但是，只会在Activity从停止状态继续时调用 onRestart() 方法，因此您可以使用它执行只有在Activity之前停止但未销毁的情况下可能必须执行的特殊恢复工作。

### 重新创建Activity
当您的Activity因用户按了返回 或Activity自行完成而被销毁时，系统的 Activity 实例概念将永久消失，因为行为指示不再需要Activity。 但是，如果系统因系统局限性（而非正常应用行为）而销毁Activity，尽管 Activity 实际实例已不在，系统会记住其存在，这样，如果用户导航回实例，系统会使用描述Activity被销毁时状态的一组已保存数据创建Activity的新实例。 系统用于恢复先前状态的已保存数据被称为“实例状态”，并且是 Bundle 对象中存储的键值对集合。

注意：每次用户旋转屏幕时，您的Activity将被销毁并重新创建。 当屏幕方向变化时，系统会销毁并重新创建前台Activity，因为屏幕配置已更改并且您的Activity可能需要加载备用资源（比如布局）。

默认情况下，系统会使用 Bundle 实例状态保存您的Activity布局（比如，输入到 EditText 对象中的文本值）中有关每个 View 对象的信息。 这样，如果您的Activity实例被销毁并重新创建，布局状态便恢复为其先前的状态，且您无需代码。 但是，您的Activity可能具有您要恢复的更多状态信息，比如跟踪用户在Activity中进度的成员变量。

注意：为了 Android 系统恢复Activity中视图的状态，每个视图必须具有 android:id 属性提供的唯一 ID。

要保存有关Activity状态的其他数据，您必须替代 onSaveInstanceState() 回调方法。当用户要离开Activity并在Activity意外销毁时向其传递将保存的 Bundle 对象时，系统会调用此方法。 如果系统必须稍后重新创建Activity实例，它会将相同的 Bundle 对象同时传递给 onRestoreInstanceState() 和 onCreate() 方法。

![lifecycle_savestate](https://github.com/RogerGold/media/blob/master/basic_lifecycle_savestate.png)

当系统开始停止您的Activity时，它会 调用 onSaveInstanceState() (1)，因此，您可以指定您希望在 Activity 实例必须重新创建时保存的额外状态数据。如果Activity被销毁且必须重新创建相同的实例，系统将在 (1) 中定义的状态数据同时传递给 onCreate() 方法(2) 和 onRestoreInstanceState() 方法(3)。

####保存Activity状态
当您的Activity开始停止时，系统会调用 onSaveInstanceState() 以便您的Activity可以保存带有键值对集合的状态信息。 此方法的默认实现保存有关Activity视图层次的状态信息，例如 EditText 小工具中的文本或ListView 的滚动位置。

要保存Activity的更多状态信息，您必须实现 onSaveInstanceState() 并将键值对添加至 Bundle 对象。 例如：

        static final String STATE_SCORE = "playerScore";
        static final String STATE_LEVEL = "playerLevel";
        ...

        @Override
        public void onSaveInstanceState(Bundle savedInstanceState) {
            // Save the user's current game state
            savedInstanceState.putInt(STATE_SCORE, mCurrentScore);
            savedInstanceState.putInt(STATE_LEVEL, mCurrentLevel);

            // Always call the superclass so it can save the view hierarchy state
            super.onSaveInstanceState(savedInstanceState);
        }
        
注意：始终调用 onSaveInstanceState() 的超类实现，以便默认实现可以保存视图层次的状态。

#### 恢复Activity状态
当您的Activity在先前销毁之后重新创建时，您可以从系统向Activity传递的 Bundle 恢复已保存的状态。onCreate() 和 onRestoreInstanceState() 回调方法均接收包含实例状态信息的相同 Bundle。

因为无论系统正在创建Activity的新实例还是重新创建先前的实例，都会调用 onCreate() 方法，因此您必须在尝试读取它之前检查状态 Bundle 是否为 null。 如果为 null，则系统将创建Activity的新实例，而不是恢复已销毁的先前实例。

例如，此处显示您如何可以在 onCreate() 中恢复一些状态数据：

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState); // Always call the superclass first

            // Check whether we're recreating a previously destroyed instance
            if (savedInstanceState != null) {
                // Restore value of members from saved state
                mCurrentScore = savedInstanceState.getInt(STATE_SCORE);
                mCurrentLevel = savedInstanceState.getInt(STATE_LEVEL);
            } else {
                // Probably initialize members with default values for a new instance
            }
            ...
        }

您可以选择实现系统在 onStart() 方法之后调用的 onRestoreInstanceState() ，而不是在onCreate() 期间恢复状态。 系统只在存在要恢复的已保存状态时调用 onRestoreInstanceState() ，因此您无需检查 Bundle 是否为 null：

        public void onRestoreInstanceState(Bundle savedInstanceState) {
            // Always call the superclass so it can restore the view hierarchy
            super.onRestoreInstanceState(savedInstanceState);

            // Restore state members from saved instance
            mCurrentScore = savedInstanceState.getInt(STATE_SCORE);
            mCurrentLevel = savedInstanceState.getInt(STATE_LEVEL);
        }

注意：始终调用 onSaveInstanceState() 的超类实现，以便默认实现可以恢复视图层次的状态。
