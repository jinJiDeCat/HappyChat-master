package com.zz.zy.happychat.utils;


import android.util.Log;

import com.orhanobut.logger.Logger;
import com.zz.zy.happychat.mvp.model.GiftMessage;

import org.greenrobot.eventbus.EventBus;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;

public class MyReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {

    /**
     * 收到消息的处理。
     * @param message 收到的消息实体。
     * @param left 剩余未拉取消息数目。
     * @return
     */
    @Override
    public boolean onReceived(Message message, int left) {
        //开发者根据自己需求自行处理

        MessageContent messageContent = message.getContent();
        Logger.e("收到消息");
        if (messageContent instanceof TextMessage) {//文本消息
            TextMessage textMessage = (TextMessage) messageContent;
            Logger.e(message.getTargetId());
            Log.e("message", "onReceived-TextMessage:" + textMessage.getContent());
            EventBus.getDefault().post(new GiftMessage(textMessage.getContent()));
        } else {
            Log.e("message", "onReceived-其他消息，自己来判断处理");
        }

        return false;
    }
}