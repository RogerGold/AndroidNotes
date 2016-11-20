# Android process death 

Your Android application (process) can be killed at any time if it’s in paused or stopped state. 
The state of your Activities, Fragments and Views will be saved. 
When your return back to the application — the system will start the process again,
recreate the top activity (the Activities in the back-stack will be recreated on demand when you go back) 
and you will get a Bundle with the stored state.

And here lies the issue some developers don’t fully realise — the whole process was killed. 
So any Singletons (or any “application scope” objects), any temporary data, 
any data stored in your “retained Fragments” — everything will be in a state as if you just launched the application.
With one big difference — the state is restored, the user is at the point where he left the app.

Imagine, in your Activity you are depending on some shared Object, 
or some injected dependency where you keep recent data. 
Most likely the application will just crash on a NullPointerException because you didn’t expect the data to be in null.

### How to test the application background kill & restore?

- Launch your application, open some new Activity, do some work.
- Hit the Home button (application will be in the background, in stopped state).
- Kill the Application — easiest way is to just click the red “stop” button in Android Studio.
- Return back to your application (launch from Recent apps).
- Crash? You are doing something wrong in your application

![CrashAPP](https://github.com/RogerGold/media/blob/master/AS_CrashAPP.PNG)

### Troublemakers in your app
- Singletons
- Any other shared instances that keep mutable data (such as injected dependencies where you keep some state)
- Data and State stored in your Application class
- Mutable static fields
- Retained fragments (state is restored, data is lost)
- Basically anything which is not stored in onSaveInstanceState and you depend on it

There is no single solution and it depends on the type of your application.
Generally you should try to stay away from anything mentioned in the list, but that’s not always easy or possible.

You should be able to “reinitialise” the state — either load data from a database, 
SharedPreferences or reload anything needed again.

### Know the Android platform rules
Any architecture, framework or library has to play according to the Android platform rules.
So anytime you see a new library or approach — think about if and how is it handling state restoration.

And as always — test your application for these cases.
This particular issue is bad in the way that you will almost never see it during development and simple testing.
But the end users will run into it regularly.
