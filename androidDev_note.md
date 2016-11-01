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

### 构建灵活的界面Fragment
利用 FragmentManager 类提供的方法，你可以在运行时添加、移除和替换 Activity 中的 Fragment，以便为用户提供一种动态体验。

#### 创建一个 Fragment

 必须要实现onCreateView() 回调来定义一个 layout.
 
        public class ArticleFragment extends Fragment {
            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
                // Inflate the layout for this fragment
                return inflater.inflate(R.layout.article_view, container, false);
            }
        }


#### 在运行时向 Activity 添加 Fragment
要执行添加或移除 Fragment 等事务，你必须使用 FragmentManager 创建一个 FragmentTransaction，后者可提供用于执行添加、移除、替换以及其他 Fragment 事务的 API。

如果 Activity 中的 Fragment 可以移除和替换，你应在调用 Activity 的 onCreate() 方法期间为 Activity 添加初始 Fragment(s)。

在处理 Fragment（特别是在运行时添加的 Fragment ）时，请谨记以下重要规则：必须在布局中为 Fragment 提供 View 容器，以便保存 Fragment 的布局。

        <FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
                android:id="@+id/fragment_container"
                android:layout_width="match_parent"
                android:layout_height="match_parent" />

在 Activity 内部，使用 Support Library API 调用 getSupportFragmentManager() 以获取 FragmentManager，然后调用 beginTransaction() 创建 FragmentTransaction，同时调用 add() 添加 Fragment。

你可以使用同一个 FragmentTransaction 对 Activity 执行多 Fragment 事务。当你准备好进行更改时，必须调用 commit()。

添加 Fragment ：

          &Override
                public void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    setContentView(R.layout.news_articles);

                    // 确认 Activity 使用的布局版本包含
                    // fragment_container FrameLayout
                    if (findViewById(R.id.fragment_container) != null) {

                        // 不过，如果我们要从先前的状态还原，
                        // 则无需执行任何操作而应返回
                        // 否则就会得到重叠的 Fragment 。
                        if (savedInstanceState != null) {
                            return;
                        }

                        // 创建一个要放入 Activity 布局中的新 Fragment
                        HeadlinesFragment firstFragment = new HeadlinesFragment();

                        // 如果此 Activity 是通过 Intent 发出的特殊指令来启动的，
                        // 请将该 Intent 的 extras 以参数形式传递给该 Fragment
                        firstFragment.setArguments(getIntent().getExtras());

                        // 将该 Fragment 添加到“fragment_container”FrameLayout 中
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.fragment_container, firstFragment).commit();
                    }
                }
                
                
      由于该 Fragment 已在运行时添加到 FrameLayout 容器中，而不是在 Activity 布局中通过 <fragment> 元素进行定义，因此该 Activity 可以移除和替换这个 Fragment 。
      
#### 用一个 Fragment 替换另一个 Fragment
请注意，当你执行替换或移除 Fragment 等 Fragment 事务时，最好能让用户向后导航和“撤消”所做更改。要通过 Fragment 事务允许用户向后导航，你必须调用 addToBackStack()，然后再执行 FragmentTransaction。

注意：当你移除或替换 Fragment 并向返回堆栈添加事务时，已移除的 Fragment 会停止（而不是销毁）。如果用户向后导航，还原该 Fragment，它会重新启动。如果你没有向返回堆栈添加事务，那么该 Fragment 在移除或替换时就会被销毁。

替换 Fragment 的示例：

        // 创建 Fragment 并为其添加一个参数，用来指定应显示的文章
            ArticleFragment newFragment = new ArticleFragment();
            Bundle args = new Bundle();
            args.putInt(ArticleFragment.ARG_POSITION, position);
            newFragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // 将 fragment_container View 中的内容替换为此 Fragment ，
            // 然后将该事务添加到返回堆栈，以便用户可以向后导航
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);

            // 执行事务
            transaction.commit();

addToBackStack() 方法可接受可选的字符串参数，来为事务指定独一无二的名称。除非你打算使用 FragmentManager.BackStackEntry API 执行高级 Fragment 操作，否则无需使用此名称。

### 数据的保存
#### 保存键值集SharedPreferences 
写入共享首选项：
注意：如果您创建带 MODE_WORLD_READABLE 或 MODE_WORLD_WRITEABLE 的共享首选项文件，那么知道文件标识符 的任何其他应用都可以访问您的数据。
要写入共享首选项文件， 请通过对您的 SharedPreferences 调用 edit() 来创建一个 SharedPreferences.Editor。
传递您想要使用诸如 putInt() 和 putString() 方法写入的键和值。然后调用 commit() 以保存更改。

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.saved_high_score), newHighScore);
        editor.commit();
        
共享首选项读取信息：

要从共享首选项文件中检索值，请调用诸如 getInt() 和 getString() 等方法，为您想要的值提供键，并根据需要提供要在键不存在的情况下返回的默认值。 例如：

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        int defaultValue = getResources().getInteger(R.string.saved_high_score_default);
        long highScore = sharedPref.getInt(getString(R.string.saved_high_score), defaultValue);
        
#### 保存文件
File 对象适合按照从开始到结束的顺序不跳过地读取或写入大量数据。 例如，它适合于图像文件或通过网络交换的任何内容。
#### 内部和外部存储
所有 Android 设备都有两个文件存储区域：“内部”和“外部”存储。这些名称在 Android 早期产生，当时大多数设备都提供内置的非易失性内存（内部存储），以及移动存储介质，比如微型 SD 卡（外部存储）。

#### 内部存储：
- 它始终可用。
- 默认情况下只有您的应用可以访问此处保存的文件。
- 当用户卸载您的应用时，系统会从内部存储中删除您的应用的所有文件。
- 当您希望确保用户或其他应用均无法访问您的文件时，内部存储是最佳选择。

在内部存储中保存文件时，您可以通过调用以下两种方法之一获取作为 File 的相应目录：

getFilesDir()
返回表示您的应用的内部目录的 File 。

getCacheDir()
返回表示您的应用临时缓存文件的内部目录的 File 。 务必删除所有不再需要的文件并对在指定时间您使用的内存量实现合理大小限制，比如，1MB。 如果在系统即将耗尽存储，它会在不进行警告的情况下删除您的缓存文件。

#### 外部存储：
- 它并非始终可用，因为用户可采用 USB 存储的形式装载外部存储，并在某些情况下会从设备中将其删除。
- 它是全局可读的，因此此处保存的文件可能不受您控制地被读取。
- 当用户卸载您的应用时，只有在您通过 getExternalFilesDir() 将您的应用的文件保存在目录中时，系统才会从此处删除您的应用的文件。
- 对于无需访问限制以及您希望与其他应用共享

以下方法对于确定存储可用性非常有用：

        /* Checks if external storage is available for read and write */
        public boolean isExternalStorageWritable() {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                return true;
            }
            return false;
        }

        /* Checks if external storage is available to at least read */
        public boolean isExternalStorageReadable() {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                return true;
            }
            return false;
        }

提示：尽管应用默认安装在内部存储中，但您可在您的宣示说明中指定 android:installLocation 属性，这样您的应用便可安装在在外部存储中。 当 APK 非常大且它们的外部存储空间大于内部存储时，用户更青睐这个选择。 

注意：当用户卸载您的应用时，Android 系统会删除以下各项：
- 您保存在内部存储中的所有文件
- 您使用 getExternalFilesDir() 保存在外部存储中的所有文件。

但是，您应手动删除使用 getCacheDir() 定期创建的所有缓存文件并且定期删除不再需要的其他文件。

#### SQL 数据库中保存数据
将数据保存到数据库对于重复或结构化数据（比如契约信息） 而言是理想之选。

注意：通过实现 BaseColumns 接口，您的内部类可继承调用的主键字段_ID ，某些 Android 类（比如光标适配器）将需要内部类拥有该字段。 这并非必需项，但可帮助您的数据库与 Android 框架协调工作。

例如，该代码段定义了单个表格的表格名称和列名称：

        public final class FeedReaderContract {
            // To prevent someone from accidentally instantiating the contract class,
            // give it an empty constructor.
            public FeedReaderContract() {}

            /* Inner class that defines the table contents */
            public static abstract class FeedEntry implements BaseColumns {
                public static final String TABLE_NAME = "entry";
                public static final String COLUMN_NAME_ENTRY_ID = "entryid";
                public static final String COLUMN_NAME_TITLE = "title";
                public static final String COLUMN_NAME_SUBTITLE = "subtitle";
                ...
            }
        }
        
 SQLiteOpenHelper 类中有一组有用的 API。当您使用此类获取对您数据库的引用时，系统将只在需要之时而不是 应用启动过程中执行可能长期运行的操作：创建和更新数据库。 您只需调用 getWritableDatabase() 或 getReadableDatabase()。

注意：由于它们可能长期运行，因此请确保您在后台线程中调用 getWritableDatabase() 或 getReadableDatabase() ， 比如使用 AsyncTask 或 IntentService。

要使用 SQLiteOpenHelper，请创建一个 替代 onCreate()、onUpgrade() 和 onOpen() 回调方法的子类。

要从数据库中读取信息，请使用 query() 方法，将其传递至选择条件和所需列。该方法结合 insert() 和 update() 的元素，除非列列表定义了您希望获取的数据，而不是希望插入的数据。 查询的结果将在 Cursor 对象中返回给您。

要查看游标中的某一行，请使用 Cursor 移动方法之一，您必须在开始读取值之前始终调用这些方法。 一般情况下，您应通过调用 moveToFirst() 开始，其将“读取位置”置于结果中的第一个条目中。 对于每一行，您可以通过调用 Cursor 获取方法之一读取列的值，比如 getString() 或 getLong()。对于每种获取方法，您必须传递所需列的索引位置，您可以通过调用 getColumnIndex() 或 getColumnIndexOrThrow() 获取。例如：

        cursor.moveToFirst();
        long itemId = cursor.getLong(
            cursor.getColumnIndexOrThrow(FeedEntry._ID)
        );
        
 要从表格中删除行，您需要提供识别行的选择条件。 数据库 API 提供了一种机制，用于创建防止 SQL 注入的选择条件。 该机制将选择规范划分为选择子句和选择参数。 该子句定义要查看的列，还允许您合并列测试。 参数是根据捆绑到子句的项进行测试的值。由于结果并未按照与常规 SQL 语句相同的方式进行处理，它不受 SQL 注入的影响。

        // Define 'where' part of query.
        String selection = FeedEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { String.valueOf(rowId) };
        // Issue SQL statement.
        db.delete(table_name, selection, selectionArgs);

### 应用间的交互
注意：如果您调用了intent，但设备上没有可用于处理intent的应用，您的应用将崩溃。
#### 确认是否存在接收意向的应用

        // Verify it resolves
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(mapIntent, 0);
        boolean isIntentSafe = activities.size() > 0;

        // Start an activity if it's safe
        if (isIntentSafe) {
            startActivity(intent);
        }else{
         //可以为用户提供下载该应用的链接
        }
        
 #### 显示应用选择器
 当您通过将您的 Intent 传递至 startActivity() 而开始Activity时，有多个应用响应意向，用户可以选择默认使用哪个应用。
 
         Intent intent = new Intent(Intent.ACTION_SEND);
        ...

        // Always use string resources for UI text.
        // This says something like "Share this photo with"
        String title = getResources().getString(R.string.chooser_title);
        // Create intent to show chooser
        Intent chooser = Intent.createChooser(intent, title);

        // Verify the intent will resolve to at least one activity
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
        
#### 获取Activity的结果
开始并不一定是单向的另一个Activity。您还可以开始另一个Activity并 接收返回的结果。要接收结果，请调用 startActivityForResult()（而不是 startActivity()）。

需要向 startActivityForResult() 方法传递额外的整数参数。

该整数参数是识别您的请求的“请求代码”。当您收到结果Intent 时，回调提供相同的请求代码，以便您的应用可以正确识别结果并确定如何处理它。

例如，此处显示如何开始允许用户选择联系人的Activity：

        static final int PICK_CONTACT_REQUEST = 1;  // The request code
        ...
        private void pickContact() {
            Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
            pickContactIntent.setType(Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
            startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
        }
     
#### 接收结果

如果您想要向调用您的Activity的Activity返回结果，只需调用 setResult() 指定结果代码和结果 Intent。当您的操作完成且用户应返回原始Activity时，调用 finish() 关闭（和销毁）您的Activity。 例如：

        // Create intent to deliver some kind of result data
        Intent result = new Intent("com.example.RESULT_ACTION", Uri.parse("content://result_uri");
        setResult(Activity.RESULT_OK, result);
        finish();
        
 您必须始终为结果指定结果代码。通常，它为 RESULT_OK 或 RESULT_CANCELED。您之后可以根据需要为 Intent 提供额外的数据。
 如果您只需返回指示若干结果选项之一的整数，您可以将结果代码设置为大于 0 的任何值。 如果您使用结果代码传递整数，并且您无需包含 Intent，您可以调用 setResult() 并且仅传递结果代码。 例如：

        setResult(RESULT_COLOR_RED);
        finish();
        
当用户完成后续Activity并且返回时，系统会调用您的Activity onActivityResult() 的方法。此方法包括三个参数：
- 您向 startActivityForResult() 传递的请求代码。
- 第二个Activity指定的结果代码。如果操作成功，这是 RESULT_OK；如果用户退出或操作出于某种原因失败，则是 RESULT_CANCELED。
- 传送结果数据的 Intent。

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            // Check which request we're responding to
            if (requestCode == PICK_CONTACT_REQUEST) {
                // Make sure the request was successful
                if (resultCode == RESULT_OK) {
                    // The user picked a contact.
                    // The Intent's data Uri identifies which contact was selected.

                    // Do something with the contact here (bigger example below)
                }
            }
        }
        
###  System Permissions
#### Declaring Permissions
Every Android app runs in a limited-access sandbox. If an app needs to use resources or information outside of its own sandbox, the app has to request the appropriate permission. You declare that your app needs a permission by listing the permission in the App Manifest.
#### Add Permissions to the Manifest
To declare that your app needs a permission, put a <uses-permission> element in your app manifest, as a child of the top-level <manifest> element. For example, an app that needs to send SMS messages would have this line in the manifest:

        <manifest xmlns:android="http://schemas.android.com/apk/res/android"
                package="com.example.snazzyapp">

            <uses-permission android:name="android.permission.SEND_SMS"/>

            <application ...>
                ...
            </application>

        </manifest>
 
 The system's behavior after you declare a permission depends on how sensitive the permission is. If the permission does not affect user privacy, the system grants the permission automatically. If the permission might grant access to sensitive user information, the system asks the user to approve the request.
#### Requesting Permissions at Run Time
Beginning in Android 6.0 (API level 23), users grant permissions to apps while the app is running, not when they install the app. 
System permissions are divided into two categories, normal and dangerous:
- Normal permissions do not directly risk the user's privacy. If your app lists a normal permission in its manifest, the system grants the permission automatically.
- Dangerous permissions can give the app access to the user's confidential data. If your app lists a normal permission in its manifest, the system grants the permission automatically. If you list a dangerous permission, the user has to explicitly give approval to your app.
