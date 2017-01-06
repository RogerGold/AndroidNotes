# Battery Optimization for Android Apps
Battery Usage Reduction is also an important part of an android development as this optimization will ultimately lead to retain the user, 
as many times the user uninstall the application because of the battery draining issue.

##Tips for improving battery usage in an android application:
- Reduce network calls as much as you can: Cache your data and retrieve it from the cache when required next time.
- Avoid wake lock as much as possible: A wake lock is a mechanism to indicate that your application needs to have the device stay on.
- Use AlarmManager carefully: Wrong use of AlarmManager can easily drain the battery.
- Batch the network calls: You should batch the network calls if possible so that you can prevent the device from waking every second.
- A Different logic for Mobile Data and Wifi: You should write different logic for mobile data and wifi as one logic may be optimized for mobile data and other may be optimized for wifi.
- Check all background processes: You should check all the background processes.
- Use GPS carefully: Do not use it frequently, use it only when actually required, don't forget use timer for GPS.
- Use JobScheduler: This is an API for scheduling various types of jobs against the framework that will be executed in your 
  application’s own process. The framework will be intelligent about when you receive your callbacks and attempt to batch 
  and defer them as much as possible. For backward compatibility use [Evernote’s android-job library](https://github.com/evernote/android-job).

## Which tool should I use to check the battery usage of android app?
### Battery Historian
[Battery Historian](https://github.com/google/battery-historian) is a tool that displays information about your phone’s battery usage in HTML form.
You will be able to get the following from the Battery Historian:
- CPU running time.
- Screen up time.
- Mobile radio status.
= Data connection usage.
- Doze mode status.
- Charging status.
- Wifi running time.
- Change in signal strength.
- Detect wake lock.
and much more.

This tool to inspect battery related information and events on an Android device running Android 5.0 Lollipop (API level 21) and later, 
while the device was not plugged in. It allows application developers to visualize system and application level events on a timeline with 
panning and zooming functionality, easily sees various aggregated statistics since the device was last fully charged, 
and select an application and inspect the metrics that impact battery specific to the chosen application. 
It also allows an A/B comparison of two bug-reports, highlighting differences in key battery related metrics.
