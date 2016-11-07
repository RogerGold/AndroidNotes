
It depends on how your created your notification.

### 1. use mNotificationManager.notify(MY_NOTIFICATION_ID, notification) show a notification.

    public static void cancelNotification(Context ctx, int notifyId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        nMgr.cancel(notifyId);
    }

 NOTE: notifyId can't be 0.
 use mNotificationManager.notify to show it, Just make sure you use the same id to cancel your notification as you used when you created your notification. 
 To cancel: mNotificationManager.cancel(MY_NOTIFICATION_ID); 
 
### 2.If you are generating Notification from a Service that is started in the foreground using

      startForeground(NOTIFICATION_ID, notificationBuilder.build());
      
Then issuing

    notificationManager.cancel(NOTIFICATION_ID);
    
does't work canceling the Notification & notification still appears in the status bar. In this particular case, you will solve these by 2 ways:

1> Using stopForeground( false ) inside service:

    stopForeground( false );
    notificationManager.cancel(NOTIFICATION_ID);
2> Destroy that service class with calling activity:

    Intent i = new Intent(context, Service.class);
    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    if(ServiceCallingActivity.activity != null) {
        ServiceCallingActivity.activity.finish();
    }
    context.stopService(i);
    
Second way prefer in music player notification more because thay way not only notification remove but remove player also.
