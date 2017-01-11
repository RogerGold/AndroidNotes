# android开发中的坑

1. 服务在其托管进程的主线程中运行
注意：服务在其托管进程的主线程中运行，它既不创建自己的线程，也不在单独的进程中运行（除非另行指定）。 
这意味着，如果服务将执行任何 CPU 密集型工作或阻止性操作（例如 MP3 播放或联网），则应在服务内创建新线程来完成这项工作。
通过使用单独的线程，可以降低发生“应用无响应”(ANR) 错误的风险，而应用的主线程仍可继续专注于运行用户与 Activity 之间的交互。

2.显式 Intent 启动或绑定 Service
为了确保应用的安全性，请始终使用显式 Intent 启动或绑定 Service，且不要为服务声明 Intent 过滤器。
还可以通过添加 android:exported 属性并将其设置为 "false"，确保服务仅适用于您的应用。这可以有效阻止其他应用启动您的服务，即便在使用显式 Intent 时也如此。

3. 前台服务
前台服务被认为是用户主动意识到的一种服务，因此在内存不足时，系统也不会考虑将其终止。 
前台服务必须为状态栏提供通知，放在“正在进行”标题下方，这意味着除非服务停止或从前台移除，否则不能清除通知。

    Notification notification = new Notification(R.drawable.icon, getText(R.string.ticker_text),
            System.currentTimeMillis());
    Intent notificationIntent = new Intent(this, ExampleActivity.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
    notification.setLatestEventInfo(this, getText(R.string.notification_title),
            getText(R.string.notification_message), pendingIntent);
    startForeground(ONGOING_NOTIFICATION_ID, notification);
 
注意：提供给 startForeground() 的整型 ID 不得为 0。
要从前台移除服务，请调用 stopForeground()。此方法采用一个布尔值，指示是否也移除状态栏通知。
此方法不会停止服务。 但是，如果您在服务正在前台运行时将其停止，则通知也会被移除。
