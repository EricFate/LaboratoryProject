package hl.iss.whu.edu.laboratoryproject.listener;

import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hl.iss.whu.edu.laboratoryproject.entity.MessageRecord;
import hl.iss.whu.edu.laboratoryproject.service.ChatMessageRecordService;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Message;

/**
 * Created by fate on 2017/2/25.
 */

public class RecordSendMessageListener implements RongIM.OnSendMessageListener {
    private ChatMessageRecordService mService;

    public RecordSendMessageListener(ChatMessageRecordService service) {
        mService = service;
        Log.i("RecordSend", "RecordSendMessageListener: "+mService);
    }
    @Override
    public Message onSend(Message message) {
        return message;
    }

    @Override
    public boolean onSent(Message message, RongIM.SentMessageErrorCode code) {
        if (message.getSentStatus() == Message.SentStatus.SENT) {
            String from = message.getSenderUserId();
            String to = message.getTargetId();
            record(from,to);
        }
        return false;
    }

    public void record(String from, String to) {
        mService.record(from,to);
    }
}
