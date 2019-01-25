package garye.utils.jhy.common;

import android.app.Notification;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

/**
 * Created by hayoung on 26/12/2018.
 * gkduduu@naver.com
 */
public class NotiService extends NotificationListenerService {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.i("jhy", "[jhy] onNotificationPosted() - " + sbn.toString());
        Log.i("jhy", "[jhy] PackageName:" + sbn.getPackageName());
        Log.i("jhy", "[jhy] PostTime:" + sbn.getPostTime());

        Notification notificatin = sbn.getNotification();
        Bundle extras = notificatin.extras;
        String title = extras.getString(Notification.EXTRA_TITLE);
        int smallIconRes = extras.getInt(Notification.EXTRA_SMALL_ICON);
        Bitmap largeIcon = ((Bitmap) extras.getParcelable(Notification.EXTRA_LARGE_ICON));
        CharSequence text = extras.getCharSequence(Notification.EXTRA_TEXT);
        CharSequence subText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT);

        Log.i("jhy", "[jhy] Title:" + title);
        Log.i("jhy", "[jhy] Text:" + text);
        Log.i("jhy", "[jhy] Sub Text:" + subText);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i("jhy", "[jhy] onNotificationRemoved() - " + sbn.toString());
    }

}
