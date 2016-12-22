# StrictMode for Runtime Analysis on Android
As StrictMode works on the thread or VM level, it is at a much lower level than most runtime analysis can be. 
This has its pros and cons though — it is certainly extremely thorough, but the penalties available are hardcoded and inflexible. 
Alas, there are no custom handlers for StrictMode in the current API.

But don’t take that inflexibility as a death knell — just consider carefully what StrictMode checks you do use and make sure you 
enable them only on debug builds — the BuildConfig.DEBUG flag is helpful for those cases:

    if (BuildConfig.DEBUG) {
      // Enable StrictMode
      StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
        .detectFileUriExposure()
        .detectCleartextNetwork()
        .penaltyLog()
        .penaltyDeath()
        .build());
    }

ref [youtube](https://www.youtube.com/watch?v=oGrXdxpWgyY?utm_campaign=android_series_strictmode_012116&utm_source=medium&utm_medium=blog)
