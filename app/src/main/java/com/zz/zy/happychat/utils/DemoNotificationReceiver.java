package com.zz.zy.happychat.utils;

import android.content.Context;
import android.content.Intent;


import com.orhanobut.logger.Logger;
import com.zz.zy.happychat.activity.MainActivity;
import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;


public class DemoNotificationReceiver extends PushMessageReceiver {
    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage pushNotificationMessage) {
        //RongPushClient.sendNotification(context,pushNotificationMessage);
        Logger.e(pushNotificationMessage.getPushContent());
        if(pushNotificationMessage.getPushContent().contains("语音")){
            Logger.e("-------");
            Intent intent=new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        return true;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage pushNotificationMessage) {
//        RongPushClient.clearAllPushNotifications(context);
//        context.startActivity(new Intent(context,MainActivity.class));
        return false;
    }
}
